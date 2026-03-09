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

class JsonReader_201_2Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  @Test
    @Timeout(8000)
  void nextBoolean_whenPeekedIsPEEKED_TRUE_returnsTrueAndAdvances() throws Exception {
    setPeekedField(getPeekedConstant("PEEKED_TRUE"));
    setStackSize(1);
    setPathIndices(new int[]{0});

    boolean result = jsonReader.nextBoolean();

    assertTrue(result);
    assertEquals(getPeekedConstant("PEEKED_NONE"), getPeekedField());
    assertEquals(1, getPathIndices()[0]);
  }

  @Test
    @Timeout(8000)
  void nextBoolean_whenPeekedIsPEEKED_FALSE_returnsFalseAndAdvances() throws Exception {
    setPeekedField(getPeekedConstant("PEEKED_FALSE"));
    setStackSize(1);
    setPathIndices(new int[]{0});

    boolean result = jsonReader.nextBoolean();

    assertFalse(result);
    assertEquals(getPeekedConstant("PEEKED_NONE"), getPeekedField());
    assertEquals(1, getPathIndices()[0]);
  }

  @Test
    @Timeout(8000)
  void nextBoolean_whenPeekedIsPEEKED_NONEAndDoPeekReturnsTrue() throws Exception {
    setPeekedField(getPeekedConstant("PEEKED_NONE"));
    setStackSize(1);
    setPathIndices(new int[]{0});

    JsonReader spyReader = spy(jsonReader);
    doReturn(getPeekedConstant("PEEKED_TRUE")).when(spyReader).doPeek();

    boolean result = spyReader.nextBoolean();

    assertTrue(result);
    assertEquals(getPeekedConstant("PEEKED_NONE"), getPeekedField(spyReader));
    assertEquals(1, getPathIndices(spyReader)[0]);
  }

  @Test
    @Timeout(8000)
  void nextBoolean_whenPeekedIsNotBoolean_throwsIllegalStateException() throws Exception {
    setPeekedField(getPeekedConstant("PEEKED_BEGIN_OBJECT"));
    setStackSize(1);
    setPathIndices(new int[]{0});

    JsonReader spyReader = spy(jsonReader);
    doReturn(getPeekedConstant("PEEKED_BEGIN_OBJECT")).when(spyReader).doPeek();
    doReturn(JsonToken.BEGIN_OBJECT).when(spyReader).peek();
    doReturn(" at path $").when(spyReader).locationString();

    IllegalStateException thrown = assertThrows(IllegalStateException.class, spyReader::nextBoolean);
    assertTrue(thrown.getMessage().contains("Expected a boolean but was"));
  }

  // Helper methods to access private fields and constants

  private int getPeekedConstant(String name) throws Exception {
    Field f = JsonReader.class.getDeclaredField(name);
    f.setAccessible(true);
    return f.getInt(null);
  }

  private void setPeekedField(int value) throws Exception {
    Field f = JsonReader.class.getDeclaredField("peeked");
    f.setAccessible(true);
    f.setInt(jsonReader, value);
  }

  private int getPeekedField() throws Exception {
    Field f = JsonReader.class.getDeclaredField("peeked");
    f.setAccessible(true);
    return f.getInt(jsonReader);
  }

  private int[] getPathIndices() throws Exception {
    Field f = JsonReader.class.getDeclaredField("pathIndices");
    f.setAccessible(true);
    return (int[]) f.get(jsonReader);
  }

  private void setPathIndices(int[] indices) throws Exception {
    Field f = JsonReader.class.getDeclaredField("pathIndices");
    f.setAccessible(true);
    f.set(jsonReader, indices);
  }

  private void setStackSize(int size) throws Exception {
    Field f = JsonReader.class.getDeclaredField("stackSize");
    f.setAccessible(true);
    f.setInt(jsonReader, size);
  }

  private int getPeekedField(JsonReader instance) throws Exception {
    Field f = JsonReader.class.getDeclaredField("peeked");
    f.setAccessible(true);
    return f.getInt(instance);
  }

  private int[] getPathIndices(JsonReader instance) throws Exception {
    Field f = JsonReader.class.getDeclaredField("pathIndices");
    f.setAccessible(true);
    return (int[]) f.get(instance);
  }
}