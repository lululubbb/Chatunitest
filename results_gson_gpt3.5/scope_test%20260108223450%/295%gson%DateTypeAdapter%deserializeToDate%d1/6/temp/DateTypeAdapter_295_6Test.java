package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.JavaVersion;
import com.google.gson.internal.PreJava9DateFormatProvider;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.Locale;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.internal.bind.util.ISO8601Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DateTypeAdapter_295_6Test {

    private DateTypeAdapter dateTypeAdapter;
    private Method deserializeToDateMethod;

    @BeforeEach
    public void setUp() throws NoSuchMethodException {
        dateTypeAdapter = new DateTypeAdapter();
        deserializeToDateMethod = DateTypeAdapter.class.getDeclaredMethod("deserializeToDate", JsonReader.class);
        deserializeToDateMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void testDeserializeToDate_successWithDateFormat() throws Throwable {
        JsonReader jsonReader = mock(JsonReader.class);
        String dateString = "2023-06-01";
        when(jsonReader.nextString()).thenReturn(dateString);

        // Create a mock DateFormat that successfully parses the date string
        DateFormat mockDateFormat = mock(DateFormat.class);
        Date expectedDate = new Date();
        when(mockDateFormat.parse(dateString)).thenReturn(expectedDate);

        // Inject the mock DateFormat into the dateFormats list
        List<DateFormat> dateFormats = getDateFormats(dateTypeAdapter);
        dateFormats.add(mockDateFormat);

        Date actualDate = invokeDeserializeToDate(jsonReader);
        assertEquals(expectedDate, actualDate);
        verify(jsonReader).nextString();
        verify(mockDateFormat).parse(dateString);
    }

    @Test
    @Timeout(8000)
    public void testDeserializeToDate_fallbackToISO8601Utils() throws Throwable {
        JsonReader jsonReader = mock(JsonReader.class);
        String dateString = "2023-06-01T12:34:56Z";
        when(jsonReader.nextString()).thenReturn(dateString);

        // Add a DateFormat that always throws ParseException to force fallback
        DateFormat failingDateFormat = mock(DateFormat.class);
        try {
            when(failingDateFormat.parse(dateString)).thenThrow(ParseException.class);
        } catch (ParseException e) {
            // This block will never be reached because Mockito does not throw checked exceptions here
        }
        List<DateFormat> dateFormats = getDateFormats(dateTypeAdapter);
        dateFormats.add(failingDateFormat);

        // Spy ISO8601Utils.parse to return a specific date
        Date expectedDate = new Date();
        // Because ISO8601Utils.parse is static final, we cannot mock it easily without a framework like PowerMock,
        // so we rely on the actual method. The string must be valid ISO8601.

        Date actualDate = invokeDeserializeToDate(jsonReader);
        assertNotNull(actualDate);
        // The actualDate should be close to expectedDate but we cannot assert exact equality
        verify(jsonReader).nextString();
        verify(failingDateFormat).parse(dateString);
    }

    @Test
    @Timeout(8000)
    public void testDeserializeToDate_throwsJsonSyntaxException() throws Throwable {
        JsonReader jsonReader = mock(JsonReader.class);
        String dateString = "invalid-date";
        when(jsonReader.nextString()).thenReturn(dateString);
        when(jsonReader.getPreviousPath()).thenReturn("$.date");

        // Add a DateFormat that always throws ParseException to force fallback
        DateFormat failingDateFormat = mock(DateFormat.class);
        try {
            when(failingDateFormat.parse(dateString)).thenThrow(ParseException.class);
        } catch (ParseException e) {
            // ignored
        }
        List<DateFormat> dateFormats = getDateFormats(dateTypeAdapter);
        dateFormats.add(failingDateFormat);

        // We cannot mock ISO8601Utils.parse easily as it is static final, so we use an invalid date string to cause it to throw

        JsonSyntaxException thrown = assertThrows(JsonSyntaxException.class, () -> {
            invokeDeserializeToDate(jsonReader);
        });
        assertTrue(thrown.getMessage().contains("Failed parsing"));
        assertTrue(thrown.getMessage().contains("$.date"));
        verify(jsonReader).nextString();
        verify(failingDateFormat).parse(dateString);
        verify(jsonReader).getPreviousPath();
    }

    private List<DateFormat> getDateFormats(DateTypeAdapter adapter) throws Exception {
        java.lang.reflect.Field field = DateTypeAdapter.class.getDeclaredField("dateFormats");
        field.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<DateFormat> dateFormats = (List<DateFormat>) field.get(adapter);
        return dateFormats;
    }

    private Date invokeDeserializeToDate(JsonReader jsonReader) throws Throwable {
        try {
            return (Date) deserializeToDateMethod.invoke(dateTypeAdapter, jsonReader);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }
}