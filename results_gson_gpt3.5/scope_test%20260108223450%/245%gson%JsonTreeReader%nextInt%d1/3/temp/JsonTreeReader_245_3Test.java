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

class JsonTreeReader_245_3Test {

  private JsonTreeReader jsonTreeReader;

  @BeforeEach
  void setUp() throws Exception {
    // Create a JsonPrimitive with int value 42
    JsonPrimitive jsonPrimitive = new JsonPrimitive(42);

    // Use the public constructor JsonTreeReader(JsonElement)
    // Since JsonPrimitive extends JsonElement, we can use it directly
    jsonTreeReader = new JsonTreeReader(jsonPrimitive);

    // Using reflection to set up private fields stack, stackSize, pathIndices for coverage
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(jsonTreeReader);

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);

    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);

    // Set stack[0] = jsonPrimitive and stackSize = 1
    stack[0] = jsonPrimitive;
    stackField.set(jsonTreeReader, stack);
    stackSizeField.setInt(jsonTreeReader, 1);

    // Initialize pathIndices[0] = 0
    int[] pathIndices = (int[]) pathIndicesField.get(jsonTreeReader);
    pathIndices[0] = 0;
    pathIndicesField.set(jsonTreeReader, pathIndices);
  }

  @Test
    @Timeout(8000)
  void nextInt_validNumberToken_returnsInt() throws Exception {
    // peek() should return JsonToken.NUMBER for this test
    // The constructor and setup already put JsonPrimitive(42) on stack, so peek() returns NUMBER

    int result = jsonTreeReader.nextInt();

    assertEquals(42, result);

    // After nextInt, stackSize should be 0 (stack popped)
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    int stackSize = stackSizeField.getInt(jsonTreeReader);
    assertEquals(0, stackSize);

    // pathIndices[stackSize - 1] is not incremented because stackSize is 0, so no increment
  }

  @Test
    @Timeout(8000)
  void nextInt_validStringNumberToken_returnsInt() throws Exception {
    // Setup stack with JsonPrimitive string "123"
    JsonPrimitive stringPrimitive = new JsonPrimitive("123");

    // Create a new JsonTreeReader instance with stringPrimitive
    JsonTreeReader reader = new JsonTreeReader(stringPrimitive);

    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(reader);

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);

    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);

    stack[0] = stringPrimitive;
    stackField.set(reader, stack);
    stackSizeField.setInt(reader, 1);

    int[] pathIndices = (int[]) pathIndicesField.get(reader);
    pathIndices[0] = 0;
    pathIndicesField.set(reader, pathIndices);

    // Spy on reader
    JsonTreeReader spyReader = spy(reader);

    // Mock peek() to return JsonToken.STRING
    doReturn(JsonToken.STRING).when(spyReader).peek();

    int result = spyReader.nextInt();

    assertEquals(123, result);

    int stackSize = stackSizeField.getInt(spyReader);
    assertEquals(0, stackSize);

    // pathIndices not incremented because stackSize is 0
  }

  @Test
    @Timeout(8000)
  void nextInt_invalidToken_throwsIllegalStateException() throws Exception {
    // Mock peek() to return JsonToken.BEGIN_ARRAY (invalid for nextInt)
    JsonTreeReader spyReader = spy(jsonTreeReader);
    doReturn(JsonToken.BEGIN_ARRAY).when(spyReader).peek();

    IllegalStateException thrown = assertThrows(IllegalStateException.class, spyReader::nextInt);
    assertTrue(thrown.getMessage().contains("Expected NUMBER but was BEGIN_ARRAY"));
  }

  @Test
    @Timeout(8000)
  void nextInt_incrementsPathIndicesWhenStackSizeGreaterThanZero() throws Exception {
    // Setup stackSize = 2 and pathIndices[1] = 5
    JsonPrimitive jsonPrimitive = new JsonPrimitive(7);

    // Create a new JsonTreeReader instance with jsonPrimitive (will be replaced stack later)
    JsonTreeReader reader = new JsonTreeReader(jsonPrimitive);

    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(reader);

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);

    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);

    stack[0] = new JsonPrimitive(1);
    stack[1] = jsonPrimitive;
    stackField.set(reader, stack);

    stackSizeField.setInt(reader, 2);

    int[] pathIndices = (int[]) pathIndicesField.get(reader);
    pathIndices[0] = 3;
    pathIndices[1] = 5;
    pathIndicesField.set(reader, pathIndices);

    // Spy on reader
    JsonTreeReader spyReader = spy(reader);

    // Mock peek() to return JsonToken.NUMBER
    doReturn(JsonToken.NUMBER).when(spyReader).peek();

    int result = spyReader.nextInt();

    assertEquals(7, result);

    // After popStack, stackSize = 1
    int stackSize = stackSizeField.getInt(spyReader);
    assertEquals(1, stackSize);

    // pathIndices[stackSize - 1] = pathIndices[0] incremented by 1 from 3 to 4
    int[] updatedPathIndices = (int[]) pathIndicesField.get(spyReader);
    assertEquals(4, updatedPathIndices[0]);
  }
}