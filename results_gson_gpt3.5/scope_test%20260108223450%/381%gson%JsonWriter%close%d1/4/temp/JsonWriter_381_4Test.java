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
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonWriterCloseTest {

  private JsonWriter writer;
  private StringWriter stringWriter;

  @BeforeEach
  void setUp() {
    stringWriter = new StringWriter();
    writer = new JsonWriter(stringWriter);
  }

  @Test
    @Timeout(8000)
  void close_validEmptyContext_writesCloseBracket() throws Exception {
    // Arrange
    setStackAndSize(JsonScope.EMPTY_OBJECT);
    setDeferredName(null);

    Method closeMethod = JsonWriter.class.getDeclaredMethod("close", int.class, int.class, char.class);
    closeMethod.setAccessible(true);

    // Act
    JsonWriter result = (JsonWriter) closeMethod.invoke(writer, JsonScope.EMPTY_OBJECT, JsonScope.NONEMPTY_OBJECT, '}');

    // Assert
    assertSame(writer, result);
    assertEquals("}", stringWriter.toString());
    assertEquals(0, getStackSize());
  }

  @Test
    @Timeout(8000)
  void close_validNonEmptyContext_writesNewlineAndCloseBracket() throws Exception {
    // Arrange
    setStackAndSize(JsonScope.NONEMPTY_ARRAY);
    setDeferredName(null);
    // Instead of setIndent(String), set the private field 'indent' via reflection
    setIndentField("  "); // so newline() writes something

    Method closeMethod = JsonWriter.class.getDeclaredMethod("close", int.class, int.class, char.class);
    closeMethod.setAccessible(true);

    // Act
    JsonWriter result = (JsonWriter) closeMethod.invoke(writer, JsonScope.EMPTY_ARRAY, JsonScope.NONEMPTY_ARRAY, ']');

    // Assert
    assertSame(writer, result);
    String output = stringWriter.toString();
    assertTrue(output.startsWith("\n"));
    assertTrue(output.endsWith("]"));
    assertEquals(0, getStackSize());
  }

  @Test
    @Timeout(8000)
  void close_invalidContext_throwsIllegalStateException() throws Exception {
    // Arrange
    setStackAndSize(JsonScope.EMPTY_DOCUMENT);
    setDeferredName(null);

    Method closeMethod = JsonWriter.class.getDeclaredMethod("close", int.class, int.class, char.class);
    closeMethod.setAccessible(true);

    // Act & Assert
    IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
      closeMethod.invoke(writer, JsonScope.EMPTY_ARRAY, JsonScope.NONEMPTY_ARRAY, ']');
    });
    assertTrue(ex.getCause() instanceof IllegalStateException);
    assertEquals("Nesting problem.", ex.getCause().getMessage());
  }

  @Test
    @Timeout(8000)
  void close_withDeferredName_throwsIllegalStateException() throws Exception {
    // Arrange
    setStackAndSize(JsonScope.EMPTY_OBJECT);
    setDeferredName("danglingName");

    Method closeMethod = JsonWriter.class.getDeclaredMethod("close", int.class, int.class, char.class);
    closeMethod.setAccessible(true);

    // Act & Assert
    IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
      closeMethod.invoke(writer, JsonScope.EMPTY_OBJECT, JsonScope.NONEMPTY_OBJECT, '}');
    });
    assertTrue(ex.getCause() instanceof IllegalStateException);
    assertEquals("Dangling name: danglingName", ex.getCause().getMessage());
  }

  // Helper to set stack and stackSize fields via reflection
  private void setStackAndSize(int top) throws Exception {
    Field stackField = JsonWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    int[] stack = new int[32];
    stack[0] = top;
    stackField.set(writer, stack);

    Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(writer, 1);
  }

  private int getStackSize() throws Exception {
    Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    return stackSizeField.getInt(writer);
  }

  private void setDeferredName(String name) throws Exception {
    Field deferredNameField = JsonWriter.class.getDeclaredField("deferredName");
    deferredNameField.setAccessible(true);
    deferredNameField.set(writer, name);
  }

  private void setIndentField(String indent) throws Exception {
    Field indentField = JsonWriter.class.getDeclaredField("indent");
    indentField.setAccessible(true);
    indentField.set(writer, indent);
  }
}