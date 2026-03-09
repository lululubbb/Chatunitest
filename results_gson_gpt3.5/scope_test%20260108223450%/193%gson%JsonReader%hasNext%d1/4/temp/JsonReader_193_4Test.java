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

class JsonReader_193_4Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  private static final int PEEKED_NONE = 0;
  private static final int PEEKED_END_OBJECT = 2;
  private static final int PEEKED_END_ARRAY = 4;
  private static final int PEEKED_EOF = 17;

  @BeforeEach
  void setup() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  @Test
    @Timeout(8000)
  void hasNext_peekedIsNotNoneAndNotEndTokens_returnsTrue() throws Exception {
    setPeeked(1); // PEEKED_BEGIN_OBJECT (1), should return true
    assertTrue(jsonReader.hasNext());
    setPeeked(3); // PEEKED_BEGIN_ARRAY (3), should return true
    assertTrue(jsonReader.hasNext());
    setPeeked(5); // PEEKED_TRUE (5), should return true
    assertTrue(jsonReader.hasNext());
  }

  @Test
    @Timeout(8000)
  void hasNext_peekedIsNone_callsDoPeekAndReturnsCorrectly() throws Exception {
    setPeeked(PEEKED_NONE);

    // Use reflection to mock doPeek to return PEEKED_BEGIN_OBJECT (1)
    Method doPeekMethod = JsonReader.class.getDeclaredMethod("doPeek");
    doPeekMethod.setAccessible(true);

    // Spy on jsonReader to mock doPeek
    JsonReader spyReader = spy(jsonReader);
    doReturn(1).when(spyReader).doPeek();

    assertTrue(spyReader.hasNext());

    doReturn(PEEKED_END_OBJECT).when(spyReader).doPeek();
    assertFalse(spyReader.hasNext());

    doReturn(PEEKED_END_ARRAY).when(spyReader).doPeek();
    assertFalse(spyReader.hasNext());

    doReturn(PEEKED_EOF).when(spyReader).doPeek();
    assertFalse(spyReader.hasNext());
  }

  @Test
    @Timeout(8000)
  void hasNext_peekedIsEndTokens_returnsFalse() throws Exception {
    setPeeked(PEEKED_END_OBJECT);
    assertFalse(jsonReader.hasNext());
    setPeeked(PEEKED_END_ARRAY);
    assertFalse(jsonReader.hasNext());
    setPeeked(PEEKED_EOF);
    assertFalse(jsonReader.hasNext());
  }

  private void setPeeked(int value) throws Exception {
    Field peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    peekedField.setInt(jsonReader, value);
  }
}