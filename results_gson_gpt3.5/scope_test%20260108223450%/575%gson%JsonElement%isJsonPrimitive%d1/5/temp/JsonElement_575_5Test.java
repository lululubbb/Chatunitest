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
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

class JsonElement_575_5Test {

  @Test
    @Timeout(8000)
  void isJsonPrimitive_withJsonPrimitiveInstance_returnsTrue() {
    JsonElement jsonPrimitive = new JsonPrimitive("test");
    assertTrue(jsonPrimitive.isJsonPrimitive());
  }

  @Test
    @Timeout(8000)
  void isJsonPrimitive_withAnonymousJsonElementSubclass_returnsFalse() {
    JsonElement jsonElement = new JsonElement() {
      @Override
      public JsonElement deepCopy() {
        return this;
      }
    };
    assertFalse(jsonElement.isJsonPrimitive());
  }
}