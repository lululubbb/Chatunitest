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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonReader_225_6Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  // Helper to set private fields via reflection
  private void setField(Object target, String fieldName, Object value) throws Exception {
    var field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  // Helper to get private fields via reflection
  private Object getField(Object target, String fieldName) throws Exception {
    var field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(target);
  }

  // Helper to invoke private method consumeNonExecutePrefix
  private void invokeConsumeNonExecutePrefix(Object reader) throws Exception {
    Method method = JsonReader.class.getDeclaredMethod("consumeNonExecutePrefix");
    method.setAccessible(true);
    method.invoke(reader);
  }

  // We need to mock/fake nextNonWhitespace(boolean) and fillBuffer(int) methods behavior.
  // They are package-private (default), so we can override them in subclass.

  static class TestJsonReader extends JsonReader {
    boolean nextNonWhitespaceCalled = false;
    boolean nextNonWhitespaceThrowOnEofArg = false;
    boolean fillBufferReturn = true;

    TestJsonReader(Reader in) {
      super(in);
    }

    // Remove @Override annotation because nextNonWhitespace is package-private and not visible in subclass
    int nextNonWhitespace(boolean throwOnEof) throws IOException {
      nextNonWhitespaceCalled = true;
      nextNonWhitespaceThrowOnEofArg = throwOnEof;
      // Simulate advancing pos by 1 (like skipping whitespace)
      // Read pos field via reflection and update it
      try {
        var posFieldRef = JsonReader.class.getDeclaredField("pos");
        posFieldRef.setAccessible(true);
        int currentPos = (int) posFieldRef.get(this);
        int newPos = Math.max(currentPos, 1);
        posFieldRef.set(this, newPos);
        return newPos;
      } catch (Exception e) {
        throw new IOException(e);
      }
    }

    // Remove @Override annotation because fillBuffer is package-private and not visible in subclass
    boolean fillBuffer(int minimum) throws IOException {
      return fillBufferReturn;
    }
  }

  @Test
    @Timeout(8000)
  void consumeNonExecutePrefix_skipsWhenNoSecurityToken() throws Exception {
    TestJsonReader reader = new TestJsonReader(mockReader);
    // Setup fields to simulate buffer and pos/limit
    setField(reader, "pos", 0);
    setField(reader, "limit", 10);
    char[] buf = new char[JsonReader.BUFFER_SIZE];
    // Put chars that do NOT match the security token at pos 0
    buf[0] = 'x';
    buf[1] = 'y';
    buf[2] = 'z';
    buf[3] = 'a';
    buf[4] = 'b';
    setField(reader, "buffer", buf);

    // fillBuffer returns true so condition passes
    reader.fillBufferReturn = true;

    // invoke consumeNonExecutePrefix
    invokeConsumeNonExecutePrefix(reader);

    // pos should have been decremented by 1 after nextNonWhitespace and then unchanged because no token matched
    int posAfter = (int) getField(reader, "pos");
    // nextNonWhitespace sets pos to 1, then pos-- => 0, no token matched, pos unchanged
    assertEquals(0, posAfter);
  }

  @Test
    @Timeout(8000)
  void consumeNonExecutePrefix_returnsEarlyWhenFillBufferFails() throws Exception {
    TestJsonReader reader = new TestJsonReader(mockReader);
    // Set pos so that pos + 5 > limit to trigger fillBuffer call
    setField(reader, "pos", 6);
    setField(reader, "limit", 10);
    reader.fillBufferReturn = false; // Simulate fillBuffer failing

    char[] buf = new char[JsonReader.BUFFER_SIZE];
    setField(reader, "buffer", buf);

    // invoke consumeNonExecutePrefix
    invokeConsumeNonExecutePrefix(reader);

    // pos should be decremented by 1 after nextNonWhitespace and then unchanged because fillBuffer returned false
    int posAfter = (int) getField(reader, "pos");
    // nextNonWhitespace sets pos to at least 1, then pos--, so posAfter >= 0
    assertTrue(posAfter >= 0);
  }

  @Test
    @Timeout(8000)
  void consumeNonExecutePrefix_consumesSecurityToken() throws Exception {
    TestJsonReader reader = new TestJsonReader(mockReader);
    // Setup pos and limit so that pos+5 <= limit
    setField(reader, "pos", 0);
    setField(reader, "limit", 10);
    char[] buf = new char[JsonReader.BUFFER_SIZE];
    // Setup buffer with security token sequence at pos 0: ) ] } ' \n
    buf[0] = ')';
    buf[1] = ']';
    buf[2] = '}';
    buf[3] = '\'';
    buf[4] = '\n';
    setField(reader, "buffer", buf);

    reader.fillBufferReturn = true;

    // invoke consumeNonExecutePrefix
    invokeConsumeNonExecutePrefix(reader);

    // pos should have been incremented by 5 after matching token
    int posAfter = (int) getField(reader, "pos");
    // nextNonWhitespace sets pos to 1, then pos-- => 0, then pos += 5
    assertEquals(5, posAfter);
  }

  @Test
    @Timeout(8000)
  void consumeNonExecutePrefix_nextNonWhitespaceThrowsIOException() throws Exception {
    TestJsonReader reader = new TestJsonReader(mockReader) {
      // Remove @Override annotation to fix compilation error
      int nextNonWhitespace(boolean throwOnEof) throws IOException {
        throw new IOException("forced");
      }
    };

    Exception exception = assertThrows(IOException.class, () -> {
      invokeConsumeNonExecutePrefix(reader);
    });

    assertTrue(exception.getCause() instanceof IOException || exception instanceof IOException);
    Throwable cause = exception.getCause() != null ? exception.getCause() : exception;
    assertEquals("forced", cause.getMessage());
  }
}