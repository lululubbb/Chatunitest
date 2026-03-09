package com.google.gson.internal.sql;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;

class SqlDateTypeAdapter_405_5Test {

    private SqlDateTypeAdapter adapter;
    private JsonWriter jsonWriter;
    private StringWriter stringWriter;

    @BeforeEach
    void setUp() throws Exception {
        // Use reflection to access the private constructor
        Constructor<SqlDateTypeAdapter> constructor = SqlDateTypeAdapter.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        adapter = constructor.newInstance();

        stringWriter = new StringWriter();
        jsonWriter = spy(new JsonWriter(stringWriter));
    }

    @Test
    @Timeout(8000)
    void write_nullValue_callsNullValueOnJsonWriter() throws IOException {
        adapter.write(jsonWriter, null);
        verify(jsonWriter).nullValue();
        verify(jsonWriter, never()).value(anyString());
    }

    @Test
    @Timeout(8000)
    void write_nonNullValue_formatsDateAndWritesValue() throws Exception {
        // Use fully qualified name to avoid ambiguity for java.sql.Date
        java.sql.Date date = java.sql.Date.valueOf("2023-06-15");

        // Use reflection to get the private 'format' field
        Field formatField = SqlDateTypeAdapter.class.getDeclaredField("format");
        formatField.setAccessible(true);
        SimpleDateFormat format = (SimpleDateFormat) formatField.get(adapter);

        // Format the date manually to compare
        String expectedFormattedDate;
        synchronized (adapter) {
            expectedFormattedDate = format.format(date);
        }

        adapter.write(jsonWriter, date);

        InOrder inOrder = inOrder(jsonWriter);
        inOrder.verify(jsonWriter).value(expectedFormattedDate);
        inOrder.verifyNoMoreInteractions();
    }
}