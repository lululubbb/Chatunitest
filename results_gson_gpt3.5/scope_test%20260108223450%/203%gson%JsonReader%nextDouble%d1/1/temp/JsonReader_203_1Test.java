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

public class JsonReader_203_1Test {
  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  public void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  @Test
    @Timeout(8000)
  public void testNextDouble_peekedNone_peekedLong() throws Exception {
    setField(jsonReader, "peeked", 0); // PEEKED_NONE
    setField(jsonReader, "peekedLong", 123L);
    setField(jsonReader, "stackSize", 1);
    setField(jsonReader, "pathIndices", new int[32]);
    setField(jsonReader, "peeked", 0); // reset peeked

    // Mock doPeek to return PEEKED_LONG (15)
    Method doPeek = JsonReader.class.getDeclaredMethod("doPeek");
    doPeek.setAccessible(true);
    JsonReader spyReader = spy(jsonReader);
    doReturn(15).when(spyReader).doPeek();

    setField(spyReader, "peekedLong", 123L);
    setField(spyReader, "stackSize", 1);
    setField(spyReader, "pathIndices", new int[32]);

    double result = spyReader.nextDouble();

    assertEquals(123.0, result);
    assertEquals(0, getField(spyReader, "peeked"));
    assertEquals(1, ((int[]) getField(spyReader, "pathIndices"))[0]);
  }

  @Test
    @Timeout(8000)
  public void testNextDouble_peekedNumber_validDouble() throws Exception {
    setField(jsonReader, "peeked", 16); // PEEKED_NUMBER
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "peekedNumberLength", 4);
    setField(jsonReader, "buffer", "1234".toCharArray());
    setField(jsonReader, "lenient", false);
    setField(jsonReader, "stackSize", 1);
    setField(jsonReader, "pathIndices", new int[32]);

    double result = jsonReader.nextDouble();

    assertEquals(1234.0, result);
    assertEquals(0, getField(jsonReader, "peeked"));
    assertEquals(4, getField(jsonReader, "pos"));
    assertEquals(1, ((int[]) getField(jsonReader, "pathIndices"))[0]);
    assertNull(getField(jsonReader, "peekedString"));
  }

  @Test
    @Timeout(8000)
  public void testNextDouble_peekedSingleQuoted_validDouble() throws Exception {
    setField(jsonReader, "peeked", 8); // PEEKED_SINGLE_QUOTED
    setField(jsonReader, "lenient", false);
    setField(jsonReader, "stackSize", 1);
    setField(jsonReader, "pathIndices", new int[32]);

    // Spy to override nextQuotedValue to return "12.34"
    JsonReader spyReader = spy(jsonReader);
    doReturn("12.34").when(spyReader).nextQuotedValue('\'');

    double result = spyReader.nextDouble();

    assertEquals(12.34, result);
    assertEquals(0, getField(spyReader, "peeked"));
    assertEquals(1, ((int[]) getField(spyReader, "pathIndices"))[0]);
    assertNull(getField(spyReader, "peekedString"));
  }

  @Test
    @Timeout(8000)
  public void testNextDouble_peekedDoubleQuoted_validDouble() throws Exception {
    setField(jsonReader, "peeked", 9); // PEEKED_DOUBLE_QUOTED
    setField(jsonReader, "lenient", false);
    setField(jsonReader, "stackSize", 1);
    setField(jsonReader, "pathIndices", new int[32]);

    JsonReader spyReader = spy(jsonReader);
    doReturn("56.78").when(spyReader).nextQuotedValue('"');

    double result = spyReader.nextDouble();

    assertEquals(56.78, result);
    assertEquals(0, getField(spyReader, "peeked"));
    assertEquals(1, ((int[]) getField(spyReader, "pathIndices"))[0]);
    assertNull(getField(spyReader, "peekedString"));
  }

  @Test
    @Timeout(8000)
  public void testNextDouble_peekedUnquoted_validDouble() throws Exception {
    setField(jsonReader, "peeked", 10); // PEEKED_UNQUOTED
    setField(jsonReader, "lenient", false);
    setField(jsonReader, "stackSize", 1);
    setField(jsonReader, "pathIndices", new int[32]);

    JsonReader spyReader = spy(jsonReader);
    doReturn("78.9").when(spyReader).nextUnquotedValue();

    double result = spyReader.nextDouble();

    assertEquals(78.9, result);
    assertEquals(0, getField(spyReader, "peeked"));
    assertEquals(1, ((int[]) getField(spyReader, "pathIndices"))[0]);
    assertNull(getField(spyReader, "peekedString"));
  }

  @Test
    @Timeout(8000)
  public void testNextDouble_peekedInvalid_throwsIllegalStateException() throws Exception {
    setField(jsonReader, "peeked", 5); // PEEKED_TRUE (not a double)
    setField(jsonReader, "stackSize", 1);
    setField(jsonReader, "pathIndices", new int[32]);

    IllegalStateException ex = assertThrows(IllegalStateException.class, () -> jsonReader.nextDouble());
    assertTrue(ex.getMessage().contains("Expected a double but was"));
  }

  @Test
    @Timeout(8000)
  public void testNextDouble_NaNOrInfiniteLenientFalse_throwsMalformedJsonException() throws Exception {
    setField(jsonReader, "peeked", 10); // PEEKED_UNQUOTED
    setField(jsonReader, "lenient", false);
    setField(jsonReader, "stackSize", 1);
    setField(jsonReader, "pathIndices", new int[32]);

    JsonReader spyReader = spy(jsonReader);
    doReturn("NaN").when(spyReader).nextUnquotedValue();

    MalformedJsonException ex = assertThrows(MalformedJsonException.class, () -> spyReader.nextDouble());
    assertTrue(ex.getMessage().contains("NaN and infinities"));
  }

  @Test
    @Timeout(8000)
  public void testNextDouble_NaNOrInfiniteLenientTrue_returnsValue() throws Exception {
    setField(jsonReader, "peeked", 10); // PEEKED_UNQUOTED
    setField(jsonReader, "lenient", true);
    setField(jsonReader, "stackSize", 1);
    setField(jsonReader, "pathIndices", new int[32]);

    JsonReader spyReader = spy(jsonReader);
    doReturn("NaN").when(spyReader).nextUnquotedValue();

    double result = spyReader.nextDouble();

    assertTrue(Double.isNaN(result));
    assertEquals(0, getField(spyReader, "peeked"));
    assertEquals(1, ((int[]) getField(spyReader, "pathIndices"))[0]);
  }

  @Test
    @Timeout(8000)
  public void testNextDouble_invalidNumberFormat_throwsNumberFormatException() throws Exception {
    setField(jsonReader, "peeked", 10); // PEEKED_UNQUOTED
    setField(jsonReader, "lenient", true);
    setField(jsonReader, "stackSize", 1);
    setField(jsonReader, "pathIndices", new int[32]);

    JsonReader spyReader = spy(jsonReader);
    doReturn("invalid").when(spyReader).nextUnquotedValue();

    assertThrows(NumberFormatException.class, () -> spyReader.nextDouble());
  }

  private void setField(Object target, String fieldName, Object value) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  private Object getField(Object target, String fieldName) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(target);
  }
}