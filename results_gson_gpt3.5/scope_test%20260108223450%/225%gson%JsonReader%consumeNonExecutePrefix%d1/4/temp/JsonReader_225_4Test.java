package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import java.io.Closeable;
import java.io.EOFException;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonReader_225_4Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  public void setUp() {
    mockReader = new Reader() {
      @Override
      public int read(char[] cbuf, int off, int len) throws IOException {
        return -1; // EOF
      }
      @Override
      public void close() throws IOException {}
    };
    jsonReader = new JsonReader(mockReader);
  }

  @Test
    @Timeout(8000)
  public void consumeNonExecutePrefix_notEnoughBuffer_fillsBufferAndReturns() throws Exception {
    // Setup pos and limit so that pos + 5 > limit and fillBuffer returns false
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", 3);

    // Create a subclass to override nextNonWhitespace and fillBuffer via reflection
    JsonReader spyReader = new JsonReader(mockReader) {
      // Override nextNonWhitespace by reflection, since it's private and not visible here
      public int callNextNonWhitespace(boolean throwOnEof) throws IOException {
        try {
          Method m = JsonReader.class.getDeclaredMethod("nextNonWhitespace", boolean.class);
          m.setAccessible(true);
          return (int) m.invoke(this, throwOnEof);
        } catch (Exception e) {
          throw new IOException(e);
        }
      }
      @Override
      public int nextNonWhitespace(boolean throwOnEof) throws IOException {
        try {
          int pos = getField(this, "pos");
          setField(this, "pos", pos - 1);
          return pos - 1;
        } catch (Exception e) {
          throw new IOException(e);
        }
      }
      @Override
      public boolean fillBuffer(int minimum) throws IOException {
        return false;
      }
    };

    // pos will be decremented by 1 after nextNonWhitespace, so set pos to 1 initially
    setField(spyReader, "pos", 1);
    setField(spyReader, "limit", 3);

    invokeConsumeNonExecutePrefix(spyReader);

    // pos should remain 0 (1 - 1) because fillBuffer returned false and method returned early
    assertEquals(0, getField(spyReader, "pos"));
  }

  @Test
    @Timeout(8000)
  public void consumeNonExecutePrefix_bufferDoesNotMatchSecurityToken_returnsWithoutChangingPos() throws Exception {
    // Setup pos and limit so that pos + 5 <= limit and fillBuffer returns true
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", 10);

    // Setup buffer that does not match the security token
    char[] buffer = new char[1024];
    buffer[0] = 'x';
    buffer[1] = ']';
    buffer[2] = '}';
    buffer[3] = '\'';
    buffer[4] = '\n';
    setField(jsonReader, "buffer", buffer);

    JsonReader spyReader = new JsonReader(mockReader) {
      @Override
      public int nextNonWhitespace(boolean throwOnEof) throws IOException {
        try {
          int pos = getField(this, "pos");
          setField(this, "pos", pos - 1);
          return pos - 1;
        } catch (Exception e) {
          throw new IOException(e);
        }
      }
      @Override
      public boolean fillBuffer(int minimum) throws IOException {
        return true;
      }
    };

    setField(spyReader, "pos", 1);
    setField(spyReader, "limit", 10);
    setField(spyReader, "buffer", buffer);

    invokeConsumeNonExecutePrefix(spyReader);

    // pos should be decremented by 1 but not increased further
    assertEquals(0, getField(spyReader, "pos"));
  }

  @Test
    @Timeout(8000)
  public void consumeNonExecutePrefix_bufferMatchesSecurityToken_posIncreasedBy5() throws Exception {
    // Setup pos and limit so that pos + 5 <= limit and fillBuffer returns true
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", 10);

    // Setup buffer that matches the security token: )]}'\n
    char[] buffer = new char[1024];
    buffer[0] = ')';
    buffer[1] = ']';
    buffer[2] = '}';
    buffer[3] = '\'';
    buffer[4] = '\n';
    setField(jsonReader, "buffer", buffer);

    JsonReader spyReader = new JsonReader(mockReader) {
      @Override
      public int nextNonWhitespace(boolean throwOnEof) throws IOException {
        try {
          int pos = getField(this, "pos");
          setField(this, "pos", pos - 1);
          return pos - 1;
        } catch (Exception e) {
          throw new IOException(e);
        }
      }
      @Override
      public boolean fillBuffer(int minimum) throws IOException {
        return true;
      }
    };

    setField(spyReader, "pos", 1);
    setField(spyReader, "limit", 10);
    setField(spyReader, "buffer", buffer);

    invokeConsumeNonExecutePrefix(spyReader);

    // pos should be 5 after decrement and increment by 5 (1 - 1 + 5)
    assertEquals(5, getField(spyReader, "pos"));
  }

  // Helper to invoke private consumeNonExecutePrefix method via reflection
  private void invokeConsumeNonExecutePrefix(JsonReader instance) throws Exception {
    Method method = JsonReader.class.getDeclaredMethod("consumeNonExecutePrefix");
    method.setAccessible(true);
    method.invoke(instance);
  }

  // Helper to set private fields via reflection
  private void setField(Object instance, String fieldName, Object value) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(instance, value);
  }

  // Helper to get private int fields via reflection
  private int getField(JsonReader instance, String fieldName) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.getInt(instance);
  }
}