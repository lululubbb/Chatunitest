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

class JsonReader_207_1Test {

  private JsonReader jsonReader;

  @BeforeEach
  void setUp() {
    // Create a JsonReader instance with a dummy Reader (not used in skipQuotedValue)
    jsonReader = new JsonReader(mock(java.io.Reader.class));
  }

  /**
   * Helper to set the internal buffer, pos, limit, lineNumber, lineStart fields via reflection.
   */
  private void setInternalState(char[] buffer, int pos, int limit, int lineNumber, int lineStart) throws Exception {
    var bufferField = JsonReader.class.getDeclaredField("buffer");
    bufferField.setAccessible(true);
    bufferField.set(jsonReader, buffer);

    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    posField.setInt(jsonReader, pos);

    var limitField = JsonReader.class.getDeclaredField("limit");
    limitField.setAccessible(true);
    limitField.setInt(jsonReader, limit);

    var lineNumberField = JsonReader.class.getDeclaredField("lineNumber");
    lineNumberField.setAccessible(true);
    lineNumberField.setInt(jsonReader, lineNumber);

    var lineStartField = JsonReader.class.getDeclaredField("lineStart");
    lineStartField.setAccessible(true);
    lineStartField.setInt(jsonReader, lineStart);
  }

  /**
   * Helper to get pos field value.
   */
  private int getPos() throws Exception {
    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    return posField.getInt(jsonReader);
  }

  /**
   * Helper to get lineNumber field value.
   */
  private int getLineNumber() throws Exception {
    var lineNumberField = JsonReader.class.getDeclaredField("lineNumber");
    lineNumberField.setAccessible(true);
    return lineNumberField.getInt(jsonReader);
  }

  /**
   * Helper to get lineStart field value.
   */
  private int getLineStart() throws Exception {
    var lineStartField = JsonReader.class.getDeclaredField("lineStart");
    lineStartField.setAccessible(true);
    return lineStartField.getInt(jsonReader);
  }

  /**
   * Helper to set pos field.
   */
  private void setPos(int pos) throws Exception {
    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    posField.setInt(jsonReader, pos);
  }

  /**
   * Helper to set limit field.
   */
  private void setLimit(int limit) throws Exception {
    var limitField = JsonReader.class.getDeclaredField("limit");
    limitField.setAccessible(true);
    limitField.setInt(jsonReader, limit);
  }

  /**
   * Helper to mock fillBuffer(int) method via subclassing to control its behavior.
   */
  private static class JsonReaderSpy extends JsonReader {
    boolean fillBufferReturnValue = false;

    JsonReaderSpy() {
      super(mock(java.io.Reader.class));
    }

    @Override
    protected boolean fillBuffer(int minimum) throws IOException {
      return fillBufferReturnValue;
    }

    @Override
    protected void readEscapeCharacter() throws IOException {
      // For testing, just increment pos by 1 to simulate consuming an escape sequence
      try {
        var posField = JsonReader.class.getDeclaredField("pos");
        posField.setAccessible(true);
        int pos = posField.getInt(this);
        posField.setInt(this, pos + 1);
      } catch (Exception e) {
        throw new IOException(e);
      }
    }
  }

  /**
   * Invoke private skipQuotedValue(char) method via reflection.
   */
  private void invokeSkipQuotedValue(char quote) throws Exception {
    Method method = JsonReader.class.getDeclaredMethod("skipQuotedValue", char.class);
    method.setAccessible(true);
    method.invoke(jsonReader, quote);
  }

