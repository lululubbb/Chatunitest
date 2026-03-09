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

class JsonElement_573_4Test {

  private static class JsonElementSubclass extends JsonElement {
    @Override
    public JsonElement deepCopy() {
      return null;
    }
  }

  @Test
    @Timeout(8000)
  void testIsJsonArray_whenInstanceIsJsonArray_returnsTrue() throws Exception {
    // JsonArray is final, so create instance via reflection
    Constructor<JsonArray> constructor = JsonArray.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    JsonElement element = constructor.newInstance();
    assertTrue(element.isJsonArray());
  }

  @Test
    @Timeout(8000)
  void testIsJsonArray_whenInstanceIsNotJsonArray_returnsFalse() {
    JsonElement element = new JsonElementSubclass();
    assertFalse(element.isJsonArray());
  }
}