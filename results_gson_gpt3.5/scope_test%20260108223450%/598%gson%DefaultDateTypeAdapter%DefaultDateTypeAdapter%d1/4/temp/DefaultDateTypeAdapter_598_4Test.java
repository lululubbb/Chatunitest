package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.JavaVersion;
import com.google.gson.internal.PreJava9DateFormatProvider;
import com.google.gson.internal.bind.util.ISO8601Utils;
import java.io.IOException;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class DefaultDateTypeAdapter_598_4Test {

    private Class<?> adapterClass;
    private Object adapterInstance;
    private DateFormat usDateFormat;

    @BeforeEach
    public void setUp() throws Exception {
        adapterClass = Class.forName("com.google.gson.internal.bind.DefaultDateTypeAdapter");

        // Simplify: Use the public constructor DefaultDateTypeAdapter(DateFormat) instead of the private one with DateType

        try {
            Constructor<?> ctor = adapterClass.getDeclaredConstructor(DateFormat.class);
            ctor.setAccessible(true);
            adapterInstance = ctor.newInstance(DateFormat.getDateInstance(DateFormat.SHORT, Locale.US));
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("No suitable constructor found for DefaultDateTypeAdapter");
        }

        usDateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.US);
    }

    @Test
    @Timeout(8000)
    public void testWrite_and_Read() throws Exception {
        JsonWriter writer = mock(JsonWriter.class);
        Date now = new Date();

        // Test write with non-null date
        Method writeMethod = adapterClass.getMethod("write", JsonWriter.class, Date.class);
        writeMethod.invoke(adapterInstance, writer, now);
        verify(writer).value(anyString());

        // Test write with null date
        writeMethod.invoke(adapterInstance, writer, (Date) null);
        verify(writer).nullValue();

        // Prepare JsonReader mock for read tests
        JsonReader reader = mock(JsonReader.class);

        // Case 1: JsonToken.NULL
        when(reader.peek()).thenReturn(JsonToken.NULL);
        doNothing().when(reader).nextNull();
        Method readMethod = adapterClass.getMethod("read", JsonReader.class);
        Object result = readMethod.invoke(adapterInstance, reader);
        assertNull(result);

        // Case 2: JsonToken.STRING with valid date string
        when(reader.peek()).thenReturn(JsonToken.STRING);
        String formattedDate = usDateFormat.format(new Date());
        when(reader.nextString()).thenReturn(formattedDate);
        result = readMethod.invoke(adapterInstance, reader);
        assertNotNull(result);
        assertTrue(result instanceof Date);

        // Case 3: JsonToken.STRING with invalid date string throws JsonSyntaxException
        when(reader.peek()).thenReturn(JsonToken.STRING);
        when(reader.nextString()).thenReturn("invalid-date-string");
        assertThrows(RuntimeException.class, () -> {
            readMethod.invoke(adapterInstance, reader);
        });

        // Case 4: JsonToken.NUMBER (simulate timestamp)
        when(reader.peek()).thenReturn(JsonToken.NUMBER);
        long timestamp = System.currentTimeMillis();
        when(reader.nextLong()).thenReturn(timestamp);
        result = readMethod.invoke(adapterInstance, reader);
        assertNotNull(result);
        assertTrue(result instanceof Date);
        assertEquals(timestamp, ((Date) result).getTime());
    }

    @Test
    @Timeout(8000)
    public void testDeserializeToDate_withValidString() throws Exception {
        JsonReader reader = mock(JsonReader.class);
        String dateStr = usDateFormat.format(new Date());

        when(reader.peek()).thenReturn(JsonToken.STRING);
        when(reader.nextString()).thenReturn(dateStr);

        Method deserializeMethod = adapterClass.getDeclaredMethod("deserializeToDate", JsonReader.class);
        deserializeMethod.setAccessible(true);

        Object date = deserializeMethod.invoke(adapterInstance, reader);
        assertNotNull(date);
        assertTrue(date instanceof Date);
    }

    @Test
    @Timeout(8000)
    public void testDeserializeToDate_withNull() throws Exception {
        JsonReader reader = mock(JsonReader.class);

        when(reader.peek()).thenReturn(JsonToken.NULL);
        doNothing().when(reader).nextNull();

        Method deserializeMethod = adapterClass.getDeclaredMethod("deserializeToDate", JsonReader.class);
        deserializeMethod.setAccessible(true);

        Object date = deserializeMethod.invoke(adapterInstance, reader);
        assertNull(date);
    }

    @Test
    @Timeout(8000)
    public void testToString_containsSimpleName() throws Exception {
        Method toStringMethod = adapterClass.getMethod("toString");
        String str = (String) toStringMethod.invoke(adapterInstance);
        assertNotNull(str);
        assertTrue(str.contains("DefaultDateTypeAdapter"));
    }
}