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

class TypeAdapter_166_2Test {

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
  void testToJson_writerAndValue() throws IOException {
    StringWriter writer = new StringWriter();
    typeAdapter.toJson(writer, "test");
    String json = writer.toString();
    assertEquals("\"test\"", json);
  }

  @Test
    @Timeout(8000)
  void testToJson_writerAndNullValue() throws IOException {
    StringWriter writer = new StringWriter();
    typeAdapter.toJson(writer, null);
    String json = writer.toString();
    assertEquals("null", json);
  }

  @Test
    @Timeout(8000)
  void testToJson_value() throws IOException {
    String json = typeAdapter.toJson("hello");
    assertEquals("\"hello\"", json);
  }

  @Test
    @Timeout(8000)
  void testToJson_valueNull() throws IOException {
    String json = typeAdapter.toJson(null);
    assertEquals("null", json);
  }

  @Test
    @Timeout(8000)
  void testToJsonTree_value() {
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
  void testFromJson_reader() throws IOException {
    String json = "\"value\"";
    Reader reader = new StringReader(json);
    String result = typeAdapter.fromJson(reader);
    assertEquals("value", result);
  }

  @Test
    @Timeout(8000)
  void testFromJson_readerNull() throws IOException {
    String json = "null";
    Reader reader = new StringReader(json);
    String result = typeAdapter.fromJson(reader);
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void testFromJson_string() throws IOException {
    String json = "\"stringValue\"";
    String result = typeAdapter.fromJson(json);
    assertEquals("stringValue", result);
  }

  @Test
    @Timeout(8000)
  void testFromJson_stringNull() throws IOException {
    String json = "null";
    String result = typeAdapter.fromJson(json);
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void testFromJsonTree_nonNull() {
    JsonElement jsonElement = new JsonPrimitive("value");
    String result = typeAdapter.fromJsonTree(jsonElement);
    assertEquals("value", result);
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
  void testNullSafe_writeAndRead() throws IOException {
    TypeAdapter<String> nullSafeAdapter = typeAdapter.nullSafe();

    StringWriter writer = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(writer);
    nullSafeAdapter.write(jsonWriter, null);
    jsonWriter.close();
    assertEquals("null", writer.toString());

    Reader reader = new StringReader("null");
    String result = nullSafeAdapter.fromJson(reader);
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void testNullSafe_writeAndReadNonNull() throws IOException {
    TypeAdapter<String> nullSafeAdapter = typeAdapter.nullSafe();

    StringWriter writer = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(writer);
    nullSafeAdapter.write(jsonWriter, "foo");
    jsonWriter.close();
    assertEquals("\"foo\"", writer.toString());

    Reader reader = new StringReader("\"foo\"");
    String result = nullSafeAdapter.fromJson(reader);
    assertEquals("foo", result);
  }

  @Test
    @Timeout(8000)
  void testPrivateToJsonMethodViaReflection() throws Exception {
    Method toJsonMethod = TypeAdapter.class.getDeclaredMethod("toJson", Writer.class, Object.class);
    toJsonMethod.setAccessible(true);
    StringWriter writer = new StringWriter();
    toJsonMethod.invoke(typeAdapter, writer, "reflectionTest");
    assertEquals("\"reflectionTest\"", writer.toString());
  }

}