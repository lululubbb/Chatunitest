package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;

class TypeAdapterWriteTest {

    private TypeAdapter<String> typeAdapter;
    private JsonWriter jsonWriter;
    private StringWriter stringWriter;

    @BeforeEach
    void setUp() {
        // Create a concrete anonymous subclass for testing
        typeAdapter = new TypeAdapter<String>() {
            @Override
            public void write(JsonWriter out, String value) throws IOException {
                if (value == null) {
                    out.nullValue();
                } else {
                    out.value(value);
                }
            }

            @Override
            public String read(com.google.gson.stream.JsonReader in) {
                return null;
            }
        };
        stringWriter = new StringWriter();
        jsonWriter = new JsonWriter(stringWriter);
    }

    @Test
    @Timeout(8000)
    void testWrite_withNonNullValue() throws IOException {
        String testValue = "testString";
        typeAdapter.write(jsonWriter, testValue);
        jsonWriter.flush();
        assertEquals("\"testString\"", stringWriter.toString());
    }

    @Test
    @Timeout(8000)
    void testWrite_withNullValue() throws IOException {
        typeAdapter.write(jsonWriter, null);
        jsonWriter.flush();
        assertEquals("null", stringWriter.toString());
    }

    @Test
    @Timeout(8000)
    void testWrite_throwsIOException() throws IOException {
        JsonWriter mockWriter = mock(JsonWriter.class);
        doThrow(new IOException("write error")).when(mockWriter).value(anyString());

        IOException thrown = assertThrows(IOException.class, () -> {
            typeAdapter.write(mockWriter, "value");
        });
        assertEquals("write error", thrown.getMessage());
    }
}