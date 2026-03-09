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

class JsonReader_209_3Test {

  private JsonReader jsonReader;

  @BeforeEach
  void setUp() throws Exception {
    Reader mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);

    // Initialize stackSize and pathIndices for coverage
    Field stackSizeField = JsonReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonReader, 1);

    Field pathIndicesField = JsonReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(jsonReader);
    pathIndices[0] = 0;
  }

  @Test
    @Timeout(8000)
  void nextInt_peekedLongExactInt_returnsInt() throws Exception {
    setField("peeked", 15); // PEEKED_LONG
    setField("peekedLong", 123);
    int result = jsonReader.nextInt();
    assertEquals(123, result);
    assertEquals(0, getFieldInt("peeked"));
    assertEquals(1, getPathIndicesAt(0));
  }

  @Test
    @Timeout(8000)
  void nextInt_peekedLongNotExactInt_throwsNumberFormatException() throws Exception {
    setField("peeked", 15); // PEEKED_LONG
    setField("peekedLong", 1234567890123L);
    NumberFormatException ex = assertThrows(NumberFormatException.class, () -> jsonReader.nextInt());
    assertTrue(ex.getMessage().contains("Expected an int but was"));
  }

  @Test
    @Timeout(8000)
  void nextInt_peekedNumber_parsesInt() throws Exception {
    setField("peeked", 16); // PEEKED_NUMBER
    setField("pos", 0);
    setField("peekedNumberLength", 3);
    setField("buffer", "123456".toCharArray());
    int result = jsonReader.nextInt();
    assertEquals(123, result);
    assertEquals(0, getFieldInt("peeked"));
    assertEquals(3, getFieldInt("pos"));
    assertEquals(1, getPathIndicesAt(0));
  }

  @Test
    @Timeout(8000)
  void nextInt_peekedSingleQuoted_validIntString() throws Exception {
    setField("peeked", 8); // PEEKED_SINGLE_QUOTED
    setPeekedString("456");
    int result = jsonReader.nextInt();
    assertEquals(456, result);
    assertEquals(0, getFieldInt("peeked"));
    assertEquals(1, getPathIndicesAt(0));
  }

  @Test
    @Timeout(8000)
  void nextInt_peekedDoubleQuoted_validIntString() throws Exception {
    setField("peeked", 9); // PEEKED_DOUBLE_QUOTED
    setPeekedString("789");
    int result = jsonReader.nextInt();
    assertEquals(789, result);
    assertEquals(0, getFieldInt("peeked"));
    assertEquals(1, getPathIndicesAt(0));
  }

  @Test
    @Timeout(8000)
  void nextInt_peekedUnquoted_validIntString() throws Exception {
    setField("peeked", 10); // PEEKED_UNQUOTED
    setPeekedString("321");
    int result = jsonReader.nextInt();
    assertEquals(321, result);
    assertEquals(0, getFieldInt("peeked"));
    assertEquals(1, getPathIndicesAt(0));
  }

  @Test
    @Timeout(8000)
  void nextInt_peekedUnquoted_invalidIntString_fallbackToDouble() throws Exception {
    setField("peeked", 10); // PEEKED_UNQUOTED
    setPeekedString("123.0");
    int result = jsonReader.nextInt();
    assertEquals(123, result);
    assertEquals(0, getFieldInt("peeked"));
    assertEquals(1, getPathIndicesAt(0));
  }

  @Test
    @Timeout(8000)
  void nextInt_peekedUnquoted_invalidDouble_throwsNumberFormatException() throws Exception {
    setField("peeked", 10); // PEEKED_UNQUOTED
    setPeekedString("123.4");
    NumberFormatException ex = assertThrows(NumberFormatException.class, () -> jsonReader.nextInt());
    assertTrue(ex.getMessage().contains("Expected an int but was"));
  }

  @Test
    @Timeout(8000)
  void nextInt_peekedSingleQuoted_invalidIntString_fallbackToDouble() throws Exception {
    setField("peeked", 8); // PEEKED_SINGLE_QUOTED
    setPeekedString("123.0");
    int result = jsonReader.nextInt();
    assertEquals(123, result);
    assertEquals(0, getFieldInt("peeked"));
    assertEquals(1, getPathIndicesAt(0));
  }

  @Test
    @Timeout(8000)
  void nextInt_peekedSingleQuoted_invalidDouble_throwsNumberFormatException() throws Exception {
    setField("peeked", 8); // PEEKED_SINGLE_QUOTED
    setPeekedString("123.5");
    NumberFormatException ex = assertThrows(NumberFormatException.class, () -> jsonReader.nextInt());
    assertTrue(ex.getMessage().contains("Expected an int but was"));
  }

  @Test
    @Timeout(8000)
  void nextInt_peekedDoubleQuoted_invalidIntString_fallbackToDouble() throws Exception {
    setField("peeked", 9); // PEEKED_DOUBLE_QUOTED
    setPeekedString("456.0");
    int result = jsonReader.nextInt();
    assertEquals(456, result);
    assertEquals(0, getFieldInt("peeked"));
    assertEquals(1, getPathIndicesAt(0));
  }

  @Test
    @Timeout(8000)
  void nextInt_peekedDoubleQuoted_invalidDouble_throwsNumberFormatException() throws Exception {
    setField("peeked", 9); // PEEKED_DOUBLE_QUOTED
    setPeekedString("456.7");
    NumberFormatException ex = assertThrows(NumberFormatException.class, () -> jsonReader.nextInt());
    assertTrue(ex.getMessage().contains("Expected an int but was"));
  }

  @Test
    @Timeout(8000)
  void nextInt_peekedNone_callsDoPeek() throws Exception {
    setField("peeked", 0); // PEEKED_NONE
    Method doPeekMethod = JsonReader.class.getDeclaredMethod("doPeek");
    doPeekMethod.setAccessible(true);
    int peekValue = (int) doPeekMethod.invoke(jsonReader);
    // To cover the branch, forcibly set peeked to PEEKED_NUMBER after doPeek
    setField("peeked", 16);
    setField("pos", 0);
    setField("peekedNumberLength", 1);
    setField("buffer", "1".toCharArray());
    int result = jsonReader.nextInt();
    assertEquals(1, result);
  }

  @Test
    @Timeout(8000)
  void nextInt_invalidPeek_throwsIllegalStateException() throws Exception {
    setField("peeked", 5); // PEEKED_TRUE (invalid for nextInt)
    IllegalStateException ex = assertThrows(IllegalStateException.class, () -> jsonReader.nextInt());
    assertTrue(ex.getMessage().contains("Expected an int but was"));
  }

  // Helper methods to access and set private fields

  private void setField(String fieldName, Object value) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    if (field.getType().isArray() && value instanceof String) {
      // Special handling for buffer char[]
      char[] chars = ((String) value).toCharArray();
      field.set(jsonReader, chars);
    } else {
      field.set(jsonReader, value);
    }
  }

  private int getFieldInt(String fieldName) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.getInt(jsonReader);
  }

  private int getPathIndicesAt(int index) throws Exception {
    Field pathIndicesField = JsonReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(jsonReader);
    return pathIndices[index];
  }

  private void setPeekedString(String value) throws Exception {
    // We need to mock nextQuotedValue and nextUnquotedValue calls to set peekedString
    // Instead, directly set peekedString and set peeked to PEEKED_SINGLE_QUOTED/DOUBLE_QUOTED/UNQUOTED
    setField("peekedString", value);
    // Also set peeked to PEEKED_SINGLE_QUOTED, PEEKED_DOUBLE_QUOTED or PEEKED_UNQUOTED accordingly
  }
}