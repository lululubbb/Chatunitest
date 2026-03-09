package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.stream.JsonToken;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TypeAdapterToJsonTreeTest {

    private TypeAdapter<String> typeAdapter;

    @BeforeEach
    void setUp() {
        typeAdapter = new TypeAdapter<>() {
            @Override
            public void write(JsonWriter out, String value) throws IOException {
                if (value == null) {
                    out.nullValue();
                } else {
                    out.value(value);
                }
            }

            @Override
            public String read(JsonReader in) {
                return null;
            }
        };
    }

    @Test
    @Timeout(8000)
    void toJsonTree_shouldReturnJsonElement_whenValueIsNonNull() {
        String testValue = "test";

        JsonElement jsonElement = typeAdapter.toJsonTree(testValue);

        assertNotNull(jsonElement);
        assertTrue(jsonElement.isJsonPrimitive());
        assertEquals(testValue, jsonElement.getAsString());
    }

    @Test
    @Timeout(8000)
    void toJsonTree_shouldReturnJsonNull_whenValueIsNull() {
        String testValue = null;

        JsonElement jsonElement = typeAdapter.toJsonTree(testValue);

        assertNotNull(jsonElement);
        assertTrue(jsonElement.isJsonNull());
    }

    @Test
    @Timeout(8000)
    void toJsonTree_shouldThrowJsonIOException_whenWriteThrowsIOException() {
        TypeAdapter<String> throwingAdapter = new TypeAdapter<>() {
            @Override
            public void write(JsonWriter out, String value) throws IOException {
                throw new IOException("forced");
            }

            @Override
            public String read(JsonReader in) {
                return null;
            }
        };

        JsonIOException thrown = assertThrows(JsonIOException.class, () -> {
            throwingAdapter.toJsonTree("any");
        });
        assertEquals("java.io.IOException: forced", thrown.getCause().toString());
    }
}