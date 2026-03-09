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
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonWriter_382_2Test {

  private JsonWriter jsonWriter;
  private Method pushMethod;
  private Field stackField;
  private Field stackSizeField;

  @BeforeEach
  public void setUp() throws Exception {
    // Create JsonWriter instance with a dummy Writer since push doesn't use it
    jsonWriter = new JsonWriter(mock(java.io.Writer.class));

    // Access private method push(int)
    pushMethod = JsonWriter.class.getDeclaredMethod("push", int.class);
    pushMethod.setAccessible(true);

    // Access private fields stack and stackSize
    stackField = JsonWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);

    stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  public void testPushAddsElementWhenStackNotFull() throws Exception {
    // Initialize stackSize to 0 and stack length 32 (default)
    stackSizeField.setInt(jsonWriter, 0);
    stackField.set(jsonWriter, new int[32]);

    // Call push with newTop = 5
    pushMethod.invoke(jsonWriter, 5);

    int stackSize = stackSizeField.getInt(jsonWriter);
    int[] stack = (int[]) stackField.get(jsonWriter);

    assertEquals(1, stackSize);
    assertEquals(5, stack[0]);
  }

  @Test
    @Timeout(8000)
  public void testPushDoublesStackWhenFull() throws Exception {
    // Prepare stack full scenario
    int[] initialStack = new int[4];
    for (int i = 0; i < 4; i++) {
      initialStack[i] = i * 10;
    }
    stackField.set(jsonWriter, initialStack);
    stackSizeField.setInt(jsonWriter, 4);

    // Replace stack length to 4 for test (smaller than default 32)
    Field stackFieldInternal = JsonWriter.class.getDeclaredField("stack");
    stackFieldInternal.setAccessible(true);

    // Invoke push with newTop = 99, should double stack length to 8
    pushMethod.invoke(jsonWriter, 99);

    int stackSize = stackSizeField.getInt(jsonWriter);
    int[] stack = (int[]) stackField.get(jsonWriter);

    // stack size increased by 1
    assertEquals(5, stackSize);

    // stack length doubled from 4 to 8
    assertEquals(8, stack.length);

    // Original elements preserved
    assertArrayEquals(new int[] {0, 10, 20, 30, 99, 0, 0, 0}, stack);
  }

  @Test
    @Timeout(8000)
  public void testPushMultiplePushes() throws Exception {
    stackSizeField.setInt(jsonWriter, 0);
    stackField.set(jsonWriter, new int[2]);

    // Push 1
    pushMethod.invoke(jsonWriter, 1);
    // Push 2
    pushMethod.invoke(jsonWriter, 2);
    // Push 3 triggers resize
    pushMethod.invoke(jsonWriter, 3);

    int stackSize = stackSizeField.getInt(jsonWriter);
    int[] stack = (int[]) stackField.get(jsonWriter);

    assertEquals(3, stackSize);
    assertEquals(4, stack.length);
    assertArrayEquals(new int[] {1, 2, 3, 0}, stack);
  }
}