  /**
   * Test that skipQuotedValue returns normally when quote character is found in buffer.
   */
  @Test
    @Timeout(8000)
  void skipQuotedValue_findsClosingQuote() throws Exception {
    // Setup buffer with quoted string: e.g. "abc\"def"
    char quote = '"';
    char[] buffer = new char[JsonReader.BUFFER_SIZE];
    buffer[0] = 'a';
    buffer[1] = 'b';
    buffer[2] = 'c';
    buffer[3] = '\\'; // escape char, should call readEscapeCharacter
    buffer[4] = 'n';  // part of escape sequence
    buffer[5] = '"';  // closing quote

    // Set initial pos and limit
    setInternalState(buffer, 0, 6, 0, 0);

    // Use JsonReaderSpy to override fillBuffer and readEscapeCharacter
    JsonReaderSpy spy = new JsonReaderSpy();
    // Copy buffer and fields from jsonReader into spy
    var bufferField = JsonReader.class.getDeclaredField("buffer");
    bufferField.setAccessible(true);
    bufferField.set(spy, buffer);

    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    posField.setInt(spy, 0);

    var limitField = JsonReader.class.getDeclaredField("limit");
    limitField.setAccessible(true);
    limitField.setInt(spy, 6);

    var lineNumberField = JsonReader.class.getDeclaredField("lineNumber");
    lineNumberField.setAccessible(true);
    lineNumberField.setInt(spy, 0);

    var lineStartField = JsonReader.class.getDeclaredField("lineStart");
    lineStartField.setAccessible(true);
    lineStartField.setInt(spy, 0);

    // fillBuffer returns false so loop will not continue infinitely
    spy.fillBufferReturnValue = false;

    // Use reflection to invoke skipQuotedValue on spy
    Method method = JsonReader.class.getDeclaredMethod("skipQuotedValue", char.class);
    method.setAccessible(true);
    method.invoke(spy, quote);

    // After method returns, pos should be after the closing quote (pos = 6)
    int posAfter = posField.getInt(spy);
    assertEquals(6, posAfter);

    // lineNumber and lineStart should remain unchanged
    int lineNumberAfter = lineNumberField.getInt(spy);
    int lineStartAfter = lineStartField.getInt(spy);
    assertEquals(0, lineNumberAfter);
    assertEquals(0, lineStartAfter);
  }

  /**
   * Test that skipQuotedValue increments lineNumber and lineStart when newline is found.
   */
  @Test
    @Timeout(8000)
  void skipQuotedValue_handlesNewline() throws Exception {
    char quote = '\'';
    char[] buffer = new char[JsonReader.BUFFER_SIZE];
    buffer[0] = 'a';
    buffer[1] = '\n';
    buffer[2] = '\'';

    setInternalState(buffer, 0, 3, 0, 0);

    // Use JsonReaderSpy to override fillBuffer and readEscapeCharacter
    JsonReaderSpy spy = new JsonReaderSpy();

    var bufferField = JsonReader.class.getDeclaredField("buffer");
    bufferField.setAccessible(true);
    bufferField.set(spy, buffer);

    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    posField.setInt(spy, 0);

    var limitField = JsonReader.class.getDeclaredField("limit");
    limitField.setAccessible(true);
    limitField.setInt(spy, 3);

    var lineNumberField = JsonReader.class.getDeclaredField("lineNumber");
    lineNumberField.setAccessible(true);
    lineNumberField.setInt(spy, 0);

    var lineStartField = JsonReader.class.getDeclaredField("lineStart");
    lineStartField.setAccessible(true);
    lineStartField.setInt(spy, 0);

    spy.fillBufferReturnValue = false;

    Method method = JsonReader.class.getDeclaredMethod("skipQuotedValue", char.class);
    method.setAccessible(true);
    method.invoke(spy, quote);

    // pos should be after closing quote
    int posAfter = posField.getInt(spy);
    assertEquals(3, posAfter);

    // lineNumber should be incremented by 1 due to \n
    int lineNumberAfter = lineNumberField.getInt(spy);
    assertEquals(1, lineNumberAfter);

    // lineStart should be updated to position after \n (pos 2)
    int lineStartAfter = lineStartField.getInt(spy);
    assertEquals(2, lineStartAfter);
  }

  /**
   * Test that skipQuotedValue calls readEscapeCharacter and updates pos accordingly.
   */
  @Test
    @Timeout(8000)
  void skipQuotedValue_handlesEscapeCharacter() throws Exception {
    char quote = '"';
    char[] buffer = new char[JsonReader.BUFFER_SIZE];
    buffer[0] = '\\';
    buffer[1] = '"';

    setInternalState(buffer, 0, 2, 0, 0);

    JsonReaderSpy spy = new JsonReaderSpy();

    var bufferField = JsonReader.class.getDeclaredField("buffer");
    bufferField.setAccessible(true);
    bufferField.set(spy, buffer);

    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    posField.setInt(spy, 0);

    var limitField = JsonReader.class.getDeclaredField("limit");
    limitField.setAccessible(true);
    limitField.setInt(spy, 2);

    var lineNumberField = JsonReader.class.getDeclaredField("lineNumber");
    lineNumberField.setAccessible(true);
    lineNumberField.setInt(spy, 0);

    var lineStartField = JsonReader.class.getDeclaredField("lineStart");
    lineStartField.setAccessible(true);
    lineStartField.setInt(spy, 0);

    spy.fillBufferReturnValue = false;

    Method method = JsonReader.class.getDeclaredMethod("skipQuotedValue", char.class);
    method.setAccessible(true);
    method.invoke(spy, quote);

    // pos should be updated by readEscapeCharacter (pos incremented by 1 inside readEscapeCharacter)
    int posAfter = posField.getInt(spy);
    // After reading escape at pos 0, readEscapeCharacter increments pos by 1, then loop continues.
    // The closing quote is at buffer[1], so pos should be 2 after finishing.
    assertEquals(2, posAfter);
  }

