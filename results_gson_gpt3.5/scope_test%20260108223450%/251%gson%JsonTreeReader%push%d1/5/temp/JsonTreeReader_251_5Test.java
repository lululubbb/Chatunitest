package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
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

import com.google.gson.JsonElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public class JsonTreeReader_251_5Test {

  private JsonTreeReader jsonTreeReader;

  @BeforeEach
  public void setUp() throws Exception {
    // Create a dummy JsonElement mock for constructor (not used in push)
    JsonElement element = mock(JsonElement.class);
    jsonTreeReader = new JsonTreeReader(element);

    // Reset stack and stackSize fields via reflection to known state
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    stackField.set(jsonTreeReader, new Object[32]);

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonTreeReader, 0);

    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    pathIndicesField.set(jsonTreeReader, new int[32]);

    Field pathNamesField = JsonTreeReader.class.getDeclaredField("pathNames");
    pathNamesField.setAccessible(true);
    pathNamesField.set(jsonTreeReader, new String[32]);
  }

  @Test
    @Timeout(8000)
  public void push_shouldAddElementAndIncrementStackSize() throws Exception {
    Method pushMethod = JsonTreeReader.class.getDeclaredMethod("push", Object.class);
    pushMethod.setAccessible(true);

    // Initial stackSize = 0
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);

    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);

    Object[] stackBefore = (Object[]) stackField.get(jsonTreeReader);

    Object newTop = "testElement";

    pushMethod.invoke(jsonTreeReader, newTop);

    int stackSizeAfter = stackSizeField.getInt(jsonTreeReader);
    assertEquals(1, stackSizeAfter);

    Object[] stackAfter = (Object[]) stackField.get(jsonTreeReader);
    assertSame(newTop, stackAfter[0]);

    // Other elements remain null
    for (int i = 1; i < stackAfter.length; i++) {
      assertNull(stackAfter[i]);
    }
  }

  @Test
    @Timeout(8000)
  public void push_shouldExpandStackArraysWhenFull() throws Exception {
    Method pushMethod = JsonTreeReader.class.getDeclaredMethod("push", Object.class);
    pushMethod.setAccessible(true);

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);

    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);

    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);

    Field pathNamesField = JsonTreeReader.class.getDeclaredField("pathNames");
    pathNamesField.setAccessible(true);

    // Fill the stack to its length (32)
    Object[] fullStack = new Object[32];
    for (int i = 0; i < 32; i++) {
      fullStack[i] = "elem" + i;
    }
    stackField.set(jsonTreeReader, fullStack);
    stackSizeField.setInt(jsonTreeReader, 32);

    int[] fullPathIndices = new int[32];
    for (int i = 0; i < 32; i++) {
      fullPathIndices[i] = i;
    }
    pathIndicesField.set(jsonTreeReader, fullPathIndices);

    String[] fullPathNames = new String[32];
    for (int i = 0; i < 32; i++) {
      fullPathNames[i] = "name" + i;
    }
    pathNamesField.set(jsonTreeReader, fullPathNames);

    Object newTop = "newTopElement";

    pushMethod.invoke(jsonTreeReader, newTop);

    int stackSizeAfter = stackSizeField.getInt(jsonTreeReader);
    assertEquals(33, stackSizeAfter);

    Object[] stackAfter = (Object[]) stackField.get(jsonTreeReader);
    assertEquals(64, stackAfter.length);

    // Verify old elements preserved
    for (int i = 0; i < 32; i++) {
      assertEquals("elem" + i, stackAfter[i]);
    }
    // Verify new element added at position 32
    assertEquals(newTop, stackAfter[32]);
    // Remaining elements null
    for (int i = 33; i < stackAfter.length; i++) {
      assertNull(stackAfter[i]);
    }

    int[] pathIndicesAfter = (int[]) pathIndicesField.get(jsonTreeReader);
    assertEquals(64, pathIndicesAfter.length);
    for (int i = 0; i < 32; i++) {
      assertEquals(i, pathIndicesAfter[i]);
    }
    for (int i = 32; i < pathIndicesAfter.length; i++) {
      assertEquals(0, pathIndicesAfter[i]);
    }

    String[] pathNamesAfter = (String[]) pathNamesField.get(jsonTreeReader);
    assertEquals(64, pathNamesAfter.length);
    for (int i = 0; i < 32; i++) {
      assertEquals("name" + i, pathNamesAfter[i]);
    }
    for (int i = 32; i < pathNamesAfter.length; i++) {
      assertNull(pathNamesAfter[i]);
    }
  }
}