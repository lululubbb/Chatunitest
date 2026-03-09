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

import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.text.DateFormat;

class SqlDateTypeAdapter_405_1Test {

    private SqlDateTypeAdapter adapter;
    private JsonWriter jsonWriter;

    @BeforeEach
    void setUp() throws Exception {
        // Use reflection to create instance of SqlDateTypeAdapter since constructor is private
        Constructor<SqlDateTypeAdapter> constructor = SqlDateTypeAdapter.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        adapter = constructor.newInstance();

        // JsonWriter requires a Writer, we can use StringWriter for testing
        jsonWriter = spy(new JsonWriter(new StringWriter()));
    }

    @Test
    @Timeout(8000)
    void write_nullValue_callsNullValue() throws IOException {
        adapter.write(jsonWriter, null);

        verify(jsonWriter).nullValue();
        verify(jsonWriter, never()).value(anyString());
    }

    @Test
    @Timeout(8000)
    void write_nonNullValue_formatsDateAndCallsValue() throws Exception {
        // Prepare a java.sql.Date
        java.sql.Date sqlDate = java.sql.Date.valueOf("2023-06-15");

        // Access the private final DateFormat field 'format' via reflection
        Field formatField = SqlDateTypeAdapter.class.getDeclaredField("format");
        formatField.setAccessible(true);
        DateFormat format = (DateFormat) formatField.get(adapter);

        // Spy on the format to verify synchronized call (optional)
        DateFormat formatSpy = spy(format);
        formatField.set(adapter, formatSpy);

        adapter.write(jsonWriter, sqlDate);

        // Verify that format.format was called with sqlDate
        verify(formatSpy).format(sqlDate);

        // Verify the output string is as expected
        String expectedDateString = format.format(sqlDate);
        verify(jsonWriter).value(expectedDateString);

        // Verify nullValue was not called
        verify(jsonWriter, never()).nullValue();
    }
}