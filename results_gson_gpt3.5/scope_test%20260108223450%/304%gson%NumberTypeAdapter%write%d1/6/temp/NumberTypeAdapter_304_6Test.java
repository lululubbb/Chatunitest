package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.ToNumberStrategy;
import com.google.gson.ToNumberPolicy;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import static org.mockito.Mockito.*;

import com.google.gson.internal.bind.NumberTypeAdapter;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

public class NumberTypeAdapter_304_6Test {

    private NumberTypeAdapter numberTypeAdapter;
    private JsonWriter jsonWriter;

    @BeforeEach
    public void setUp() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        // Use reflection to create NumberTypeAdapter instance with null ToNumberStrategy since constructor is private
        Constructor<NumberTypeAdapter> constructor = NumberTypeAdapter.class.getDeclaredConstructor(com.google.gson.ToNumberStrategy.class);
        constructor.setAccessible(true);
        numberTypeAdapter = constructor.newInstance((Object) null);

        jsonWriter = mock(JsonWriter.class);
    }

    @Test
    @Timeout(8000)
    public void testWrite_withIntegerValue() throws IOException {
        Number value = Integer.valueOf(123);

        when(jsonWriter.value(value)).thenReturn(jsonWriter);

        numberTypeAdapter.write(jsonWriter, value);

        verify(jsonWriter).value(value);
    }

    @Test
    @Timeout(8000)
    public void testWrite_withDoubleValue() throws IOException {
        Number value = Double.valueOf(123.456);

        when(jsonWriter.value(value)).thenReturn(jsonWriter);

        numberTypeAdapter.write(jsonWriter, value);

        verify(jsonWriter).value(value);
    }

    @Test
    @Timeout(8000)
    public void testWrite_withNullValue() throws IOException {
        Number value = null;

        // The JsonWriter.value(Number) method is expected to handle nulls without throwing exceptions.
        when(jsonWriter.value(value)).thenReturn(jsonWriter);

        numberTypeAdapter.write(jsonWriter, value);

        verify(jsonWriter).value(value);
    }

    @Test
    @Timeout(8000)
    public void testWrite_throwsIOException() throws IOException {
        Number value = Integer.valueOf(1);

        doThrow(new IOException("Test IO Exception")).when(jsonWriter).value(value);

        IOException exception = assertThrows(IOException.class, () -> numberTypeAdapter.write(jsonWriter, value));

        assertEquals("Test IO Exception", exception.getMessage());
    }
}