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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonWriter_382_5Test {

  private JsonWriter jsonWriter;
  private StringWriter stringWriter;

  @BeforeEach
  public void setUp() {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);
  }

  @Test
    @Timeout(8000)
  public void testPushAddsNewTopToStack() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
    // Use reflection to access private push method
    Method pushMethod = JsonWriter.class.getDeclaredMethod("push", int.class);
    pushMethod.setAccessible(true);

    // Access stack and stackSize fields via reflection once
    Field stackField = JsonWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);

    // Clear stackSize to 0 before starting test to ensure consistent state
    stackSizeField.setInt(jsonWriter, 0);

    // Initially stackSize is 0, stack length is 32
    // Push a value and verify stackSize increments and value is set
    pushMethod.invoke(jsonWriter, 5);

    int[] stack = (int[]) stackField.get(jsonWriter);
    int stackSize = stackSizeField.getInt(jsonWriter);

    assertEquals(1, stackSize);
    assertEquals(5, stack[0]);

    // Push more values to fill to stack length, then push one more to trigger resize
    // Fill stack to length 32
    for (int i = 1; i < 32; i++) {
      pushMethod.invoke(jsonWriter, i);
    }
    stackSize = stackSizeField.getInt(jsonWriter);
    assertEquals(32, stackSize);

    // Now push one more to trigger resize
    pushMethod.invoke(jsonWriter, 99);

    // After resize, stack length should be doubled (64)
    stack = (int[]) stackField.get(jsonWriter);
    stackSize = stackSizeField.getInt(jsonWriter);
    assertEquals(33, stackSize);
    assertEquals(99, stack[32]);
    assertEquals(64, stack.length);

    // Verify that previously pushed values remain intact after resize
    for (int i = 0; i < 32; i++) {
      if (i == 0) {
        assertEquals(5, stack[i]);
      } else {
        assertEquals(i, stack[i]);
      }
    }
  }
}