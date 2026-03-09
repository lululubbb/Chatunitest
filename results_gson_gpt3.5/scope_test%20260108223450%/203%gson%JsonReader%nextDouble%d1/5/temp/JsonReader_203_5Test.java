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

public class JsonReader_203_5Test {

  private JsonReader jsonReader;

  @BeforeEach
  public void setUp() throws Exception {
    Reader mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);

    // Set stackSize to 1 to avoid IndexOutOfBounds in pathIndices
    setField(jsonReader, "stackSize", 1);
    // Initialize pathIndices array with 0 at index 0
    int[] pathIndices = new int[32];
    pathIndices[0] = 0;
    setField(jsonReader, "pathIndices", pathIndices);
  }

  @Test
    @Timeout(8000)
  public void nextDouble_peekedNone_peekedLong() throws Exception {
    setField(jsonReader, "peeked", 0); // PEEKED_NONE

    // Mock doPeek() to return PEEKED_LONG
    Method doPeek = JsonReader.class.getDeclaredMethod("doPeek");
    doPeek.setAccessible(true);
    JsonReader spyReader = spy(jsonReader);
    doReturn(15).when(spyReader).doPeek(); // PEEKED_LONG

    // Set peekedLong value
    setField(spyReader, "peekedLong", 42L);

    // Set stackSize and pathIndices for increment
    setField(spyReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    pathIndices[0] = 0;
    setField(spyReader, "pathIndices", pathIndices);

    double result = spyReader.nextDouble();

    assertEquals(42.0, result);
    assertEquals(0, getField(spyReader, "peeked"));
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void nextDouble_peekedNumber_validDouble() throws Exception {
    setField(jsonReader, "peeked", 16); // PEEKED_NUMBER
    char[] buffer = "123.456 extra".toCharArray();
    setField(jsonReader, "buffer", buffer);
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "peekedNumberLength", 7); // length of "123.456"
    setField(jsonReader, "lenient", false);
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    pathIndices[0] = 0;
    setField(jsonReader, "pathIndices", pathIndices);

    double result = jsonReader.nextDouble();

    assertEquals(123.456, result, 0.000001);
    assertNull(getField(jsonReader, "peekedString"));
    assertEquals(0, getField(jsonReader, "peeked"));
    assertEquals(7, getField(jsonReader, "pos"));
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void nextDouble_peekedSingleQuoted_validDouble() throws Exception {
    setField(jsonReader, "peeked", 8); // PEEKED_SINGLE_QUOTED
    setField(jsonReader, "lenient", false);
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    pathIndices[0] = 0;
    setField(jsonReader, "pathIndices", pathIndices);

    // Spy to mock nextQuotedValue
    JsonReader spyReader = spy(jsonReader);
    doReturn("789.01").when(spyReader).nextQuotedValue('\'');

    double result = spyReader.nextDouble();

    assertEquals(789.01, result, 0.000001);
    assertNull(getField(spyReader, "peekedString"));
    assertEquals(0, getField(spyReader, "peeked"));
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void nextDouble_peekedDoubleQuoted_validDouble() throws Exception {
    setField(jsonReader, "peeked", 9); // PEEKED_DOUBLE_QUOTED
    setField(jsonReader, "lenient", false);
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    pathIndices[0] = 0;
    setField(jsonReader, "pathIndices", pathIndices);

    JsonReader spyReader = spy(jsonReader);
    doReturn("3.14159").when(spyReader).nextQuotedValue('"');

    double result = spyReader.nextDouble();

    assertEquals(3.14159, result, 0.000001);
    assertNull(getField(spyReader, "peekedString"));
    assertEquals(0, getField(spyReader, "peeked"));
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void nextDouble_peekedUnquoted_validDouble() throws Exception {
    setField(jsonReader, "peeked", 10); // PEEKED_UNQUOTED
    setField(jsonReader, "lenient", false);
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    pathIndices[0] = 0;
    setField(jsonReader, "pathIndices", pathIndices);

    JsonReader spyReader = spy(jsonReader);
    doReturn("2.71828").when(spyReader).nextUnquotedValue();

    double result = spyReader.nextDouble();

    assertEquals(2.71828, result, 0.000001);
    assertNull(getField(spyReader, "peekedString"));
    assertEquals(0, getField(spyReader, "peeked"));
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void nextDouble_peekedInvalid_throwsIllegalStateException() throws Exception {
    setField(jsonReader, "peeked", 5); // PEEKED_TRUE (not handled)

    // Mock peek() to return JsonToken.TRUE
    JsonReader spyReader = spy(jsonReader);
    when(spyReader.peek()).thenReturn(JsonToken.TRUE);

    IllegalStateException ex = assertThrows(IllegalStateException.class, spyReader::nextDouble);
    assertTrue(ex.getMessage().contains("Expected a double but was TRUE"));
  }

  @Test
    @Timeout(8000)
  public void nextDouble_lenientFalse_nan_throwsMalformedJsonException() throws Exception {
    setField(jsonReader, "peeked", 16); // PEEKED_NUMBER
    setField(jsonReader, "peekedString", "NaN");
    setField(jsonReader, "lenient", false);
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    pathIndices[0] = 0;
    setField(jsonReader, "pathIndices", pathIndices);

    // We need to set peeked to PEEKED_BUFFERED to avoid IllegalStateException
    setField(jsonReader, "peeked", 16);

    // We simulate the path where peeked == PEEKED_NUMBER sets peekedString and peeked = PEEKED_BUFFERED
    // So call nextDouble after setting peekedString and peeked to PEEKED_BUFFERED
    setField(jsonReader, "peeked", 16);
    setField(jsonReader, "peekedNumberLength", 3);
    char[] buffer = "NaN".toCharArray();
    setField(jsonReader, "buffer", buffer);
    setField(jsonReader, "pos", 0);

    // Call nextDouble and expect MalformedJsonException
    MalformedJsonException ex = assertThrows(MalformedJsonException.class, () -> {
      jsonReader.nextDouble();
    });
    assertTrue(ex.getMessage().contains("JSON forbids NaN and infinities"));
  }

  @Test
    @Timeout(8000)
  public void nextDouble_lenientTrue_nan_returnsNan() throws Exception {
    setField(jsonReader, "peeked", 16); // PEEKED_NUMBER
    setField(jsonReader, "peekedString", "NaN");
    setField(jsonReader, "lenient", true);
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    pathIndices[0] = 0;
    setField(jsonReader, "pathIndices", pathIndices);

    // Setup buffer and pos for peekedNumberLength
    setField(jsonReader, "peekedNumberLength", 3);
    char[] buffer = "NaN".toCharArray();
    setField(jsonReader, "buffer", buffer);
    setField(jsonReader, "pos", 0);

    // Call nextDouble and expect NaN result
    double result = jsonReader.nextDouble();

    assertTrue(Double.isNaN(result));
    assertNull(getField(jsonReader, "peekedString"));
    assertEquals(0, getField(jsonReader, "peeked"));
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void nextDouble_lenientFalse_infinite_throwsMalformedJsonException() throws Exception {
    setField(jsonReader, "peeked", 16); // PEEKED_NUMBER
    setField(jsonReader, "peekedString", "Infinity");
    setField(jsonReader, "lenient", false);
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    pathIndices[0] = 0;
    setField(jsonReader, "pathIndices", pathIndices);

    setField(jsonReader, "peekedNumberLength", 8);
    char[] buffer = "Infinity".toCharArray();
    setField(jsonReader, "buffer", buffer);
    setField(jsonReader, "pos", 0);

    MalformedJsonException ex = assertThrows(MalformedJsonException.class, () -> {
      jsonReader.nextDouble();
    });
    assertTrue(ex.getMessage().contains("JSON forbids NaN and infinities"));
  }

  @Test
    @Timeout(8000)
  public void nextDouble_lenientTrue_infinite_returnsInfinite() throws Exception {
    setField(jsonReader, "peeked", 16); // PEEKED_NUMBER
    setField(jsonReader, "peekedString", "Infinity");
    setField(jsonReader, "lenient", true);
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    pathIndices[0] = 0;
    setField(jsonReader, "pathIndices", pathIndices);

    setField(jsonReader, "peekedNumberLength", 8);
    char[] buffer = "Infinity".toCharArray();
    setField(jsonReader, "buffer", buffer);
    setField(jsonReader, "pos", 0);

    double result = jsonReader.nextDouble();

    assertTrue(Double.isInfinite(result));
    assertNull(getField(jsonReader, "peekedString"));
    assertEquals(0, getField(jsonReader, "peeked"));
    assertEquals(1, pathIndices[0]);
  }

  private void setField(Object target, String fieldName, Object value) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  private <T> T getField(Object target, String fieldName) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    return (T) field.get(target);
  }
}