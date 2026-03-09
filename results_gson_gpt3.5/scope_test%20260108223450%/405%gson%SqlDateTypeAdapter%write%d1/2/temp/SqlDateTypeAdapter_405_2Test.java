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
import java.util.Date;

import static org.mockito.Mockito.*;

import com.google.gson.internal.sql.SqlDateTypeAdapter;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

class SqlDateTypeAdapter_405_2Test {

    private SqlDateTypeAdapter adapter;
    private JsonWriter jsonWriter;
    private StringWriter stringWriter;

    @BeforeEach
    void setUp() throws Exception {
        // Use reflection to instantiate the private constructor
        Constructor<SqlDateTypeAdapter> constructor = SqlDateTypeAdapter.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        adapter = constructor.newInstance();

        stringWriter = new StringWriter();
        jsonWriter = new JsonWriter(stringWriter);
    }

    @Test
    @Timeout(8000)
    void write_nullValue_callsNullValueOnJsonWriter() throws IOException {
        adapter.write(jsonWriter, null);
        String output = stringWriter.toString();
        // JsonWriter nullValue() writes "null" to output
        // So we verify the output string is "null"
        assert output.equals("null");
    }

    @Test
    @Timeout(8000)
    void write_nonNullValue_writesFormattedDate() throws Exception {
        // Prepare a java.sql.Date
        java.sql.Date date = java.sql.Date.valueOf("2024-06-15"); // 15 June 2024

        // Use reflection to get the private DateFormat instance and its pattern
        Field formatField = SqlDateTypeAdapter.class.getDeclaredField("format");
        formatField.setAccessible(true);
        DateFormat dateFormat = (DateFormat) formatField.get(adapter);

        // Format date string via the adapter's format instance to match expected output
        String expectedFormattedDate;
        synchronized (adapter) {
            expectedFormattedDate = dateFormat.format(date);
        }

        // Write date to jsonWriter
        adapter.write(jsonWriter, date);

        // The output should be the formatted date string wrapped in quotes
        String output = stringWriter.toString();

        // JsonWriter.value(String) writes the string wrapped in quotes, so output should be quoted
        String expectedOutput = "\"" + expectedFormattedDate + "\"";

        assert output.equals(expectedOutput);
    }

    @Test
    @Timeout(8000)
    void write_threadSafety_synchronizedFormat() throws Exception {
        java.sql.Date date = java.sql.Date.valueOf("2024-06-15");

        // Use reflection to get the private DateFormat instance
        Field formatField = SqlDateTypeAdapter.class.getDeclaredField("format");
        formatField.setAccessible(true);

        // Create a custom DateFormat subclass that tracks calls
        class TrackingDateFormat extends SimpleDateFormat {
            boolean called = false;

            TrackingDateFormat(String pattern) {
                super(pattern);
            }

            @Override
            public StringBuffer format(java.util.Date date, StringBuffer toAppendTo, java.text.FieldPosition pos) {
                called = true;
                return super.format(date, toAppendTo, pos);
            }
        }

        TrackingDateFormat trackingFormat = new TrackingDateFormat("MMM d, yyyy");
        formatField.set(adapter, trackingFormat);

        adapter.write(jsonWriter, date);

        // The format method should have been called (inside synchronized)
        assert trackingFormat.called;

        String output = stringWriter.toString();
        assert output != null && output.length() > 0;
    }
}