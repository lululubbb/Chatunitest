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
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.bind.DefaultDateTypeAdapter;
import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DefaultDateTypeAdapter_602_4Test {

    private DefaultDateTypeAdapter<Date> adapter;
    private JsonReader jsonReaderMock;

    @BeforeEach
    public void setUp() throws Exception {
        Class<DefaultDateTypeAdapter> clazz = DefaultDateTypeAdapter.class;

        // Find constructor with (DateType<T>, String) signature
        Constructor<?> constructor = null;
        for (Constructor<?> c : clazz.getDeclaredConstructors()) {
            Class<?>[] params = c.getParameterTypes();
            if (params.length == 2 && params[1] == String.class) {
                constructor = c;
                break;
            }
        }
        assertNotNull(constructor, "Expected constructor with (DateType, String) parameters");
        constructor.setAccessible(true);

        // DateType is a static final class inside DefaultDateTypeAdapter
        Class<?> dateTypeClass = null;
        for (Class<?> innerClass : clazz.getDeclaredClasses()) {
            if ("DateType".equals(innerClass.getSimpleName())) {
                dateTypeClass = innerClass;
                break;
            }
        }
        assertNotNull(dateTypeClass, "DateType inner class not found");

        // DateType has a synthetic constructor with no parameters (likely private static class)
        Constructor<?> dtConstructor = null;
        for (Constructor<?> c : dateTypeClass.getDeclaredConstructors()) {
            Class<?>[] params = c.getParameterTypes();
            if (params.length == 0) {
                dtConstructor = c;
                break;
            }
        }
        assertNotNull(dtConstructor, "Expected DateType no-arg constructor");
        dtConstructor.setAccessible(true);
        Object dateTypeInstance = dtConstructor.newInstance();

        adapter = (DefaultDateTypeAdapter<Date>) constructor.newInstance(dateTypeInstance, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        // Clear dateFormats and add an empty list
        Field dateFormatsField = clazz.getDeclaredField("dateFormats");
        dateFormatsField.setAccessible(true);
        List<DateFormat> dateFormats = new ArrayList<>();
        dateFormatsField.set(adapter, dateFormats);

        jsonReaderMock = mock(JsonReader.class);
    }

    @Test
    @Timeout(8000)
    public void testDeserializeToDate_successWithDateFormat() throws Exception {
        String dateString = "2023-06-10";
        when(jsonReaderMock.nextString()).thenReturn(dateString);

        // Add a DateFormat that can parse the dateString
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Field dateFormatsField = DefaultDateTypeAdapter.class.getDeclaredField("dateFormats");
        dateFormatsField.setAccessible(true);
        List<DateFormat> dateFormats = (List<DateFormat>) dateFormatsField.get(adapter);
        dateFormats.add(dateFormat);

        Method method = DefaultDateTypeAdapter.class.getDeclaredMethod("deserializeToDate", JsonReader.class);
        method.setAccessible(true);

        Date result = (Date) method.invoke(adapter, jsonReaderMock);

        assertNotNull(result);
        assertEquals(dateFormat.parse(dateString), result);
    }

    @Test
    @Timeout(8000)
    public void testDeserializeToDate_successWithISO8601Utils() throws Exception {
        String isoDate = "2023-06-10T12:34:56.789Z";
        when(jsonReaderMock.nextString()).thenReturn(isoDate);

        // Empty dateFormats to force fallback to ISO8601Utils
        Field dateFormatsField = DefaultDateTypeAdapter.class.getDeclaredField("dateFormats");
        dateFormatsField.setAccessible(true);
        List<DateFormat> dateFormats = (List<DateFormat>) dateFormatsField.get(adapter);
        dateFormats.clear();

        Method method = DefaultDateTypeAdapter.class.getDeclaredMethod("deserializeToDate", JsonReader.class);
        method.setAccessible(true);

        Date result = (Date) method.invoke(adapter, jsonReaderMock);

        assertNotNull(result);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        format.setLenient(false);
        assertEquals(isoDate, format.format(result));
    }

    @Test
    @Timeout(8000)
    public void testDeserializeToDate_parseExceptionThrowsJsonSyntaxException() throws Exception {
        String badDate = "bad-date-string";
        when(jsonReaderMock.nextString()).thenReturn(badDate);
        when(jsonReaderMock.getPreviousPath()).thenReturn("$.date");

        // Empty dateFormats to force fallback to ISO8601Utils which will throw ParseException
        Field dateFormatsField = DefaultDateTypeAdapter.class.getDeclaredField("dateFormats");
        dateFormatsField.setAccessible(true);
        List<DateFormat> dateFormats = (List<DateFormat>) dateFormatsField.get(adapter);
        dateFormats.clear();

        Method method = DefaultDateTypeAdapter.class.getDeclaredMethod("deserializeToDate", JsonReader.class);
        method.setAccessible(true);

        JsonSyntaxException thrown = assertThrows(JsonSyntaxException.class, () -> {
            try {
                method.invoke(adapter, jsonReaderMock);
            } catch (java.lang.reflect.InvocationTargetException e) {
                // Rethrow the cause of the InvocationTargetException
                throw e.getCause();
            }
        });

        assertTrue(thrown.getMessage().contains("Failed parsing"));
        assertTrue(thrown.getMessage().contains(badDate));
        assertTrue(thrown.getMessage().contains("$.date"));
        assertNotNull(thrown.getCause());
        assertEquals(ParseException.class, thrown.getCause().getClass());
    }
}