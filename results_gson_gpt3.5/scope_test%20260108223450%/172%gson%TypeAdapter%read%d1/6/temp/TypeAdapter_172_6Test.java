package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.stream.JsonWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TypeAdapter_172_6Test {

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
            public void write(com.google.gson.stream.JsonWriter out, String value) {
                // No-op for this test
            }
        };
        jsonReader = mock(JsonReader.class);
    }

    @Test
    @Timeout(8000)
    void read_shouldReturnNull_whenJsonTokenIsNull() throws IOException {
        when(jsonReader.peek()).thenReturn(JsonToken.NULL);
        doNothing().when(jsonReader).nextNull();

        String result = typeAdapter.read(jsonReader);

        assertNull(result);
        verify(jsonReader).peek();
        verify(jsonReader).nextNull();
        verifyNoMoreInteractions(jsonReader);
    }

    @Test
    @Timeout(8000)
    void read_shouldReturnString_whenJsonTokenIsString() throws IOException {
        when(jsonReader.peek()).thenReturn(JsonToken.STRING);
        when(jsonReader.nextString()).thenReturn("test string");

        String result = typeAdapter.read(jsonReader);

        assertEquals("test string", result);
        verify(jsonReader).peek();
        verify(jsonReader).nextString();
        verifyNoMoreInteractions(jsonReader);
    }

    @Test
    @Timeout(8000)
    void read_shouldThrowIOException_whenIOExceptionOccurs() throws IOException {
        when(jsonReader.peek()).thenThrow(new IOException("read error"));

        IOException exception = assertThrows(IOException.class, () -> typeAdapter.read(jsonReader));
        assertEquals("read error", exception.getMessage());

        verify(jsonReader).peek();
        verifyNoMoreInteractions(jsonReader);
    }
}