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

public class JsonReader_209_6Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  public void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
    // Initialize stackSize and pathIndices for increment usage
    try {
      Field stackSizeField = JsonReader.class.getDeclaredField("stackSize");
      stackSizeField.setAccessible(true);
      stackSizeField.setInt(jsonReader, 1);

      Field pathIndicesField = JsonReader.class.getDeclaredField("pathIndices");
      pathIndicesField.setAccessible(true);
      int[] pathIndices = new int[32];
      pathIndices[0] = 0;
      pathIndicesField.set(jsonReader, pathIndices);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void setPeeked(int value) throws Exception {
    Field peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    peekedField.setInt(jsonReader, value);
  }

  private int getPeeked() throws Exception {
    Field peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    return peekedField.getInt(jsonReader);
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

  private void setPos(int pos) throws Exception {
    Field posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    posField.setInt(jsonReader, pos);
  }

  private void setBuffer(char[] buffer) throws Exception {
    Field bufferField = JsonReader.class.getDeclaredField("buffer");
    bufferField.setAccessible(true);
    bufferField.set(jsonReader, buffer);
  }

  private void setPeekedString(String value) throws Exception {
    Field peekedStringField = JsonReader.class.getDeclaredField("peekedString");
    peekedStringField.setAccessible(true);
    peekedStringField.set(jsonReader, value);
  }

  private void setStackSize(int size) throws Exception {
    Field stackSizeField = JsonReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonReader, size);
  }

  private int[] getPathIndices() throws Exception {
    Field pathIndicesField = JsonReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    return (int[]) pathIndicesField.get(jsonReader);
  }

  private void setPeekedNumber(int val) throws Exception {
    setPeeked(val);
  }

  private int invokeNextInt() throws Exception {
    Method nextIntMethod = JsonReader.class.getDeclaredMethod("nextInt");
    nextIntMethod.setAccessible(true);
    return (int) nextIntMethod.invoke(jsonReader);
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

  private JsonToken invokePeek() throws Exception {
    Method peek = JsonReader.class.getDeclaredMethod("peek");
    peek.setAccessible(true);
    return (JsonToken) peek.invoke(jsonReader);
  }

  private void setLenient(boolean lenient) throws Exception {
    Method setLenient = JsonReader.class.getDeclaredMethod("setLenient", boolean.class);
    setLenient.setAccessible(true);
    setLenient.invoke(jsonReader, lenient);
  }

  private void setPeekedStringToNextQuotedValue(char quote, String val) throws Exception {
    // nextQuotedValue is private, so we invoke it for side-effect or set peekedString directly
    setPeekedString(val);
    setPeeked(quote == '\'' ? 8 : 9);
  }

  private void setPeekedStringToNextUnquotedValue(String val) throws Exception {
    setPeekedString(val);
    setPeeked(10);
  }

  @Test
    @Timeout(8000)
  public void testNextInt_peekedNone_callsDoPeekAndReturnsIntFromPeekedLong() throws Exception {
    setPeeked(0); // PEEKED_NONE
    setPeekedLong(123);
    // doPeek returns PEEKED_LONG
    Method doPeek = JsonReader.class.getDeclaredMethod("doPeek");
    doPeek.setAccessible(true);
    // Spy jsonReader to mock doPeek
    JsonReader spyReader = spy(jsonReader);
    doReturn(15).when(spyReader).doPeek(); // PEEKED_LONG

    // Set fields for spy
    Field peekedLongField = JsonReader.class.getDeclaredField("peekedLong");
    peekedLongField.setAccessible(true);
    peekedLongField.setLong(spyReader, 123L);

    Field stackSizeField = JsonReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(spyReader, 1);

    Field pathIndicesField = JsonReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = new int[32];
    pathIndices[0] = 0;
    pathIndicesField.set(spyReader, pathIndices);

    int result = spyReader.nextInt();
    assertEquals(123, result);
    assertEquals(0, spyReader.peeked);
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void testNextInt_peekedLongNotInt_throwsNumberFormatException() throws Exception {
    setPeeked(15); // PEEKED_LONG
    setPeekedLong(Integer.MAX_VALUE + 1L);
    Field stackSizeField = JsonReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonReader, 1);
    Field pathIndicesField = JsonReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = new int[32];
    pathIndicesField.set(jsonReader, pathIndices);

    NumberFormatException ex = assertThrows(NumberFormatException.class, () -> jsonReader.nextInt());
    assertTrue(ex.getMessage().contains("Expected an int but was"));
  }

  @Test
    @Timeout(8000)
  public void testNextInt_peekedNumber_parsesIntFromBuffer() throws Exception {
    setPeeked(16); // PEEKED_NUMBER
    setPeekedNumberLength(3);
    setPos(0);
    char[] buffer = new char[1024];
    buffer[0] = '1';
    buffer[1] = '2';
    buffer[2] = '3';
    setBuffer(buffer);
    Field stackSizeField = JsonReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonReader, 1);
    Field pathIndicesField = JsonReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = new int[32];
    pathIndicesField.set(jsonReader, pathIndices);

    int result = jsonReader.nextInt();
    assertEquals(123, result);
    assertEquals(3, getPos());
    assertEquals(0, getPeeked());
    assertEquals(1, pathIndices[0]);
  }

  private int getPos() throws Exception {
    Field posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    return posField.getInt(jsonReader);
  }

  @Test
    @Timeout(8000)
  public void testNextInt_peekedSingleQuoted_parsesInt() throws Exception {
    setPeeked(8); // PEEKED_SINGLE_QUOTED
    setPeekedString("456");
    Field stackSizeField = JsonReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonReader, 1);
    Field pathIndicesField = JsonReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = new int[32];
    pathIndicesField.set(jsonReader, pathIndices);

    int result = jsonReader.nextInt();
    assertEquals(456, result);
    assertEquals(0, getPeeked());
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void testNextInt_peekedDoubleQuoted_parsesInt() throws Exception {
    setPeeked(9); // PEEKED_DOUBLE_QUOTED
    setPeekedString("789");
    Field stackSizeField = JsonReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonReader, 1);
    Field pathIndicesField = JsonReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = new int[32];
    pathIndicesField.set(jsonReader, pathIndices);

    int result = jsonReader.nextInt();
    assertEquals(789, result);
    assertEquals(0, getPeeked());
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void testNextInt_peekedUnquoted_parsesInt() throws Exception {
    setPeeked(10); // PEEKED_UNQUOTED
    setPeekedString("321");
    Field stackSizeField = JsonReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonReader, 1);
    Field pathIndicesField = JsonReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = new int[32];
    pathIndicesField.set(jsonReader, pathIndices);

    int result = jsonReader.nextInt();
    assertEquals(321, result);
    assertEquals(0, getPeeked());
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void testNextInt_peekedUnquoted_nonIntFallsBackToDouble() throws Exception {
    setPeeked(10); // PEEKED_UNQUOTED
    setPeekedString("123.0");
    Field stackSizeField = JsonReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonReader, 1);
    Field pathIndicesField = JsonReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = new int[32];
    pathIndicesField.set(jsonReader, pathIndices);

    int result = jsonReader.nextInt();
    assertEquals(123, result);
    assertNull(getPeekedString());
    assertEquals(0, getPeeked());
    assertEquals(1, pathIndices[0]);
  }

  private String getPeekedString() throws Exception {
    Field peekedStringField = JsonReader.class.getDeclaredField("peekedString");
    peekedStringField.setAccessible(true);
    return (String) peekedStringField.get(jsonReader);
  }

  @Test
    @Timeout(8000)
  public void testNextInt_peekedUnquoted_nonIntDoubleThrowsNumberFormatException() throws Exception {
    setPeeked(10); // PEEKED_UNQUOTED
    setPeekedString("123.4");
    Field stackSizeField = JsonReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonReader, 1);
    Field pathIndicesField = JsonReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = new int[32];
    pathIndicesField.set(jsonReader, pathIndices);

    NumberFormatException ex = assertThrows(NumberFormatException.class, () -> jsonReader.nextInt());
    assertTrue(ex.getMessage().contains("Expected an int but was"));
  }

  @Test
    @Timeout(8000)
  public void testNextInt_invalidPeeked_throwsIllegalStateException() throws Exception {
    setPeeked(5); // PEEKED_TRUE (invalid for nextInt)
    Field stackSizeField = JsonReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonReader, 1);

    IllegalStateException ex = assertThrows(IllegalStateException.class, () -> jsonReader.nextInt());
    assertTrue(ex.getMessage().contains("Expected an int but was"));
  }
}