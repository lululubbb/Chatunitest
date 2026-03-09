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

public class JsonReader_202_4Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  public void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  @Test
    @Timeout(8000)
  public void nextNull_peekedIsNull_shouldConsumeNull() throws Exception {
    // Set peeked to PEEKED_NULL (7)
    setField(jsonReader, "peeked", 7);
    // Initialize stackSize and pathIndices for increment
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    setField(jsonReader, "pathIndices", pathIndices);

    jsonReader.nextNull();

    // peeked should be reset to PEEKED_NONE (0)
    assertEquals(0, getField(jsonReader, "peeked"));
    // pathIndices[stackSize - 1] incremented by 1
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void nextNull_peekedIsNoneAndDoPeekReturnsNull_shouldConsumeNull() throws Exception {
    // Set peeked to PEEKED_NONE (0)
    setField(jsonReader, "peeked", 0);
    // mock doPeek() to return PEEKED_NULL (7)
    Method doPeekMethod = JsonReader.class.getDeclaredMethod("doPeek");
    doPeekMethod.setAccessible(true);
    JsonReader spyReader = spy(jsonReader);
    doReturn(7).when(spyReader).doPeek();

    // Initialize stackSize and pathIndices for increment
    setField(spyReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    setField(spyReader, "pathIndices", pathIndices);

    spyReader.nextNull();

    // peeked should be reset to PEEKED_NONE (0)
    assertEquals(0, getField(spyReader, "peeked"));
    // pathIndices[stackSize - 1] incremented by 1
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void nextNull_peekedIsNoneAndDoPeekReturnsNotNull_shouldThrow() throws Exception {
    // Set peeked to PEEKED_NONE (0)
    setField(jsonReader, "peeked", 0);
    // mock doPeek() to return something other than PEEKED_NULL (e.g. PEEKED_TRUE=5)
    JsonReader spyReader = spy(jsonReader);
    doReturn(5).when(spyReader).doPeek();

    // mock peek() to return a token string - use correct enum name "TRUE"
    doReturn(getJsonTokenValue("TRUE")).when(spyReader).peek();

    // mock locationString() to return a location suffix
    doReturn(" at line 1 column 1 path $").when(spyReader).locationString();

    IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
      spyReader.nextNull();
    });
    assertTrue(ex.getMessage().contains("Expected null but was TRUE at line 1 column 1 path $"));
  }

  @Test
    @Timeout(8000)
  public void nextNull_peekedIsNotNoneOrNull_shouldThrow() throws Exception {
    // Set peeked to a token other than NONE or NULL (e.g. PEEKED_TRUE=5)
    setField(jsonReader, "peeked", 5);

    // mock peek() to return a token string - use correct enum name "TRUE"
    JsonReader spyReader = spy(jsonReader);
    doReturn(getJsonTokenValue("TRUE")).when(spyReader).peek();

    // mock locationString() to return a location suffix
    doReturn(" at line 1 column 1 path $").when(spyReader).locationString();

    IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
      spyReader.nextNull();
    });
    assertTrue(ex.getMessage().contains("Expected null but was TRUE at line 1 column 1 path $"));
  }

  // Helper methods for reflection

  private void setField(Object instance, String fieldName, Object value) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(instance, value);
  }

  private int getField(JsonReader instance, String fieldName) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.getInt(instance);
  }

  private Object getJsonTokenValue(String name) throws Exception {
    Class<?> jsonTokenClass = Class.forName("com.google.gson.stream.JsonToken");
    for (Object constant : jsonTokenClass.getEnumConstants()) {
      if (((Enum<?>) constant).name().equals(name)) {
        return constant;
      }
    }
    throw new IllegalArgumentException("No enum constant " + jsonTokenClass.getName() + "." + name);
  }
}