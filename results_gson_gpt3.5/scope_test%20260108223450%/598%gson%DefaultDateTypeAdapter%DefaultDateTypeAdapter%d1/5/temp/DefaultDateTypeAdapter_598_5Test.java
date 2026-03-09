package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.JavaVersion;
import com.google.gson.internal.PreJava9DateFormatProvider;
import com.google.gson.internal.bind.util.ISO8601Utils;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
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
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

class DefaultDateTypeAdapter_598_5Test {

    private Class<?> adapterClass;
    private Class<?> dateTypeClass;
    private Constructor<?> constructor;
    private Object adapterInstance;
    private DateFormat usDateFormat;

    private Object dateTypeProxy;

    @BeforeEach
    void setUp() throws Exception {
        adapterClass = Class.forName("com.google.gson.internal.bind.DefaultDateTypeAdapter");

        // Find the nested DateType interface/class
        dateTypeClass = null;
        for (Class<?> inner : adapterClass.getDeclaredClasses()) {
            if ("DateType".equals(inner.getSimpleName())) {
                dateTypeClass = inner;
                break;
            }
        }
        assertNotNull(dateTypeClass, "DateType class not found inside DefaultDateTypeAdapter");

        // Create a dynamic proxy implementing DateType interface/class
        if (dateTypeClass.isInterface()) {
            dateTypeProxy = Proxy.newProxyInstance(
                    adapterClass.getClassLoader(),
                    new Class[]{dateTypeClass},
                    new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) {
                            if ("deserialize".equals(method.getName())) {
                                return args[0]; // return the Date passed in
                            } else if ("getDateClass".equals(method.getName())) {
                                return Date.class;
                            }
                            return null;
                        }
                    });
        } else {
            // DateType is a class, create a subclass instance with required methods via Proxy on its interfaces
            Class<?>[] interfaces = dateTypeClass.getInterfaces();
            if (interfaces.length == 0) {
                // No interfaces, create anonymous subclass via reflection
                dateTypeProxy = new Object() {
                    public Date deserialize(Date d) {
                        return d;
                    }
                    public Class<?> getDateClass() {
                        return Date.class;
                    }
                };
            } else {
                dateTypeProxy = Proxy.newProxyInstance(
                        adapterClass.getClassLoader(),
                        interfaces,
                        new InvocationHandler() {
                            @Override
                            public Object invoke(Object proxy, Method method, Object[] args) {
                                if ("deserialize".equals(method.getName())) {
                                    return args[0]; // return the Date passed in
                                } else if ("getDateClass".equals(method.getName())) {
                                    return Date.class;
                                }
                                return null;
                            }
                        });
            }
        }

        // Use the private constructor DefaultDateTypeAdapter(DateType<T> dateType, int style)
        constructor = adapterClass.getDeclaredConstructor(dateTypeClass, int.class);
        constructor.setAccessible(true);

        // If dateTypeProxy is not assignable to dateTypeClass, wrap it with a Proxy implementing dateTypeClass interface
        if (!dateTypeClass.isInstance(dateTypeProxy)) {
            if (dateTypeClass.isInterface()) {
                // Wrap with Proxy implementing dateTypeClass
                dateTypeProxy = Proxy.newProxyInstance(
                        adapterClass.getClassLoader(),
                        new Class[]{dateTypeClass},
                        (proxy, method, args) -> {
                            if ("deserialize".equals(method.getName())) {
                                return args[0];
                            } else if ("getDateClass".equals(method.getName())) {
                                return Date.class;
                            }
                            return null;
                        });
            } else {
                // If class and no interfaces, cannot proxy, try anonymous subclass via reflection (already done above)
                // If still not assignable, fail fast
                if (!dateTypeClass.isInstance(dateTypeProxy)) {
                    throw new IllegalStateException("dateTypeProxy is not assignable to dateTypeClass");
                }
            }
        }

        adapterInstance = constructor.newInstance(dateTypeProxy, DateFormat.SHORT);

        usDateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.US);
    }

    @Test
    @Timeout(8000)
    void testConstructor_addsCorrectDateFormats() throws Exception {
        var dateFormatsField = adapterClass.getDeclaredField("dateFormats");
        dateFormatsField.setAccessible(true);

        @SuppressWarnings("unchecked")
        var dateFormats = (List<DateFormat>) dateFormatsField.get(adapterInstance);

        assertNotNull(dateFormats);
        assertTrue(dateFormats.size() >= 1);
        boolean containsUS = dateFormats.stream().anyMatch(df -> df.equals(usDateFormat));
        assertTrue(containsUS);

        if (!Locale.getDefault().equals(Locale.US)) {
            assertTrue(dateFormats.size() >= 2);
        }
    }

    @Test
    @Timeout(8000)
    void testWrite_andRead() throws Exception {
        JsonWriter mockWriter = mock(JsonWriter.class);
        Date now = new Date();

        Method writeMethod = adapterClass.getMethod("write", JsonWriter.class, Date.class);
        writeMethod.invoke(adapterInstance, mockWriter, now);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(mockWriter).value(captor.capture());
        String writtenValue = captor.getValue();
        assertNotNull(writtenValue);
        assertFalse(writtenValue.isEmpty());

        JsonReader mockReader = mock(JsonReader.class);
        when(mockReader.peek()).thenReturn(JsonToken.STRING);
        when(mockReader.nextString()).thenReturn(writtenValue);

        Method readMethod = adapterClass.getMethod("read", JsonReader.class);
        Object readDate = readMethod.invoke(adapterInstance, mockReader);

        assertNotNull(readDate);
        assertTrue(readDate instanceof Date);
    }

    @Test
    @Timeout(8000)
    void testRead_nullJsonToken() throws Exception {
        JsonReader mockReader = mock(JsonReader.class);
        when(mockReader.peek()).thenReturn(JsonToken.NULL);
        doNothing().when(mockReader).nextNull();

        Method readMethod = adapterClass.getMethod("read", JsonReader.class);
        Object result = readMethod.invoke(adapterInstance, mockReader);

        assertNull(result);
        verify(mockReader).nextNull();
    }

    @Test
    @Timeout(8000)
    void testDeserializeToDate_validAndInvalid() throws Exception {
        Method deserializeMethod = adapterClass.getDeclaredMethod("deserializeToDate", JsonReader.class);
        deserializeMethod.setAccessible(true);

        JsonReader mockReader = mock(JsonReader.class);

        String validDateStr = usDateFormat.format(new Date());
        when(mockReader.nextString()).thenReturn(validDateStr);

        Object dateObj = deserializeMethod.invoke(adapterInstance, mockReader);
        assertNotNull(dateObj);
        assertTrue(dateObj instanceof Date);

        when(mockReader.nextString()).thenReturn("invalid-date-string");

        try {
            deserializeMethod.invoke(adapterInstance, mockReader);
            fail("Expected InvocationTargetException wrapping JsonSyntaxException");
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            assertNotNull(cause);
            assertEquals("com.google.gson.JsonSyntaxException", cause.getClass().getName());
        }
    }

    @Test
    @Timeout(8000)
    void testToString_containsSimpleName() throws Exception {
        Method toStringMethod = adapterClass.getDeclaredMethod("toString");
        Object result = toStringMethod.invoke(adapterInstance);
        assertNotNull(result);
        String str = result.toString();
        assertTrue(str.contains("DefaultDateTypeAdapter"));
    }
}