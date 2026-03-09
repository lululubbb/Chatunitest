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
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonReader_209_2Test {

  private JsonReader jsonReader;

  @BeforeEach
  public void setUp() {
    Reader mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  private void setField(Object target, String fieldName, Object value) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  private Object invokeDoPeek() throws Exception {
    Method doPeek = JsonReader.class.getDeclaredMethod("doPeek");
    doPeek.setAccessible(true);
    return doPeek.invoke(jsonReader);
  }

  private String invokeLocationString() throws Exception {
    Method locationString = JsonReader.class.getDeclaredMethod("locationString");
    locationString.setAccessible(true);
    return (String) locationString.invoke(jsonReader);
  }

  private String invokeNextUnquotedValue() throws Exception {
    Method nextUnquotedValue = JsonReader.class.getDeclaredMethod("nextUnquotedValue");
    nextUnquotedValue.setAccessible(true);
    return (String) nextUnquotedValue.invoke(jsonReader);
  }

  private String invokeNextQuotedValue(char quote) throws Exception {
    Method nextQuotedValue = JsonReader.class.getDeclaredMethod("nextQuotedValue", char.class);
    nextQuotedValue.setAccessible(true);
    return (String) nextQuotedValue.invoke(jsonReader, quote);
  }

  @Test
    @Timeout(8000)
  public void nextInt_peekedNone_callsDoPeekAndReturnsIntFromPeekedLong() throws Exception {
    // Setup peeked to PEEKED_NONE so doPeek() is called
    setField(jsonReader, "peeked", 0); // PEEKED_NONE = 0

    // Mock doPeek() to return PEEKED_LONG (15)
    JsonReader spyReader = spy(jsonReader);
    doReturn(15).when(spyReader).doPeek();

    // Set peekedLong to int value
    setField(spyReader, "peekedLong", 42L);
    setField(spyReader, "peeked", 0); // reset peeked to PEEKED_NONE before call
    setField(spyReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    setField(spyReader, "pathIndices", pathIndices);

    int result = spyReader.nextInt();
    assertEquals(42, result);
    assertEquals(1, pathIndices[0]);
    assertEquals(0, spyReader.peeked);
  }

  @Test
    @Timeout(8000)
  public void nextInt_peekedLongNotInt_throwsNumberFormatException() throws Exception {
    setField(jsonReader, "peeked", 15); // PEEKED_LONG
    setField(jsonReader, "peekedLong", (long) Integer.MAX_VALUE + 1);
    setField(jsonReader, "stackSize", 1);

    NumberFormatException e = assertThrows(NumberFormatException.class, () -> jsonReader.nextInt());
    assertTrue(e.getMessage().contains("Expected an int but was"));
  }

  @Test
    @Timeout(8000)
  public void nextInt_peekedNumber_parsesIntFromBuffer() throws Exception {
    setField(jsonReader, "peeked", 16); // PEEKED_NUMBER
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "peekedNumberLength", 2);
    char[] buffer = new char[1024];
    buffer[0] = '4';
    buffer[1] = '2';
    setField(jsonReader, "buffer", buffer);
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    setField(jsonReader, "pathIndices", pathIndices);

    int result = jsonReader.nextInt();
    assertEquals(42, result);
    assertEquals(2, (int) getField(jsonReader, "pos"));
    assertEquals(1, pathIndices[0]);
    assertEquals(0, jsonReader.peeked);
  }

  @Test
    @Timeout(8000)
  public void nextInt_peekedSingleQuoted_parsesIntFromNextQuotedValue() throws Exception {
    setField(jsonReader, "peeked", 8); // PEEKED_SINGLE_QUOTED
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    setField(jsonReader, "pathIndices", pathIndices);

    JsonReader spyReader = spy(jsonReader);
    doReturn("42").when(spyReader).nextQuotedValue('\'');

    int result = spyReader.nextInt();
    assertEquals(42, result);
    assertEquals(1, pathIndices[0]);
    assertEquals(0, spyReader.peeked);
  }

  @Test
    @Timeout(8000)
  public void nextInt_peekedDoubleQuoted_parsesIntFromNextQuotedValue() throws Exception {
    setField(jsonReader, "peeked", 9); // PEEKED_DOUBLE_QUOTED
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    setField(jsonReader, "pathIndices", pathIndices);

    JsonReader spyReader = spy(jsonReader);
    doReturn("42").when(spyReader).nextQuotedValue('"');

    int result = spyReader.nextInt();
    assertEquals(42, result);
    assertEquals(1, pathIndices[0]);
    assertEquals(0, spyReader.peeked);
  }

  @Test
    @Timeout(8000)
  public void nextInt_peekedUnquoted_parsesIntFromNextUnquotedValue() throws Exception {
    setField(jsonReader, "peeked", 10); // PEEKED_UNQUOTED
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    setField(jsonReader, "pathIndices", pathIndices);

    JsonReader spyReader = spy(jsonReader);
    doReturn("42").when(spyReader).nextUnquotedValue();

    int result = spyReader.nextInt();
    assertEquals(42, result);
    assertEquals(1, pathIndices[0]);
    assertEquals(0, spyReader.peeked);
  }

  @Test
    @Timeout(8000)
  public void nextInt_peekedUnquoted_nonIntFallsBackToDouble() throws Exception {
    setField(jsonReader, "peeked", 10); // PEEKED_UNQUOTED
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    setField(jsonReader, "pathIndices", pathIndices);

    JsonReader spyReader = spy(jsonReader);
    doReturn("42.0").when(spyReader).nextUnquotedValue();

    int result = spyReader.nextInt();
    assertEquals(42, result);
    assertEquals(1, pathIndices[0]);
    assertEquals(0, spyReader.peeked);
  }

  @Test
    @Timeout(8000)
  public void nextInt_peekedUnquoted_nonIntNonDouble_throwsNumberFormatException() throws Exception {
    setField(jsonReader, "peeked", 10); // PEEKED_UNQUOTED
    setField(jsonReader, "stackSize", 1);

    JsonReader spyReader = spy(jsonReader);
    doReturn("notANumber").when(spyReader).nextUnquotedValue();

    NumberFormatException e = assertThrows(NumberFormatException.class, spyReader::nextInt);
    assertTrue(e.getMessage().contains("notANumber"));
  }

  @Test
    @Timeout(8000)
  public void nextInt_peekedUnexpected_throwsIllegalStateException() throws Exception {
    setField(jsonReader, "peeked", 5); // PEEKED_TRUE (unexpected for int)
    setField(jsonReader, "stackSize", 1);

    IllegalStateException e = assertThrows(IllegalStateException.class, () -> jsonReader.nextInt());
    assertTrue(e.getMessage().startsWith("Expected an int but was"));
  }

  @Test
    @Timeout(8000)
  public void nextInt_doubleNotInt_throwsNumberFormatException() throws Exception {
    setField(jsonReader, "peeked", 16); // PEEKED_NUMBER
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "peekedNumberLength", 4);
    char[] buffer = new char[1024];
    buffer[0] = '4';
    buffer[1] = '2';
    buffer[2] = '.';
    buffer[3] = '5';
    setField(jsonReader, "buffer", buffer);
    setField(jsonReader, "stackSize", 1);

    NumberFormatException e = assertThrows(NumberFormatException.class, () -> jsonReader.nextInt());
    assertTrue(e.getMessage().contains("Expected an int but was"));
  }

  private Object getField(Object target, String fieldName) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(target);
  }
}