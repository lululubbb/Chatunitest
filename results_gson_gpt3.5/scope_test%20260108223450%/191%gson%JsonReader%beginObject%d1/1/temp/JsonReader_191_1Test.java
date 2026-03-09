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

public class JsonReader_191_1Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  public void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  @Test
    @Timeout(8000)
  public void beginObject_peekedIsPeekedBeginObject_pushesScopeAndResetsPeeked() throws Exception {
    setPeekedField(jsonReader, 1); // PEEKED_BEGIN_OBJECT

    jsonReader.beginObject();

    int peekedAfter = getPeekedField(jsonReader);
    assertEquals(0, peekedAfter, "peeked field should be reset to PEEKED_NONE");

    int[] stack = getStackField(jsonReader);
    int stackSize = getStackSizeField(jsonReader);
    assertTrue(stackSize > 0, "stackSize should be greater than 0 after push");
    assertEquals(JsonScope.EMPTY_OBJECT, stack[stackSize - 1], "top of stack should be EMPTY_OBJECT");
  }

  @Test
    @Timeout(8000)
  public void beginObject_peekedIsPeekedNone_callsDoPeekAndBehavesAsPeekedBeginObject() throws Exception {
    setPeekedField(jsonReader, 0); // PEEKED_NONE

    // Mock doPeek to return PEEKED_BEGIN_OBJECT (1)
    Method doPeek = JsonReader.class.getDeclaredMethod("doPeek");
    doPeek.setAccessible(true);

    JsonReader spyReader = spy(jsonReader);
    doReturn(1).when(spyReader).doPeek();

    spyReader.beginObject();

    int peekedAfter = getPeekedField(spyReader);
    assertEquals(0, peekedAfter, "peeked field should be reset to PEEKED_NONE");

    int[] stack = getStackField(spyReader);
    int stackSize = getStackSizeField(spyReader);
    assertTrue(stackSize > 0, "stackSize should be greater than 0 after push");
    assertEquals(JsonScope.EMPTY_OBJECT, stack[stackSize - 1], "top of stack should be EMPTY_OBJECT");
  }

  @Test
    @Timeout(8000)
  public void beginObject_peekedIsNotBeginObject_throwsIllegalStateException() throws Exception {
    setPeekedField(jsonReader, 5); // PEEKED_TRUE (not PEEKED_BEGIN_OBJECT)

    // Mock peek() to return a JsonToken for error message
    JsonToken mockToken = JsonToken.BOOLEAN;
    JsonReader spyReader = spy(jsonReader);
    doReturn(mockToken).when(spyReader).peek();

    // Mock locationString() to return some location info
    doReturn(" at line 1 column 1 path $").when(spyReader).locationString();

    IllegalStateException ex = assertThrows(IllegalStateException.class, spyReader::beginObject);
    assertTrue(ex.getMessage().contains("Expected BEGIN_OBJECT but was " + mockToken.toString()));
    assertTrue(ex.getMessage().contains("at line 1 column 1 path $"));
  }

  // Helper to set private int peeked field
  private void setPeekedField(JsonReader reader, int value) throws Exception {
    Field peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    peekedField.setInt(reader, value);
  }

  // Helper to get private int peeked field
  private int getPeekedField(JsonReader reader) throws Exception {
    Field peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    return peekedField.getInt(reader);
  }

  // Helper to get private int[] stack field
  private int[] getStackField(JsonReader reader) throws Exception {
    Field stackField = JsonReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    return (int[]) stackField.get(reader);
  }

  // Helper to get private int stackSize field
  private int getStackSizeField(JsonReader reader) throws Exception {
    Field stackSizeField = JsonReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    return stackSizeField.getInt(reader);
  }
}