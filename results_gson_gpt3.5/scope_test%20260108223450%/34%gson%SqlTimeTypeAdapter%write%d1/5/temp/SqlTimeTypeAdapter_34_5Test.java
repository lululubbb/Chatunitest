package com.google.gson.internal.sql;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.mockito.Mockito.*;

import com.google.gson.internal.sql.SqlTimeTypeAdapter;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.Time;
import java.text.DateFormat;

class SqlTimeTypeAdapter_34_5Test {

    private SqlTimeTypeAdapter adapter;
    private JsonWriter jsonWriter;
    private StringWriter stringWriter;

    @BeforeEach
    void setUp() throws Exception {
        Constructor<SqlTimeTypeAdapter> constructor = SqlTimeTypeAdapter.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        adapter = constructor.newInstance();

        stringWriter = new StringWriter();
        jsonWriter = new JsonWriter(stringWriter);
    }

    @Test
    @Timeout(8000)
    void write_nullValue_shouldCallNullValueOnJsonWriter() throws IOException {
        adapter.write(jsonWriter, null);
        String jsonOutput = stringWriter.toString();
        // jsonWriter.nullValue() writes "null"
        assert jsonOutput.equals("null");
    }

    @Test
    @Timeout(8000)
    void write_nonNullValue_shouldWriteFormattedTime() throws Exception {
        // Use reflection to access the private final DateFormat field 'format'
        Field formatField = SqlTimeTypeAdapter.class.getDeclaredField("format");
        formatField.setAccessible(true);
        DateFormat format = (DateFormat) formatField.get(adapter);

        // Prepare a Time object for 15:30:45 (3:30:45 PM)
        Time time = Time.valueOf("15:30:45");

        // Format expected string using the same formatter under synchronization
        String expectedTimeString;
        synchronized (adapter) {
            expectedTimeString = format.format(time);
        }

        adapter.write(jsonWriter, time);
        String jsonOutput = stringWriter.toString();

        // JsonWriter.value(String) writes the string quoted, so output is quoted string
        // e.g. "03:30:45 PM"
        String expectedJson = "\"" + expectedTimeString + "\"";
        assert jsonOutput.equals(expectedJson);
    }

    @Test
    @Timeout(8000)
    void write_threadSafety_synchronizedFormatInvocation() throws Exception {
        Constructor<SqlTimeTypeAdapter> constructor = SqlTimeTypeAdapter.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        SqlTimeTypeAdapter spyAdapter = Mockito.spy(constructor.newInstance());

        stringWriter = new StringWriter();
        jsonWriter = new JsonWriter(stringWriter);

        Time time = Time.valueOf("10:15:30");

        spyAdapter.write(jsonWriter, time);

        // Verify write was called once with jsonWriter and time
        verify(spyAdapter, times(1)).write(any(JsonWriter.class), eq(time));

        Field formatField = SqlTimeTypeAdapter.class.getDeclaredField("format");
        formatField.setAccessible(true);
        DateFormat format = (DateFormat) formatField.get(spyAdapter);

        String output = stringWriter.toString();
        synchronized (spyAdapter) {
            String expected = "\"" + format.format(time) + "\"";
            assert output.equals(expected);
        }
    }
}