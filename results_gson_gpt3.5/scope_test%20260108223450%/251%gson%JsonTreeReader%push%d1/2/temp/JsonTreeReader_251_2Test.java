package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonTreeReader_251_2Test {

  private JsonTreeReader jsonTreeReader;
  private Method pushMethod;
  private Field stackField;
  private Field stackSizeField;
  private Field pathIndicesField;
  private Field pathNamesField;

  @BeforeEach
  public void setUp() throws Exception {
    // Create a minimal JsonElement mock to instantiate JsonTreeReader
    // Using Mockito to mock JsonElement as constructor requires it
    com.google.gson.JsonElement mockElement = mock(com.google.gson.JsonElement.class);
    jsonTreeReader = new JsonTreeReader(mockElement);

    // Access private push method
    pushMethod = JsonTreeReader.class.getDeclaredMethod("push", Object.class);
    pushMethod.setAccessible(true);

    // Access private fields used in push
    stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);

    stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);

    pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);

    pathNamesField = JsonTreeReader.class.getDeclaredField("pathNames");
    pathNamesField.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  public void testPushAddsElementWhenStackNotFull() throws Exception {
    // Arrange
    Object elementToPush = new Object();

    // Initial stackSize should be 0
    stackSizeField.setInt(jsonTreeReader, 0);
    Object[] stack = (Object[]) stackField.get(jsonTreeReader);
    int initialLength = stack.length;

    // Act
    pushMethod.invoke(jsonTreeReader, elementToPush);

    // Assert
    int newStackSize = stackSizeField.getInt(jsonTreeReader);
    assertEquals(1, newStackSize, "stackSize should increment by 1");

    Object[] updatedStack = (Object[]) stackField.get(jsonTreeReader);
    assertSame(elementToPush, updatedStack[0], "New top element should be pushed onto stack");
    assertEquals(initialLength, updatedStack.length, "Stack length should not change when not full");

    // pathIndices and pathNames length should remain unchanged
    int[] pathIndices = (int[]) pathIndicesField.get(jsonTreeReader);
    String[] pathNames = (String[]) pathNamesField.get(jsonTreeReader);
    assertEquals(initialLength, pathIndices.length);
    assertEquals(initialLength, pathNames.length);
  }

  @Test
    @Timeout(8000)
  public void testPushDoublesStackSizeAndCopiesArraysWhenFull() throws Exception {
    // Arrange
    // Set stackSize to stack.length to trigger resizing
    Object[] oldStack = (Object[]) stackField.get(jsonTreeReader);
    int oldLength = oldStack.length;
    stackSizeField.setInt(jsonTreeReader, oldLength);

    // Pre-fill stack with dummy objects
    Object[] newStack = new Object[oldLength];
    for (int i = 0; i < oldLength; i++) {
      newStack[i] = new Object();
    }
    stackField.set(jsonTreeReader, newStack);

    // Pre-fill pathIndices and pathNames
    int[] oldPathIndices = new int[oldLength];
    for (int i = 0; i < oldLength; i++) {
      oldPathIndices[i] = i;
    }
    pathIndicesField.set(jsonTreeReader, oldPathIndices);

    String[] oldPathNames = new String[oldLength];
    for (int i = 0; i < oldLength; i++) {
      oldPathNames[i] = "name" + i;
    }
    pathNamesField.set(jsonTreeReader, oldPathNames);

    Object elementToPush = new Object();

    // Act
    pushMethod.invoke(jsonTreeReader, elementToPush);

    // Assert
    int newStackSize = stackSizeField.getInt(jsonTreeReader);
    assertEquals(oldLength + 1, newStackSize, "stackSize should increment by 1");

    Object[] updatedStack = (Object[]) stackField.get(jsonTreeReader);
    assertEquals(oldLength * 2, updatedStack.length, "Stack array length should double");
    assertSame(elementToPush, updatedStack[oldLength], "New element should be added at previous stackSize index");

    // Verify old elements preserved
    for (int i = 0; i < oldLength; i++) {
      assertSame(newStack[i], updatedStack[i], "Existing stack elements should be preserved");
    }

    int[] updatedPathIndices = (int[]) pathIndicesField.get(jsonTreeReader);
    assertEquals(oldLength * 2, updatedPathIndices.length, "pathIndices array length should double");
    for (int i = 0; i < oldLength; i++) {
      assertEquals(oldPathIndices[i], updatedPathIndices[i], "pathIndices should be preserved");
    }

    String[] updatedPathNames = (String[]) pathNamesField.get(jsonTreeReader);
    assertEquals(oldLength * 2, updatedPathNames.length, "pathNames array length should double");
    for (int i = 0; i < oldLength; i++) {
      assertEquals(oldPathNames[i], updatedPathNames[i], "pathNames should be preserved");
    }
  }
}