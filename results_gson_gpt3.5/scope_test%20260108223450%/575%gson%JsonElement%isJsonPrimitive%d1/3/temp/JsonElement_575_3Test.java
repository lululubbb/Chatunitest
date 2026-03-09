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

class JsonElement_575_3Test {

  // A minimal concrete subclass of JsonElement to test isJsonPrimitive for non-JsonPrimitive instances.
  private static class JsonElementSubclass extends JsonElement {
    @Override
    public JsonElement deepCopy() {
      return this;
    }
  }

  @Test
    @Timeout(8000)
  void testIsJsonPrimitive_withJsonPrimitiveInstance() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive("test");
    assertTrue(jsonPrimitive.isJsonPrimitive());
  }

  @Test
    @Timeout(8000)
  void testIsJsonPrimitive_withNonJsonPrimitiveInstance() {
    JsonElement jsonElement = new JsonElementSubclass();
    assertFalse(jsonElement.isJsonPrimitive());
  }
}