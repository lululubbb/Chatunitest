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
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

class DefaultDateTypeAdapter_deserializeToDate_Test {

    private DefaultDateTypeAdapter<?> adapter;
    private JsonReader jsonReaderMock;

    @BeforeEach
    void setUp() throws Exception {
        // Use reflection to instantiate DefaultDateTypeAdapter with a dummy DateType
        Class<?> clazz = DefaultDateTypeAdapter.class;
        Class<?> dateTypeClass = Class.forName("com.google.gson.internal.bind.DefaultDateTypeAdapter$DateType");
        Object dummyDateType = java.lang.reflect.Proxy.newProxyInstance(
                dateTypeClass.getClassLoader(),
                new Class[]{dateTypeClass},
                (proxy, method, args) -> {
                    if ("getTimestamp".equals(method.getName())) {
                        return null;
                    }
                    return null;
                });

        // Use private constructor DefaultDateTypeAdapter(DateType<T> dateType, String datePattern)
        var constructor = clazz.getDeclaredConstructor(dateTypeClass, String.class);
        constructor.setAccessible(true);
        adapter = (DefaultDateTypeAdapter<?>) constructor.newInstance(dummyDateType, "yyyy-MM-dd");

        // Prepare dateFormats list with multiple formats for testing
        Field dateFormatsField = clazz.getDeclaredField("dateFormats");
        dateFormatsField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<DateFormat> dateFormats = (List<DateFormat>) dateFormatsField.get(adapter);
        dateFormats.clear();
        dateFormats.add(new SimpleDateFormat("yyyy-MM-dd", Locale.US));
        dateFormats.add(new SimpleDateFormat("dd/MM/yyyy", Locale.US));

        // Mock JsonReader
        jsonReaderMock = Mockito.mock(JsonReader.class);
    }

    @Test
    @Timeout(8000)
    void testDeserializeToDate_parsesWithFirstDateFormat() throws Exception {
        String dateString = "2023-06-20";
        when(jsonReaderMock.nextString()).thenReturn(dateString);

        Method method = DefaultDateTypeAdapter.class.getDeclaredMethod("deserializeToDate", JsonReader.class);
        method.setAccessible(true);
        Date result = (Date) method.invoke(adapter, jsonReaderMock);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date expected = df.parse(dateString);
        assertEquals(expected, result);
    }

    @Test
    @Timeout(8000)
    void testDeserializeToDate_parsesWithSecondDateFormat() throws Exception {
        String dateString = "20/06/2023";
        when(jsonReaderMock.nextString()).thenReturn(dateString);

        Method method = DefaultDateTypeAdapter.class.getDeclaredMethod("deserializeToDate", JsonReader.class);
        method.setAccessible(true);
        Date result = (Date) method.invoke(adapter, jsonReaderMock);

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        Date expected = df.parse(dateString);
        assertEquals(expected, result);
    }

    @Test
    @Timeout(8000)
    void testDeserializeToDate_parsesWithISO8601Utils() throws Exception {
        // Clear dateFormats to force fallback to ISO8601Utils parsing
        Field dateFormatsField = DefaultDateTypeAdapter.class.getDeclaredField("dateFormats");
        dateFormatsField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<DateFormat> dateFormats = (List<DateFormat>) dateFormatsField.get(adapter);
        dateFormats.clear();

        String iso8601Date = "2023-06-20T15:30:45Z";
        when(jsonReaderMock.nextString()).thenReturn(iso8601Date);

        Method method = DefaultDateTypeAdapter.class.getDeclaredMethod("deserializeToDate", JsonReader.class);
        method.setAccessible(true);
        Date result = (Date) method.invoke(adapter, jsonReaderMock);

        // ISO8601Utils.parse returns correct date, verify non-null
        assertNotNull(result);
    }

    @Test
    @Timeout(8000)
    void testDeserializeToDate_throwsJsonSyntaxException_whenParseFails() throws Exception {
        // Clear dateFormats to force fallback to ISO8601Utils parsing
        Field dateFormatsField = DefaultDateTypeAdapter.class.getDeclaredField("dateFormats");
        dateFormatsField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<DateFormat> dateFormats = (List<DateFormat>) dateFormatsField.get(adapter);
        dateFormats.clear();

        String invalidDate = "invalid-date";
        when(jsonReaderMock.nextString()).thenReturn(invalidDate);
        when(jsonReaderMock.getPreviousPath()).thenReturn("$.date");

        Method method = DefaultDateTypeAdapter.class.getDeclaredMethod("deserializeToDate", JsonReader.class);
        method.setAccessible(true);

        Exception exception = assertThrows(Exception.class, () -> {
            method.invoke(adapter, jsonReaderMock);
        });

        Throwable cause = exception.getCause();
        assertNotNull(cause);
        assertTrue(cause instanceof JsonSyntaxException);
        assertTrue(cause.getMessage().contains("Failed parsing 'invalid-date' as Date; at path $.date"));
    }
}