package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigInteger;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.gson.JsonElement;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class JsonElement_590_2Test {

  static class JsonElementImpl extends JsonElement {
    @Override
    public JsonElement deepCopy() {
      return null;
    }
  }

  @Test
    @Timeout(8000)
  void getAsBigDecimal_throwsUnsupportedOperationException() {
    JsonElement jsonElement = new JsonElementImpl();
    UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, jsonElement::getAsBigDecimal);
    assertEquals("JsonElementImpl", exception.getMessage());
  }
}