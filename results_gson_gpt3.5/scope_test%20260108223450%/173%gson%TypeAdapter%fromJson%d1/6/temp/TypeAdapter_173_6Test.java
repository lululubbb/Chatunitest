package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.stream.JsonWriter;
import java.io.StringWriter;
import java.io.Writer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

class TypeAdapterFromJsonTest {

    private TypeAdapter<String> typeAdapter;

    @BeforeEach
    void setUp() {
        typeAdapter = new TypeAdapter<String>() {
            @Override
            public void write(com.google.gson.stream.JsonWriter out, String value) {
                // no-op for this test
            }

            @Override
            public String read(JsonReader in) throws IOException {
                if (in.peek() == JsonToken.STRING) {
                    return in.nextString();
                } else if (in.peek() == JsonToken.NULL) {
                    in.nextNull();
                    return null;
                }
                throw new IOException("Expected STRING or NULL token");
            }
        };
    }

    @Test
    @Timeout(8000)
    void fromJson_withValidJsonString_returnsParsedValue() throws IOException {
        String json = "\"testValue\"";
        Reader reader = new StringReader(json);

        String result = typeAdapter.fromJson(reader);

        assertEquals("testValue", result);
    }

    @Test
    @Timeout(8000)
    void fromJson_withNullJson_returnsNull() throws IOException {
        String json = "null";
        Reader reader = new StringReader(json);

        String result = typeAdapter.fromJson(reader);

        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void fromJson_withEmptyJson_throwsIOException() {
        String json = "";
        Reader reader = new StringReader(json);

        assertThrows(IOException.class, () -> typeAdapter.fromJson(reader));
    }

    @Test
    @Timeout(8000)
    void fromJson_callsReadWithCorrectJsonReader() throws IOException {
        TypeAdapter<String> spyAdapter = spy(typeAdapter);
        doReturn("spyTest").when(spyAdapter).read(any(JsonReader.class));

        String result = spyAdapter.fromJson(new StringReader("\"spyTest\""));

        assertEquals("spyTest", result);
        verify(spyAdapter).read(any(JsonReader.class));
    }
}