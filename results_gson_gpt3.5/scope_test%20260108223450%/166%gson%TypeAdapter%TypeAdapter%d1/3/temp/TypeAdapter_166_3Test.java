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

class TypeAdapter_166_3Test {

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
                }
                return in.nextString();
            }
        };
    }

    @Test
    @Timeout(8000)
    void testToJson_withNonNullValue() throws IOException {
        StringWriter writer = new StringWriter();
        typeAdapter.toJson(writer, "test");
        assertEquals("\"test\"", writer.toString());
    }

    @Test
    @Timeout(8000)
    void testToJson_withNullValue() throws IOException {
        StringWriter writer = new StringWriter();
        typeAdapter.toJson(writer, null);
        assertEquals("null", writer.toString());
    }

    @Test
    @Timeout(8000)
    void testToJson_returnString_nonNull() {
        String json = typeAdapter.toJson("hello");
        assertEquals("\"hello\"", json);
    }

    @Test
    @Timeout(8000)
    void testToJson_returnString_null() {
        String json = typeAdapter.toJson(null);
        assertEquals("null", json);
    }

    @Test
    @Timeout(8000)
    void testToJsonTree_nonNull() {
        JsonElement jsonElement = typeAdapter.toJsonTree("abc");
        assertTrue(jsonElement.isJsonPrimitive());
        assertEquals("abc", jsonElement.getAsString());
    }

    @Test
    @Timeout(8000)
    void testToJsonTree_null() {
        JsonElement jsonElement = typeAdapter.toJsonTree(null);
        assertTrue(jsonElement.isJsonNull());
    }

    @Test
    @Timeout(8000)
    void testFromJsonReader_nonNull() throws IOException {
        StringReader reader = new StringReader("\"value\"");
        String result = typeAdapter.fromJson(reader);
        assertEquals("value", result);
    }

    @Test
    @Timeout(8000)
    void testFromJsonReader_null() throws IOException {
        StringReader reader = new StringReader("null");
        String result = typeAdapter.fromJson(reader);
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void testFromJsonString_nonNull() throws IOException {
        String json = "\"stringValue\"";
        String result = typeAdapter.fromJson(json);
        assertEquals("stringValue", result);
    }

    @Test
    @Timeout(8000)
    void testFromJsonString_null() throws IOException {
        String json = "null";
        String result = typeAdapter.fromJson(json);
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void testFromJsonTree_nonNull() {
        JsonElement jsonElement = new JsonPrimitive("treeValue");
        String result = typeAdapter.fromJsonTree(jsonElement);
        assertEquals("treeValue", result);
    }

    @Test
    @Timeout(8000)
    void testFromJsonTree_null() {
        JsonElement jsonElement = JsonNull.INSTANCE;
        String result = typeAdapter.fromJsonTree(jsonElement);
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void testNullSafe_withNonNullValue() throws IOException {
        TypeAdapter<String> nullSafeAdapter = typeAdapter.nullSafe();
        StringWriter writer = new StringWriter();
        nullSafeAdapter.write(new JsonWriter(writer), "safe");
        assertEquals("\"safe\"", writer.toString());
    }

    @Test
    @Timeout(8000)
    void testNullSafe_withNullValue() throws IOException {
        TypeAdapter<String> nullSafeAdapter = typeAdapter.nullSafe();
        StringWriter writer = new StringWriter();
        nullSafeAdapter.write(new JsonWriter(writer), null);
        assertEquals("null", writer.toString());
    }

    @Test
    @Timeout(8000)
    void testInvokePrivateToJsonUsingReflection() throws Exception {
        Method toJsonMethod = TypeAdapter.class.getDeclaredMethod("toJson", Writer.class, Object.class);
        toJsonMethod.setAccessible(true);

        StringWriter writer = new StringWriter();
        toJsonMethod.invoke(typeAdapter, writer, "reflect");
        assertEquals("\"reflect\"", writer.toString());
    }

    @Test
    @Timeout(8000)
    void testInvokePrivateToJson_nullUsingReflection() throws Exception {
        Method toJsonMethod = TypeAdapter.class.getDeclaredMethod("toJson", Writer.class, Object.class);
        toJsonMethod.setAccessible(true);

        StringWriter writer = new StringWriter();
        toJsonMethod.invoke(typeAdapter, writer, null);
        assertEquals("null", writer.toString());
    }
}