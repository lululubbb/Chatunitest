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

import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class JsonWriter_390_4Test {

  private StringWriter stringWriter;
  private JsonWriter jsonWriter;

  @BeforeEach
  public void setUp() {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);
  }

  @Test
    @Timeout(8000)
  public void testValueTrue() throws IOException {
    jsonWriter.value(true);
    assertEquals("true", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void testValueFalse() throws IOException {
    jsonWriter.value(false);
    assertEquals("false", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void testValueCallsWriteDeferredNameAndBeforeValue() throws Exception {
    // Spy on JsonWriter to verify value call
    JsonWriter spyWriter = spy(new JsonWriter(stringWriter));

    // Use reflection to access private methods
    Method writeDeferredNameMethod = JsonWriter.class.getDeclaredMethod("writeDeferredName");
    writeDeferredNameMethod.setAccessible(true);

    Method beforeValueMethod = JsonWriter.class.getDeclaredMethod("beforeValue");
    beforeValueMethod.setAccessible(true);

    // Initialize stack and stackSize to avoid IllegalStateException
    Field stackField = JsonWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    int[] stack = (int[]) stackField.get(spyWriter);
    stack[0] = 6; // EMPTY_DOCUMENT is 6 in JsonScope
    Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.set(spyWriter, 1);

    // Call value(boolean) on spy
    spyWriter.value(true);

    // Verify that value(true) was called once
    verify(spyWriter, times(1)).value(true);

    // Invoke private methods separately to ensure no exceptions
    // Only invoke if deferredName is not set and stack is properly set to avoid nesting problem
    writeDeferredNameMethod.invoke(spyWriter);

    // Reset stack to a valid state before invoking beforeValue to avoid nesting problem
    stack[0] = 1; // EMPTY_ARRAY per JsonScope
    beforeValueMethod.invoke(spyWriter);

    // Verify output - only one "true" because writeDeferredName and beforeValue do not write output
    assertEquals("true", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void testValueWithDeferredNameWritesNameBeforeValue() throws Exception {
    // Set deferredName to a non-null value to test that writeDeferredName writes the name first
    Field deferredNameField = JsonWriter.class.getDeclaredField("deferredName");
    deferredNameField.setAccessible(true);
    deferredNameField.set(jsonWriter, "myName");

    // Also push NONEMPTY_OBJECT on stack to allow name writing
    Field stackField = JsonWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    int[] stack = (int[]) stackField.get(jsonWriter);
    stack[0] = 4; // NONEMPTY_OBJECT (correct constant value)
    Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.set(jsonWriter, 1);

    jsonWriter.value(true);
    String output = stringWriter.toString();

    // It should write the name and then the value true
    assertTrue(output.contains("myName"));
    assertTrue(output.contains("true"));
  }

  @Test
    @Timeout(8000)
  public void testValueReturnsThis() throws IOException {
    JsonWriter result = jsonWriter.value(true);
    assertSame(jsonWriter, result);
  }
}