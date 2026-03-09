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

import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Constructor;

class NumberTypeAdapter_304_4Test {

    private NumberTypeAdapter numberTypeAdapter;
    private JsonWriter jsonWriter;

    @BeforeEach
    void setUp() throws Exception {
        Constructor<NumberTypeAdapter> constructor = NumberTypeAdapter.class.getDeclaredConstructor(com.google.gson.ToNumberStrategy.class);
        constructor.setAccessible(true);
        // Using null for ToNumberStrategy since constructor is private and not used in write()
        numberTypeAdapter = constructor.newInstance((Object) null);
        jsonWriter = Mockito.mock(JsonWriter.class);
    }

    @Test
    @Timeout(8000)
    void write_withNonNullNumber_callsJsonWriterValue() throws IOException {
        Number value = 123;

        numberTypeAdapter.write(jsonWriter, value);

        verify(jsonWriter).value(value);
    }

    @Test
    @Timeout(8000)
    void write_withNullNumber_callsJsonWriterValueWithNull() throws IOException {
        Number value = null;

        numberTypeAdapter.write(jsonWriter, value);

        verify(jsonWriter).value((Number) null);
    }
}