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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

class TypeAdapter_166_1Test {

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
            public String read(JsonReader in) throws IOException {
                JsonToken token = in.peek();
                if (token == JsonToken.NULL) {
                    in.nextNull();
                    return null;
                } else if (token == JsonToken.STRING) {
                    return in.nextString();
                }
                throw new IOException("Unexpected token: " + token);
            }
        };
    }

    @Test
    @Timeout(8000)
    void testToJsonWriterAndFromJsonReader() throws IOException {
        StringWriter writer = new StringWriter();
        String value = "testValue";

        typeAdapter.toJson(writer, value);
        String json = writer.toString();
        assertEquals("\"testValue\"", json);

        StringReader reader = new StringReader(json);
        String result = typeAdapter.fromJson(reader);
        assertEquals(value, result);
    }

    @Test
    @Timeout(8000)
    void testToJsonWithNullValue() throws IOException {
        StringWriter writer = new StringWriter();
        typeAdapter.toJson(writer, null);
        assertEquals("null", writer.toString());

        StringReader reader = new StringReader("null");
        String result = typeAdapter.fromJson(reader);
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void testToJsonString() {
        String value = "hello";
        String json = typeAdapter.toJson(value);
        assertEquals("\"hello\"", json);

        String nullJson = typeAdapter.toJson(null);
        assertEquals("null", nullJson);
    }

    @Test
    @Timeout(8000)
    void testFromJsonString() throws IOException {
        String json = "\"hello\"";
        String result = typeAdapter.fromJson(json);
        assertEquals("hello", result);

        String nullJson = "null";
        String nullResult = typeAdapter.fromJson(nullJson);
        assertNull(nullResult);
    }

    @Test
    @Timeout(8000)
    void testToJsonTreeAndFromJsonTree() throws Exception {
        String value = "jsonTreeValue";

        JsonElement jsonTree = typeAdapter.toJsonTree(value);
        assertTrue(jsonTree.isJsonPrimitive());
        assertEquals("jsonTreeValue", jsonTree.getAsString());

        String fromTree = typeAdapter.fromJsonTree(jsonTree);
        assertEquals(value, fromTree);
    }

    @Test
    @Timeout(8000)
    void testNullSafeWritesNull() throws IOException {
        TypeAdapter<String> nullSafeAdapter = typeAdapter.nullSafe();

        StringWriter writer = new StringWriter();
        nullSafeAdapter.toJson(writer, null);
        assertEquals("null", writer.toString());

        StringReader reader = new StringReader("null");
        String result = nullSafeAdapter.fromJson(reader);
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void testNullSafeDelegatesNonNull() throws IOException {
        TypeAdapter<String> nullSafeAdapter = typeAdapter.nullSafe();

        StringWriter writer = new StringWriter();
        nullSafeAdapter.toJson(writer, "abc");
        assertEquals("\"abc\"", writer.toString());

        StringReader reader = new StringReader("\"abc\"");
        String result = nullSafeAdapter.fromJson(reader);
        assertEquals("abc", result);
    }

    @Test
    @Timeout(8000)
    void testPrivateMethodsReflection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // toJson(Writer, T)
        Method toJsonWriterMethod = TypeAdapter.class.getDeclaredMethod("toJson", Writer.class, Object.class);
        toJsonWriterMethod.setAccessible(true);
        StringWriter writer = new StringWriter();
        toJsonWriterMethod.invoke(typeAdapter, writer, "reflect");
        assertEquals("\"reflect\"", writer.toString());

        // fromJson(Reader)
        Method fromJsonReaderMethod = TypeAdapter.class.getDeclaredMethod("fromJson", Reader.class);
        fromJsonReaderMethod.setAccessible(true);
        StringReader reader = new StringReader("\"reflect\"");
        String result = (String) fromJsonReaderMethod.invoke(typeAdapter, reader);
        assertEquals("reflect", result);

        // toJson(T)
        Method toJsonStringMethod = TypeAdapter.class.getDeclaredMethod("toJson", Object.class);
        toJsonStringMethod.setAccessible(true);
        String json = (String) toJsonStringMethod.invoke(typeAdapter, "reflect");
        assertEquals("\"reflect\"", json);

        // toJsonTree(T)
        Method toJsonTreeMethod = TypeAdapter.class.getDeclaredMethod("toJsonTree", Object.class);
        toJsonTreeMethod.setAccessible(true);
        JsonElement jsonElement = (JsonElement) toJsonTreeMethod.invoke(typeAdapter, "reflect");
        assertTrue(jsonElement.isJsonPrimitive());
        assertEquals("reflect", jsonElement.getAsString());

        // fromJson(String)
        Method fromJsonStringMethod = TypeAdapter.class.getDeclaredMethod("fromJson", String.class);
        fromJsonStringMethod.setAccessible(true);
        String fromJsonResult = (String) fromJsonStringMethod.invoke(typeAdapter, "\"reflect\"");
        assertEquals("reflect", fromJsonResult);

        // fromJsonTree(JsonElement)
        Method fromJsonTreeMethod = TypeAdapter.class.getDeclaredMethod("fromJsonTree", JsonElement.class);
        fromJsonTreeMethod.setAccessible(true);
        String fromTreeResult = (String) fromJsonTreeMethod.invoke(typeAdapter, jsonElement);
        assertEquals("reflect", fromTreeResult);

        // nullSafe()
        Method nullSafeMethod = TypeAdapter.class.getDeclaredMethod("nullSafe");
        nullSafeMethod.setAccessible(true);
        Object nullSafeAdapter = nullSafeMethod.invoke(typeAdapter);
        assertNotNull(nullSafeAdapter);
        assertTrue(nullSafeAdapter instanceof TypeAdapter);
    }
}