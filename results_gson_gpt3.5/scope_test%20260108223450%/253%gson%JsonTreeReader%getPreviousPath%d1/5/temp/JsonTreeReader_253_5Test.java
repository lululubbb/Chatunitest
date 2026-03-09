package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class JsonTreeReader_253_5Test {

  private JsonTreeReader jsonTreeReader;

  @BeforeEach
  public void setUp() {
    // Initialize with a JsonObject for testing
    jsonTreeReader = new JsonTreeReader(new JsonObject());
  }

  @Test
    @Timeout(8000)
  public void testGetPreviousPath_emptyStack_returnsRoot() throws Exception {
    // stackSize is 0 initially, so path should be "$"
    String previousPath = (String) invokeGetPath(true);
    assertEquals("$", previousPath);
  }

  @Test
    @Timeout(8000)
  public void testGetPreviousPath_withArrayAndObjectPath() throws Exception {
    // Setup stack, pathNames and pathIndices to simulate a path like $.store.book[3].title
    setStackSize(5);

    // Set stack elements (not used directly by getPath but for completeness)
    setStackElement(0, new JsonObject());
    setStackElement(1, new JsonObject());
    setStackElement(2, new JsonArray());
    setStackElement(3, new JsonObject());
    setStackElement(4, new JsonPrimitive("title"));

    // pathNames correspond to object keys for stack elements that are objects
    setPathName(0, null);
    setPathName(1, "store");
    setPathName(2, "book");
    setPathName(3, null);
    setPathName(4, "title");

    // pathIndices correspond to array indices for stack elements that are arrays
    setPathIndex(0, -1);
    setPathIndex(1, -1);
    setPathIndex(2, 3);  // set to 3 directly for getPath(true) because getPath(true) subtracts 1 internally
    setPathIndex(3, -1);
    setPathIndex(4, -1);

    String previousPath = (String) invokeGetPath(true);
    assertEquals("$.store.book[3].title", previousPath);
  }

  @Test
    @Timeout(8000)
  public void testGetPreviousPath_withPreviousPathFalse() throws Exception {
    // Setup stackSize and pathNames/pathIndices similar to previous test
    setStackSize(3);

    setStackElement(0, new JsonObject());
    setStackElement(1, new JsonArray());
    setStackElement(2, new JsonPrimitive("value"));

    setPathName(0, null);
    setPathName(1, null);
    setPathName(2, null);

    setPathIndex(0, 0);
    setPathIndex(1, 1); // set to 1 because getPath(false) uses pathIndices as is
    setPathIndex(2, -1);

    String currentPath = (String) invokeGetPath(false);
    assertEquals("$[0][1]", currentPath);
  }

  @Test
    @Timeout(8000)
  public void testGetPreviousPath_withNullPathNameAndIndex() throws Exception {
    setStackSize(2);

    setStackElement(0, new JsonObject());
    setStackElement(1, new JsonPrimitive("value"));

    setPathName(0, null);
    setPathName(1, null);

    setPathIndex(0, -1);
    setPathIndex(1, -1);

    String previousPath = (String) invokeGetPath(true);
    assertEquals("$", previousPath);
  }

  // Helper methods to access private getPath(boolean)
  private Object invokeGetPath(boolean usePreviousPath) throws Exception {
    Method method = JsonTreeReader.class.getDeclaredMethod("getPath", boolean.class);
    method.setAccessible(true);
    return method.invoke(jsonTreeReader, usePreviousPath);
  }

  private void setStackSize(int size) throws Exception {
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonTreeReader, size);
  }

  private void setStackElement(int index, Object value) throws Exception {
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(jsonTreeReader);
    if (index >= stack.length) {
      // Expand array if needed
      Object[] newStack = new Object[index + 1];
      System.arraycopy(stack, 0, newStack, 0, stack.length);
      stack = newStack;
      stackField.set(jsonTreeReader, stack);
    }
    stack[index] = value;
  }

  private void setPathName(int index, String name) throws Exception {
    Field pathNamesField = JsonTreeReader.class.getDeclaredField("pathNames");
    pathNamesField.setAccessible(true);
    String[] pathNames = (String[]) pathNamesField.get(jsonTreeReader);
    if (index >= pathNames.length) {
      String[] newPathNames = new String[index + 1];
      System.arraycopy(pathNames, 0, newPathNames, 0, pathNames.length);
      pathNames = newPathNames;
      pathNamesField.set(jsonTreeReader, pathNames);
    }
    pathNames[index] = name;
  }

  private void setPathIndex(int index, int value) throws Exception {
    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(jsonTreeReader);
    if (index >= pathIndices.length) {
      int[] newPathIndices = new int[index + 1];
      System.arraycopy(pathIndices, 0, newPathIndices, 0, pathIndices.length);
      pathIndices = newPathIndices;
      pathIndicesField.set(jsonTreeReader, pathIndices);
    }
    pathIndices[index] = value;
  }
}