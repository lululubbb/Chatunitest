package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.JavaVersion;
import com.google.gson.internal.PreJava9DateFormatProvider;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.bind.DefaultDateTypeAdapter;
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
import java.util.Date;
import java.util.List;

import com.google.gson.internal.bind.util.ISO8601Utils;

class DefaultDateTypeAdapter_deserializeToDate_Test {

    private DefaultDateTypeAdapter<?> adapter;
    private JsonReader jsonReaderMock;

    @BeforeEach
    public void setUp() throws Exception {
        // Create an instance of DefaultDateTypeAdapter with a dummy DateType (null as we don't use it here)
        // Use the constructor with (DateType<T> dateType, int dateStyle, int timeStyle) to avoid null pointer issues
        adapter = (DefaultDateTypeAdapter<?>) DefaultDateTypeAdapter.class
                .getDeclaredConstructor(DefaultDateTypeAdapter.DateType.class, int.class, int.class)
                .newInstance((Object) null, DateFormat.SHORT, DateFormat.SHORT);

        // Clear the dateFormats list to control it fully
        Field dateFormatsField = DefaultDateTypeAdapter.class.getDeclaredField("dateFormats");
        dateFormatsField.setAccessible(true);
        List<DateFormat> dateFormats = (List<DateFormat>) dateFormatsField.get(adapter);
        dateFormats.clear();

        // Create mock JsonReader
        jsonReaderMock = mock(JsonReader.class);
    }

    @Test
    @Timeout(8000)
    void testDeserializeToDate_parsesWithDateFormatSuccessfully() throws Exception {
        String dateString = "2023-06-01";

        // Mock JsonReader.nextString() to return dateString
        when(jsonReaderMock.nextString()).thenReturn(dateString);

        // Add a DateFormat that successfully parses the string
        DateFormat mockDateFormat = mock(DateFormat.class);
        Date expectedDate = new Date();
        when(mockDateFormat.parse(dateString)).thenReturn(expectedDate);

        // Inject this mock DateFormat into dateFormats list
        Field dateFormatsField = DefaultDateTypeAdapter.class.getDeclaredField("dateFormats");
        dateFormatsField.setAccessible(true);
        List<DateFormat> dateFormats = (List<DateFormat>) dateFormatsField.get(adapter);
        dateFormats.clear();
        dateFormats.add(mockDateFormat);

        // Invoke private method
        Method method = DefaultDateTypeAdapter.class.getDeclaredMethod("deserializeToDate", JsonReader.class);
        method.setAccessible(true);
        Date actualDate = (Date) method.invoke(adapter, jsonReaderMock);

        assertSame(expectedDate, actualDate);
        verify(mockDateFormat).parse(dateString);
        verify(jsonReaderMock).nextString();
    }

    @Test
    @Timeout(8000)
    void testDeserializeToDate_fallbackToISO8601Utils() throws Exception {
        String dateString = "2023-06-02T10:15:30Z";

        when(jsonReaderMock.nextString()).thenReturn(dateString);

        // Add a DateFormat that throws ParseException to force fallback
        DateFormat failingDateFormat = mock(DateFormat.class);
        when(failingDateFormat.parse(dateString)).thenThrow(new ParseException("fail", 0));

        Field dateFormatsField = DefaultDateTypeAdapter.class.getDeclaredField("dateFormats");
        dateFormatsField.setAccessible(true);
        List<DateFormat> dateFormats = (List<DateFormat>) dateFormatsField.get(adapter);
        dateFormats.clear();
        dateFormats.add(failingDateFormat);

        // Mock static ISO8601Utils.parse
        try (MockedStatic<ISO8601Utils> isoMock = Mockito.mockStatic(ISO8601Utils.class)) {
            Date expectedDate = new Date();
            isoMock.when(() -> ISO8601Utils.parse(eq(dateString), any(ParsePosition.class))).thenReturn(expectedDate);

            Method method = DefaultDateTypeAdapter.class.getDeclaredMethod("deserializeToDate", JsonReader.class);
            method.setAccessible(true);
            Date actualDate = (Date) method.invoke(adapter, jsonReaderMock);

            assertSame(expectedDate, actualDate);
            verify(failingDateFormat).parse(dateString);
            isoMock.verify(() -> ISO8601Utils.parse(eq(dateString), any(ParsePosition.class)));
        }
    }

    @Test
    @Timeout(8000)
    void testDeserializeToDate_throwsJsonSyntaxExceptionOnInvalidDate() throws Exception {
        String invalidDateString = "invalid-date";

        when(jsonReaderMock.nextString()).thenReturn(invalidDateString);
        when(jsonReaderMock.getPreviousPath()).thenReturn("$.dateField");

        // Add a DateFormat that throws ParseException to force fallback
        DateFormat failingDateFormat = mock(DateFormat.class);
        when(failingDateFormat.parse(invalidDateString)).thenThrow(new ParseException("fail", 0));

        Field dateFormatsField = DefaultDateTypeAdapter.class.getDeclaredField("dateFormats");
        dateFormatsField.setAccessible(true);
        List<DateFormat> dateFormats = (List<DateFormat>) dateFormatsField.get(adapter);
        dateFormats.clear();
        dateFormats.add(failingDateFormat);

        // Mock static ISO8601Utils.parse to throw ParseException
        try (MockedStatic<ISO8601Utils> isoMock = Mockito.mockStatic(ISO8601Utils.class)) {
            isoMock.when(() -> ISO8601Utils.parse(eq(invalidDateString), any(ParsePosition.class)))
                    .thenThrow(new ParseException("fail", 0));

            Method method = DefaultDateTypeAdapter.class.getDeclaredMethod("deserializeToDate", JsonReader.class);
            method.setAccessible(true);

            JsonSyntaxException thrown = assertThrows(JsonSyntaxException.class, () -> {
                try {
                    method.invoke(adapter, jsonReaderMock);
                } catch (java.lang.reflect.InvocationTargetException e) {
                    // unwrap the cause thrown by invoked method
                    throw e.getCause();
                }
            });

            // The cause should be ParseException
            Throwable cause = thrown.getCause();
            assertNotNull(cause);
            assertTrue(cause instanceof ParseException);

            String message = thrown.getMessage();
            assertTrue(message.contains(invalidDateString));
            assertTrue(message.contains("$.dateField"));

            verify(failingDateFormat).parse(invalidDateString);
            isoMock.verify(() -> ISO8601Utils.parse(eq(invalidDateString), any(ParsePosition.class)));
            verify(jsonReaderMock).getPreviousPath();
        }
    }
}