package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import java.io.Closeable;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonReader_207_5Test {

  private JsonReader jsonReader;

  @BeforeEach
  public void setUp() {
    // Create a JsonReader with a dummy Reader since we will manipulate buffer directly
    jsonReader = new JsonReader(mock(Reader.class));
  }

  private void setField(String fieldName, Object value) throws Exception {
    var field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(jsonReader, value);
  }

  private Object getField(String fieldName) throws Exception {
    var field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(jsonReader);
  }

  private void invokeSkipQuotedValue(Object target, char quote) throws Throwable {
    Method method = JsonReader.class.getDeclaredMethod("skipQuotedValue", char.class);
    method.setAccessible(true);
    try {
      method.invoke(target, quote);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }

  private void invokeReadEscapeCharacter(Object target) throws Throwable {
    Method method = JsonReader.class.getDeclaredMethod("readEscapeCharacter");
    method.setAccessible(true);
    try {
      method.invoke(target);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }

  private boolean invokeFillBuffer(Object target, int minimum) throws Throwable {
    Method method = JsonReader.class.getDeclaredMethod("fillBuffer", int.class);
    method.setAccessible(true);
    try {
      return (boolean) method.invoke(target, minimum);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }

  @Test
    @Timeout(8000)
  public void skipQuotedValue_closesAtQuote() throws Throwable {
    // Setup buffer with a quoted string ending with quote character '"'
    char[] buffer = new char[1024];
    buffer[0] = 'a';
    buffer[1] = 'b';
    buffer[2] = '"';
    setField("buffer", buffer);
    setField("pos", 0);
    setField("limit", 3);
    setField("lineNumber", 0);
    setField("lineStart", 0);

    // Call skipQuotedValue with quote '"'
    invokeSkipQuotedValue(jsonReader, '"');

    // pos should be after the quote character (3)
    int pos = (int) getField("pos");
    assertEquals(3, pos);
  }

  @Test
    @Timeout(8000)
  public void skipQuotedValue_handlesEscapeCharacter() throws Throwable {
    // Setup buffer with escape sequence: "a\"b"
    char[] buffer = new char[1024];
    buffer[0] = 'a';
    buffer[1] = '\\';
    buffer[2] = 'n'; // escape character '\n'
    buffer[3] = '"';
    setField("buffer", buffer);
    setField("pos", 0);
    setField("limit", 4);
    setField("lineNumber", 0);
    setField("lineStart", 0);

    // Spy on jsonReader to mock private readEscapeCharacter method via reflection
    JsonReader spyReader = spy(jsonReader);

    // Use reflection to override readEscapeCharacter behavior
    // We cannot mock private methods directly with Mockito, so use doAnswer on a public wrapper
    // Instead, we override readEscapeCharacter by using a dynamic proxy or by using a subclass.
    // Since subclassing is simpler here, create an anonymous subclass:

    JsonReader testReader = new JsonReader(mock(Reader.class)) {
      @Override
      protected char readEscapeCharacter() throws IOException {
        try {
          int pos = (int) getField("pos");
          setField("pos", pos + 1);
        } catch (Exception e) {
          throw new IOException(e);
        }
        return 'n';
      }

      @Override
      protected boolean fillBuffer(int minimum) throws IOException {
        try {
          return (boolean) invokeFillBuffer(this, minimum);
        } catch (Throwable t) {
          throw new IOException(t);
        }
      }
    };

    // Copy fields from spyReader to testReader
    Field bufferField = JsonReader.class.getDeclaredField("buffer");
    bufferField.setAccessible(true);
    bufferField.set(testReader, buffer);
    setField("pos", 0);
    setField("limit", 4);
    setField("lineNumber", 0);
    setField("lineStart", 0);

    // Actually set pos, limit, lineNumber, lineStart on testReader
    Field posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    posField.set(testReader, 0);

    Field limitField = JsonReader.class.getDeclaredField("limit");
    limitField.setAccessible(true);
    limitField.set(testReader, 4);

    Field lineNumberField = JsonReader.class.getDeclaredField("lineNumber");
    lineNumberField.setAccessible(true);
    lineNumberField.set(testReader, 0);

    Field lineStartField = JsonReader.class.getDeclaredField("lineStart");
    lineStartField.setAccessible(true);
    lineStartField.set(testReader, 0);

    // Invoke skipQuotedValue on testReader
    invokeSkipQuotedValue(testReader, '"');

    int pos = (int) posField.get(testReader);
    // pos should be after the closing quote (4)
    assertEquals(4, pos);
  }

  @Test
    @Timeout(8000)
  public void skipQuotedValue_incrementsLineNumberOnNewLine() throws Throwable {
    // Setup buffer with '\n' and then closing quote
    char[] buffer = new char[1024];
    buffer[0] = 'a';
    buffer[1] = '\n';
    buffer[2] = '"';
    setField("buffer", buffer);
    setField("pos", 0);
    setField("limit", 3);
    setField("lineNumber", 1);
    setField("lineStart", 0);

    invokeSkipQuotedValue(jsonReader, '"');

    int lineNumber = (int) getField("lineNumber");
    int lineStart = (int) getField("lineStart");
    int pos = (int) getField("pos");

    assertEquals(2, lineNumber);
    assertEquals(pos, lineStart);
    assertEquals(3, pos);
  }

  @Test
    @Timeout(8000)
  public void skipQuotedValue_throwsOnUnterminatedString() throws Throwable {
    // Setup buffer with no closing quote, fillBuffer returns false
    char[] buffer = new char[1024];
    buffer[0] = 'a';
    buffer[1] = 'b';

    // Create a subclass to override fillBuffer to always return false
    JsonReader testReader = new JsonReader(mock(Reader.class)) {
      @Override
      protected boolean fillBuffer(int minimum) {
        return false;
      }
    };

    // Set buffer and other fields on testReader
    Field bufferField = JsonReader.class.getDeclaredField("buffer");
    bufferField.setAccessible(true);
    bufferField.set(testReader, buffer);

    Field posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    posField.set(testReader, 0);

    Field limitField = JsonReader.class.getDeclaredField("limit");
    limitField.setAccessible(true);
    limitField.set(testReader, 2);

    Field lineNumberField = JsonReader.class.getDeclaredField("lineNumber");
    lineNumberField.setAccessible(true);
    lineNumberField.set(testReader, 0);

    Field lineStartField = JsonReader.class.getDeclaredField("lineStart");
    lineStartField.setAccessible(true);
    lineStartField.set(testReader, 0);

    Method method = JsonReader.class.getDeclaredMethod("skipQuotedValue", char.class);
    method.setAccessible(true);

    Throwable thrown = assertThrows(IOException.class, () -> {
      try {
        method.invoke(testReader, '"');
      } catch (InvocationTargetException e) {
        throw e.getCause();
      }
    });
    assertTrue(thrown.getMessage().contains("Unterminated string"));
  }
}