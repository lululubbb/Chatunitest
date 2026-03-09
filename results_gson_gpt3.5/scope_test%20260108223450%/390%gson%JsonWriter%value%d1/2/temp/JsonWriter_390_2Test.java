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
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonWriter_value_Test {

  private StringWriter stringWriter;
  private JsonWriter jsonWriter;

  @BeforeEach
  void setUp() {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);
  }

  @Test
    @Timeout(8000)
  void value_true_writesTrue() throws IOException {
    jsonWriter.value(true);
    assertEquals("true", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void value_false_writesFalse() throws IOException {
    jsonWriter.value(false);
    assertEquals("false", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void value_deferredName_writtenBeforeValue() throws Exception {
    // Use reflection to set deferredName to a non-null value to trigger writeDeferredName()
    var deferredNameField = JsonWriter.class.getDeclaredField("deferredName");
    deferredNameField.setAccessible(true);
    deferredNameField.set(jsonWriter, "name");

    // Also need to set stack and stackSize so beforeValue() does not throw
    var stackField = JsonWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    int[] stack = new int[32];
    stack[0] = JsonScope.EMPTY_OBJECT; // fake state to allow beforeValue to work
    stackField.set(jsonWriter, stack);
    var stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonWriter, 1);

    // We call value(true) which calls writeDeferredName internally
    jsonWriter.value(true);

    // The output should contain the deferred name and the value true
    String output = stringWriter.toString();
    assertTrue(output.contains("name"));
    assertTrue(output.contains("true"));
  }

  @Test
    @Timeout(8000)
  void value_beforeValue_called() throws Exception {
    // Spy on JsonWriter to verify beforeValue() is called
    JsonWriter spyWriter = spy(new JsonWriter(stringWriter));

    // Setup stack and stackSize for beforeValue
    var stackField = JsonWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    int[] stack = new int[32];
    stack[0] = JsonScope.EMPTY_DOCUMENT;
    stackField.set(spyWriter, stack);
    var stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(spyWriter, 1);

    // Use reflection to invoke private beforeValue() to verify it is called
    Method beforeValueMethod = JsonWriter.class.getDeclaredMethod("beforeValue");
    beforeValueMethod.setAccessible(true);

    spyWriter.value(false);

    // Verify beforeValue() was called once via reflection invocation count
    // Since verify(spyWriter).beforeValue() cannot be used (private method),
    // we instead check indirectly by spying on the value method and intercepting beforeValue via reflection.
    // Alternatively, use reflection to invoke beforeValue and check no exception thrown.
    // Here, we verify that value(false) produces expected output,
    // implying beforeValue() was called internally without error.
    assertEquals("false", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void value_returnsThis() throws IOException {
    JsonWriter returned = jsonWriter.value(true);
    assertSame(jsonWriter, returned);
  }
}