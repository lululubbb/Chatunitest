package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.JavaVersion;
import com.google.gson.internal.PreJava9DateFormatProvider;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.stream.JsonWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Date;

class DefaultDateTypeAdapter_601_2Test {

    private DefaultDateTypeAdapter<Date> adapter;
    private DefaultDateTypeAdapter.DateType<Date> dateType;

    @BeforeEach
    void setUp() throws Exception {
        dateType = mock(DefaultDateTypeAdapter.DateType.class);

        // Use reflection to invoke the private constructor
        Constructor<DefaultDateTypeAdapter> constructor = DefaultDateTypeAdapter.class.getDeclaredConstructor(DefaultDateTypeAdapter.DateType.class, String.class);
        constructor.setAccessible(true);
        adapter = (DefaultDateTypeAdapter<Date>) constructor.newInstance(dateType, "yyyy-MM-dd");
    }

    @Test
    @Timeout(8000)
    void testRead_NullToken() throws IOException {
        JsonReader jsonReader = mock(JsonReader.class);
        when(jsonReader.peek()).thenReturn(JsonToken.NULL);

        doNothing().when(jsonReader).nextNull();

        Date result = adapter.read(jsonReader);

        verify(jsonReader).peek();
        verify(jsonReader).nextNull();
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void testRead_ValidDate() throws Exception {
        JsonReader jsonReader = mock(JsonReader.class);
        when(jsonReader.peek()).thenReturn(JsonToken.STRING);
        when(jsonReader.nextString()).thenReturn("2023-01-01");

        // Use reflection to access private method deserializeToDate
        Method method = DefaultDateTypeAdapter.class.getDeclaredMethod("deserializeToDate", JsonReader.class);
        method.setAccessible(true);

        // Invoke deserializeToDate on a *new* JsonReader mock to avoid consuming nextString calls twice
        JsonReader jsonReaderForDeserialize = mock(JsonReader.class);
        when(jsonReaderForDeserialize.nextString()).thenReturn("2023-01-01");
        Date deserialized = (Date) method.invoke(adapter, jsonReaderForDeserialize);

        // Mock dateType.deserialize to return deserialized date
        when(dateType.deserialize(deserialized)).thenReturn(deserialized);

        // Call read() directly on adapter
        Date result = adapter.read(jsonReader);

        verify(jsonReader).peek();
        verify(jsonReader).nextString();
        verify(dateType).deserialize(deserialized);
        assertEquals(deserialized, result);
    }
}