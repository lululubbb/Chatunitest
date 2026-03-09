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

  @BeforeEach
  void setUp() {
    jsonWriter = new JsonWriter(mock(Writer.class));
  }

  @Test
    @Timeout(8000)
  void push_whenStackNotFull_addsNewTopAndIncrementsStackSize() throws Exception {
    // Access private fields
    Field stackField = JsonWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);

    // Initialize stack and stackSize to known state
    int[] stack = new int[32];
    Arrays.fill(stack, -1);
    stackField.set(jsonWriter, stack);
    stackSizeField.setInt(jsonWriter, 0);

    // Access private method push
    Method pushMethod = JsonWriter.class.getDeclaredMethod("push", int.class);
    pushMethod.setAccessible(true);

    // Call push with newTop = 5
    pushMethod.invoke(jsonWriter, 5);

    // Verify stackSize incremented and newTop added at position 0
    int stackSize = stackSizeField.getInt(jsonWriter);
    int[] updatedStack = (int[]) stackField.get(jsonWriter);

    assertEquals(1, stackSize);
    assertEquals(5, updatedStack[0]);
  }

  @Test
    @Timeout(8000)
  void push_whenStackFull_expandsStackAndAddsNewTop() throws Exception {
    // Access private fields
    Field stackField = JsonWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);

    // Fill stack fully
    int[] fullStack = new int[32];
    for (int i = 0; i < 32; i++) {
      fullStack[i] = i;
    }
    stackField.set(jsonWriter, fullStack);
    stackSizeField.setInt(jsonWriter, 32);

    // Access private method push
    Method pushMethod = JsonWriter.class.getDeclaredMethod("push", int.class);
    pushMethod.setAccessible(true);

    // Call push with newTop = 99
    pushMethod.invoke(jsonWriter, 99);

    // Verify stackSize incremented and stack expanded
    int stackSize = stackSizeField.getInt(jsonWriter);
    int[] updatedStack = (int[]) stackField.get(jsonWriter);

    assertEquals(33, stackSize);
    assertEquals(99, updatedStack[32]);
    assertEquals(64, updatedStack.length);

    // Verify old values preserved
    for (int i = 0; i < 32; i++) {
      assertEquals(i, updatedStack[i]);
    }
  }
}