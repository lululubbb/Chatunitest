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

class JsonReader_205_3Test {

  private JsonReader jsonReader;

  @BeforeEach
  void setUp() {
    // Create a JsonReader with a dummy Reader (not used directly in nextQuotedValue)
    jsonReader = new JsonReader(mock(java.io.Reader.class));
  }

  private String invokeNextQuotedValue(char quote) throws Throwable {
    Method method = JsonReader.class.getDeclaredMethod("nextQuotedValue", char.class);
    method.setAccessible(true);
    try {
      return (String) method.invoke(jsonReader, quote);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }

  private void setPrivateField(String fieldName, Object value) throws Exception {
    var field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(jsonReader, value);
  }

  @Test
    @Timeout(8000)
  void nextQuotedValue_simpleQuotedString_returnsString() throws Throwable {
    // buffer = "hello\"rest"
    char[] buffer = "hello\"rest".toCharArray();
    setPrivateField("buffer", buffer);
    setPrivateField("pos", 0);
    setPrivateField("limit", buffer.length);
    setPrivateField("lineNumber", 0);
    setPrivateField("lineStart", 0);

    String result = invokeNextQuotedValue('"');
    assertEquals("hello", result);
    assertEquals(6, jsonReader.pos);
  }

  @Test
    @Timeout(8000)
  void nextQuotedValue_withEscapedCharacters_returnsUnescapedString() throws Throwable {
    // buffer = "hel\\\"lo\"rest"
    // positions: h=0 e=1 l=2 \=3 " escape at 4, l=5 o=6 " at 7
    char[] buffer = new char[] {'h', 'e', 'l', '\\', '"', 'l', 'o', '"'};
    setPrivateField("buffer", buffer);
    setPrivateField("pos", 0);
    setPrivateField("limit", buffer.length);
    setPrivateField("lineNumber", 0);
    setPrivateField("lineStart", 0);

    // Mock readEscapeCharacter to return '"'
    Method readEscapeCharacter = JsonReader.class.getDeclaredMethod("readEscapeCharacter");
    readEscapeCharacter.setAccessible(true);
    JsonReader spyReader = spy(jsonReader);
    doReturn('"').when(spyReader).readEscapeCharacter();

    // Replace jsonReader with spyReader for invocation
    Method method = JsonReader.class.getDeclaredMethod("nextQuotedValue", char.class);
    method.setAccessible(true);
    String result;
    try {
      result = (String) method.invoke(spyReader, '"');
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }

    assertEquals("hel\"lo", result);
    assertEquals(8, spyReader.pos);
  }

  @Test
    @Timeout(8000)
  void nextQuotedValue_multilineString_updatesLineNumberAndLineStart() throws Throwable {
    // buffer = "line1\nline2\""
    char[] buffer = "line1\nline2\"".toCharArray();
    setPrivateField("buffer", buffer);
    setPrivateField("pos", 0);
    setPrivateField("limit", buffer.length);
    setPrivateField("lineNumber", 0);
    setPrivateField("lineStart", 0);

    String result = invokeNextQuotedValue('"');

    assertEquals("line1\nline2", result);
    assertEquals(11, jsonReader.pos);
    assertEquals(1, jsonReader.lineNumber);
    assertEquals(6, jsonReader.lineStart);
  }

  @Test
    @Timeout(8000)
  void nextQuotedValue_unterminatedString_throwsIOException() throws Throwable {
    // buffer = "unterminated"
    char[] buffer = "unterminated".toCharArray();
    setPrivateField("buffer", buffer);
    setPrivateField("pos", 0);
    setPrivateField("limit", buffer.length);
    setPrivateField("lineNumber", 0);
    setPrivateField("lineStart", 0);

    // Override fillBuffer to return false to simulate EOF
    JsonReader spyReader = spy(jsonReader);
    doReturn(false).when(spyReader).fillBuffer(1);

    Method method = JsonReader.class.getDeclaredMethod("nextQuotedValue", char.class);
    method.setAccessible(true);

    Throwable thrown = null;
    try {
      method.invoke(spyReader, '"');
    } catch (InvocationTargetException e) {
      thrown = e.getCause();
    }

    assertNotNull(thrown);
    assertTrue(thrown instanceof IOException);
    assertTrue(thrown.getMessage().contains("Unterminated string"));
  }

  @Test
    @Timeout(8000)
  void nextQuotedValue_multipleEscapesAndLines() throws Throwable {
    // buffer includes escaped characters and newlines:
    // "a\\nb\\\"c\n\""
    char[] buffer = new char[] {
        'a', '\\', 'n', 'b', '\\', '"', 'c', '\n', '"'
    };
    setPrivateField("buffer", buffer);
    setPrivateField("pos", 0);
    setPrivateField("limit", buffer.length);
    setPrivateField("lineNumber", 0);
    setPrivateField("lineStart", 0);

    JsonReader spyReader = spy(jsonReader);
    // Setup readEscapeCharacter to return appropriate chars for each escape found
    // sequence: \n and \" -> returns '\n' then '"'
    doReturn('\n').doReturn('"').when(spyReader).readEscapeCharacter();

    Method method = JsonReader.class.getDeclaredMethod("nextQuotedValue", char.class);
    method.setAccessible(true);
    String result;
    try {
      result = (String) method.invoke(spyReader, '"');
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }

    assertEquals("a\nb\"c\n", result);
    assertEquals(buffer.length, spyReader.pos);
    assertEquals(1, spyReader.lineNumber);
    assertEquals(8, spyReader.lineStart);
  }
}