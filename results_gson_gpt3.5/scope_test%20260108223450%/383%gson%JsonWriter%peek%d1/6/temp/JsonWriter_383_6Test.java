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
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

class JsonWriter_383_6Test {

  @Test
    @Timeout(8000)
  void peek_whenStackSizeZero_throwsIllegalStateException() throws Exception {
    StringWriter stringWriter = new StringWriter();
    JsonWriter writer = new JsonWriter(stringWriter);

    // Set stackSize to 0 explicitly to simulate closed state
    Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(writer, 0);

    Method peekMethod = JsonWriter.class.getDeclaredMethod("peek");
    peekMethod.setAccessible(true);

    IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
      try {
        peekMethod.invoke(writer);
      } catch (InvocationTargetException e) {
        // Rethrow the original cause to be caught by assertThrows
        throw e.getCause();
      }
    });
    assertEquals("JsonWriter is closed.", exception.getMessage());
  }

  @Test
    @Timeout(8000)
  void peek_returnsTopOfStack() throws Exception {
    StringWriter stringWriter = new StringWriter();
    JsonWriter writer = new JsonWriter(stringWriter);

    // Setup stack with some values and stackSize > 0
    Field stackField = JsonWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    int[] stack = new int[32];
    stack[0] = 12345;
    stackField.set(writer, stack);

    Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(writer, 1);

    Method peekMethod = JsonWriter.class.getDeclaredMethod("peek");
    peekMethod.setAccessible(true);

    int result = (int) peekMethod.invoke(writer);
    assertEquals(12345, result);

    // Test with larger stackSize and different top value
    stack[1] = 67890;
    stackSizeField.setInt(writer, 2);
    result = (int) peekMethod.invoke(writer);
    assertEquals(67890, result);
  }
}