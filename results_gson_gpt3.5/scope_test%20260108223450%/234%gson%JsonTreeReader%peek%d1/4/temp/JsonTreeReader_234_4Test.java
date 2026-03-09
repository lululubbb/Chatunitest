package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.stream.JsonReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class JsonTreeReader_234_4Test {

  private JsonTreeReader reader;

  @BeforeEach
  public void setUp() throws Exception {
    // Create a dummy JsonElement for constructor (can be JsonNull)
    JsonElement element = JsonNull.INSTANCE;
    reader = new JsonTreeReader(element);

    // Use reflection to set stackSize and stack for test setup
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);

    Object[] stack = new Object[32];
    stackField.set(reader, stack);
    stackSizeField.setInt(reader, 0);
  }

  private void setStackAndSize(Object[] stackContent, int size) throws Exception {
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(reader);
    System.arraycopy(stackContent, 0, stack, 0, size);
    stackSizeField.setInt(reader, size);
  }

  private Object peekStack() throws Exception {
    Method peekStackMethod = JsonTreeReader.class.getDeclaredMethod("peekStack");
    peekStackMethod.setAccessible(true);
    return peekStackMethod.invoke(reader);
  }

  private void pushToStack(Object newTop) throws Exception {
    Method pushMethod = JsonTreeReader.class.getDeclaredMethod("push", Object.class);
    pushMethod.setAccessible(true);
    pushMethod.invoke(reader, newTop);
  }

  @Test
    @Timeout(8000)
  public void peek_stackSizeZero_returnsEndDocument() throws IOException {
    // stackSize == 0
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(reader, 0);

    JsonToken token = reader.peek();
    assertEquals(JsonToken.END_DOCUMENT, token);
  }

  @Test
    @Timeout(8000)
  public void peek_stackTopIsIterator_hasNextTrue_objectContext_returnsName() throws Exception {
    // Setup stack so that top is Iterator with hasNext true and second top is JsonObject
    JsonObject obj = new JsonObject();
    Iterator<String> iterator = Collections.singleton("key").iterator();

    Object[] stackContent = new Object[32];
    stackContent[0] = obj; // second top
    stackContent[1] = iterator; // top

    setStackAndSize(stackContent, 2);

    JsonToken token = reader.peek();
    assertEquals(JsonToken.NAME, token);
  }

  @Test
    @Timeout(8000)
  public void peek_stackTopIsIterator_hasNextTrue_arrayContext_pushesNextAndRecurses() throws Exception {
    // Setup stack so that top is Iterator with hasNext true and second top is JsonArray
    JsonArray array = new JsonArray();
    array.add(new JsonPrimitive("value"));
    Iterator<JsonElement> iterator = array.iterator();

    Object[] stackContent = new Object[32];
    stackContent[0] = array; // second top
    stackContent[1] = iterator; // top

    setStackAndSize(stackContent, 2);

    JsonToken token = reader.peek();
    // The call pushes next element and recurses, so expected token is STRING (from JsonPrimitive string)
    assertEquals(JsonToken.STRING, token);
  }

  @Test
    @Timeout(8000)
  public void peek_stackTopIsIterator_hasNextFalse_objectContext_returnsEndObject() throws Exception {
    JsonObject obj = new JsonObject();
    Iterator<?> emptyIterator = Collections.emptyIterator();

    Object[] stackContent = new Object[32];
    stackContent[0] = obj; // second top
    stackContent[1] = emptyIterator; // top

    setStackAndSize(stackContent, 2);

    JsonToken token = reader.peek();
    assertEquals(JsonToken.END_OBJECT, token);
  }

  @Test
    @Timeout(8000)
  public void peek_stackTopIsIterator_hasNextFalse_arrayContext_returnsEndArray() throws Exception {
    JsonArray array = new JsonArray();
    Iterator<?> emptyIterator = Collections.emptyIterator();

    Object[] stackContent = new Object[32];
    stackContent[0] = array; // second top
    stackContent[1] = emptyIterator; // top

    setStackAndSize(stackContent, 2);

    JsonToken token = reader.peek();
    assertEquals(JsonToken.END_ARRAY, token);
  }

  @Test
    @Timeout(8000)
  public void peek_stackTopIsJsonObject_returnsBeginObject() throws Exception {
    JsonObject obj = new JsonObject();

    Object[] stackContent = new Object[32];
    stackContent[0] = obj;

    setStackAndSize(stackContent, 1);

    JsonToken token = reader.peek();
    assertEquals(JsonToken.BEGIN_OBJECT, token);
  }

  @Test
    @Timeout(8000)
  public void peek_stackTopIsJsonArray_returnsBeginArray() throws Exception {
    JsonArray array = new JsonArray();

    Object[] stackContent = new Object[32];
    stackContent[0] = array;

    setStackAndSize(stackContent, 1);

    JsonToken token = reader.peek();
    assertEquals(JsonToken.BEGIN_ARRAY, token);
  }

  @Test
    @Timeout(8000)
  public void peek_stackTopIsJsonPrimitive_string_returnsString() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive("abc");

    Object[] stackContent = new Object[32];
    stackContent[0] = primitive;

    setStackAndSize(stackContent, 1);

    JsonToken token = reader.peek();
    assertEquals(JsonToken.STRING, token);
  }

  @Test
    @Timeout(8000)
  public void peek_stackTopIsJsonPrimitive_boolean_returnsBoolean() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive(true);

    Object[] stackContent = new Object[32];
    stackContent[0] = primitive;

    setStackAndSize(stackContent, 1);

    JsonToken token = reader.peek();
    assertEquals(JsonToken.BOOLEAN, token);
  }

  @Test
    @Timeout(8000)
  public void peek_stackTopIsJsonPrimitive_number_returnsNumber() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive(42);

    Object[] stackContent = new Object[32];
    stackContent[0] = primitive;

    setStackAndSize(stackContent, 1);

    JsonToken token = reader.peek();
    assertEquals(JsonToken.NUMBER, token);
  }

  @Test
    @Timeout(8000)
  public void peek_stackTopIsJsonPrimitive_invalid_throwsAssertionError() throws Exception {
    // Create a JsonPrimitive subclass with all false for isString, isBoolean, isNumber
    JsonPrimitive primitive = mock(JsonPrimitive.class);
    when(primitive.isString()).thenReturn(false);
    when(primitive.isBoolean()).thenReturn(false);
    when(primitive.isNumber()).thenReturn(false);

    Object[] stackContent = new Object[32];
    stackContent[0] = primitive;

    setStackAndSize(stackContent, 1);

    AssertionError thrown = assertThrows(AssertionError.class, () -> reader.peek());
    assertNotNull(thrown);
  }

  @Test
    @Timeout(8000)
  public void peek_stackTopIsJsonNull_returnsNull() throws Exception {
    Object[] stackContent = new Object[32];
    stackContent[0] = JsonNull.INSTANCE;

    setStackAndSize(stackContent, 1);

    JsonToken token = reader.peek();
    assertEquals(JsonToken.NULL, token);
  }

  @Test
    @Timeout(8000)
  public void peek_stackTopIsSentinelClosed_throwsIllegalStateException() throws Exception {
    Field sentinelField = JsonTreeReader.class.getDeclaredField("SENTINEL_CLOSED");
    sentinelField.setAccessible(true);
    Object sentinelClosed = sentinelField.get(null);

    Object[] stackContent = new Object[32];
    stackContent[0] = sentinelClosed;

    setStackAndSize(stackContent, 1);

    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> reader.peek());
    assertEquals("JsonReader is closed", thrown.getMessage());
  }

  @Test
    @Timeout(8000)
  public void peek_stackTopIsUnknown_throwsMalformedJsonException() throws Exception {
    Object unknown = new Object();

    Object[] stackContent = new Object[32];
    stackContent[0] = unknown;

    setStackAndSize(stackContent, 1);

    MalformedJsonException thrown = assertThrows(MalformedJsonException.class, () -> reader.peek());
    assertTrue(thrown.getMessage().contains("Custom JsonElement subclass"));
    assertTrue(thrown.getMessage().contains(unknown.getClass().getName()));
  }
}