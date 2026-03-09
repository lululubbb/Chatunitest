package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.gson.stream.JsonWriter;
import com.google.gson.JsonIOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.StringWriter;

class TypeAdapter_170_3Test {

    private TypeAdapter<String> typeAdapter;

    @BeforeEach
    void setUp() {
        typeAdapter = new TypeAdapter<String>() {
            @Override
            public void write(JsonWriter out, String value) throws IOException {
                if (value == null) {
                    out.nullValue();
                } else if ("throw".equals(value)) {
                    throw new IOException("forced exception");
                } else {
                    out.value(value);
                }
            }

            @Override
            public String read(com.google.gson.stream.JsonReader in) {
                return null;
            }
        };
    }

    @Test
    @Timeout(8000)
    void toJson_shouldReturnJsonString() {
        String json = typeAdapter.toJson("hello");
        assertEquals("\"hello\"", json);
    }

    @Test
    @Timeout(8000)
    void toJson_shouldReturnJsonNullForNullValue() {
        String json = typeAdapter.toJson(null);
        assertEquals("null", json);
    }

    @Test
    @Timeout(8000)
    void toJson_shouldThrowJsonIOException_whenWriteThrowsIOException() {
        JsonIOException ex = assertThrows(JsonIOException.class, () -> typeAdapter.toJson("throw"));
        assertEquals(IOException.class, ex.getCause().getClass());
        assertEquals("forced exception", ex.getCause().getMessage());
    }
}