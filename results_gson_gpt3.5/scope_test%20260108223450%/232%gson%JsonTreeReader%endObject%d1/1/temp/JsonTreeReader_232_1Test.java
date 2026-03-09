package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class JsonTreeReader_232_1Test {

  private JsonTreeReader jsonTreeReader;
  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() throws Exception {
    jsonObject = new JsonObject();
    jsonObject.addProperty("key", "value");
    jsonTreeReader = new JsonTreeReader(jsonObject);

    // We need to push an object and an iterator to stack to simulate state before endObject()
    // The stack top should be an iterator, below it the object

    Method pushMethod = JsonTreeReader.class.getDeclaredMethod("push", Object.class);
    pushMethod.setAccessible(true);

    // Push the JsonObject (the object to be ended)
    pushMethod.invoke(jsonTreeReader, jsonObject);

    // Push an iterator (simulate the iterator of the object's entry set)
    Object iterator = jsonObject.entrySet().iterator();
    pushMethod.invoke(jsonTreeReader, iterator);

    // Set stackSize accordingly (should be 2 now)
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonTreeReader, 2);

    // Initialize pathNames and pathIndices arrays at index 1 (stackSize - 1)
    Field pathNamesField = JsonTreeReader.class.getDeclaredField("pathNames");
    pathNamesField.setAccessible(true);
    String[] pathNames = (String[]) pathNamesField.get(jsonTreeReader);
    pathNames[1] = "key";

    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(jsonTreeReader);
    pathIndices[1] = 5;
  }

  @Test
    @Timeout(8000)
  public void testEndObject_Normal() throws Exception {
    // Create spy
    JsonTreeReader spyReader = spy(jsonTreeReader);

    Method endObjectMethod = JsonTreeReader.class.getDeclaredMethod("endObject");
    endObjectMethod.setAccessible(true);

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);

    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);

    Object[] stack = (Object[]) stackField.get(spyReader);

    // Clear stack and reset pathNames and pathIndices
    Field pathNamesField = JsonTreeReader.class.getDeclaredField("pathNames");
    pathNamesField.setAccessible(true);
    String[] pathNames = (String[]) pathNamesField.get(spyReader);

    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(spyReader);

    for (int i = 0; i < stack.length; i++) {
      stack[i] = null;
      pathNames[i] = null;
      pathIndices[i] = 0;
    }
    stackSizeField.setInt(spyReader, 0);

    Method pushMethod = JsonTreeReader.class.getDeclaredMethod("push", Object.class);
    pushMethod.setAccessible(true);

    // Push JsonObject first (bottom)
    pushMethod.invoke(spyReader, jsonObject);
    // Push iterator (middle)
    pushMethod.invoke(spyReader, jsonObject.entrySet().iterator());
    // Push JsonObject on top (to be ended)
    pushMethod.invoke(spyReader, jsonObject);

    stackSizeField.setInt(spyReader, 3);

    // Initialize pathNames and pathIndices for top element
    pathNames[2] = "key";
    pathIndices[2] = 5;

    // Now call endObject and catch IOException from expect
    try {
      endObjectMethod.invoke(spyReader);
    } catch (java.lang.reflect.InvocationTargetException e) {
      if (e.getCause() instanceof java.io.IOException) {
        // ignore IOException from expect, as we want to test other behavior
      } else {
        throw e;
      }
    }

    // After popping twice, stackSize should be 1
    int stackSizeAfter = stackSizeField.getInt(spyReader);
    assertEquals(1, stackSizeAfter);

    // pathNames[stackSize] should be null after endObject
    assertNull(pathNames[stackSizeAfter]);

    // pathIndices[stackSize - 1] should be incremented by 1 (from 0 to 1)
    assertEquals(1, pathIndices[stackSizeAfter - 1]);

    // pathNames[stackSize - 1] should not be null (should remain unchanged)
    assertNotNull(pathNames[stackSizeAfter - 1]);

    // Re-setup state for stackSize = 2 condition
    for (int i = 0; i < stack.length; i++) {
      stack[i] = null;
      pathNames[i] = null;
      pathIndices[i] = 0;
    }
    stackSizeField.setInt(spyReader, 0);

    // Push JsonObject first (bottom)
    pushMethod.invoke(spyReader, jsonObject);
    // Push iterator (top)
    pushMethod.invoke(spyReader, jsonObject.entrySet().iterator());

    stackSizeField.setInt(spyReader, 2);

    pathIndices[1] = 5;
    pathNames[1] = "key";

    // Call endObject again via reflection and expect IOException (since top is iterator, expect will fail)
    try {
      endObjectMethod.invoke(spyReader);
      fail("Expected IOException due to invalid stack state");
    } catch (java.lang.reflect.InvocationTargetException e) {
      assertTrue(e.getCause() instanceof java.io.IOException);
    }

    // Now fix stack for endObject call: top must be JsonObject, below iterator
    for (int i = 0; i < stack.length; i++) {
      stack[i] = null;
      pathNames[i] = null;
      pathIndices[i] = 0;
    }
    stackSizeField.setInt(spyReader, 0);

    // Push iterator first (bottom)
    pushMethod.invoke(spyReader, jsonObject.entrySet().iterator());
    // Push JsonObject on top
    pushMethod.invoke(spyReader, jsonObject);

    stackSizeField.setInt(spyReader, 2);

    pathIndices[1] = 5;
    pathNames[1] = "key";

    // Call endObject again and catch IOException from expect
    try {
      endObjectMethod.invoke(spyReader);
    } catch (java.lang.reflect.InvocationTargetException e) {
      if (e.getCause() instanceof java.io.IOException) {
        // ignore IOException from expect
      } else {
        throw e;
      }
    }

    // After popping twice, stackSize should be 0
    int stackSizeAfterSecond = stackSizeField.getInt(spyReader);
    assertEquals(0, stackSizeAfterSecond);

    // Reset stackSize to 3 and push accordingly with correct order:
    for (int i = 0; i < stack.length; i++) {
      stack[i] = null;
      pathNames[i] = null;
      pathIndices[i] = 0;
    }
    stackSizeField.setInt(spyReader, 0);

    // Push bottom object
    pushMethod.invoke(spyReader, jsonObject);
    // Push iterator
    pushMethod.invoke(spyReader, jsonObject.entrySet().iterator());
    // Push top object (to be ended)
    pushMethod.invoke(spyReader, jsonObject);
    stackSizeField.setInt(spyReader, 3);

    pathIndices[2] = 5;
    pathNames[2] = "name";

    // Call endObject again via reflection and ignore IOException
    try {
      endObjectMethod.invoke(spyReader);
    } catch (java.lang.reflect.InvocationTargetException e) {
      if (e.getCause() instanceof java.io.IOException) {
        // ignore IOException from expect
      } else {
        throw e;
      }
    }

    // After popping twice, stackSize should be 1
    int stackSizeAfterThird = stackSizeField.getInt(spyReader);
    assertEquals(1, stackSizeAfterThird);

    // pathIndices[stackSize - 1] = pathIndices[0] should be incremented by 1 (from 0 to 1)
    assertEquals(1, pathIndices[0]);

    // pathNames[stackSize - 1] should not be null (should remain unchanged)
    assertNotNull(pathNames[stackSizeAfterThird - 1]);
    // pathNames[stackSize] should be null
    assertNull(pathNames[stackSizeAfterThird]);
  }
}