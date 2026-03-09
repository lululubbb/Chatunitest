package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;

class JsonElement_591_6Test {

  static class JsonElementImpl extends JsonElement {
    @Override
    public JsonElement deepCopy() {
      return null;
    }
  }

  @Test
    @Timeout(8000)
  void testGetAsBigInteger_UnsupportedOperationException() {
    JsonElement element = new JsonElementImpl();
    UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, element::getAsBigInteger);
    assertEquals("JsonElementImpl", exception.getMessage());
  }
}