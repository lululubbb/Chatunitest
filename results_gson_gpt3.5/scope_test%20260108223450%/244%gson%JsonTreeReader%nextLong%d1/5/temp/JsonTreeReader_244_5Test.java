package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class JsonTreeReader_244_5Test {
  private JsonTreeReader reader;

  @BeforeEach
  public void setUp() throws Exception {
    // Create a JsonPrimitive with a long value
    JsonPrimitive primitive = new JsonPrimitive(1234567890123L);

    // Instantiate JsonTreeReader with a dummy JsonElement (null for constructor param)
    // We will replace internal stack manually
    reader = new JsonTreeReader(null);

    // Use reflection to set private fields:
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = new Object[32];
    stack[0] = primitive;
    stackField.set(reader, stack);

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(reader, 1);

    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = new int[32];
    pathIndicesField.set(reader, pathIndices);
  }

  @Test
    @Timeout(8000)
  public void testNextLong_withNumberToken_returnsLong() throws Exception {
    // Mock peek() to return JsonToken.NUMBER
    JsonTreeReader spyReader = spy(reader);
    doReturn(JsonToken.NUMBER).when(spyReader).peek();

    // Invoke nextLong
    long result = spyReader.nextLong();

    // Verify result matches the primitive long value
    assertEquals(1234567890123L, result);

    // Verify stackSize decremented
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    int stackSize = stackSizeField.getInt(spyReader);
    assertEquals(0, stackSize);

    // Verify pathIndices incremented (should not increment because stackSize is 0 now)
    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(spyReader);
    assertEquals(0, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void testNextLong_withStringToken_returnsLong() throws Exception {
    // Setup JsonPrimitive with string long value
    JsonPrimitive primitive = new JsonPrimitive("9876543210987");
    // Replace stack and stackSize
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = new Object[32];
    stack[0] = primitive;
    stackField.set(reader, stack);

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(reader, 1);

    // Mock peek() to return STRING
    JsonTreeReader spyReader = spy(reader);
    doReturn(JsonToken.STRING).when(spyReader).peek();

    long result = spyReader.nextLong();

    assertEquals(9876543210987L, result);

    int stackSize = stackSizeField.getInt(spyReader);
    assertEquals(0, stackSize);

    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(spyReader);
    assertEquals(0, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void testNextLong_withInvalidToken_throwsIllegalStateException() throws Exception {
    JsonTreeReader spyReader = spy(reader);
    doReturn(JsonToken.BEGIN_ARRAY).when(spyReader).peek();

    IllegalStateException ex = assertThrows(IllegalStateException.class, spyReader::nextLong);
    assertTrue(ex.getMessage().contains("Expected " + JsonToken.NUMBER));
    assertTrue(ex.getMessage().contains("but was " + JsonToken.BEGIN_ARRAY));
  }

  @Test
    @Timeout(8000)
  public void testNextLong_incrementsPathIndicesWhenStackSizeGreaterThanZero() throws Exception {
    // Setup stack with two elements
    JsonPrimitive primitive = new JsonPrimitive(42L);
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = new Object[32];
    stack[0] = new JsonPrimitive("dummy");
    stack[1] = primitive;
    stackField.set(reader, stack);

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(reader, 2);

    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = new int[32];
    pathIndices[0] = 5;
    pathIndicesField.set(reader, pathIndices);

    JsonTreeReader spyReader = spy(reader);
    doReturn(JsonToken.NUMBER).when(spyReader).peek();

    long result = spyReader.nextLong();

    assertEquals(42L, result);

    int stackSize = stackSizeField.getInt(spyReader);
    assertEquals(1, stackSize);

    int[] updatedPathIndices = (int[]) pathIndicesField.get(spyReader);
    assertEquals(6, updatedPathIndices[0]);
  }
}