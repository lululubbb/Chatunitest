package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import java.io.Closeable;
import java.io.EOFException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonReader_205_5Test {

  private JsonReader jsonReader;

  @BeforeEach
  public void setUp() {
    // Create JsonReader with a dummy Reader (not used in nextQuotedValue)
    jsonReader = new JsonReader(mock(java.io.Reader.class));
  }

  @Test
    @Timeout(8000)
  public void testNextQuotedValue_simpleQuotedString() throws Throwable {
    // Prepare buffer with a quoted string: "hello"
    setField("buffer", "hello\"extra".toCharArray());
    setField("pos", 0);
    setField("limit", 6);
    setField("lineNumber", 0);
    setField("lineStart", 0);

    String result = invokeNextQuotedValue('"');
    assertEquals("hello", result);
    assertEquals(6, getField("pos"));
  }

  @Test
    @Timeout(8000)
  public void testNextQuotedValue_stringWithEscape() throws Throwable {
    // Prepare buffer with a quoted string containing an escape: "hel\\nlo"
    // buffer content: hel\nlo"
    char[] buffer = new char[] {'h','e','l','\\','n','l','o','"'};
    setField("buffer", buffer);
    setField("pos", 0);
    setField("limit", buffer.length);
    setField("lineNumber", 0);
    setField("lineStart", 0);

    // Mock readEscapeCharacter to return '\n' when called
    JsonReader spyReader = spy(jsonReader);
    doReturn('\n').when(spyReader).readEscapeCharacter();

    String result = invokeNextQuotedValue(spyReader, '"');
    assertEquals("hel\nlo", result);
    assertEquals(buffer.length, getField(spyReader, "pos"));
  }

  @Test
    @Timeout(8000)
  public void testNextQuotedValue_multilineString() throws Throwable {
    // Prepare buffer with a string that ends with newline inside before quote
    // "hello\nworld"
    char[] buffer = new char[] {'h','e','l','l','o','\n','w','o','r','l','d','"'};
    setField("buffer", buffer);
    setField("pos", 0);
    setField("limit", buffer.length);
    setField("lineNumber", 0);
    setField("lineStart", 0);

    String result = invokeNextQuotedValue('"');
    assertEquals("hello\nworld", result);
    assertEquals(buffer.length, getField("pos"));
    assertEquals(1, getField("lineNumber")); // lineNumber incremented once for \n
    assertEquals(6, getField("lineStart")); // lineStart set to position after \n
  }

  @Test
    @Timeout(8000)
  public void testNextQuotedValue_unterminatedString_throws() throws Throwable {
    // Prepare buffer with no closing quote, fillBuffer returns false
    char[] buffer = new char[] {'h','e','l','l','o'};
    setField("buffer", buffer);
    setField("pos", 0);
    setField("limit", buffer.length);
    setField("lineNumber", 0);
    setField("lineStart", 0);

    JsonReader spyReader = spy(jsonReader);
    doReturn(false).when(spyReader).fillBuffer(1);

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      invokeNextQuotedValue(spyReader, '"');
    });
    Throwable cause = thrown.getCause();
    assertTrue(cause instanceof IOException);
    assertTrue(cause.getMessage().contains("Unterminated string"));
  }

  @Test
    @Timeout(8000)
  public void testNextQuotedValue_builderAppendsMultipleSegments() throws Throwable {
    // Prepare buffer to simulate multiple segments with escapes and fillBuffer calls
    // First segment: hel\
    // Second segment: l\n
    // Third segment: o"
    char[] buffer1 = new char[] {'h','e','l','\\'};
    char[] buffer2 = new char[] {'l','\\','n'};
    char[] buffer3 = new char[] {'o','"'};

    JsonReader spyReader = spy(jsonReader);

    // Setup buffers and limits simulating reading in parts
    // We'll simulate fillBuffer to refill buffer with buffer2 then buffer3

    // Setup initial buffer and pointers
    setField(spyReader, "buffer", buffer1);
    setField(spyReader, "pos", 0);
    setField(spyReader, "limit", buffer1.length);
    setField(spyReader, "lineNumber", 0);
    setField(spyReader, "lineStart", 0);

    // readEscapeCharacter to return 'l' first, then '\n'
    doReturn('l').doReturn('\n').when(spyReader).readEscapeCharacter();

    // fillBuffer to simulate reading next buffers
    doAnswer(invocation -> {
      // On first call, refill buffer with buffer2
      setField(spyReader, "buffer", buffer2);
      setField(spyReader, "pos", 0);
      setField(spyReader, "limit", buffer2.length);
      return true;
    }).doAnswer(invocation -> {
      // On second call, refill buffer with buffer3
      setField(spyReader, "buffer", buffer3);
      setField(spyReader, "pos", 0);
      setField(spyReader, "limit", buffer3.length);
      return true;
    }).doReturn(false).when(spyReader).fillBuffer(1);

    String result = invokeNextQuotedValue(spyReader, '"');
    assertEquals("hell\no", result);
    assertEquals(buffer3.length, getField(spyReader, "pos"));
  }

  // Helper methods to access private fields and invoke private method

  private String invokeNextQuotedValue(char quote) throws Throwable {
    return invokeNextQuotedValue(jsonReader, quote);
  }

  private String invokeNextQuotedValue(JsonReader instance, char quote) throws Throwable {
    Method method = JsonReader.class.getDeclaredMethod("nextQuotedValue", char.class);
    method.setAccessible(true);
    try {
      return (String) method.invoke(instance, quote);
    } catch (InvocationTargetException e) {
      throw e;
    }
  }

  private void setField(String fieldName, Object value) {
    setField(jsonReader, fieldName, value);
  }

  private void setField(Object instance, String fieldName, Object value) {
    try {
      var field = JsonReader.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      if (field.getType().isArray() && value instanceof char[]) {
        // Defensive copy for char[] to avoid mutation issues
        char[] original = (char[]) value;
        char[] copy = new char[original.length];
        System.arraycopy(original, 0, copy, 0, original.length);
        field.set(instance, copy);
      } else {
        field.set(instance, value);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private <T> T getField(String fieldName) {
    return getField(jsonReader, fieldName);
  }

  @SuppressWarnings("unchecked")
  private <T> T getField(Object instance, String fieldName) {
    try {
      var field = JsonReader.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      return (T) field.get(instance);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}