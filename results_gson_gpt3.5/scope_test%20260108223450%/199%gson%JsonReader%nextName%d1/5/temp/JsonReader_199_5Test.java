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

public class JsonReader_199_5Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  public void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  @Test
    @Timeout(8000)
  public void nextName_peekedNone_callsDoPeekAndReturnsUnquotedName() throws Exception {
    setPeeked(JsonReader.PEEKED_NONE);
    // Mock doPeek to return PEEKED_UNQUOTED_NAME
    setDoPeekReturnValue(JsonReader.PEEKED_UNQUOTED_NAME);
    // Mock nextUnquotedValue to return "unquotedName"
    setNextUnquotedValueReturn("unquotedName");

    String name = jsonReader.nextName();

    assertEquals("unquotedName", name);
    assertEquals(JsonReader.PEEKED_NONE, getPeeked());
    assertEquals("unquotedName", getPathNames()[getStackSize() - 1]);
  }

  @Test
    @Timeout(8000)
  public void nextName_peekedUnquotedName_returnsUnquotedName() throws Exception {
    setPeeked(JsonReader.PEEKED_UNQUOTED_NAME);
    setNextUnquotedValueReturn("nameUnquoted");

    String name = jsonReader.nextName();

    assertEquals("nameUnquoted", name);
    assertEquals(JsonReader.PEEKED_NONE, getPeeked());
    assertEquals("nameUnquoted", getPathNames()[getStackSize() - 1]);
  }

  @Test
    @Timeout(8000)
  public void nextName_peekedSingleQuotedName_returnsQuotedValueSingleQuote() throws Exception {
    setPeeked(JsonReader.PEEKED_SINGLE_QUOTED_NAME);
    setNextQuotedValueReturn('\'' , "nameSingleQuoted");

    String name = jsonReader.nextName();

    assertEquals("nameSingleQuoted", name);
    assertEquals(JsonReader.PEEKED_NONE, getPeeked());
    assertEquals("nameSingleQuoted", getPathNames()[getStackSize() - 1]);
  }

  @Test
    @Timeout(8000)
  public void nextName_peekedDoubleQuotedName_returnsQuotedValueDoubleQuote() throws Exception {
    setPeeked(JsonReader.PEEKED_DOUBLE_QUOTED_NAME);
    setNextQuotedValueReturn('"', "nameDoubleQuoted");

    String name = jsonReader.nextName();

    assertEquals("nameDoubleQuoted", name);
    assertEquals(JsonReader.PEEKED_NONE, getPeeked());
    assertEquals("nameDoubleQuoted", getPathNames()[getStackSize() - 1]);
  }

  @Test
    @Timeout(8000)
  public void nextName_invalidPeeked_throwsIllegalStateException() throws Exception {
    setPeeked(JsonReader.PEEKED_BEGIN_OBJECT); // invalid for nextName

    IllegalStateException ex = assertThrows(IllegalStateException.class, () -> jsonReader.nextName());
    assertTrue(ex.getMessage().startsWith("Expected a name but was "));
  }

  // Helper methods to set private fields and mock private methods

  private void setPeeked(int value) throws Exception {
    setField("peeked", value);
  }

  private int getPeeked() throws Exception {
    return (int) getField("peeked");
  }

  private void setDoPeekReturnValue(int value) throws Exception {
    Method doPeek = JsonReader.class.getDeclaredMethod("doPeek");
    doPeek.setAccessible(true);
    // Create spy to override doPeek return value
    JsonReader spyReader = spy(jsonReader);
    doReturn(value).when(spyReader).doPeek();
    // Replace jsonReader with spyReader for test
    this.jsonReader = spyReader;
  }

  private void setNextUnquotedValueReturn(String returnValue) throws Exception {
    Method nextUnquotedValue = JsonReader.class.getDeclaredMethod("nextUnquotedValue");
    nextUnquotedValue.setAccessible(true);
    JsonReader spyReader = spy(jsonReader);
    doReturn(returnValue).when(spyReader).nextUnquotedValue();
    this.jsonReader = spyReader;
  }

  private void setNextQuotedValueReturn(char quote, String returnValue) throws Exception {
    Method nextQuotedValue = JsonReader.class.getDeclaredMethod("nextQuotedValue", char.class);
    nextQuotedValue.setAccessible(true);
    JsonReader spyReader = spy(jsonReader);
    doReturn(returnValue).when(spyReader).nextQuotedValue(quote);
    this.jsonReader = spyReader;
  }

  private String[] getPathNames() throws Exception {
    return (String[]) getField("pathNames");
  }

  private int getStackSize() throws Exception {
    return (int) getField("stackSize");
  }

  private Object getField(String fieldName) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(jsonReader);
  }

  private void setField(String fieldName, Object value) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(jsonReader, value);
  }
}