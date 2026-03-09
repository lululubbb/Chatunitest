package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.stream.JsonReader;
import java.io.Reader;
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
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class JsonTreeReader_234_2Test {

  private JsonTreeReader reader;

  @BeforeEach
  public void setUp() throws Exception {
    // Create a dummy JsonElement for constructor (can be JsonNull)
    JsonElement element = JsonNull.INSTANCE;
    reader = new JsonTreeReader(element);
  }

  private void setStack(Object[] stack, int stackSize) throws Exception {
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    stackField.set(reader, stack);
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(reader, stackSize);
  }

  private Object peekStack() throws Exception {
    Method peekStackMethod = JsonTreeReader.class.getDeclaredMethod("peekStack");
    peekStackMethod.setAccessible(true);
    return peekStackMethod.invoke(reader);
  }

  private void push(Object newTop) throws Exception {
    Method pushMethod = JsonTreeReader.class.getDeclaredMethod("push", Object.class);
    pushMethod.setAccessible(true);
    pushMethod.invoke(reader, newTop);
  }

  @Test
    @Timeout(8000)
  public void peek_stackSizeZero_returnsEndDocument() throws Exception {
    setStack(new Object[32], 0);
    JsonToken token = reader.peek();
    assertEquals(JsonToken.END_DOCUMENT, token);
  }

  @Test
    @Timeout(8000)
  public void peek_stackTopIsIterator_hasNextTrue_objectContext_returnsName() throws Exception {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("key", "value");
    Iterator<?> iterator = jsonObject.entrySet().iterator();

    Object[] stack = new Object[32];
    stack[0] = jsonObject;
    stack[1] = iterator;

    setStack(stack, 2);

    JsonToken token = reader.peek();
    assertEquals(JsonToken.NAME, token);
  }

  @Test
    @Timeout(8000)
  public void peek_stackTopIsIterator_hasNextTrue_arrayContext_returnsRecursivePeek() throws Exception {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(new JsonPrimitive("elem1"));
    jsonArray.add(new JsonPrimitive(2));

    Iterator<?> iterator = jsonArray.iterator();

    Object[] stack = new Object[32];
    stack[0] = jsonArray;
    stack[1] = iterator;

    setStack(stack, 2);

    JsonToken token = reader.peek();
    // Should push next element and peek again, so first element is STRING
    assertEquals(JsonToken.STRING, token);

    // After peek, stackSize should be 3 (array, iterator, pushed element)
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    int stackSize = stackSizeField.getInt(reader);
    assertEquals(3, stackSize);

    // The top of stack should be the pushed element ("elem1")
    Object top = peekStack();
    assertTrue(top instanceof JsonPrimitive);
    assertTrue(((JsonPrimitive) top).isString());
  }

  @Test
    @Timeout(8000)
  public void peek_stackTopIsIterator_hasNextFalse_objectContext_returnsEndObject() throws Exception {
    JsonObject jsonObject = new JsonObject();
    Iterator<?> iterator = Arrays.<Map.Entry<String, JsonElement>>asList().iterator();

    Object[] stack = new Object[32];
    stack[0] = jsonObject;
    stack[1] = iterator;

    setStack(stack, 2);

    JsonToken token = reader.peek();
    assertEquals(JsonToken.END_OBJECT, token);
  }

  @Test
    @Timeout(8000)
  public void peek_stackTopIsIterator_hasNextFalse_arrayContext_returnsEndArray() throws Exception {
    JsonArray jsonArray = new JsonArray();
    Iterator<?> iterator = Arrays.<JsonElement>asList().iterator();

    Object[] stack = new Object[32];
    stack[0] = jsonArray;
    stack[1] = iterator;

    setStack(stack, 2);

    JsonToken token = reader.peek();
    assertEquals(JsonToken.END_ARRAY, token);
  }

  @Test
    @Timeout(8000)
  public void peek_stackTopIsJsonObject_returnsBeginObject() throws Exception {
    JsonObject jsonObject = new JsonObject();

    Object[] stack = new Object[32];
    stack[0] = jsonObject;

    setStack(stack, 1);

    JsonToken token = reader.peek();
    assertEquals(JsonToken.BEGIN_OBJECT, token);
  }

  @Test
    @Timeout(8000)
  public void peek_stackTopIsJsonArray_returnsBeginArray() throws Exception {
    JsonArray jsonArray = new JsonArray();

    Object[] stack = new Object[32];
    stack[0] = jsonArray;

    setStack(stack, 1);

    JsonToken token = reader.peek();
    assertEquals(JsonToken.BEGIN_ARRAY, token);
  }

  @Test
    @Timeout(8000)
  public void peek_stackTopIsJsonPrimitive_string_returnsString() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive("string");

    Object[] stack = new Object[32];
    stack[0] = primitive;

    setStack(stack, 1);

    JsonToken token = reader.peek();
    assertEquals(JsonToken.STRING, token);
  }

  @Test
    @Timeout(8000)
  public void peek_stackTopIsJsonPrimitive_boolean_returnsBoolean() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive(true);

    Object[] stack = new Object[32];
    stack[0] = primitive;

    setStack(stack, 1);

    JsonToken token = reader.peek();
    assertEquals(JsonToken.BOOLEAN, token);
  }

  @Test
    @Timeout(8000)
  public void peek_stackTopIsJsonPrimitive_number_returnsNumber() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive(123);

    Object[] stack = new Object[32];
    stack[0] = primitive;

    setStack(stack, 1);

    JsonToken token = reader.peek();
    assertEquals(JsonToken.NUMBER, token);
  }

  @Test
    @Timeout(8000)
  public void peek_stackTopIsJsonPrimitive_other_throwsAssertionError() throws Exception {
    JsonPrimitive primitive = mock(JsonPrimitive.class);
    when(primitive.isString()).thenReturn(false);
    when(primitive.isBoolean()).thenReturn(false);
    when(primitive.isNumber()).thenReturn(false);

    Object[] stack = new Object[32];
    stack[0] = primitive;

    setStack(stack, 1);

    AssertionError error = assertThrows(AssertionError.class, () -> reader.peek());
    assertNotNull(error);
  }

  @Test
    @Timeout(8000)
  public void peek_stackTopIsJsonNull_returnsNull() throws Exception {
    Object[] stack = new Object[32];
    stack[0] = JsonNull.INSTANCE;

    setStack(stack, 1);

    JsonToken token = reader.peek();
    assertEquals(JsonToken.NULL, token);
  }

  @Test
    @Timeout(8000)
  public void peek_stackTopIsSentinelClosed_throwsIllegalStateException() throws Exception {
    Field sentinelField = JsonTreeReader.class.getDeclaredField("SENTINEL_CLOSED");
    sentinelField.setAccessible(true);
    Object sentinel = sentinelField.get(null);

    Object[] stack = new Object[32];
    stack[0] = sentinel;

    setStack(stack, 1);

    IllegalStateException ex = assertThrows(IllegalStateException.class, () -> reader.peek());
    assertEquals("JsonReader is closed", ex.getMessage());
  }

  @Test
    @Timeout(8000)
  public void peek_stackTopIsUnsupportedType_throwsMalformedJsonException() throws Exception {
    Object unsupported = new Object();

    Object[] stack = new Object[32];
    stack[0] = unsupported;

    setStack(stack, 1);

    MalformedJsonException ex = assertThrows(MalformedJsonException.class, () -> reader.peek());
    assertTrue(ex.getMessage().contains("Custom JsonElement subclass"));
    assertTrue(ex.getMessage().contains(unsupported.getClass().getName()));
  }
}