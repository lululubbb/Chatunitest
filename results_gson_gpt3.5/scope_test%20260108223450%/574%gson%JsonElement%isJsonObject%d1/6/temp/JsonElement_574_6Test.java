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
import com.google.gson.JsonObject;

class JsonElement_574_6Test {

  // A simple concrete subclass of JsonElement for testing non-JsonObject instances
  private static class JsonElementStub extends JsonElement {
    @Override
    public JsonElement deepCopy() {
      return this;
    }
  }

  @Test
    @Timeout(8000)
  void testIsJsonObject_whenInstanceIsJsonObject() {
    JsonElement element = new JsonObject();
    assertTrue(element.isJsonObject());
  }

  @Test
    @Timeout(8000)
  void testIsJsonObject_whenInstanceIsNotJsonObject() {
    JsonElement element = new JsonElementStub();
    assertFalse(element.isJsonObject());
  }
}