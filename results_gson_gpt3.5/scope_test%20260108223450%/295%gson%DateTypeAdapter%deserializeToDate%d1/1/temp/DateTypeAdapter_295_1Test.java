package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.JavaVersion;
import com.google.gson.internal.PreJava9DateFormatProvider;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.text.ParsePosition;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DateTypeAdapter_295_1Test {

    private DateTypeAdapter dateTypeAdapter;
    private Method deserializeToDateMethod;

    @BeforeEach
    public void setUp() throws Exception {
        dateTypeAdapter = new DateTypeAdapter();
        deserializeToDateMethod = DateTypeAdapter.class.getDeclaredMethod("deserializeToDate", JsonReader.class);
        deserializeToDateMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void testDeserializeToDate_successfulParseFromDateFormat() throws Exception {
        // Prepare mock JsonReader
        JsonReader jsonReader = mock(JsonReader.class);
        String dateString = "2023-06-01";
        when(jsonReader.nextString()).thenReturn(dateString);

        // Setup a DateFormat that successfully parses the string
        DateFormat successfulDateFormat = mock(DateFormat.class);
        Date expectedDate = new Date();
        when(successfulDateFormat.parse(dateString)).thenReturn(expectedDate);

        // Inject the mocked DateFormat into dateFormats list
        Field dateFormatsField = DateTypeAdapter.class.getDeclaredField("dateFormats");
        dateFormatsField.setAccessible(true);
        List<DateFormat> dateFormats = new ArrayList<>();
        dateFormats.add(successfulDateFormat);
        dateFormatsField.set(dateTypeAdapter, dateFormats);

        // Invoke private method
        Date actualDate = (Date) deserializeToDateMethod.invoke(dateTypeAdapter, jsonReader);

        // Verify
        assertSame(expectedDate, actualDate);
        verify(successfulDateFormat, times(1)).parse(dateString);
        verify(jsonReader, times(1)).nextString();
    }

    @Test
    @Timeout(8000)
    public void testDeserializeToDate_allDateFormatsFail_ISO8601Success() throws Exception {
        JsonReader jsonReader = mock(JsonReader.class);
        String dateString = "2023-06-01T12:34:56Z";
        when(jsonReader.nextString()).thenReturn(dateString);

        // Setup DateFormats to throw ParseException
        DateFormat failingDateFormat = mock(DateFormat.class);
        when(failingDateFormat.parse(dateString)).thenThrow(new ParseException("fail", 0));

        Field dateFormatsField = DateTypeAdapter.class.getDeclaredField("dateFormats");
        dateFormatsField.setAccessible(true);
        List<DateFormat> dateFormats = new ArrayList<>();
        dateFormats.add(failingDateFormat);
        dateFormatsField.set(dateTypeAdapter, dateFormats);

        // We cannot mock ISO8601Utils.parse as it's static final method, so we test with a valid ISO8601 string

        Date date = (Date) deserializeToDateMethod.invoke(dateTypeAdapter, jsonReader);
        assertNotNull(date);

        verify(failingDateFormat, times(1)).parse(dateString);
        verify(jsonReader, times(1)).nextString();
    }

    @Test
    @Timeout(8000)
    public void testDeserializeToDate_allDateFormatsFail_ISO8601Fails_throwsJsonSyntaxException() throws Exception {
        JsonReader jsonReader = mock(JsonReader.class);
        String dateString = "invalid-date";
        when(jsonReader.nextString()).thenReturn(dateString);
        when(jsonReader.getPreviousPath()).thenReturn("$.date");

        // Setup DateFormats to throw ParseException
        DateFormat failingDateFormat = mock(DateFormat.class);
        when(failingDateFormat.parse(dateString)).thenThrow(new ParseException("fail", 0));

        Field dateFormatsField = DateTypeAdapter.class.getDeclaredField("dateFormats");
        dateFormatsField.setAccessible(true);
        List<DateFormat> dateFormats = new ArrayList<>();
        dateFormats.add(failingDateFormat);
        dateFormatsField.set(dateTypeAdapter, dateFormats);

        // Since ISO8601Utils.parse throws ParseException on invalid date, expect JsonSyntaxException

        Throwable thrown = assertThrows(Throwable.class, () -> {
            deserializeToDateMethod.invoke(dateTypeAdapter, jsonReader);
        });

        // Unwrap InvocationTargetException to get the actual cause
        Throwable cause = thrown;
        while (cause instanceof java.lang.reflect.InvocationTargetException && cause.getCause() != null) {
            cause = cause.getCause();
        }

        assertNotNull(cause);
        assertTrue(cause instanceof JsonSyntaxException);

        verify(failingDateFormat, times(1)).parse(dateString);
        verify(jsonReader, times(1)).nextString();
        verify(jsonReader, times(1)).getPreviousPath();
    }
}