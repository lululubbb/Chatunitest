package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JsonElement_576_4Test {

  // Concrete subclass of JsonElement for testing non-JsonNull instances
  static class JsonElementImpl extends JsonElement {
    @Override
    public JsonElement deepCopy() {
      return this;
    }
  }

  @Test
    @Timeout(8000)
  void testIsJsonNull_whenInstanceIsJsonNull() {
    JsonElement jsonNull = JsonNull.INSTANCE;
    assertTrue(jsonNull.isJsonNull());
  }

  @Test
    @Timeout(8000)
  void testIsJsonNull_whenInstanceIsNotJsonNull() {
    JsonElement jsonElement = new JsonElementImpl();
    assertFalse(jsonElement.isJsonNull());
  }
}