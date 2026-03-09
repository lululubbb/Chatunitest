package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.JavaVersion;
import com.google.gson.internal.PreJava9DateFormatProvider;
import com.google.gson.internal.bind.util.ISO8601Utils;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DefaultDateTypeAdapter_597_1Test {

    private Class<?> adapterClass;
    private Object adapterInstance;
    private Class<?> dateTypeClass;
    private Object dateTypeMock;

    @BeforeEach
    public void setUp() throws Exception {
        adapterClass = Class.forName("com.google.gson.internal.bind.DefaultDateTypeAdapter");
        dateTypeClass = Class.forName("com.google.gson.internal.bind.DefaultDateTypeAdapter$DateType");

        // Create an anonymous subclass of DateType<Date> with required methods implemented.
        dateTypeMock = new DateTypeImpl();

        Constructor<?> constructor = adapterClass.getDeclaredConstructor(dateTypeClass, String.class);
        constructor.setAccessible(true);

        adapterInstance = constructor.newInstance(dateTypeMock, "yyyy-MM-dd");
    }

    // Concrete implementation of DateType<Date> with required methods
    private static class DateTypeImpl extends com.google.gson.internal.bind.DefaultDateTypeAdapter.DateType<Date> {
        protected DateTypeImpl() {
            super(Date.class);
        }

        @Override
        public Date deserialize(String s) throws Exception {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            return sdf.parse(s);
        }

        @Override
        public String serialize(Date date) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            return sdf.format(date);
        }
    }

    @Test
    @Timeout(8000)
    public void testWrite_and_Read() throws Exception {
        JsonWriter jsonWriter = mock(JsonWriter.class);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date testDate = sdf.parse("2023-04-01");

        Method writeMethod = adapterClass.getMethod("write", JsonWriter.class, Date.class);
        writeMethod.invoke(adapterInstance, jsonWriter, testDate);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(jsonWriter).value(captor.capture());
        String formattedDate = captor.getValue();
        assertEquals("2023-04-01", formattedDate);

        JsonReader jsonReader = mock(JsonReader.class);
        when(jsonReader.peek()).thenReturn(JsonToken.STRING);
        when(jsonReader.nextString()).thenReturn("2023-04-01");

        Method readMethod = adapterClass.getMethod("read", JsonReader.class);
        Object result = readMethod.invoke(adapterInstance, jsonReader);

        assertNotNull(result);
        assertTrue(result instanceof Date);
        Date parsedDate = (Date) result;
        assertEquals(testDate, parsedDate);
    }

    @Test
    @Timeout(8000)
    public void testRead_nullJsonToken() throws Exception {
        JsonReader jsonReader = mock(JsonReader.class);
        when(jsonReader.peek()).thenReturn(JsonToken.NULL);
        doNothing().when(jsonReader).nextNull();

        Method readMethod = adapterClass.getMethod("read", JsonReader.class);
        Object result = readMethod.invoke(adapterInstance, jsonReader);

        assertNull(result);
        verify(jsonReader).nextNull();
    }

    @Test
    @Timeout(8000)
    public void testDeserializeToDate_validAndInvalid() throws Exception {
        Method deserializeToDateMethod = adapterClass.getDeclaredMethod("deserializeToDate", JsonReader.class);
        deserializeToDateMethod.setAccessible(true);

        JsonReader jsonReader = mock(JsonReader.class);

        when(jsonReader.nextString()).thenReturn("2023-04-01");
        Object dateObj = deserializeToDateMethod.invoke(adapterInstance, jsonReader);
        assertNotNull(dateObj);
        assertTrue(dateObj instanceof Date);

        when(jsonReader.nextString()).thenReturn("invalid-date");
        try {
            deserializeToDateMethod.invoke(adapterInstance, jsonReader);
            fail("Expected JsonSyntaxException");
        } catch (java.lang.reflect.InvocationTargetException e) {
            assertTrue(e.getCause() instanceof JsonSyntaxException);
        }
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