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

class JsonElement_576_5Test {

  @Test
    @Timeout(8000)
  void testIsJsonNull_whenInstanceIsJsonNull() throws Exception {
    // JsonNull is final, so instantiate via its public INSTANCE field
    JsonNull jsonNull = JsonNull.INSTANCE;
    assertTrue(jsonNull.isJsonNull());
  }

  static class JsonElementMock extends JsonElement {
    @Override
    public JsonElement deepCopy() {
      return this;
    }
  }

  @Test
    @Timeout(8000)
  void testIsJsonNull_whenInstanceIsNotJsonNull() {
    JsonElement element = new JsonElementMock();
    assertFalse(element.isJsonNull());
  }
}