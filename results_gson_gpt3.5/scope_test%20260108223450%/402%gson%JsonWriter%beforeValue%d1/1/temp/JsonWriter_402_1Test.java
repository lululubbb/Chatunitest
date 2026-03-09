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
import com.google.gson.stream.JsonScope;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class JsonWriterBeforeValueTest {

  private JsonWriter writer;
  private Writer out;

  @BeforeEach
  public void setUp() {
    out = mock(Writer.class);
    writer = new JsonWriter(out);
  }

  private void setStackAndStackSize(int[] stackValues, int stackSize) throws Exception {
    var stackField = JsonWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    stackField.set(writer, stackValues);

    var stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(writer, stackSize);
  }

  private int invokePeek() throws Exception {
    Method peek = JsonWriter.class.getDeclaredMethod("peek");
    peek.setAccessible(true);
    return (int) peek.invoke(writer);
  }

  private void invokeReplaceTop(int topOfStack) throws Exception {
    Method replaceTop = JsonWriter.class.getDeclaredMethod("replaceTop", int.class);
    replaceTop.setAccessible(true);
    replaceTop.invoke(writer, topOfStack);
  }

  private void invokeNewline() throws Exception {
    Method newline = JsonWriter.class.getDeclaredMethod("newline");
    newline.setAccessible(true);
    newline.invoke(writer);
  }

  private void invokeBeforeValue() throws Throwable {
    Method beforeValue = JsonWriter.class.getDeclaredMethod("beforeValue");
    beforeValue.setAccessible(true);
    try {
      beforeValue.invoke(writer);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }

  @Test
    @Timeout(8000)
  public void testBeforeValue_nonEmptyDocumentLenientFalse_throws() throws Throwable {
    setStackAndStackSize(new int[] {JsonScope.NONEMPTY_DOCUMENT}, 1);
    writer.setLenient(false);

    IllegalStateException thrown = assertThrows(IllegalStateException.class, this::invokeBeforeValue);
    assertEquals("JSON must have only one top-level value.", thrown.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testBeforeValue_nonEmptyDocumentLenientTrue_replacesTop() throws Throwable {
    setStackAndStackSize(new int[] {JsonScope.NONEMPTY_DOCUMENT}, 1);
    writer.setLenient(true);

    invokeBeforeValue();

    int[] stack = (int[]) JsonWriter.class.getDeclaredField("stack").get(writer);
    assertEquals(JsonScope.NONEMPTY_DOCUMENT, stack[0]);
    verify(out, never()).append(anyChar());
  }

  @Test
    @Timeout(8000)
  public void testBeforeValue_emptyDocument_replacesTop() throws Throwable {
    setStackAndStackSize(new int[] {JsonScope.EMPTY_DOCUMENT}, 1);

    invokeBeforeValue();

    int[] stack = (int[]) JsonWriter.class.getDeclaredField("stack").get(writer);
    assertEquals(JsonScope.NONEMPTY_DOCUMENT, stack[0]);
    verify(out, never()).append(anyChar());
  }

  @Test
    @Timeout(8000)
  public void testBeforeValue_emptyArray_callsNewlineAndReplacesTop() throws Throwable {
    setStackAndStackSize(new int[] {JsonScope.EMPTY_ARRAY}, 1);

    invokeBeforeValue();

    int[] stack = (int[]) JsonWriter.class.getDeclaredField("stack").get(writer);
    assertEquals(JsonScope.NONEMPTY_ARRAY, stack[0]);
    verify(out, never()).append(',');
  }

  @Test
    @Timeout(8000)
  public void testBeforeValue_nonEmptyArray_appendsCommaAndCallsNewline() throws Throwable {
    setStackAndStackSize(new int[] {JsonScope.NONEMPTY_ARRAY}, 1);

    invokeBeforeValue();

    verify(out).append(',');
  }

  @Test
    @Timeout(8000)
  public void testBeforeValue_danglingName_appendsSeparatorAndReplacesTop() throws Throwable {
    setStackAndStackSize(new int[] {JsonScope.DANGLING_NAME}, 1);

    // Set separator field to ":" (default)
    var sepField = JsonWriter.class.getDeclaredField("separator");
    sepField.setAccessible(true);
    sepField.set(writer, ":");

    invokeBeforeValue();

    verify(out).append(":");

    int[] stack = (int[]) JsonWriter.class.getDeclaredField("stack").get(writer);
    assertEquals(JsonScope.NONEMPTY_OBJECT, stack[0]);
  }

  @Test
    @Timeout(8000)
  public void testBeforeValue_invalidState_throws() throws Throwable {
    setStackAndStackSize(new int[] {9999}, 1);

    IllegalStateException thrown = assertThrows(IllegalStateException.class, this::invokeBeforeValue);
    assertEquals("Nesting problem.", thrown.getMessage());
  }
}