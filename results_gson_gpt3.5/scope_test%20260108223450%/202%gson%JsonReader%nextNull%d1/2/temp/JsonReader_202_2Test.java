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

public class JsonReader_202_2Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  public void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  @Test
    @Timeout(8000)
  public void nextNull_peekedIsPeekedNull_shouldConsumeNull() throws Exception {
    // Set peeked to PEEKED_NULL (7)
    setPeeked(7);
    setStackSize(1);
    setPathIndices(new int[]{0});

    jsonReader.nextNull();

    assertEquals(0, getPeeked());
    int[] pathIndices = getPathIndices();
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void nextNull_peekedIsPeekedNone_doPeekReturnsPeekedNull_shouldConsumeNull() throws Exception {
    // Set peeked to PEEKED_NONE (0)
    setPeeked(0);
    setStackSize(1);
    setPathIndices(new int[]{0});

    // Mock doPeek to return PEEKED_NULL (7)
    Method doPeek = JsonReader.class.getDeclaredMethod("doPeek");
    doPeek.setAccessible(true);

    JsonReader spyReader = spy(jsonReader);
    doReturn(7).when(spyReader).doPeek();

    // Use reflection to invoke nextNull on spyReader
    Method nextNull = JsonReader.class.getDeclaredMethod("nextNull");
    nextNull.setAccessible(true);

    nextNull.invoke(spyReader);

    Field peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    int peeked = peekedField.getInt(spyReader);
    assertEquals(0, peeked);

    int[] pathIndices = getPathIndices(spyReader);
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void nextNull_peekedIsNotNull_shouldThrowIllegalStateException() throws Exception {
    setPeeked(5); // PEEKED_TRUE, not null
    setStackSize(1);
    setPathIndices(new int[]{0});

    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
      jsonReader.nextNull();
    });
    String message = thrown.getMessage();
    assertTrue(message.contains("Expected null but was"));
    assertTrue(message.contains(" at path "));
  }

  // Helper methods to access private fields via reflection

  private void setPeeked(int value) throws Exception {
    Field peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    peekedField.setInt(jsonReader, value);
  }

  private int getPeeked() throws Exception {
    Field peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    return peekedField.getInt(jsonReader);
  }

  private void setStackSize(int size) throws Exception {
    Field stackSizeField = JsonReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonReader, size);
  }

  private void setPathIndices(int[] indices) throws Exception {
    Field pathIndicesField = JsonReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    pathIndicesField.set(jsonReader, indices);
  }

  private int[] getPathIndices() throws Exception {
    Field pathIndicesField = JsonReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    return (int[]) pathIndicesField.get(jsonReader);
  }

  private int[] getPathIndices(JsonReader reader) throws Exception {
    Field pathIndicesField = JsonReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    return (int[]) pathIndicesField.get(reader);
  }
}