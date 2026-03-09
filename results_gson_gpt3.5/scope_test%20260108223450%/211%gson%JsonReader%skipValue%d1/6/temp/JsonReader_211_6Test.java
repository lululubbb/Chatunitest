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

public class JsonReader_211_6Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  public void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  @Test
    @Timeout(8000)
  public void testSkipValue_beginArrayAndEndArray() throws Exception {
    // Setup: peeked = PEEKED_BEGIN_ARRAY then PEEKED_END_ARRAY
    setField(jsonReader, "peeked", 0);
    setField(jsonReader, "stackSize", 0);
    setField(jsonReader, "pathIndices", new int[32]);
    setField(jsonReader, "pathNames", new String[32]);

    // Spy on jsonReader to mock doPeek and push methods
    JsonReader spyReader = spy(jsonReader);

    // doPeek returns PEEKED_BEGIN_ARRAY first, then PEEKED_END_ARRAY
    when(spyReader.doPeek()).thenReturn(3).thenReturn(4);

    // Use real push method
    doCallRealMethod().when(spyReader).push(anyInt());

    // Call skipValue
    spyReader.skipValue();

    // Verify push called once with EMPTY_ARRAY (6)
    verify(spyReader, times(1)).push(JsonScope.EMPTY_ARRAY);

    // After skipping, stackSize should be back to 0
    int stackSize = getField(spyReader, "stackSize");
    assertEquals(0, stackSize);

    // pathIndices[stackSize - 1]++ should not throw (stackSize is 0, so no increment)
  }

  @Test
    @Timeout(8000)
  public void testSkipValue_beginObjectAndEndObject() throws Exception {
    setField(jsonReader, "peeked", 0);
    setField(jsonReader, "stackSize", 0);
    setField(jsonReader, "pathIndices", new int[32]);
    String[] pathNames = new String[32];
    setField(jsonReader, "pathNames", pathNames);

    JsonReader spyReader = spy(jsonReader);

    when(spyReader.doPeek()).thenReturn(1).thenReturn(2);

    doCallRealMethod().when(spyReader).push(anyInt());

    spyReader.skipValue();

    verify(spyReader, times(1)).push(JsonScope.EMPTY_OBJECT);

    int stackSize = getField(spyReader, "stackSize");
    assertEquals(0, stackSize);

    // Check pathNames[stackSize - 1] is null (stackSize 0, so no update)
  }

  @Test
    @Timeout(8000)
  public void testSkipValue_unquoted() throws Exception {
    setField(jsonReader, "peeked", 10); // PEEKED_UNQUOTED
    setField(jsonReader, "stackSize", 1);
    setField(jsonReader, "pathIndices", new int[32]);
    setField(jsonReader, "pathNames", new String[32]);

    JsonReader spyReader = spy(jsonReader);

    doNothing().when(spyReader).skipUnquotedValue();

    spyReader.skipValue();

    verify(spyReader, times(1)).skipUnquotedValue();

    int pathIndex = getField(spyReader, "pathIndices")[0];
    assertEquals(1, pathIndex);
  }

  @Test
    @Timeout(8000)
  public void testSkipValue_singleQuoted() throws Exception {
    setField(jsonReader, "peeked", 8); // PEEKED_SINGLE_QUOTED
    setField(jsonReader, "stackSize", 1);
    setField(jsonReader, "pathIndices", new int[32]);
    setField(jsonReader, "pathNames", new String[32]);

    JsonReader spyReader = spy(jsonReader);

    doNothing().when(spyReader).skipQuotedValue('\'');

    spyReader.skipValue();

    verify(spyReader, times(1)).skipQuotedValue('\'');

    int pathIndex = getField(spyReader, "pathIndices")[0];
    assertEquals(1, pathIndex);
  }

  @Test
    @Timeout(8000)
  public void testSkipValue_doubleQuoted() throws Exception {
    setField(jsonReader, "peeked", 9); // PEEKED_DOUBLE_QUOTED
    setField(jsonReader, "stackSize", 1);
    setField(jsonReader, "pathIndices", new int[32]);
    setField(jsonReader, "pathNames", new String[32]);

    JsonReader spyReader = spy(jsonReader);

    doNothing().when(spyReader).skipQuotedValue('"');

    spyReader.skipValue();

    verify(spyReader, times(1)).skipQuotedValue('"');

    int pathIndex = getField(spyReader, "pathIndices")[0];
    assertEquals(1, pathIndex);
  }

  @Test
    @Timeout(8000)
  public void testSkipValue_unquotedName() throws Exception {
    setField(jsonReader, "peeked", 14); // PEEKED_UNQUOTED_NAME
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    setField(jsonReader, "pathIndices", pathIndices);
    String[] pathNames = new String[32];
    setField(jsonReader, "pathNames", pathNames);

    JsonReader spyReader = spy(jsonReader);

    doNothing().when(spyReader).skipUnquotedValue();

    spyReader.skipValue();

    verify(spyReader, times(1)).skipUnquotedValue();

    assertEquals("<skipped>", pathNames[0]);
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void testSkipValue_singleQuotedName() throws Exception {
    setField(jsonReader, "peeked", 12); // PEEKED_SINGLE_QUOTED_NAME
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    setField(jsonReader, "pathIndices", pathIndices);
    String[] pathNames = new String[32];
    setField(jsonReader, "pathNames", pathNames);

    JsonReader spyReader = spy(jsonReader);

    doNothing().when(spyReader).skipQuotedValue('\'');

    spyReader.skipValue();

    verify(spyReader, times(1)).skipQuotedValue('\'');

    assertEquals("<skipped>", pathNames[0]);
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void testSkipValue_doubleQuotedName() throws Exception {
    setField(jsonReader, "peeked", 13); // PEEKED_DOUBLE_QUOTED_NAME
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    setField(jsonReader, "pathIndices", pathIndices);
    String[] pathNames = new String[32];
    setField(jsonReader, "pathNames", pathNames);

    JsonReader spyReader = spy(jsonReader);

    doNothing().when(spyReader).skipQuotedValue('"');

    spyReader.skipValue();

    verify(spyReader, times(1)).skipQuotedValue('"');

    assertEquals("<skipped>", pathNames[0]);
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void testSkipValue_number() throws Exception {
    setField(jsonReader, "peeked", 16); // PEEKED_NUMBER
    setField(jsonReader, "peekedNumberLength", 5);
    setField(jsonReader, "pos", 10);
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    setField(jsonReader, "pathIndices", pathIndices);

    jsonReader.skipValue();

    int pos = getField(jsonReader, "pos");
    assertEquals(15, pos);

    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void testSkipValue_peekedNoneCallsDoPeek() throws Exception {
    setField(jsonReader, "peeked", 0); // PEEKED_NONE
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    setField(jsonReader, "pathIndices", pathIndices);

    JsonReader spyReader = spy(jsonReader);
    when(spyReader.doPeek()).thenReturn(16);
    setField(spyReader, "peekedNumberLength", 3);
    setField(spyReader, "pos", 0);

    spyReader.skipValue();

    verify(spyReader, times(1)).doPeek();

    int pos = getField(spyReader, "pos");
    assertEquals(3, pos);

    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void testSkipValue_peekedEofReturnsImmediately() throws Exception {
    setField(jsonReader, "peeked", 17); // PEEKED_EOF
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    setField(jsonReader, "pathIndices", pathIndices);

    jsonReader.skipValue();

    // stackSize and pathIndices unchanged
    int stackSize = getField(jsonReader, "stackSize");
    assertEquals(1, stackSize);
    assertEquals(0, pathIndices[0]);
  }

  // Helper methods for reflection

  private void setField(Object target, String fieldName, Object value) throws Exception {
    Field field = getField(target.getClass(), fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  @SuppressWarnings("unchecked")
  private <T> T getField(Object target, String fieldName) throws Exception {
    Field field = getField(target.getClass(), fieldName);
    field.setAccessible(true);
    return (T) field.get(target);
  }

  private Field getField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
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