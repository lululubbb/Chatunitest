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
import static org.mockito.Mockito.*;

import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonWriter_383_2Test {

  private JsonWriter jsonWriter;
  private StringWriter stringWriter;

  @BeforeEach
  void setUp() {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);
  }

  @Test
    @Timeout(8000)
  void peek_whenStackSizeZero_throwsIllegalStateException() throws Exception {
    // Use reflection to set stackSize to 0 explicitly (should be 0 initially)
    Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonWriter, 0);

    Method peekMethod = JsonWriter.class.getDeclaredMethod("peek");
    peekMethod.setAccessible(true);

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      peekMethod.invoke(jsonWriter);
    });
    assertTrue(thrown.getCause() instanceof IllegalStateException);
    assertEquals("JsonWriter is closed.", thrown.getCause().getMessage());
  }

  @Test
    @Timeout(8000)
  void peek_whenStackSizeOne_returnsTopOfStack() throws Exception {
    // Setup stackSize = 1 and stack[0] = some int value, e.g. 42
    Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    Field stackField = JsonWriter.class.getDeclaredField("stack");
    stackSizeField.setAccessible(true);
    stackField.setAccessible(true);

    stackSizeField.setInt(jsonWriter, 1);
    int[] stack = (int[]) stackField.get(jsonWriter);
    stack[0] = 42;

    Method peekMethod = JsonWriter.class.getDeclaredMethod("peek");
    peekMethod.setAccessible(true);

    int result = (int) peekMethod.invoke(jsonWriter);
    assertEquals(42, result);
  }

  @Test
    @Timeout(8000)
  void peek_whenStackSizeMultiple_returnsTopOfStack() throws Exception {
    // Setup stackSize = 3 and stack[2] = 99
    Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    Field stackField = JsonWriter.class.getDeclaredField("stack");
    stackSizeField.setAccessible(true);
    stackField.setAccessible(true);

    stackSizeField.setInt(jsonWriter, 3);
    int[] stack = (int[]) stackField.get(jsonWriter);
    stack[0] = 10;
    stack[1] = 20;
    stack[2] = 99;

    Method peekMethod = JsonWriter.class.getDeclaredMethod("peek");
    peekMethod.setAccessible(true);

    int result = (int) peekMethod.invoke(jsonWriter);
    assertEquals(99, result);
  }
}