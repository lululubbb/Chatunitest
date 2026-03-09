package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import java.io.Closeable;
import java.io.EOFException;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonReader_207_6Test {

  private JsonReader jsonReader;

  @BeforeEach
  public void setUp() {
    Reader mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  private void setField(Object target, String fieldName, Object value) throws Exception {
    var field = target.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  private Object invokeSkipQuotedValue(char quote) throws Exception {
    Method method = JsonReader.class.getDeclaredMethod("skipQuotedValue", char.class);
    method.setAccessible(true);
    return method.invoke(jsonReader, quote);
  }

  @Test
    @Timeout(8000)
  public void skipQuotedValue_closesOnQuote() throws Exception {
    // Setup buffer with a quoted string: 'a', 'b', quote at the end
    char quote = '"';
    char[] buffer = new char[JsonReader.BUFFER_SIZE];
    buffer[0] = 'a';
    buffer[1] = 'b';
    buffer[2] = quote;
    setField(jsonReader, "buffer", buffer);
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", 3);
    setField(jsonReader, "lineNumber", 0);
    setField(jsonReader, "lineStart", 0);

    // Invoke skipQuotedValue, should return normally and pos should move past quote
    invokeSkipQuotedValue(quote);

    int pos = (int) jsonReader.getClass().getDeclaredField("pos").get(jsonReader);
    assertEquals(3, pos);
  }

  @Test
    @Timeout(8000)
  public void skipQuotedValue_handlesEscapeCharacter() throws Exception {
    // Setup buffer with a backslash escape sequence and a closing quote
    char quote = '"';
    char[] buffer = new char[JsonReader.BUFFER_SIZE];
    buffer[0] = 'a';
    buffer[1] = '\\';
    buffer[2] = 'n'; // escape character
    buffer[3] = quote;
    setField(jsonReader, "buffer", buffer);
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", 4);
    setField(jsonReader, "lineNumber", 0);
    setField(jsonReader, "lineStart", 0);

    // Mock readEscapeCharacter to simulate consuming escape char and advancing pos
    JsonReader spyReader = spy(jsonReader);
    doAnswer(invocation -> {
      // simulate readEscapeCharacter advancing pos by 1
      int pos = (int) spyReader.getClass().getDeclaredField("pos").get(spyReader);
      spyReader.getClass().getDeclaredField("pos").set(spyReader, pos + 1);
      return '\n';
    }).when(spyReader).getClass().getDeclaredMethod("readEscapeCharacter").invoke(spyReader);

    // Instead of spy, use reflection to override readEscapeCharacter to advance pos
    Method readEscapeCharacterMethod = JsonReader.class.getDeclaredMethod("readEscapeCharacter");
    readEscapeCharacterMethod.setAccessible(true);

    // We'll create a subclass to override readEscapeCharacter for the test
    JsonReader testReader = new JsonReader(jsonReader.in) {
      @Override
      protected char readEscapeCharacter() throws IOException {
        try {
          int pos = (int) getClass().getSuperclass().getDeclaredField("pos").get(this);
          getClass().getSuperclass().getDeclaredField("pos").set(this, pos + 1);
        } catch (Exception e) {
          throw new IOException(e);
        }
        return '\n';
      }
    };

    setField(testReader, "buffer", buffer);
    setField(testReader, "pos", 0);
    setField(testReader, "limit", 4);
    setField(testReader, "lineNumber", 0);
    setField(testReader, "lineStart", 0);

    Method method = JsonReader.class.getDeclaredMethod("skipQuotedValue", char.class);
    method.setAccessible(true);
    method.invoke(testReader, quote);

    int pos = (int) testReader.getClass().getDeclaredField("pos").get(testReader);
    assertEquals(4, pos);
  }

  @Test
    @Timeout(8000)
  public void skipQuotedValue_incrementsLineNumberOnNewline() throws Exception {
    char quote = '"';
    char[] buffer = new char[JsonReader.BUFFER_SIZE];
    buffer[0] = 'a';
    buffer[1] = '\n';
    buffer[2] = quote;
    setField(jsonReader, "buffer", buffer);
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", 3);
    setField(jsonReader, "lineNumber", 5);
    setField(jsonReader, "lineStart", 0);

    invokeSkipQuotedValue(quote);

    int lineNumber = (int) jsonReader.getClass().getDeclaredField("lineNumber").get(jsonReader);
    int lineStart = (int) jsonReader.getClass().getDeclaredField("lineStart").get(jsonReader);
    int pos = (int) jsonReader.getClass().getDeclaredField("pos").get(jsonReader);

    assertEquals(6, lineNumber);
    assertEquals(2, lineStart);
    assertEquals(3, pos);
  }

  @Test
    @Timeout(8000)
  public void skipQuotedValue_throwsOnUnterminatedString() throws Exception {
    char quote = '"';
    char[] buffer = new char[JsonReader.BUFFER_SIZE];
    buffer[0] = 'a';
    buffer[1] = 'b';
    setField(jsonReader, "buffer", buffer);
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", 2);
    setField(jsonReader, "lineNumber", 0);
    setField(jsonReader, "lineStart", 0);

    // Override fillBuffer to return false to simulate EOF
    Method fillBufferMethod = JsonReader.class.getDeclaredMethod("fillBuffer", int.class);
    fillBufferMethod.setAccessible(true);

    JsonReader testReader = new JsonReader(jsonReader.in) {
      @Override
      protected boolean fillBuffer(int minimum) {
        return false;
      }

      @Override
      protected IOException syntaxError(String message) {
        return new IOException(message);
      }
    };
    setField(testReader, "buffer", buffer);
    setField(testReader, "pos", 0);
    setField(testReader, "limit", 2);

    Method skipMethod = JsonReader.class.getDeclaredMethod("skipQuotedValue", char.class);
    skipMethod.setAccessible(true);

    IOException thrown = assertThrows(IOException.class, () -> {
      skipMethod.invoke(testReader, quote);
    });

    assertTrue(thrown.getCause().getMessage().contains("Unterminated string"));
  }
}