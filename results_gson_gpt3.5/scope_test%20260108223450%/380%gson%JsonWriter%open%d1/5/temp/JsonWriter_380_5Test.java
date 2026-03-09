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
import java.io.Writer;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonWriterOpenTest {
  private Writer mockWriter;
  private JsonWriter jsonWriter;

  @BeforeEach
  public void setUp() {
    mockWriter = mock(Writer.class);
    jsonWriter = new JsonWriter(mockWriter);
  }

  @Test
    @Timeout(8000)
  public void testOpen_emptyArray() throws Throwable {
    invokeOpenAndVerify(JsonScope.EMPTY_ARRAY, '[');
  }

  @Test
    @Timeout(8000)
  public void testOpen_emptyDocument() throws Throwable {
    invokeOpenAndVerify(JsonScope.EMPTY_DOCUMENT, '{');
  }

  @Test
    @Timeout(8000)
  public void testOpen_emptyObject() throws Throwable {
    invokeOpenAndVerify(JsonScope.EMPTY_OBJECT, '{');
  }

  @Test
    @Timeout(8000)
  public void testOpen_nonemptyArray() throws Throwable {
    invokeOpenAndVerify(JsonScope.NONEMPTY_ARRAY, '[');
  }

  @Test
    @Timeout(8000)
  public void testOpen_nonemptyDocument() throws Throwable {
    invokeOpenAndVerify(JsonScope.NONEMPTY_DOCUMENT, '{');
  }

  @Test
    @Timeout(8000)
  public void testOpen_nonemptyObject() throws Throwable {
    invokeOpenAndVerify(JsonScope.NONEMPTY_OBJECT, '{');
  }

  @Test
    @Timeout(8000)
  public void testOpen_danglingName() throws Throwable {
    invokeOpenAndVerify(JsonScope.DANGLING_NAME, '{');
  }

  private void invokeOpenAndVerify(int scope, char openBracket) throws Throwable {
    Method openMethod = JsonWriter.class.getDeclaredMethod("open", int.class, char.class);
    openMethod.setAccessible(true);

    int beforeStackSize = getStackSize(jsonWriter);

    try {
      Object result = openMethod.invoke(jsonWriter, scope, openBracket);

      verify(mockWriter).write(openBracket);

      int afterStackSize = getStackSize(jsonWriter);
      assertEquals(beforeStackSize + 1, afterStackSize);

      int top = getTopOfStack(jsonWriter);
      assertEquals(scope, top);

      assertNotNull(result);
      assertSame(jsonWriter, result);
    } catch (Throwable t) {
      Throwable cause = t.getCause();
      if (cause instanceof IOException) {
        throw cause;
      }
      throw t;
    }
  }

  private int getStackSize(JsonWriter writer) throws Exception {
    var stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    return (int) stackSizeField.get(writer);
  }

  private int getTopOfStack(JsonWriter writer) throws Exception {
    var stackField = JsonWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    int[] stack = (int[]) stackField.get(writer);

    int stackSize = getStackSize(writer);
    if (stackSize == 0) {
      throw new IllegalStateException("Stack is empty");
    }
    return stack[stackSize - 1];
  }
}