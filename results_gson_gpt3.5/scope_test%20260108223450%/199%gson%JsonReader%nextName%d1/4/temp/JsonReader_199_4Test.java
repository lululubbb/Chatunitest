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

public class JsonReader_199_4Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  public void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  @Test
    @Timeout(8000)
  public void testNextName_PeekedNone_callsDoPeekAndReturnsUnquotedName() throws Exception {
    setPeeked(JsonReader.PEEKED_NONE);
    // Mock doPeek to return PEEKED_UNQUOTED_NAME
    setDoPeekReturnValue(JsonReader.PEEKED_UNQUOTED_NAME);
    // Mock nextUnquotedValue to return "unquotedName"
    setNextUnquotedValueReturn("unquotedName");

    String result = jsonReader.nextName();

    assertEquals("unquotedName", result);
    assertEquals(JsonReader.PEEKED_NONE, getPeeked());
    assertEquals("unquotedName", getPathNameAtStackTop());
  }

  @Test
    @Timeout(8000)
  public void testNextName_PeekedUnquotedName_returnsUnquotedName() throws Exception {
    setPeeked(JsonReader.PEEKED_UNQUOTED_NAME);
    setNextUnquotedValueReturn("unquotedName2");

    String result = jsonReader.nextName();

    assertEquals("unquotedName2", result);
    assertEquals(JsonReader.PEEKED_NONE, getPeeked());
    assertEquals("unquotedName2", getPathNameAtStackTop());
  }

  @Test
    @Timeout(8000)
  public void testNextName_PeekedSingleQuotedName_returnsQuotedValue() throws Exception {
    setPeeked(JsonReader.PEEKED_SINGLE_QUOTED_NAME);
    setNextQuotedValueReturn("singleQuotedName", '\'');

    String result = jsonReader.nextName();

    assertEquals("singleQuotedName", result);
    assertEquals(JsonReader.PEEKED_NONE, getPeeked());
    assertEquals("singleQuotedName", getPathNameAtStackTop());
  }

  @Test
    @Timeout(8000)
  public void testNextName_PeekedDoubleQuotedName_returnsQuotedValue() throws Exception {
    setPeeked(JsonReader.PEEKED_DOUBLE_QUOTED_NAME);
    setNextQuotedValueReturn("doubleQuotedName", '"');

    String result = jsonReader.nextName();

    assertEquals("doubleQuotedName", result);
    assertEquals(JsonReader.PEEKED_NONE, getPeeked());
    assertEquals("doubleQuotedName", getPathNameAtStackTop());
  }

  @Test
    @Timeout(8000)
  public void testNextName_InvalidPeeked_throwsIllegalStateException() throws Exception {
    setPeeked(JsonReader.PEEKED_BEGIN_OBJECT); // invalid for nextName

    Exception e = assertThrows(IllegalStateException.class, () -> jsonReader.nextName());
    assertTrue(e.getMessage().startsWith("Expected a name but was "));
  }

  // Helper methods to set private fields and invoke private methods

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

  private void setDoPeekReturnValue(int value) throws Exception {
    Method doPeekMethod = JsonReader.class.getDeclaredMethod("doPeek");
    doPeekMethod.setAccessible(true);

    // Create a spy to override doPeek
    JsonReader spyReader = spy(jsonReader);
    doReturn(value).when(spyReader).doPeek();

    Field inField = JsonReader.class.getDeclaredField("in");
    inField.setAccessible(true);
    inField.set(spyReader, mockReader);

    jsonReader = spyReader;
  }

  private void setNextUnquotedValueReturn(String returnValue) throws Exception {
    Method method = JsonReader.class.getDeclaredMethod("nextUnquotedValue");
    method.setAccessible(true);

    JsonReader spyReader = spy(jsonReader);
    doReturn(returnValue).when(spyReader).nextUnquotedValue();

    jsonReader = spyReader;
  }

  private void setNextQuotedValueReturn(String returnValue, char quote) throws Exception {
    Method method = JsonReader.class.getDeclaredMethod("nextQuotedValue", char.class);
    method.setAccessible(true);

    JsonReader spyReader = spy(jsonReader);
    doReturn(returnValue).when(spyReader).nextQuotedValue(quote);

    jsonReader = spyReader;
  }

  private String getPathNameAtStackTop() throws Exception {
    Field stackSizeField = JsonReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    int stackSize = stackSizeField.getInt(jsonReader);

    Field pathNamesField = JsonReader.class.getDeclaredField("pathNames");
    pathNamesField.setAccessible(true);
    String[] pathNames = (String[]) pathNamesField.get(jsonReader);

    return pathNames[stackSize - 1];
  }
}