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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonReaderBeginObjectTest {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  void setUp() throws Exception {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
    // Initialize stackSize and stack properly to avoid ArrayIndexOutOfBounds
    setField(jsonReader, "stackSize", 1);
    int[] stack = getField(jsonReader, "stack");
    stack[0] = JsonScope.EMPTY_DOCUMENT; // initial state
  }

  @Test
    @Timeout(8000)
  void beginObject_peekedIsPeekedBeginObject_pushesScopeAndResetsPeeked() throws Exception {
    // Set peeked to PEEKED_BEGIN_OBJECT (1)
    setField(jsonReader, "peeked", 1);

    // Initialize stackSize and stack
    setField(jsonReader, "stackSize", 1);
    int[] stack = getField(jsonReader, "stack");
    stack[0] = JsonScope.EMPTY_DOCUMENT;

    // Call beginObject()
    jsonReader.beginObject();

    // Verify peeked reset to PEEKED_NONE (0)
    int peeked = getField(jsonReader, "peeked");
    assertEquals(0, peeked);

    // Verify stack top is JsonScope.EMPTY_OBJECT (empty object scope)
    int[] stackAfter = getField(jsonReader, "stack");
    int stackSize = getField(jsonReader, "stackSize");
    assertEquals(JsonScope.EMPTY_OBJECT, stackAfter[stackSize - 1]);
  }

  @Test
    @Timeout(8000)
  void beginObject_peekedIsPeekedNone_doPeekReturnsPeekedBeginObject_pushesScopeAndResetsPeeked() throws Exception {
    // Set peeked to PEEKED_NONE (0)
    setField(jsonReader, "peeked", 0);

    // Initialize stackSize and stack for spyReader
    JsonReader spyReader = spy(jsonReader);
    setField(spyReader, "stackSize", 1);
    int[] stack = getField(spyReader, "stack");
    stack[0] = JsonScope.EMPTY_DOCUMENT;

    // Mock doPeek to return PEEKED_BEGIN_OBJECT (1)
    doReturn(1).when(spyReader).doPeek();

    // Also mock peek() to return BEGIN_OBJECT token string for error message if needed
    doReturn(JsonToken.BEGIN_OBJECT).when(spyReader).peek();

    // Call beginObject()
    spyReader.beginObject();

    // Verify peeked reset to PEEKED_NONE (0)
    int peeked = getField(spyReader, "peeked");
    assertEquals(0, peeked);

    // Verify stack top is JsonScope.EMPTY_OBJECT
    int[] stackAfter = getField(spyReader, "stack");
    int stackSizeAfter = getField(spyReader, "stackSize");
    assertEquals(JsonScope.EMPTY_OBJECT, stackAfter[stackSizeAfter - 1]);
  }

  @Test
    @Timeout(8000)
  void beginObject_invalidPeeked_throwsIllegalStateException() throws Exception {
    // Set peeked to an invalid value (e.g., PEEKED_END_OBJECT = 2)
    setField(jsonReader, "peeked", 2);

    // Initialize stackSize and stack for spyReader
    JsonReader spyReader = spy(jsonReader);
    setField(spyReader, "stackSize", 1);
    int[] stack = getField(spyReader, "stack");
    stack[0] = JsonScope.EMPTY_DOCUMENT;

    // Mock peek() to return a token for error message
    doReturn(JsonToken.END_OBJECT).when(spyReader).peek();

    // Mock locationString() to return a dummy location string
    doReturn(" at line 1 column 1 path $").when(spyReader).locationString();

    IllegalStateException ex = assertThrows(IllegalStateException.class, spyReader::beginObject);
    assertTrue(ex.getMessage().contains("Expected BEGIN_OBJECT but was END_OBJECT"));
    assertTrue(ex.getMessage().contains("at line 1 column 1 path $"));
  }

  // Utility methods to access private fields
  @SuppressWarnings("unchecked")
  private <T> T getField(Object instance, String name) throws Exception {
    Field field = findField(instance.getClass(), name);
    field.setAccessible(true);
    return (T) field.get(instance);
  }

  private void setField(Object instance, String name, Object value) throws Exception {
    Field field = findField(instance.getClass(), name);
    field.setAccessible(true);
    field.set(instance, value);
  }

  private Field findField(Class<?> clazz, String name) throws NoSuchFieldException {
    Class<?> current = clazz;
    while (current != null) {
      try {
        return current.getDeclaredField(name);
      } catch (NoSuchFieldException e) {
        current = current.getSuperclass();
      }
    }
    throw new NoSuchFieldException(name);
  }
}