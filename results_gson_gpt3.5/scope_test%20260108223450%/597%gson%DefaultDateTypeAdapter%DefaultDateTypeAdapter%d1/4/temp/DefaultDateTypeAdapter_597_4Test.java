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

import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DefaultDateTypeAdapter_597_4Test {

    private Class<?> adapterClass;
    private Class<?> dateTypeClass;
    private Object dateTypeInstance;

    @BeforeEach
    public void setUp() throws Exception {
        adapterClass = Class.forName("com.google.gson.internal.bind.DefaultDateTypeAdapter");
        // Instead of getting DateType interface (which does not exist), get the nested class DateType
        dateTypeClass = Class.forName("com.google.gson.internal.bind.DefaultDateTypeAdapter$DateType");

        // DateType is not an enum, so we cannot use Enum.valueOf.
        // Instead, get the static field DateType.DATE
        dateTypeInstance = dateTypeClass.getField("DATE").get(null);
    }

    private Object createAdapterWithPattern(String pattern) throws Exception {
        Constructor<?> constructor = adapterClass.getDeclaredConstructor(dateTypeClass, String.class);
        constructor.setAccessible(true);
        return constructor.newInstance(dateTypeInstance, pattern);
    }

    @Test
    @Timeout(8000)
    public void testWriteAndRead() throws Exception {
        Object adapter = createAdapterWithPattern("yyyy-MM-dd");

        // Prepare JsonWriter
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);

        Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse("2023-06-01");

        // Invoke write method
        Method writeMethod = adapterClass.getMethod("write", JsonWriter.class, Date.class);
        writeMethod.invoke(adapter, jsonWriter, date);
        jsonWriter.flush();

        String jsonOutput = stringWriter.toString();
        assertNotNull(jsonOutput);
        assertTrue(jsonOutput.contains("2023-06-01"));

        // Prepare JsonReader with the output string
        JsonReader jsonReader = new JsonReader(new StringReader(jsonOutput));

        // Invoke read method
        Method readMethod = adapterClass.getMethod("read", JsonReader.class);
        Object result = readMethod.invoke(adapter, jsonReader);

        assertNotNull(result);
        assertTrue(result instanceof Date);
        assertEquals(date, result);
    }

    @Test
    @Timeout(8000)
    public void testReadNull() throws Exception {
        Object adapter = createAdapterWithPattern("yyyy-MM-dd");

        JsonReader jsonReader = new JsonReader(new StringReader("null"));

        Method readMethod = adapterClass.getMethod("read", JsonReader.class);
        Object result = readMethod.invoke(adapter, jsonReader);

        assertNull(result);
    }

    @Test
    @Timeout(8000)
    public void testDeserializeToDateWithValidDate() throws Exception {
        Object adapter = createAdapterWithPattern("yyyy-MM-dd");

        JsonReader jsonReader = mock(JsonReader.class);
        when(jsonReader.peek()).thenReturn(JsonToken.STRING);
        when(jsonReader.nextString()).thenReturn("2023-06-01");

        Method method = adapterClass.getDeclaredMethod("deserializeToDate", JsonReader.class);
        method.setAccessible(true);
        Date date = (Date) method.invoke(adapter, jsonReader);

        assertNotNull(date);
        assertEquals("2023-06-01", new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(date));
    }

    @Test
    @Timeout(8000)
    public void testDeserializeToDateWithNullToken() throws Exception {
        Object adapter = createAdapterWithPattern("yyyy-MM-dd");

        JsonReader jsonReader = mock(JsonReader.class);
        when(jsonReader.peek()).thenReturn(JsonToken.NULL);
        doAnswer(invocation -> {
            // Simulate nextNull() consuming the null token
            when(jsonReader.peek()).thenReturn(JsonToken.END_DOCUMENT);
            return null;
        }).when(jsonReader).nextNull();

        Method method = adapterClass.getDeclaredMethod("deserializeToDate", JsonReader.class);
        method.setAccessible(true);
        // Call method.invoke inside try-catch to catch InvocationTargetException and unwrap cause
        try {
            Date date = (Date) method.invoke(adapter, jsonReader);
            assertNull(date);
        } catch (java.lang.reflect.InvocationTargetException e) {
            // If cause is NullPointerException due to internal code, fail test
            fail("InvocationTargetException thrown: " + e.getCause());
        }

        verify(jsonReader).nextNull();
    }

    @Test
    @Timeout(8000)
    public void testDeserializeToDateWithInvalidStringThrows() throws Exception {
        Object adapter = createAdapterWithPattern("yyyy-MM-dd");

        JsonReader jsonReader = mock(JsonReader.class);
        when(jsonReader.peek()).thenReturn(JsonToken.STRING);
        when(jsonReader.nextString()).thenReturn("invalid-date");

        Method method = adapterClass.getDeclaredMethod("deserializeToDate", JsonReader.class);
        method.setAccessible(true);

        Exception ex = assertThrows(Exception.class, () -> method.invoke(adapter, jsonReader));
        // InvocationTargetException is expected, check cause
        Throwable cause = ex.getCause();
        assertNotNull(cause);
        assertTrue(cause instanceof JsonSyntaxException || (cause.getCause() != null && cause.getCause() instanceof java.text.ParseException));
    }

    @Test
    @Timeout(8000)
    public void testToStringContainsPattern() throws Exception {
        Object adapter = createAdapterWithPattern("yyyy-MM-dd");

        Method toStringMethod = adapterClass.getMethod("toString");
        String str = (String) toStringMethod.invoke(adapter);

        assertNotNull(str);
        assertTrue(str.contains("yyyy-MM-dd"));
    }
}