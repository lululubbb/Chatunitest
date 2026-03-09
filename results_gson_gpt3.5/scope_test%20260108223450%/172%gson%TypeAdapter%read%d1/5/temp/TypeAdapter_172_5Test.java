package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.stream.JsonWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Method;

class TypeAdapter_172_5Test {

    private TypeAdapter<String> typeAdapter;

    @BeforeEach
    void setUp() {
        // Create a concrete anonymous subclass for testing
        typeAdapter = new TypeAdapter<String>() {
            @Override
            public String read(JsonReader in) throws IOException {
                // simple implementation for testing
                if (in.peek() == JsonToken.NULL) {
                    in.nextNull();
                    return null;
                }
                return in.nextString();
            }

            @Override
            public void write(JsonWriter out, String value) throws IOException {
                if (value == null) {
                    out.nullValue();
                } else {
                    out.value(value);
                }
            }
        };
    }

    @Test
    @Timeout(8000)
    void testRead_withNonNullString() throws Exception {
        String json = "\"hello\"";
        JsonReader jsonReader = new JsonReader(new StringReader(json));
        jsonReader.setLenient(true);

        String result = typeAdapter.read(jsonReader);

        assertEquals("hello", result);
    }

    @Test
    @Timeout(8000)
    void testRead_withNullValue() throws Exception {
        String json = "null";
        JsonReader jsonReader = new JsonReader(new StringReader(json));
        jsonReader.setLenient(true);

        String result = typeAdapter.read(jsonReader);

        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void testRead_throwsIOException() throws Exception {
        JsonReader mockReader = mock(JsonReader.class);
        when(mockReader.peek()).thenThrow(new IOException("mock io exception"));

        IOException thrown = assertThrows(IOException.class, () -> typeAdapter.read(mockReader));
        assertEquals("mock io exception", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testRead_reflectionInvocation() throws Exception {
        Method readMethod = TypeAdapter.class.getDeclaredMethod("read", JsonReader.class);
        readMethod.setAccessible(true);

        JsonReader jsonReader = new JsonReader(new StringReader("\"reflection\""));
        jsonReader.setLenient(true);

        // Use the anonymous subclass instance
        Object result = readMethod.invoke(typeAdapter, jsonReader);

        assertEquals("reflection", result);
    }
}