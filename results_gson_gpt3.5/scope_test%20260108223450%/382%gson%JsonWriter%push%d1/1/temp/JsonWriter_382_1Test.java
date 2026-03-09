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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonWriterPushTest {

  private JsonWriter jsonWriter;
  private Writer mockWriter;

  @BeforeEach
  void setUp() {
    mockWriter = mock(Writer.class);
    jsonWriter = new JsonWriter(mockWriter);
  }

  @Test
    @Timeout(8000)
  void push_whenStackNotFull_addsNewTopAndIncrementsStackSize() throws Exception {
    // Access private fields
    Field stackField = JsonWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);

    // Initialize stack with capacity 32 and stackSize less than length
    int[] stack = new int[32];
    stackField.set(jsonWriter, stack);
    stackSizeField.setInt(jsonWriter, 0);

    // Invoke private method push with newTop = 5
    Method pushMethod = JsonWriter.class.getDeclaredMethod("push", int.class);
    pushMethod.setAccessible(true);
    pushMethod.invoke(jsonWriter, 5);

    int stackSize = stackSizeField.getInt(jsonWriter);
    int[] updatedStack = (int[]) stackField.get(jsonWriter);

    assertEquals(1, stackSize, "stackSize should increment by 1");
    assertEquals(5, updatedStack[0], "stack[0] should be newTop value 5");
  }

  @Test
    @Timeout(8000)
  void push_whenStackFull_expandsStackAndAddsNewTop() throws Exception {
    Field stackField = JsonWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);

    // Fill stack to capacity 32
    int[] stack = new int[32];
    for (int i = 0; i < 32; i++) {
      stack[i] = i;
    }
    stackField.set(jsonWriter, stack);
    stackSizeField.setInt(jsonWriter, 32);

    Method pushMethod = JsonWriter.class.getDeclaredMethod("push", int.class);
    pushMethod.setAccessible(true);
    pushMethod.invoke(jsonWriter, 99);

    int stackSize = stackSizeField.getInt(jsonWriter);
    int[] updatedStack = (int[]) stackField.get(jsonWriter);

    // After push, stack size should be 33
    assertEquals(33, stackSize, "stackSize should increment by 1 after expansion");
    // The underlying array should have doubled in size
    assertEquals(64, updatedStack.length, "stack array length should double to 64");
    // The last element should be the newTop value 99
    assertEquals(99, updatedStack[32], "stack[32] should be newTop value 99");
    // The first 32 elements should remain unchanged
    for (int i = 0; i < 32; i++) {
      assertEquals(i, updatedStack[i], "stack[" + i + "] should remain unchanged");
    }
  }
}