package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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
import static org.mockito.Mockito.spy;

import com.google.gson.JsonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class JsonTreeReader_249_2Test {

  private JsonTreeReader jsonTreeReader;

  @BeforeEach
  public void setUp() {
    // Create a JsonElement to pass to constructor; simplest is JsonNull.INSTANCE
    jsonTreeReader = new JsonTreeReader(JsonNull.INSTANCE);
  }

  @Test
    @Timeout(8000)
  public void testToString_includesClassNameAndLocationString() throws Exception {
    // Spy on the instance without mocking private method (Mockito cannot mock private methods)
    JsonTreeReader spyReader = spy(jsonTreeReader);

    // Use reflection to forcibly set the locationString method result by temporarily replacing it
    // Since locationString is private and final, we cannot mock it directly with Mockito.
    // Instead, we invoke toString and check it contains the class name and the locationString output.

    // Use reflection to invoke locationString()
    Method locationStringMethod = JsonTreeReader.class.getDeclaredMethod("locationString");
    locationStringMethod.setAccessible(true);
    String locationString = (String) locationStringMethod.invoke(spyReader);

    // Call toString and verify output
    String toStringResult = spyReader.toString();
    assertEquals(spyReader.getClass().getSimpleName() + locationString, toStringResult);
  }

  @Test
    @Timeout(8000)
  public void testLocationString_reflection_call() throws Exception {
    // Use reflection to invoke private locationString method
    Method locationStringMethod = JsonTreeReader.class.getDeclaredMethod("locationString");
    locationStringMethod.setAccessible(true);

    // Call locationString on the instance
    String location = (String) locationStringMethod.invoke(jsonTreeReader);

    // locationString returns a string starting with " at path " or empty string
    // We just check it is non-null
    assertEquals(true, location != null);
  }
}