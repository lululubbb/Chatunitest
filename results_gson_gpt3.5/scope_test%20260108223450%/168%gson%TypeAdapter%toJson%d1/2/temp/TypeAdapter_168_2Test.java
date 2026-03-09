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
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class TypeAdapterToJsonTest {

    private TypeAdapter<String> typeAdapter;

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
    }

    @Test
    @Timeout(8000)
    void toJson_writesValueToWriter() throws IOException {
        StringWriter writer = new StringWriter();
        String value = "testValue";

        typeAdapter.toJson(writer, value);

        String json = writer.toString();
        assertEquals("\"testValue\"", json);
    }

    @Test
    @Timeout(8000)
    void toJson_writesNullValue() throws IOException {
        StringWriter writer = new StringWriter();
        typeAdapter.toJson(writer, null);

        String json = writer.toString();
        assertEquals("null", json);
    }

    @Test
    @Timeout(8000)
    void toJson_invokesWriteMethod() throws IOException {
        TypeAdapter<String> spyAdapter = spy(typeAdapter);

        StringWriter writer = new StringWriter();
        String value = "spyValue";

        spyAdapter.toJson(writer, value);

        verify(spyAdapter, times(1)).write(any(JsonWriter.class), eq(value));
    }

    @Test
    @Timeout(8000)
    void toJson_throwsIOExceptionWhenWriteFails() throws IOException {
        TypeAdapter<String> failingAdapter = new TypeAdapter<String>() {
            @Override
            public void write(JsonWriter out, String value) throws IOException {
                throw new IOException("write failed");
            }

            @Override
            public String read(com.google.gson.stream.JsonReader in) {
                return null;
            }
        };

        StringWriter writer = new StringWriter();

        IOException thrown = assertThrows(IOException.class, () -> failingAdapter.toJson(writer, "value"));
        assertEquals("write failed", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void toJson_privateMethod_callViaReflection() throws Exception {
        // Use getDeclaredMethod instead of getMethod because toJson(Writer, Object) is final but not public
        Method toJsonMethod = TypeAdapter.class.getDeclaredMethod("toJson", java.io.Writer.class, Object.class);
        toJsonMethod.setAccessible(true);

        StringWriter writer = new StringWriter();
        String value = "reflectionValue";

        toJsonMethod.invoke(typeAdapter, writer, value);

        assertEquals("\"reflectionValue\"", writer.toString());
    }
}