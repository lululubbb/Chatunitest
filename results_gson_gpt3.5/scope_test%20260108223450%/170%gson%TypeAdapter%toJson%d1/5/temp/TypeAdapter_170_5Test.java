package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonIOException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

class TypeAdapterToJsonTest {

    private TypeAdapter<String> typeAdapter;

    @BeforeEach
    void setUp() {
        typeAdapter = new TypeAdapter<String>() {
            @Override
            public void write(JsonWriter out, String value) {
                try {
                    out.value(value);
                } catch (IOException e) {
                    throw new RuntimeException(e);
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
    void toJson_returnsJsonString() {
        String input = "testValue";
        String json = typeAdapter.toJson(input);
        assertEquals("\"testValue\"", json);
    }

    @Test
    @Timeout(8000)
    void toJson_handlesIOExceptionFromToJsonWriter() throws Exception {
        TypeAdapter<String> spyAdapter = Mockito.spy(typeAdapter);

        doThrow(new IOException("IO error")).when(spyAdapter).write(any(JsonWriter.class), anyString());

        JsonIOException thrown = assertThrows(JsonIOException.class, () -> spyAdapter.toJson("value"));
        assertEquals("java.io.IOException: IO error", thrown.getMessage());
        assertTrue(thrown.getCause() instanceof IOException);
    }
}