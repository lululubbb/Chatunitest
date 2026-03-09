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

class JsonElement_574_4Test {

  private static class JsonElementSubclass extends JsonElement {
    @Override
    public JsonElement deepCopy() {
      return this;
    }
  }

  @Test
    @Timeout(8000)
  void isJsonObject_shouldReturnTrue_whenInstanceIsJsonObject() throws Exception {
    // Use reflection to create an instance of final JsonObject
    JsonElement element = JsonObject.class.getDeclaredConstructor().newInstance();
    assertTrue(element.isJsonObject());
  }

  @Test
    @Timeout(8000)
  void isJsonObject_shouldReturnFalse_whenInstanceIsNotJsonObject() {
    JsonElement element = new JsonElementSubclass();
    assertFalse(element.isJsonObject());
  }
}