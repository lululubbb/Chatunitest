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

public class JsonReader_204_5Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  public void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
    // initialize stack size and pathIndices to avoid ArrayIndexOutOfBoundsException
    try {
      Field stackSizeField = JsonReader.class.getDeclaredField("stackSize");
      stackSizeField.setAccessible(true);
      stackSizeField.setInt(jsonReader, 1);

      Field pathIndicesField = JsonReader.class.getDeclaredField("pathIndices");
      pathIndicesField.setAccessible(true);
      int[] pathIndices = (int[]) pathIndicesField.get(jsonReader);
      pathIndices[0] = 0;
    } catch (Exception e) {
      fail("Reflection setup failed: " + e.getMessage());
    }
  }

  @Test
    @Timeout(8000)
  public void testNextLong_PeekedLong() throws Exception {
    setField("peeked", 15); // PEEKED_LONG
    setField("peekedLong", 123456789L);
    int[] pathIndices = getPathIndices();
    int stackSize = getStackSize();

    long result = jsonReader.nextLong();

    assertEquals(123456789L, result);
    assertEquals(0, getFieldInt("peeked"));
    assertEquals(1, pathIndices[stackSize - 1]);
  }

  @Test
    @Timeout(8000)
  public void testNextLong_PeekedNumber() throws Exception {
    setField("peeked", 16); // PEEKED_NUMBER
    char[] buffer = getFieldBuffer();
    String numberStr = "9876543210";
    System.arraycopy(numberStr.toCharArray(), 0, buffer, 0, numberStr.length());
    setField("pos", 0);
    setField("peekedNumberLength", numberStr.length());
    int[] pathIndices = getPathIndices();
    int stackSize = getStackSize();

    long result = jsonReader.nextLong();

    assertEquals(9876543210L, result);
    assertEquals(numberStr.length(), getFieldInt("pos"));
    assertEquals(0, getFieldInt("peeked"));
    assertNull(getFieldString("peekedString"));
    assertEquals(1, pathIndices[stackSize - 1]);
  }

  @Test
    @Timeout(8000)
  public void testNextLong_PeekedSingleQuoted_ValidLong() throws Exception {
    setField("peeked", 8); // PEEKED_SINGLE_QUOTED
    setField("stackSize", 1);
    setField("pathIndices", new int[]{0});

    // Mock nextQuotedValue to return a valid long string
    JsonReader spyReader = spy(jsonReader);
    doReturn("123456789").when(spyReader).nextQuotedValue('\'');

    long result = spyReader.nextLong();

    assertEquals(123456789L, result);
    assertEquals(0, getFieldInt(spyReader, "peeked"));
    int[] pathIndices = getPathIndices(spyReader);
    assertEquals(1, pathIndices[getStackSize(spyReader) - 1]);
  }

  @Test
    @Timeout(8000)
  public void testNextLong_PeekedDoubleQuoted_ValidLong() throws Exception {
    setField("peeked", 9); // PEEKED_DOUBLE_QUOTED
    setField("stackSize", 1);
    setField("pathIndices", new int[]{0});

    JsonReader spyReader = spy(jsonReader);
    doReturn("987654321").when(spyReader).nextQuotedValue('"');

    long result = spyReader.nextLong();

    assertEquals(987654321L, result);
    assertEquals(0, getFieldInt(spyReader, "peeked"));
    int[] pathIndices = getPathIndices(spyReader);
    assertEquals(1, pathIndices[getStackSize(spyReader) - 1]);
  }

  @Test
    @Timeout(8000)
  public void testNextLong_PeekedUnquoted_ValidLong() throws Exception {
    setField("peeked", 10); // PEEKED_UNQUOTED
    setField("stackSize", 1);
    setField("pathIndices", new int[]{0});

    JsonReader spyReader = spy(jsonReader);
    doReturn("12345").when(spyReader).nextUnquotedValue();

    long result = spyReader.nextLong();

    assertEquals(12345L, result);
    assertEquals(0, getFieldInt(spyReader, "peeked"));
    int[] pathIndices = getPathIndices(spyReader);
    assertEquals(1, pathIndices[getStackSize(spyReader) - 1]);
  }

  @Test
    @Timeout(8000)
  public void testNextLong_PeekedSingleQuoted_InvalidLongFallsBackToDouble() throws Exception {
    setField("peeked", 8); // PEEKED_SINGLE_QUOTED
    setField("stackSize", 1);
    setField("pathIndices", new int[]{0});

    JsonReader spyReader = spy(jsonReader);
    doReturn("123.0").when(spyReader).nextQuotedValue('\'');

    long result = spyReader.nextLong();

    assertEquals(123L, result);
    assertEquals(0, getFieldInt(spyReader, "peeked"));
    int[] pathIndices = getPathIndices(spyReader);
    assertEquals(1, pathIndices[getStackSize(spyReader) - 1]);
  }

  @Test
    @Timeout(8000)
  public void testNextLong_PeekedSingleQuoted_InvalidLongDoublePrecisionLoss() throws Exception {
    setField("peeked", 8); // PEEKED_SINGLE_QUOTED
    setField("stackSize", 1);
    setField("pathIndices", new int[]{0});

    JsonReader spyReader = spy(jsonReader);
    doReturn("123.456").when(spyReader).nextQuotedValue('\'');

    NumberFormatException thrown = assertThrows(NumberFormatException.class, spyReader::nextLong);
    assertTrue(thrown.getMessage().contains("Expected a long but was 123.456"));
  }

  @Test
    @Timeout(8000)
  public void testNextLong_IllegalStateException() throws Exception {
    setField("peeked", 5); // PEEKED_TRUE (not a number)
    setField("stackSize", 1);
    setField("pathIndices", new int[]{0});

    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> jsonReader.nextLong());
    assertTrue(thrown.getMessage().startsWith("Expected a long but was "));
  }

  @Test
    @Timeout(8000)
  public void testNextLong_PeekedNoneCallsDoPeek() throws Exception {
    setField("peeked", 0); // PEEKED_NONE
    JsonReader spyReader = spy(jsonReader);

    doReturn(15).when(spyReader).doPeek();
    setField(spyReader, "peekedLong", 42L);
    setField(spyReader, "stackSize", 1);
    setField(spyReader, "pathIndices", new int[]{0});

    long result = spyReader.nextLong();

    assertEquals(42L, result);
    verify(spyReader).doPeek();
  }

  // Helper methods for reflection access

  private void setField(String name, Object value) {
    try {
      Field field = JsonReader.class.getDeclaredField(name);
      field.setAccessible(true);
      field.set(jsonReader, value);
    } catch (Exception e) {
      fail("Failed to set field " + name + ": " + e.getMessage());
    }
  }

  private void setField(JsonReader instance, String name, Object value) {
    try {
      Field field = JsonReader.class.getDeclaredField(name);
      field.setAccessible(true);
      field.set(instance, value);
    } catch (Exception e) {
      fail("Failed to set field " + name + ": " + e.getMessage());
    }
  }

  private int getFieldInt(String name) {
    try {
      Field field = JsonReader.class.getDeclaredField(name);
      field.setAccessible(true);
      return field.getInt(jsonReader);
    } catch (Exception e) {
      fail("Failed to get int field " + name + ": " + e.getMessage());
      return -1;
    }
  }

  private int getFieldInt(JsonReader instance, String name) {
    try {
      Field field = JsonReader.class.getDeclaredField(name);
      field.setAccessible(true);
      return field.getInt(instance);
    } catch (Exception e) {
      fail("Failed to get int field " + name + ": " + e.getMessage());
      return -1;
    }
  }

  private String getFieldString(String name) {
    try {
      Field field = JsonReader.class.getDeclaredField(name);
      field.setAccessible(true);
      return (String) field.get(jsonReader);
    } catch (Exception e) {
      fail("Failed to get String field " + name + ": " + e.getMessage());
      return null;
    }
  }

  private char[] getFieldBuffer() {
    try {
      Field field = JsonReader.class.getDeclaredField("buffer");
      field.setAccessible(true);
      return (char[]) field.get(jsonReader);
    } catch (Exception e) {
      fail("Failed to get buffer field: " + e.getMessage());
      return null;
    }
  }

  private int[] getPathIndices() {
    try {
      Field field = JsonReader.class.getDeclaredField("pathIndices");
      field.setAccessible(true);
      return (int[]) field.get(jsonReader);
    } catch (Exception e) {
      fail("Failed to get pathIndices field: " + e.getMessage());
      return null;
    }
  }

  private int[] getPathIndices(JsonReader instance) {
    try {
      Field field = JsonReader.class.getDeclaredField("pathIndices");
      field.setAccessible(true);
      return (int[]) field.get(instance);
    } catch (Exception e) {
      fail("Failed to get pathIndices field: " + e.getMessage());
      return null;
    }
  }

  private int getStackSize() {
    try {
      Field field = JsonReader.class.getDeclaredField("stackSize");
      field.setAccessible(true);
      return field.getInt(jsonReader);
    } catch (Exception e) {
      fail("Failed to get stackSize field: " + e.getMessage());
      return -1;
    }
  }

  private int getStackSize(JsonReader instance) {
    try {
      Field field = JsonReader.class.getDeclaredField("stackSize");
      field.setAccessible(true);
      return field.getInt(instance);
    } catch (Exception e) {
      fail("Failed to get stackSize field: " + e.getMessage());
      return -1;
    }
  }
}