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
import org.mockito.Mockito;

import java.io.IOException;

public class NumberTypeAdapter_304_1Test {

    private NumberTypeAdapter numberTypeAdapter;
    private JsonWriter jsonWriter;

    @BeforeEach
    public void setUp() {
        // Since the constructor is private, use reflection to create an instance
        try {
            var constructor = NumberTypeAdapter.class.getDeclaredConstructor(com.google.gson.ToNumberStrategy.class);
            constructor.setAccessible(true);
            numberTypeAdapter = constructor.newInstance((com.google.gson.ToNumberStrategy) null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        jsonWriter = Mockito.mock(JsonWriter.class);
    }

    @Test
    @Timeout(8000)
    public void write_shouldCallJsonWriterValue_withNonNullNumber() throws IOException {
        Number testNumber = 123;

        numberTypeAdapter.write(jsonWriter, testNumber);

        verify(jsonWriter).value(testNumber);
    }

    @Test
    @Timeout(8000)
    public void write_shouldCallJsonWriterValue_withNullValue() throws IOException {
        Number testNumber = null;

        numberTypeAdapter.write(jsonWriter, testNumber);

        verify(jsonWriter).value((Number) null);
    }
}