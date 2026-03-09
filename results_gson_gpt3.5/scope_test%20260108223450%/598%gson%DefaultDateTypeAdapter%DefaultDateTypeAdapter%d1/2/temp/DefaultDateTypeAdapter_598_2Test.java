package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.JavaVersion;
import com.google.gson.internal.PreJava9DateFormatProvider;
import com.google.gson.internal.bind.util.ISO8601Utils;
import java.io.IOException;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DefaultDateTypeAdapter_598_2Test {

    private Class<?> adapterClass;
    private Object adapterInstance;

    interface DateType<T extends Date> {
        T deserialize(Date date);
        Date serialize(T t);
    }

    static class DateTypeImpl implements DateType<Date> {
        @Override
        public Date deserialize(Date date) {
            return date;
        }

        @Override
        public Date serialize(Date t) {
            return t;
        }
    }

    @BeforeEach
    void setUp() throws Exception {
        adapterClass = Class.forName("com.google.gson.internal.bind.DefaultDateTypeAdapter");

        // Find a constructor that accepts DateType and int (dateStyle) - it is private, so getDeclaredConstructors
        Constructor<?> constructor = null;
        for (Constructor<?> c : adapterClass.getDeclaredConstructors()) {
            Class<?>[] params = c.getParameterTypes();
            if (params.length == 2 &&
                params[0].getSimpleName().equals("DateType") &&
                params[1] == int.class) {
                constructor = c;
                break;
            }
        }
        if (constructor == null) {
            throw new NoSuchMethodException("No suitable constructor found for DefaultDateTypeAdapter");
        }
        constructor.setAccessible(true);

        // Use the static DateTypeImpl class instance
        adapterInstance = constructor.newInstance(new DateTypeImpl(), DateFormat.SHORT);
    }

    @Test
    @Timeout(8000)
    void testConstructorAddsDateFormats() throws Exception {
        var dateFormatsField = adapterClass.getDeclaredField("dateFormats");
        dateFormatsField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<DateFormat> dateFormats = (List<DateFormat>) dateFormatsField.get(adapterInstance);

        assertFalse(dateFormats.isEmpty());
        assertTrue(dateFormats.get(0) instanceof DateFormat);
        if (!Locale.getDefault().equals(Locale.US)) {
            assertTrue(dateFormats.size() >= 2);
        }
    }

    @Test
    @Timeout(8000)
    void testWriteAndRead() throws Exception {
        JsonWriter writer = mock(JsonWriter.class);
        Date now = new Date();

        Method writeMethod = adapterClass.getMethod("write", JsonWriter.class, Date.class);
        writeMethod.invoke(adapterInstance, writer, now);

        verify(writer).value(anyString());

        JsonReader reader = mock(JsonReader.class);
        when(reader.peek()).thenReturn(JsonToken.STRING);
        when(reader.nextString()).thenReturn(new SimpleDateFormat("MMM d, yyyy", Locale.US).format(now));

        Method readMethod = adapterClass.getMethod("read", JsonReader.class);
        Object result = readMethod.invoke(adapterInstance, reader);

        assertNotNull(result);
        assertTrue(result instanceof Date);
    }

    @Test
    @Timeout(8000)
    void testReadNull() throws Exception {
        JsonReader reader = mock(JsonReader.class);
        when(reader.peek()).thenReturn(JsonToken.NULL);
        doNothing().when(reader).nextNull();

        Method readMethod = adapterClass.getMethod("read", JsonReader.class);
        Object result = readMethod.invoke(adapterInstance, reader);

        assertNull(result);
        verify(reader).nextNull();
    }

    @Test
    @Timeout(8000)
    void testDeserializeToDateValidString() throws Exception {
        JsonReader reader = mock(JsonReader.class);
        Date now = new Date();
        String dateStr = new SimpleDateFormat("MMM d, yyyy", Locale.US).format(now);

        when(reader.nextString()).thenReturn(dateStr);

        Method deserializeMethod = adapterClass.getDeclaredMethod("deserializeToDate", JsonReader.class);
        deserializeMethod.setAccessible(true);

        Object date = deserializeMethod.invoke(adapterInstance, reader);

        assertNotNull(date);
        assertTrue(date instanceof Date);
    }

    @Test
    @Timeout(8000)
    void testDeserializeToDateInvalidStringThrows() throws Exception {
        JsonReader reader = mock(JsonReader.class);
        when(reader.nextString()).thenReturn("invalid-date");

        Method deserializeMethod = adapterClass.getDeclaredMethod("deserializeToDate", JsonReader.class);
        deserializeMethod.setAccessible(true);

        InvocationTargetException e = assertThrows(InvocationTargetException.class, () -> {
            deserializeMethod.invoke(adapterInstance, reader);
        });
        assertTrue(e.getCause() instanceof JsonSyntaxException);
    }

    @Test
    @Timeout(8000)
    void testToStringContainsClassName() throws Exception {
        Method toStringMethod = adapterClass.getMethod("toString");
        String str = (String) toStringMethod.invoke(adapterInstance);
        assertTrue(str.contains("DefaultDateTypeAdapter"));
    }
}