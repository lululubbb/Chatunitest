package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
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

import static com.google.gson.stream.JsonScope.DANGLING_NAME;
import static com.google.gson.stream.JsonScope.EMPTY_ARRAY;
import static com.google.gson.stream.JsonScope.EMPTY_DOCUMENT;
import static com.google.gson.stream.JsonScope.EMPTY_OBJECT;
import static com.google.gson.stream.JsonScope.NONEMPTY_ARRAY;
import static com.google.gson.stream.JsonScope.NONEMPTY_DOCUMENT;
import static com.google.gson.stream.JsonScope.NONEMPTY_OBJECT;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JsonWriter_beforeValue_Test {

  private JsonWriter writer;
  private StringWriter stringWriter;
  private Method beforeValueMethod;

  @BeforeEach
  void setUp() throws NoSuchMethodException {
    stringWriter = new StringWriter();
    writer = new JsonWriter(stringWriter);

    beforeValueMethod = JsonWriter.class.getDeclaredMethod("beforeValue");
    beforeValueMethod.setAccessible(true);
  }

  private void setStackAndStackSize(int[] stack, int stackSize) throws Exception {
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

  private String getOutContent() {
    return stringWriter.toString();
  }

  private void setSeparator(String separator) throws Exception {
    Field separatorField = JsonWriter.class.getDeclaredField("separator");
    separatorField.setAccessible(true);
    separatorField.set(writer, separator);
  }

  @Test
    @Timeout(8000)
  void testBeforeValue_nonemptyDocumentLenientFalse_throwsIllegalStateException() throws Throwable {
    setStackAndStackSize(new int[]{NONEMPTY_DOCUMENT}, 1);
    setLenient(false);

    InvocationTargetException e = assertThrows(InvocationTargetException.class, () -> beforeValueMethod.invoke(writer));
    assertTrue(e.getCause() instanceof IllegalStateException);
    assertEquals("JSON must have only one top-level value.", e.getCause().getMessage());
  }

  @Test
    @Timeout(8000)
  void testBeforeValue_nonemptyDocumentLenientTrue_replacesTop() throws Throwable {
    setStackAndStackSize(new int[]{NONEMPTY_DOCUMENT}, 1);
    setLenient(true);

    beforeValueMethod.invoke(writer);

    int top = peek();
    assertEquals(NONEMPTY_DOCUMENT, top);
    assertEquals("", getOutContent());
  }

  @Test
    @Timeout(8000)
  void testBeforeValue_emptyDocument_replacesTop() throws Throwable {
    setStackAndStackSize(new int[]{EMPTY_DOCUMENT}, 1);

    beforeValueMethod.invoke(writer);

    int top = peek();
    assertEquals(NONEMPTY_DOCUMENT, top);
    assertEquals("", getOutContent());
  }

  @Test
    @Timeout(8000)
  void testBeforeValue_emptyArray_replacesTopAndNewline() throws Throwable {
    setStackAndStackSize(new int[]{EMPTY_ARRAY}, 1);

    beforeValueMethod.invoke(writer);

    int top = peek();
    assertEquals(NONEMPTY_ARRAY, top);
    String out = getOutContent();
    assertTrue(out.endsWith("\n") || out.endsWith(System.lineSeparator()));
  }

  @Test
    @Timeout(8000)
  void testBeforeValue_nonemptyArray_appendsCommaAndNewline() throws Throwable {
    setStackAndStackSize(new int[]{NONEMPTY_ARRAY}, 1);

    beforeValueMethod.invoke(writer);

    int top = peek();
    assertEquals(NONEMPTY_ARRAY, top);
    String out = getOutContent();
    assertTrue(out.startsWith(","));
    assertTrue(out.endsWith("\n") || out.endsWith(System.lineSeparator()));
  }

  @Test
    @Timeout(8000)
  void testBeforeValue_danglingName_appendsSeparatorAndReplacesTop() throws Throwable {
    setStackAndStackSize(new int[]{DANGLING_NAME}, 1);

    setSeparator(":");

    beforeValueMethod.invoke(writer);

    int top = peek();
    assertEquals(NONEMPTY_OBJECT, top);

    String out = getOutContent();
    assertEquals(":", out);
  }

  @Test
    @Timeout(8000)
  void testBeforeValue_defaultCase_throwsIllegalStateException() throws Throwable {
    setStackAndStackSize(new int[]{EMPTY_OBJECT}, 1);

    InvocationTargetException e = assertThrows(InvocationTargetException.class, () -> beforeValueMethod.invoke(writer));
    assertTrue(e.getCause() instanceof IllegalStateException);
    assertEquals("Nesting problem.", e.getCause().getMessage());
  }
}