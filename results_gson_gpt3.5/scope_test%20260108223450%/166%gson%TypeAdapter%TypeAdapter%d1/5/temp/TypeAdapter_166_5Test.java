package com.google.gson;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.*;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.*;

public class TypeAdapter_166_5Test {

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
                JsonToken token = in.peek();
                if (token == JsonToken.NULL) {
                    in.nextNull();
                    return null;
                }
                return in.nextString();
            }
        };
    }

    @Test
    @Timeout(8000)
    public void testToJson_withNonNullValue() throws IOException {
        StringWriter writer = new StringWriter();
        String testValue = "test";

        typeAdapter.toJson(writer, testValue);

        String json = writer.toString();
        assertEquals("\"test\"", json);
    }

    @Test
    @Timeout(8000)
    public void testToJson_withNullValue() throws IOException {
        StringWriter writer = new StringWriter();

        typeAdapter.toJson(writer, null);

        String json = writer.toString();
        assertEquals("null", json);
    }

    @Test
    @Timeout(8000)
    public void testToJson_returnsJsonString() {
        String json = typeAdapter.toJson("hello");
        assertEquals("\"hello\"", json);
    }

    @Test
    @Timeout(8000)
    public void testToJson_returnsJsonString_nullValue() {
        String json = typeAdapter.toJson(null);
        assertEquals("null", json);
    }

    @Test
    @Timeout(8000)
    public void testToJsonTree_withNonNullValue() {
        JsonElement jsonElement = typeAdapter.toJsonTree("abc");
        assertTrue(jsonElement.isJsonPrimitive());
        assertEquals("abc", jsonElement.getAsString());
    }

    @Test
    @Timeout(8000)
    public void testToJsonTree_withNullValue() {
        JsonElement jsonElement = typeAdapter.toJsonTree(null);
        assertTrue(jsonElement.isJsonNull());
    }

    @Test
    @Timeout(8000)
    public void testFromJson_withNonNullString() throws IOException {
        String json = "\"hello\"";
        String result = typeAdapter.fromJson(new StringReader(json));
        assertEquals("hello", result);
    }

    @Test
    @Timeout(8000)
    public void testFromJson_withNullString() throws IOException {
        String json = "null";
        String result = typeAdapter.fromJson(new StringReader(json));
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    public void testFromJson_withStringInput() throws IOException {
        String json = "\"world\"";
        String result = typeAdapter.fromJson(json);
        assertEquals("world", result);
    }

    @Test
    @Timeout(8000)
    public void testFromJson_withNullStringInput() throws IOException {
        String json = "null";
        String result = typeAdapter.fromJson(json);
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    public void testFromJsonTree_withNonNullJsonElement() {
        JsonPrimitive jsonPrimitive = new JsonPrimitive("value");
        String result = typeAdapter.fromJsonTree(jsonPrimitive);
        assertEquals("value", result);
    }

    @Test
    @Timeout(8000)
    public void testFromJsonTree_withNullJsonElement() {
        JsonNull jsonNull = JsonNull.INSTANCE;
        String result = typeAdapter.fromJsonTree(jsonNull);
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    public void testNullSafe_withNonNullValue() throws IOException {
        TypeAdapter<String> nullSafeAdapter = typeAdapter.nullSafe();

        StringWriter writer = new StringWriter();
        nullSafeAdapter.write(new JsonWriter(writer), "safe");

        assertEquals("\"safe\"", writer.toString());
    }

    @Test
    @Timeout(8000)
    public void testNullSafe_withNullValue() throws IOException {
        TypeAdapter<String> nullSafeAdapter = typeAdapter.nullSafe();

        StringWriter writer = new StringWriter();
        nullSafeAdapter.write(new JsonWriter(writer), null);

        assertEquals("null", writer.toString());
    }

    @Test
    @Timeout(8000)
    public void testNullSafe_readWithNonNull() throws IOException {
        TypeAdapter<String> nullSafeAdapter = typeAdapter.nullSafe();

        JsonReader reader = new JsonReader(new StringReader("\"safe\""));
        String result = nullSafeAdapter.read(reader);
        assertEquals("safe", result);
    }

    @Test
    @Timeout(8000)
    public void testNullSafe_readWithNull() throws IOException {
        TypeAdapter<String> nullSafeAdapter = typeAdapter.nullSafe();

        JsonReader reader = new JsonReader(new StringReader("null"));
        String result = nullSafeAdapter.read(reader);
        assertNull(result);
    }
}