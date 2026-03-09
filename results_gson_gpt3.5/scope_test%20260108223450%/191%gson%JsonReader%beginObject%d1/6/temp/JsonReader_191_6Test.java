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

public class JsonReader_191_6Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  public void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  @Test
    @Timeout(8000)
  public void beginObject_peekedIsPeekedBeginObject_pushCalledAndPeekedReset() throws Exception {
    setPeeked(1); // PEEKED_BEGIN_OBJECT = 1

    // We cannot override private methods, so instead we spy and verify stack changes
    JsonReader spyReader = spy(jsonReader);
    setField(spyReader, "peeked", 1);

    Method beginObjectMethod = JsonReader.class.getDeclaredMethod("beginObject");
    beginObjectMethod.setAccessible(true);
    beginObjectMethod.invoke(spyReader);

    int peekedAfter = getPeeked(spyReader);
    assertEquals(0, peekedAfter, "peeked should be reset to PEEKED_NONE after beginObject");

    Field stackField = JsonReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    int[] stack = (int[]) stackField.get(spyReader);

    Field stackSizeField = JsonReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    int stackSize = (int) stackSizeField.get(spyReader);

    assertTrue(stackSize > 0, "stackSize should be > 0 after push");
    assertEquals(JsonScope.EMPTY_OBJECT, stack[stackSize - 1], "top of stack should be EMPTY_OBJECT");
  }

  @Test
    @Timeout(8000)
  public void beginObject_peekedIsPeekedNone_callsDoPeekAndBehavesCorrectly() throws Exception {
    setPeeked(0); // PEEKED_NONE = 0

    JsonReader spyReader = spy(jsonReader);
    doReturn(1).when(spyReader).doPeek();

    Method beginObjectMethod = JsonReader.class.getDeclaredMethod("beginObject");
    beginObjectMethod.setAccessible(true);
    beginObjectMethod.invoke(spyReader);

    int peekedAfter = getPeeked(spyReader);
    assertEquals(0, peekedAfter, "peeked should be reset to PEEKED_NONE after beginObject");

    Field stackField = JsonReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    int[] stack = (int[]) stackField.get(spyReader);

    Field stackSizeField = JsonReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    int stackSize = (int) stackSizeField.get(spyReader);

    assertTrue(stackSize > 0, "stackSize should be > 0 after push");
    assertEquals(JsonScope.EMPTY_OBJECT, stack[stackSize - 1], "top of stack should be EMPTY_OBJECT");
  }

  @Test
    @Timeout(8000)
  public void beginObject_peekedIsNotBeginObject_throwsIllegalStateException() throws Exception {
    setPeeked(2); // PEEKED_END_OBJECT, not PEEKED_BEGIN_OBJECT

    JsonReader spyReader = spy(jsonReader);
    doReturn(JsonToken.END_OBJECT).when(spyReader).peek();
    doReturn(" at line 1 column 1").when(spyReader).locationString();

    Method beginObjectMethod = JsonReader.class.getDeclaredMethod("beginObject");
    beginObjectMethod.setAccessible(true);

    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
      try {
        beginObjectMethod.invoke(spyReader);
      } catch (java.lang.reflect.InvocationTargetException e) {
        throw e.getCause();
      }
    });

    assertTrue(thrown.getMessage().contains("Expected BEGIN_OBJECT but was"));
  }

  // Helper to set private field peeked
  private void setPeeked(int value) {
    try {
      Field peekedField = JsonReader.class.getDeclaredField("peeked");
      peekedField.setAccessible(true);
      peekedField.setInt(jsonReader, value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private int getPeeked(Object instance) throws Exception {
    Field peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    return peekedField.getInt(instance);
  }

  private void setField(Object instance, String fieldName, Object value) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(instance, value);
  }
}