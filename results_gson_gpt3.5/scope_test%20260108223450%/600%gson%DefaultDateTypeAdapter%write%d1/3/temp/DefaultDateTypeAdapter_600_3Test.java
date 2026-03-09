package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.JavaVersion;
import com.google.gson.internal.PreJava9DateFormatProvider;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

class DefaultDateTypeAdapterWriteTest {

    private DefaultDateTypeAdapter<Date> adapter;
    private JsonWriter jsonWriter;
    private StringWriter stringWriter;

    // A minimal DateType implementation for testing
    interface DateType<T extends Date> {
        T fromMillis(long millis);
    }

    private static class TestDateType implements DateType<Date> {
        @Override
        public Date fromMillis(long millis) {
            return new Date(millis);
        }
    }

    @BeforeEach
    void setUp() throws Exception {
        // Create a real DateFormat instance to be used inside the adapter
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);

        // Create the adapter instance via reflection because constructors are private
        // We use the constructor with DateType and String pattern
        Constructor<DefaultDateTypeAdapter> constructor = DefaultDateTypeAdapter.class.getDeclaredConstructor(DefaultDateTypeAdapter.DateType.class, String.class);
        constructor.setAccessible(true);
        adapter = constructor.newInstance(new TestDateType(), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        // Inject the dateFormats list with a single DateFormat instance for thread safety
        Field dateFormatsField = DefaultDateTypeAdapter.class.getDeclaredField("dateFormats");
        dateFormatsField.setAccessible(true);
        List<DateFormat> dateFormats = (List<DateFormat>) dateFormatsField.get(adapter);
        dateFormats.clear();
        dateFormats.add(df);

        // Prepare JsonWriter wrapping a StringWriter to capture output
        stringWriter = new StringWriter();
        JsonWriter realJsonWriter = new JsonWriter(stringWriter);
        jsonWriter = spy(realJsonWriter);
    }

    @Test
    @Timeout(8000)
    void write_nullValue_callsNullValueOnJsonWriter() throws IOException {
        adapter.write(jsonWriter, null);

        verify(jsonWriter).nullValue();
        assertEquals("", stringWriter.toString());
    }

    @Test
    @Timeout(8000)
    void write_nonNullValue_formatsDateAndWritesString() throws IOException {
        Date date = new Date(0L); // Epoch

        adapter.write(jsonWriter, date);

        // Capture the string written to JsonWriter
        String output = stringWriter.toString();

        // The output should be the formatted date string
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        String expected = df.format(date);

        // JsonWriter.value(String) writes the string surrounded by quotes
        assertEquals("\"" + expected + "\"", output);

        // Verify JsonWriter.value was called with the formatted string
        verify(jsonWriter).value(expected);
    }

    @Test
    @Timeout(8000)
    void write_threadSafety_synchronizesOnDateFormats() throws Exception {
        Date date = new Date(0L);

        // Spy on the dateFormats list inside adapter to verify usage
        Field dateFormatsField = DefaultDateTypeAdapter.class.getDeclaredField("dateFormats");
        dateFormatsField.setAccessible(true);
        List<DateFormat> dateFormats = (List<DateFormat>) dateFormatsField.get(adapter);

        List<DateFormat> spyDateFormats = spy(new ArrayList<>(dateFormats));
        dateFormatsField.set(adapter, spyDateFormats);

        // Call the method
        adapter.write(jsonWriter, date);

        // Verify that get(0) was called on the dateFormats list
        verify(spyDateFormats).get(0);

        // We cannot directly verify synchronization but this ensures use of dateFormats list
        String output = stringWriter.toString();
        assertNotNull(output);
    }
}