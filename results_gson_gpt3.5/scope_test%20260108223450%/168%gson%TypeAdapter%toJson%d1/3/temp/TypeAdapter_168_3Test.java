package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;

import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;

class TypeAdapterToJsonTest {

    private TypeAdapter<String> typeAdapter;

    @BeforeEach
    void setUp() {
        typeAdapter = new TypeAdapter<String>() {
            @Override
            public void write(JsonWriter out, String value) throws IOException {
                out.value(value);
            }

            @Override
            public String read(com.google.gson.stream.JsonReader in) {
                return null; // Not relevant for this test
            }
        };
    }

    @Test
    @Timeout(8000)
    void toJson_writesValueToWriter() throws IOException {
        StringWriter stringWriter = new StringWriter();
        typeAdapter.toJson(stringWriter, "testValue");
        String json = stringWriter.toString();
        assertEquals("\"testValue\"", json);
    }

    @Test
    @Timeout(8000)
    void toJson_withNullValue_writesNull() throws IOException {
        StringWriter stringWriter = new StringWriter();
        TypeAdapter<String> nullWritingAdapter = new TypeAdapter<String>() {
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
                return null; // Not relevant
            }
        };
        nullWritingAdapter.toJson(stringWriter, null);
        assertEquals("null", stringWriter.toString());
    }

    @Test
    @Timeout(8000)
    void toJson_invokesWriteWithCorrectJsonWriter() throws IOException {
        StringWriter stringWriter = new StringWriter();
        TypeAdapter<String> adapterSpy = new TypeAdapter<String>() {
            @Override
            public void write(JsonWriter out, String value) throws IOException {
                out.value(value);
            }

            @Override
            public String read(com.google.gson.stream.JsonReader in) {
                return null;
            }
        };
        adapterSpy.toJson(stringWriter, "value");
        String result = stringWriter.toString();
        assertEquals("\"value\"", result);
    }
}