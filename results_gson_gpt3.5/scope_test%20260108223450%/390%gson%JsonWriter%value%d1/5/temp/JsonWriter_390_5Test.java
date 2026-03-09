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

import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

class JsonWriter_value_Test {
  private StringWriter stringWriter;
  private JsonWriter jsonWriter;

  @BeforeEach
  void setUp() throws Exception {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);

    try {
      // Initialize stackSize to 1 and stack[0] to EMPTY_DOCUMENT (0) to avoid IllegalStateException in beforeValue()
      Field stackField = JsonWriter.class.getDeclaredField("stack");
      stackField.setAccessible(true);
      int[] stack = (int[]) stackField.get(jsonWriter);
      stack[0] = 0; // EMPTY_DOCUMENT == 0

      Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
      stackSizeField.setAccessible(true);
      stackSizeField.setInt(jsonWriter, 1);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
    @Timeout(8000)
  void valueTrue_writesTrue() throws IOException {
    JsonWriter returned = jsonWriter.value(true);
    assertSame(jsonWriter, returned);
    assertEquals("true", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void valueFalse_writesFalse() throws IOException {
    JsonWriter returned = jsonWriter.value(false);
    assertSame(jsonWriter, returned);
    assertEquals("false", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void value_withDeferredName_writesNameAndValue() throws Exception {
    // Use reflection to set private field deferredName
    Field deferredNameField = JsonWriter.class.getDeclaredField("deferredName");
    deferredNameField.setAccessible(true);
    deferredNameField.set(jsonWriter, "myName");

    // Also push EMPTY_OBJECT to stack and set stackSize to 1 to simulate inside object
    Field stackField = JsonWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    int[] stack = (int[]) stackField.get(jsonWriter);
    stack[0] = 1; // EMPTY_OBJECT == 1

    Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonWriter, 1);

    // Call value(true)
    JsonWriter returned = jsonWriter.value(true);
    assertSame(jsonWriter, returned);

    String result = stringWriter.toString();
    // It should write the name and value, separated by colon (":") by default
    assertTrue(result.startsWith("\"myName\""));
    assertTrue(result.contains(":true"));
    // deferredName should be cleared
    assertNull(deferredNameField.get(jsonWriter));
  }

  @Test
    @Timeout(8000)
  void value_invokesBeforeValue() throws Exception {
    // Use reflection to invoke private beforeValue and verify it does not throw
    Method beforeValueMethod = JsonWriter.class.getDeclaredMethod("beforeValue");
    beforeValueMethod.setAccessible(true);
    beforeValueMethod.invoke(jsonWriter);

    // Now call value and verify output is "true"
    jsonWriter.value(true);
    assertEquals("true", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void value_multipleCalls_writesValuesInSequence() throws Exception {
    try {
      // Reset stack and stackSize to allow multiple values
      Field stackField = JsonWriter.class.getDeclaredField("stack");
      stackField.setAccessible(true);
      int[] stack = (int[]) stackField.get(jsonWriter);
      stack[0] = 2; // NONEMPTY_DOCUMENT == 2

      Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
      stackSizeField.setAccessible(true);
      stackSizeField.setInt(jsonWriter, 1);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }

    jsonWriter.value(true);
    jsonWriter.value(false);
    assertEquals("truefalse", stringWriter.toString());
  }
}