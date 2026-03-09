package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.stream.JsonReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class JsonTreeReader_234_5Test {

  private JsonTreeReader reader;

  @BeforeEach
  void setUp() {
    // Create a dummy JsonElement for constructor - use JsonNull.INSTANCE as simplest
    reader = new JsonTreeReader(JsonNull.INSTANCE);
  }

  @Test
    @Timeout(8000)
  void peek_stackSizeZero_returnsEndDocument() throws Exception {
    setStackSize(0);
    JsonToken token = reader.peek();
    assertEquals(JsonToken.END_DOCUMENT, token);
  }

  @Test
    @Timeout(8000)
  void peek_iteratorHasNext_object_returnsName() throws Exception {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("key", "value");
    Iterator<?> iterator = jsonObject.entrySet().iterator();

    setStackSize(2);
    setStack(0, jsonObject);
    setStack(1, iterator);

    JsonToken token = reader.peek();
    assertEquals(JsonToken.NAME, token);
  }

  @Test
    @Timeout(8000)
  void peek_iteratorHasNext_array_returnsElementToken() throws Exception {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(new JsonPrimitive("elem"));
    Iterator<?> iterator = jsonArray.iterator();

    setStackSize(2);
    setStack(0, jsonArray);
    setStack(1, iterator);

    // Because peek calls push(iterator.next()) and then peek() recursively,
    // the stack will be updated with the element (JsonPrimitive string)
    JsonToken token = reader.peek();
    assertEquals(JsonToken.STRING, token);

    // After peek, top of stack should be JsonPrimitive string
    Object top = peekStack();
    assertTrue(top instanceof JsonPrimitive);
    assertTrue(((JsonPrimitive) top).isString());
  }

  @Test
    @Timeout(8000)
  void peek_iteratorHasNoNext_object_returnsEndObject() throws Exception {
    JsonObject jsonObject = new JsonObject();
    Iterator<?> emptyIterator = Collections.emptyIterator();

    setStackSize(2);
    setStack(0, jsonObject);
    setStack(1, emptyIterator);

    JsonToken token = reader.peek();
    assertEquals(JsonToken.END_OBJECT, token);
  }

  @Test
    @Timeout(8000)
  void peek_iteratorHasNoNext_array_returnsEndArray() throws Exception {
    JsonArray jsonArray = new JsonArray();
    Iterator<?> emptyIterator = Collections.emptyIterator();

    setStackSize(2);
    setStack(0, jsonArray);
    setStack(1, emptyIterator);

    JsonToken token = reader.peek();
    assertEquals(JsonToken.END_ARRAY, token);
  }

  @Test
    @Timeout(8000)
  void peek_topIsJsonObject_returnsBeginObject() throws Exception {
    JsonObject jsonObject = new JsonObject();

    setStackSize(1);
    setStack(0, jsonObject);

    JsonToken token = reader.peek();
    assertEquals(JsonToken.BEGIN_OBJECT, token);
  }

  @Test
    @Timeout(8000)
  void peek_topIsJsonArray_returnsBeginArray() throws Exception {
    JsonArray jsonArray = new JsonArray();

    setStackSize(1);
    setStack(0, jsonArray);

    JsonToken token = reader.peek();
    assertEquals(JsonToken.BEGIN_ARRAY, token);
  }

  @Test
    @Timeout(8000)
  void peek_topIsJsonPrimitiveString_returnsString() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive("string");

    setStackSize(1);
    setStack(0, primitive);

    JsonToken token = reader.peek();
    assertEquals(JsonToken.STRING, token);
  }

  @Test
    @Timeout(8000)
  void peek_topIsJsonPrimitiveBoolean_returnsBoolean() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive(true);

    setStackSize(1);
    setStack(0, primitive);

    JsonToken token = reader.peek();
    assertEquals(JsonToken.BOOLEAN, token);
  }

  @Test
    @Timeout(8000)
  void peek_topIsJsonPrimitiveNumber_returnsNumber() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive(123);

    setStackSize(1);
    setStack(0, primitive);

    JsonToken token = reader.peek();
    assertEquals(JsonToken.NUMBER, token);
  }

  @Test
    @Timeout(8000)
  void peek_topIsJsonPrimitiveOther_throwsAssertionError() throws Exception {
    JsonPrimitive primitive = Mockito.mock(JsonPrimitive.class);
    Mockito.when(primitive.isString()).thenReturn(false);
    Mockito.when(primitive.isBoolean()).thenReturn(false);
    Mockito.when(primitive.isNumber()).thenReturn(false);

    setStackSize(1);
    setStack(0, primitive);

    assertThrows(AssertionError.class, () -> reader.peek());
  }

  @Test
    @Timeout(8000)
  void peek_topIsJsonNull_returnsNull() throws Exception {
    setStackSize(1);
    setStack(0, JsonNull.INSTANCE);

    JsonToken token = reader.peek();
    assertEquals(JsonToken.NULL, token);
  }

  @Test
    @Timeout(8000)
  void peek_topIsSentinelClosed_throwsIllegalStateException() throws Exception {
    Object sentinelClosed = getSentinelClosed();

    setStackSize(1);
    setStack(0, sentinelClosed);

    IllegalStateException ex = assertThrows(IllegalStateException.class, () -> reader.peek());
    assertEquals("JsonReader is closed", ex.getMessage());
  }

  @Test
    @Timeout(8000)
  void peek_topIsUnsupportedType_throwsMalformedJsonException() throws Exception {
    Object unsupported = new Object();

    setStackSize(1);
    setStack(0, unsupported);

    MalformedJsonException ex = assertThrows(MalformedJsonException.class, () -> reader.peek());
    assertTrue(ex.getMessage().startsWith("Custom JsonElement subclass"));
  }

  // Helper methods to manipulate private fields via reflection

  private void setStackSize(int size) throws Exception {
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(reader, size);
  }

  private void setStack(int index, Object value) throws Exception {
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(reader);
    stack[index] = value;
  }

  private Object peekStack() throws Exception {
    Method peekStackMethod = JsonTreeReader.class.getDeclaredMethod("peekStack");
    peekStackMethod.setAccessible(true);
    return peekStackMethod.invoke(reader);
  }

  private Object getSentinelClosed() throws Exception {
    Field sentinelField = JsonTreeReader.class.getDeclaredField("SENTINEL_CLOSED");
    sentinelField.setAccessible(true);
    return sentinelField.get(null);
  }
}