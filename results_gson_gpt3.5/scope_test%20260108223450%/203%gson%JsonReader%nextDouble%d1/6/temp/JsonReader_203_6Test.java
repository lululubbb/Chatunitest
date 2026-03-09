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

public class JsonReader_203_6Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  @Test
    @Timeout(8000)
  void nextDouble_peekedNone_callsDoPeekAndParsesDouble() throws Exception {
    setField(jsonReader, "peeked", 0); // PEEKED_NONE
    // Mock doPeek to return PEEKED_NUMBER
    setMethodReturn(jsonReader, "doPeek", 16); // PEEKED_NUMBER
    setField(jsonReader, "peeked", 16);
    char[] buffer = "123.45".toCharArray();
    setField(jsonReader, "buffer", buffer);
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "peekedNumberLength", buffer.length);
    setField(jsonReader, "pathIndices", new int[] {0});
    setField(jsonReader, "stackSize", 1);

    double result = jsonReader.nextDouble();

    assertEquals(123.45, result, 0.00001);
    assertEquals(6, getField(jsonReader, "pos"));
    assertEquals(0, getField(jsonReader, "peeked"));
    assertNull(getField(jsonReader, "peekedString"));
    assertEquals(1, ((int[]) getField(jsonReader, "pathIndices"))[0]);
  }

  @Test
    @Timeout(8000)
  void nextDouble_peekedLong_returnsPeekedLongAsDouble() throws IOException {
    setField(jsonReader, "peeked", 15); // PEEKED_LONG
    setField(jsonReader, "peekedLong", 42L);
    setField(jsonReader, "pathIndices", new int[] {5});
    setField(jsonReader, "stackSize", 1);

    double result = jsonReader.nextDouble();

    assertEquals(42.0, result, 0.00001);
    assertEquals(0, getField(jsonReader, "peeked"));
    assertEquals(6, ((int[]) getField(jsonReader, "pathIndices"))[0]);
  }

  @Test
    @Timeout(8000)
  void nextDouble_peekedSingleQuoted_returnsParsedDouble() throws Exception {
    setField(jsonReader, "peeked", 8); // PEEKED_SINGLE_QUOTED
    setField(jsonReader, "pathIndices", new int[] {0});
    setField(jsonReader, "stackSize", 1);
    // Mock nextQuotedValue to return "3.14"
    setMethodReturn(jsonReader, "nextQuotedValue", "3.14", char.class);

    double result = jsonReader.nextDouble();

    assertEquals(3.14, result, 0.00001);
    assertEquals(0, getField(jsonReader, "peeked"));
    assertNull(getField(jsonReader, "peekedString"));
    assertEquals(1, ((int[]) getField(jsonReader, "pathIndices"))[0]);
  }

  @Test
    @Timeout(8000)
  void nextDouble_peekedDoubleQuoted_returnsParsedDouble() throws Exception {
    setField(jsonReader, "peeked", 9); // PEEKED_DOUBLE_QUOTED
    setField(jsonReader, "pathIndices", new int[] {0});
    setField(jsonReader, "stackSize", 1);
    // Mock nextQuotedValue to return "2.718"
    setMethodReturn(jsonReader, "nextQuotedValue", "2.718", char.class);

    double result = jsonReader.nextDouble();

    assertEquals(2.718, result, 0.00001);
    assertEquals(0, getField(jsonReader, "peeked"));
    assertNull(getField(jsonReader, "peekedString"));
    assertEquals(1, ((int[]) getField(jsonReader, "pathIndices"))[0]);
  }

  @Test
    @Timeout(8000)
  void nextDouble_peekedUnquoted_returnsParsedDouble() throws Exception {
    setField(jsonReader, "peeked", 10); // PEEKED_UNQUOTED
    setField(jsonReader, "pathIndices", new int[] {0});
    setField(jsonReader, "stackSize", 1);
    // Mock nextUnquotedValue to return "0.57721"
    setMethodReturn(jsonReader, "nextUnquotedValue", "0.57721");

    double result = jsonReader.nextDouble();

    assertEquals(0.57721, result, 0.00001);
    assertEquals(0, getField(jsonReader, "peeked"));
    assertNull(getField(jsonReader, "peekedString"));
    assertEquals(1, ((int[]) getField(jsonReader, "pathIndices"))[0]);
  }

  @Test
    @Timeout(8000)
  void nextDouble_peekedInvalid_throwsIllegalStateException() throws IOException {
    setField(jsonReader, "peeked", 5); // PEEKED_TRUE (invalid for double)
    Exception e = assertThrows(IllegalStateException.class, () -> jsonReader.nextDouble());
    assertTrue(e.getMessage().contains("Expected a double but was"));
  }

  @Test
    @Timeout(8000)
  void nextDouble_notLenientNaN_throwsMalformedJsonException() throws Exception {
    setField(jsonReader, "peeked", 10); // PEEKED_UNQUOTED
    setField(jsonReader, "pathIndices", new int[] {0});
    setField(jsonReader, "stackSize", 1);
    setField(jsonReader, "lenient", false);
    // Mock nextUnquotedValue to return "NaN"
    setMethodReturn(jsonReader, "nextUnquotedValue", "NaN");

    Exception e = assertThrows(MalformedJsonException.class, () -> jsonReader.nextDouble());
    assertTrue(e.getMessage().contains("JSON forbids NaN and infinities"));
  }

  @Test
    @Timeout(8000)
  void nextDouble_lenientNaN_returnsNaN() throws Exception {
    setField(jsonReader, "peeked", 10); // PEEKED_UNQUOTED
    setField(jsonReader, "pathIndices", new int[] {0});
    setField(jsonReader, "stackSize", 1);
    setField(jsonReader, "lenient", true);
    // Mock nextUnquotedValue to return "NaN"
    setMethodReturn(jsonReader, "nextUnquotedValue", "NaN");

    double result = jsonReader.nextDouble();

    assertTrue(Double.isNaN(result));
    assertEquals(0, getField(jsonReader, "peeked"));
    assertNull(getField(jsonReader, "peekedString"));
  }

  @Test
    @Timeout(8000)
  void nextDouble_lenientInfinity_returnsInfinity() throws Exception {
    setField(jsonReader, "peeked", 10); // PEEKED_UNQUOTED
    setField(jsonReader, "pathIndices", new int[] {0});
    setField(jsonReader, "stackSize", 1);
    setField(jsonReader, "lenient", true);
    // Mock nextUnquotedValue to return "Infinity"
    setMethodReturn(jsonReader, "nextUnquotedValue", "Infinity");

    double result = jsonReader.nextDouble();

    assertTrue(Double.isInfinite(result));
    assertEquals(0, getField(jsonReader, "peeked"));
    assertNull(getField(jsonReader, "peekedString"));
  }

  // Helper methods to set private fields via reflection
  private void setField(Object target, String fieldName, Object value) {
    try {
      Field field = JsonReader.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(target, value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private Object getField(Object target, String fieldName) {
    try {
      Field field = JsonReader.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      return field.get(target);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  // Helper to mock private method returning int or String with no args or one char arg
  private void setMethodReturn(JsonReader target, String methodName, Object returnValue, Class<?>... paramTypes) throws Exception {
    Method method = JsonReader.class.getDeclaredMethod(methodName, paramTypes);
    method.setAccessible(true);

    // Create spy to override method
    JsonReader spy = spy(target);

    if (paramTypes.length == 0) {
      doReturn(returnValue).when(spy, methodName);
    } else if (paramTypes.length == 1 && paramTypes[0] == char.class) {
      doReturn(returnValue).when(spy).nextQuotedValue(anyChar());
    }

    // Replace original reference to spy
    this.jsonReader = spy;
  }
}