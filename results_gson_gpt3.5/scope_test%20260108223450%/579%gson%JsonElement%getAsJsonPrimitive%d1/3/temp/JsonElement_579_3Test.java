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

class JsonElement_579_3Test {

  // Concrete subclass of JsonElement for testing that behaves as a primitive
  static class TestJsonPrimitive extends JsonElement {
    @Override
    public boolean isJsonPrimitive() {
      return true;
    }

    @Override
    public JsonPrimitive getAsJsonPrimitive() {
      return new JsonPrimitive("dummy") {
        @Override
        public String toString() {
          return "TestJsonPrimitive";
        }
      };
    }

    @Override
    public JsonElement deepCopy() {
      return this;
    }

    @Override
    public boolean isJsonArray() { return false; }

    @Override
    public boolean isJsonObject() { return false; }

    @Override
    public boolean isJsonNull() { return false; }

    @Override
    public String toString() {
      return "TestJsonPrimitive";
    }
  }

  static class TestJsonNonPrimitive extends JsonElement {
    @Override
    public JsonPrimitive getAsJsonPrimitive() {
      return super.getAsJsonPrimitive();
    }
    @Override
    public JsonElement deepCopy() { return this; }
    @Override
    public boolean isJsonPrimitive() { return false; }
    @Override
    public boolean isJsonArray() { return false; }
    @Override
    public boolean isJsonObject() { return false; }
    @Override
    public boolean isJsonNull() { return false; }
    @Override
    public String toString() { return "TestJsonNonPrimitive"; }
  }

  @Test
    @Timeout(8000)
  void testGetAsJsonPrimitive_whenIsJsonPrimitiveTrue_returnsSelf() {
    TestJsonPrimitive primitive = new TestJsonPrimitive();
    JsonPrimitive result = primitive.getAsJsonPrimitive();
    assertEquals("TestJsonPrimitive", result.toString());
  }

  @Test
    @Timeout(8000)
  void testGetAsJsonPrimitive_whenIsJsonPrimitiveFalse_throwsIllegalStateException() {
    TestJsonNonPrimitive nonPrimitive = new TestJsonNonPrimitive();
    IllegalStateException thrown = assertThrows(IllegalStateException.class,
        nonPrimitive::getAsJsonPrimitive);
    assertTrue(thrown.getMessage().contains("Not a JSON Primitive"));
    assertTrue(thrown.getMessage().contains("TestJsonNonPrimitive"));
  }
}