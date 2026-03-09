package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.io.Reader;
import java.io.StringReader;

import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TypeAdapter_168_5Test {

    private TypeAdapter<String> typeAdapter;
    private JsonWriter jsonWriterMock;
    private Writer writer;

    @BeforeEach
    void setUp() {
        writer = new StringWriter();
        typeAdapter = new TypeAdapter<String>() {
            @Override
            public void write(JsonWriter out, String value) throws IOException {
                // For testing, write the value as JSON string or null
                if (value == null) {
                    out.nullValue();
                } else {
                    out.value(value);
                }
            }

            @Override
            public String read(com.google.gson.stream.JsonReader in) {
                return null; // Not used in this test
            }
        };
    }

    @Test
    @Timeout(8000)
    void toJson_writesValueToWriter() throws IOException {
        StringWriter stringWriter = new StringWriter();
        String testValue = "testString";

        typeAdapter.toJson(stringWriter, testValue);

        String json = stringWriter.toString();
        assertEquals("\"testString\"", json);
    }

    @Test
    @Timeout(8000)
    void toJson_writesNullValueToWriter() throws IOException {
        StringWriter stringWriter = new StringWriter();

        typeAdapter.toJson(stringWriter, null);

        String json = stringWriter.toString();
        assertEquals("null", json);
    }

    @Test
    @Timeout(8000)
    void toJson_throwsIOExceptionFromWrite() {
        TypeAdapter<String> throwingAdapter = new TypeAdapter<String>() {
            @Override
            public void write(JsonWriter out, String value) throws IOException {
                throw new IOException("Write error");
            }

            @Override
            public String read(com.google.gson.stream.JsonReader in) {
                return null;
            }
        };

        StringWriter stringWriter = new StringWriter();

        IOException thrown = assertThrows(IOException.class, () -> throwingAdapter.toJson(stringWriter, "value"));
        assertEquals("Write error", thrown.getMessage());
    }
}