package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.JavaVersion;
import com.google.gson.internal.PreJava9DateFormatProvider;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.stream.JsonToken;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DefaultDateTypeAdapter_600_1Test {

    private DefaultDateTypeAdapter<Date> adapter;
    private JsonWriter jsonWriter;
    private StringWriter stringWriter;

    @BeforeEach
    public void setUp() throws Exception {
        // Instead of Proxy, instantiate the DateType enum constant directly
        Class<?> dateTypeClass = null;
        for (Class<?> innerClass : DefaultDateTypeAdapter.class.getDeclaredClasses()) {
            if ("DateType".equals(innerClass.getSimpleName())) {
                dateTypeClass = innerClass;
                break;
            }
        }
        assertNotNull(dateTypeClass);

        // DateType is an enum, get the enum constant for Date
        Object dateType = null;
        Object[] enumConstants = dateTypeClass.getEnumConstants();
        for (Object constant : enumConstants) {
            // The enum constant for Date should have getDateClass() == Date.class
            var method = dateTypeClass.getDeclaredMethod("getDateClass");
            Object result = method.invoke(constant);
            if (Date.class.equals(result)) {
                dateType = constant;
                break;
            }
        }
        assertNotNull(dateType);

        Constructor<DefaultDateTypeAdapter> constructor =
                DefaultDateTypeAdapter.class.getDeclaredConstructor(dateTypeClass, String.class);
        constructor.setAccessible(true);
        adapter = (DefaultDateTypeAdapter<Date>) constructor.newInstance(dateType, "yyyy-MM-dd'T'HH:mm:ss'Z'");

        stringWriter = new StringWriter();
        jsonWriter = new JsonWriter(stringWriter);
    }

    @Test
    @Timeout(8000)
    public void testWrite_NullValue_CallsNullValueOnJsonWriter() throws IOException {
        JsonWriter spyWriter = spy(jsonWriter);

        adapter.write(spyWriter, null);

        verify(spyWriter).nullValue();
        verify(spyWriter, never()).value(anyString());
        assertEquals("", stringWriter.toString());
    }

    @Test
    @Timeout(8000)
    public void testWrite_ValidDate_FormatsAndWritesValue() throws Exception {
        Date date = new Date(0L); // Epoch

        var dateFormatsField = DefaultDateTypeAdapter.class.getDeclaredField("dateFormats");
        dateFormatsField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<DateFormat> dateFormats = (List<DateFormat>) dateFormatsField.get(adapter);

        dateFormats.clear();
        DateFormat fixedFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        dateFormats.add(fixedFormat);

        JsonWriter spyWriter = spy(jsonWriter);

        adapter.write(spyWriter, date);

        String expectedFormatted = fixedFormat.format(date);

        InOrder inOrder = inOrder(spyWriter);
        inOrder.verify(spyWriter).value(expectedFormatted);

        verify(spyWriter, never()).nullValue();

        String output = stringWriter.toString();
        assertTrue(output.contains(expectedFormatted));
    }
}