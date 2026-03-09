package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import static com.google.gson.stream.JsonScope.DANGLING_NAME;
import static com.google.gson.stream.JsonScope.EMPTY_ARRAY;
import static com.google.gson.stream.JsonScope.EMPTY_DOCUMENT;
import static com.google.gson.stream.JsonScope.EMPTY_OBJECT;
import static com.google.gson.stream.JsonScope.NONEMPTY_ARRAY;
import static com.google.gson.stream.JsonScope.NONEMPTY_DOCUMENT;
import static com.google.gson.stream.JsonScope.NONEMPTY_OBJECT;
import java.io.Closeable;
import java.io.Flushable;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonWriter_382_3Test {

  private JsonWriter jsonWriter;
  private StringWriter stringWriter;

  @BeforeEach
  void setUp() {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);
  }

  @Test
    @Timeout(8000)
  void push_shouldAddNewTopToStack_whenStackNotFull() throws Exception {
    // Setup initial stackSize less than stack length
    setField(jsonWriter, "stackSize", 0);
    setField(jsonWriter, "stack", new int[32]);

    // Invoke private method push with newTop = 42
    invokePush(jsonWriter, 42);

    int stackSize = (int) getField(jsonWriter, "stackSize");
    int[] stack = (int[]) getField(jsonWriter, "stack");

    assertEquals(1, stackSize);
    assertEquals(42, stack[0]);
  }

  @Test
    @Timeout(8000)
  void push_shouldExpandStackAndAddNewTop_whenStackFull() throws Exception {
    int[] initialStack = new int[2];
    initialStack[0] = 10;
    initialStack[1] = 20;

    setField(jsonWriter, "stack", initialStack);
    setField(jsonWriter, "stackSize", 2);

    // Invoke private method push with newTop = 99
    invokePush(jsonWriter, 99);

    int stackSize = (int) getField(jsonWriter, "stackSize");
    int[] stack = (int[]) getField(jsonWriter, "stack");

    // Stack size should be incremented
    assertEquals(3, stackSize);
    // Stack array length should be doubled (2 * 2 = 4)
    assertEquals(4, stack.length);
    // Previous values preserved
    assertEquals(10, stack[0]);
    assertEquals(20, stack[1]);
    // New value added at stackSize - 1 index
    assertEquals(99, stack[2]);
  }

  // Helper to invoke private push method via reflection
  private void invokePush(JsonWriter writer, int newTop) throws Exception {
    Method pushMethod = JsonWriter.class.getDeclaredMethod("push", int.class);
    pushMethod.setAccessible(true);
    pushMethod.invoke(writer, newTop);
  }

  // Helper to get private field value via reflection
  private Object getField(Object obj, String fieldName) throws Exception {
    Field field = JsonWriter.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(obj);
  }

  // Helper to set private field value via reflection
  private void setField(Object obj, String fieldName, Object value) throws Exception {
    Field field = JsonWriter.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(obj, value);
  }
}