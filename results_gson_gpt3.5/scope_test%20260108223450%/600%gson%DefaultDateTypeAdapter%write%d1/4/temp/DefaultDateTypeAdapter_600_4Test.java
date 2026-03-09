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

    @BeforeEach
    void setUp() throws Exception {
        @SuppressWarnings("unchecked")
        Class<DefaultDateTypeAdapter<Date>> clazz = (Class<DefaultDateTypeAdapter<Date>>) (Class<?>) DefaultDateTypeAdapter.class;
        Constructor<DefaultDateTypeAdapter<Date>> constructor = null;
        for (Constructor<?> c : clazz.getDeclaredConstructors()) {
            if (c.getParameterCount() == 2) {
                @SuppressWarnings("unchecked")
                Constructor<DefaultDateTypeAdapter<Date>> cons = (Constructor<DefaultDateTypeAdapter<Date>>) c;
                constructor = cons;
                break;
            }
        }
        assertNotNull(constructor, "Constructor with 2 parameters not found");
        constructor.setAccessible(true);

        // Pass null for DateType<T> and the date pattern string for the constructor
        adapter = constructor.newInstance(null, "yyyy-MM-dd'T'HH:mm:ss'Z'");

        // Inject a dateFormats list with a SimpleDateFormat instance at index 0
        Field dateFormatsField = clazz.getDeclaredField("dateFormats");
        dateFormatsField.setAccessible(true);
        List<DateFormat> dateFormats = new ArrayList<>();
        dateFormats.add(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US));
        dateFormatsField.set(adapter, dateFormats);

        // Prepare JsonWriter with a StringWriter to capture output
        stringWriter = new StringWriter();
        JsonWriter realJsonWriter = new JsonWriter(stringWriter);
        jsonWriter = spy(realJsonWriter);
    }

    @Test
    @Timeout(8000)
    void testWrite_NullValue_CallsNullValueOnJsonWriter() throws IOException {
        adapter.write(jsonWriter, null);
        verify(jsonWriter).nullValue();
        assertEquals("", stringWriter.toString());
    }

    @Test
    @Timeout(8000)
    void testWrite_FormatsDateAndWritesValue() throws IOException {
        Date date = new Date(0L); // Epoch time

        adapter.write(jsonWriter, date);

        // Capture the formatted string from the underlying StringWriter
        String jsonOutput = stringWriter.toString();

        // The output should contain the formatted date string as JSON string (quoted)
        // JsonWriter.value(String) writes quoted string, so output contains quotes
        assertTrue(jsonOutput.startsWith("\""));
        assertTrue(jsonOutput.endsWith("\""));

        // Extract the inner string without quotes
        String formattedDate = jsonOutput.substring(1, jsonOutput.length() - 1);

        // The formatted date string should match the SimpleDateFormat pattern
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        String expected = df.format(date);
        assertEquals(expected, formattedDate);

        // Verify that JsonWriter.value was called with the formatted string
        verify(jsonWriter).value(expected);
    }

    @Test
    @Timeout(8000)
    void testWrite_SynchronizationOnDateFormats() throws Exception {
        // We want to verify synchronization on dateFormats list during formatting

        // Use reflection to get the dateFormats list object
        Field dateFormatsField = DefaultDateTypeAdapter.class.getDeclaredField("dateFormats");
        dateFormatsField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<DateFormat> dateFormats = (List<DateFormat>) dateFormatsField.get(adapter);

        Date date = new Date(0L);

        // Spy on the first DateFormat in the list to verify format call
        DateFormat spyDateFormat = spy(dateFormats.get(0));
        List<DateFormat> newList = new ArrayList<>();
        newList.add(spyDateFormat);
        dateFormatsField.set(adapter, newList);

        // Clear stringWriter before test
        stringWriter.getBuffer().setLength(0);

        // Call write method
        adapter.write(jsonWriter, date);

        // Verify that format method was called on the spied DateFormat
        verify(spyDateFormat).format(date);

        // The synchronization block cannot be directly verified, but we can check no exceptions and correct output
        String jsonOutput = stringWriter.toString();
        assertTrue(jsonOutput.startsWith("\""));
        assertTrue(jsonOutput.endsWith("\""));
    }
}