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

class JsonElement_574_3Test {

  // Instead of extending final classes, use anonymous JsonElement instances
  // and override isJsonObject method by proxying the instanceof check.

  static JsonElement createJsonObject() {
    // Use an actual JsonObject instance
    return new JsonObject();
  }

  static JsonElement createJsonArray() {
    return new JsonArray();
  }

  static JsonElement createJsonPrimitive() {
    return new JsonPrimitive("test");
  }

  static JsonElement createJsonNull() {
    return JsonNull.INSTANCE;
  }

  @Test
    @Timeout(8000)
  void testIsJsonObject_whenInstanceIsJsonObject_returnsTrue() {
    JsonElement jsonElement = createJsonObject();
    assertTrue(jsonElement.isJsonObject());
  }

  @Test
    @Timeout(8000)
  void testIsJsonObject_whenInstanceIsJsonArray_returnsFalse() {
    JsonElement jsonElement = createJsonArray();
    assertFalse(jsonElement.isJsonObject());
  }

  @Test
    @Timeout(8000)
  void testIsJsonObject_whenInstanceIsJsonPrimitive_returnsFalse() {
    JsonElement jsonElement = createJsonPrimitive();
    assertFalse(jsonElement.isJsonObject());
  }

  @Test
    @Timeout(8000)
  void testIsJsonObject_whenInstanceIsJsonNull_returnsFalse() {
    JsonElement jsonElement = createJsonNull();
    assertFalse(jsonElement.isJsonObject());
  }
}