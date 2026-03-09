package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.stream.JsonToken;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.JsonReader;
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
        stringWriter = new StringWriter();
        jsonWriter = new JsonWriter(stringWriter);

        // Create a concrete subclass of TypeAdapter<String> for testing
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
            public String read(JsonReader in) {
                return null; // Not needed for this test
            }
        };
    }

    @Test
    @Timeout(8000)
    void write_shouldWriteNullValueWhenValueIsNull() throws IOException {
        typeAdapter.write(jsonWriter, null);
        jsonWriter.flush();
        assertEquals("null", stringWriter.toString());
    }

    @Test
    @Timeout(8000)
    void write_shouldWriteStringValueWhenValueIsNotNull() throws IOException {
        String testValue = "test string";
        typeAdapter.write(jsonWriter, testValue);
        jsonWriter.flush();
        assertEquals("\"test string\"", stringWriter.toString());
    }

    @Test
    @Timeout(8000)
    void write_shouldThrowIOException_whenJsonWriterThrows() throws IOException {
        JsonWriter mockWriter = mock(JsonWriter.class);
        doThrow(new IOException("mock IO exception")).when(mockWriter).value(anyString());

        IOException thrown = assertThrows(IOException.class, () -> {
            typeAdapter.write(mockWriter, "value");
        });
        assertEquals("mock IO exception", thrown.getMessage());
    }
}