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
import java.lang.reflect.Method;
import java.util.Date;

class DefaultDateTypeAdapter_601_5Test {

    private DefaultDateTypeAdapter<Date> adapter;
    private DefaultDateTypeAdapter.DateType<Date> dateType;

    @BeforeEach
    void setUp() throws Exception {
        dateType = mock(DefaultDateTypeAdapter.DateType.class);
        // Use reflection to access the private constructor with int style (e.g. DateFormat.SHORT)
        Constructor<DefaultDateTypeAdapter> constructor = DefaultDateTypeAdapter.class.getDeclaredConstructor(DefaultDateTypeAdapter.DateType.class, int.class);
        constructor.setAccessible(true);
        adapter = (DefaultDateTypeAdapter<Date>) constructor.newInstance(dateType, 0); // 0 is DateFormat.SHORT
    }

    @Test
    @Timeout(8000)
    void read_shouldReturnNull_whenJsonTokenIsNull() throws IOException {
        JsonReader in = mock(JsonReader.class);
        when(in.peek()).thenReturn(JsonToken.NULL);

        Date result = adapter.read(in);

        verify(in).nextNull();
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void read_shouldReturnDeserializedDate() throws Exception {
        JsonReader in = mock(JsonReader.class);
        when(in.peek()).thenReturn(JsonToken.STRING);
        when(in.nextString()).thenReturn("2026-01-09");

        Date expectedDate = new Date();

        // Spy adapter without stubbing private method directly
        var spyAdapter = Mockito.spy(adapter);

        // Instead of stubbing private method directly, invoke read() and mock dateType.deserialize
        // We use reflection to replace deserializeToDate to return expectedDate by invoking the real method is complicated,
        // so instead we mock dateType.deserialize to return expectedDate when called with any Date.

        when(dateType.deserialize(any(Date.class))).thenReturn(expectedDate);

        Date actual = spyAdapter.read(in);

        verify(dateType).deserialize(any(Date.class));
        assertEquals(expectedDate, actual);
    }
}