  /**
   * Test that skipQuotedValue calls fillBuffer when buffer is exhausted and continues reading.
   */
  @Test
    @Timeout(8000)
  void skipQuotedValue_callsFillBufferAndContinues() throws Exception {
    char quote = '"';

    // Setup buffer with no closing quote in first limit chars, will force fillBuffer call
    char[] buffer = new char[JsonReader.BUFFER_SIZE];
    buffer[0] = 'a';
    buffer[1] = 'b';

    // Use JsonReaderSpy to override fillBuffer and readEscapeCharacter
    JsonReaderSpy spy = new JsonReaderSpy();

    var bufferField = JsonReader.class.getDeclaredField("buffer");
    bufferField.setAccessible(true);
    bufferField.set(spy, buffer);

    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    posField.setInt(spy, 0);

    var limitField = JsonReader.class.getDeclaredField("limit");
    limitField.setAccessible(true);
    limitField.setInt(spy, 2);

    var lineNumberField = JsonReader.class.getDeclaredField("lineNumber");
    lineNumberField.setAccessible(true);
    lineNumberField.setInt(spy, 0);

    var lineStartField = JsonReader.class.getDeclaredField("lineStart");
    lineStartField.setAccessible(true);
    lineStartField.setInt(spy, 0);

    // Setup fillBuffer to return true once, then false to simulate more data then EOF
    final int[] callCount = {0};
    spy = new JsonReaderSpy() {
      @Override
      protected boolean fillBuffer(int minimum) {
        callCount[0]++;
        if (callCount[0] == 1) {
          // On first call, add closing quote at buffer[2]
          try {
            var bufferField = JsonReader.class.getDeclaredField("buffer");
            bufferField.setAccessible(true);
            char[] buf = (char[]) bufferField.get(this);
            buf[2] = '"';

            var limitField = JsonReader.class.getDeclaredField("limit");
            limitField.setAccessible(true);
            limitField.setInt(this, 3);
          } catch (Exception e) {
            throw new RuntimeException(e);
          }
          return true;
        }
        return false;
      }
    };

    bufferField.set(spy, buffer);
    posField.setInt(spy, 0);
    limitField.setInt(spy, 2);
    lineNumberField.setInt(spy, 0);
    lineStartField.setInt(spy, 0);

    Method method = JsonReader.class.getDeclaredMethod("skipQuotedValue", char.class);
    method.setAccessible(true);
    method.invoke(spy, quote);

    // pos should be after closing quote (pos = 3)
    int posAfter = posField.getInt(spy);
    assertEquals(3, posAfter);

    // fillBuffer should be called at least once
    assertTrue(callCount[0] >= 1);
  }

  /**
   * Test that skipQuotedValue throws IOException with "Unterminated string" when no closing quote found.
   */
  @Test
    @Timeout(8000)
  void skipQuotedValue_throwsOnUnterminatedString() throws Exception {
    char quote = '"';

    char[] buffer = new char[JsonReader.BUFFER_SIZE];
    buffer[0] = 'a';
    buffer[1] = 'b';

    setInternalState(buffer, 0, 2, 0, 0);

    JsonReaderSpy spy = new JsonReaderSpy();

    var bufferField = JsonReader.class.getDeclaredField("buffer");
    bufferField.setAccessible(true);
    bufferField.set(spy, buffer);

    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    posField.setInt(spy, 0);

    var limitField = JsonReader.class.getDeclaredField("limit");
    limitField.setAccessible(true);
    limitField.setInt(spy, 2);

    var lineNumberField = JsonReader.class.getDeclaredField("lineNumber");
    lineNumberField.setAccessible(true);
    lineNumberField.setInt(spy, 0);

    var lineStartField = JsonReader.class.getDeclaredField("lineStart");
    lineStartField.setAccessible(true);
    lineStartField.setInt(spy, 0);

    // fillBuffer returns false to simulate EOF without closing quote
    spy.fillBufferReturnValue = false;

    Method method = JsonReader.class.getDeclaredMethod("skipQuotedValue", char.class);
    method.setAccessible(true);

    IOException thrown = assertThrows(IOException.class, () -> method.invoke(spy, quote),
        "Expected skipQuotedValue to throw IOException for unterminated string");

    // InvocationTargetException wraps the IOException, unwrap it
    Throwable cause = thrown.getCause();
    assertNotNull(cause);
    assertTrue(cause instanceof IOException);
    assertTrue(cause.getMessage().contains("Unterminated string"));
  }
}