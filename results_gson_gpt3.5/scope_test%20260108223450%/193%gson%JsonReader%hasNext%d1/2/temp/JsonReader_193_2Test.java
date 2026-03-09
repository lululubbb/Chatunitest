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

public class JsonReader_193_2Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  private static final int PEEKED_NONE = 0;
  private static final int PEEKED_END_OBJECT = 2;
  private static final int PEEKED_END_ARRAY = 4;
  private static final int PEEKED_EOF = 17;

  @BeforeEach
  public void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  @Test
    @Timeout(8000)
  public void hasNext_peekedNotNoneAndNotEndTokens_returnsTrue() throws IOException, Exception {
    setPeeked(PEEKED_END_OBJECT - 1); // 1 is PEEKED_BEGIN_OBJECT, which is not an end token
    assertTrue(jsonReader.hasNext());
  }

  @Test
    @Timeout(8000)
  public void hasNext_peekedIsEndObject_returnsFalse() throws IOException, Exception {
    setPeeked(PEEKED_END_OBJECT);
    assertFalse(jsonReader.hasNext());
  }

  @Test
    @Timeout(8000)
  public void hasNext_peekedIsEndArray_returnsFalse() throws IOException, Exception {
    setPeeked(PEEKED_END_ARRAY);
    assertFalse(jsonReader.hasNext());
  }

  @Test
    @Timeout(8000)
  public void hasNext_peekedIsEof_returnsFalse() throws IOException, Exception {
    setPeeked(PEEKED_EOF);
    assertFalse(jsonReader.hasNext());
  }

  @Test
    @Timeout(8000)
  public void hasNext_peekedIsNone_callsDoPeekAndReturnsCorrect() throws Exception {
    setPeeked(PEEKED_NONE);

    // Mock doPeek() to return a non-end token (e.g. PEEKED_BEGIN_OBJECT)
    setDoPeekReturnValue(1);
    assertTrue(jsonReader.hasNext());

    // Mock doPeek() to return PEEKED_END_OBJECT
    setDoPeekReturnValue(PEEKED_END_OBJECT);
    assertFalse(jsonReader.hasNext());

    // Mock doPeek() to return PEEKED_END_ARRAY
    setDoPeekReturnValue(PEEKED_END_ARRAY);
    assertFalse(jsonReader.hasNext());

    // Mock doPeek() to return PEEKED_EOF
    setDoPeekReturnValue(PEEKED_EOF);
    assertFalse(jsonReader.hasNext());
  }

  private void setPeeked(int value) throws Exception {
    Field peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    peekedField.setInt(jsonReader, value);
  }

  private void setDoPeekReturnValue(int returnValue) throws Exception {
    Method doPeekMethod = JsonReader.class.getDeclaredMethod("doPeek");
    doPeekMethod.setAccessible(true);

    // Create a spy to override doPeek()
    JsonReader spyReader = spy(jsonReader);
    doReturn(returnValue).when(spyReader).doPeek();

    // Replace the jsonReader instance with spyReader for testing hasNext
    Field peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    peekedField.set(spyReader, PEEKED_NONE);

    this.jsonReader = spyReader;
  }
}