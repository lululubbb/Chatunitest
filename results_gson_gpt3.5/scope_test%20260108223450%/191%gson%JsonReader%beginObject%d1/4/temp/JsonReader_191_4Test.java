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

public class JsonReader_191_4Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  public void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  @Test
    @Timeout(8000)
  public void beginObject_peekedIsPEEKED_BEGIN_OBJECT_pushesScopeAndResetsPeeked() throws Exception {
    // Set peeked to PEEKED_BEGIN_OBJECT (1)
    setField(jsonReader, "peeked", 1);
    // Set initial stackSize to 0
    setField(jsonReader, "stackSize", 0);

    jsonReader.beginObject();

    int peekedAfter = getField(jsonReader, "peeked");
    int stackSizeAfter = getField(jsonReader, "stackSize");
    int[] stack = getField(jsonReader, "stack");

    assertEquals(0, peekedAfter, "peeked should be reset to PEEKED_NONE (0)");
    assertEquals(1, stackSizeAfter, "stackSize should be incremented by 1");
    assertEquals(JsonScope.EMPTY_OBJECT, stack[stackSizeAfter - 1], "Top of stack should be EMPTY_OBJECT");
  }

  @Test
    @Timeout(8000)
  public void beginObject_peekedIsPEEKED_NONE_callsDoPeekAndPushesScope() throws Exception {
    // Set peeked to PEEKED_NONE (0)
    setField(jsonReader, "peeked", 0);
    // Mock doPeek to return PEEKED_BEGIN_OBJECT (1)
    Method doPeek = JsonReader.class.getDeclaredMethod("doPeek");
    doPeek.setAccessible(true);

    JsonReader spyReader = spy(jsonReader);
    doReturn(1).when(spyReader).doPeek();

    // Set initial stackSize to 0
    setField(spyReader, "stackSize", 0);

    spyReader.beginObject();

    int peekedAfter = getField(spyReader, "peeked");
    int stackSizeAfter = getField(spyReader, "stackSize");
    int[] stack = getField(spyReader, "stack");

    assertEquals(0, peekedAfter, "peeked should be reset to PEEKED_NONE (0)");
    assertEquals(1, stackSizeAfter, "stackSize should be incremented by 1");
    assertEquals(JsonScope.EMPTY_OBJECT, stack[stackSizeAfter - 1], "Top of stack should be EMPTY_OBJECT");
  }

  @Test
    @Timeout(8000)
  public void beginObject_peekedIsNotBeginObject_throwsIllegalStateException() throws Exception {
    // Set peeked to some invalid value (e.g. PEEKED_END_OBJECT = 2)
    setField(jsonReader, "peeked", 2);

    // Mock peek() to return JsonToken.END_OBJECT
    JsonToken fakeToken = JsonToken.END_OBJECT;
    JsonReader spyReader = spy(jsonReader);
    doReturn(fakeToken).when(spyReader).peek();

    // Mock locationString() to return ": at line 1 column 1 path $"
    doReturn(": at line 1 column 1 path $").when(spyReader).locationString();

    IllegalStateException ex = assertThrows(IllegalStateException.class, spyReader::beginObject);
    assertTrue(ex.getMessage().contains("Expected BEGIN_OBJECT but was END_OBJECT"));
  }

  // Helper to set private fields via reflection
  private <T> void setField(Object target, String fieldName, T value) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  // Helper to get private fields via reflection
  @SuppressWarnings("unchecked")
  private <T> T getField(Object target, String fieldName) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    return (T) field.get(target);
  }
}