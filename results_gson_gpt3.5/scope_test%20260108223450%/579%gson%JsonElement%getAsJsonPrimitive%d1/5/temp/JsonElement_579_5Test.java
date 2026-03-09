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

import java.lang.reflect.Method;

class JsonElement_579_5Test {

  static class JsonPrimitiveStub extends JsonPrimitive {
    JsonPrimitiveStub() {
      super("stub"); // call a JsonPrimitive constructor with a dummy value
    }
    @Override
    public boolean isJsonPrimitive() {
      return true;
    }
    @Override
    public JsonPrimitive deepCopy() {
      return this;
    }
    @Override
    public String toString() {
      return "JsonPrimitiveStub";
    }
  }

  static class JsonNonPrimitiveStub extends JsonElement {
    @Override
    public boolean isJsonPrimitive() {
      return false;
    }
    @Override
    public JsonElement deepCopy() {
      return this;
    }
    @Override
    public String toString() {
      return "JsonNonPrimitiveStub";
    }
  }

  @Test
    @Timeout(8000)
  void testGetAsJsonPrimitive_whenIsPrimitive_returnsSelfAsJsonPrimitive() throws Exception {
    JsonPrimitiveStub primitive = new JsonPrimitiveStub();
    // getAsJsonPrimitive is declared in JsonElement, returns JsonPrimitive
    JsonPrimitive result = (JsonPrimitive) JsonElement.class.getMethod("getAsJsonPrimitive").invoke(primitive);
    assertSame(primitive, result);
  }

  @Test
    @Timeout(8000)
  void testGetAsJsonPrimitive_whenNotPrimitive_throwsIllegalStateException() {
    JsonNonPrimitiveStub nonPrimitive = new JsonNonPrimitiveStub();
    IllegalStateException thrown = assertThrows(IllegalStateException.class,
        () -> nonPrimitive.getAsJsonPrimitive());
    assertTrue(thrown.getMessage().contains("Not a JSON Primitive"));
    assertTrue(thrown.getMessage().contains("JsonNonPrimitiveStub"));
  }
}