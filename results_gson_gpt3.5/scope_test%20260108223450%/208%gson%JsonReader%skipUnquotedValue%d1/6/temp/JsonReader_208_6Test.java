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

import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class JsonReader_208_6Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  public void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  private void setField(Object target, String fieldName, Object value) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  private Object invokeSkipUnquotedValue(JsonReader reader) throws Exception {
    Method method = JsonReader.class.getDeclaredMethod("skipUnquotedValue");
    method.setAccessible(true);
    return method.invoke(reader);
  }

  private void setBufferAndPosLimit(char[] bufferContent, int pos, int limit) throws Exception {
    setField(jsonReader, "buffer", bufferContent);
    setField(jsonReader, "pos", pos);
    setField(jsonReader, "limit", limit);
  }

  private void setLenient(boolean lenient) throws Exception {
    jsonReader.setLenient(lenient);
  }

  private void setFillBufferMock(boolean returnValue) throws Exception {
    // We spy on jsonReader to mock fillBuffer
    JsonReader spyReader = Mockito.spy(jsonReader);
    doReturn(returnValue).when(spyReader).fillBuffer(1);
    jsonReader = spyReader;
  }

  @Test
    @Timeout(8000)
  public void testSkipUnquotedValue_stopsOnSpecialChar() throws Exception {
    // buffer with unquoted value followed by a comma
    char[] buf = new char[1024];
    String val = "unquotedValue,rest";
    System.arraycopy(val.toCharArray(), 0, buf, 0, val.length());
    setBufferAndPosLimit(buf, 0, val.length());
    setLenient(true);

    // call method
    invokeSkipUnquotedValue(jsonReader);

    // pos should be at the index of comma (13)
    Field posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    int pos = (int) posField.get(jsonReader);
    assertEquals(13, pos);
  }

  @Test
    @Timeout(8000)
  public void testSkipUnquotedValue_callsCheckLenientOnSpecialChars() throws Exception {
    // buffer with a slash '/' at pos 0
    char[] buf = new char[1024];
    buf[0] = '/';
    setBufferAndPosLimit(buf, 0, 1);
    setLenient(true);

    // Spy on jsonReader to verify checkLenient call
    JsonReader spyReader = Mockito.spy(jsonReader);
    setField(spyReader, "buffer", buf);
    setField(spyReader, "pos", 0);
    setField(spyReader, "limit", 1);
    jsonReader = spyReader;

    invokeSkipUnquotedValue(jsonReader);

    verify(spyReader, times(1)).checkLenient();

    Field posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    int pos = (int) posField.get(jsonReader);
    assertEquals(0, pos);
  }

  @Test
    @Timeout(8000)
  public void testSkipUnquotedValue_advancesPosWhenNoSpecialCharAndFillBufferTrue() throws Exception {
    // buffer with all non-special characters (letters)
    char[] buf = new char[1024];
    String val = "abcdefghijklmnopqrstuvwxyz";
    System.arraycopy(val.toCharArray(), 0, buf, 0, val.length());
    setBufferAndPosLimit(buf, 0, val.length());
    setLenient(true);

    // Spy to mock fillBuffer to return true once, then false
    JsonReader spyReader = Mockito.spy(jsonReader);
    setField(spyReader, "buffer", buf);
    setField(spyReader, "pos", 0);
    setField(spyReader, "limit", val.length());
    doReturn(true).doReturn(false).when(spyReader).fillBuffer(1);
    jsonReader = spyReader;

    invokeSkipUnquotedValue(jsonReader);

    Field posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    int pos = (int) posField.get(jsonReader);
    // pos should be advanced to the limit (26)
    assertEquals(26, pos);
  }

  @Test
    @Timeout(8000)
  public void testSkipUnquotedValue_advancesPosWhenNoSpecialCharAndFillBufferFalse() throws Exception {
    // buffer with all non-special characters (letters)
    char[] buf = new char[1024];
    String val = "abc";
    System.arraycopy(val.toCharArray(), 0, buf, 0, val.length());
    setBufferAndPosLimit(buf, 0, val.length());
    setLenient(true);

    // Spy to mock fillBuffer to return false immediately
    JsonReader spyReader = Mockito.spy(jsonReader);
    setField(spyReader, "buffer", buf);
    setField(spyReader, "pos", 0);
    setField(spyReader, "limit", val.length());
    doReturn(false).when(spyReader).fillBuffer(1);
    jsonReader = spyReader;

    invokeSkipUnquotedValue(jsonReader);

    Field posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    int pos = (int) posField.get(jsonReader);
    // pos should be advanced to the limit (3)
    assertEquals(3, pos);
  }

  @Test
    @Timeout(8000)
  public void testSkipUnquotedValue_checkLenientThrowsWhenLenientFalse() throws Exception {
    // buffer with a slash '/' at pos 0
    char[] buf = new char[1024];
    buf[0] = '/';
    setBufferAndPosLimit(buf, 0, 1);
    setLenient(false);

    // Spy on jsonReader to verify checkLenient call throws
    JsonReader spyReader = Mockito.spy(jsonReader);
    setField(spyReader, "buffer", buf);
    setField(spyReader, "pos", 0);
    setField(spyReader, "limit", 1);
    jsonReader = spyReader;

    // Make checkLenient throw IOException when lenient is false
    doCallRealMethod().when(spyReader).checkLenient();
    doAnswer(invocation -> {
      Field lenientField = JsonReader.class.getDeclaredField("lenient");
      lenientField.setAccessible(true);
      boolean lenient = (boolean) lenientField.get(spyReader);
      if (!lenient) {
        throw new IOException("Use JsonReader.setLenient(true) to accept malformed JSON at line 1 column 1 path $");
      }
      return null;
    }).when(spyReader).checkLenient();

    IOException thrown = assertThrows(IOException.class, () -> invokeSkipUnquotedValue(spyReader));
    assertTrue(thrown.getMessage().contains("Use JsonReader.setLenient(true)"));
  }
}