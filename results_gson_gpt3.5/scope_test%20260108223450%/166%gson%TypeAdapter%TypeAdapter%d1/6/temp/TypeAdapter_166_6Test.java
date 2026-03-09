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
import org.mockito.InOrder;

public class TypeAdapter_166_6Test {

    private TypeAdapter<String> typeAdapter;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

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
    public void testWriteAndRead() throws IOException {
        StringWriter writer = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(writer);

        typeAdapter.write(jsonWriter, "test");
        jsonWriter.flush();
        assertEquals("\"test\"", writer.toString());

        StringReader reader = new StringReader(writer.toString());
        JsonReader jsonReader = new JsonReader(reader);

        String result = typeAdapter.read(jsonReader);
        assertEquals("test", result);
    }

    @Test
    @Timeout(8000)
    public void testWriteNull() throws IOException {
        StringWriter writer = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(writer);

        typeAdapter.write(jsonWriter, null);
        jsonWriter.flush();
        assertEquals("null", writer.toString());

        StringReader reader = new StringReader(writer.toString());
        JsonReader jsonReader = new JsonReader(reader);

        String result = typeAdapter.read(jsonReader);
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    public void testToJsonWriterAndValue() throws IOException {
        StringWriter writer = new StringWriter();
        typeAdapter.toJson(writer, "hello");
        assertEquals("\"hello\"", writer.toString());

        String json = typeAdapter.toJson("world");
        assertEquals("\"world\"", json);
    }

    @Test
    @Timeout(8000)
    public void testToJsonTree() throws Exception {
        JsonElement jsonElement = typeAdapter.toJsonTree("jsonTreeTest");
        assertTrue(jsonElement.isJsonPrimitive());
        assertEquals("jsonTreeTest", jsonElement.getAsString());
    }

    @Test
    @Timeout(8000)
    public void testFromJsonReaderAndString() throws IOException {
        String json = "\"fromJsonTest\"";
        StringReader stringReader = new StringReader(json);
        String result = typeAdapter.fromJson(stringReader);
        assertEquals("fromJsonTest", result);

        result = typeAdapter.fromJson(json);
        assertEquals("fromJsonTest", result);
    }

    @Test
    @Timeout(8000)
    public void testFromJsonTree() {
        JsonElement jsonElement = new JsonPrimitive("fromJsonTreeTest");
        String result = typeAdapter.fromJsonTree(jsonElement);
        assertEquals("fromJsonTreeTest", result);
    }

    @Test
    @Timeout(8000)
    public void testNullSafeWriteAndRead() throws IOException {
        TypeAdapter<String> nullSafeAdapter = typeAdapter.nullSafe();

        StringWriter writer = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(writer);

        nullSafeAdapter.write(jsonWriter, null);
        jsonWriter.flush();
        assertEquals("null", writer.toString());

        StringReader reader = new StringReader("null");
        JsonReader jsonReader = new JsonReader(reader);

        String result = nullSafeAdapter.read(jsonReader);
        assertNull(result);

        writer = new StringWriter();
        jsonWriter = new JsonWriter(writer);
        nullSafeAdapter.write(jsonWriter, "notNull");
        jsonWriter.flush();
        assertEquals("\"notNull\"", writer.toString());

        reader = new StringReader(writer.toString());
        jsonReader = new JsonReader(reader);

        result = nullSafeAdapter.read(jsonReader);
        assertEquals("notNull", result);
    }

    @Test
    @Timeout(8000)
    public void testPrivateMethodsInvoke() throws Exception {
        // Using reflection to invoke private methods if any existed
        // Since TypeAdapter does not have private methods, invoke toJsonTree and fromJsonTree via reflection

        Method toJsonTreeMethod = TypeAdapter.class.getDeclaredMethod("toJsonTree", Object.class);
        toJsonTreeMethod.setAccessible(true);
        JsonElement jsonElement = (JsonElement) toJsonTreeMethod.invoke(typeAdapter, "reflectionTest");
        assertEquals("reflectionTest", jsonElement.getAsString());

        Method fromJsonTreeMethod = TypeAdapter.class.getDeclaredMethod("fromJsonTree", JsonElement.class);
        fromJsonTreeMethod.setAccessible(true);
        String result = (String) fromJsonTreeMethod.invoke(typeAdapter, jsonElement);
        assertEquals("reflectionTest", result);
    }
}