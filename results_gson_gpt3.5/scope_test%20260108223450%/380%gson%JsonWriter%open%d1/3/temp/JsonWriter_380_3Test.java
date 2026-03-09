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
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonWriterOpenTest {

  private JsonWriter jsonWriter;
  private Writer mockWriter;

  @BeforeEach
  void setUp() throws Exception {
    mockWriter = mock(Writer.class);
    jsonWriter = new JsonWriter(mockWriter);

    // Initialize stackSize to 0 and stack array to empty state (already done in constructor)
    // But just to be safe, reset stackSize to 0 via reflection
    Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonWriter, 0);
  }

  @Test
    @Timeout(8000)
  void open_shouldPushEmptyAndWriteOpenBracket_andReturnThis() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method openMethod = JsonWriter.class.getDeclaredMethod("open", int.class, char.class);
    openMethod.setAccessible(true);

    // Call open with EMPTY_ARRAY (1) and '['
    JsonWriter returned = (JsonWriter) openMethod.invoke(jsonWriter, JsonScope.EMPTY_ARRAY, '[');

    // Verify beforeValue() caused stack push and out.write called
    verify(mockWriter).write('[');
    assertSame(jsonWriter, returned);

    // The stack should have one element with value EMPTY_ARRAY (1)
    Method peekMethod = JsonWriter.class.getDeclaredMethod("peek");
    peekMethod.setAccessible(true);
    int top = (int) peekMethod.invoke(jsonWriter);
    assertEquals(JsonScope.EMPTY_ARRAY, top);
  }

  @Test
    @Timeout(8000)
  void open_shouldCallBeforeValueAndPushMultipleScopes() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
    Method openMethod = JsonWriter.class.getDeclaredMethod("open", int.class, char.class);
    openMethod.setAccessible(true);

    // Call open with EMPTY_OBJECT (3) and '{'
    JsonWriter returned = (JsonWriter) openMethod.invoke(jsonWriter, JsonScope.EMPTY_OBJECT, '{');
    verify(mockWriter).write('{');
    assertSame(jsonWriter, returned);

    Method peekMethod = JsonWriter.class.getDeclaredMethod("peek");
    peekMethod.setAccessible(true);
    int top = (int) peekMethod.invoke(jsonWriter);
    assertEquals(JsonScope.EMPTY_OBJECT, top);

    // Call open again with EMPTY_ARRAY to check stack increments
    returned = (JsonWriter) openMethod.invoke(jsonWriter, JsonScope.EMPTY_ARRAY, '[');
    verify(mockWriter).write('[');
    assertSame(jsonWriter, returned);

    top = (int) peekMethod.invoke(jsonWriter);
    assertEquals(JsonScope.EMPTY_ARRAY, top);

    // Stack size should be 2 now, verify by reflection on stackSize field
    Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    int stackSize = (int) stackSizeField.get(jsonWriter);
    assertEquals(2, stackSize);
  }

  @Test
    @Timeout(8000)
  void open_shouldThrowIOException_whenWriteFails() throws NoSuchMethodException {
    try {
      doThrow(new IOException("write failed")).when(mockWriter).write('[');
      Method openMethod = JsonWriter.class.getDeclaredMethod("open", int.class, char.class);
      openMethod.setAccessible(true);
      openMethod.invoke(jsonWriter, JsonScope.EMPTY_ARRAY, '[');
      fail("Expected InvocationTargetException caused by IOException");
    } catch (InvocationTargetException e) {
      assertTrue(e.getCause() instanceof IOException);
      assertEquals("write failed", e.getCause().getMessage());
    } catch (Exception e) {
      fail("Expected InvocationTargetException but got " + e);
    }
  }
}