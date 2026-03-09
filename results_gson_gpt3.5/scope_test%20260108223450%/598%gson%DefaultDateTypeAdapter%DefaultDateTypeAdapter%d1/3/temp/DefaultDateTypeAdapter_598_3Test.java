package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.JavaVersion;
import com.google.gson.internal.PreJava9DateFormatProvider;
import com.google.gson.internal.bind.util.ISO8601Utils;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DefaultDateTypeAdapter_598_3Test {

    private Class<?> clazz;
    private Object adapterInstance;
    private DateType<Date> dateType;

    interface DateType<T extends Date> {
        T deserialize(Date date);
        Date serialize(T t);
    }

    @BeforeEach
    void setUp() throws Exception {
        clazz = Class.forName("com.google.gson.internal.bind.DefaultDateTypeAdapter");

        dateType = new DateType<Date>() {
            @Override
            public Date deserialize(Date date) {
                return date;
            }
            @Override
            public Date serialize(Date t) {
                return t;
            }
        };

        Constructor<?> ctor = clazz.getDeclaredConstructor(DateType.class, int.class);
        ctor.setAccessible(true);
        adapterInstance = ctor.newInstance(dateType, DateFormat.SHORT);
    }

    @Test
    @Timeout(8000)
    void testConstructor_addsDateFormats() throws Exception {
        // The constructor with int style adds at least one DateFormat
        var dateFormatsField = clazz.getDeclaredField("dateFormats");
        dateFormatsField.setAccessible(true);
        @SuppressWarnings("unchecked")
        var dateFormats = (java.util.List<DateFormat>) dateFormatsField.get(adapterInstance);
        assertFalse(dateFormats.isEmpty());
        assertTrue(dateFormats.get(0) instanceof DateFormat);
    }

    @Test
    @Timeout(8000)
    void testWrite_andRead_withValidDate() throws Exception {
        JsonWriter jsonWriter = mock(JsonWriter.class);
        Date now = new Date();

        Method writeMethod = clazz.getMethod("write", JsonWriter.class, Date.class);
        writeMethod.invoke(adapterInstance, jsonWriter, now);

        JsonReader jsonReader = mock(JsonReader.class);
        when(jsonReader.peek()).thenReturn(JsonToken.STRING);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String formatted = df.format(now);
        when(jsonReader.nextString()).thenReturn(formatted);

        Method readMethod = clazz.getMethod("read", JsonReader.class);
        Date result = (Date) readMethod.invoke(adapterInstance, jsonReader);

        assertNotNull(result);
    }

    @Test
    @Timeout(8000)
    void testWrite_withNullValue() throws Exception {
        JsonWriter jsonWriter = mock(JsonWriter.class);

        Method writeMethod = clazz.getMethod("write", JsonWriter.class, Date.class);
        writeMethod.invoke(adapterInstance, jsonWriter, (Date) null);

        verify(jsonWriter).nullValue();
    }

    @Test
    @Timeout(8000)
    void testRead_withNullToken() throws Exception {
        JsonReader jsonReader = mock(JsonReader.class);
        when(jsonReader.peek()).thenReturn(JsonToken.NULL);
        doNothing().when(jsonReader).nextNull();

        Method readMethod = clazz.getMethod("read", JsonReader.class);
        Date result = (Date) readMethod.invoke(adapterInstance, jsonReader);

        assertNull(result);
        verify(jsonReader).nextNull();
    }

    @Test
    @Timeout(8000)
    void testDeserializeToDate_withValidString() throws Exception {
        JsonReader jsonReader = mock(JsonReader.class);
        when(jsonReader.nextString()).thenReturn("2023-01-01T00:00:00Z");

        Method deserializeMethod = clazz.getDeclaredMethod("deserializeToDate", JsonReader.class);
        deserializeMethod.setAccessible(true);
        Date date = (Date) deserializeMethod.invoke(adapterInstance, jsonReader);

        assertNotNull(date);
    }

    @Test
    @Timeout(8000)
    void testDeserializeToDate_withParseException() throws Exception {
        JsonReader jsonReader = mock(JsonReader.class);
        when(jsonReader.nextString()).thenReturn("invalid-date");

        Method deserializeMethod = clazz.getDeclaredMethod("deserializeToDate", JsonReader.class);
        deserializeMethod.setAccessible(true);

        Exception ex = assertThrows(Exception.class, () -> {
            deserializeMethod.invoke(adapterInstance, jsonReader);
        });
        assertTrue(ex.getCause() instanceof com.google.gson.JsonSyntaxException);
    }

    @Test
    @Timeout(8000)
    void testToString_containsClassName() throws Exception {
        Method toStringMethod = clazz.getMethod("toString");
        String str = (String) toStringMethod.invoke(adapterInstance);
        assertTrue(str.contains("DefaultDateTypeAdapter"));
    }
}