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

class JsonElement_585_4Test {

  // A simple subclass to instantiate the abstract class
  static class JsonElementSubclass extends JsonElement {
    @Override
    public JsonElement deepCopy() {
      return this;
    }
  }

  @Test
    @Timeout(8000)
  void getAsFloat_unsupportedOperationExceptionThrown() {
    JsonElement element = new JsonElementSubclass();
    UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, element::getAsFloat);
    assertEquals("JsonElementSubclass", exception.getMessage());
  }
}