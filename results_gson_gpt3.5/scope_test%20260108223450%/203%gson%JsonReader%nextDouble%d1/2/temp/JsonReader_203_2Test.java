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
import org.mockito.Mockito;

public class JsonReader_203_2Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  public void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
    // Initialize stackSize and pathIndices for tests
    try {
      Field stackSizeField = JsonReader.class.getDeclaredField("stackSize");
      stackSizeField.setAccessible(true);
      stackSizeField.setInt(jsonReader, 1);

      Field pathIndicesField = JsonReader.class.getDeclaredField("pathIndices");
      pathIndicesField.setAccessible(true);
      int[] pathIndices = (int[]) pathIndicesField.get(jsonReader);
      pathIndices[0] = 0;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Test
    @Timeout(8000)
  public void testNextDouble_PeekedLong() throws Exception {
    setPeeked(JsonReader.class.getDeclaredField("PEEKED_LONG"), 15);
    setPeekedLong(123L);
    setStackSize(1);
    setPathIndices(0, 0);
    double result = jsonReader.nextDouble();
    assertEquals(123.0, result);
    assertEquals(1, getPathIndices(0));
    assertEquals(0, getPeeked());
  }

  @Test
    @Timeout(8000)
  public void testNextDouble_PeekedNumber_ValidDouble() throws Exception {
    setPeeked(JsonReader.class.getDeclaredField("PEEKED_NUMBER"), 16);
    String numberStr = "123.456";
    setPeekedNumberLength(numberStr.length());
    setBuffer(numberStr);
    setPos(0);
    setStackSize(1);
    setPathIndices(0, 0);
    setLenient(false);

    double result = jsonReader.nextDouble();
    assertEquals(123.456, result);
    assertEquals(1, getPathIndices(0));
    assertEquals(0, getPeeked());
    assertNull(getPeekedString());
  }

  @Test
    @Timeout(8000)
  public void testNextDouble_PeekedNumber_NaN_NotLenient() throws Exception {
    setPeeked(JsonReader.class.getDeclaredField("PEEKED_NUMBER"), 16);
    String numberStr = "NaN";
    setPeekedNumberLength(numberStr.length());
    setBuffer(numberStr);
    setPos(0);
    setStackSize(1);
    setPathIndices(0, 0);
    setLenient(false);

    IOException ex = assertThrows(IOException.class, () -> jsonReader.nextDouble());
    assertTrue(ex.getMessage().contains("NaN"));
  }

  @Test
    @Timeout(8000)
  public void testNextDouble_PeekedNumber_NaN_Lenient() throws Exception {
    setPeeked(JsonReader.class.getDeclaredField("PEEKED_NUMBER"), 16);
    String numberStr = "NaN";
    setPeekedNumberLength(numberStr.length());
    setBuffer(numberStr);
    setPos(0);
    setStackSize(1);
    setPathIndices(0, 0);
    setLenient(true);

    double result = jsonReader.nextDouble();
    assertTrue(Double.isNaN(result));
    assertEquals(1, getPathIndices(0));
    assertEquals(0, getPeeked());
    assertNull(getPeekedString());
  }

  @Test
    @Timeout(8000)
  public void testNextDouble_PeekedSingleQuoted() throws Exception {
    setPeeked(JsonReader.class.getDeclaredField("PEEKED_SINGLE_QUOTED"), 8);
    setStackSize(1);
    setPathIndices(0, 0);
    setLenient(false);

    // Mock nextQuotedValue to return a valid double string
    Method nextQuotedValueMethod = JsonReader.class.getDeclaredMethod("nextQuotedValue", char.class);
    nextQuotedValueMethod.setAccessible(true);
    JsonReader spyReader = Mockito.spy(jsonReader);
    doReturn("42.42").when(spyReader).nextQuotedValue('\'');

    setPeekedField(spyReader, 8);
    double result = spyReader.nextDouble();
    assertEquals(42.42, result);
    assertEquals(1, getPathIndices(0, spyReader));
    assertEquals(0, getPeeked(spyReader));
    assertNull(getPeekedString(spyReader));
  }

  @Test
    @Timeout(8000)
  public void testNextDouble_PeekedDoubleQuoted() throws Exception {
    setPeeked(JsonReader.class.getDeclaredField("PEEKED_DOUBLE_QUOTED"), 9);
    setStackSize(1);
    setPathIndices(0, 0);
    setLenient(false);

    // Mock nextQuotedValue to return a valid double string
    Method nextQuotedValueMethod = JsonReader.class.getDeclaredMethod("nextQuotedValue", char.class);
    nextQuotedValueMethod.setAccessible(true);
    JsonReader spyReader = Mockito.spy(jsonReader);
    doReturn("3.14").when(spyReader).nextQuotedValue('"');

    setPeekedField(spyReader, 9);
    double result = spyReader.nextDouble();
    assertEquals(3.14, result);
    assertEquals(1, getPathIndices(0, spyReader));
    assertEquals(0, getPeeked(spyReader));
    assertNull(getPeekedString(spyReader));
  }

  @Test
    @Timeout(8000)
  public void testNextDouble_PeekedUnquoted() throws Exception {
    setPeeked(JsonReader.class.getDeclaredField("PEEKED_UNQUOTED"), 10);
    setStackSize(1);
    setPathIndices(0, 0);
    setLenient(false);

    // Mock nextUnquotedValue to return a valid double string
    Method nextUnquotedValueMethod = JsonReader.class.getDeclaredMethod("nextUnquotedValue");
    nextUnquotedValueMethod.setAccessible(true);
    JsonReader spyReader = Mockito.spy(jsonReader);
    doReturn("2.71828").when(spyReader).nextUnquotedValue();

    setPeekedField(spyReader, 10);
    double result = spyReader.nextDouble();
    assertEquals(2.71828, result);
    assertEquals(1, getPathIndices(0, spyReader));
    assertEquals(0, getPeeked(spyReader));
    assertNull(getPeekedString(spyReader));
  }

  @Test
    @Timeout(8000)
  public void testNextDouble_ThrowsIllegalStateException() throws Exception {
    setPeeked(JsonReader.class.getDeclaredField("PEEKED_TRUE"), 5); // use a peeked value that is invalid for double
    setStackSize(1);
    setPathIndices(0, 0);
    setLenient(false);

    IllegalStateException ex = assertThrows(IllegalStateException.class, () -> jsonReader.nextDouble());
    assertTrue(ex.getMessage().startsWith("Expected a double but was"));
  }

  // Helper methods to set private fields

  private void setPeeked(Field constantField, int value) throws Exception {
    Field peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    peekedField.setInt(jsonReader, value);
  }

  private void setPeekedField(JsonReader reader, int value) throws Exception {
    Field peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    peekedField.setInt(reader, value);
  }

  private int getPeeked() throws Exception {
    Field peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    return peekedField.getInt(jsonReader);
  }

  private int getPeeked(JsonReader reader) throws Exception {
    Field peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    return peekedField.getInt(reader);
  }

  private void setPeekedLong(long value) throws Exception {
    Field peekedLongField = JsonReader.class.getDeclaredField("peekedLong");
    peekedLongField.setAccessible(true);
    peekedLongField.setLong(jsonReader, value);
  }

  private void setPeekedNumberLength(int length) throws Exception {
    Field peekedNumberLengthField = JsonReader.class.getDeclaredField("peekedNumberLength");
    peekedNumberLengthField.setAccessible(true);
    peekedNumberLengthField.setInt(jsonReader, length);
  }

  private void setBuffer(String s) throws Exception {
    Field bufferField = JsonReader.class.getDeclaredField("buffer");
    bufferField.setAccessible(true);
    char[] buffer = (char[]) bufferField.get(jsonReader);
    Arrays.fill(buffer, '\0');
    for (int i = 0; i < s.length(); i++) {
      buffer[i] = s.charAt(i);
    }
  }

  private void setPos(int pos) throws Exception {
    Field posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    posField.setInt(jsonReader, pos);
  }

  private void setStackSize(int size) throws Exception {
    Field stackSizeField = JsonReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonReader, size);
  }

  private void setPathIndices(int index, int value) throws Exception {
    Field pathIndicesField = JsonReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(jsonReader);
    pathIndices[index] = value;
  }

  private int getPathIndices(int index) throws Exception {
    Field pathIndicesField = JsonReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(jsonReader);
    return pathIndices[index];
  }

  private int getPathIndices(int index, JsonReader reader) throws Exception {
    Field pathIndicesField = JsonReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(reader);
    return pathIndices[index];
  }

  private void setLenient(boolean lenient) throws Exception {
    Field lenientField = JsonReader.class.getDeclaredField("lenient");
    lenientField.setAccessible(true);
    lenientField.setBoolean(jsonReader, lenient);
  }

  private String getPeekedString() throws Exception {
    Field peekedStringField = JsonReader.class.getDeclaredField("peekedString");
    peekedStringField.setAccessible(true);
    return (String) peekedStringField.get(jsonReader);
  }

  private String getPeekedString(JsonReader reader) throws Exception {
    Field peekedStringField = JsonReader.class.getDeclaredField("peekedString");
    peekedStringField.setAccessible(true);
    return (String) peekedStringField.get(reader);
  }
}