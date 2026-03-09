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
import org.mockito.InOrder;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Method;

public class NumberTypeAdapter_304_2Test {

    private NumberTypeAdapter numberTypeAdapter;
    private JsonWriter jsonWriter;

    @BeforeEach
    public void setUp() {
        // Using reflection to create instance since constructor is private
        try {
            var constructor = NumberTypeAdapter.class.getDeclaredConstructor(com.google.gson.ToNumberStrategy.class);
            constructor.setAccessible(true);
            numberTypeAdapter = (NumberTypeAdapter) constructor.newInstance((Object) null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        jsonWriter = Mockito.mock(JsonWriter.class);
    }

    @Test
    @Timeout(8000)
    public void testWrite_withIntegerValue() throws IOException {
        Integer value = 123;

        numberTypeAdapter.write(jsonWriter, value);

        verify(jsonWriter).value(value);
        verifyNoMoreInteractions(jsonWriter);
    }

    @Test
    @Timeout(8000)
    public void testWrite_withDoubleValue() throws IOException {
        Double value = 45.67;

        numberTypeAdapter.write(jsonWriter, value);

        verify(jsonWriter).value(value);
        verifyNoMoreInteractions(jsonWriter);
    }

    @Test
    @Timeout(8000)
    public void testWrite_withNullValue() throws IOException {
        Number value = null;

        numberTypeAdapter.write(jsonWriter, value);

        verify(jsonWriter).value((Number) null);
        verifyNoMoreInteractions(jsonWriter);
    }

    @Test
    @Timeout(8000)
    public void testWrite_withLongValue() throws IOException {
        Long value = 123456789L;

        numberTypeAdapter.write(jsonWriter, value);

        verify(jsonWriter).value(value);
        verifyNoMoreInteractions(jsonWriter);
    }
}