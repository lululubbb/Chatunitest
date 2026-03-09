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

public class JsonReader_206_3Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  private Method nextUnquotedValueMethod;

  private Field posField;
  private Field limitField;
  private Field bufferField;
  private Method checkLenientMethod;

  @BeforeEach
  public void setUp() throws NoSuchMethodException, NoSuchFieldException {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
    // Access private method nextUnquotedValue via reflection
    nextUnquotedValueMethod = JsonReader.class.getDeclaredMethod("nextUnquotedValue");
    nextUnquotedValueMethod.setAccessible(true);

    // Access private fields via reflection
    posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);

    limitField = JsonReader.class.getDeclaredField("limit");
    limitField.setAccessible(true);

    bufferField = JsonReader.class.getDeclaredField("buffer");
    bufferField.setAccessible(true);

    // Access private method checkLenient via reflection
    checkLenientMethod = JsonReader.class.getDeclaredMethod("checkLenient");
    checkLenientMethod.setAccessible(true);
  }

  private int getPos(JsonReader reader) throws IllegalAccessException {
    return (int) posField.get(reader);
  }

  private void setPos(JsonReader reader, int value) throws IllegalAccessException {
    posField.set(reader, value);
  }

  private int getLimit(JsonReader reader) throws IllegalAccessException {
    return (int) limitField.get(reader);
  }

  private void setLimit(JsonReader reader, int value) throws IllegalAccessException {
    limitField.set(reader, value);
  }

  private char[] getBuffer(JsonReader reader) throws IllegalAccessException {
    return (char[]) bufferField.get(reader);
  }

  private void setBuffer(JsonReader reader, char[] buffer) throws IllegalAccessException {
    bufferField.set(reader, buffer);
  }

  private void setBufferContent(JsonReader reader, char[] content, int offset, int length) throws IllegalAccessException {
    char[] buffer = getBuffer(reader);
    System.arraycopy(content, offset, buffer, 0, length);
    setPos(reader, 0);
    setLimit(reader, length);
  }

  private String invokeNextUnquotedValue(JsonReader reader) throws Throwable {
    try {
      return (String) nextUnquotedValueMethod.invoke(reader);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }

  private void invokeCheckLenient(JsonReader reader) throws Throwable {
    try {
      checkLenientMethod.invoke(reader);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }

  @Test
    @Timeout(8000)
  public void testNextUnquotedValue_simpleLiteral() throws Throwable {
    // Setup buffer with simple literal ending with space
    String literal = "simpleLiteral ";
    setBufferContent(jsonReader, literal.toCharArray(), 0, literal.length());

    String result = invokeNextUnquotedValue(jsonReader);

    assertEquals("simpleLiteral", result);
    assertEquals(literal.length(), getPos(jsonReader));
  }

  @Test
    @Timeout(8000)
  public void testNextUnquotedValue_literalEndsWithDelimiter() throws Throwable {
    // Setup buffer with literal ending with comma ','
    String literal = "value,";
    setBufferContent(jsonReader, literal.toCharArray(), 0, literal.length());

    String result = invokeNextUnquotedValue(jsonReader);

    assertEquals("value", result);
    assertEquals(literal.length() - 1, getPos(jsonReader));
  }

  @Test
    @Timeout(8000)
  public void testNextUnquotedValue_withLenientCheckLenientCalled() throws Throwable {
    // Setup buffer with literal containing a slash '/' which triggers checkLenient
    String literal = "value/next";
    setBufferContent(jsonReader, literal.toCharArray(), 0, literal.length());
    jsonReader.setLenient(true);

    // Spy jsonReader to verify checkLenient call
    JsonReader spyReader = spy(jsonReader);

    // We need to override checkLenient method to call the real private method via reflection
    doAnswer(invocation -> {
      invokeCheckLenient(spyReader);
      return null;
    }).when(spyReader).checkLenient();

    String result;
    try {
      result = (String) nextUnquotedValueMethod.invoke(spyReader);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }

    assertEquals("value", result);
    verify(spyReader, atLeastOnce()).checkLenient();
  }

  @Test
    @Timeout(8000)
  public void testNextUnquotedValue_longLiteralUsesStringBuilder() throws Throwable {
    // Create a literal longer than buffer length to force StringBuilder usage
    char[] buffer = getBuffer(jsonReader);
    int length = buffer.length + 10;
    char[] longLiteral = new char[length + 1];
    for (int i = 0; i < length; i++) {
      longLiteral[i] = 'a';
    }
    longLiteral[length] = ' '; // delimiter to end literal

    // Fill first buffer with partial literal
    setBufferContent(jsonReader, longLiteral, 0, buffer.length);

    // Spy jsonReader to override fillBuffer
    JsonReader spyReader = spy(jsonReader);

    doAnswer(invocation -> {
      int minimum = invocation.getArgument(0);
      int currentLimit = getLimit(spyReader);
      int toCopy = Math.min(minimum, longLiteral.length - currentLimit);
      if (toCopy <= 0) {
        return false;
      }
      char[] spyBuffer = getBuffer(spyReader);
      System.arraycopy(longLiteral, currentLimit, spyBuffer, 0, toCopy);
      setPos(spyReader, 0);
      setLimit(spyReader, toCopy);
      return true;
    }).when(spyReader).fillBuffer(anyInt());

    String result;
    try {
      result = (String) nextUnquotedValueMethod.invoke(spyReader);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }

    assertEquals(new String(longLiteral, 0, length), result);
    assertEquals(length + 1, getPos(spyReader));
  }

  @Test
    @Timeout(8000)
  public void testNextUnquotedValue_fillsBufferReturnsFalseEnds() throws Throwable {
    // Setup buffer with partial literal
    char[] partial = "partial".toCharArray();
    setBufferContent(jsonReader, partial, 0, partial.length);

    // Spy to simulate fillBuffer returns false when called
    JsonReader spyReader = spy(jsonReader);
    doReturn(false).when(spyReader).fillBuffer(anyInt());

    String result;
    try {
      result = (String) nextUnquotedValueMethod.invoke(spyReader);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }

    assertEquals("partial", result);
    assertEquals(partial.length, getPos(spyReader));
  }
}