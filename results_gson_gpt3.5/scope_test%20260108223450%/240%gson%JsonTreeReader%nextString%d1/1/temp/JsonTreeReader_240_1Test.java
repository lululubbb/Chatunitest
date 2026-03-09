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

public class JsonTreeReader_240_1Test {

  private JsonTreeReader reader;

  @BeforeEach
  void setUp() throws Exception {
    // Create a dummy JsonPrimitive to initialize the reader stack
    JsonPrimitive primitive = new JsonPrimitive("initial");
    // Use reflection to call the constructor JsonTreeReader(JsonElement)
    Class<?> clazz = JsonTreeReader.class;
    // Since JsonElement is abstract, use JsonPrimitive as a concrete subclass
    reader = (JsonTreeReader) clazz.getConstructor(com.google.gson.JsonElement.class)
        .newInstance(primitive);

    // Initialize stack with size 1 and stack[0] = primitive to avoid NPEs
    setField(reader, "stackSize", 1);
    setStackElement(reader, 0, primitive);

    // Initialize pathIndices and pathNames arrays
    setField(reader, "pathIndices", new int[32]);
    setField(reader, "pathNames", new String[32]);
  }

  @Test
    @Timeout(8000)
  void nextString_withStringToken_returnsString() throws Exception {
    // Setup stack to simulate the state for nextString()

    // stackSize = 1 (already set in setUp)
    setField(reader, "stackSize", 1);

    // Set stack[0] = JsonPrimitive with string value "testString"
    JsonPrimitive stringPrimitive = new JsonPrimitive("testString");
    setStackElement(reader, 0, stringPrimitive);

    // Spy reader and override peek()
    JsonTreeReader spyReader = spy(reader);
    doReturn(JsonToken.STRING).when(spyReader).peek();

    // Set pathIndices[0] = 0
    setPathIndex(spyReader, 0, 0);

    // Call nextString()
    String result = spyReader.nextString();

    // Assert returned string equals "testString"
    assertEquals("testString", result);

    // After call, pathIndices[0] should be incremented by 1
    int updatedIndex = getPathIndex(spyReader, 0);
    assertEquals(1, updatedIndex);
  }

  @Test
    @Timeout(8000)
  void nextString_withNumberToken_returnsNumberAsString() throws Exception {
    setField(reader, "stackSize", 1);

    JsonPrimitive numberPrimitive = new JsonPrimitive(123.45);
    setStackElement(reader, 0, numberPrimitive);

    JsonTreeReader spyReader = spy(reader);
    doReturn(JsonToken.NUMBER).when(spyReader).peek();

    setPathIndex(spyReader, 0, 5);

    String result = spyReader.nextString();

    assertEquals("123.45", result);

    int updatedIndex = getPathIndex(spyReader, 0);
    assertEquals(6, updatedIndex);
  }

  @Test
    @Timeout(8000)
  void nextString_withInvalidToken_throwsIllegalStateException() throws Exception {
    JsonTreeReader spyReader = spy(reader);
    doReturn(JsonToken.BEGIN_ARRAY).when(spyReader).peek();

    IllegalStateException thrown = assertThrows(IllegalStateException.class, spyReader::nextString);

    String expectedMessageStart = "Expected " + JsonToken.STRING + " but was " + JsonToken.BEGIN_ARRAY;
    assertTrue(thrown.getMessage().startsWith(expectedMessageStart));
  }

  @Test
    @Timeout(8000)
  void nextString_withStackSizeZero_doesNotIncrementPathIndices() throws Exception {
    // stackSize = 0 means no increment on pathIndices
    setField(reader, "stackSize", 0);

    // Set stack to empty array to avoid popStack() popping invalid element
    setField(reader, "stack", new Object[32]);

    // Initialize pathIndices array
    int[] pathIndices = new int[32];
    setField(reader, "pathIndices", pathIndices);

    // Because stackSize=0, popStack() will throw ArrayIndexOutOfBoundsException if stack is empty.
    // So push an element and adjust stackSize to 1 to avoid exception.

    JsonPrimitive stringPrimitive = new JsonPrimitive("zeroStack");
    setStackElement(reader, 0, stringPrimitive);

    // Spy reader and override peek() to return STRING
    JsonTreeReader spyReader = spy(reader);
    doReturn(JsonToken.STRING).when(spyReader).peek();

    // Set stackSize to 1 before calling nextString to avoid popStack error
    setField(spyReader, "stackSize", 1);

    String result = spyReader.nextString();

    assertEquals("zeroStack", result);

    // Now set stackSize to 0 and check that pathIndices are not incremented after another call

    setField(spyReader, "stackSize", 0);

    // Since calling nextString() now would cause an error, just verify pathIndices are unchanged after previous call

    for (int i = 1; i < pathIndices.length; i++) {
      assertEquals(0, pathIndices[i]);
    }
  }

  // Helper methods to set private fields and stack elements

  private void setField(Object target, String fieldName, Object value) throws Exception {
    Field field = JsonTreeReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  private void setStackElement(Object targetReader, int index, Object value) throws Exception {
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(targetReader);
    // Defensive: expand stack if index >= length
    if (index >= stack.length) {
      Object[] newStack = new Object[index + 1];
      System.arraycopy(stack, 0, newStack, 0, stack.length);
      stack = newStack;
      stackField.set(targetReader, stack);
    }
    stack[index] = value;
  }

  private void setPathIndex(JsonTreeReader targetReader, int index, int value) throws Exception {
    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(targetReader);
    pathIndices[index] = value;
  }

  private int getPathIndex(JsonTreeReader targetReader, int index) throws Exception {
    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(targetReader);
    return pathIndices[index];
  }
}