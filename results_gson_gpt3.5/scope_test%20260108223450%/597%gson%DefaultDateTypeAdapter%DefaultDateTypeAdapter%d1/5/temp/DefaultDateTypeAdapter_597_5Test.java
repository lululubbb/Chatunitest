package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.JavaVersion;
import com.google.gson.internal.PreJava9DateFormatProvider;
import com.google.gson.internal.bind.util.ISO8601Utils;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DefaultDateTypeAdapter_597_5Test {

    private DefaultDateTypeAdapter<Date> adapter;

    @BeforeEach
    public void setUp() throws Exception {
        Class<?> dateTypeClass = null;
        for (Class<?> innerClass : DefaultDateTypeAdapter.class.getDeclaredClasses()) {
            if ("DateType".equals(innerClass.getSimpleName())) {
                dateTypeClass = innerClass;
                break;
            }
        }
        if (dateTypeClass == null) {
            throw new ClassNotFoundException("DateType inner class not found");
        }

        Constructor<?> enumConstructor = dateTypeClass.getDeclaredConstructors()[0];
        enumConstructor.setAccessible(true);

        // DateType is an enum, so instantiate the enum constant for "DATE" or "DEFAULT" or first constant
        Object dateType = null;
        Object[] enumConstants = dateTypeClass.getEnumConstants();
        if (enumConstants != null && enumConstants.length > 0) {
            dateType = enumConstants[0];
        }
        if (dateType == null) {
            throw new IllegalStateException("No enum constants found for DateType");
        }

        Constructor<DefaultDateTypeAdapter> constructor = DefaultDateTypeAdapter.class.getDeclaredConstructor(dateTypeClass, String.class);
        constructor.setAccessible(true);
        adapter = constructor.newInstance(dateType, "yyyy-MM-dd'T'HH:mm:ss'Z'");
    }

    @Test
    @Timeout(8000)
    public void testWrite_withNonNullDate() throws IOException, ParseException {
        JsonWriter writer = mock(JsonWriter.class);
        Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).parse("2023-01-01T12:00:00Z");
        adapter.write(writer, date);
        verify(writer).value(anyString());
    }

    @Test
    @Timeout(8000)
    public void testWrite_withNullDate() throws IOException {
        JsonWriter writer = mock(JsonWriter.class);
        adapter.write(writer, null);
        verify(writer).nullValue();
    }

    @Test
    @Timeout(8000)
    public void testRead_withValidDate() throws Exception {
        JsonReader reader = mock(JsonReader.class);
        when(reader.peek()).thenReturn(JsonToken.STRING);
        when(reader.nextString()).thenReturn("2023-01-01T12:00:00Z");

        Date result = adapter.read(reader);
        assertNotNull(result);
    }

    @Test
    @Timeout(8000)
    public void testRead_withNull() throws Exception {
        JsonReader reader = mock(JsonReader.class);
        when(reader.peek()).thenReturn(JsonToken.NULL);
        doNothing().when(reader).nextNull();

        Date result = adapter.read(reader);
        assertNull(result);
        verify(reader).nextNull();
    }

    @Test
    @Timeout(8000)
    public void testDeserializeToDate_withValidString() throws Exception {
        JsonReader reader = mock(JsonReader.class);
        when(reader.nextString()).thenReturn("2023-01-01T12:00:00Z");

        Method method = DefaultDateTypeAdapter.class.getDeclaredMethod("deserializeToDate", JsonReader.class);
        method.setAccessible(true);
        Date date = (Date) method.invoke(adapter, reader);
        assertNotNull(date);
    }

    @Test
    @Timeout(8000)
    public void testDeserializeToDate_withParseException() throws Exception {
        JsonReader reader = mock(JsonReader.class);
        when(reader.nextString()).thenReturn("invalid-date");

        Method method = DefaultDateTypeAdapter.class.getDeclaredMethod("deserializeToDate", JsonReader.class);
        method.setAccessible(true);
        assertThrows(RuntimeException.class, () -> {
            try {
                method.invoke(adapter, reader);
            } catch (Exception e) {
                Throwable cause = e.getCause();
                if (cause != null) {
                    throw cause;
                } else {
                    throw e;
                }
            }
        });
    }

    @Test
    @Timeout(8000)
    public void testToString_containsDateFormats() {
        String toString = adapter.toString();
        assertTrue(toString.contains("DefaultDateTypeAdapter"));
        assertTrue(toString.contains("SimpleDateFormat"));
    }
}