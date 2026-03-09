package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.JavaVersion;
import com.google.gson.internal.PreJava9DateFormatProvider;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.bind.DefaultDateTypeAdapter;
import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

class DefaultDateTypeAdapterDeserializeToDateTest {

    private DefaultDateTypeAdapter<Date> adapter;
    private JsonReader jsonReaderMock;

    // A minimal implementation of DateType to satisfy constructor
    private static class DummyDateType implements DefaultDateTypeAdapter.DateType<Date> {
        @Override
        public Date cast(Object value) {
            return (Date) value;
        }

        @Override
        public Class<Date> getDateClass() {
            return Date.class;
        }
    }

    @BeforeEach
    public void setUp() throws Exception {
        // Create a dummy DateType to satisfy constructor parameter
        DefaultDateTypeAdapter.DateType<Date> dateTypeDummy = new DummyDateType();

        // Use reflection to access private constructor:
        var constructors = DefaultDateTypeAdapter.class.getDeclaredConstructors();
        var ctor = constructors[0];
        ctor.setAccessible(true);

        // Create instance with dummy dateType and datePattern
        adapter = (DefaultDateTypeAdapter<Date>) ctor.newInstance(dateTypeDummy, "yyyy-MM-dd");

        // Inject a dateFormat into dateFormats list to test parsing
        Field dateFormatsField = DefaultDateTypeAdapter.class.getDeclaredField("dateFormats");
        dateFormatsField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<DateFormat> dateFormats = (List<DateFormat>) dateFormatsField.get(adapter);
        dateFormats.clear();
        dateFormats.add(new SimpleDateFormat("yyyy-MM-dd", Locale.US));

        // Mock JsonReader
        jsonReaderMock = mock(JsonReader.class);
    }

    @Test
    @Timeout(8000)
    public void testDeserializeToDate_successfulParseWithDateFormats() throws Exception {
        String dateStr = "2023-06-15";
        when(jsonReaderMock.nextString()).thenReturn(dateStr);

        Method method = DefaultDateTypeAdapter.class.getDeclaredMethod("deserializeToDate", JsonReader.class);
        method.setAccessible(true);

        Date result = (Date) method.invoke(adapter, jsonReaderMock);

        assertNotNull(result);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        assertEquals(sdf.parse(dateStr), result);
    }

    @Test
    @Timeout(8000)
    public void testDeserializeToDate_successfulParseWithISO8601Utils() throws Exception {
        // Clear dateFormats so that parsing with dateFormats fails
        Field dateFormatsField = DefaultDateTypeAdapter.class.getDeclaredField("dateFormats");
        dateFormatsField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<DateFormat> dateFormats = (List<DateFormat>) dateFormatsField.get(adapter);
        dateFormats.clear();

        // Provide an ISO8601 string
        String iso8601Str = "2023-06-15T12:34:56Z";
        when(jsonReaderMock.nextString()).thenReturn(iso8601Str);

        Method method = DefaultDateTypeAdapter.class.getDeclaredMethod("deserializeToDate", JsonReader.class);
        method.setAccessible(true);

        Date result = (Date) method.invoke(adapter, jsonReaderMock);

        assertNotNull(result);

        // We expect the date to be parsed to the given ISO8601 date-time
        // We can verify by formatting back or checking year/month/day
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX", Locale.US);
        Date expectedDate = isoFormat.parse(iso8601Str);
        assertEquals(expectedDate, result);
    }

    @Test
    @Timeout(8000)
    public void testDeserializeToDate_parseExceptionThrowsJsonSyntaxException() throws Exception {
        // Clear dateFormats so that parsing with dateFormats fails
        Field dateFormatsField = DefaultDateTypeAdapter.class.getDeclaredField("dateFormats");
        dateFormatsField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<DateFormat> dateFormats = (List<DateFormat>) dateFormatsField.get(adapter);
        dateFormats.clear();

        String invalidDateStr = "invalid-date-string";
        when(jsonReaderMock.nextString()).thenReturn(invalidDateStr);
        when(jsonReaderMock.getPreviousPath()).thenReturn("$.date");

        Method method = DefaultDateTypeAdapter.class.getDeclaredMethod("deserializeToDate", JsonReader.class);
        method.setAccessible(true);

        JsonSyntaxException thrown = assertThrows(JsonSyntaxException.class, () -> {
            try {
                method.invoke(adapter, jsonReaderMock);
            } catch (java.lang.reflect.InvocationTargetException e) {
                // unwrap and throw the cause to be caught by assertThrows
                throw e.getCause();
            }
        });

        assertTrue(thrown.getMessage().contains("Failed parsing"));
        assertTrue(thrown.getMessage().contains(invalidDateStr));
        assertTrue(thrown.getMessage().contains("$.date"));
    }
}