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

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class TypeAdapter_168_4Test {

    private TypeAdapter<String> adapter;
    private StringWriter stringWriter;

    @BeforeEach
    void setUp() {
        adapter = new TypeAdapter<String>() {
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
    }

    @Test
    @Timeout(8000)
    void testToJson_callsWriteWithJsonWriter() throws IOException {
        TypeAdapter<String> spyAdapter = spy(adapter);
        spyAdapter.toJson(stringWriter, "testValue");

        // Verify that write was called with a JsonWriter wrapping the stringWriter and the value "testValue"
        ArgumentCaptor<JsonWriter> writerCaptor = ArgumentCaptor.forClass(JsonWriter.class);
        verify(spyAdapter).write(writerCaptor.capture(), eq("testValue"));

        // The underlying writer of JsonWriter should be stringWriter
        JsonWriter capturedWriter = writerCaptor.getValue();
        assertNotNull(capturedWriter);

        // The output string should contain the JSON string "testValue"
        String output = stringWriter.toString();
        assertEquals("\"testValue\"", output);
    }

    @Test
    @Timeout(8000)
    void testToJson_withNullValue_writesNull() throws IOException {
        TypeAdapter<String> spyAdapter = spy(adapter);
        spyAdapter.toJson(stringWriter, null);

        ArgumentCaptor<JsonWriter> writerCaptor = ArgumentCaptor.forClass(JsonWriter.class);
        verify(spyAdapter).write(writerCaptor.capture(), isNull());

        String output = stringWriter.toString();
        assertEquals("null", output);
    }

    @Test
    @Timeout(8000)
    void testToJson_noExceptionOnWrite() throws Exception {
        // Use reflection to invoke private method toJson(Writer,T) to confirm accessibility
        Method toJsonMethod = TypeAdapter.class.getDeclaredMethod("toJson", java.io.Writer.class, Object.class);
        toJsonMethod.setAccessible(true);
        StringWriter sw = new StringWriter();

        // Should not throw IOException
        toJsonMethod.invoke(adapter, sw, "reflectionTest");

        String output = sw.toString();
        assertEquals("\"reflectionTest\"", output);
    }
}