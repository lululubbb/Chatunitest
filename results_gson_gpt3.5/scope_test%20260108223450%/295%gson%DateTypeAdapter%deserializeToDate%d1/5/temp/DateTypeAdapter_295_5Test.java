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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.internal.bind.util.ISO8601Utils;

public class DateTypeAdapter_295_5Test {

    private DateTypeAdapter dateTypeAdapter;
    private Method deserializeToDateMethod;

    @BeforeEach
    public void setUp() throws Exception {
        dateTypeAdapter = new DateTypeAdapter();
        deserializeToDateMethod = DateTypeAdapter.class.getDeclaredMethod("deserializeToDate", JsonReader.class);
        deserializeToDateMethod.setAccessible(true);
    }

    private void setDateFormats(List<DateFormat> dateFormats) throws Exception {
        Field dateFormatsField = DateTypeAdapter.class.getDeclaredField("dateFormats");
        dateFormatsField.setAccessible(true);
        dateFormatsField.set(dateTypeAdapter, dateFormats);
    }

    @Test
    @Timeout(8000)
    public void testDeserializeToDate_withDateFormatParsingSuccess() throws Exception {
        JsonReader jsonReader = mock(JsonReader.class);
        String dateString = "2023-06-01";
        when(jsonReader.nextString()).thenReturn(dateString);

        // Create a DateFormat mock that successfully parses the date string
        DateFormat dateFormat = mock(DateFormat.class);
        Date expectedDate = new Date();
        when(dateFormat.parse(dateString)).thenReturn(expectedDate);

        List<DateFormat> dateFormats = new ArrayList<>();
        dateFormats.add(dateFormat);
        setDateFormats(dateFormats);

        Date result = (Date) deserializeToDateMethod.invoke(dateTypeAdapter, jsonReader);

        assertSame(expectedDate, result);
        verify(jsonReader).nextString();
        verify(dateFormat).parse(dateString);
    }

    @Test
    @Timeout(8000)
    public void testDeserializeToDate_withDateFormatParsingFailure_thenISO8601Success() throws Exception {
        JsonReader jsonReader = mock(JsonReader.class);
        String dateString = "2023-06-01T12:00:00Z";
        when(jsonReader.nextString()).thenReturn(dateString);

        // Create a DateFormat mock that throws ParseException
        DateFormat dateFormat = mock(DateFormat.class);
        when(dateFormat.parse(dateString)).thenThrow(ParseException.class);

        List<DateFormat> dateFormats = new ArrayList<>();
        dateFormats.add(dateFormat);
        setDateFormats(dateFormats);

        Date expectedDate = new Date();

        try (MockedStatic<ISO8601Utils> mockedISO8601Utils = Mockito.mockStatic(ISO8601Utils.class)) {
            mockedISO8601Utils.when(() -> ISO8601Utils.parse(eq(dateString), any(ParsePosition.class))).thenReturn(expectedDate);

            Date result = (Date) deserializeToDateMethod.invoke(dateTypeAdapter, jsonReader);

            assertSame(expectedDate, result);
            verify(jsonReader).nextString();
            verify(dateFormat).parse(dateString);
            mockedISO8601Utils.verify(() -> ISO8601Utils.parse(eq(dateString), any(ParsePosition.class)));
        }
    }

    @Test
    @Timeout(8000)
    public void testDeserializeToDate_withAllParsingFailures_throwsJsonSyntaxException() throws Exception {
        JsonReader jsonReader = mock(JsonReader.class);
        String dateString = "invalid-date";
        when(jsonReader.nextString()).thenReturn(dateString);
        when(jsonReader.getPreviousPath()).thenReturn("$.date");

        // DateFormat throws ParseException
        DateFormat dateFormat = mock(DateFormat.class);
        when(dateFormat.parse(dateString)).thenThrow(ParseException.class);

        List<DateFormat> dateFormats = new ArrayList<>();
        dateFormats.add(dateFormat);
        setDateFormats(dateFormats);

        try (MockedStatic<ISO8601Utils> mockedISO8601Utils = Mockito.mockStatic(ISO8601Utils.class)) {
            mockedISO8601Utils.when(() -> ISO8601Utils.parse(eq(dateString), any(ParsePosition.class)))
                    .thenThrow(new ParseException("error", 0));

            Exception exception = assertThrows(Exception.class, () -> {
                deserializeToDateMethod.invoke(dateTypeAdapter, jsonReader);
            });

            // Because invoke wraps exception in InvocationTargetException, we unwrap it
            Throwable cause = exception.getCause();
            assertTrue(cause instanceof JsonSyntaxException);
            assertTrue(cause.getMessage().contains("Failed parsing"));
            assertTrue(cause.getMessage().contains(dateString));
            assertTrue(cause.getMessage().contains("$.date"));

            verify(jsonReader).nextString();
            verify(dateFormat).parse(dateString);
            mockedISO8601Utils.verify(() -> ISO8601Utils.parse(eq(dateString), any(ParsePosition.class)));
            verify(jsonReader).getPreviousPath();
        }
    }
}