package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
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
import static com.google.gson.stream.JsonScope.EMPTY_ARRAY;
import static com.google.gson.stream.JsonScope.EMPTY_DOCUMENT;
import static com.google.gson.stream.JsonScope.EMPTY_OBJECT;
import static com.google.gson.stream.JsonScope.NONEMPTY_ARRAY;
import static com.google.gson.stream.JsonScope.NONEMPTY_DOCUMENT;
import static com.google.gson.stream.JsonScope.NONEMPTY_OBJECT;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JsonWriterBeforeValueTest {

  private JsonWriter jsonWriter;
  private Writer mockWriter;
  private Method beforeValueMethod;

  @BeforeEach
  void setUp() throws NoSuchMethodException {
    mockWriter = mock(Writer.class);
    jsonWriter = new JsonWriter(mockWriter);

    beforeValueMethod = JsonWriter.class.getDeclaredMethod("beforeValue");
    beforeValueMethod.setAccessible(true);
  }

  private void setStackTop(int value) throws Exception {
    // Use reflection to set private fields stack and stackSize
    var stackField = JsonWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    int[] stack = (int[]) stackField.get(jsonWriter);
    stack[0] = value;
    stackField.set(jsonWriter, stack);

    var stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonWriter, 1);
  }

  private void setLenient(boolean lenient) throws Exception {
    var lenientField = JsonWriter.class.getDeclaredField("lenient");
    lenientField.setAccessible(true);
    lenientField.setBoolean(jsonWriter, lenient);
  }

  private String getSeparator() throws Exception {
    var sepField = JsonWriter.class.getDeclaredField("separator");
    sepField.setAccessible(true);
    return (String) sepField.get(jsonWriter);
  }

  private void setSeparator(String separator) throws Exception {
    var sepField = JsonWriter.class.getDeclaredField("separator");
    sepField.setAccessible(true);
    sepField.set(jsonWriter, separator);
  }

  private int peek() throws Exception {
    Method peekMethod = JsonWriter.class.getDeclaredMethod("peek");
    peekMethod.setAccessible(true);
    return (int) peekMethod.invoke(jsonWriter);
  }

  private int getStackTop() throws Exception {
    var stackField = JsonWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    int[] stack = (int[]) stackField.get(jsonWriter);
    return stack[0];
  }

  @Test
    @Timeout(8000)
  void beforeValue_nonemptyDocumentLenientFalse_throwsIllegalStateException() throws Exception {
    setStackTop(NONEMPTY_DOCUMENT);
    setLenient(false);

    IllegalStateException ex = assertThrows(IllegalStateException.class, () -> beforeValueMethod.invoke(jsonWriter));
    assertEquals("JSON must have only one top-level value.", ex.getCause().getMessage());
  }

  @Test
    @Timeout(8000)
  void beforeValue_nonemptyDocumentLenientTrue_replacesTop() throws Exception {
    setStackTop(NONEMPTY_DOCUMENT);
    setLenient(true);

    beforeValueMethod.invoke(jsonWriter);

    assertEquals(NONEMPTY_DOCUMENT, getStackTop());
  }

  @Test
    @Timeout(8000)
  void beforeValue_emptyDocument_replacesTopToNonemptyDocument() throws Exception {
    setStackTop(EMPTY_DOCUMENT);

    beforeValueMethod.invoke(jsonWriter);

    assertEquals(NONEMPTY_DOCUMENT, getStackTop());
  }

  @Test
    @Timeout(8000)
  void beforeValue_emptyArray_replacesTopToNonemptyArrayAndCallsNewline() throws Exception {
    setStackTop(EMPTY_ARRAY);

    JsonWriter spyWriter = Mockito.spy(jsonWriter);
    // Replace original jsonWriter with spy for this test
    beforeValueMethod = JsonWriter.class.getDeclaredMethod("beforeValue");
    beforeValueMethod.setAccessible(true);

    // Replace stack and stackSize in spy
    var stackField = JsonWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    int[] stack = (int[]) stackField.get(spyWriter);
    stack[0] = EMPTY_ARRAY;
    stackField.set(spyWriter, stack);

    var stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(spyWriter, 1);

    // Use reflection to spy private method newline
    Method newlineMethod = JsonWriter.class.getDeclaredMethod("newline");
    newlineMethod.setAccessible(true);
    JsonWriter spyWriterForNewline = spyWriter;
    // Use Mockito spy with doNothing on private newline method via reflection proxy
    doAnswer(invocation -> {
      // do nothing on newline
      return null;
    }).when(spyWriterForNewline).newline();

    beforeValueMethod.invoke(spyWriter);

    assertEquals(NONEMPTY_ARRAY, getStackTopFrom(spyWriter));
    // Verify newline was called via reflection
    verify(spyWriter, times(1)).newline();
  }

  @Test
    @Timeout(8000)
  void beforeValue_nonemptyArray_appendsCommaAndCallsNewline() throws Throwable {
    setStackTop(NONEMPTY_ARRAY);

    JsonWriter spyWriter = Mockito.spy(jsonWriter);

    // Set stack and stackSize in spyWriter
    var stackField = JsonWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    int[] stack = (int[]) stackField.get(spyWriter);
    stack[0] = NONEMPTY_ARRAY;
    stackField.set(spyWriter, stack);

    var stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(spyWriter, 1);

    // Use reflection to spy private method newline
    Method newlineMethod = JsonWriter.class.getDeclaredMethod("newline");
    newlineMethod.setAccessible(true);
    JsonWriter spyWriterForNewline = spyWriter;
    doAnswer(invocation -> null).when(spyWriterForNewline).newline();

    // Use reflection to get 'out' field
    var outField = JsonWriter.class.getDeclaredField("out");
    outField.setAccessible(true);
    Writer spyOut = spy((Writer) outField.get(spyWriter));
    outField.set(spyWriter, spyOut);

    Method beforeValueSpy = JsonWriter.class.getDeclaredMethod("beforeValue");
    beforeValueSpy.setAccessible(true);

    beforeValueSpy.invoke(spyWriter);

    verify(spyWriter, times(1)).newline();
    verify(spyOut).append(',');
  }

  @Test
    @Timeout(8000)
  void beforeValue_danglingName_appendsSeparatorAndReplacesTop() throws Throwable {
    setStackTop(DANGLING_NAME);
    setSeparator(": ");

    JsonWriter spyWriter = Mockito.spy(jsonWriter);

    // Set stack and stackSize in spyWriter
    var stackField = JsonWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    int[] stack = (int[]) stackField.get(spyWriter);
    stack[0] = DANGLING_NAME;
    stackField.set(spyWriter, stack);

    var stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(spyWriter, 1);

    // Use reflection to get 'out' field and spy it
    var outField = JsonWriter.class.getDeclaredField("out");
    outField.setAccessible(true);
    Writer spyOut = spy((Writer) outField.get(spyWriter));
    outField.set(spyWriter, spyOut);

    Method beforeValueSpy = JsonWriter.class.getDeclaredMethod("beforeValue");
    beforeValueSpy.setAccessible(true);

    beforeValueSpy.invoke(spyWriter);

    verify(spyOut).append(getSeparator());
    assertEquals(NONEMPTY_OBJECT, getStackTopFrom(spyWriter));
  }

  @Test
    @Timeout(8000)
  void beforeValue_invalidState_throwsIllegalStateException() throws Throwable {
    setStackTop(EMPTY_OBJECT); // Not handled in switch, triggers default

    InvocationTargetException ex = assertThrows(InvocationTargetException.class, () -> beforeValueMethod.invoke(jsonWriter));
    assertTrue(ex.getCause() instanceof IllegalStateException);
    assertEquals("Nesting problem.", ex.getCause().getMessage());
  }

  // Helper to get stack top from a given JsonWriter instance
  private int getStackTopFrom(JsonWriter writer) throws Exception {
    var stackField = JsonWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    int[] stack = (int[]) stackField.get(writer);
    return stack[0];
  }
}