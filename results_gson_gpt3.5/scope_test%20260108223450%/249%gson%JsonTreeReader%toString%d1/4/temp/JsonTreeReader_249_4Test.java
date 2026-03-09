package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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

import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.JsonObject;
import com.google.gson.JsonNull;

public class JsonTreeReader_249_4Test {

  private JsonTreeReader jsonTreeReader;

  @BeforeEach
  public void setUp() {
    // Initialize with JsonNull for simplicity
    jsonTreeReader = new JsonTreeReader(JsonNull.INSTANCE);
  }

  @Test
    @Timeout(8000)
  public void testToString_simple() {
    String expectedPrefix = JsonTreeReader.class.getSimpleName();
    String actual = jsonTreeReader.toString();
    // toString returns class simple name + locationString()
    // locationString() returns a string starting with " at path " or empty string
    // So actual should start with expectedPrefix
    assertEquals(true, actual.startsWith(expectedPrefix));
  }

  @Test
    @Timeout(8000)
  public void testToString_withPath() throws Exception {
    // Use reflection to invoke private method push to add elements to stack and path
    Method pushMethod = JsonTreeReader.class.getDeclaredMethod("push", Object.class);
    pushMethod.setAccessible(true);

    // Push a JsonObject to stack
    pushMethod.invoke(jsonTreeReader, new JsonObject());

    // Use reflection to set pathNames and pathIndices to simulate a path
    var pathNamesField = JsonTreeReader.class.getDeclaredField("pathNames");
    pathNamesField.setAccessible(true);
    String[] pathNames = (String[]) pathNamesField.get(jsonTreeReader);
    pathNames[0] = "field"; // fix: set to non-null to have name used in path string

    var pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(jsonTreeReader);
    pathIndices[0] = 0; // fix: set to 0 to match behavior for named fields

    var stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonTreeReader, 1);

    String expectedPrefix = JsonTreeReader.class.getSimpleName();

    String actual = jsonTreeReader.toString();

    // It should start with class simple name
    assertEquals(true, actual.startsWith(expectedPrefix));

    // It should contain " at path " and the path string
    assertEquals(true, actual.contains(" at path "));

    // It should contain the name ".field"
    assertEquals(true, actual.contains(".field"));
  }

  @Test
    @Timeout(8000)
  public void testLocationString_emptyPath() throws Exception {
    Method locationStringMethod = JsonTreeReader.class.getDeclaredMethod("locationString");
    locationStringMethod.setAccessible(true);
    String location = (String) locationStringMethod.invoke(jsonTreeReader);
    // Without stack elements, locationString should be " at path $"
    assertEquals(" at path $", location);
  }

  @Test
    @Timeout(8000)
  public void testLocationString_nonEmptyPath() throws Exception {
    Method locationStringMethod = JsonTreeReader.class.getDeclaredMethod("locationString");
    locationStringMethod.setAccessible(true);
    Method pushMethod = JsonTreeReader.class.getDeclaredMethod("push", Object.class);
    pushMethod.setAccessible(true);

    // Push a JsonObject to stack
    pushMethod.invoke(jsonTreeReader, new JsonObject());

    // Set pathNames and pathIndices
    var pathNamesField = JsonTreeReader.class.getDeclaredField("pathNames");
    pathNamesField.setAccessible(true);
    String[] pathNames = (String[]) pathNamesField.get(jsonTreeReader);
    pathNames[0] = "field"; // fix: set non-null here to force path string to use name

    var pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(jsonTreeReader);
    pathIndices[0] = 0; // fix: set to 0 to match named field usage

    var stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonTreeReader, 1);

    String location = (String) locationStringMethod.invoke(jsonTreeReader);

    // locationString should start with " at path "
    assertEquals(true, location.startsWith(" at path "));

    // It should contain the name ".field"
    assertEquals(true, location.contains(".field"));
  }
}