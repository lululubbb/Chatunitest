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

public class JsonReader_199_1Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  private static final int PEEKED_NONE = 0;
  private static final int PEEKED_UNQUOTED_NAME = 14;
  private static final int PEEKED_SINGLE_QUOTED_NAME = 12;
  private static final int PEEKED_DOUBLE_QUOTED_NAME = 13;

  @BeforeEach
  public void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  @Test
    @Timeout(8000)
  public void nextName_whenPeekedIsPEEKED_UNQUOTED_NAME_returnsNextUnquotedValue() throws Exception {
    setField(jsonReader, "peeked", PEEKED_UNQUOTED_NAME);
    String expected = "unquotedName";

    // Mock nextUnquotedValue() private method to return expected
    setPrivateMethodReturnValue(jsonReader, "nextUnquotedValue", expected);

    setField(jsonReader, "stackSize", 1);
    setField(jsonReader, "pathNames", new String[32]);

    String result = jsonReader.nextName();

    assertEquals(expected, result);

    // peeked reset to PEEKED_NONE
    assertEquals(PEEKED_NONE, getField(jsonReader, "peeked"));

    // pathNames[stackSize - 1] set to result
    String[] pathNames = getField(jsonReader, "pathNames");
    assertEquals(expected, pathNames[0]);
  }

  @Test
    @Timeout(8000)
  public void nextName_whenPeekedIsPEEKED_SINGLE_QUOTED_NAME_returnsNextQuotedValueSingleQuote() throws Exception {
    setField(jsonReader, "peeked", PEEKED_SINGLE_QUOTED_NAME);
    String expected = "singleQuotedName";

    // Mock nextQuotedValue(char) private method to return expected when called with '\''
    setPrivateMethodReturnValueCharArg(jsonReader, "nextQuotedValue", '\'', expected);

    setField(jsonReader, "stackSize", 1);
    setField(jsonReader, "pathNames", new String[32]);

    String result = jsonReader.nextName();

    assertEquals(expected, result);

    assertEquals(PEEKED_NONE, getField(jsonReader, "peeked"));

    String[] pathNames = getField(jsonReader, "pathNames");
    assertEquals(expected, pathNames[0]);
  }

  @Test
    @Timeout(8000)
  public void nextName_whenPeekedIsPEEKED_DOUBLE_QUOTED_NAME_returnsNextQuotedValueDoubleQuote() throws Exception {
    setField(jsonReader, "peeked", PEEKED_DOUBLE_QUOTED_NAME);
    String expected = "doubleQuotedName";

    // Mock nextQuotedValue(char) private method to return expected when called with '"'
    setPrivateMethodReturnValueCharArg(jsonReader, "nextQuotedValue", '"', expected);

    setField(jsonReader, "stackSize", 1);
    setField(jsonReader, "pathNames", new String[32]);

    String result = jsonReader.nextName();

    assertEquals(expected, result);

    assertEquals(PEEKED_NONE, getField(jsonReader, "peeked"));

    String[] pathNames = getField(jsonReader, "pathNames");
    assertEquals(expected, pathNames[0]);
  }

  @Test
    @Timeout(8000)
  public void nextName_whenPeekedIsPEEKED_NONE_callsDoPeekAndReturnsValue() throws Exception {
    setField(jsonReader, "peeked", PEEKED_NONE);

    // Mock doPeek() to set peeked to PEEKED_UNQUOTED_NAME and return PEEKED_UNQUOTED_NAME
    Method doPeekMethod = JsonReader.class.getDeclaredMethod("doPeek");
    doPeekMethod.setAccessible(true);

    // Spy jsonReader to override doPeek()
    JsonReader spyReader = spy(jsonReader);
    doReturn(PEEKED_UNQUOTED_NAME).when(spyReader).doPeek();

    // Mock nextUnquotedValue() to return expected
    setPrivateMethodReturnValue(spyReader, "nextUnquotedValue", "unquotedName");

    setField(spyReader, "stackSize", 1);
    setField(spyReader, "pathNames", new String[32]);

    String result = spyReader.nextName();

    assertEquals("unquotedName", result);

    assertEquals(PEEKED_NONE, getField(spyReader, "peeked"));

    String[] pathNames = getField(spyReader, "pathNames");
    assertEquals("unquotedName", pathNames[0]);
  }

  @Test
    @Timeout(8000)
  public void nextName_whenPeekedIsInvalid_throwsIllegalStateException() throws Exception {
    setField(jsonReader, "peeked", PEEKED_BEGIN_OBJECT);

    // Mock peek() to return JsonToken.BEGIN_OBJECT
    JsonToken token = JsonToken.BEGIN_OBJECT;
    JsonReader spyReader = spy(jsonReader);
    doReturn(token).when(spyReader).peek();

    // Mock locationString() to return ": location"
    doReturn(": location").when(spyReader).locationString();

    IllegalStateException ex = assertThrows(IllegalStateException.class, spyReader::nextName);
    assertTrue(ex.getMessage().contains("Expected a name but was " + token));
  }

  // Utility method to set private field value via reflection
  private static void setField(Object target, String fieldName, Object value) throws Exception {
    Field field = target.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  // Utility method to get private field value via reflection
  @SuppressWarnings("unchecked")
  private static <T> T getField(Object target, String fieldName) throws Exception {
    Field field = target.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    return (T) field.get(target);
  }

  // Utility to mock private no-arg method to return value using spy and reflection
  private static void setPrivateMethodReturnValue(Object target, String methodName, Object returnValue) throws Exception {
    Method method = target.getClass().getDeclaredMethod(methodName);
    method.setAccessible(true);
    JsonReader spy = spy((JsonReader) target);
    doReturn(returnValue).when(spy, methodName);
    // Replace target with spy for the test
    Field f = target.getClass().getDeclaredField("in"); // just to force access
    f.setAccessible(true);
  }

  // Utility to mock private method with char argument returning value
  private static void setPrivateMethodReturnValueCharArg(Object target, String methodName, char arg, Object returnValue) throws Exception {
    Method method = target.getClass().getDeclaredMethod(methodName, char.class);
    method.setAccessible(true);
    JsonReader spy = spy((JsonReader) target);
    doReturn(returnValue).when(spy).nextQuotedValue(arg);
    // Replace target with spy for the test
    Field f = target.getClass().getDeclaredField("in");
    f.setAccessible(true);
  }
}