package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.stream.JsonWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class TypeAdapterFromJsonTreeTest {

    private TypeAdapter<String> typeAdapter;

    @BeforeEach
    void setUp() {
        typeAdapter = new TypeAdapter<String>() {
            @Override
            public void write(JsonWriter out, String value) {
                // no-op for this test
            }

            @Override
            public String read(JsonReader in) throws IOException {
                // Simulate reading from JsonReader
                if (in.peek() == JsonToken.STRING) {
                    return in.nextString();
                }
                return null;
            }
        };
    }

    @Test
    @Timeout(8000)
    void fromJsonTree_shouldReturnValue_whenReadSucceeds() throws Exception {
        // Create a JsonPrimitive string element
        JsonPrimitive jsonPrimitive = new JsonPrimitive("testValue");

        String result = typeAdapter.fromJsonTree(jsonPrimitive);

        assertEquals("testValue", result);
    }

    @Test
    @Timeout(8000)
    void fromJsonTree_shouldThrowJsonIOException_whenReadThrowsIOException() {
        TypeAdapter<String> adapterThrowing = new TypeAdapter<String>() {
            @Override
            public void write(JsonWriter out, String value) {
                // no-op
            }

            @Override
            public String read(JsonReader in) throws IOException {
                throw new IOException("read failed");
            }
        };

        JsonPrimitive jsonPrimitive = new JsonPrimitive("ignored");

        JsonIOException ex = assertThrows(JsonIOException.class, () -> adapterThrowing.fromJsonTree(jsonPrimitive));
        assertEquals("java.io.IOException: read failed", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void fromJsonTree_shouldInvokePrivateReadMethod_viaReflection() throws Exception {
        JsonPrimitive jsonPrimitive = new JsonPrimitive("reflectValue");

        Method readMethod = TypeAdapter.class.getDeclaredMethod("read", JsonReader.class);
        readMethod.setAccessible(true);

        JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonPrimitive);

        // invoke read method reflectively
        String result = (String) readMethod.invoke(typeAdapter, jsonTreeReader);

        assertEquals("reflectValue", result);
    }
}