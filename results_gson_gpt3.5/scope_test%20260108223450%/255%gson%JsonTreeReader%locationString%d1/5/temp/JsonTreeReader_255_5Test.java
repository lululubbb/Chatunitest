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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class JsonTreeReader_255_5Test {

  private JsonTreeReader jsonTreeReader;

  @BeforeEach
  public void setUp() {
    // Initialize with a simple JsonElement (empty JsonObject)
    jsonTreeReader = new JsonTreeReader(new JsonObject());
  }

  @Test
    @Timeout(8000)
  public void testLocationString_initialState() throws Exception {
    Method locationStringMethod = JsonTreeReader.class.getDeclaredMethod("locationString");
    locationStringMethod.setAccessible(true);

    String location = (String) locationStringMethod.invoke(jsonTreeReader);

    // The path for a newly created JsonTreeReader with empty JsonObject is "$"
    // So locationString returns " at path $"
    assertEquals(" at path $", location);
  }

  @Test
    @Timeout(8000)
  public void testLocationString_afterPushes() throws Exception {
    Method locationStringMethod = JsonTreeReader.class.getDeclaredMethod("locationString");
    locationStringMethod.setAccessible(true);

    // Using reflection to call private push(Object newTop) method to simulate stack changes
    Method pushMethod = JsonTreeReader.class.getDeclaredMethod("push", Object.class);
    pushMethod.setAccessible(true);

    // Push JsonArray
    pushMethod.invoke(jsonTreeReader, new JsonArray());
    // Push JsonObject
    pushMethod.invoke(jsonTreeReader, new JsonObject());
    // Push JsonPrimitive
    pushMethod.invoke(jsonTreeReader, new JsonPrimitive("test"));

    // Also set pathNames and pathIndices via reflection to simulate a real path
    var pathNamesField = JsonTreeReader.class.getDeclaredField("pathNames");
    pathNamesField.setAccessible(true);
    String[] pathNames = (String[]) pathNamesField.get(jsonTreeReader);
    pathNames[0] = "first";
    pathNames[1] = "second";
    pathNames[2] = "third";

    var pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(jsonTreeReader);
    pathIndices[0] = 0;
    pathIndices[1] = 1;
    pathIndices[2] = 2;

    var stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonTreeReader, 3);

    String location = (String) locationStringMethod.invoke(jsonTreeReader);

    // The path should reflect the pushed stack and pathNames/Indices
    // The getPath() method returns a path string starting with '$' and then path elements
    // So locationString returns " at path " + getPath()
    // Expected path: "$.first[0].second[1].third[2]" or similar depending on getPath() implementation
    // Since getPath() is public, call it directly to get expected path
    String expectedPath = jsonTreeReader.getPath();
    assertEquals(" at path " + expectedPath, location);
  }

  @Test
    @Timeout(8000)
  public void testLocationString_afterPopStack() throws Exception {
    Method locationStringMethod = JsonTreeReader.class.getDeclaredMethod("locationString");
    locationStringMethod.setAccessible(true);

    Method pushMethod = JsonTreeReader.class.getDeclaredMethod("push", Object.class);
    pushMethod.setAccessible(true);

    Method popStackMethod = JsonTreeReader.class.getDeclaredMethod("popStack");
    popStackMethod.setAccessible(true);

    // Push some elements
    pushMethod.invoke(jsonTreeReader, new JsonObject());
    pushMethod.invoke(jsonTreeReader, new JsonArray());

    var stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonTreeReader, 2);

    // Set pathNames and pathIndices accordingly
    var pathNamesField = JsonTreeReader.class.getDeclaredField("pathNames");
    pathNamesField.setAccessible(true);
    String[] pathNames = (String[]) pathNamesField.get(jsonTreeReader);
    pathNames[0] = "obj";
    pathNames[1] = "arr";

    var pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(jsonTreeReader);
    pathIndices[0] = 5;
    pathIndices[1] = 10;

    // Pop one element
    popStackMethod.invoke(jsonTreeReader);
    stackSizeField.setInt(jsonTreeReader, 1);

    String location = (String) locationStringMethod.invoke(jsonTreeReader);

    String expectedPath = jsonTreeReader.getPath();
    assertEquals(" at path " + expectedPath, location);
  }
}