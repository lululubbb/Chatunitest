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

class TypeAdapter_172_2Test {

    private TypeAdapter<String> typeAdapter;

    @BeforeEach
    void setUp() {
        typeAdapter = new TypeAdapter<String>() {
            @Override
            public String read(JsonReader in) throws IOException {
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
    void testReadReturnsString() throws IOException {
        JsonReader jsonReader = new JsonReader(new StringReader("\"hello\""));
        String result = typeAdapter.read(jsonReader);
        assertEquals("hello", result);
    }

    @Test
    @Timeout(8000)
    void testReadReturnsNull() throws IOException {
        JsonReader jsonReader = new JsonReader(new StringReader("null"));
        String result = typeAdapter.read(jsonReader);
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void testReadWithMockedJsonReaderNextString() throws IOException {
        JsonReader jsonReader = mock(JsonReader.class);
        when(jsonReader.peek()).thenReturn(JsonToken.STRING);
        when(jsonReader.nextString()).thenReturn("mocked string");
        String result = typeAdapter.read(jsonReader);
        assertEquals("mocked string", result);
        verify(jsonReader).peek();
        verify(jsonReader).nextString();
    }

    @Test
    @Timeout(8000)
    void testReadWithMockedJsonReaderNull() throws IOException {
        JsonReader jsonReader = mock(JsonReader.class);
        when(jsonReader.peek()).thenReturn(JsonToken.NULL);
        doNothing().when(jsonReader).nextNull();
        String result = typeAdapter.read(jsonReader);
        assertNull(result);
        verify(jsonReader).peek();
        verify(jsonReader).nextNull();
    }

    @Test
    @Timeout(8000)
    void testReadInvokedViaReflection() throws Exception {
        JsonReader jsonReader = new JsonReader(new StringReader("\"reflection\""));
        Method readMethod = TypeAdapter.class.getDeclaredMethod("read", JsonReader.class);
        readMethod.setAccessible(true);
        String result = (String) readMethod.invoke(typeAdapter, jsonReader);
        assertEquals("reflection", result);
    }
}