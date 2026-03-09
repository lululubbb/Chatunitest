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

public class JsonWriter_382_6Test {
  private JsonWriter jsonWriter;

  @BeforeEach
  public void setUp() {
    // Provide a dummy Writer since it's required by the constructor but not used in push
    jsonWriter = new JsonWriter(mock(java.io.Writer.class));
  }

  @Test
    @Timeout(8000)
  public void testPushAddsNewTopToStackAndIncrementsStackSize() throws Exception {
    // Access private fields
    Field stackField = JsonWriter.class.getDeclaredField("stack");
    Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackField.setAccessible(true);
    stackSizeField.setAccessible(true);

    // Initial state: stackSize=0, stack length=32
    stackSizeField.setInt(jsonWriter, 0);
    stackField.set(jsonWriter, new int[32]);

    // Access private method push
    Method pushMethod = JsonWriter.class.getDeclaredMethod("push", int.class);
    pushMethod.setAccessible(true);

    // Call push with newTop=5
    pushMethod.invoke(jsonWriter, 5);

    // Verify stackSize incremented
    int stackSizeAfter = stackSizeField.getInt(jsonWriter);
    assertEquals(1, stackSizeAfter);

    // Verify stack[0] == 5
    int[] stackAfter = (int[]) stackField.get(jsonWriter);
    assertEquals(5, stackAfter[0]);
  }

  @Test
    @Timeout(8000)
  public void testPushExpandsStackWhenFull() throws Exception {
    Field stackField = JsonWriter.class.getDeclaredField("stack");
    Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackField.setAccessible(true);
    stackSizeField.setAccessible(true);

    // Fill stack to capacity (32)
    int[] fullStack = new int[32];
    for (int i = 0; i < 32; i++) {
      fullStack[i] = i;
    }
    stackField.set(jsonWriter, fullStack);
    stackSizeField.setInt(jsonWriter, 32);

    Method pushMethod = JsonWriter.class.getDeclaredMethod("push", int.class);
    pushMethod.setAccessible(true);

    // Call push with newTop=99, should trigger stack expansion
    pushMethod.invoke(jsonWriter, 99);

    // Verify stackSize incremented to 33
    int stackSizeAfter = stackSizeField.getInt(jsonWriter);
    assertEquals(33, stackSizeAfter);

    // Verify stack length doubled (64)
    int[] stackAfter = (int[]) stackField.get(jsonWriter);
    assertEquals(64, stackAfter.length);

    // Verify old values preserved
    for (int i = 0; i < 32; i++) {
      assertEquals(i, stackAfter[i]);
    }

    // Verify newTop added at position 32
    assertEquals(99, stackAfter[32]);
  }
}