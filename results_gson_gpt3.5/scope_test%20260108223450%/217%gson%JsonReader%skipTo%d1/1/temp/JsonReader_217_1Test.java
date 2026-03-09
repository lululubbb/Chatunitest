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
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonReader_217_1Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  public void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  @Test
    @Timeout(8000)
  public void testSkipTo_foundAtStart() throws Throwable {
    setBuffer("hello world");
    setPos(0);
    setLimit(11);
    setLineNumber(0);
    setLineStart(0);

    boolean result = invokeSkipTo("hello");
    assertTrue(result);
    assertEquals(0, getPos());
  }

  @Test
    @Timeout(8000)
  public void testSkipTo_foundInMiddle() throws Throwable {
    setBuffer("abc def ghi");
    setPos(4);
    setLimit(11);
    setLineNumber(0);
    setLineStart(0);

    boolean result = invokeSkipTo("def");
    assertTrue(result);
    assertEquals(4, getPos());
  }

  @Test
    @Timeout(8000)
  public void testSkipTo_notFound() throws Throwable {
    setBuffer("abc def ghi");
    setPos(0);
    setLimit(11);
    setLineNumber(0);
    setLineStart(0);

    boolean result = invokeSkipTo("xyz");
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  public void testSkipTo_withNewlines() throws Throwable {
    setBuffer("abc\nxyz");
    setPos(0);
    setLimit(7);
    setLineNumber(0);
    setLineStart(0);

    boolean result = invokeSkipTo("xyz");
    assertTrue(result);
    assertEquals(4, getPos());
    assertEquals(1, getLineNumber());
    assertEquals(4, getLineStart());
  }

  @Test
    @Timeout(8000)
  public void testSkipTo_fillBufferReturnsTrue() throws Throwable {
    // buffer initially empty, pos=0, limit=0
    setBuffer("");
    setPos(0);
    setLimit(0);
    setLineNumber(0);
    setLineStart(0);

    // Spy on jsonReader to mock fillBuffer via reflection
    JsonReader spyReader = spy(jsonReader);

    // Use reflection to get the private fillBuffer method
    Method fillBufferMethod = JsonReader.class.getDeclaredMethod("fillBuffer", int.class);
    fillBufferMethod.setAccessible(true);

    // Use doAnswer with reflection to mock fillBuffer calls
    doAnswer(invocation -> {
      setBuffer("abc def");
      setLimit(7);
      return true;
    }).doReturn(false).when(spyReader).fillBufferProxy(anyInt());

    boolean result = invokeSkipTo(spyReader, "def");
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  public void testSkipTo_fillBufferReturnsFalse() throws Throwable {
    setBuffer("abc");
    setPos(0);
    setLimit(3);
    setLineNumber(0);
    setLineStart(0);

    JsonReader spyReader = spy(jsonReader);

    doReturn(false).when(spyReader).fillBufferProxy(anyInt());

    boolean result = invokeSkipTo(spyReader, "def");
    assertFalse(result);
  }

  // Helper methods to access private fields and methods

  private void setBuffer(String content) {
    char[] buf = new char[JsonReader.BUFFER_SIZE];
    int len = Math.min(content.length(), buf.length);
    content.getChars(0, len, buf, 0);
    setField("buffer", buf);
  }

  private void setPos(int pos) {
    setField("pos", pos);
  }

  private int getPos() {
    return (int) getField("pos");
  }

  private void setLimit(int limit) {
    setField("limit", limit);
  }

  private void setLineNumber(int lineNumber) {
    setField("lineNumber", lineNumber);
  }

  private int getLineNumber() {
    return (int) getField("lineNumber");
  }

  private void setLineStart(int lineStart) {
    setField("lineStart", lineStart);
  }

  private int getLineStart() {
    return (int) getField("lineStart");
  }

  private Object getField(String name) {
    try {
      Field field = JsonReader.class.getDeclaredField(name);
      field.setAccessible(true);
      return field.get(jsonReader);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void setField(String name, Object value) {
    try {
      Field field = JsonReader.class.getDeclaredField(name);
      field.setAccessible(true);
      field.set(jsonReader, value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private boolean invokeSkipTo(String toFind) throws Throwable {
    return invokeSkipTo(jsonReader, toFind);
  }

  private boolean invokeSkipTo(JsonReader instance, String toFind) throws Throwable {
    // For tests with spyReader, we use a proxy method to call skipTo
    if (instance.getClass() != JsonReader.class) {
      // The instance is a spy; we need to call the private skipTo method via reflection on the underlying instance
      Method method = JsonReader.class.getDeclaredMethod("skipTo", String.class);
      method.setAccessible(true);
      try {
        return (boolean) method.invoke(instance, toFind);
      } catch (InvocationTargetException e) {
        throw e.getCause();
      }
    } else {
      Method method = JsonReader.class.getDeclaredMethod("skipTo", String.class);
      method.setAccessible(true);
      try {
        return (boolean) method.invoke(instance, toFind);
      } catch (InvocationTargetException e) {
        throw e.getCause();
      }
    }
  }

  // Add a proxy method to enable mocking fillBuffer since it's private
  private boolean fillBufferProxy(int minimum) throws IOException {
    try {
      Method fillBufferMethod = JsonReader.class.getDeclaredMethod("fillBuffer", int.class);
      fillBufferMethod.setAccessible(true);
      return (boolean) fillBufferMethod.invoke(jsonReader, minimum);
    } catch (InvocationTargetException e) {
      Throwable cause = e.getCause();
      if (cause instanceof IOException) {
        throw (IOException) cause;
      }
      throw new RuntimeException(cause);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  // We need to add this proxy method to the spy instance dynamically
  // To do that, use a dynamic proxy or Mockito's withSettings().defaultAnswer()
  // But since Java does not allow adding methods dynamically, we can create a subclass with this method and spy on it.

  // So, we create a subclass of JsonReader for testing with the proxy method

  private static class JsonReaderWithProxy extends JsonReader {
    public JsonReaderWithProxy(Reader in) {
      super(in);
    }

    public boolean fillBufferProxy(int minimum) throws IOException {
      try {
        Method fillBufferMethod = JsonReader.class.getDeclaredMethod("fillBuffer", int.class);
        fillBufferMethod.setAccessible(true);
        return (boolean) fillBufferMethod.invoke(this, minimum);
      } catch (InvocationTargetException e) {
        Throwable cause = e.getCause();
        if (cause instanceof IOException) {
          throw (IOException) cause;
        }
        throw new RuntimeException(cause);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }

  // Override setUp to initialize jsonReader as JsonReaderWithProxy for tests that need fillBufferProxy

  @BeforeEach
  public void setUpWithProxy() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReaderWithProxy(mockReader);
  }

  // Override invokeSkipTo for JsonReaderWithProxy

  private boolean invokeSkipTo(JsonReaderWithProxy instance, String toFind) throws Throwable {
    Method method = JsonReader.class.getDeclaredMethod("skipTo", String.class);
    method.setAccessible(true);
    try {
      return (boolean) method.invoke(instance, toFind);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }

  // Adjust the tests that require fillBufferProxy to use JsonReaderWithProxy and spy on it

  @Test
    @Timeout(8000)
  public void testSkipTo_fillBufferReturnsTrue_withProxy() throws Throwable {
    setBuffer("");// buffer initially empty, pos=0, limit=0
    setPos(0);
    setLimit(0);
    setLineNumber(0);
    setLineStart(0);

    JsonReaderWithProxy readerWithProxy = new JsonReaderWithProxy(mockReader);
    JsonReaderWithProxy spyReader = spy(readerWithProxy);

    doAnswer(invocation -> {
      setBuffer("abc def");
      setLimit(7);
      return true;
    }).doReturn(false).when(spyReader).fillBufferProxy(anyInt());

    boolean result = invokeSkipTo(spyReader, "def");
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  public void testSkipTo_fillBufferReturnsFalse_withProxy() throws Throwable {
    setBuffer("abc");
    setPos(0);
    setLimit(3);
    setLineNumber(0);
    setLineStart(0);

    JsonReaderWithProxy readerWithProxy = new JsonReaderWithProxy(mockReader);
    JsonReaderWithProxy spyReader = spy(readerWithProxy);

    doReturn(false).when(spyReader).fillBufferProxy(anyInt());

    boolean result = invokeSkipTo(spyReader, "def");
    assertFalse(result);
  }
}