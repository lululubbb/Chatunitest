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

class JsonReader_205_2Test {
  private JsonReader jsonReader;

  @BeforeEach
  void setUp() {
    jsonReader = new JsonReader(mock(java.io.Reader.class));
  }

  @Test
    @Timeout(8000)
  void nextQuotedValue_returnsSimpleQuotedString() throws Throwable {
    // Setup buffer with quoted string: "hello"
    setField("buffer", new char[] {'h', 'e', 'l', 'l', 'o', '"'});
    setField("pos", 0);
    setField("limit", 6);
    String result = invokeNextQuotedValue('"');
    assertEquals("hello", result);
    assertEquals(6, getField("pos"));
  }

  @Test
    @Timeout(8000)
  void nextQuotedValue_handlesEscapeCharacter() throws Throwable {
    // Setup buffer with quoted string: "hel\nlo"
    // 'hel' + '\' + 'n' + 'lo"'
    setField("buffer", new char[] {'h', 'e', 'l', '\\', 'n', 'l', 'o', '"'});
    setField("pos", 0);
    setField("limit", 8);

    // Mock readEscapeCharacter to return '\n' when called
    Method readEscapeCharacter = JsonReader.class.getDeclaredMethod("readEscapeCharacter");
    readEscapeCharacter.setAccessible(true);
    JsonReader spyReader = spy(jsonReader);
    doReturn('\n').when(spyReader).readEscapeCharacter();

    // Replace jsonReader with spyReader for invocation
    String result = invokeNextQuotedValue(spyReader, '"');
    assertEquals("hel\nlo", result);
    assertEquals(8, getField(spyReader, "pos"));
  }

  @Test
    @Timeout(8000)
  void nextQuotedValue_handlesNewlineInBuffer() throws Throwable {
    // Setup buffer with quoted string containing newline character
    // "hel\nlo"
    char[] buf = new char[] {'h', 'e', 'l', '\n', 'l', 'o', '"'};
    setField("buffer", buf);
    setField("pos", 0);
    setField("limit", 7);
    setField("lineNumber", 0);
    setField("lineStart", 0);

    String result = invokeNextQuotedValue('"');
    assertEquals("hel\nlo", result);
    assertEquals(7, getField("pos"));
    assertEquals(1, getField("lineNumber"));
    assertEquals(4, getField("lineStart"));
  }

  @Test
    @Timeout(8000)
  void nextQuotedValue_readsMultipleBuffers() throws Throwable {
    // Setup partial buffer and mock fillBuffer to simulate reading multiple times
    char[] initialBuffer = new char[] {'h', 'e', 'l', 'l', 'o'};
    setField("buffer", initialBuffer);
    setField("pos", 0);
    setField("limit", 5);

    JsonReader spyReader = spy(jsonReader);
    doAnswer(invocation -> {
      setField(spyReader, "buffer", new char[] {' ', 'w', 'o', 'r', 'l', 'd', '"'});
      setField(spyReader, "pos", 0);
      setField(spyReader, "limit", 7);
      return true;
    }).when(spyReader).fillBuffer(1);

    String result = invokeNextQuotedValue(spyReader, '"');
    assertEquals("hello world", result);
  }

  @Test
    @Timeout(8000)
  void nextQuotedValue_throwsSyntaxErrorWhenUnterminated() throws Throwable {
    // Setup buffer without closing quote and fillBuffer returns false
    char[] buf = new char[] {'h', 'e', 'l', 'l', 'o'};
    setField("buffer", buf);
    setField("pos", 0);
    setField("limit", 5);

    JsonReader spyReader = spy(jsonReader);
    doReturn(false).when(spyReader).fillBuffer(1);

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      invokeNextQuotedValue(spyReader, '"');
    });
    assertTrue(thrown.getCause() instanceof IOException);
    assertTrue(thrown.getCause().getMessage().contains("Unterminated string"));
  }

  // Helper to invoke private nextQuotedValue on default jsonReader
  private String invokeNextQuotedValue(char quote) throws Throwable {
    return invokeNextQuotedValue(jsonReader, quote);
  }

  // Helper to invoke private nextQuotedValue on given JsonReader instance
  private String invokeNextQuotedValue(JsonReader reader, char quote) throws Throwable {
    Method method = JsonReader.class.getDeclaredMethod("nextQuotedValue", char.class);
    method.setAccessible(true);
    try {
      return (String) method.invoke(reader, quote);
    } catch (InvocationTargetException e) {
      throw e;
    }
  }

  // Helper to set private fields on default jsonReader
  private void setField(String name, Object value) {
    setField(jsonReader, name, value);
  }

  // Helper to set private fields on given JsonReader instance
  private void setField(JsonReader instance, String name, Object value) {
    try {
      java.lang.reflect.Field field = JsonReader.class.getDeclaredField(name);
      field.setAccessible(true);
      field.set(instance, value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  // Helper to get private int fields from default jsonReader
  private int getField(String name) {
    return getField(jsonReader, name);
  }

  // Helper to get private int fields from given JsonReader instance
  private int getField(JsonReader instance, String name) {
    try {
      java.lang.reflect.Field field = JsonReader.class.getDeclaredField(name);
      field.setAccessible(true);
      return field.getInt(instance);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}