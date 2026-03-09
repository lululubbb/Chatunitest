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

public class JsonElement_591_4Test {

  static class JsonElementImpl extends JsonElement {
    @Override
    public JsonElement deepCopy() {
      return this;
    }
  }

  @Test
    @Timeout(8000)
  void getAsBigInteger_throwsUnsupportedOperationException() {
    JsonElement jsonElement = new JsonElementImpl();

    UnsupportedOperationException thrown = assertThrows(UnsupportedOperationException.class, jsonElement::getAsBigInteger);
    assertEquals("JsonElementImpl", thrown.getMessage());
  }
}