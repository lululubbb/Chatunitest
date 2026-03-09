package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.internal.bind.JsonTreeWriter;
import java.io.Reader;
import java.io.Writer;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.StringReader;
import java.lang.reflect.Method;

class TypeAdapterWriteTest {

    private TypeAdapter<String> typeAdapter;

    @BeforeEach
    void setUp() {
        // Create a concrete subclass for testing since TypeAdapter is abstract
        typeAdapter = new TypeAdapter<String>() {
            @Override
            public void write(JsonWriter out, String value) throws IOException {
                if (value == null) {
                    out.nullValue();
                    return;
                }
                out.value(value);
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
    void write_nullValue_callsNullValue() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);

        typeAdapter.write(jsonWriter, null);
        jsonWriter.flush();

        assertEquals("null", stringWriter.toString());
    }

    @Test
    @Timeout(8000)
    void write_nonNullValue_writesValue() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);

        String testValue = "test string";
        typeAdapter.write(jsonWriter, testValue);
        jsonWriter.flush();

        assertEquals("\"test string\"", stringWriter.toString());
    }

    @Test
    @Timeout(8000)
    void toJson_withValue_returnsJsonString() throws IOException {
        String testValue = "hello";

        String json = typeAdapter.toJson(testValue);

        assertEquals("\"hello\"", json);
    }

    @Test
    @Timeout(8000)
    void toJson_withNull_returnsNullJson() throws IOException {
        String json = typeAdapter.toJson(null);

        assertEquals("null", json);
    }

    @Test
    @Timeout(8000)
    void toJson_writerOutput_containsCorrectJson() throws IOException {
        StringWriter writer = new StringWriter();
        typeAdapter.toJson(writer, "abc");
        writer.flush();

        assertEquals("\"abc\"", writer.toString());
    }

    @Test
    @Timeout(8000)
    void nullSafe_wrapsTypeAdapter() throws IOException {
        TypeAdapter<String> nullSafeAdapter = typeAdapter.nullSafe();

        StringWriter writer = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(writer);

        // nullSafeAdapter should write "null" for null values
        nullSafeAdapter.write(jsonWriter, null);
        jsonWriter.flush();
        assertEquals("null", writer.toString());

        writer.getBuffer().setLength(0);

        // and delegate to original adapter for non-null values
        nullSafeAdapter.write(jsonWriter, "value");
        jsonWriter.flush();
        assertEquals("\"value\"", writer.toString());
    }

    @Test
    @Timeout(8000)
    void toJsonTree_withValue_returnsJsonElement() throws IOException {
        JsonElement element = typeAdapter.toJsonTree("abc");
        assertTrue(element.isJsonPrimitive());
        assertEquals("abc", element.getAsString());
    }

    @Test
    @Timeout(8000)
    void toJsonTree_withNull_returnsJsonNull() throws IOException {
        JsonElement element = typeAdapter.toJsonTree(null);
        assertTrue(element.isJsonNull());
    }

    @Test
    @Timeout(8000)
    void fromJson_readerWithNull_returnsNull() throws IOException {
        StringReader stringReader = new StringReader("null");
        String result = typeAdapter.fromJson(stringReader);
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void fromJson_readerWithValue_returnsValue() throws IOException {
        StringReader stringReader = new StringReader("\"hello\"");
        String result = typeAdapter.fromJson(stringReader);
        assertEquals("hello", result);
    }

    @Test
    @Timeout(8000)
    void fromJson_stringWithValue_returnsValue() throws IOException {
        String json = "\"hello\"";
        String result = typeAdapter.fromJson(json);
        assertEquals("hello", result);
    }

    @Test
    @Timeout(8000)
    void fromJsonTree_withJsonPrimitive_returnsValue() throws IOException {
        JsonElement element = new JsonPrimitive("hello");
        String result = typeAdapter.fromJsonTree(element);
        assertEquals("hello", result);
    }

    @Test
    @Timeout(8000)
    void fromJsonTree_withJsonNull_returnsNull() throws IOException {
        JsonElement element = JsonNull.INSTANCE;
        String result = typeAdapter.fromJsonTree(element);
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void invokeWriteViaReflection_nonNullValue() throws Exception {
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);

        Method writeMethod = TypeAdapter.class.getDeclaredMethod("write", JsonWriter.class, Object.class);
        writeMethod.setAccessible(true);
        writeMethod.invoke(typeAdapter, jsonWriter, "reflection");

        jsonWriter.flush();
        assertEquals("\"reflection\"", stringWriter.toString());
    }

    @Test
    @Timeout(8000)
    void invokeWriteViaReflection_nullValue() throws Exception {
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);

        Method writeMethod = TypeAdapter.class.getDeclaredMethod("write", JsonWriter.class, Object.class);
        writeMethod.setAccessible(true);
        // Pass null as a direct argument, not wrapped in Object[]
        writeMethod.invoke(typeAdapter, jsonWriter, (Object) null);

        jsonWriter.flush();
        assertEquals("null", stringWriter.toString());
    }
}