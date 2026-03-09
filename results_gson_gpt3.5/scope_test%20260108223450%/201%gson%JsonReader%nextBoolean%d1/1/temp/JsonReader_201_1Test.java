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

public class JsonReader_201_1Test {
  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  public void setup() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
    // Initialize stackSize and pathIndices to avoid ArrayIndexOutOfBoundsException
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
  public void testNextBoolean_peekedTrue() throws Exception {
    setPeekedField(JsonReader.class, jsonReader, 5); // PEEKED_TRUE

    boolean result = jsonReader.nextBoolean();

    assertTrue(result);
    assertEquals(0, getPeekedField(JsonReader.class, jsonReader));
    assertEquals(1, getPathIndicesAt(jsonReader, 0));
  }

  @Test
    @Timeout(8000)
  public void testNextBoolean_peekedFalse() throws Exception {
    setPeekedField(JsonReader.class, jsonReader, 6); // PEEKED_FALSE

    boolean result = jsonReader.nextBoolean();

    assertFalse(result);
    assertEquals(0, getPeekedField(JsonReader.class, jsonReader));
    assertEquals(1, getPathIndicesAt(jsonReader, 0));
  }

  @Test
    @Timeout(8000)
  public void testNextBoolean_peekedNone_doPeekReturnsTrue() throws Exception {
    setPeekedField(JsonReader.class, jsonReader, 0); // PEEKED_NONE

    // Mock doPeek to return PEEKED_TRUE (5)
    Method doPeekMethod = JsonReader.class.getDeclaredMethod("doPeek");
    doPeekMethod.setAccessible(true);

    JsonReader spyReader = spy(jsonReader);
    doReturn(5).when(spyReader).doPeek();

    // Also set stackSize and pathIndices for spyReader
    setStackSize(spyReader, 1);
    setPathIndices(spyReader, 0, 0);

    boolean result = spyReader.nextBoolean();

    assertTrue(result);
    assertEquals(0, getPeekedField(JsonReader.class, spyReader));
    assertEquals(1, getPathIndicesAt(spyReader, 0));
  }

  @Test
    @Timeout(8000)
  public void testNextBoolean_peekedNone_doPeekReturnsFalse() throws Exception {
    setPeekedField(JsonReader.class, jsonReader, 0); // PEEKED_NONE

    // Mock doPeek to return PEEKED_FALSE (6)
    JsonReader spyReader = spy(jsonReader);
    doReturn(6).when(spyReader).doPeek();

    setStackSize(spyReader, 1);
    setPathIndices(spyReader, 0, 0);

    boolean result = spyReader.nextBoolean();

    assertFalse(result);
    assertEquals(0, getPeekedField(JsonReader.class, spyReader));
    assertEquals(1, getPathIndicesAt(spyReader, 0));
  }

  @Test
    @Timeout(8000)
  public void testNextBoolean_throwsIllegalStateException() throws Exception {
    setPeekedField(JsonReader.class, jsonReader, 0); // PEEKED_NONE

    JsonReader spyReader = spy(jsonReader);
    doReturn(1).when(spyReader).doPeek(); // PEEKED_BEGIN_OBJECT (not boolean)
    doReturn(JsonToken.BEGIN_OBJECT).when(spyReader).peek();
    // locationString returns some string, mock it via reflection
    Method locationStringMethod = JsonReader.class.getDeclaredMethod("locationString");
    locationStringMethod.setAccessible(true);
    // We can't mock private method easily, so we spy and override locationString
    JsonReader spyReader2 = new JsonReader(mockReader) {
      @Override
      public JsonToken peek() {
        return JsonToken.BEGIN_OBJECT;
      }
      @Override
      String locationString() {
        return " at path $";
      }
      @Override
      int doPeek() throws IOException {
        return 1;
      }
    };
    setPeekedField(JsonReader.class, spyReader2, 0);
    setStackSize(spyReader2, 1);
    setPathIndices(spyReader2, 0, 0);

    IllegalStateException ex = assertThrows(IllegalStateException.class, spyReader2::nextBoolean);
    assertTrue(ex.getMessage().contains("Expected a boolean but was"));
  }

  // Helper methods to access private fields and set them
  private void setPeekedField(Class<?> clazz, Object instance, int value) throws Exception {
    Field peekedField = clazz.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    peekedField.setInt(instance, value);
  }

  private int getPeekedField(Class<?> clazz, Object instance) throws Exception {
    Field peekedField = clazz.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    return peekedField.getInt(instance);
  }

  private int getPathIndicesAt(Object instance, int index) throws Exception {
    Field pathIndicesField = JsonReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(instance);
    return pathIndices[index];
  }

  private void setStackSize(Object instance, int size) throws Exception {
    Field stackSizeField = JsonReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(instance, size);
  }

  private void setPathIndices(Object instance, int index, int value) throws Exception {
    Field pathIndicesField = JsonReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(instance);
    pathIndices[index] = value;
  }
}