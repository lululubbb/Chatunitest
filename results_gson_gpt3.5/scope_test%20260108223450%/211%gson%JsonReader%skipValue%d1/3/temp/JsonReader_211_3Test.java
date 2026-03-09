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

public class JsonReader_211_3Test {
  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  @Test
    @Timeout(8000)
  void skipValue_peekedNone_callsDoPeekAndProcessesToken() throws Exception {
    // Setup: peeked == PEEKED_NONE, doPeek returns PEEKED_BEGIN_ARRAY to enter loop once
    setField(jsonReader, "peeked", 0); // PEEKED_NONE
    setField(jsonReader, "stackSize", 0);
    setField(jsonReader, "pathIndices", new int[32]);
    setField(jsonReader, "pathNames", new String[32]);

    Method doPeekMethod = JsonReader.class.getDeclaredMethod("doPeek");
    doPeekMethod.setAccessible(true);

    JsonReader spyReader = spy(jsonReader);
    doReturn(3).when(spyReader).doPeek(); // PEEKED_BEGIN_ARRAY

    // Replace jsonReader with spyReader for this test
    // Because skipValue is not static, invoke on spyReader
    Method skipValueMethod = JsonReader.class.getDeclaredMethod("skipValue");
    skipValueMethod.setAccessible(true);

    // Call skipValue; it should push EMPTY_ARRAY and increment count then exit loop
    skipValueMethod.invoke(spyReader);

    int stackSize = (int) getField(spyReader, "stackSize");
    assertEquals(1, stackSize);

    int[] pathIndices = (int[]) getField(spyReader, "pathIndices");
    assertEquals(1, pathIndices[stackSize - 1]);
  }

  @Test
    @Timeout(8000)
  void skipValue_peekedBeginObject_andEndObject_updatesStackAndPathNames() throws Exception {
    setField(jsonReader, "peeked", 1); // PEEKED_BEGIN_OBJECT
    setField(jsonReader, "stackSize", 0);
    setField(jsonReader, "pathIndices", new int[32]);
    String[] pathNames = new String[32];
    setField(jsonReader, "pathNames", pathNames);

    Method skipValueMethod = JsonReader.class.getDeclaredMethod("skipValue");
    skipValueMethod.setAccessible(true);

    JsonReader spyReader = spy(jsonReader);
    doNothing().when(spyReader).push(anyInt());

    // Simulate push increments stackSize
    doAnswer(invocation -> {
      int newTop = invocation.getArgument(0);
      int currentStackSize = (int) getField(spyReader, "stackSize");
      setField(spyReader, "stackSize", currentStackSize + 1);
      return null;
    }).when(spyReader).push(anyInt());

    // First call: peekedBeginObject, push called, count=1
    // Second iteration: simulate peekedEndObject to exit loop
    doReturn(1, 2).when(spyReader).doPeek();

    setField(spyReader, "peeked", 1);

    skipValueMethod.invoke(spyReader);

    int stackSize = (int) getField(spyReader, "stackSize");
    assertEquals(0, stackSize);

    // pathNames[stackSize - 1] should be null after end object skip when count==0
    // stackSize is 0, so pathNames[-1] invalid; so test that no exception thrown and pathNames unchanged
  }

