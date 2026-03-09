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

class JsonTreeWriter_503_6Test {

  private JsonTreeWriter writer;

  @BeforeEach
  void setUp() {
    writer = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  void constructor_initializesFieldsCorrectly() throws Exception {
    // Validate that the writer's stack is empty initially
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<?> stack = (List<?>) stackField.get(writer);
    assertNotNull(stack);
    assertTrue(stack.isEmpty());

    // pendingName should be null
    Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
    pendingNameField.setAccessible(true);
    assertNull(pendingNameField.get(writer));

    // product should be JsonNull.INSTANCE
    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);
    JsonElement product = (JsonElement) productField.get(writer);
    assertSame(JsonNull.INSTANCE, product);
  }

  @Test
    @Timeout(8000)
  void get_returnsProduct() throws Exception {
    // Initially product is JsonNull.INSTANCE
    Method getMethod = JsonTreeWriter.class.getDeclaredMethod("get");
    getMethod.setAccessible(true);
    JsonElement result = (JsonElement) getMethod.invoke(writer);
    assertSame(JsonNull.INSTANCE, result);

    // Modify product field and verify get returns updated value
    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);
    JsonObject obj = new JsonObject();
    productField.set(writer, obj);
    JsonElement result2 = (JsonElement) getMethod.invoke(writer);
    assertSame(obj, result2);
  }

  @Test
    @Timeout(8000)
  void peek_returnsTopOfStack() throws Exception {
    Method peekMethod = JsonTreeWriter.class.getDeclaredMethod("peek");
    peekMethod.setAccessible(true);

    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<JsonElement> stack = (List<JsonElement>) stackField.get(writer);

    // Empty stack should throw IndexOutOfBoundsException or similar
    assertThrows(IndexOutOfBoundsException.class, () -> peekMethod.invoke(writer));

    // Add element and peek should return it
    JsonPrimitive prim = new JsonPrimitive("test");
    stack.add(prim);
    JsonElement peeked = (JsonElement) peekMethod.invoke(writer);
    assertSame(prim, peeked);
  }

  @Test
    @Timeout(8000)
  void put_addsElementToStackOrProduct() throws Exception {
    Method putMethod = JsonTreeWriter.class.getDeclaredMethod("put", JsonElement.class);
    putMethod.setAccessible(true);

    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<JsonElement> stack = (List<JsonElement>) stackField.get(writer);

    Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
    pendingNameField.setAccessible(true);

    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);

    // Case 1: stack empty, product should be set to value
    JsonPrimitive prim1 = new JsonPrimitive("value1");
    putMethod.invoke(writer, prim1);
    assertSame(prim1, productField.get(writer));
    assertTrue(stack.isEmpty());

    // Case 2: stack non-empty, pendingName null, top is JsonArray, value should be added to array
    stack.add(new JsonArray());
    pendingNameField.set(writer, null);
    JsonPrimitive prim2 = new JsonPrimitive("value2");
    putMethod.invoke(writer, prim2);
    JsonArray array = (JsonArray) stack.get(0);
    assertEquals(1, array.size());
    assertEquals(prim2, array.get(0));
    assertSame(prim1, productField.get(writer)); // product unchanged

    // Case 3: stack non-empty, pendingName set, top is JsonObject, value should be added with name
    JsonObject obj = new JsonObject();
    stack.clear();
    stack.add(obj);
    pendingNameField.set(writer, "name1");
    JsonPrimitive prim3 = new JsonPrimitive("value3");
    putMethod.invoke(writer, prim3);
    assertEquals(prim3, obj.get("name1"));
    assertNull(pendingNameField.get(writer));
  }

  @Test
    @Timeout(8000)
  void beginArray_and_endArray_behavior() throws IOException {
    JsonWriter result = writer.beginArray();
    assertSame(writer, result);

    List<JsonElement> stack;
    JsonElement product;
    try {
      stack = getStackField();
      assertFalse(stack.isEmpty());
      assertTrue(stack.get(stack.size() - 1) instanceof JsonArray);

      writer.endArray();

      product = getProductField();
    } catch (Exception e) {
      throw new IOException(e);
    }

    // After endArray, if stack empty, product should be the JsonArray
    assertTrue(product instanceof JsonArray);
    assertTrue(stack.isEmpty());
  }

  @Test
    @Timeout(8000)
  void beginObject_and_endObject_behavior() throws IOException {
    JsonWriter result = writer.beginObject();
    assertSame(writer, result);

    List<JsonElement> stack;
    JsonElement product;
    try {
      stack = getStackField();
      assertFalse(stack.isEmpty());
      assertTrue(stack.get(stack.size() - 1) instanceof JsonObject);

      writer.endObject();

      product = getProductField();
    } catch (Exception e) {
      throw new IOException(e);
    }

    assertTrue(product instanceof JsonObject);
    assertTrue(stack.isEmpty());
  }

  @Test
    @Timeout(8000)
  void name_setsPendingName() throws IOException {
    writer.beginObject();
    JsonWriter result = writer.name("fieldName");
    assertSame(writer, result);

    String pendingName;
    try {
      pendingName = getPendingNameField();
    } catch (Exception e) {
      throw new IOException(e);
    }
    assertEquals("fieldName", pendingName);
  }

  @Test
    @Timeout(8000)
  void value_and_nullValue_addsCorrectJsonPrimitives() throws IOException {
    writer.beginArray();

    writer.value("stringValue");
    writer.value(true);
    writer.value(false);
    writer.value(123L);
    writer.value(1.23f);
    writer.value(4.56d);
    writer.value((Number) 789);
    writer.nullValue();

    JsonArray array;
    try {
      array = (JsonArray) getStackField().get(0);
    } catch (Exception e) {
      throw new IOException(e);
    }
    assertEquals(8, array.size());
    assertEquals("stringValue", array.get(0).getAsString());
    assertTrue(array.get(1).getAsBoolean());
    assertFalse(array.get(2).getAsBoolean());
    assertEquals(123L, array.get(3).getAsLong());
    assertEquals(1.23f, array.get(4).getAsFloat(), 0.0001);
    assertEquals(4.56d, array.get(5).getAsDouble(), 0.0001);
    assertEquals(789, array.get(6).getAsInt());
    assertTrue(array.get(7).isJsonNull());
  }

  @Test
    @Timeout(8000)
  void jsonValue_addsParsedJsonElement() throws IOException {
    writer.beginArray();
    writer.jsonValue("\"hello\"");
    JsonArray array;
    try {
      array = (JsonArray) getStackField().get(0);
    } catch (Exception e) {
      throw new IOException(e);
    }
    assertEquals(1, array.size());
    assertTrue(array.get(0).isJsonPrimitive());
    assertEquals("hello", array.get(0).getAsString());
  }

  @Test
    @Timeout(8000)
  void close_setsProductToSentinelClosed() throws IOException {
    writer.beginArray();
    writer.close();

    JsonElement product;
    JsonPrimitive sentinel;
    try {
      product = getProductField();
      Field sentinelField = JsonTreeWriter.class.getDeclaredField("SENTINEL_CLOSED");
      sentinelField.setAccessible(true);
      sentinel = (JsonPrimitive) sentinelField.get(null);
    } catch (Exception e) {
      throw new IOException(e);
    }

    assertSame(sentinel, product);

    // Further calls to beginArray should throw IllegalStateException
    assertThrows(IllegalStateException.class, () -> writer.beginArray());
  }

  @Test
    @Timeout(8000)
  void flush_doesNothing() throws IOException {
    // flush should not throw or change state
    writer.flush();
  }

  // Helper methods to access private fields

  @SuppressWarnings("unchecked")
  private List<JsonElement> getStackField() throws Exception {
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    return (List<JsonElement>) stackField.get(writer);
  }

  private JsonElement getProductField() throws Exception {
    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);
    return (JsonElement) productField.get(writer);
  }

  private String getPendingNameField() throws Exception {
    Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
    pendingNameField.setAccessible(true);
    return (String) pendingNameField.get(writer);
  }
}