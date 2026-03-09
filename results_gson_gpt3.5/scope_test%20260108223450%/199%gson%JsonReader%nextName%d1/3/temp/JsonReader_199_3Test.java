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

public class JsonReader_199_3Test {
  private JsonReader jsonReader;
  private Reader mockReader;

  private int PEEKED_NONE;
  private int PEEKED_UNQUOTED_NAME;
  private int PEEKED_SINGLE_QUOTED_NAME;
  private int PEEKED_DOUBLE_QUOTED_NAME;
  private int PEEKED_BEGIN_OBJECT;

  @BeforeEach
  void setUp() throws Exception {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);

    // Load private static final int constants via reflection
    PEEKED_NONE = getStaticIntField("PEEKED_NONE");
    PEEKED_UNQUOTED_NAME = getStaticIntField("PEEKED_UNQUOTED_NAME");
    PEEKED_SINGLE_QUOTED_NAME = getStaticIntField("PEEKED_SINGLE_QUOTED_NAME");
    PEEKED_DOUBLE_QUOTED_NAME = getStaticIntField("PEEKED_DOUBLE_QUOTED_NAME");
    PEEKED_BEGIN_OBJECT = getStaticIntField("PEEKED_BEGIN_OBJECT");
  }

  @Test
    @Timeout(8000)
  void nextName_peekedNone_callsDoPeekAndHandlesUnquotedName() throws Exception {
    setPeeked(PEEKED_NONE);
    // Mock doPeek to return PEEKED_UNQUOTED_NAME
    setDoPeekReturn(PEEKED_UNQUOTED_NAME);
    // Mock nextUnquotedValue to return a test string
    setNextUnquotedValue("unquotedName");

    String result = jsonReader.nextName();

    assertEquals("unquotedName", result);
    assertEquals(PEEKED_NONE, getPeeked());
    assertEquals("unquotedName", getPathNameAtStackTop());
  }

  @Test
    @Timeout(8000)
  void nextName_peekedUnquotedName_returnsNextUnquotedValue() throws Exception {
    setPeeked(PEEKED_UNQUOTED_NAME);
    setNextUnquotedValue("unquotedName2");

    String result = jsonReader.nextName();

    assertEquals("unquotedName2", result);
    assertEquals(PEEKED_NONE, getPeeked());
    assertEquals("unquotedName2", getPathNameAtStackTop());
  }

  @Test
    @Timeout(8000)
  void nextName_peekedSingleQuotedName_returnsNextQuotedValueWithSingleQuote() throws Exception {
    setPeeked(PEEKED_SINGLE_QUOTED_NAME);
    setNextQuotedValue('\'', "singleQuotedName");

    String result = jsonReader.nextName();

    assertEquals("singleQuotedName", result);
    assertEquals(PEEKED_NONE, getPeeked());
    assertEquals("singleQuotedName", getPathNameAtStackTop());
  }

  @Test
    @Timeout(8000)
  void nextName_peekedDoubleQuotedName_returnsNextQuotedValueWithDoubleQuote() throws Exception {
    setPeeked(PEEKED_DOUBLE_QUOTED_NAME);
    setNextQuotedValue('"', "doubleQuotedName");

    String result = jsonReader.nextName();

    assertEquals("doubleQuotedName", result);
    assertEquals(PEEKED_NONE, getPeeked());
    assertEquals("doubleQuotedName", getPathNameAtStackTop());
  }

  @Test
    @Timeout(8000)
  void nextName_invalidPeeked_throwsIllegalStateException() throws Exception {
    setPeeked(PEEKED_BEGIN_OBJECT); // invalid for nextName()

    IllegalStateException ex = assertThrows(IllegalStateException.class, () -> jsonReader.nextName());
    assertTrue(ex.getMessage().startsWith("Expected a name but was"));
  }

  // Helper methods to set/get private fields and invoke private methods

  private int getStaticIntField(String fieldName) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.getInt(null);
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

  private void setDoPeekReturn(int value) throws Exception {
    Method doPeekMethod = JsonReader.class.getDeclaredMethod("doPeek");
    doPeekMethod.setAccessible(true);

    // Create a spy to override doPeek
    JsonReader spyReader = spy(jsonReader);
    doReturn(value).when(spyReader).doPeek();

    // Replace jsonReader with spyReader
    this.jsonReader = spyReader;
  }

  private void setNextUnquotedValue(String returnValue) throws Exception {
    Method nextUnquotedValueMethod = JsonReader.class.getDeclaredMethod("nextUnquotedValue");
    nextUnquotedValueMethod.setAccessible(true);

    JsonReader spyReader = spy(jsonReader);
    doReturn(returnValue).when(spyReader).nextUnquotedValue();

    this.jsonReader = spyReader;
  }

  private void setNextQuotedValue(char quote, String returnValue) throws Exception {
    Method nextQuotedValueMethod = JsonReader.class.getDeclaredMethod("nextQuotedValue", char.class);
    nextQuotedValueMethod.setAccessible(true);

    JsonReader spyReader = spy(jsonReader);
    doReturn(returnValue).when(spyReader).nextQuotedValue(quote);

    this.jsonReader = spyReader;
  }

  private String getPathNameAtStackTop() throws Exception {
    Field pathNamesField = JsonReader.class.getDeclaredField("pathNames");
    pathNamesField.setAccessible(true);
    String[] pathNames = (String[]) pathNamesField.get(jsonReader);

    Field stackSizeField = JsonReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    int stackSize = stackSizeField.getInt(jsonReader);

    return pathNames[stackSize - 1];
  }

  @Test
    @Timeout(8000)
  void nextName_pathNamesUpdatedWithCorrectStackSize() throws Exception {
    setPeeked(PEEKED_UNQUOTED_NAME);
    setNextUnquotedValue("nameForPathTest");

    // Set stackSize to 1 manually
    Field stackSizeField = JsonReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonReader, 1);

    String result = jsonReader.nextName();

    assertEquals("nameForPathTest", result);
    assertEquals("nameForPathTest", getPathNameAtStackTop());
  }
}