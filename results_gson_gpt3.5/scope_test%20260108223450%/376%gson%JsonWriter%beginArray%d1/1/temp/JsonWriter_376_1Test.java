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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonWriter_376_1Test {

  private StringWriter stringWriter;
  private JsonWriter jsonWriter;

  @BeforeEach
  void setUp() {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);
    // Initialize stack and stackSize properly to avoid "JsonWriter is closed" errors
    int[] stack = new int[32];
    stack[0] = JsonScope.EMPTY_DOCUMENT;
    setField(jsonWriter, "stack", stack);
    setField(jsonWriter, "stackSize", 1);
    setField(jsonWriter, "deferredName", null);
  }

  @Test
    @Timeout(8000)
  void beginArray_shouldReturnJsonWriterAndWriteOpenArray() throws IOException {
    // Arrange
    // stack and stackSize already set in @BeforeEach

    // Act
    JsonWriter result = jsonWriter.beginArray();

    // Assert
    assertSame(jsonWriter, result);

    // The output should contain '['
    String output = stringWriter.toString();
    assertTrue(output.contains("["));

    // The stack top should now be EMPTY_ARRAY (1)
    int stackSize = (int) getField(jsonWriter, "stackSize");
    assertEquals(2, stackSize); // stackSize increments by 1 after beginArray
    int[] updatedStack = (int[]) getField(jsonWriter, "stack");
    assertEquals(JsonScope.EMPTY_ARRAY, updatedStack[stackSize - 1]);
  }

  @Test
    @Timeout(8000)
  void beginArray_shouldCallWriteDeferredName_whenDeferredNameIsNotNull() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    // Arrange
    int[] stack = new int[32];
    stack[0] = JsonScope.EMPTY_OBJECT; // Changed from EMPTY_DOCUMENT to EMPTY_OBJECT to avoid nesting problem
    setField(jsonWriter, "stack", stack);
    setField(jsonWriter, "stackSize", 1);
    setField(jsonWriter, "deferredName", "someName");

    // Spy on jsonWriter to verify writeDeferredName call via reflection
    JsonWriter spyWriter = spy(jsonWriter);

    // Use reflection to invoke beginArray on spyWriter
    Method beginArrayMethod = JsonWriter.class.getDeclaredMethod("beginArray");
    beginArrayMethod.setAccessible(true);
    beginArrayMethod.invoke(spyWriter);

    // Verify deferredName was reset to null after writeDeferredName call
    assertNull(getField(spyWriter, "deferredName"));
  }

  @Test
    @Timeout(8000)
  void beginArray_shouldThrowIOException_ifWriterThrows() throws IOException {
    Writer mockWriter = mock(Writer.class);
    // Make sure any write call throws IOException
    doThrow(new IOException("test exception")).when(mockWriter).write(any(char[].class), anyInt(), anyInt());
    // Also make sure flush() throws IOException to cover other calls
    doThrow(new IOException("test exception")).when(mockWriter).flush();

    JsonWriter writerWithMock = new JsonWriter(mockWriter);

    // Arrange stack so open() will attempt to write
    int[] stack = new int[32];
    stack[0] = JsonScope.EMPTY_DOCUMENT;
    setField(writerWithMock, "stack", stack);
    setField(writerWithMock, "stackSize", 1);
    setField(writerWithMock, "deferredName", null);

    // Act & Assert
    IOException thrown = assertThrows(IOException.class, writerWithMock::beginArray);
    assertEquals("test exception", thrown.getMessage());
  }

  // Helper methods for reflection

  private static void setField(Object target, String fieldName, Object value) {
    try {
      var field = target.getClass().getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(target, value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static Object getField(Object target, String fieldName) {
    try {
      var field = target.getClass().getDeclaredField(fieldName);
      field.setAccessible(true);
      return field.get(target);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}