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

public class JsonReader_211_4Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  public void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  @Test
    @Timeout(8000)
  public void testSkipValue_peekedNoneWithBeginArrayAndEndArray() throws Exception {
    // Setup peeked to PEEKED_NONE so doPeek() is called, mock doPeek() to return PEEKED_BEGIN_ARRAY first
    setField(jsonReader, "peeked", 0);
    setField(jsonReader, "stackSize", 0);

    // Spy on jsonReader to mock doPeek()
    JsonReader spyReader = spy(jsonReader);
    doReturn(3) // PEEKED_BEGIN_ARRAY
      .doReturn(4) // PEEKED_END_ARRAY
      .when(spyReader).doPeek();

    // Setup stackSize and pathIndices for increments
    setField(spyReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    setField(spyReader, "pathIndices", pathIndices);
    String[] pathNames = new String[32];
    setField(spyReader, "pathNames", pathNames);

    // Call skipValue()
    spyReader.skipValue();

    // Verify stackSize is back to initial 1 after begin and end array
    assertEquals(1, getField(spyReader, "stackSize"));

    // Verify pathIndices[stackSize - 1] incremented by 1
    assertEquals(1, pathIndices[0]);

    // Verify peeked reset to PEEKED_NONE
    assertEquals(0, getField(spyReader, "peeked"));
  }

  @Test
    @Timeout(8000)
  public void testSkipValue_peekedBeginObjectAndEndObject() throws Exception {
    setField(jsonReader, "peeked", 0);
    setField(jsonReader, "stackSize", 1);
    String[] pathNames = new String[32];
    setField(jsonReader, "pathNames", pathNames);
    int[] pathIndices = new int[32];
    setField(jsonReader, "pathIndices", pathIndices);

    JsonReader spyReader = spy(jsonReader);
    doReturn(1) // PEEKED_BEGIN_OBJECT
      .doReturn(2) // PEEKED_END_OBJECT
      .when(spyReader).doPeek();

    spyReader.skipValue();

    assertEquals(1, getField(spyReader, "stackSize"));
    assertEquals(1, pathIndices[0]);
    assertNull(pathNames[0]);
    assertEquals(0, getField(spyReader, "peeked"));
  }

  @Test
    @Timeout(8000)
  public void testSkipValue_peekedUnquoted() throws Exception {
    setField(jsonReader, "peeked", JsonReader.PEEKED_UNQUOTED);
    setField(jsonReader, "stackSize", 1);
    String[] pathNames = new String[32];
    setField(jsonReader, "pathNames", pathNames);
    int[] pathIndices = new int[32];
    setField(jsonReader, "pathIndices", pathIndices);

    JsonReader spyReader = spy(jsonReader);
    doNothing().when(spyReader).skipUnquotedValue();

    spyReader.skipValue();

    verify(spyReader).skipUnquotedValue();
    assertEquals(1, getField(spyReader, "stackSize"));
    assertEquals(1, pathIndices[0]);
    assertEquals(0, getField(spyReader, "peeked"));
  }

  @Test
    @Timeout(8000)
  public void testSkipValue_peekedSingleQuoted() throws Exception {
    setField(jsonReader, "peeked", JsonReader.PEEKED_SINGLE_QUOTED);
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    setField(jsonReader, "pathIndices", pathIndices);

    JsonReader spyReader = spy(jsonReader);
    doNothing().when(spyReader).skipQuotedValue('\'');

    spyReader.skipValue();

    verify(spyReader).skipQuotedValue('\'');
    assertEquals(1, getField(spyReader, "stackSize"));
    assertEquals(1, pathIndices[0]);
    assertEquals(0, getField(spyReader, "peeked"));
  }

  @Test
    @Timeout(8000)
  public void testSkipValue_peekedDoubleQuoted() throws Exception {
    setField(jsonReader, "peeked", JsonReader.PEEKED_DOUBLE_QUOTED);
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    setField(jsonReader, "pathIndices", pathIndices);

    JsonReader spyReader = spy(jsonReader);
    doNothing().when(spyReader).skipQuotedValue('"');

    spyReader.skipValue();

    verify(spyReader).skipQuotedValue('"');
    assertEquals(1, getField(spyReader, "stackSize"));
    assertEquals(1, pathIndices[0]);
    assertEquals(0, getField(spyReader, "peeked"));
  }

  @Test
    @Timeout(8000)
  public void testSkipValue_peekedUnquotedName_countZero_setsPathNameSkipped() throws Exception {
    setField(jsonReader, "peeked", JsonReader.PEEKED_UNQUOTED_NAME);
    setField(jsonReader, "stackSize", 1);
    String[] pathNames = new String[32];
    setField(jsonReader, "pathNames", pathNames);
    int[] pathIndices = new int[32];
    setField(jsonReader, "pathIndices", pathIndices);

    JsonReader spyReader = spy(jsonReader);
    doNothing().when(spyReader).skipUnquotedValue();

    spyReader.skipValue();

    verify(spyReader).skipUnquotedValue();
    assertEquals("<skipped>", pathNames[0]);
    assertEquals(1, pathIndices[0]);
    assertEquals(0, getField(spyReader, "peeked"));
  }

  @Test
    @Timeout(8000)
  public void testSkipValue_peekedSingleQuotedName_countZero_setsPathNameSkipped() throws Exception {
    setField(jsonReader, "peeked", JsonReader.PEEKED_SINGLE_QUOTED_NAME);
    setField(jsonReader, "stackSize", 1);
    String[] pathNames = new String[32];
    setField(jsonReader, "pathNames", pathNames);
    int[] pathIndices = new int[32];
    setField(jsonReader, "pathIndices", pathIndices);

    JsonReader spyReader = spy(jsonReader);
    doNothing().when(spyReader).skipQuotedValue('\'');

    spyReader.skipValue();

    verify(spyReader).skipQuotedValue('\'');
    assertEquals("<skipped>", pathNames[0]);
    assertEquals(1, pathIndices[0]);
    assertEquals(0, getField(spyReader, "peeked"));
  }

  @Test
    @Timeout(8000)
  public void testSkipValue_peekedDoubleQuotedName_countZero_setsPathNameSkipped() throws Exception {
    setField(jsonReader, "peeked", JsonReader.PEEKED_DOUBLE_QUOTED_NAME);
    setField(jsonReader, "stackSize", 1);
    String[] pathNames = new String[32];
    setField(jsonReader, "pathNames", pathNames);
    int[] pathIndices = new int[32];
    setField(jsonReader, "pathIndices", pathIndices);

    JsonReader spyReader = spy(jsonReader);
    doNothing().when(spyReader).skipQuotedValue('"');

    spyReader.skipValue();

    verify(spyReader).skipQuotedValue('"');
    assertEquals("<skipped>", pathNames[0]);
    assertEquals(1, pathIndices[0]);
    assertEquals(0, getField(spyReader, "peeked"));
  }

  @Test
    @Timeout(8000)
  public void testSkipValue_peekedNumber_advancesPos() throws Exception {
    setField(jsonReader, "peeked", JsonReader.PEEKED_NUMBER);
    setField(jsonReader, "stackSize", 1);
    setField(jsonReader, "pos", 5);
    setField(jsonReader, "peekedNumberLength", 3);
    int[] pathIndices = new int[32];
    setField(jsonReader, "pathIndices", pathIndices);

    jsonReader.skipValue();

    assertEquals(8, getField(jsonReader, "pos"));
    assertEquals(1, pathIndices[0]);
    assertEquals(0, getField(jsonReader, "peeked"));
  }

  @Test
    @Timeout(8000)
  public void testSkipValue_peekedEOF_returnsImmediately() throws Exception {
    setField(jsonReader, "peeked", JsonReader.PEEKED_EOF);
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    setField(jsonReader, "pathIndices", pathIndices);

    jsonReader.skipValue();

    // peeked remains PEEKED_EOF (17)
    assertEquals(JsonReader.PEEKED_EOF, getField(jsonReader, "peeked"));
    // pathIndices not incremented
    assertEquals(0, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void testSkipValue_peekedEndObject_countZero_setsPathNameNull() throws Exception {
    setField(jsonReader, "peeked", JsonReader.PEEKED_END_OBJECT);
    setField(jsonReader, "stackSize", 1);
    String[] pathNames = new String[32];
    pathNames[0] = "someName";
    setField(jsonReader, "pathNames", pathNames);

    // count=0 means first iteration, so pathNames[stackSize-1] should be set to null
    // We simulate this by setting peeked to PEEKED_END_OBJECT and count=0 in first iteration

    // Use spy to return PEEKED_END_OBJECT immediately without doPeek call
    JsonReader spyReader = spy(jsonReader);
    doReturn(JsonReader.PEEKED_END_OBJECT).when(spyReader).doPeek();

    spyReader.skipValue();

    assertNull(pathNames[0]);
    assertEquals(0, getField(spyReader, "stackSize"));
    assertEquals(0, getField(spyReader, "peeked"));
  }

  private static void setField(Object target, String fieldName, Object value) throws Exception {
    Field field = getField(target.getClass(), fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  private static <T> T getField(Object target, String fieldName) throws Exception {
    Field field = getField(target.getClass(), fieldName);
    field.setAccessible(true);
    @SuppressWarnings("unchecked")
    T value = (T) field.get(target);
    return value;
  }

  private static Field getField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
    Class<?> current = clazz;
    while (current != null) {
      try {
        return current.getDeclaredField(fieldName);
      } catch (NoSuchFieldException e) {
        current = current.getSuperclass();
      }
    }
    throw new NoSuchFieldException(fieldName);
  }
}