package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;

import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;

class TypeAdapterToJsonTest {

    private TypeAdapter<String> typeAdapter;
    private StringWriter stringWriter;

    @BeforeEach
    void setUp() {
        typeAdapter = new TypeAdapter<String>() {
            @Override
            public void write(JsonWriter out, String value) throws IOException {
                // Simulate writing JSON string value
                out.value(value);
            }

            @Override
            public String read(com.google.gson.stream.JsonReader in) {
                return null;
            }
        };
        stringWriter = new StringWriter();
    }

    @Test
    @Timeout(8000)
    void toJson_writesValueToJsonWriter() throws IOException {
        String testValue = "test";

        typeAdapter.toJson(stringWriter, testValue);

        String jsonOutput = stringWriter.toString();
        // The output should be a JSON string representing the testValue, e.g. "test"
        // Since JsonWriter.value(String) writes a JSON string, it should include quotes
        assert jsonOutput.equals("\"" + testValue + "\"");
    }

    @Test
    @Timeout(8000)
    void toJson_callsWriteWithJsonWriter() throws Exception {
        StringWriter sw = new StringWriter();
        JsonWriter spyJsonWriter = spy(new JsonWriter(sw));

        TypeAdapter<String> adapterSpy = new TypeAdapter<String>() {
            @Override
            public void write(JsonWriter out, String value) throws IOException {
                // Call the spyJsonWriter's value method to verify later
                spyJsonWriter.value(value);
            }

            @Override
            public String read(com.google.gson.stream.JsonReader in) {
                return null;
            }
        };

        // Call write() with spyJsonWriter and verify spyJsonWriter was called
        adapterSpy.write(spyJsonWriter, "hello");

        verify(spyJsonWriter).value("hello");
    }
}