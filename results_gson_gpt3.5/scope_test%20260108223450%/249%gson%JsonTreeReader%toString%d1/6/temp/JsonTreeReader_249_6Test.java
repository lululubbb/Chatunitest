package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

class JsonTreeReader_249_6Test {

  private JsonTreeReader jsonTreeReader;

  @BeforeEach
  void setUp() {
    jsonTreeReader = new JsonTreeReader(JsonNull.INSTANCE);
  }

  @Test
    @Timeout(8000)
  void toString_shouldReturnClassNamePlusLocationString() throws Exception {
    // Use reflection to invoke private locationString method
    Method locationStringMethod = JsonTreeReader.class.getDeclaredMethod("locationString");
    locationStringMethod.setAccessible(true);
    String location = (String) locationStringMethod.invoke(jsonTreeReader);

    String expected = JsonTreeReader.class.getSimpleName() + location;
    String actual = jsonTreeReader.toString();

    assertEquals(expected, actual);
  }

  @Test
    @Timeout(8000)
  void locationString_shouldReturnExpectedFormatForEmptyStack() throws Exception {
    // locationString is private, invoke with reflection
    Method locationStringMethod = JsonTreeReader.class.getDeclaredMethod("locationString");
    locationStringMethod.setAccessible(true);

    // Initially stackSize is 0, so locationString should reflect that
    String location = (String) locationStringMethod.invoke(jsonTreeReader);
    assertNotNull(location);
    // Should start with " at path " and then the current path string (empty or "$")
    assertTrue(location.startsWith(" at path "));
  }

  @Test
    @Timeout(8000)
  void locationString_shouldReturnExpectedFormatAfterPush() throws Exception {
    // Push an element and check locationString reflects updated path
    // Use reflection to invoke private push method
    Method pushMethod = JsonTreeReader.class.getDeclaredMethod("push", Object.class);
    pushMethod.setAccessible(true);

    pushMethod.invoke(jsonTreeReader, JsonNull.INSTANCE);

    Method locationStringMethod = JsonTreeReader.class.getDeclaredMethod("locationString");
    locationStringMethod.setAccessible(true);
    String location = (String) locationStringMethod.invoke(jsonTreeReader);

    assertNotNull(location);
    assertTrue(location.startsWith(" at path "));
  }

  @Test
    @Timeout(8000)
  void toString_afterPush_shouldReturnClassNamePlusUpdatedLocationString() throws Exception {
    Method pushMethod = JsonTreeReader.class.getDeclaredMethod("push", Object.class);
    pushMethod.setAccessible(true);
    pushMethod.invoke(jsonTreeReader, JsonNull.INSTANCE);

    Method locationStringMethod = JsonTreeReader.class.getDeclaredMethod("locationString");
    locationStringMethod.setAccessible(true);
    String location = (String) locationStringMethod.invoke(jsonTreeReader);

    String expected = JsonTreeReader.class.getSimpleName() + location;
    assertEquals(expected, jsonTreeReader.toString());
  }
}