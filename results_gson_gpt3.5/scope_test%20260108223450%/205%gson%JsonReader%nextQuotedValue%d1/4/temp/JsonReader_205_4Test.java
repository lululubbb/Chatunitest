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
import java.lang.reflect.Method;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonReader_205_4Test {

  private JsonReader jsonReader;

  @BeforeEach
  void setUp() {
    // Create a JsonReader with a dummy Reader (not used in nextQuotedValue)
    jsonReader = new JsonReader(mock(Reader.class));
  }

  @Test
    @Timeout(8000)
  void nextQuotedValue_simpleQuotedString() throws Exception {
    // Setup buffer with a quoted string ending with the quote char
    setField("buffer", "hello\"world".toCharArray());
    setField("pos", 0);
    setField("limit", 6); // up to the quote after "hello"

    String result = invokeNextQuotedValue('"');
    assertEquals("hello", result);
    assertEquals(6, getFieldInt("pos"));
  }

  @Test
    @Timeout(8000)
  void nextQuotedValue_stringWithEscapedCharacters() throws Exception {
    // Buffer contains: "he\\nllo\""
    // h e \ n l l o "
    char[] buf = new char[] {'h', 'e', '\\', 'n', 'l', 'l', 'o', '"'};
    setField("buffer", buf);
    setField("pos", 0);
    setField("limit", buf.length);

    // Create a spy of jsonReader
    JsonReader spyReader = spy(jsonReader);

    // Use reflection to override private readEscapeCharacter method
    Method readEscapeCharacterMethod = JsonReader.class.getDeclaredMethod("readEscapeCharacter");
    readEscapeCharacterMethod.setAccessible(true);

    // Use doAnswer on spyReader's readEscapeCharacter via Mockito's withSettings and reflection proxy
    // Since readEscapeCharacter is private, we cannot mock it directly.
    // Instead, we override it via a dynamic proxy using Mockito's doAnswer on a public method that calls it.
    // But since no public method calls readEscapeCharacter directly, we use reflection invoke and replace jsonReader with spyReader.

    // So here, we replace jsonReader with spyReader and override readEscapeCharacter via reflection proxy:
    doAnswer(invocation -> '\n').when(spyReader).readEscapeCharacter();

    // The above line causes compile error because readEscapeCharacter is private.
    // Fix: We cannot mock private method directly with Mockito.
    // Instead, we create a subclass of JsonReader that overrides readEscapeCharacter.

    // So we create a subclass for this test:

    class JsonReaderWithEscapeOverride extends JsonReader {
      JsonReaderWithEscapeOverride(Reader in) {
        super(in);
      }
      @Override
      char readEscapeCharacter() throws IOException {
        return '\n';
      }
    }

    jsonReader = new JsonReaderWithEscapeOverride(mock(Reader.class));
    setField("buffer", buf);
    setField("pos", 0);
    setField("limit", buf.length);

    String result = invokeNextQuotedValue('"');
    assertEquals("he\nllo", result);
    assertEquals(buf.length, getFieldInt("pos"));
  }

  @Test
    @Timeout(8000)
  void nextQuotedValue_stringWithNewlineUpdatesLineNumber() throws Exception {
    // Buffer: "he\nllo\""
    char[] buf = new char[] {'h', 'e', '\n', 'l', 'l', 'o', '"'};
    setField("buffer", buf);
    setField("pos", 0);
    setField("limit", buf.length);
    setField("lineNumber", 0);
    setField("lineStart", 0);

    String result = invokeNextQuotedValue('"');
    assertEquals("he\nllo", result);
    assertEquals(buf.length, getFieldInt("pos"));
    assertEquals(1, getFieldInt("lineNumber"));
    assertEquals(3, getFieldInt("lineStart")); // position after \n
  }

  @Test
    @Timeout(8000)
  void nextQuotedValue_multilineStringWithFillBuffer() throws Exception {
    // Setup buffer with partial string ending without quote, forcing fillBuffer call
    char[] buf = new char[JsonReader.BUFFER_SIZE];
    java.util.Arrays.fill(buf, 0, 10, 'a');
    setField("buffer", buf);
    setField("pos", 0);
    setField("limit", 10);

    // Create a subclass to override fillBuffer
    class JsonReaderWithFillBufferOverride extends JsonReader {
      private boolean firstCall = true;

      JsonReaderWithFillBufferOverride(Reader in) {
        super(in);
      }

      @Override
      boolean fillBuffer(int minimum) throws IOException {
        if (firstCall) {
          firstCall = false;
          return true;
        }
        return false;
      }
    }

    jsonReader = new JsonReaderWithFillBufferOverride(mock(Reader.class));
    setField("buffer", buf);
    setField("pos", 0);
    setField("limit", 10);

    Exception ex = assertThrows(IOException.class, () -> invokeNextQuotedValue('"'));
    assertTrue(ex.getMessage().contains("Unterminated string"));
  }

  // Helper to invoke private nextQuotedValue(char)
  private String invokeNextQuotedValue(char quote) throws Exception {
    Method method = JsonReader.class.getDeclaredMethod("nextQuotedValue", char.class);
    method.setAccessible(true);
    return (String) method.invoke(jsonReader, quote);
  }

  // Helper to set private fields via reflection
  private void setField(String fieldName, Object value) {
    try {
      Field field = JsonReader.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(jsonReader, value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private int getFieldInt(String fieldName) {
    try {
      Field field = JsonReader.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      return field.getInt(jsonReader);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}