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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class JsonTreeReader_255_1Test {

  private JsonTreeReader jsonTreeReader;
  private Method locationStringMethod;

  @BeforeEach
  public void setUp() throws Exception {
    // Initialize JsonTreeReader with a JsonNull element (empty tree)
    jsonTreeReader = new JsonTreeReader(JsonNull.INSTANCE);

    // Access private locationString method via reflection
    locationStringMethod = JsonTreeReader.class.getDeclaredMethod("locationString");
    locationStringMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  public void testLocationString_rootPath() throws Exception {
    // When no navigation, path should be "$"
    String result = (String) locationStringMethod.invoke(jsonTreeReader);
    assertEquals(" at path $", result);
  }

  @Test
    @Timeout(8000)
  public void testLocationString_afterBeginObject() throws Exception {
    // Create JsonObject with no properties to avoid IllegalStateException
    JsonObject obj = new JsonObject();
    jsonTreeReader = new JsonTreeReader(obj);

    // Begin object, path should be "$" (without trailing dot)
    jsonTreeReader.beginObject();
    String result = (String) locationStringMethod.invoke(jsonTreeReader);
    assertEquals(" at path $", result);
  }

  @Test
    @Timeout(8000)
  public void testLocationString_afterNextName() throws Exception {
    // Create JsonObject with one property
    JsonObject obj = new JsonObject();
    obj.add("key", new JsonPrimitive("value"));
    jsonTreeReader = new JsonTreeReader(obj);

    // beginObject to move into object
    jsonTreeReader.beginObject();
    // nextName to move to property name
    String name = jsonTreeReader.nextName();
    assertEquals("key", name);

    // locationString should reflect path with property name
    String result = (String) locationStringMethod.invoke(jsonTreeReader);
    assertEquals(" at path $.key", result);
  }

  @Test
    @Timeout(8000)
  public void testLocationString_inArray() throws Exception {
    // Create JsonArray with 3 elements
    JsonArray array = new JsonArray();
    array.add(new JsonPrimitive(1));
    array.add(new JsonPrimitive(2));
    array.add(new JsonPrimitive(3));
    jsonTreeReader = new JsonTreeReader(array);

    // beginArray to enter array
    jsonTreeReader.beginArray();

    // Initially at index 0
    String result0 = (String) locationStringMethod.invoke(jsonTreeReader);
    assertEquals(" at path $[0]", result0);

    // Consume first element
    jsonTreeReader.skipValue();

    // Now at index 1
    String result1 = (String) locationStringMethod.invoke(jsonTreeReader);
    assertEquals(" at path $[1]", result1);

    // Consume second element
    jsonTreeReader.skipValue();

    // Now at index 2
    String result2 = (String) locationStringMethod.invoke(jsonTreeReader);
    assertEquals(" at path $[2]", result2);
  }

  @Test
    @Timeout(8000)
  public void testLocationString_nestedObjectsAndArrays() throws Exception {
    // Build nested structure: { "obj": [ { "a": 1 }, { "b": 2 } ] }
    JsonObject innerObj1 = new JsonObject();
    innerObj1.add("a", new JsonPrimitive(1));
    JsonObject innerObj2 = new JsonObject();
    innerObj2.add("b", new JsonPrimitive(2));
    JsonArray array = new JsonArray();
    array.add(innerObj1);
    array.add(innerObj2);
    JsonObject root = new JsonObject();
    root.add("obj", array);

    jsonTreeReader = new JsonTreeReader(root);

    // begin root object
    jsonTreeReader.beginObject();
    // nextName "obj"
    String name = jsonTreeReader.nextName();
    assertEquals("obj", name);
    // begin array
    jsonTreeReader.beginArray();

    // At array index 0, path should be $.obj[0]
    String path0 = (String) locationStringMethod.invoke(jsonTreeReader);
    assertEquals(" at path $.obj[0]", path0);

    // begin object at index 0
    jsonTreeReader.beginObject();
    // nextName "a"
    String nameA = jsonTreeReader.nextName();
    assertEquals("a", nameA);
    // locationString should be $.obj[0].a
    String pathA = (String) locationStringMethod.invoke(jsonTreeReader);
    assertEquals(" at path $.obj[0].a", pathA);

    // nextInt consumes the 1
    jsonTreeReader.nextInt();
    // end object
    jsonTreeReader.endObject();

    // end object at index 0, now advance array index by skipping value
    jsonTreeReader.skipValue();

    // Now at index 1 in array
    String path1 = (String) locationStringMethod.invoke(jsonTreeReader);
    assertEquals(" at path $.obj[1]", path1);

    // begin object at index 1
    jsonTreeReader.beginObject();

    // nextName "b"
    String nameB = jsonTreeReader.nextName();
    assertEquals("b", nameB);
    // locationString should be $.obj[1].b
    String pathB = (String) locationStringMethod.invoke(jsonTreeReader);
    assertEquals(" at path $.obj[1].b", pathB);

    // nextInt consumes the 2
    jsonTreeReader.nextInt();
    // end object
    jsonTreeReader.endObject();

    // end array
    jsonTreeReader.endArray();
    // end root object
    jsonTreeReader.endObject();
  }

  @Test
    @Timeout(8000)
  public void testLocationString_afterEndObject() throws Exception {
    JsonObject obj = new JsonObject();
    obj.add("key", new JsonPrimitive("value"));
    jsonTreeReader = new JsonTreeReader(obj);

    jsonTreeReader.beginObject();
    jsonTreeReader.nextName();
    jsonTreeReader.nextString();
    jsonTreeReader.endObject();

    // After endObject, path should be root "$"
    String result = (String) locationStringMethod.invoke(jsonTreeReader);
    assertEquals(" at path $", result);
  }

  @Test
    @Timeout(8000)
  public void testLocationString_afterEndArray() throws Exception {
    JsonArray array = new JsonArray();
    array.add(new JsonPrimitive(1));
    jsonTreeReader = new JsonTreeReader(array);

    jsonTreeReader.beginArray();
    jsonTreeReader.nextInt();
    jsonTreeReader.endArray();

    // After endArray, path should be root "$"
    String result = (String) locationStringMethod.invoke(jsonTreeReader);
    assertEquals(" at path $", result);
  }
}