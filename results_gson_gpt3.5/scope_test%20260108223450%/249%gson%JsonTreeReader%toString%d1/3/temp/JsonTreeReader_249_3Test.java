package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class JsonTreeReader_249_3Test {

  private JsonTreeReader jsonTreeReader;

  @BeforeEach
  public void setUp() {
    // Create a minimal JsonElement to pass to constructor
    jsonTreeReader = new JsonTreeReader(null);
  }

  @Test
    @Timeout(8000)
  public void testToString_returnsClassNamePlusLocationString() throws Exception {
    // Use reflection to invoke the private locationString() method
    Method locationStringMethod = JsonTreeReader.class.getDeclaredMethod("locationString");
    locationStringMethod.setAccessible(true);

    // Invoke locationString() on the instance
    String locationString = (String) locationStringMethod.invoke(jsonTreeReader);

    String expected = "JsonTreeReader" + locationString;
    String actual = jsonTreeReader.toString();

    assertEquals(expected, actual);
  }
}