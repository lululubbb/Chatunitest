package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.JavaVersion;
import com.google.gson.internal.PreJava9DateFormatProvider;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.bind.DateTypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;

public class DateTypeAdapter_294_4Test {

    private DateTypeAdapter dateTypeAdapter;

    @BeforeEach
    public void setUp() {
        dateTypeAdapter = new DateTypeAdapter();
    }

    @Test
    @Timeout(8000)
    public void testRead_NullToken_ReturnsNull() throws IOException {
        JsonReader jsonReader = mock(JsonReader.class);
        when(jsonReader.peek()).thenReturn(JsonToken.NULL);

        // nextNull() should be called
        doNothing().when(jsonReader).nextNull();

        Date result = dateTypeAdapter.read(jsonReader);

        verify(jsonReader).peek();
        verify(jsonReader).nextNull();
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    public void testRead_ValidDateToken_ReturnsDate() throws Exception {
        JsonReader jsonReader = mock(JsonReader.class);
        when(jsonReader.peek()).thenReturn(JsonToken.STRING);
        when(jsonReader.nextString()).thenReturn("Jan 1, 1970 00:00:00 AM");

        // Create a spy on dateTypeAdapter
        DateTypeAdapter spyAdapter = Mockito.spy(dateTypeAdapter);

        // Use reflection to get the private method
        Method deserializeToDateMethod = DateTypeAdapter.class.getDeclaredMethod("deserializeToDate", JsonReader.class);
        deserializeToDateMethod.setAccessible(true);

        // Invoke deserializeToDate directly
        Date result = (Date) deserializeToDateMethod.invoke(spyAdapter, jsonReader);

        assertNotNull(result);
        assertEquals(Date.class, result.getClass());

        // Now test read method returns the same as deserializeToDate
        Date readResult = spyAdapter.read(jsonReader);
        assertNotNull(readResult);
        assertEquals(Date.class, readResult.getClass());

        verify(jsonReader, atLeastOnce()).peek();
        verify(jsonReader, atLeastOnce()).nextString();
    }
}