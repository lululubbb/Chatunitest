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

public class JsonReader_209_4Test {

  private JsonReader jsonReader;

  @BeforeEach
  public void setUp() {
    Reader mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  private void setField(String fieldName, Object value) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(jsonReader, value);
  }

  private Object getField(String fieldName) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(jsonReader);
  }

  private int invokeDoPeek() throws Exception {
    Method doPeek = JsonReader.class.getDeclaredMethod("doPeek");
    doPeek.setAccessible(true);
    return (int) doPeek.invoke(jsonReader);
  }

  private String invokeLocationString() throws Exception {
    Method locationString = JsonReader.class.getDeclaredMethod("locationString");
    locationString.setAccessible(true);
    return (String) locationString.invoke(jsonReader);
  }

  private String invokeNextQuotedValue(char quote) throws Exception {
    Method nextQuotedValue = JsonReader.class.getDeclaredMethod("nextQuotedValue", char.class);
    nextQuotedValue.setAccessible(true);
    return (String) nextQuotedValue.invoke(jsonReader, quote);
  }

  private String invokeNextUnquotedValue() throws Exception {
    Method nextUnquotedValue = JsonReader.class.getDeclaredMethod("nextUnquotedValue");
    nextUnquotedValue.setAccessible(true);
    return (String) nextUnquotedValue.invoke(jsonReader);
  }

  @Test
    @Timeout(8000)
  public void testNextInt_peekedLongExactInt() throws Exception {
    setField("peeked", 15); // PEEKED_LONG
    setField("peekedLong", 123);
    setField("stackSize", 1);
    setField("pathIndices", new int[32]);
    int result = jsonReader.nextInt();
    assertEquals(123, result);
    assertEquals(0, (int) getField("peeked"));
    int[] pathIndices = (int[]) getField("pathIndices");
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void testNextInt_peekedLongNotExactInt_throws() throws Exception {
    setField("peeked", 15); // PEEKED_LONG
    setField("peekedLong", (long) Integer.MAX_VALUE + 1);
    setField("stackSize", 1);
    setField("pathIndices", new int[32]);
    NumberFormatException thrown = assertThrows(NumberFormatException.class, () -> jsonReader.nextInt());
    assertTrue(thrown.getMessage().contains("Expected an int but was"));
  }

  @Test
    @Timeout(8000)
  public void testNextInt_peekedNumber_parsesInt() throws Exception {
    setField("peeked", 16); // PEEKED_NUMBER
    char[] buffer = "12345".toCharArray();
    setField("buffer", buffer);
    setField("pos", 0);
    setField("peekedNumberLength", 5);
    setField("stackSize", 1);
    setField("pathIndices", new int[32]);
    int result = jsonReader.nextInt();
    assertEquals(12345, result);
    assertEquals(5, (int) getField("pos"));
    assertEquals(0, (int) getField("peeked"));
    int[] pathIndices = (int[]) getField("pathIndices");
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void testNextInt_peekedSingleQuoted_parsesInt() throws Exception {
    setField("peeked", 8); // PEEKED_SINGLE_QUOTED
    setField("stackSize", 1);
    setField("pathIndices", new int[32]);
    JsonReader spyReader = spy(jsonReader);
    doReturn("42").when(spyReader).nextQuotedValue('\'');
    setField("peekedString", null);

    // Replace jsonReader with spyReader for this test
    Field inField = JsonReader.class.getDeclaredField("in");
    inField.setAccessible(true);
    inField.set(spyReader, getField("in"));

    int result = spyReader.nextInt();
    assertEquals(42, result);
    assertEquals(0, (int) getField("peeked"));
    int[] pathIndices = (int[]) getField("pathIndices");
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void testNextInt_peekedDoubleQuoted_parsesInt() throws Exception {
    setField("peeked", 9); // PEEKED_DOUBLE_QUOTED
    setField("stackSize", 1);
    setField("pathIndices", new int[32]);
    JsonReader spyReader = spy(jsonReader);
    doReturn("123").when(spyReader).nextQuotedValue('"');
    setField("peekedString", null);

    Field inField = JsonReader.class.getDeclaredField("in");
    inField.setAccessible(true);
    inField.set(spyReader, getField("in"));

    int result = spyReader.nextInt();
    assertEquals(123, result);
    assertEquals(0, (int) getField("peeked"));
    int[] pathIndices = (int[]) getField("pathIndices");
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void testNextInt_peekedUnquoted_parsesInt() throws Exception {
    setField("peeked", 10); // PEEKED_UNQUOTED
    setField("stackSize", 1);
    setField("pathIndices", new int[32]);
    JsonReader spyReader = spy(jsonReader);
    doReturn("77").when(spyReader).nextUnquotedValue();
    setField("peekedString", null);

    Field inField = JsonReader.class.getDeclaredField("in");
    inField.setAccessible(true);
    inField.set(spyReader, getField("in"));

    int result = spyReader.nextInt();
    assertEquals(77, result);
    assertEquals(0, (int) getField("peeked"));
    int[] pathIndices = (int[]) getField("pathIndices");
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void testNextInt_peekedUnquoted_parseIntFailsFallsBackToDouble() throws Exception {
    setField("peeked", 10); // PEEKED_UNQUOTED
    setField("stackSize", 1);
    setField("pathIndices", new int[32]);
    JsonReader spyReader = spy(jsonReader);
    doReturn("123.0").when(spyReader).nextUnquotedValue();
    setField("peekedString", null);

    Field inField = JsonReader.class.getDeclaredField("in");
    inField.setAccessible(true);
    inField.set(spyReader, getField("in"));

    int result = spyReader.nextInt();
    assertEquals(123, result);
    assertEquals(0, (int) getField("peeked"));
    int[] pathIndices = (int[]) getField("pathIndices");
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void testNextInt_peekedUnquoted_parseIntFailsFallsBackToDouble_nonInt_throws() throws Exception {
    setField("peeked", 10); // PEEKED_UNQUOTED
    setField("stackSize", 1);
    setField("pathIndices", new int[32]);
    JsonReader spyReader = spy(jsonReader);
    doReturn("123.45").when(spyReader).nextUnquotedValue();
    setField("peekedString", null);

    Field inField = JsonReader.class.getDeclaredField("in");
    inField.setAccessible(true);
    inField.set(spyReader, getField("in"));

    NumberFormatException thrown = assertThrows(NumberFormatException.class, () -> spyReader.nextInt());
    assertTrue(thrown.getMessage().contains("Expected an int but was"));
  }

  @Test
    @Timeout(8000)
  public void testNextInt_peekedNone_callsDoPeek() throws Exception {
    setField("peeked", 0); // PEEKED_NONE
    setField("stackSize", 1);
    setField("pathIndices", new int[32]);
    JsonReader spyReader = spy(jsonReader);
    doReturn(15).when(spyReader).doPeek();
    setField("peekedLong", 5L);
    doReturn(5L).when(spyReader).getClass().getDeclaredField("peekedLong").get(spyReader);

    // Setup peekedLong to be int-compatible
    setField("peekedLong", 5L);

    // Also set peeked to PEEKED_LONG after doPeek
    doAnswer(invocation -> {
      setField("peeked", 15);
      return 15;
    }).when(spyReader).doPeek();

    Field inField = JsonReader.class.getDeclaredField("in");
    inField.setAccessible(true);
    inField.set(spyReader, getField("in"));

    int result = spyReader.nextInt();
    assertEquals(5, result);
  }

  @Test
    @Timeout(8000)
  public void testNextInt_invalidPeek_throwsIllegalStateException() throws Exception {
    setField("peeked", 5); // PEEKED_TRUE (not valid for int)
    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> jsonReader.nextInt());
    assertTrue(thrown.getMessage().startsWith("Expected an int but was"));
  }
}