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
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class JsonReader_211_1Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  @Test
    @Timeout(8000)
  void skipValue_peekedNone_doPeekReturnsBeginArray_shouldPushAndSkip() throws Exception {
    setField(jsonReader, "peeked", 0);
    setMethodReturnValue(jsonReader, "doPeek", JsonReader.class.getDeclaredField("PEEKED_BEGIN_ARRAY").getInt(null));

    setField(jsonReader, "stackSize", 0);
    setField(jsonReader, "pathIndices", new int[32]);
    setField(jsonReader, "pathNames", new String[32]);

    jsonReader.skipValue();

    assertEquals(1, getField(jsonReader, "stackSize"));
    assertEquals(1, ((int[]) getField(jsonReader, "pathIndices"))[0]);
  }

  @Test
    @Timeout(8000)
  void skipValue_peekedNone_doPeekReturnsBeginObject_shouldPushAndSkip() throws Exception {
    setField(jsonReader, "peeked", 0);
    setMethodReturnValue(jsonReader, "doPeek", JsonReader.class.getDeclaredField("PEEKED_BEGIN_OBJECT").getInt(null));

    setField(jsonReader, "stackSize", 0);
    setField(jsonReader, "pathIndices", new int[32]);
    setField(jsonReader, "pathNames", new String[32]);

    jsonReader.skipValue();

    assertEquals(1, getField(jsonReader, "stackSize"));
    assertEquals(1, ((int[]) getField(jsonReader, "pathIndices"))[0]);
  }

  @Test
    @Timeout(8000)
  void skipValue_peekedEndArray_shouldDecreaseStackSizeAndCount() throws Exception {
    setField(jsonReader, "peeked", JsonReader.class.getDeclaredField("PEEKED_END_ARRAY").getInt(null));
    setField(jsonReader, "stackSize", 1);
    setField(jsonReader, "pathIndices", new int[32]);
    setField(jsonReader, "pathNames", new String[32]);

    jsonReader.skipValue();

    assertEquals(0, getField(jsonReader, "stackSize"));
  }

  @Test
    @Timeout(8000)
  void skipValue_peekedEndObject_countZero_shouldClearPathNameAndDecreaseStackSize() throws Exception {
    setField(jsonReader, "peeked", JsonReader.class.getDeclaredField("PEEKED_END_OBJECT").getInt(null));
    setField(jsonReader, "stackSize", 1);
    setField(jsonReader, "pathIndices", new int[32]);
    String[] pathNames = new String[32];
    pathNames[0] = "something";
    setField(jsonReader, "pathNames", pathNames);

    // count is 0 in first iteration, so pathNames[stackSize-1] should be null after skipValue
    jsonReader.skipValue();

    assertEquals(0, getField(jsonReader, "stackSize"));
    assertNull(((String[]) getField(jsonReader, "pathNames"))[0]);
  }

  @Test
    @Timeout(8000)
  void skipValue_peekedUnquoted_shouldInvokeSkipUnquotedValue() throws Exception {
    setField(jsonReader, "peeked", JsonReader.class.getDeclaredField("PEEKED_UNQUOTED").getInt(null));

    JsonReader spyReader = Mockito.spy(jsonReader);
    doNothing().when(spyReader).skipUnquotedValue();

    setField(spyReader, "stackSize", 1);
    setField(spyReader, "pathIndices", new int[32]);
    setField(spyReader, "pathNames", new String[32]);

    spyReader.skipValue();

    verify(spyReader, times(1)).skipUnquotedValue();
  }

  @Test
    @Timeout(8000)
  void skipValue_peekedSingleQuoted_shouldInvokeSkipQuotedValueWithSingleQuote() throws Exception {
    setField(jsonReader, "peeked", JsonReader.class.getDeclaredField("PEEKED_SINGLE_QUOTED").getInt(null));

    JsonReader spyReader = Mockito.spy(jsonReader);
    doNothing().when(spyReader).skipQuotedValue('\'');

    setField(spyReader, "stackSize", 1);
    setField(spyReader, "pathIndices", new int[32]);
    setField(spyReader, "pathNames", new String[32]);

    spyReader.skipValue();

    verify(spyReader, times(1)).skipQuotedValue('\'');
  }

  @Test
    @Timeout(8000)
  void skipValue_peekedDoubleQuoted_shouldInvokeSkipQuotedValueWithDoubleQuote() throws Exception {
    setField(jsonReader, "peeked", JsonReader.class.getDeclaredField("PEEKED_DOUBLE_QUOTED").getInt(null));

    JsonReader spyReader = Mockito.spy(jsonReader);
    doNothing().when(spyReader).skipQuotedValue('"');

    setField(spyReader, "stackSize", 1);
    setField(spyReader, "pathIndices", new int[32]);
    setField(spyReader, "pathNames", new String[32]);

    spyReader.skipValue();

    verify(spyReader, times(1)).skipQuotedValue('"');
  }

  @Test
    @Timeout(8000)
  void skipValue_peekedUnquotedName_countZero_shouldSetPathNameSkipped() throws Exception {
    setField(jsonReader, "peeked", JsonReader.class.getDeclaredField("PEEKED_UNQUOTED_NAME").getInt(null));

    JsonReader spyReader = Mockito.spy(jsonReader);
    doNothing().when(spyReader).skipUnquotedValue();

    setField(spyReader, "stackSize", 1);
    setField(spyReader, "pathIndices", new int[32]);
    String[] pathNames = new String[32];
    setField(spyReader, "pathNames", pathNames);

    spyReader.skipValue();

    assertEquals("<skipped>", ((String[]) getField(spyReader, "pathNames"))[0]);
  }

  @Test
    @Timeout(8000)
  void skipValue_peekedSingleQuotedName_countZero_shouldSetPathNameSkipped() throws Exception {
    setField(jsonReader, "peeked", JsonReader.class.getDeclaredField("PEEKED_SINGLE_QUOTED_NAME").getInt(null));

    JsonReader spyReader = Mockito.spy(jsonReader);
    doNothing().when(spyReader).skipQuotedValue('\'');

    setField(spyReader, "stackSize", 1);
    setField(spyReader, "pathIndices", new int[32]);
    String[] pathNames = new String[32];
    setField(spyReader, "pathNames", pathNames);

    spyReader.skipValue();

    assertEquals("<skipped>", ((String[]) getField(spyReader, "pathNames"))[0]);
  }

  @Test
    @Timeout(8000)
  void skipValue_peekedDoubleQuotedName_countZero_shouldSetPathNameSkipped() throws Exception {
    setField(jsonReader, "peeked", JsonReader.class.getDeclaredField("PEEKED_DOUBLE_QUOTED_NAME").getInt(null));

    JsonReader spyReader = Mockito.spy(jsonReader);
    doNothing().when(spyReader).skipQuotedValue('"');

    setField(spyReader, "stackSize", 1);
    setField(spyReader, "pathIndices", new int[32]);
    String[] pathNames = new String[32];
    setField(spyReader, "pathNames", pathNames);

    spyReader.skipValue();

    assertEquals("<skipped>", ((String[]) getField(spyReader, "pathNames"))[0]);
  }

  @Test
    @Timeout(8000)
  void skipValue_peekedNumber_shouldAdvancePosByPeekedNumberLength() throws Exception {
    setField(jsonReader, "peeked", JsonReader.class.getDeclaredField("PEEKED_NUMBER").getInt(null));
    setField(jsonReader, "peekedNumberLength", 5);
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "stackSize", 1);
    setField(jsonReader, "pathIndices", new int[32]);
    setField(jsonReader, "pathNames", new String[32]);

    jsonReader.skipValue();

    assertEquals(5, getField(jsonReader, "pos"));
  }

  @Test
    @Timeout(8000)
  void skipValue_peekedEOF_shouldReturnImmediately() throws Exception {
    setField(jsonReader, "peeked", JsonReader.class.getDeclaredField("PEEKED_EOF").getInt(null));
    setField(jsonReader, "stackSize", 1);
    setField(jsonReader, "pathIndices", new int[32]);
    setField(jsonReader, "pathNames", new String[32]);

    jsonReader.skipValue();

    // stackSize and pathIndices should be unchanged
    assertEquals(1, getField(jsonReader, "stackSize"));
    assertEquals(0, ((int[]) getField(jsonReader, "pathIndices"))[0]);
  }

  // Helper methods for reflection

  private static void setField(Object target, String fieldName, Object value) throws Exception {
    java.lang.reflect.Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  private static Object getField(Object target, String fieldName) throws Exception {
    java.lang.reflect.Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(target);
  }

  private static void setMethodReturnValue(Object target, String methodName, Object returnValue) throws Exception {
    Method method = JsonReader.class.getDeclaredMethod(methodName);
    method.setAccessible(true);

    JsonReader spy = Mockito.spy(target);
    Mockito.doReturn(returnValue).when(spy).doPeek();
  }
}