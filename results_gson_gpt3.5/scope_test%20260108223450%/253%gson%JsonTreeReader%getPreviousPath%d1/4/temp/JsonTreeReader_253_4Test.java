package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.stream.JsonReader;
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
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JsonTreeReader_253_4Test {

  private JsonTreeReader jsonTreeReader;

  @BeforeEach
  public void setUp() {
    JsonObject root = new JsonObject();
    JsonArray array = new JsonArray();
    array.add(new JsonPrimitive("value1"));
    array.add(new JsonPrimitive("value2"));
    root.add("array", array);
    root.add("nullValue", JsonNull.INSTANCE);
    jsonTreeReader = new JsonTreeReader(root);
  }

  @Test
    @Timeout(8000)
  public void testGetPreviousPath_rootObject() throws Exception {
    // Initially path should be empty or "$"
    String path = jsonTreeReader.getPreviousPath();
    assertEquals("$", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPreviousPath_afterBeginObjectAndNextName() throws Exception {
    jsonTreeReader.beginObject();
    String name = jsonTreeReader.nextName();
    assertEquals("array", name);
    String path = jsonTreeReader.getPreviousPath();
    assertEquals("$.array", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPreviousPath_afterBeginArrayAndNextString() throws Exception {
    jsonTreeReader.beginObject();
    jsonTreeReader.nextName(); // "array"
    jsonTreeReader.beginArray();
    String value1 = jsonTreeReader.nextString();
    assertEquals("value1", value1);
    String path = jsonTreeReader.getPreviousPath();
    assertEquals("$.array[0]", path);

    String value2 = jsonTreeReader.nextString();
    assertEquals("value2", value2);
    path = jsonTreeReader.getPreviousPath();
    assertEquals("$.array[1]", path);

    jsonTreeReader.endArray();
    jsonTreeReader.nextName(); // "nullValue"
    jsonTreeReader.nextNull();
    path = jsonTreeReader.getPreviousPath();
    assertEquals("$.nullValue", path);

    jsonTreeReader.endObject();
    path = jsonTreeReader.getPreviousPath();
    assertEquals("$", path);
  }

  @Test
    @Timeout(8000)
  public void testGetPreviousPath_reflectiveInvocation() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method getPathMethod = JsonTreeReader.class.getDeclaredMethod("getPath", boolean.class);
    getPathMethod.setAccessible(true);

    // Pass true to simulate getPreviousPath call
    String resultTrue = (String) getPathMethod.invoke(jsonTreeReader, true);
    assertEquals("$", resultTrue);

    // Pass false to simulate getPath() call
    String resultFalse = (String) getPathMethod.invoke(jsonTreeReader, false);
    assertEquals("$", resultFalse);
  }
}