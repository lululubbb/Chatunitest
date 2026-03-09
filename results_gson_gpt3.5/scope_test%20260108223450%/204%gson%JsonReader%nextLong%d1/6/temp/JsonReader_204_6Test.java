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

public class JsonReader_204_6Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  public void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
    setField(jsonReader, "stackSize", 1);
    setField(jsonReader, "pathIndices", new int[32]);
  }

  @Test
    @Timeout(8000)
  public void testNextLong_peekedLong_returnsPeekedLong() throws Exception {
    setField(jsonReader, "peeked", getPeekedLong());
    setField(jsonReader, "peekedLong", 123456789L);
    int[] pathIndices = new int[32];
    pathIndices[0] = 0;
    setField(jsonReader, "pathIndices", pathIndices);
    setField(jsonReader, "stackSize", 1);

    long result = jsonReader.nextLong();

    assertEquals(123456789L, result);
    assertEquals(1, pathIndices[0]);
    assertEquals(getPeekedNone(), getFieldInt(jsonReader, "peeked"));
  }

  @Test
    @Timeout(8000)
  public void testNextLong_peekedNumber_parsesLongFromBuffer() throws Exception {
    setField(jsonReader, "peeked", getPeekedNumber());
    char[] buffer = "1234567890extra".toCharArray();
    setField(jsonReader, "buffer", buffer);
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "peekedNumberLength", 10);
    int[] pathIndices = new int[32];
    setField(jsonReader, "pathIndices", pathIndices);
    setField(jsonReader, "stackSize", 1);

    long result = jsonReader.nextLong();

    assertEquals(1234567890L, result);
    assertEquals(10, getFieldInt(jsonReader, "pos"));
    assertEquals(1, pathIndices[0]);
    assertEquals(getPeekedNone(), getFieldInt(jsonReader, "peeked"));
  }

  @Test
    @Timeout(8000)
  public void testNextLong_peekedSingleQuoted_parsesLongFromQuotedValue() throws Exception {
    setField(jsonReader, "peeked", getPeekedSingleQuoted());
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    setField(jsonReader, "pathIndices", pathIndices);

    // Mock nextQuotedValue to return "42"
    Method nextQuotedValue = JsonReader.class.getDeclaredMethod("nextQuotedValue", char.class);
    nextQuotedValue.setAccessible(true);
    JsonReader spyReader = spy(jsonReader);
    doReturn("42").when(spyReader).nextQuotedValue('\'');

    long result = spyReader.nextLong();

    assertEquals(42L, result);
    assertEquals(1, pathIndices[0]);
    assertEquals(getPeekedNone(), getFieldInt(spyReader, "peeked"));
  }

  @Test
    @Timeout(8000)
  public void testNextLong_peekedDoubleQuoted_parsesLongFromQuotedValue() throws Exception {
    setField(jsonReader, "peeked", getPeekedDoubleQuoted());
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    setField(jsonReader, "pathIndices", pathIndices);

    // Mock nextQuotedValue to return "99"
    Method nextQuotedValue = JsonReader.class.getDeclaredMethod("nextQuotedValue", char.class);
    nextQuotedValue.setAccessible(true);
    JsonReader spyReader = spy(jsonReader);
    doReturn("99").when(spyReader).nextQuotedValue('"');

    long result = spyReader.nextLong();

    assertEquals(99L, result);
    assertEquals(1, pathIndices[0]);
    assertEquals(getPeekedNone(), getFieldInt(spyReader, "peeked"));
  }

  @Test
    @Timeout(8000)
  public void testNextLong_peekedUnquoted_parsesLongFromUnquotedValue() throws Exception {
    setField(jsonReader, "peeked", getPeekedUnquoted());
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    setField(jsonReader, "pathIndices", pathIndices);

    // Mock nextUnquotedValue to return "1234"
    JsonReader spyReader = spy(jsonReader);
    doReturn("1234").when(spyReader).nextUnquotedValue();

    long result = spyReader.nextLong();

    assertEquals(1234L, result);
    assertEquals(1, pathIndices[0]);
    assertEquals(getPeekedNone(), getFieldInt(spyReader, "peeked"));
  }

  @Test
    @Timeout(8000)
  public void testNextLong_peekedUnquoted_invalidLongFallsBackToDouble() throws Exception {
    setField(jsonReader, "peeked", getPeekedUnquoted());
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    setField(jsonReader, "pathIndices", pathIndices);

    JsonReader spyReader = spy(jsonReader);
    doReturn("1234.0").when(spyReader).nextUnquotedValue();

    long result = spyReader.nextLong();

    assertEquals(1234L, result);
    assertEquals(1, pathIndices[0]);
    assertEquals(getPeekedNone(), getFieldInt(spyReader, "peeked"));
  }

  @Test
    @Timeout(8000)
  public void testNextLong_peekedUnquoted_invalidLongAndDouble_throwsNumberFormatException() throws Exception {
    setField(jsonReader, "peeked", getPeekedUnquoted());
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    setField(jsonReader, "pathIndices", pathIndices);

    JsonReader spyReader = spy(jsonReader);
    doReturn("notANumber").when(spyReader).nextUnquotedValue();

    NumberFormatException ex = assertThrows(NumberFormatException.class, spyReader::nextLong);
    assertTrue(ex.getMessage().contains("Expected a long but was"));
  }

  @Test
    @Timeout(8000)
  public void testNextLong_peekedOther_throwsIllegalStateException() throws Exception {
    setField(jsonReader, "peeked", getPeekedBeginObject());

    IllegalStateException ex = assertThrows(IllegalStateException.class, () -> jsonReader.nextLong());
    assertTrue(ex.getMessage().startsWith("Expected a long but was"));
  }

  // Helpers to access private fields and constants

  private void setField(Object target, String fieldName, Object value) {
    try {
      Field field = JsonReader.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(target, value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private int getFieldInt(Object target, String fieldName) {
    try {
      Field field = JsonReader.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      return field.getInt(target);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private int getPeekedNone() {
    return getStaticFinalInt("PEEKED_NONE");
  }

  private int getPeekedLong() {
    return getStaticFinalInt("PEEKED_LONG");
  }

  private int getPeekedNumber() {
    return getStaticFinalInt("PEEKED_NUMBER");
  }

  private int getPeekedSingleQuoted() {
    return getStaticFinalInt("PEEKED_SINGLE_QUOTED");
  }

  private int getPeekedDoubleQuoted() {
    return getStaticFinalInt("PEEKED_DOUBLE_QUOTED");
  }

  private int getPeekedUnquoted() {
    return getStaticFinalInt("PEEKED_UNQUOTED");
  }

  private int getPeekedBeginObject() {
    return getStaticFinalInt("PEEKED_BEGIN_OBJECT");
  }

  private int getStaticFinalInt(String name) {
    try {
      Field f = JsonReader.class.getDeclaredField(name);
      f.setAccessible(true);
      return f.getInt(null);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}