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

class JsonTreeWriter_503_1Test {

  private JsonTreeWriter writer;

  @BeforeEach
  void setUp() {
    writer = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  void testConstructor_initialState() throws Exception {
    // product should be JsonNull.INSTANCE
    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);
    JsonElement product = (JsonElement) productField.get(writer);
    assertEquals(JsonNull.INSTANCE, product);

    // stack should be empty
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<?> stack = (List<?>) stackField.get(writer);
    assertTrue(stack.isEmpty());

    // pendingName should be null
    Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
    pendingNameField.setAccessible(true);
    String pendingName = (String) pendingNameField.get(writer);
    assertNull(pendingName);
  }

  @Test
    @Timeout(8000)
  void testGet_returnsProduct() throws Exception {
    // Initially product is JsonNull.INSTANCE
    Method getMethod = JsonTreeWriter.class.getDeclaredMethod("get");
    getMethod.setAccessible(true);
    Object product = getMethod.invoke(writer);
    assertEquals(JsonNull.INSTANCE, product);

    // Push a JsonPrimitive and check get returns it
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<JsonElement> stack = (List<JsonElement>) stackField.get(writer);
    JsonPrimitive prim = new JsonPrimitive("test");
    stack.clear();
    stack.add(prim);

    // Clear product field to null to avoid conflict with stack
    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);
    productField.set(writer, null);

    // Because get() expects exactly one element on the stack,
    // and product == null, get() should return the single element on stack
    Object productAfterPush = getMethod.invoke(writer);
    assertEquals(prim, productAfterPush);
  }

  @Test
    @Timeout(8000)
  void testPeek_returnsTopElement() throws Exception {
    Method peekMethod = JsonTreeWriter.class.getDeclaredMethod("peek");
    peekMethod.setAccessible(true);

    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<JsonElement> stack = (List<JsonElement>) stackField.get(writer);

    // empty stack should throw IllegalStateException
    assertThrows(IllegalStateException.class, () -> peekMethod.invoke(writer));

    // add element and peek should return it
    JsonPrimitive prim = new JsonPrimitive("peek");
    stack.add(prim);
    Object top = peekMethod.invoke(writer);
    assertEquals(prim, top);
  }

  @Test
    @Timeout(8000)
  void testPut_addsElementCorrectly() throws Exception {
    Method putMethod = JsonTreeWriter.class.getDeclaredMethod("put", JsonElement.class);
    putMethod.setAccessible(true);

    Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
    pendingNameField.setAccessible(true);

    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<JsonElement> stack = (List<JsonElement>) stackField.get(writer);

    // Case 1: pendingName != null, top of stack is JsonObject
    JsonObject obj = new JsonObject();
    stack.clear();
    stack.add(obj);
    pendingNameField.set(writer, "key");
    JsonPrimitive value = new JsonPrimitive("value");
    putMethod.invoke(writer, value);
    assertEquals(value, obj.get("key"));
    assertNull(pendingNameField.get(writer));

    // Case 2: pendingName == null, stack empty, product should be set
    stack.clear();
    pendingNameField.set(writer, null);
    putMethod.invoke(writer, value);
    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);
    JsonElement product = (JsonElement) productField.get(writer);
    assertEquals(value, product);

    // Case 3: pendingName == null, stack top is JsonArray, value added to array
    JsonArray array = new JsonArray();
    stack.clear();
    stack.add(array);
    putMethod.invoke(writer, value);
    assertEquals(value, array.get(array.size() - 1));

    // Case 4: pendingName == null, stack top is JsonPrimitive (invalid), should throw IllegalStateException
    stack.clear();
    stack.add(new JsonPrimitive("prim"));
    IllegalStateException ex = assertThrows(IllegalStateException.class,
        () -> putMethod.invoke(writer, value));
    assertTrue(ex.getCause() instanceof IllegalStateException);
  }

  @Test
    @Timeout(8000)
  void testBeginArray_and_EndArray() throws IOException, Exception {
    JsonWriter returned = writer.beginArray();
    assertSame(writer, returned);

    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<JsonElement> stack = (List<JsonElement>) stackField.get(writer);
    assertFalse(stack.isEmpty());
    assertTrue(stack.get(stack.size() - 1) instanceof JsonArray);

    // End array should remove last element and return this
    JsonWriter endReturned = writer.endArray();
    assertSame(writer, endReturned);
    assertTrue(stack.isEmpty());

    // End array on empty stack should throw IllegalStateException
    assertThrows(IllegalStateException.class, () -> writer.endArray());
  }

  @Test
    @Timeout(8000)
  void testBeginObject_and_EndObject() throws IOException, Exception {
    JsonWriter returned = writer.beginObject();
    assertSame(writer, returned);

    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<JsonElement> stack = (List<JsonElement>) stackField.get(writer);
    assertFalse(stack.isEmpty());
    assertTrue(stack.get(stack.size() - 1) instanceof JsonObject);

    // End object should remove last element and return this
    JsonWriter endReturned = writer.endObject();
    assertSame(writer, endReturned);
    assertTrue(stack.isEmpty());

    // End object on empty stack should throw IllegalStateException
    assertThrows(IllegalStateException.class, () -> writer.endObject());
  }

  @Test
    @Timeout(8000)
  void testName_setsPendingName() throws IOException {
    JsonWriter returned = writer.beginObject();
    assertSame(writer, returned);
    JsonWriter nameReturned = writer.name("myName");
    assertSame(writer, nameReturned);

    // Setting name again without writing value should throw IllegalStateException
    IllegalStateException ex = assertThrows(IllegalStateException.class, () -> writer.name("otherName"));
    assertEquals("Name has already been set", ex.getMessage());
  }

  @Test
    @Timeout(8000)
  void testValue_methods() throws IOException {
    writer.beginArray();

    // value(String)
    JsonWriter ret = writer.value("stringValue");
    assertSame(writer, ret);

    // value(boolean)
    ret = writer.value(true);
    assertSame(writer, ret);

    // value(Boolean)
    ret = writer.value(Boolean.FALSE);
    assertSame(writer, ret);

    // value(float)
    ret = writer.value(1.23f);
    assertSame(writer, ret);

    // value(double)
    ret = writer.value(4.56d);
    assertSame(writer, ret);

    // value(long)
    ret = writer.value(789L);
    assertSame(writer, ret);

    // value(Number)
    ret = writer.value(Integer.valueOf(100));
    assertSame(writer, ret);

    // nullValue()
    ret = writer.nullValue();
    assertSame(writer, ret);
  }

  @Test
    @Timeout(8000)
  void testJsonValue() throws IOException {
    writer.beginArray();

    JsonWriter returned = writer.jsonValue("true");
    assertSame(writer, returned);
  }

  @Test
    @Timeout(8000)
  void testFlush_and_Close() throws IOException, Exception {
    // flush does nothing but should not throw
    writer.flush();

    // close sets product to SENTINEL_CLOSED
    writer.close();

    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);
    JsonElement product = (JsonElement) productField.get(writer);
    Field sentinelField = JsonTreeWriter.class.getDeclaredField("SENTINEL_CLOSED");
    sentinelField.setAccessible(true);
    JsonPrimitive sentinel = (JsonPrimitive) sentinelField.get(null);
    assertEquals(sentinel, product);

    // close again should throw IOException
    IOException ex = assertThrows(IOException.class, () -> writer.close());
    assertEquals("JsonWriter is closed.", ex.getMessage());
  }
}