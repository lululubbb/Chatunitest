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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonWriterBeforeValueTest {

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

  private void setStackTop(int value) throws Exception {
    // set stackSize to 1 and stack[0] to value
    var stackField = JsonWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    var stack = (int[]) stackField.get(writer);
    stack[0] = value;
    var stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(writer, 1);
  }

  private void setLenient(boolean lenient) throws Exception {
    var lenientField = JsonWriter.class.getDeclaredField("lenient");
    lenientField.setAccessible(true);
    lenientField.setBoolean(writer, lenient);
  }

  private String getOutContent() {
    return stringWriter.toString();
  }

  private String getSeparator() throws Exception {
    var sepField = JsonWriter.class.getDeclaredField("separator");
    sepField.setAccessible(true);
    return (String) sepField.get(writer);
  }

  private void setSeparator(String sep) throws Exception {
    var sepField = JsonWriter.class.getDeclaredField("separator");
    sepField.setAccessible(true);
    sepField.set(writer, sep);
  }

  private int peek() throws Exception {
    Method peekMethod = JsonWriter.class.getDeclaredMethod("peek");
    peekMethod.setAccessible(true);
    return (int) peekMethod.invoke(writer);
  }

  private int getStackTop() throws Exception {
    var stackField = JsonWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    var stack = (int[]) stackField.get(writer);
    return stack[0];
  }

  @Test
    @Timeout(8000)
  void testBeforeValue_nonemptyDocumentLenientFalse_throws() throws Throwable {
    setStackTop(JsonScope.NONEMPTY_DOCUMENT);
    setLenient(false);
    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
      beforeValueMethod.invoke(writer);
    });
    assertEquals("JSON must have only one top-level value.", thrown.getCause().getMessage());
  }

  @Test
    @Timeout(8000)
  void testBeforeValue_nonemptyDocumentLenientTrue_replacesTop() throws Throwable {
    setStackTop(JsonScope.NONEMPTY_DOCUMENT);
    setLenient(true);

    beforeValueMethod.invoke(writer);

    int top = getStackTop();
    assertEquals(JsonScope.NONEMPTY_DOCUMENT, top);
  }

  @Test
    @Timeout(8000)
  void testBeforeValue_emptyDocument_replacesTop() throws Throwable {
    setStackTop(JsonScope.EMPTY_DOCUMENT);

    beforeValueMethod.invoke(writer);

    int top = getStackTop();
    assertEquals(JsonScope.NONEMPTY_DOCUMENT, top);
  }

  @Test
    @Timeout(8000)
  void testBeforeValue_emptyArray_replacesTopAndNewline() throws Throwable {
    setStackTop(JsonScope.EMPTY_ARRAY);

    beforeValueMethod.invoke(writer);

    int top = getStackTop();
    assertEquals(JsonScope.NONEMPTY_ARRAY, top);

    // newline() writes to out, but default indent is null so no newline char added
    assertEquals("", getOutContent());
  }

  @Test
    @Timeout(8000)
  void testBeforeValue_nonemptyArray_appendsCommaAndNewline() throws Throwable {
    setStackTop(JsonScope.NONEMPTY_ARRAY);

    beforeValueMethod.invoke(writer);

    int top = getStackTop();
    assertEquals(JsonScope.NONEMPTY_ARRAY, top);

    // Should append comma
    assertEquals(",", getOutContent());
  }

  @Test
    @Timeout(8000)
  void testBeforeValue_danglingName_appendsSeparatorAndReplacesTop() throws Throwable {
    setStackTop(JsonScope.DANGLING_NAME);
    setSeparator(": ");

    beforeValueMethod.invoke(writer);

    int top = getStackTop();
    assertEquals(JsonScope.NONEMPTY_OBJECT, top);

    assertEquals(": ", getOutContent());
  }

  @Test
    @Timeout(8000)
  void testBeforeValue_default_throws() throws Throwable {
    setStackTop(9999);

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      beforeValueMethod.invoke(writer);
    });
    Throwable cause = thrown.getCause();
    assertTrue(cause instanceof IllegalStateException);
    assertEquals("Nesting problem.", cause.getMessage());
  }
}