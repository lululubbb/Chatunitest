package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

class JsonElement_579_4Test {

  @Test
    @Timeout(8000)
  void getAsJsonPrimitive_whenIsJsonPrimitiveTrue_returnsThisAsJsonPrimitive() {
    // Create a real JsonPrimitive instance with some value
    JsonPrimitive jsonPrimitive = new JsonPrimitive("test");

    // Spy on it to override isJsonPrimitive() to return true
    JsonPrimitive spyPrimitive = spy(jsonPrimitive);
    doReturn(true).when(spyPrimitive).isJsonPrimitive();

    // Call getAsJsonPrimitive(), which should return this instance casted
    JsonPrimitive result = spyPrimitive.getAsJsonPrimitive();

    assertSame(spyPrimitive, result);
  }

  @Test
    @Timeout(8000)
  void getAsJsonPrimitive_whenIsJsonPrimitiveFalse_throwsIllegalStateException() {
    // Use a real JsonElement subclass to avoid serialization issues in toString()
    JsonElement jsonElement = new JsonElement() {
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
        return "JsonElementToString";
      }
    };

    IllegalStateException exception = assertThrows(IllegalStateException.class, jsonElement::getAsJsonPrimitive);
    assertEquals("Not a JSON Primitive: JsonElementToString", exception.getMessage());
  }
}