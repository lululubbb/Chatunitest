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

public class JsonReader_189_4Test {

  private JsonReader jsonReader;

  @BeforeEach
  public void setUp() throws Exception {
    Reader mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
    // Initialize stackSize to 1 to avoid ArrayIndexOutOfBounds in beginArray
    Field stackSizeField = JsonReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonReader, 1);
    // Initialize pathIndices array with size at least 1
    Field pathIndicesField = JsonReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(jsonReader);
    pathIndices[0] = -1;
  }

  @Test
    @Timeout(8000)
  public void beginArray_peekedIsPeekedBeginArray_shouldPushEmptyArrayAndResetPeeked() throws Exception {
    // Set peeked to PEEKED_BEGIN_ARRAY (3)
    Field peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    peekedField.setInt(jsonReader, 3);

    // Record current stackSize
    Field stackSizeField = JsonReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    int stackSizeBefore = stackSizeField.getInt(jsonReader);

    // Call beginArray
    jsonReader.beginArray();

    // Check stackSize incremented
    int stackSizeAfter = stackSizeField.getInt(jsonReader);
    assertEquals(stackSizeBefore + 1, stackSizeAfter);

    // Check stack top is JsonScope.EMPTY_ARRAY (4)
    Field stackField = JsonReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    int[] stack = (int[]) stackField.get(jsonReader);
    assertEquals(JsonScope.EMPTY_ARRAY, stack[stackSizeAfter - 1]);

    // Check pathIndices[stackSize-1] == 0
    Field pathIndicesField = JsonReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(jsonReader);
    assertEquals(0, pathIndices[stackSizeAfter - 1]);

    // Check peeked reset to PEEKED_NONE (0)
    int peekedAfter = peekedField.getInt(jsonReader);
    assertEquals(0, peekedAfter);
  }

  // Helper subclass to override doPeek()
  static class JsonReaderWithMockedDoPeek extends JsonReader {
    JsonReaderWithMockedDoPeek(Reader in) {
      super(in);
    }

    @Override
    int doPeek() {
      return 3; // PEEKED_BEGIN_ARRAY
    }
  }

  @Test
    @Timeout(8000)
  public void beginArray_peekedIsPeekedNone_doPeekReturnsBeginArray_shouldWork() throws Exception {
    // Create instance of helper subclass with mocked doPeek()
    Reader mockReader = mock(Reader.class);
    JsonReaderWithMockedDoPeek testReader = new JsonReaderWithMockedDoPeek(mockReader);

    // Set stackSize to 1 for testReader
    Field stackSizeField = JsonReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(testReader, 1);

    // Initialize pathIndices array with size at least 1
    Field pathIndicesField = JsonReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(testReader);
    pathIndices[0] = -1;

    // Set peeked to PEEKED_NONE (0)
    Field peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    peekedField.setInt(testReader, 0);

    // Call beginArray
    testReader.beginArray();

    // Check stack top is JsonScope.EMPTY_ARRAY (4)
    Field stackField = JsonReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    int[] stack = (int[]) stackField.get(testReader);
    int stackSize = stackSizeField.getInt(testReader);
    assertEquals(JsonScope.EMPTY_ARRAY, stack[stackSize - 1]);

    // Check pathIndices[stackSize - 1] == 0
    int[] pathIndicesAfter = (int[]) pathIndicesField.get(testReader);
    assertEquals(0, pathIndicesAfter[stackSize - 1]);

    // Check peeked reset to PEEKED_NONE (0)
    int peekedAfter = peekedField.getInt(testReader);
    assertEquals(0, peekedAfter);
  }

  @Test
    @Timeout(8000)
  public void beginArray_peekedIsNotBeginArray_shouldThrowIllegalStateException() throws Exception {
    // Set peeked to a value other than PEEKED_NONE or PEEKED_BEGIN_ARRAY
    Field peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    peekedField.setInt(jsonReader, 1); // PEEKED_BEGIN_OBJECT

    // Mock peek() to return a token string for error message
    JsonReader spyReader = spy(jsonReader);
    doReturn(JsonToken.BEGIN_OBJECT).when(spyReader).peek();

    // Call beginArray and expect IllegalStateException
    IllegalStateException exception = assertThrows(IllegalStateException.class, spyReader::beginArray);
    assertTrue(exception.getMessage().contains("Expected BEGIN_ARRAY but was"));
    assertTrue(exception.getMessage().contains("BEGIN_OBJECT"));
  }
}