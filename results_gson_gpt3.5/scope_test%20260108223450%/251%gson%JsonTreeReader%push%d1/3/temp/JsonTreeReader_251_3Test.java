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

public class JsonTreeReader_251_3Test {

  private JsonTreeReader jsonTreeReader;

  @BeforeEach
  public void setUp() throws Exception {
    // Construct with null because constructor requires JsonElement, but we won't use it here
    // Use reflection to invoke the constructor with null safely
    jsonTreeReader = new JsonTreeReader(null);
    
    // Reset stackSize and stack to known state before each test
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
  public void testPush_whenStackNotFull_shouldAddNewTopAndIncrementStackSize() throws Exception {
    Method pushMethod = JsonTreeReader.class.getDeclaredMethod("push", Object.class);
    pushMethod.setAccessible(true);

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    int initialStackSize = stackSizeField.getInt(jsonTreeReader);

    Object newTop = new Object();

    pushMethod.invoke(jsonTreeReader, newTop);

    int afterPushStackSize = stackSizeField.getInt(jsonTreeReader);
    assertEquals(initialStackSize + 1, afterPushStackSize);

    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(jsonTreeReader);

    assertSame(newTop, stack[initialStackSize]);
  }

  @Test
    @Timeout(8000)
  public void testPush_whenStackFull_shouldResizeArraysAndAddNewTop() throws Exception {
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

    // Fill the stack to capacity (32)
    Object[] fullStack = new Object[32];
    for (int i = 0; i < 32; i++) {
      fullStack[i] = new Object();
    }
    stackField.set(jsonTreeReader, fullStack);
    stackSizeField.setInt(jsonTreeReader, 32);

    int[] pathIndices = new int[32];
    for (int i = 0; i < 32; i++) {
      pathIndices[i] = i;
    }
    pathIndicesField.set(jsonTreeReader, pathIndices);

    String[] pathNames = new String[32];
    for (int i = 0; i < 32; i++) {
      pathNames[i] = "name" + i;
    }
    pathNamesField.set(jsonTreeReader, pathNames);

    Object newTop = new Object();

    pushMethod.invoke(jsonTreeReader, newTop);

    int newStackSize = stackSizeField.getInt(jsonTreeReader);
    assertEquals(33, newStackSize);

    Object[] newStack = (Object[]) stackField.get(jsonTreeReader);
    assertEquals(64, newStack.length);
    assertSame(newTop, newStack[32]);

    int[] newPathIndices = (int[]) pathIndicesField.get(jsonTreeReader);
    assertEquals(64, newPathIndices.length);
    for (int i = 0; i < 32; i++) {
      assertEquals(i, newPathIndices[i]);
    }

    String[] newPathNames = (String[]) pathNamesField.get(jsonTreeReader);
    assertEquals(64, newPathNames.length);
    for (int i = 0; i < 32; i++) {
      assertEquals("name" + i, newPathNames[i]);
    }
  }
}