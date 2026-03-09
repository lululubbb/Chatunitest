package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import static com.google.gson.stream.JsonScope.EMPTY_ARRAY;
import static com.google.gson.stream.JsonScope.EMPTY_DOCUMENT;
import static com.google.gson.stream.JsonScope.NONEMPTY_ARRAY;
import static com.google.gson.stream.JsonScope.NONEMPTY_DOCUMENT;
import java.io.Closeable;
import java.io.Flushable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

import static com.google.gson.stream.JsonScope.DANGLING_NAME;
import static com.google.gson.stream.JsonScope.EMPTY_OBJECT;
import static com.google.gson.stream.JsonScope.NONEMPTY_OBJECT;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JsonWriterBeforeNameTest {

  private JsonWriter jsonWriter;
  private Writer mockWriter;

  @BeforeEach
  void setUp() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    mockWriter = mock(Writer.class);
    jsonWriter = new JsonWriter(mockWriter);
    // Initialize stackSize to 1 and stack[0] to EMPTY_OBJECT to avoid IllegalStateException on construction
    setStackTop(EMPTY_OBJECT);

    // Stub newline() to avoid IOException during tests using reflection to replace method call
    Method newline = JsonWriter.class.getDeclaredMethod("newline");
    newline.setAccessible(true);

    // Instead of mocking newline() directly (which is private), replace it by reflection with a no-op
    // Create a proxy/wrapper JsonWriter that overrides newline() is complicated,
    // so we use a spy and stub the method via reflection invocation handler

    // Use a spy and stub the newline method invocation via reflection
    JsonWriter spyWriter = spy(jsonWriter);

    // Use doAnswer to intercept calls to newline() via reflection
    doAnswer(invocation -> {
      // do nothing
      return null;
    }).when(spyWriter).getClass().getDeclaredMethod("newline").invoke(spyWriter);

    // But above won't compile or work, so instead we use reflection to replace the method call inside beforeName

    // So, to fix the compilation error, we remove the doNothing().when(spyWriter).newline();
    // and instead override beforeName() to call a no-op newline

    // Replace jsonWriter reference with spy
    jsonWriter = spyWriter;
  }

  @Test
    @Timeout(8000)
  void beforeName_writesCommaWhenNonEmptyObject() throws Throwable {
    // Arrange: set stack so peek() returns NONEMPTY_OBJECT
    setStackTop(NONEMPTY_OBJECT);

    // Act
    invokeBeforeName();

    // Assert: verify comma written, newline called, and top replaced with DANGLING_NAME
    verify(mockWriter).write(',');
    assertStackTop(DANGLING_NAME);
  }

  @Test
    @Timeout(8000)
  void beforeName_noCommaWhenEmptyObject() throws Throwable {
    // Arrange: set stack so peek() returns EMPTY_OBJECT
    setStackTop(EMPTY_OBJECT);

    // Act
    invokeBeforeName();

    // Assert: verify no comma written, newline called, and top replaced with DANGLING_NAME
    verify(mockWriter, never()).write(',');
    assertStackTop(DANGLING_NAME);
  }

  @Test
    @Timeout(8000)
  void beforeName_throwsIllegalStateExceptionWhenNotObjectContext() throws Throwable {
    // Arrange: set stack so peek() returns something else (e.g. EMPTY_ARRAY)
    setStackTop(JsonScope.EMPTY_ARRAY);

    // Act & Assert
    IllegalStateException ex = assertThrows(IllegalStateException.class, this::invokeBeforeName);
    assertEquals("Nesting problem.", ex.getMessage());
  }

  // Helper to set the top of the stack (stackSize=1)
  private void setStackTop(int context) {
    try {
      Field stackField = JsonWriter.class.getDeclaredField("stack");
      stackField.setAccessible(true);
      int[] stack = new int[32];
      stack[0] = context;
      stackField.set(jsonWriter, stack);

      Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
      stackSizeField.setAccessible(true);
      stackSizeField.setInt(jsonWriter, 1);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  // Helper to assert the top of the stack value
  private void assertStackTop(int expected) {
    try {
      Field stackField = JsonWriter.class.getDeclaredField("stack");
      stackField.setAccessible(true);
      int[] stack = (int[]) stackField.get(jsonWriter);

      Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
      stackSizeField.setAccessible(true);
      int stackSize = stackSizeField.getInt(jsonWriter);

      assertTrue(stackSize > 0);
      assertEquals(expected, stack[stackSize - 1]);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  // Helper to invoke private beforeName method via reflection
  private void invokeBeforeName() throws Throwable {
    try {
      // Because newline() is private and called inside beforeName(), and cannot be stubbed,
      // we replace newline() with a no-op by temporarily replacing it via reflection.

      // Backup original newline method
      Method newline = JsonWriter.class.getDeclaredMethod("newline");
      newline.setAccessible(true);

      // Use reflection to temporarily replace newline() method body is impossible in Java,
      // so instead, we create a subclass of JsonWriter with overridden newline().

      // So, create a subclass with overridden newline()
      JsonWriter writerWithNoopNewline = new JsonWriterWithNoopNewline(mockWriter);
      // Copy stack and stackSize fields from jsonWriter to writerWithNoopNewline
      copyStackAndSize(jsonWriter, writerWithNoopNewline);
      // Replace jsonWriter reference with writerWithNoopNewline for this invocation
      Method beforeName = JsonWriter.class.getDeclaredMethod("beforeName");
      beforeName.setAccessible(true);
      beforeName.invoke(writerWithNoopNewline);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }

  // Helper to copy stack and stackSize fields between JsonWriter instances
  private void copyStackAndSize(JsonWriter from, JsonWriter to) {
    try {
      Field stackField = JsonWriter.class.getDeclaredField("stack");
      stackField.setAccessible(true);
      int[] stack = (int[]) stackField.get(from);
      stackField.set(to, stack);

      Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
      stackSizeField.setAccessible(true);
      int stackSize = stackSizeField.getInt(from);
      stackSizeField.setInt(to, stackSize);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  // Subclass of JsonWriter overriding newline() to no-op
  private static class JsonWriterWithNoopNewline extends JsonWriter {
    JsonWriterWithNoopNewline(Writer out) {
      super(out);
    }

    @Override
    protected void newline() {
      // no-op
    }
  }
}