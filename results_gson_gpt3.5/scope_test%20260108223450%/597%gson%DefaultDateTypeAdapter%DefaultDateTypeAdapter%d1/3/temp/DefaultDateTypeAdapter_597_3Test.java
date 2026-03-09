package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.JavaVersion;
import com.google.gson.internal.PreJava9DateFormatProvider;
import com.google.gson.internal.bind.util.ISO8601Utils;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DefaultDateTypeAdapter_597_3Test {

    private Class<?> adapterClass;
    private Constructor<?> constructor;
    private Object adapterInstance;

    interface DateType<T extends Date> {
      T deserialize(Date date);
      void serialize(Date date, JsonWriter writer) throws IOException;
    }

    private DateType<Date> dateType = new DateType<Date>() {
        @Override
        public Date deserialize(Date date) {
            return date;
        }
        @Override
        public void serialize(Date date, JsonWriter writer) throws IOException {
            writer.value(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).format(date));
        }
    };

    @BeforeEach
    public void setup() throws Exception {
        adapterClass = Class.forName("com.google.gson.internal.bind.DefaultDateTypeAdapter");
        // Use the constructor with DateType and String parameters, but DateType must be the interface from DefaultDateTypeAdapter
        Class<?> dateTypeInterface = Class.forName("com.google.gson.internal.bind.DefaultDateTypeAdapter$DateType");
        // Create a proxy instance of DateType to pass to constructor
        Object proxyDateType = java.lang.reflect.Proxy.newProxyInstance(
                adapterClass.getClassLoader(),
                new Class<?>[]{dateTypeInterface},
                (proxy, method, args) -> {
                    if ("deserialize".equals(method.getName())) {
                        return args[0]; // return date argument
                    } else if ("serialize".equals(method.getName())) {
                        // args[1] is JsonWriter
                        ((JsonWriter) args[1]).value(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).format((Date) args[0]));
                        return null;
                    }
                    return null;
                });
        constructor = adapterClass.getDeclaredConstructor(dateTypeInterface, String.class);
        constructor.setAccessible(true);
        adapterInstance = constructor.newInstance(proxyDateType, "yyyy-MM-dd'T'HH:mm:ss'Z'");
    }

    @Test
    @Timeout(8000)
    public void testConstructor_addsTwoDateFormatsIfLocaleNotUS() throws Exception {
        Locale defaultLocale = Locale.getDefault();
        try {
            // force default locale to non-US to test second dateFormat added
            setDefaultLocale(Locale.FRANCE);
            Class<?> dateTypeInterface = Class.forName("com.google.gson.internal.bind.DefaultDateTypeAdapter$DateType");
            Object proxyDateType = java.lang.reflect.Proxy.newProxyInstance(
                    adapterClass.getClassLoader(),
                    new Class<?>[]{dateTypeInterface},
                    (proxy, method, args) -> {
                        if ("deserialize".equals(method.getName())) {
                            return args[0];
                        } else if ("serialize".equals(method.getName())) {
                            ((JsonWriter) args[1]).value(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).format((Date) args[0]));
                            return null;
                        }
                        return null;
                    });
            Object adapter = constructor.newInstance(proxyDateType, "yyyy-MM-dd");
            var dateFormatsField = adapterClass.getDeclaredField("dateFormats");
            dateFormatsField.setAccessible(true);
            @SuppressWarnings("unchecked")
            var dateFormats = (java.util.List<?>) dateFormatsField.get(adapter);
            assertEquals(2, dateFormats.size());
            assertEquals(SimpleDateFormat.class, dateFormats.get(0).getClass());
            assertEquals(SimpleDateFormat.class, dateFormats.get(1).getClass());
        } finally {
            setDefaultLocale(defaultLocale);
        }
    }

    @Test
    @Timeout(8000)
    public void testConstructor_addsOneDateFormatIfLocaleUS() throws Exception {
        Locale defaultLocale = Locale.getDefault();
        try {
            setDefaultLocale(Locale.US);
            Class<?> dateTypeInterface = Class.forName("com.google.gson.internal.bind.DefaultDateTypeAdapter$DateType");
            Object proxyDateType = java.lang.reflect.Proxy.newProxyInstance(
                    adapterClass.getClassLoader(),
                    new Class<?>[]{dateTypeInterface},
                    (proxy, method, args) -> {
                        if ("deserialize".equals(method.getName())) {
                            return args[0];
                        } else if ("serialize".equals(method.getName())) {
                            ((JsonWriter) args[1]).value(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).format((Date) args[0]));
                            return null;
                        }
                        return null;
                    });
            Object adapter = constructor.newInstance(proxyDateType, "yyyy-MM-dd");
            var dateFormatsField = adapterClass.getDeclaredField("dateFormats");
            dateFormatsField.setAccessible(true);
            @SuppressWarnings("unchecked")
            var dateFormats = (java.util.List<?>) dateFormatsField.get(adapter);
            assertEquals(1, dateFormats.size());
        } finally {
            setDefaultLocale(defaultLocale);
        }
    }

    @Test
    @Timeout(8000)
    public void testWrite_callsDateTypeSerialize() throws Exception {
        JsonWriter writer = mock(JsonWriter.class);
        Date date = new Date();
        Class<?> dateTypeInterface = Class.forName("com.google.gson.internal.bind.DefaultDateTypeAdapter$DateType");
        Object proxyDateType = java.lang.reflect.Proxy.newProxyInstance(
                adapterClass.getClassLoader(),
                new Class<?>[]{dateTypeInterface},
                new java.lang.reflect.InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        if ("serialize".equals(method.getName())) {
                            ((JsonWriter) args[1]).value(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).format((Date) args[0]));
                            return null;
                        } else if ("deserialize".equals(method.getName())) {
                            return args[0];
                        }
                        return null;
                    }
                });
        Object adapter = constructor.newInstance(proxyDateType, "yyyy-MM-dd");
        Method writeMethod = adapterClass.getMethod("write", JsonWriter.class, Date.class);
        writeMethod.invoke(adapter, writer, date);
        // We can't use verify on proxyDateType, so just verify writer.value was called
        verify(writer).value(anyString());
    }

    @Test
    @Timeout(8000)
    public void testWrite_writesNullWhenValueNull() throws Exception {
        JsonWriter writer = mock(JsonWriter.class);
        Method writeMethod = adapterClass.getMethod("write", JsonWriter.class, Date.class);
        writeMethod.invoke(adapterInstance, writer, (Date) null);
        verify(writer).nullValue();
    }

    @Test
    @Timeout(8000)
    public void testRead_returnsDateFromDeserializeToDate() throws Exception {
        JsonReader reader = mock(JsonReader.class);
        when(reader.peek()).thenReturn(JsonToken.STRING);
        when(reader.nextString()).thenReturn("2023-06-01T12:34:56Z");
        Method readMethod = adapterClass.getMethod("read", JsonReader.class);
        Object result = readMethod.invoke(adapterInstance, reader);
        assertNotNull(result);
        assertTrue(result instanceof Date);
    }

    @Test
    @Timeout(8000)
    public void testRead_returnsNullOnJsonNull() throws Exception {
        JsonReader reader = mock(JsonReader.class);
        when(reader.peek()).thenReturn(JsonToken.NULL);
        Method readMethod = adapterClass.getMethod("read", JsonReader.class);
        Object result = readMethod.invoke(adapterInstance, reader);
        assertNull(result);
        verify(reader).nextNull();
    }

    @Test
    @Timeout(8000)
    public void testDeserializeToDate_parsesDate() throws Exception {
        JsonReader reader = mock(JsonReader.class);
        when(reader.peek()).thenReturn(JsonToken.STRING);
        when(reader.nextString()).thenReturn("2023-06-01T12:34:56Z");
        Method method = adapterClass.getDeclaredMethod("deserializeToDate", JsonReader.class);
        method.setAccessible(true);
        Object date = method.invoke(adapterInstance, reader);
        assertNotNull(date);
        assertTrue(date instanceof Date);
    }

    @Test
    @Timeout(8000)
    public void testDeserializeToDate_throwsJsonSyntaxExceptionOnParseException() throws Exception {
        JsonReader reader = mock(JsonReader.class);
        when(reader.peek()).thenReturn(JsonToken.STRING);
        when(reader.nextString()).thenReturn("invalid-date");
        Method method = adapterClass.getDeclaredMethod("deserializeToDate", JsonReader.class);
        method.setAccessible(true);
        try {
            method.invoke(adapterInstance, reader);
            fail("Expected InvocationTargetException");
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            assertNotNull(cause);
            assertEquals(com.google.gson.JsonSyntaxException.class, cause.getClass());
        }
    }

    @Test
    @Timeout(8000)
    public void testToString_containsDateFormats() throws Exception {
        Method toStringMethod = adapterClass.getMethod("toString");
        String s = (String) toStringMethod.invoke(adapterInstance);
        assertTrue(s.contains("DefaultDateTypeAdapter"));
        assertTrue(s.contains("dateFormats"));
    }

    private void setDefaultLocale(Locale locale) throws Exception {
        java.lang.reflect.Field defaultLocaleField = Locale.class.getDeclaredField("defaultLocale");
        defaultLocaleField.setAccessible(true);
        defaultLocaleField.set(null, locale);
        java.lang.reflect.Field defaultDisplayLocaleField = Locale.class.getDeclaredField("defaultDisplayLocale");
        defaultDisplayLocaleField.setAccessible(true);
        defaultDisplayLocaleField.set(null, locale);
    }
}