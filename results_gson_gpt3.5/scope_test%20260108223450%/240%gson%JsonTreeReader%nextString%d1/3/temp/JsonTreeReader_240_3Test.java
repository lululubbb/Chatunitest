package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
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

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class JsonTreeReader_240_3Test {

  private JsonTreeReader reader;

  @BeforeEach
  public void setUp() throws Exception {
    // Create a JsonPrimitive to start with
    JsonPrimitive primitive = new JsonPrimitive("testString");
    // Use constructor JsonTreeReader(JsonElement element)
    reader = new JsonTreeReader(primitive);

    // Use reflection to set the internal stack and stackSize for testing
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(reader);
    stack[0] = primitive;
    stackField.set(reader, stack);

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(reader, 1);

    // Reset pathIndices array
    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(reader);
    pathIndices[0] = 0;
    pathIndicesField.set(reader, pathIndices);
  }

  @Test
    @Timeout(8000)
  public void nextString_withStringToken_returnsString() throws Exception {
    // Spy the reader and mock peek() to return STRING
    JsonTreeReader spyReader = spy(reader);
    doReturn(JsonToken.STRING).when(spyReader).peek();

    // Reset pathIndices array to size 32 to avoid index issues
    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(spyReader);
    pathIndices[0] = 0;
    pathIndicesField.set(spyReader, pathIndices);

    // Reset stackSize to 1 before call to avoid ArrayIndexOutOfBoundsException
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(spyReader, 1);

    // Setup stack with JsonPrimitive holding a string on spyReader
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(spyReader);
    stack[0] = new JsonPrimitive("testString");
    stackField.set(spyReader, stack);

    String result = spyReader.nextString();

    assertEquals("testString", result);

    // Verify that pathIndices[stackSize - 1] incremented by 1
    int[] updatedPathIndices = (int[]) pathIndicesField.get(spyReader);
    assertEquals(1, updatedPathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void nextString_withNumberToken_returnsNumberAsString() throws Exception {
    // Setup stack with JsonPrimitive holding a number on reader
    JsonPrimitive numberPrimitive = new JsonPrimitive(12345);
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(reader);
    stack[0] = numberPrimitive;
    stackField.set(reader, stack);

    // Reset stackSize to 1 on reader
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(reader, 1);

    // Reset pathIndices array on reader
    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(reader);
    pathIndices[0] = 0;
    pathIndicesField.set(reader, pathIndices);

    // Spy and mock peek() to return NUMBER
    JsonTreeReader spyReader = spy(reader);
    doReturn(JsonToken.NUMBER).when(spyReader).peek();

    // Reset stackSize to 1 on spyReader as well
    stackSizeField.setInt(spyReader, 1);

    // Setup stack with JsonPrimitive holding a number on spyReader
    Object[] spyStack = (Object[]) stackField.get(spyReader);
    spyStack[0] = new JsonPrimitive(12345);
    stackField.set(spyReader, spyStack);

    // Reset pathIndices array on spyReader
    int[] spyPathIndices = (int[]) pathIndicesField.get(spyReader);
    spyPathIndices[0] = 0;
    pathIndicesField.set(spyReader, spyPathIndices);

    String result = spyReader.nextString();

    assertEquals("12345", result);

    // Verify pathIndices incremented on spyReader
    int[] updatedPathIndices = (int[]) pathIndicesField.get(spyReader);
    assertEquals(1, updatedPathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void nextString_withInvalidToken_throwsIllegalStateException() throws Exception {
    JsonTreeReader spyReader = spy(reader);
    doReturn(JsonToken.BEGIN_OBJECT).when(spyReader).peek();

    IllegalStateException thrown = assertThrows(IllegalStateException.class, spyReader::nextString);
    assertTrue(thrown.getMessage().contains("Expected STRING but was BEGIN_OBJECT"));
  }

  @Test
    @Timeout(8000)
  public void nextString_stackSizeZero_doesNotIncrementPathIndices() throws Exception {
    // Setup stackSize to 0 on reader
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(reader, 0);

    // Spy and mock peek() to return STRING
    JsonTreeReader spyReader = spy(reader);
    doReturn(JsonToken.STRING).when(spyReader).peek();

    // Setup stack with JsonPrimitive holding a string on spyReader
    JsonPrimitive primitive = new JsonPrimitive("zeroStack");
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(spyReader);
    stack[0] = primitive;
    stackField.set(spyReader, stack);

    // Reset pathIndices array on spyReader
    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(spyReader);
    pathIndices[0] = 0;
    pathIndicesField.set(spyReader, pathIndices);

    // Ensure stackSize is 0 on spyReader as well
    stackSizeField.setInt(spyReader, 0);

    String result = spyReader.nextString();

    assertEquals("zeroStack", result);

    // pathIndices should not be incremented because stackSize == 0
    // So pathIndices[stackSize - 1] is invalid, just check no exception and pathIndices unchanged
    int[] updatedPathIndices = (int[]) pathIndicesField.get(spyReader);
    assertEquals(0, updatedPathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void nextString_invokesPopStack_privateMethod() throws Exception {
    // Spy to mock peek() to return STRING
    JsonTreeReader spyReader = spy(reader);
    doReturn(JsonToken.STRING).when(spyReader).peek();

    // Use reflection to call popStack() directly and verify it returns correct object
    Method popStackMethod = JsonTreeReader.class.getDeclaredMethod("popStack");
    popStackMethod.setAccessible(true);

    // Before popStack, stackSize = 1
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    int beforeSize = stackSizeField.getInt(spyReader);
    assertEquals(1, beforeSize);

    Object popped = popStackMethod.invoke(spyReader);
    assertTrue(popped instanceof JsonPrimitive);

    // After popStack, stackSize should be decreased by 1
    int afterSize = stackSizeField.getInt(spyReader);
    assertEquals(beforeSize - 1, afterSize);
  }
}