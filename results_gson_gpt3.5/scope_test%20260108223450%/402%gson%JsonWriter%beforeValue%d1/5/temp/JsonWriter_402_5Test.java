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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

class JsonWriter_beforeValue_Test {
  private JsonWriter writer;
  private StringWriter stringWriter;

  @BeforeEach
  public void setUp() throws Exception {
    stringWriter = new StringWriter();
    writer = new JsonWriter(stringWriter);

    // Initialize stackSize to 1 to avoid IndexOutOfBounds in beforeValue()
    Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(writer, 1);
  }

  private void setStack(int[] stack, int stackSize) throws Exception {
    Field stackField = JsonWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    stackField.set(writer, stack);

    Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(writer, stackSize);
  }

  private void setLenient(boolean lenient) throws Exception {
    Field lenientField = JsonWriter.class.getDeclaredField("lenient");
    lenientField.setAccessible(true);
    lenientField.setBoolean(writer, lenient);
  }

  private void setSeparator(String separator) throws Exception {
    Field separatorField = JsonWriter.class.getDeclaredField("separator");
    separatorField.setAccessible(true);
    separatorField.set(writer, separator);
  }

  private int peek() throws Exception {
    Method peekMethod = JsonWriter.class.getDeclaredMethod("peek");
    peekMethod.setAccessible(true);
    return (int) peekMethod.invoke(writer);
  }

  private void replaceTop(int value) throws Exception {
    Method replaceTopMethod = JsonWriter.class.getDeclaredMethod("replaceTop", int.class);
    replaceTopMethod.setAccessible(true);
    replaceTopMethod.invoke(writer, value);
  }

  private void newline() throws Exception {
    Method newlineMethod = JsonWriter.class.getDeclaredMethod("newline");
    newlineMethod.setAccessible(true);
    newlineMethod.invoke(writer);
  }

  private void beforeValue() throws Exception {
    Method beforeValueMethod = JsonWriter.class.getDeclaredMethod("beforeValue");
    beforeValueMethod.setAccessible(true);
    beforeValueMethod.invoke(writer);
  }

  @Test
    @Timeout(8000)
  public void beforeValue_nonemptyDocumentLenientFalse_throws() throws Exception {
    setStack(new int[] {JsonScope.NONEMPTY_DOCUMENT}, 1);
    setLenient(false);

    IllegalStateException ex = assertThrows(IllegalStateException.class, this::beforeValue);
    assertEquals("JSON must have only one top-level value.", ex.getMessage());
  }

  @Test
    @Timeout(8000)
  public void beforeValue_nonemptyDocumentLenientTrue_replacesTop() throws Exception {
    setStack(new int[] {JsonScope.NONEMPTY_DOCUMENT}, 1);
    setLenient(true);

    beforeValue();

    Field stackField = JsonWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    int[] stack = (int[]) stackField.get(writer);
    assertEquals(JsonScope.NONEMPTY_DOCUMENT, stack[0]);
  }

  @Test
    @Timeout(8000)
  public void beforeValue_emptyDocument_replacesTop() throws Exception {
    setStack(new int[] {JsonScope.EMPTY_DOCUMENT}, 1);

    beforeValue();

    Field stackField = JsonWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    int[] stack = (int[]) stackField.get(writer);
    assertEquals(JsonScope.NONEMPTY_DOCUMENT, stack[0]);
  }

  @Test
    @Timeout(8000)
  public void beforeValue_emptyArray_newlineCalled() throws Exception {
    setStack(new int[] {JsonScope.EMPTY_ARRAY}, 1);

    beforeValue();

    Field stackField = JsonWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    int[] stack = (int[]) stackField.get(writer);
    assertEquals(JsonScope.NONEMPTY_ARRAY, stack[0]);
    // newline appends a newline to out (StringWriter)
    String outContents = stringWriter.toString();
    // Accept either empty string or string containing newline, newline() may be no-op if indent is null
    assertTrue(outContents.isEmpty() || outContents.contains("\n"));
  }

  @Test
    @Timeout(8000)
  public void beforeValue_nonemptyArray_appendsCommaAndNewline() throws Exception {
    setStack(new int[] {JsonScope.NONEMPTY_ARRAY}, 1);

    beforeValue();

    String outContents = stringWriter.toString();
    assertTrue(outContents.startsWith(","));
    assertTrue(outContents.contains("\n") || outContents.length() == 1);
  }

  @Test
    @Timeout(8000)
  public void beforeValue_danglingName_appendsSeparatorAndReplacesTop() throws Exception {
    setStack(new int[] {JsonScope.DANGLING_NAME}, 1);
    setSeparator(":");

    beforeValue();

    String outContents = stringWriter.toString();
    assertTrue(outContents.startsWith(":"));

    Field stackField = JsonWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    int[] stack = (int[]) stackField.get(writer);
    assertEquals(JsonScope.NONEMPTY_OBJECT, stack[0]);
  }

  @Test
    @Timeout(8000)
  public void beforeValue_default_throwsIllegalStateException() throws Exception {
    setStack(new int[] {999}, 1);

    IllegalStateException ex = assertThrows(IllegalStateException.class, this::beforeValue);
    assertEquals("Nesting problem.", ex.getMessage());
  }
}