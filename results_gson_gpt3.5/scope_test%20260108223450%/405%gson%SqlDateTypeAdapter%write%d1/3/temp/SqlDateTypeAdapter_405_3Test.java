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

import com.google.gson.internal.sql.SqlDateTypeAdapter;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.text.DateFormat;

class SqlDateTypeAdapter_405_3Test {

    private SqlDateTypeAdapter adapter;
    private JsonWriter jsonWriter;

    @BeforeEach
    void setUp() throws Exception {
        Constructor<SqlDateTypeAdapter> constructor = SqlDateTypeAdapter.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        adapter = constructor.newInstance();
        jsonWriter = mock(JsonWriter.class);
    }

    @Test
    @Timeout(8000)
    void testWrite_NullValue_CallsNullValue() throws IOException {
        adapter.write(jsonWriter, null);
        verify(jsonWriter).nullValue();
        verifyNoMoreInteractions(jsonWriter);
    }

    @Test
    @Timeout(8000)
    void testWrite_ValidDate_WritesFormattedDate() throws Exception {
        // Use fully qualified name to avoid ambiguity
        java.sql.Date date = java.sql.Date.valueOf("2023-04-26"); // yyyy-MM-dd format for java.sql.Date

        // Access the private final DateFormat field "format"
        Field formatField = SqlDateTypeAdapter.class.getDeclaredField("format");
        formatField.setAccessible(true);
        DateFormat dateFormat = (DateFormat) formatField.get(adapter);

        // Format the date string using the same format to verify output
        String expectedDateString;
        synchronized (adapter) {
            expectedDateString = dateFormat.format(date);
        }

        // Call the write method
        adapter.write(jsonWriter, date);

        // Verify the correct sequence: no nullValue call, value called with formatted string
        InOrder inOrder = inOrder(jsonWriter);
        inOrder.verify(jsonWriter).value(expectedDateString);
        verifyNoMoreInteractions(jsonWriter);
    }
}