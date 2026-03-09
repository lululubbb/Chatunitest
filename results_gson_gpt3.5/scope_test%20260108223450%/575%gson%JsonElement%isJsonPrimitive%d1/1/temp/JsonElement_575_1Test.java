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
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;

class JsonElement_575_1Test {

  @Test
    @Timeout(8000)
  void testIsJsonPrimitive_withJsonPrimitiveInstance_returnsTrue() {
    JsonElement element = new JsonPrimitive("test");
    assertTrue(element.isJsonPrimitive());
  }

  @Test
    @Timeout(8000)
  void testIsJsonPrimitive_withJsonNullInstance_returnsFalse() {
    JsonElement element = JsonNull.INSTANCE;
    assertFalse(element.isJsonPrimitive());
  }

  @Test
    @Timeout(8000)
  void testIsJsonPrimitive_withJsonObjectInstance_returnsFalse() {
    JsonElement element = new JsonObject();
    assertFalse(element.isJsonPrimitive());
  }

  @Test
    @Timeout(8000)
  void testIsJsonPrimitive_withJsonArrayInstance_returnsFalse() {
    JsonElement element = new JsonArray();
    assertFalse(element.isJsonPrimitive());
  }

  @Test
    @Timeout(8000)
  void testIsJsonPrimitive_withAnonymousJsonElementSubclass_returnsFalse() {
    JsonElement element = new JsonElement() {
      @Override
      public JsonElement deepCopy() {
        return this;
      }
    };
    assertFalse(element.isJsonPrimitive());
  }
}