package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonTreeWriter_503_5Test {

  private JsonTreeWriter writer;

  @BeforeEach
  void setUp() {
    writer = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  void testConstructor_initialState() throws Exception {
    // product field should be JsonNull.INSTANCE initially
    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);
    JsonElement product = (JsonElement) productField.get(writer);
    assertSame(JsonNull.INSTANCE, product);

    // stack should be empty list
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<?> stack = (List<?>) stackField.get(writer);
    assertTrue(stack.isEmpty());

    // pendingName should be null
    Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
    pendingNameField.setAccessible(true);
    assertNull(pendingNameField.get(writer));
  }

  @Test
    @Timeout(8000)
  void testGet_returnsProduct() throws IOException {
    // Initially product is JsonNull.INSTANCE
    JsonElement product = writer.get();
    assertSame(JsonNull.INSTANCE, product);

    // Use beginObject and endObject to build a JsonObject product
    writer.beginObject();
    writer.name("key").value("value");
    writer.endObject();
    JsonElement productAfter = writer.get();
    assertTrue(productAfter.isJsonObject());
    JsonObject obj = productAfter.getAsJsonObject();
    assertEquals("value", obj.get("key").getAsString());
  }

  @Test
    @Timeout(8000)
  void testPeek_accessesTopOfStack() throws Exception {
    // Use reflection to access private peek method
    Method peekMethod = JsonTreeWriter.class.getDeclaredMethod("peek");
    peekMethod.setAccessible(true);

    // Initially stack is empty, peek should throw IndexOutOfBoundsException or similar
    assertThrows(IndexOutOfBoundsException.class, () -> {
      try {
        peekMethod.invoke(writer);
      } catch (Exception e) {
        throw e.getCause();
      }
    });

    // After beginArray, peek should return the JsonArray on top of stack
    writer.beginArray();
    JsonElement top = (JsonElement) peekMethod.invoke(writer);
    assertTrue(top.isJsonArray());

    // After endArray, peek again should throw because stack is empty
    writer.endArray();
    assertThrows(IndexOutOfBoundsException.class, () -> {
      try {
        peekMethod.invoke(writer);
      } catch (Exception e) {
        throw e.getCause();
      }
    });
  }

  @Test
    @Timeout(8000)
  void testPut_addsElementToStackOrObject() throws Exception {
    // Access private put method
    Method putMethod = JsonTreeWriter.class.getDeclaredMethod("put", JsonElement.class);
    putMethod.setAccessible(true);

    // Initially stack empty, put should set product
    JsonPrimitive prim = new JsonPrimitive("test");
    putMethod.invoke(writer, prim);
    JsonElement product = writer.get();
    assertEquals(prim, product);

    // beginObject to push JsonObject to stack
    writer.beginObject();

    // pendingName is null, put with value should throw IllegalStateException
    JsonPrimitive prim2 = new JsonPrimitive("value");
    assertThrows(IllegalStateException.class, () -> {
      try {
        putMethod.invoke(writer, prim2);
      } catch (Exception e) {
        throw e.getCause();
      }
    });

    // set pendingName via name()
    writer.name("name");
    // now put should add property to top JsonObject
    putMethod.invoke(writer, prim2);
    JsonObject obj = (JsonObject) writer.get();
    assertEquals("value", obj.get("name").getAsString());

    // endObject should pop stack
    writer.endObject();

    // After endObject, product should be the JsonObject, no longer an array or multiple elements
    JsonElement afterEnd = writer.get();
    assertTrue(afterEnd.isJsonObject());
  }

  @Test
    @Timeout(8000)
  void testBeginArrayAndEndArray() throws IOException {
    JsonWriter result = writer.beginArray();
    assertSame(writer, result);

    // After beginArray, stack top is JsonArray
    JsonElement top = writer.get();
    assertTrue(top.isJsonArray() || top == JsonNull.INSTANCE);

    JsonWriter endResult = writer.endArray();
    assertSame(writer, endResult);

    // After endArray, product should be JsonArray with elements added or JsonNull if empty
    JsonElement product = writer.get();
    assertTrue(product.isJsonArray() || product == JsonNull.INSTANCE);
  }

  @Test
    @Timeout(8000)
  void testBeginObjectAndEndObject() throws IOException {
    JsonWriter result = writer.beginObject();
    assertSame(writer, result);

    JsonWriter endResult = writer.endObject();
    assertSame(writer, endResult);

    JsonElement product = writer.get();
    assertTrue(product.isJsonObject() || product == JsonNull.INSTANCE);
  }

  @Test
    @Timeout(8000)
  void testName_setsPendingName() throws IOException, NoSuchFieldException, IllegalAccessException {
    JsonWriter result = writer.beginObject();
    assertSame(writer, result);

    JsonWriter nameResult = writer.name("myName");
    assertSame(writer, nameResult);

    Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
    pendingNameField.setAccessible(true);
    String pendingName = (String) pendingNameField.get(writer);
    assertEquals("myName", pendingName);
  }

  @Test
    @Timeout(8000)
  void testValue_StringAndNull() throws IOException {
    writer.beginObject().name("a").value("stringValue").endObject();
    JsonElement product = writer.get();
    assertTrue(product.isJsonObject());
    assertEquals("stringValue", product.getAsJsonObject().get("a").getAsString());

    writer.beginArray().value((String) null).endArray();
    JsonElement arrayProduct = writer.get();
    assertTrue(arrayProduct.isJsonArray());
    assertTrue(arrayProduct.getAsJsonArray().get(0).isJsonNull());
  }

  @Test
    @Timeout(8000)
  void testJsonValue_validAndInvalid() throws IOException {
    writer.beginArray();
    // valid JSON value string
    writer.jsonValue("123");
    JsonElement product = writer.get();
    assertTrue(product.isJsonArray());
    assertEquals(123, product.getAsJsonArray().get(0).getAsInt());

    // invalid JSON string should throw IOException
    assertThrows(IOException.class, () -> writer.jsonValue("{invalid json}"));
    writer.endArray();
  }

  @Test
    @Timeout(8000)
  void testNullValue() throws IOException {
    writer.beginArray().nullValue().endArray();
    JsonElement product = writer.get();
    assertTrue(product.isJsonArray());
    assertTrue(product.getAsJsonArray().get(0).isJsonNull());
  }

  @Test
    @Timeout(8000)
  void testValue_booleanAndBooleanObject() throws IOException {
    writer.beginArray().value(true).value(Boolean.FALSE).endArray();
    JsonElement product = writer.get();
    assertTrue(product.isJsonArray());
    assertEquals(true, product.getAsJsonArray().get(0).getAsBoolean());
    assertEquals(false, product.getAsJsonArray().get(1).getAsBoolean());
  }

  @Test
    @Timeout(8000)
  void testValue_float_double_long_Number() throws IOException {
    writer.beginArray()
        .value(1.23f)
        .value(4.56d)
        .value(789L)
        .value(Integer.valueOf(10))
        .endArray();
    JsonElement product = writer.get();
    assertTrue(product.isJsonArray());
    assertEquals(1.23f, product.getAsJsonArray().get(0).getAsFloat());
    assertEquals(4.56d, product.getAsJsonArray().get(1).getAsDouble());
    assertEquals(789L, product.getAsJsonArray().get(2).getAsLong());
    assertEquals(10, product.getAsJsonArray().get(3).getAsInt());
  }

  @Test
    @Timeout(8000)
  void testFlushAndClose() throws IOException {
    // flush is no-op, just call to ensure no exception
    writer.flush();

    // close should mark writer as closed and throw on further use
    writer.close();

    // After close, calling beginArray throws IllegalStateException
    assertThrows(IllegalStateException.class, () -> writer.beginArray());
  }
}