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

class JsonTreeReader_245_2Test {

  private JsonTreeReader jsonTreeReader;

  @BeforeEach
  void setUp() throws Exception {
    // Create a dummy JsonPrimitive to initialize JsonTreeReader
    JsonPrimitive element = new JsonPrimitive(0);
    jsonTreeReader = new JsonTreeReader(element);
  }

  @Test
    @Timeout(8000)
  void nextInt_withNumberToken_returnsIntAndUpdatesState() throws Exception {
    // Arrange
    JsonTreeReader spyReader = spy(jsonTreeReader);

    // Setup stack, stackSize, pathIndices, and pathNames for spyReader
    setStackForInstance(spyReader, new Object[]{new JsonPrimitive(42)});
    setStackSizeForInstance(spyReader, 1);
    setPathIndicesForInstance(spyReader, new int[]{0});
    setPathNamesForInstance(spyReader, new String[32]);

    doReturn(JsonToken.NUMBER).when(spyReader).peek();

    // Act
    int result = spyReader.nextInt();

    // Assert
    assertEquals(42, result);
    assertEquals(0, getStackSizeFromInstance(spyReader));
    assertEquals(1, getPathIndicesFromInstance(spyReader)[0]);
  }

  @Test
    @Timeout(8000)
  void nextInt_withStringToken_returnsIntAndUpdatesState() throws Exception {
    // Arrange
    JsonTreeReader spyReader = spy(jsonTreeReader);

    // Setup stack, stackSize, pathIndices, and pathNames for spyReader
    setStackForInstance(spyReader, new Object[]{new JsonPrimitive("123")});
    setStackSizeForInstance(spyReader, 1);
    setPathIndicesForInstance(spyReader, new int[]{5});
    setPathNamesForInstance(spyReader, new String[32]);

    doReturn(JsonToken.STRING).when(spyReader).peek();

    // Act
    int result = spyReader.nextInt();

    // Assert
    assertEquals(123, result);
    assertEquals(0, getStackSizeFromInstance(spyReader));
    assertEquals(6, getPathIndicesFromInstance(spyReader)[0]);
  }

  @Test
    @Timeout(8000)
  void nextInt_withInvalidToken_throwsIllegalStateException() throws Exception {
    JsonTreeReader spyReader = spy(jsonTreeReader);
    doReturn(JsonToken.BEGIN_ARRAY).when(spyReader).peek();

    IllegalStateException thrown = assertThrows(IllegalStateException.class, spyReader::nextInt);
    assertTrue(thrown.getMessage().contains("Expected NUMBER but was BEGIN_ARRAY"));
  }

  // Helper methods to set private fields via reflection

  private void setStack(Object[] stack) throws Exception {
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    stackField.set(jsonTreeReader, stack);
  }

  private Object[] getStack() throws Exception {
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    return (Object[]) stackField.get(jsonTreeReader);
  }

  private void setStackSize(int size) throws Exception {
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonTreeReader, size);
  }

  private int getStackSize() throws Exception {
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    return stackSizeField.getInt(jsonTreeReader);
  }

  private void setPathIndices(int[] indices) throws Exception {
    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    pathIndicesField.set(jsonTreeReader, indices);
  }

  private int[] getPathIndices() throws Exception {
    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    return (int[]) pathIndicesField.get(jsonTreeReader);
  }

  private void setPathNames(String[] names) throws Exception {
    Field pathNamesField = JsonTreeReader.class.getDeclaredField("pathNames");
    pathNamesField.setAccessible(true);
    pathNamesField.set(jsonTreeReader, names);
  }

  // Helpers for instance fields (for spyReader)

  private void setStackForInstance(JsonTreeReader instance, Object[] stack) throws Exception {
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    stackField.set(instance, stack);
  }

  private Object[] getStackFromInstance(JsonTreeReader instance) throws Exception {
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    return (Object[]) stackField.get(instance);
  }

  private void setStackSizeForInstance(JsonTreeReader instance, int size) throws Exception {
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(instance, size);
  }

  private int getStackSizeFromInstance(JsonTreeReader instance) throws Exception {
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    return stackSizeField.getInt(instance);
  }

  private void setPathIndicesForInstance(JsonTreeReader instance, int[] indices) throws Exception {
    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    pathIndicesField.set(instance, indices);
  }

  private int[] getPathIndicesFromInstance(JsonTreeReader instance) throws Exception {
    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    return (int[]) pathIndicesField.get(instance);
  }

  private void setPathNamesForInstance(JsonTreeReader instance, String[] names) throws Exception {
    Field pathNamesField = JsonTreeReader.class.getDeclaredField("pathNames");
    pathNamesField.setAccessible(true);
    pathNamesField.set(instance, names);
  }
}