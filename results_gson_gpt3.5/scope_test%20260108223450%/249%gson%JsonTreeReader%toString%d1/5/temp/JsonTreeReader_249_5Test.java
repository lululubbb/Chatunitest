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

public class JsonTreeReader_249_5Test {

  private JsonTreeReader jsonTreeReader;

  @BeforeEach
  public void setUp() {
    JsonElement element = new JsonPrimitive("test");
    jsonTreeReader = new JsonTreeReader(element);
  }

  @Test
    @Timeout(8000)
  public void testToString() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    // Using reflection to invoke private method locationString()
    Method locationStringMethod = JsonTreeReader.class.getDeclaredMethod("locationString");
    locationStringMethod.setAccessible(true);
    String locationString = (String) locationStringMethod.invoke(jsonTreeReader);

    String expected = JsonTreeReader.class.getSimpleName() + locationString;
    String actual = jsonTreeReader.toString();

    assertEquals(expected, actual);
  }
}