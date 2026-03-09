package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JsonTreeReader_255_3Test {

  private JsonTreeReader jsonTreeReader;
  private Method locationStringMethod;
  private Method pushMethod;
  private Method getPathMethod;

  @BeforeEach
  public void setUp() throws NoSuchMethodException {
    // Initialize with a simple JsonElement, e.g. JsonNull.INSTANCE
    jsonTreeReader = new JsonTreeReader(JsonNull.INSTANCE);
    locationStringMethod = JsonTreeReader.class.getDeclaredMethod("locationString");
    locationStringMethod.setAccessible(true);

    pushMethod = JsonTreeReader.class.getDeclaredMethod("push", Object.class);
    pushMethod.setAccessible(true);

    getPathMethod = JsonTreeReader.class.getDeclaredMethod("getPath", boolean.class);
    getPathMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  public void testLocationString_emptyPath() throws InvocationTargetException, IllegalAccessException {
    // When stack is empty, getPath() should return "$"
    // So locationString() returns " at path $"
    String result = (String) locationStringMethod.invoke(jsonTreeReader);
    assertEquals(" at path $", result);
  }

  @Test
    @Timeout(8000)
  public void testLocationString_withArrayIndex() throws Exception {
    // Push an array and set pathIndices to simulate an array index
    JsonArray array = new JsonArray();
    jsonTreeReader = new JsonTreeReader(array);

    // Clear stack and stackSize first
    setField(jsonTreeReader, "stackSize", 0);

    // Use reflection to push an array on the stack and set stackSize = 1
    pushMethod.invoke(jsonTreeReader, array);

    // Set stackSize to 1
    setField(jsonTreeReader, "stackSize", 1);
    // Set pathIndices[0] = 3 (simulate 4th element)
    int[] pathIndices = (int[]) getField(jsonTreeReader, "pathIndices");
    pathIndices[0] = 3;
    // Set pathNames[0] = null
    String[] pathNames = (String[]) getField(jsonTreeReader, "pathNames");
    pathNames[0] = null;

    // Set 'stack' element type to JsonArray to trigger array index path
    Object[] stack = (Object[]) getField(jsonTreeReader, "stack");
    stack[0] = array;

    // Set 'pathIndices' and 'pathNames' arrays back to the object to ensure changes are reflected
    setField(jsonTreeReader, "pathIndices", pathIndices);
    setField(jsonTreeReader, "pathNames", pathNames);
    setField(jsonTreeReader, "stack", stack);

    // The stackSize is 1, so getPath(false) includes this element
    // getPath() calls getPath(false) internally

    String result = (String) locationStringMethod.invoke(jsonTreeReader);
    assertEquals(" at path $[3]", result);
  }

  @Test
    @Timeout(8000)
  public void testLocationString_withObjectName() throws Exception {
    // Push an object and set pathNames to simulate a name
    JsonObject object = new JsonObject();
    jsonTreeReader = new JsonTreeReader(object);

    // Clear stack and stackSize first
    setField(jsonTreeReader, "stackSize", 0);

    pushMethod.invoke(jsonTreeReader, object);

    setField(jsonTreeReader, "stackSize", 1);
    // Set pathNames[0] = "key"
    String[] pathNames = (String[]) getField(jsonTreeReader, "pathNames");
    pathNames[0] = "key";
    // Set pathIndices[0] = -1 to indicate no array index
    int[] pathIndices = (int[]) getField(jsonTreeReader, "pathIndices");
    pathIndices[0] = -1;

    // Set 'stack' element type to JsonObject to trigger object name path
    Object[] stack = (Object[]) getField(jsonTreeReader, "stack");
    stack[0] = object;

    setField(jsonTreeReader, "pathNames", pathNames);
    setField(jsonTreeReader, "pathIndices", pathIndices);
    setField(jsonTreeReader, "stack", stack);

    String result = (String) locationStringMethod.invoke(jsonTreeReader);
    assertEquals(" at path $.key", result);
  }

  @Test
    @Timeout(8000)
  public void testLocationString_withMultipleStackElements() throws Exception {
    // Push an object then an array to simulate path $.key[2]
    JsonObject object = new JsonObject();
    JsonArray array = new JsonArray();

    jsonTreeReader = new JsonTreeReader(JsonNull.INSTANCE);

    // Clear stack and stackSize first
    setField(jsonTreeReader, "stackSize", 0);

    pushMethod.invoke(jsonTreeReader, object);
    pushMethod.invoke(jsonTreeReader, array);

    setField(jsonTreeReader, "stackSize", 2);

    String[] pathNames = (String[]) getField(jsonTreeReader, "pathNames");
    int[] pathIndices = (int[]) getField(jsonTreeReader, "pathIndices");

    pathNames[0] = "key";
    pathIndices[0] = -1;

    pathNames[1] = null;
    pathIndices[1] = 2;

    // Set 'stack' elements to correct types to trigger path building
    Object[] stack = (Object[]) getField(jsonTreeReader, "stack");
    stack[0] = object;
    stack[1] = array;

    setField(jsonTreeReader, "pathNames", pathNames);
    setField(jsonTreeReader, "pathIndices", pathIndices);
    setField(jsonTreeReader, "stack", stack);

    String result = (String) locationStringMethod.invoke(jsonTreeReader);
    assertEquals(" at path $.key[2]", result);
  }

  private Object getField(Object instance, String fieldName) throws Exception {
    var field = instance.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(instance);
  }

  private void setField(Object instance, String fieldName, Object value) throws Exception {
    var field = instance.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(instance, value);
  }
}