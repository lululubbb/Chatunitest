package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import java.io.Closeable;
import java.io.EOFException;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonReader_216_1Test {

  private JsonReader jsonReader;

  @BeforeEach
  void setUp() {
    jsonReader = new JsonReader(mockReader());
  }

  @Test
    @Timeout(8000)
  void skipToEndOfLine_posLessThanLimit_newline() throws Exception {
    // Setup buffer with a line containing '\n' at pos 1
    setField(jsonReader, "buffer", new char[] {'a', '\n', 'b'});
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", 3);
    setField(jsonReader, "lineNumber", 0);
    setField(jsonReader, "lineStart", 0);

    invokeSkipToEndOfLine(jsonReader);

    assertEquals(2, (int) getField(jsonReader, "pos"));
    assertEquals(1, (int) getField(jsonReader, "lineNumber"));
    assertEquals(2, (int) getField(jsonReader, "lineStart"));
  }

  @Test
    @Timeout(8000)
  void skipToEndOfLine_posLessThanLimit_carriageReturn() throws Exception {
    // Setup buffer with a line containing '\r' at pos 1
    setField(jsonReader, "buffer", new char[] {'a', '\r', 'b'});
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", 3);
    setField(jsonReader, "lineNumber", 0);
    setField(jsonReader, "lineStart", 0);

    invokeSkipToEndOfLine(jsonReader);

    assertEquals(2, (int) getField(jsonReader, "pos"));
    assertEquals(0, (int) getField(jsonReader, "lineNumber"));
    assertEquals(0, (int) getField(jsonReader, "lineStart"));
  }

  @Test
    @Timeout(8000)
  void skipToEndOfLine_posEqualsLimit_fillBufferTrue_newline() throws Exception {
    // Setup buffer empty and pos == limit, fillBuffer(1) returns true and buffer set with '\n'
    setField(jsonReader, "buffer", new char[1024]);
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", 0);
    setField(jsonReader, "lineNumber", 0);
    setField(jsonReader, "lineStart", 0);

    JsonReader spyReader = createSpyReader(jsonReader, '\n');

    invokeSkipToEndOfLine(spyReader);

    assertEquals(1, (int) getField(spyReader, "pos"));
    assertEquals(1, (int) getField(spyReader, "lineNumber"));
    assertEquals(1, (int) getField(spyReader, "lineStart"));
  }

  @Test
    @Timeout(8000)
  void skipToEndOfLine_posEqualsLimit_fillBufferTrue_carriageReturn() throws Exception {
    // Setup buffer empty and pos == limit, fillBuffer(1) returns true and buffer set with '\r'
    setField(jsonReader, "buffer", new char[1024]);
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", 0);
    setField(jsonReader, "lineNumber", 0);
    setField(jsonReader, "lineStart", 0);

    JsonReader spyReader = createSpyReader(jsonReader, '\r');

    invokeSkipToEndOfLine(spyReader);

    assertEquals(1, (int) getField(spyReader, "pos"));
    assertEquals(0, (int) getField(spyReader, "lineNumber"));
    assertEquals(0, (int) getField(spyReader, "lineStart"));
  }

  @Test
    @Timeout(8000)
  void skipToEndOfLine_posEqualsLimit_fillBufferFalse() throws Exception {
    // Setup buffer empty and pos == limit, fillBuffer(1) returns false (EOF)
    setField(jsonReader, "buffer", new char[1024]);
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", 0);
    setField(jsonReader, "lineNumber", 0);
    setField(jsonReader, "lineStart", 0);

    JsonReader spyReader = new JsonReader(mockReader()) {
      @Override
      protected boolean fillBuffer(int minimum) {
        return false;
      }
    };
    copyState(jsonReader, spyReader);

    invokeSkipToEndOfLine(spyReader);

    // pos stays at 0, no line number or line start changes
    assertEquals(0, (int) getField(spyReader, "pos"));
    assertEquals(0, (int) getField(spyReader, "lineNumber"));
    assertEquals(0, (int) getField(spyReader, "lineStart"));
  }

  private JsonReader createSpyReader(JsonReader original, char fillChar) throws Exception {
    return new JsonReader(mockReader()) {
      @Override
      protected boolean fillBuffer(int minimum) throws IOException {
        try {
          char[] buf = getField(this, "buffer");
          buf[0] = fillChar;
          setField(this, "limit", 1);
          return true;
        } catch (Exception e) {
          throw new IOException(e);
        }
      }
    };
  }

  private void invokeSkipToEndOfLine(JsonReader instance) throws Exception {
    Method method = JsonReader.class.getDeclaredMethod("skipToEndOfLine");
    method.setAccessible(true);
    method.invoke(instance);
  }

  private void setField(Object instance, String fieldName, Object value) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(instance, value);
  }

  @SuppressWarnings("unchecked")
  private <T> T getField(Object instance, String fieldName) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    return (T) field.get(instance);
  }

  private void copyState(JsonReader from, JsonReader to) throws Exception {
    setField(to, "buffer", getField(from, "buffer"));
    setField(to, "pos", getField(from, "pos"));
    setField(to, "limit", getField(from, "limit"));
    setField(to, "lineNumber", getField(from, "lineNumber"));
    setField(to, "lineStart", getField(from, "lineStart"));
  }

  private Reader mockReader() {
    return new Reader() {
      @Override
      public int read(char[] cbuf, int off, int len) {
        return -1; // EOF
      }

      @Override
      public void close() {}
    };
  }
}