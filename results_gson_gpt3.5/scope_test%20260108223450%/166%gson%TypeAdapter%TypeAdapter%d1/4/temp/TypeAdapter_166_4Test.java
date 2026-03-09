package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TypeAdapter_166_4Test {

    private TypeAdapter<String> typeAdapter;

    @BeforeEach
    public void setUp() {
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
            public String read(JsonReader in) throws IOException {
                if (in.peek() == JsonToken.NULL) {
                    in.nextNull();
                    return null;
                }
                return in.nextString();
            }
        };
    }

    @Test
    @Timeout(8000)
    public void testToJson_WriterAndValue() throws IOException {
        StringWriter writer = new StringWriter();
        typeAdapter.toJson(writer, "test");
        assertEquals("\"test\"", writer.toString());

        writer = new StringWriter();
        typeAdapter.toJson(writer, null);
        assertEquals("null", writer.toString());
    }

    @Test
    @Timeout(8000)
    public void testToJson_StringValue() throws IOException {
        String json = typeAdapter.toJson("test");
        assertEquals("\"test\"", json);

        json = typeAdapter.toJson(null);
        assertEquals("null", json);
    }

    @Test
    @Timeout(8000)
    public void testToJsonTree() {
        JsonElement jsonElement = typeAdapter.toJsonTree("test");
        assertTrue(jsonElement.isJsonPrimitive());
        assertEquals("test", jsonElement.getAsString());

        jsonElement = typeAdapter.toJsonTree(null);
        assertTrue(jsonElement.isJsonNull());
    }

    @Test
    @Timeout(8000)
    public void testFromJson_Reader() throws IOException {
        StringReader reader = new StringReader("\"test\"");
        String result = typeAdapter.fromJson(reader);
        assertEquals("test", result);

        reader = new StringReader("null");
        result = typeAdapter.fromJson(reader);
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    public void testFromJson_String() throws IOException {
        String result = typeAdapter.fromJson("\"test\"");
        assertEquals("test", result);

        result = typeAdapter.fromJson("null");
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    public void testFromJsonTree() {
        JsonElement jsonElement = new JsonPrimitive("test");
        String result = typeAdapter.fromJsonTree(jsonElement);
        assertEquals("test", result);

        result = typeAdapter.fromJsonTree(JsonNull.INSTANCE);
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    public void testNullSafe() throws IOException {
        TypeAdapter<String> nullSafeAdapter = typeAdapter.nullSafe();

        StringWriter writer = new StringWriter();
        nullSafeAdapter.write(new JsonWriter(writer), null);
        assertEquals("null", writer.toString());

        StringWriter writer2 = new StringWriter();
        nullSafeAdapter.write(new JsonWriter(writer2), "value");
        assertEquals("\"value\"", writer2.toString());
    }

    @Test
    @Timeout(8000)
    public void testPrivateMethodsInvocation() throws Exception {
        // Using reflection to invoke private methods if any existed.
        // The provided class has no private methods, but this is a demonstration.

        Method toJsonMethod = TypeAdapter.class.getDeclaredMethod("toJson", Writer.class, Object.class);
        toJsonMethod.setAccessible(true);

        StringWriter writer = new StringWriter();
        toJsonMethod.invoke(typeAdapter, writer, "reflectionTest");
        assertEquals("\"reflectionTest\"", writer.toString());
    }
}