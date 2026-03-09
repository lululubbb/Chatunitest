package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.stream.JsonWriter;
import java.io.Reader;
import java.io.StringReader;
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

class TypeAdapter_172_4Test {

    private TypeAdapter<String> typeAdapter;
    private JsonReader jsonReader;

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
            public void write(com.google.gson.stream.JsonWriter out, String value) throws IOException {
                if (value == null) {
                    out.nullValue();
                } else {
                    out.value(value);
                }
            }
        };
        jsonReader = mock(JsonReader.class);
    }

    @Test
    @Timeout(8000)
    void read_shouldReturnNullWhenJsonTokenIsNull() throws IOException {
        when(jsonReader.peek()).thenReturn(JsonToken.NULL);
        doNothing().when(jsonReader).nextNull();

        String result = typeAdapter.read(jsonReader);

        verify(jsonReader).peek();
        verify(jsonReader).nextNull();
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void read_shouldReturnStringWhenJsonTokenIsString() throws IOException {
        when(jsonReader.peek()).thenReturn(JsonToken.STRING);
        when(jsonReader.nextString()).thenReturn("testValue");

        String result = typeAdapter.read(jsonReader);

        verify(jsonReader).peek();
        verify(jsonReader).nextString();
        assertEquals("testValue", result);
    }

    @Test
    @Timeout(8000)
    void read_shouldThrowIOExceptionWhenJsonReaderThrows() throws IOException {
        when(jsonReader.peek()).thenThrow(new IOException("read error"));

        IOException thrown = assertThrows(IOException.class, () -> typeAdapter.read(jsonReader));

        assertEquals("read error", thrown.getMessage());
    }
}