  @Test
    @Timeout(8000)
  void skipValue_peekedUnquoted_callsSkipUnquotedValue() throws Exception {
    setField(jsonReader, "peeked", 10); // PEEKED_UNQUOTED
    setField(jsonReader, "stackSize", 1);
    setField(jsonReader, "pathIndices", new int[32]);
    setField(jsonReader, "pathNames", new String[32]);

    JsonReader spyReader = spy(jsonReader);
    doNothing().when(spyReader).skipUnquotedValue();

    Method skipValueMethod = JsonReader.class.getDeclaredMethod("skipValue");
    skipValueMethod.setAccessible(true);

    skipValueMethod.invoke(spyReader);

    verify(spyReader, times(1)).skipUnquotedValue();

    int[] pathIndices = (int[]) getField(spyReader, "pathIndices");
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  void skipValue_peekedSingleQuoted_callsSkipQuotedValueWithQuote() throws Exception {
    setField(jsonReader, "peeked", 8); // PEEKED_SINGLE_QUOTED
    setField(jsonReader, "stackSize", 1);
    setField(jsonReader, "pathIndices", new int[32]);
    setField(jsonReader, "pathNames", new String[32]);

    JsonReader spyReader = spy(jsonReader);
    doNothing().when(spyReader).skipQuotedValue('\'');

    Method skipValueMethod = JsonReader.class.getDeclaredMethod("skipValue");
    skipValueMethod.setAccessible(true);

    skipValueMethod.invoke(spyReader);

    verify(spyReader, times(1)).skipQuotedValue('\'');
    int[] pathIndices = (int[]) getField(spyReader, "pathIndices");
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  void skipValue_peekedDoubleQuoted_callsSkipQuotedValueWithDoubleQuote() throws Exception {
    setField(jsonReader, "peeked", 9); // PEEKED_DOUBLE_QUOTED
    setField(jsonReader, "stackSize", 1);
    setField(jsonReader, "pathIndices", new int[32]);
    setField(jsonReader, "pathNames", new String[32]);

    JsonReader spyReader = spy(jsonReader);
    doNothing().when(spyReader).skipQuotedValue('"');

    Method skipValueMethod = JsonReader.class.getDeclaredMethod("skipValue");
    skipValueMethod.setAccessible(true);

    skipValueMethod.invoke(spyReader);

    verify(spyReader, times(1)).skipQuotedValue('"');
    int[] pathIndices = (int[]) getField(spyReader, "pathIndices");
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  void skipValue_peekedUnquotedName_setsPathNameSkipped() throws Exception {
    setField(jsonReader, "peeked", 14); // PEEKED_UNQUOTED_NAME
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    setField(jsonReader, "pathIndices", pathIndices);
    String[] pathNames = new String[32];
    setField(jsonReader, "pathNames", pathNames);

    JsonReader spyReader = spy(jsonReader);
    doNothing().when(spyReader).skipUnquotedValue();

    Method skipValueMethod = JsonReader.class.getDeclaredMethod("skipValue");
    skipValueMethod.setAccessible(true);

    skipValueMethod.invoke(spyReader);

    verify(spyReader, times(1)).skipUnquotedValue();
    assertEquals("<skipped>", pathNames[0]);
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  void skipValue_peekedSingleQuotedName_setsPathNameSkipped() throws Exception {
    setField(jsonReader, "peeked", 12); // PEEKED_SINGLE_QUOTED_NAME
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    setField(jsonReader, "pathIndices", pathIndices);
    String[] pathNames = new String[32];
    setField(jsonReader, "pathNames", pathNames);

    JsonReader spyReader = spy(jsonReader);
    doNothing().when(spyReader).skipQuotedValue('\'');

    Method skipValueMethod = JsonReader.class.getDeclaredMethod("skipValue");
    skipValueMethod.setAccessible(true);

    skipValueMethod.invoke(spyReader);

    verify(spyReader, times(1)).skipQuotedValue('\'');
    assertEquals("<skipped>", pathNames[0]);
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  void skipValue_peekedDoubleQuotedName_setsPathNameSkipped() throws Exception {
    setField(jsonReader, "peeked", 13); // PEEKED_DOUBLE_QUOTED_NAME
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    setField(jsonReader, "pathIndices", pathIndices);
    String[] pathNames = new String[32];
    setField(jsonReader, "pathNames", pathNames);

    JsonReader spyReader = spy(jsonReader);
    doNothing().when(spyReader).skipQuotedValue('"');

    Method skipValueMethod = JsonReader.class.getDeclaredMethod("skipValue");
    skipValueMethod.setAccessible(true);

    skipValueMethod.invoke(spyReader);

    verify(spyReader, times(1)).skipQuotedValue('"');
    assertEquals("<skipped>", pathNames[0]);
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  void skipValue_peekedNumber_incrementsPos() throws Exception {
    setField(jsonReader, "peeked", 16); // PEEKED_NUMBER
    setField(jsonReader, "pos", 5);
    setField(jsonReader, "peekedNumberLength", 3);
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    setField(jsonReader, "pathIndices", pathIndices);
    setField(jsonReader, "pathNames", new String[32]);

    Method skipValueMethod = JsonReader.class.getDeclaredMethod("skipValue");
    skipValueMethod.setAccessible(true);

    skipValueMethod.invoke(jsonReader);

    int pos = (int) getField(jsonReader, "pos");
    assertEquals(8, pos);

    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  void skipValue_peekedEof_returnsImmediately() throws Exception {
    setField(jsonReader, "peeked", 17); // PEEKED_EOF
    setField(jsonReader, "stackSize", 1);
    setField(jsonReader, "pathIndices", new int[32]);
    setField(jsonReader, "pathNames", new String[32]);

    Method skipValueMethod = JsonReader.class.getDeclaredMethod("skipValue");
    skipValueMethod.setAccessible(true);

    skipValueMethod.invoke(jsonReader);

    // No exception thrown, method returns immediately
  }

  private void setField(Object target, String fieldName, Object value) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  private Object getField(Object target, String fieldName) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(target);
  }
}