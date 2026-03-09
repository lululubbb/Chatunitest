package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import static com.google.gson.stream.JsonScope.EMPTY_ARRAY;
import static com.google.gson.stream.JsonScope.EMPTY_DOCUMENT;
import static com.google.gson.stream.JsonScope.NONEMPTY_ARRAY;
import static com.google.gson.stream.JsonScope.NONEMPTY_DOCUMENT;
import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
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

import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JsonWriterBeforeNameTest {

  private JsonWriter jsonWriter;
  private Writer mockWriter;

  @BeforeEach
  void setUp() {
    mockWriter = mock(Writer.class);
    jsonWriter = new JsonWriter(mockWriter);
  }

  private void setStackAndSize(int[] stack, int stackSize) throws Exception {
    var stackField = JsonWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    stackField.set(jsonWriter, stack);

    var stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonWriter, stackSize);
  }

  private int peek() throws Exception {
    Method peekMethod = JsonWriter.class.getDeclaredMethod("peek");
    peekMethod.setAccessible(true);
    return (int) peekMethod.invoke(jsonWriter);
  }

  private void replaceTop(int value) throws Exception {
    Method replaceTopMethod = JsonWriter.class.getDeclaredMethod("replaceTop", int.class);
    replaceTopMethod.setAccessible(true);
    replaceTopMethod.invoke(jsonWriter, value);
  }

  private void newline() throws Exception {
    Method newlineMethod = JsonWriter.class.getDeclaredMethod("newline");
    newlineMethod.setAccessible(true);
    newlineMethod.invoke(jsonWriter);
  }

  private void beforeName() throws Throwable {
    Method beforeNameMethod = JsonWriter.class.getDeclaredMethod("beforeName");
    beforeNameMethod.setAccessible(true);
    try {
      beforeNameMethod.invoke(jsonWriter);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }

  @Test
    @Timeout(8000)
  void beforeName_whenContextIsNonemptyObject_writesCommaNewlineAndReplacesTop() throws Throwable {
    // Arrange stack so peek() returns NONEMPTY_OBJECT
    int[] stack = new int[32];
    stack[0] = NONEMPTY_OBJECT;
    setStackAndSize(stack, 1);

    // Act
    beforeName();

    // Assert
    verify(mockWriter, times(1)).write(',');
    var stackField = JsonWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    int[] actualStack = (int[]) stackField.get(jsonWriter);
    assertEquals(DANGLING_NAME, actualStack[0]);
  }

  @Test
    @Timeout(8000)
  void beforeName_whenContextIsEmptyObject_doesNotWriteCommaButWritesNewlineAndReplacesTop() throws Throwable {
    // Arrange stack so peek() returns EMPTY_OBJECT
    int[] stack = new int[32];
    stack[0] = EMPTY_OBJECT;
    setStackAndSize(stack, 1);

    // Act
    beforeName();

    // Assert
    verify(mockWriter, never()).write(',');
    var stackField = JsonWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    int[] actualStack = (int[]) stackField.get(jsonWriter);
    assertEquals(DANGLING_NAME, actualStack[0]);
  }

  @Test
    @Timeout(8000)
  void beforeName_whenContextIsNotObject_throwsIllegalStateException() throws Throwable {
    // Arrange stack so peek() returns some value other than NONEMPTY_OBJECT or EMPTY_OBJECT
    int[] stack = new int[32];
    stack[0] = 12345; // arbitrary invalid context
    setStackAndSize(stack, 1);

    // Act & Assert
    IllegalStateException thrown = assertThrows(IllegalStateException.class, this::beforeName);
    assertEquals("Nesting problem.", thrown.getMessage());
    verify(mockWriter, never()).write(anyInt());
  }
}