package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonNull;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class JsonTreeReader_255_6Test {

  private JsonTreeReader jsonTreeReader;
  private Method locationStringMethod;
  private Method getPathMethod;

  @BeforeEach
  public void setUp() throws NoSuchMethodException {
    JsonObject jsonObject = new JsonObject();
    jsonTreeReader = new JsonTreeReader(jsonObject);

    locationStringMethod = JsonTreeReader.class.getDeclaredMethod("locationString");
    locationStringMethod.setAccessible(true);

    getPathMethod = JsonTreeReader.class.getDeclaredMethod("getPath");
    getPathMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  public void testLocationString_emptyPath() throws InvocationTargetException, IllegalAccessException {
    // By default, path should be "$"
    String expectedPath = (String) getPathMethod.invoke(jsonTreeReader);
    String locationString = (String) locationStringMethod.invoke(jsonTreeReader);
    assertEquals(" at path " + expectedPath, locationString);
  }

  @Test
    @Timeout(8000)
  public void testLocationString_withArrayPath() throws Exception {
    // Push a JsonArray and set pathIndices to simulate array element
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(new JsonPrimitive("value"));
    pushToStack(jsonArray);
    setStackSize(1);
    setPathIndices(0, 2);
    setPathNames(0, null);

    String expectedPath = (String) getPathMethod.invoke(jsonTreeReader);
    String locationString = (String) locationStringMethod.invoke(jsonTreeReader);
    assertEquals(" at path " + expectedPath, locationString);
  }

  @Test
    @Timeout(8000)
  public void testLocationString_withObjectPath() throws Exception {
    // Push a JsonObject and set pathNames to simulate object property
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("key", "value");
    pushToStack(jsonObject);
    setStackSize(1);
    setPathIndices(0, 0);
    setPathNames(0, "key");

    String expectedPath = (String) getPathMethod.invoke(jsonTreeReader);
    String locationString = (String) locationStringMethod.invoke(jsonTreeReader);
    assertEquals(" at path " + expectedPath, locationString);
  }

  private void pushToStack(Object obj) throws Exception {
    var stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(jsonTreeReader);
    stack[0] = obj;
    stackField.set(jsonTreeReader, stack);
  }

  private void setStackSize(int size) throws Exception {
    var stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonTreeReader, size);
  }

  private void setPathIndices(int index, int value) throws Exception {
    var pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(jsonTreeReader);
    pathIndices[index] = value;
    pathIndicesField.set(jsonTreeReader, pathIndices);
  }

  private void setPathNames(int index, String value) throws Exception {
    var pathNamesField = JsonTreeReader.class.getDeclaredField("pathNames");
    pathNamesField.setAccessible(true);
    String[] pathNames = (String[]) pathNamesField.get(jsonTreeReader);
    pathNames[index] = value;
    pathNamesField.set(jsonTreeReader, pathNames);
  }
}