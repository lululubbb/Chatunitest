package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.Reader;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

public class JsonTreeReader_239_5Test {

  private JsonTreeReader jsonTreeReader;

  @BeforeEach
  public void setUp() {
    // Initialize with a dummy JsonElement to allow stack manipulation
    jsonTreeReader = new JsonTreeReader(new JsonObject());
  }

  /**
   * Helper method to invoke private nextName(boolean) via reflection.
   */
  private String invokeNextName(boolean skipName) throws Exception {
    Method method = JsonTreeReader.class.getDeclaredMethod("nextName", boolean.class);
    method.setAccessible(true);
    try {
      return (String) method.invoke(jsonTreeReader, skipName);
    } catch (InvocationTargetException e) {
      // unwrap IOException if thrown by nextName
      Throwable cause = e.getCause();
      if (cause instanceof IOException) {
        throw (IOException) cause;
      }
      throw e;
    }
  }

  @Test
    @Timeout(8000)
  public void testNextName_withEmptyStack_throwsIllegalStateException() {
    // Clear stack to simulate empty stack condition
    setStackSize(0);
    assertThrows(IllegalStateException.class, () -> invokeNextName(false));
  }

  @Test
    @Timeout(8000)
  public void testNextName_withTopOfStackNotJsonObject_throwsIllegalStateException() {
    // Push a JsonArray instead of JsonObject
    setStack(new Object[]{new JsonArray()}, 1);
    assertThrows(IllegalStateException.class, () -> invokeNextName(false));
  }

  @Test
    @Timeout(8000)
  public void testNextName_withEmptyJsonObject_throwsIllegalStateException() throws Exception {
    JsonObject emptyObject = new JsonObject();
    setStack(new Object[]{emptyObject}, 1);
    // Empty JsonObject has no entrySet iterator, so expect IllegalStateException
    assertThrows(IllegalStateException.class, () -> invokeNextName(false));
  }

  @Test
    @Timeout(8000)
  public void testNextName_withNonEmptyJsonObject_returnsNextName() throws Exception {
    JsonObject object = new JsonObject();
    object.add("key1", new JsonPrimitive("value1"));
    object.add("key2", new JsonPrimitive("value2"));

    // Prepare iterator over entrySet
    Iterator<Map.Entry<String, JsonElement>> iterator = object.entrySet().iterator();

    // Setup stack with the JsonObject and an iterator over entrySet at stack[stackSize-1]
    Object[] stack = new Object[32];
    stack[0] = object;
    stack[1] = iterator;
    setStack(stack, 2);

    // pathNames and pathIndices must be set appropriately for nextName to update them
    setPathNames(new String[32]);
    setPathIndices(new int[32]);

    String name = invokeNextName(false);
    assertEquals("key1", name);

    // The iterator should have advanced, so next call returns next key
    String name2 = invokeNextName(false);
    assertEquals("key2", name2);
  }

  @Test
    @Timeout(8000)
  public void testNextName_withSkipNameTrue_returnsNextNameWithoutUpdatingPathNames() throws Exception {
    JsonObject object = new JsonObject();
    object.add("key", new JsonPrimitive("value"));

    Iterator<Map.Entry<String, JsonElement>> iterator = object.entrySet().iterator();

    Object[] stack = new Object[32];
    stack[0] = object;
    stack[1] = iterator;
    setStack(stack, 2);

    String[] pathNames = new String[32];
    pathNames[0] = "oldName";
    setPathNames(pathNames);

    int[] pathIndices = new int[32];
    setPathIndices(pathIndices);

    String name = invokeNextName(true);
    assertEquals("key", name);
    // pathNames[stackSize-1] should remain unchanged because skipName is true
    assertEquals("oldName", pathNames[0]);
  }

  @Test
    @Timeout(8000)
  public void testNextName_withIteratorHasNoNext_throwsIllegalStateException() throws Exception {
    JsonObject object = new JsonObject();
    Iterator<Map.Entry<String, JsonElement>> emptyIterator = new Iterator<Map.Entry<String, JsonElement>>() {
      @Override
      public boolean hasNext() {
        return false;
      }
      @Override
      public Map.Entry<String, JsonElement> next() {
        return null;
      }
    };
    Object[] stack = new Object[32];
    stack[0] = object;
    stack[1] = emptyIterator;
    setStack(stack, 2);

    assertThrows(IllegalStateException.class, () -> invokeNextName(false));
  }

  // Helper methods to set private fields via reflection

  private void setStack(Object[] stackArray, int size) {
    try {
      var stackField = JsonTreeReader.class.getDeclaredField("stack");
      stackField.setAccessible(true);
      stackField.set(jsonTreeReader, stackArray);

      var stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
      stackSizeField.setAccessible(true);
      stackSizeField.setInt(jsonTreeReader, size);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void setStackSize(int size) {
    try {
      var stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
      stackSizeField.setAccessible(true);
      stackSizeField.setInt(jsonTreeReader, size);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void setPathNames(String[] pathNames) {
    try {
      var pathNamesField = JsonTreeReader.class.getDeclaredField("pathNames");
      pathNamesField.setAccessible(true);
      pathNamesField.set(jsonTreeReader, pathNames);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void setPathIndices(int[] pathIndices) {
    try {
      var pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
      pathIndicesField.setAccessible(true);
      pathIndicesField.set(jsonTreeReader, pathIndices);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}