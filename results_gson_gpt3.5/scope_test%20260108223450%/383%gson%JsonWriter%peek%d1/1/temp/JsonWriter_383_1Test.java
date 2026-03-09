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
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonWriter_383_1Test {

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
    // Use reflection to set stackSize to 0 explicitly to simulate closed state
    var stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
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
  void peek_returnsTopOfStack() throws Exception {
    Method pushMethod = JsonWriter.class.getDeclaredMethod("push", int.class);
    pushMethod.setAccessible(true);
    Method peekMethod = JsonWriter.class.getDeclaredMethod("peek");
    peekMethod.setAccessible(true);

    // Push some values onto the stack to set stackSize > 0
    pushMethod.invoke(jsonWriter, JsonScope.EMPTY_OBJECT);
    pushMethod.invoke(jsonWriter, JsonScope.DANGLING_NAME);
    pushMethod.invoke(jsonWriter, JsonScope.NONEMPTY_OBJECT);

    // peek should return the last pushed value (NONEMPTY_OBJECT)
    int top = (int) peekMethod.invoke(jsonWriter);
    assertEquals(JsonScope.NONEMPTY_OBJECT, top);
  }
}