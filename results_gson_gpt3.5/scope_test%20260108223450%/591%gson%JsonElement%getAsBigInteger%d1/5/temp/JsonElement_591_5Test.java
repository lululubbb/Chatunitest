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

class JsonElement_591_5Test {

  private static class JsonElementImpl extends JsonElement {
    @Override
    public JsonElement deepCopy() {
      return this;
    }
  }

  @Test
    @Timeout(8000)
  void getAsBigInteger_unsupportedOperationExceptionThrown() {
    JsonElement element = new JsonElementImpl();
    UnsupportedOperationException thrown = assertThrows(UnsupportedOperationException.class, element::getAsBigInteger);
    assertEquals("JsonElementImpl", thrown.getMessage());
  }
}