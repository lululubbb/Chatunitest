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

public class JsonReader_203_4Test {

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
      int[] pathIndices = new int[32];
      pathIndices[0] = 0;
      pathIndicesField.set(jsonReader, pathIndices);

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Test
    @Timeout(8000)
  public void nextDouble_peekedNone_peekedLong() throws Exception {
    setField(jsonReader, "peeked", 0); // PEEKED_NONE
    // Mock doPeek to return PEEKED_LONG
    Method doPeekMethod = JsonReader.class.getDeclaredMethod("doPeek");
    doPeekMethod.setAccessible(true);
    JsonReader spyReader = spy(jsonReader);
    doReturn(15).when(spyReader).doPeek(); // PEEKED_LONG

    setField(spyReader, "peekedLong", 123L);
    setField(spyReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    pathIndices[0] = 5;
    setField(spyReader, "pathIndices", pathIndices);

    setField(spyReader, "peeked", 15);
    double result = spyReader.nextDouble();

    assertEquals(123.0, result);
    assertEquals(6, pathIndices[0]);
    assertEquals(0, getField(spyReader, "peeked"));
  }

  @Test
    @Timeout(8000)
  public void nextDouble_peekedNumber_validDouble() throws Exception {
    setField(jsonReader, "peeked", 16); // PEEKED_NUMBER
    char[] buffer = "3.14159xyz".toCharArray();
    setField(jsonReader, "buffer", buffer);
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "peekedNumberLength", 7); // length of "3.14159"
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    pathIndices[0] = 0;
    setField(jsonReader, "pathIndices", pathIndices);
    setField(jsonReader, "lenient", false);

    double result = jsonReader.nextDouble();

    assertEquals(3.14159, result, 0.000001);
    assertEquals(7, getField(jsonReader, "pos"));
    assertNull(getField(jsonReader, "peekedString"));
    assertEquals(0, getField(jsonReader, "peeked"));
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void nextDouble_peekedSingleQuoted_validDouble() throws Exception {
    setField(jsonReader, "peeked", 8); // PEEKED_SINGLE_QUOTED
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    pathIndices[0] = 2;
    setField(jsonReader, "pathIndices", pathIndices);
    setField(jsonReader, "lenient", false);

    JsonReader spyReader = spy(jsonReader);
    doReturn("2.71828").when(spyReader).nextQuotedValue('\'');

    double result = spyReader.nextDouble();

    assertEquals(2.71828, result, 0.000001);
    assertNull(getField(spyReader, "peekedString"));
    assertEquals(0, getField(spyReader, "peeked"));
    assertEquals(3, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void nextDouble_peekedDoubleQuoted_validDouble() throws Exception {
    setField(jsonReader, "peeked", 9); // PEEKED_DOUBLE_QUOTED
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    pathIndices[0] = 4;
    setField(jsonReader, "pathIndices", pathIndices);
    setField(jsonReader, "lenient", false);

    JsonReader spyReader = spy(jsonReader);
    doReturn("1.41421").when(spyReader).nextQuotedValue('"');

    double result = spyReader.nextDouble();

    assertEquals(1.41421, result, 0.000001);
    assertNull(getField(spyReader, "peekedString"));
    assertEquals(0, getField(spyReader, "peeked"));
    assertEquals(5, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void nextDouble_peekedUnquoted_validDouble() throws Exception {
    setField(jsonReader, "peeked", 10); // PEEKED_UNQUOTED
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    pathIndices[0] = 7;
    setField(jsonReader, "pathIndices", pathIndices);
    setField(jsonReader, "lenient", false);

    JsonReader spyReader = spy(jsonReader);
    doReturn("0.57721").when(spyReader).nextUnquotedValue();

    double result = spyReader.nextDouble();

    assertEquals(0.57721, result, 0.000001);
    assertNull(getField(spyReader, "peekedString"));
    assertEquals(0, getField(spyReader, "peeked"));
    assertEquals(8, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void nextDouble_peekedInvalid_throwsIllegalStateException() throws Exception {
    setField(jsonReader, "peeked", 5); // PEEKED_TRUE (invalid for double)
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    pathIndices[0] = 0;
    setField(jsonReader, "pathIndices", pathIndices);

    IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
      jsonReader.nextDouble();
    });
    assertTrue(ex.getMessage().contains("Expected a double but was"));
  }

  @Test
    @Timeout(8000)
  public void nextDouble_nanAndInfiniteLenientFalse_throwsMalformedJsonException() throws Exception {
    setField(jsonReader, "peeked", 16); // PEEKED_NUMBER
    char[] buffer = "NaN".toCharArray();
    setField(jsonReader, "buffer", buffer);
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "peekedNumberLength", 3);
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    pathIndices[0] = 0;
    setField(jsonReader, "pathIndices", pathIndices);
    setField(jsonReader, "lenient", false);

    MalformedJsonException ex = assertThrows(MalformedJsonException.class, () -> {
      jsonReader.nextDouble();
    });
    assertTrue(ex.getMessage().contains("JSON forbids NaN and infinities"));
  }

  @Test
    @Timeout(8000)
  public void nextDouble_nanLenientTrue_returnsNan() throws Exception {
    setField(jsonReader, "peeked", 16); // PEEKED_NUMBER
    char[] buffer = "NaN".toCharArray();
    setField(jsonReader, "buffer", buffer);
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "peekedNumberLength", 3);
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    pathIndices[0] = 0;
    setField(jsonReader, "pathIndices", pathIndices);
    setField(jsonReader, "lenient", true);

    double result = jsonReader.nextDouble();

    assertTrue(Double.isNaN(result));
    assertNull(getField(jsonReader, "peekedString"));
    assertEquals(0, getField(jsonReader, "peeked"));
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void nextDouble_infiniteLenientTrue_returnsInfinity() throws Exception {
    setField(jsonReader, "peeked", 16); // PEEKED_NUMBER
    char[] buffer = "Infinity".toCharArray();
    setField(jsonReader, "buffer", buffer);
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "peekedNumberLength", 8);
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    pathIndices[0] = 0;
    setField(jsonReader, "pathIndices", pathIndices);
    setField(jsonReader, "lenient", true);

    double result = jsonReader.nextDouble();

    assertTrue(Double.isInfinite(result));
    assertNull(getField(jsonReader, "peekedString"));
    assertEquals(0, getField(jsonReader, "peeked"));
    assertEquals(1, pathIndices[0]);
  }

  // Helper methods to set and get private fields via reflection
  private void setField(Object instance, String fieldName, Object value) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(instance, value);
  }

  private <T> T getField(Object instance, String fieldName) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    return (T) field.get(instance);
  }
}