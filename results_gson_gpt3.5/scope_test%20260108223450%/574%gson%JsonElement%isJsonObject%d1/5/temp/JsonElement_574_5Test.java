package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

class JsonElement_574_5Test {

  // A minimal concrete subclass of JsonElement for testing
  static class TestJsonElement extends JsonElement {
    @Override
    public JsonElement deepCopy() {
      return this;
    }
  }

  // Instead of extending final JsonObject, create a JsonObject instance via reflection for testing
  private JsonElement createJsonObjectInstance() {
    try {
      Constructor<JsonObject> constructor = JsonObject.class.getDeclaredConstructor();
      constructor.setAccessible(true);
      return constructor.newInstance();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Test
    @Timeout(8000)
  void testIsJsonObject_whenInstanceIsJsonObject_returnsTrue() {
    JsonElement jsonObject = createJsonObjectInstance();
    assertTrue(jsonObject.isJsonObject());
  }

  @Test
    @Timeout(8000)
  void testIsJsonObject_whenInstanceIsNotJsonObject_returnsFalse() {
    JsonElement jsonElement = new TestJsonElement();
    assertFalse(jsonElement.isJsonObject());
  }
}