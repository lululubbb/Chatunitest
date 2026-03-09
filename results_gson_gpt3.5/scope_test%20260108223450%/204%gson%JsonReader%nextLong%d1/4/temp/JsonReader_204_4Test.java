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

class JsonReader_204_4Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
    setField(jsonReader, "stackSize", 1);
    setField(jsonReader, "pathIndices", new int[32]);
  }

  @Test
    @Timeout(8000)
  void testNextLong_peekedLong() throws Exception {
    setField(jsonReader, "peeked", getFieldValue(jsonReader, "PEEKED_LONG"));
    setField(jsonReader, "peekedLong", 123456789L);
    int[] pathIndices = (int[]) getField(jsonReader, "pathIndices").get(jsonReader);
    int stackSize = (int) getField(jsonReader, "stackSize").get(jsonReader);
    pathIndices[stackSize - 1] = 0;
    long result = jsonReader.nextLong();
    assertEquals(123456789L, result);
    assertEquals(1, pathIndices[stackSize - 1]);
    assertEquals(getFieldValue(jsonReader, "PEEKED_NONE"), getField(jsonReader, "peeked").get(jsonReader));
  }

  @Test
    @Timeout(8000)
  void testNextLong_peekedNumber() throws Exception {
    setField(jsonReader, "peeked", getFieldValue(jsonReader, "PEEKED_NUMBER"));
    char[] buffer = new char[1024];
    String number = "9876543210";
    System.arraycopy(number.toCharArray(), 0, buffer, 0, number.length());
    setField(jsonReader, "buffer", buffer);
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "peekedNumberLength", number.length());
    setField(jsonReader, "stackSize", 1);
    setField(jsonReader, "pathIndices", new int[32]);
    long result = jsonReader.nextLong();
    assertEquals(9876543210L, result);
    assertEquals(number.length(), getField(jsonReader, "pos").getInt(jsonReader));
  }

  @Test
    @Timeout(8000)
  void testNextLong_peekedSingleQuoted_validLong() throws Exception {
    testNextLong_quotedValue_validLong(getFieldValue(jsonReader, "PEEKED_SINGLE_QUOTED"), '\'', "12345", 12345L);
  }

  @Test
    @Timeout(8000)
  void testNextLong_peekedDoubleQuoted_validLong() throws Exception {
    testNextLong_quotedValue_validLong(getFieldValue(jsonReader, "PEEKED_DOUBLE_QUOTED"), '"', "67890", 67890L);
  }

  @Test
    @Timeout(8000)
  void testNextLong_peekedUnquoted_validLong() throws Exception {
    setField(jsonReader, "peeked", getFieldValue(jsonReader, "PEEKED_UNQUOTED"));
    setField(jsonReader, "stackSize", 1);
    setField(jsonReader, "pathIndices", new int[32]);

    // Mock nextUnquotedValue to return a valid long string
    Method nextUnquotedValueMethod = JsonReader.class.getDeclaredMethod("nextUnquotedValue");
    nextUnquotedValueMethod.setAccessible(true);
    JsonReader spyReader = spy(jsonReader);
    doReturn("55555").when(spyReader).nextUnquotedValue();

    long result = spyReader.nextLong();
    assertEquals(55555L, result);
    int[] pathIndices = (int[]) getField(spyReader, "pathIndices").get(spyReader);
    int stackSize = (int) getField(spyReader, "stackSize").get(spyReader);
    assertEquals(1, pathIndices[stackSize - 1]);
    assertEquals(getFieldValue(spyReader, "PEEKED_NONE"), getField(spyReader, "peeked").get(spyReader));
  }

  @Test
    @Timeout(8000)
  void testNextLong_peekedSingleQuoted_invalidLong_fallbackDouble() throws Exception {
    testNextLong_quotedValue_invalidLong(getFieldValue(jsonReader, "PEEKED_SINGLE_QUOTED"), '\'');
  }

  @Test
    @Timeout(8000)
  void testNextLong_peekedDoubleQuoted_invalidLong_fallbackDouble() throws Exception {
    testNextLong_quotedValue_invalidLong(getFieldValue(jsonReader, "PEEKED_DOUBLE_QUOTED"), '"');
  }

  @Test
    @Timeout(8000)
  void testNextLong_peekedUnquoted_invalidLong_fallbackDouble() throws Exception {
    setField(jsonReader, "peeked", getFieldValue(jsonReader, "PEEKED_UNQUOTED"));
    setField(jsonReader, "stackSize", 1);
    setField(jsonReader, "pathIndices", new int[32]);

    JsonReader spyReader = spy(jsonReader);
    doReturn("123.0").when(spyReader).nextUnquotedValue();

    long result = spyReader.nextLong();
    assertEquals(123L, result);
  }

  @Test
    @Timeout(8000)
  void testNextLong_peekedOther_throwsIllegalStateException() throws Exception {
    setField(jsonReader, "peeked", getFieldValue(jsonReader, "PEEKED_TRUE")); // some invalid peeked state
    Exception exception = assertThrows(IllegalStateException.class, () -> jsonReader.nextLong());
    assertTrue(exception.getMessage().contains("Expected a long but was"));
  }

  @Test
    @Timeout(8000)
  void testNextLong_fallbackDouble_precisionLoss_throwsNumberFormatException() throws Exception {
    setField(jsonReader, "peeked", getFieldValue(jsonReader, "PEEKED_NUMBER"));
    char[] buffer = new char[1024];
    String number = "123.456"; // double with fractional part
    System.arraycopy(number.toCharArray(), 0, buffer, 0, number.length());
    setField(jsonReader, "buffer", buffer);
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "peekedNumberLength", number.length());
    setField(jsonReader, "stackSize", 1);
    setField(jsonReader, "pathIndices", new int[32]);

    NumberFormatException exception = assertThrows(NumberFormatException.class, () -> jsonReader.nextLong());
    assertTrue(exception.getMessage().contains("Expected a long but was"));
  }

  // Helper methods

  private void testNextLong_quotedValue_validLong(int peekedValue, char quote, String quotedString, long expected) throws Exception {
    setField(jsonReader, "peeked", peekedValue);
    setField(jsonReader, "stackSize", 1);
    setField(jsonReader, "pathIndices", new int[32]);

    JsonReader spyReader = spy(jsonReader);
    doReturn(quotedString).when(spyReader).nextQuotedValue(quote);

    long result = spyReader.nextLong();
    assertEquals(expected, result);
    int[] pathIndices = (int[]) getField(spyReader, "pathIndices").get(spyReader);
    int stackSize = (int) getField(spyReader, "stackSize").get(spyReader);
    assertEquals(1, pathIndices[stackSize - 1]);
    assertEquals(getFieldValue(spyReader, "PEEKED_NONE"), getField(spyReader, "peeked").get(spyReader));
  }

  private void testNextLong_quotedValue_invalidLong(int peekedValue, char quote) throws Exception {
    setField(jsonReader, "peeked", peekedValue);
    setField(jsonReader, "stackSize", 1);
    setField(jsonReader, "pathIndices", new int[32]);

    JsonReader spyReader = spy(jsonReader);
    doReturn("123.0").when(spyReader).nextQuotedValue(quote);

    long result = spyReader.nextLong();
    assertEquals(123L, result);
  }

  private Object getFieldValue(Object instance, String fieldName) {
    try {
      Field field = instance.getClass().getDeclaredField(fieldName);
      field.setAccessible(true);
      return field.get(instance);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private Field getField(Object instance, String fieldName) {
    try {
      Field field = instance.getClass().getDeclaredField(fieldName);
      field.setAccessible(true);
      return field;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void setField(Object instance, String fieldName, Object value) {
    try {
      Field field = getField(instance, fieldName);
      if (field.getType().isPrimitive()) {
        if (field.getType() == int.class) {
          field.setInt(instance, (Integer) value);
        } else if (field.getType() == long.class) {
          field.setLong(instance, (Long) value);
        } else if (field.getType() == boolean.class) {
          field.setBoolean(instance, (Boolean) value);
        } else {
          field.set(instance, value);
        }
      } else {
        field.set(instance, value);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}