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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonTreeReader_249_1Test {

  private JsonTreeReader jsonTreeReader;

  @BeforeEach
  public void setUp() {
    // Initialize with a simple JsonElement (JsonObject) for testing
    JsonObject jsonObject = new JsonObject();
    jsonObject.add("key", new JsonPrimitive("value"));
    jsonTreeReader = new JsonTreeReader(jsonObject);
  }

  @Test
    @Timeout(8000)
  public void testToString_shouldReturnClassNamePlusLocationString() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    // Use reflection to invoke private locationString method
    Method locationStringMethod = JsonTreeReader.class.getDeclaredMethod("locationString");
    locationStringMethod.setAccessible(true);
    String locationString = (String) locationStringMethod.invoke(jsonTreeReader);

    String expected = JsonTreeReader.class.getSimpleName() + locationString;
    String actual = jsonTreeReader.toString();

    assertEquals(expected, actual);
  }
}