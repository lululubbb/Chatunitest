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
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DefaultDateTypeAdapter_598_1Test {

    private Class<?> adapterClass;
    private Object adapterInstance;
    private Object dateTypeInstance;

    @BeforeEach
    public void setUp() throws Exception {
        adapterClass = Class.forName("com.google.gson.internal.bind.DefaultDateTypeAdapter");

        // Get the DateType class from the adapterClass
        Class<?> dateTypeClass = null;
        for (Class<?> innerClass : adapterClass.getDeclaredClasses()) {
            if ("DateType".equals(innerClass.getSimpleName())) {
                dateTypeClass = innerClass;
                break;
            }
        }
        assertNotNull(dateTypeClass, "DateType inner interface/class not found");

        if (dateTypeClass.isInterface()) {
            // Create a proxy instance of the adapterClass.DateType interface
            dateTypeInstance = Proxy.newProxyInstance(
                    adapterClass.getClassLoader(),
                    new Class<?>[]{dateTypeClass},
                    new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            if ("from".equals(method.getName()) && args != null && args.length == 1 && args[0] instanceof Date) {
                                return args[0]; // identity function for Date
                            }
                            if ("toString".equals(method.getName()) && (args == null || args.length == 0)) {
                                return "DateTypeProxy";
                            }
                            if ("hashCode".equals(method.getName()) && (args == null || args.length == 0)) {
                                return System.identityHashCode(proxy);
                            }
                            if ("equals".equals(method.getName()) && args != null && args.length == 1) {
                                return proxy == args[0];
                            }
                            return null;
                        }
                    });
        } else {
            // DateType is abstract class, create an anonymous subclass via reflection
            // Find a constructor with parameters (int, int) or no-arg constructor
            Constructor<?> ctor = null;
            Constructor<?>[] ctors = dateTypeClass.getDeclaredConstructors();
            for (Constructor<?> c : ctors) {
                if (c.getParameterCount() == 0) {
                    ctor = c;
                    break;
                }
            }
            if (ctor == null) {
                // try to find constructor with (int, int) and pass DateFormat.SHORT for both
                for (Constructor<?> c : ctors) {
                    Class<?>[] params = c.getParameterTypes();
                    if (params.length == 2 &&
                            params[0] == int.class &&
                            params[1] == int.class) {
                        ctor = c;
                        break;
                    }
                }
            }
            assertNotNull(ctor, "No suitable constructor found for DateType abstract class");
            ctor.setAccessible(true);

            Object abstractInstance;
            if (ctor.getParameterCount() == 0) {
                abstractInstance = ctor.newInstance();
            } else {
                abstractInstance = ctor.newInstance(DateFormat.SHORT, DateFormat.SHORT);
            }

            // Create a Proxy implementing all interfaces of dateTypeClass to override 'from' method
            Class<?>[] interfaces = dateTypeClass.getInterfaces();
            if (interfaces.length == 0) {
                // If no interfaces, just use the abstractInstance directly (anonymous subclass)
                dateTypeInstance = abstractInstance;
            } else {
                dateTypeInstance = Proxy.newProxyInstance(
                        adapterClass.getClassLoader(),
                        interfaces,
                        (proxy, method, args) -> {
                            if ("from".equals(method.getName()) && args != null && args.length == 1 && args[0] instanceof Date) {
                                return args[0]; // identity function for Date
                            }
                            return method.invoke(abstractInstance, args);
                        });
            }
        }

        // Find the constructor with (DateType, int)
        Constructor<?> ctor = null;
        for (Constructor<?> c : adapterClass.getDeclaredConstructors()) {
            Class<?>[] params = c.getParameterTypes();
            if (params.length == 2 && params[1] == int.class && params[0].equals(dateTypeClass)) {
                ctor = c;
                break;
            }
        }
        assertNotNull(ctor, "Expected constructor (DateType, int) not found");
        ctor.setAccessible(true);
        adapterInstance = ctor.newInstance(dateTypeInstance, DateFormat.SHORT);
    }

    @Test
    @Timeout(8000)
    public void testConstructorAddsUSDateFormatAndOthers() throws Exception {
        var dateFormatsField = adapterClass.getDeclaredField("dateFormats");
        dateFormatsField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<DateFormat> dateFormats = (List<DateFormat>) dateFormatsField.get(adapterInstance);

        DateFormat usFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.US);
        String usPattern = ((SimpleDateFormat) usFormat).toPattern();
        assertTrue(dateFormats.stream()
                .filter(df -> df instanceof SimpleDateFormat)
                .anyMatch(df -> ((SimpleDateFormat) df).toPattern().equals(usPattern)));

        if (!Locale.getDefault().equals(Locale.US)) {
            DateFormat defaultLocaleFormat = DateFormat.getDateInstance(DateFormat.SHORT);
            String defaultPattern = ((SimpleDateFormat) defaultLocaleFormat).toPattern();
            assertTrue(dateFormats.stream()
                    .filter(df -> df instanceof SimpleDateFormat)
                    .anyMatch(df -> ((SimpleDateFormat) df).toPattern().equals(defaultPattern)));
        }
    }

    @Test
    @Timeout(8000)
    public void testWrite() throws Exception {
        JsonWriter writer = mock(JsonWriter.class);
        Date now = new Date();

        Method writeMethod = adapterClass.getMethod("write", JsonWriter.class, Date.class);
        writeMethod.invoke(adapterInstance, writer, now);

        verify(writer).value(anyString());
    }

    @Test
    @Timeout(8000)
    public void testWriteNull() throws Exception {
        JsonWriter writer = mock(JsonWriter.class);

        Method writeMethod = adapterClass.getMethod("write", JsonWriter.class, Date.class);
        writeMethod.invoke(adapterInstance, writer, (Date) null);

        verify(writer).nullValue();
    }

    @Test
    @Timeout(8000)
    public void testReadValidDate() throws Exception {
        JsonReader reader = mock(JsonReader.class);

        when(reader.peek()).thenReturn(JsonToken.STRING);
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.US);
        String dateString = df.format(new Date());
        when(reader.nextString()).thenReturn(dateString);

        Method readMethod = adapterClass.getMethod("read", JsonReader.class);
        Object result = readMethod.invoke(adapterInstance, reader);

        assertNotNull(result);
        assertTrue(result instanceof Date);
    }

    @Test
    @Timeout(8000)
    public void testReadNull() throws Exception {
        JsonReader reader = mock(JsonReader.class);
        when(reader.peek()).thenReturn(JsonToken.NULL);
        doNothing().when(reader).nextNull();

        Method readMethod = adapterClass.getMethod("read", JsonReader.class);
        Object result = readMethod.invoke(adapterInstance, reader);

        assertNull(result);
    }

    @Test
    @Timeout(8000)
    public void testReadInvalidDateThrowsJsonSyntaxException() throws Exception {
        JsonReader reader = mock(JsonReader.class);

        when(reader.peek()).thenReturn(JsonToken.STRING);
        when(reader.nextString()).thenReturn("invalid-date");

        Method readMethod = adapterClass.getMethod("read", JsonReader.class);

        Exception ex = assertThrows(Exception.class, () -> {
            try {
                readMethod.invoke(adapterInstance, reader);
            } catch (java.lang.reflect.InvocationTargetException ite) {
                throw ite.getCause();
            }
        });
        assertTrue(ex instanceof JsonSyntaxException);
    }

    @Test
    @Timeout(8000)
    public void testDeserializeToDateValid() throws Exception {
        JsonReader reader = mock(JsonReader.class);
        when(reader.peek()).thenReturn(JsonToken.STRING);
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.US);
        String dateString = df.format(new Date());
        when(reader.nextString()).thenReturn(dateString);

        Method method = adapterClass.getDeclaredMethod("deserializeToDate", JsonReader.class);
        method.setAccessible(true);
        Date date = (Date) method.invoke(adapterInstance, reader);

        assertNotNull(date);
    }

    @Test
    @Timeout(8000)
    public void testDeserializeToDateInvalidThrows() throws Exception {
        JsonReader reader = mock(JsonReader.class);
        when(reader.peek()).thenReturn(JsonToken.STRING);
        when(reader.nextString()).thenReturn("not-a-date");

        Method method = adapterClass.getDeclaredMethod("deserializeToDate", JsonReader.class);
        method.setAccessible(true);

        Exception ex = assertThrows(Exception.class, () -> {
            try {
                method.invoke(adapterInstance, reader);
            } catch (java.lang.reflect.InvocationTargetException ite) {
                throw ite.getCause();
            }
        });
        assertTrue(ex instanceof JsonSyntaxException);
    }

    @Test
    @Timeout(8000)
    public void testToStringContainsClassName() throws Exception {
        Method toStringMethod = adapterClass.getMethod("toString");
        String str = (String) toStringMethod.invoke(adapterInstance);
        assertTrue(str.contains("DefaultDateTypeAdapter"));
    }
}