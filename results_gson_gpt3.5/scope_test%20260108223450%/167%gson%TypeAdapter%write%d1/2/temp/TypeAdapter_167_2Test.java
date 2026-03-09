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
import java.io.IOException;
import java.io.StringWriter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TypeAdapter_167_2Test {

    private TypeAdapter<String> typeAdapter;
    private JsonWriter jsonWriter;
    private StringWriter stringWriter;

    @BeforeEach
    void setUp() {
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
    void write_withNonNullValue_writesValue() throws IOException {
        String testValue = "test";

        typeAdapter.write(jsonWriter, testValue);
        jsonWriter.flush();

        assertEquals("\"test\"", stringWriter.toString());
    }

    @Test
    @Timeout(8000)
    void write_withNullValue_writesNull() throws IOException {
        typeAdapter.write(jsonWriter, null);
        jsonWriter.flush();

        assertEquals("null", stringWriter.toString());
    }

    @Test
    @Timeout(8000)
    void write_invokedWithMockedJsonWriter_callsExpectedMethods() throws IOException {
        JsonWriter mockWriter = mock(JsonWriter.class);
        String testValue = "mocked";

        typeAdapter.write(mockWriter, testValue);

        verify(mockWriter).value(testValue);
        verify(mockWriter, never()).nullValue();
    }

    @Test
    @Timeout(8000)
    void write_invokedWithMockedJsonWriter_nullValue_callsNullValue() throws IOException {
        JsonWriter mockWriter = mock(JsonWriter.class);

        typeAdapter.write(mockWriter, null);

        verify(mockWriter).nullValue();
        verify(mockWriter, never()).value(anyString());
    }
}