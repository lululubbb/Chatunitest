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
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Date;

public class DefaultDateTypeAdapter_601_1Test {

    private DefaultDateTypeAdapter<Date> adapter;
    private DefaultDateTypeAdapter.DateType<Date> dateTypeMock;

    @BeforeEach
    public void setUp() throws Exception {
        dateTypeMock = mock(DefaultDateTypeAdapter.DateType.class);

        // Use reflection to get the private constructor with (DateType<T>, String)
        Class<DefaultDateTypeAdapter> clazz = DefaultDateTypeAdapter.class;
        Constructor<DefaultDateTypeAdapter> constructor = clazz.getDeclaredConstructor(DefaultDateTypeAdapter.DateType.class, String.class);
        constructor.setAccessible(true);

        adapter = (DefaultDateTypeAdapter<Date>) constructor.newInstance(dateTypeMock, "yyyy-MM-dd");

        // Mock dateTypeMock.deserialize to return the input date directly
        when(dateTypeMock.deserialize(any(Date.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    @Timeout(8000)
    public void testRead_NullToken_ReturnsNull() throws IOException {
        JsonReader in = mock(JsonReader.class);
        when(in.peek()).thenReturn(JsonToken.NULL);

        Date result = adapter.read(in);

        verify(in).nextNull();
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    public void testRead_ValidDate_DeserializeCalled() throws IOException {
        JsonReader in = mock(JsonReader.class);

        // Prepare the JsonReader mock to return a valid date string when nextString() is called
        when(in.peek()).thenReturn(JsonToken.STRING);
        when(in.nextString()).thenReturn("2024-06-01");

        // Call read()
        Date result = adapter.read(in);

        // Verify that dateTypeMock.deserialize was called with a non-null Date
        verify(dateTypeMock).deserialize(any(Date.class));
        assertNotNull(result);
    }
}