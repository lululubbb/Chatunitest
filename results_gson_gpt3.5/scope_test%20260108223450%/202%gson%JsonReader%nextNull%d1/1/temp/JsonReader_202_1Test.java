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

public class JsonReader_202_1Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  public void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
    // Initialize stackSize and pathIndices for nextNull increments
    try {
      Field stackSizeField = JsonReader.class.getDeclaredField("stackSize");
      stackSizeField.setAccessible(true);
      stackSizeField.setInt(jsonReader, 1);

      Field pathIndicesField = JsonReader.class.getDeclaredField("pathIndices");
      pathIndicesField.setAccessible(true);
      int[] pathIndices = (int[]) pathIndicesField.get(jsonReader);
      pathIndices[0] = 0;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Test
    @Timeout(8000)
  public void nextNull_peekedIsPeekedNull_shouldConsumeNullAndIncrementPathIndex() throws Exception {
    // Set peeked to PEEKED_NULL (7)
    setPeekedField(JsonReader.class, jsonReader, 7);

    // Capture initial pathIndices[stackSize-1]
    int initialIndex = getPathIndicesAtStackTop();

    jsonReader.nextNull();

    // After nextNull, peeked should be reset to PEEKED_NONE (0)
    assertEquals(0, getPeekedField());

    // pathIndices[stackSize-1] should be incremented by 1
    assertEquals(initialIndex + 1, getPathIndicesAtStackTop());
  }

  @Test
    @Timeout(8000)
  public void nextNull_peekedIsPeekedNoneAndDoPeekReturnsPeekedNull_shouldConsumeNullAndIncrementPathIndex() throws Exception {
    // Set peeked to PEEKED_NONE (0)
    setPeekedField(JsonReader.class, jsonReader, 0);

    // Mock doPeek() to return PEEKED_NULL (7)
    Method doPeekMethod = JsonReader.class.getDeclaredMethod("doPeek");
    doPeekMethod.setAccessible(true);

    JsonReader spyReader = spy(jsonReader);
    doReturn(7).when(spyReader).doPeek();

    // Initialize stackSize and pathIndices as in setUp
    Field stackSizeField = JsonReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(spyReader, 1);

    Field pathIndicesField = JsonReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(spyReader);
    pathIndices[0] = 0;

    int initialIndex = pathIndices[0];

    spyReader.nextNull();

    // peeked should be reset to 0
    Field peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    int peekedAfter = peekedField.getInt(spyReader);
    assertEquals(0, peekedAfter);

    // pathIndices incremented
    assertEquals(initialIndex + 1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void nextNull_peekedIsNotNull_shouldThrowIllegalStateException() throws Exception {
    // Set peeked to PEEKED_TRUE (5)
    setPeekedField(JsonReader.class, jsonReader, 5);

    IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
      jsonReader.nextNull();
    });

    // Exception message should contain "Expected null but was"
    assertTrue(exception.getMessage().contains("Expected null but was"));
  }

  @Test
    @Timeout(8000)
  public void nextNull_peekedIsPeekedNoneAndDoPeekReturnsNotNull_shouldThrowIllegalStateException() throws Exception {
    // Set peeked to PEEKED_NONE (0)
    setPeekedField(JsonReader.class, jsonReader, 0);

    JsonReader spyReader = spy(jsonReader);
    doReturn(5).when(spyReader).doPeek(); // PEEKED_TRUE

    IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
      spyReader.nextNull();
    });

    assertTrue(exception.getMessage().contains("Expected null but was"));
  }

  // Helper methods for reflection access

  private void setPeekedField(Class<?> clazz, Object instance, int value) throws Exception {
    Field peekedField = clazz.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    peekedField.setInt(instance, value);
  }

  private int getPeekedField() throws Exception {
    Field peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    return peekedField.getInt(jsonReader);
  }

  private int getPathIndicesAtStackTop() throws Exception {
    Field stackSizeField = JsonReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    int stackSize = stackSizeField.getInt(jsonReader);

    Field pathIndicesField = JsonReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(jsonReader);

    return pathIndices[stackSize - 1];
  }
}