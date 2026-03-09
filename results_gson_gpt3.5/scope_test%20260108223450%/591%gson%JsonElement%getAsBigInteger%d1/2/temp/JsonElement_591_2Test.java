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

class JsonElement_591_2Test {

  @Test
    @Timeout(8000)
  void getAsBigInteger_throwsUnsupportedOperationException() {
    JsonElement element = new JsonElement() {
      @Override
      public JsonElement deepCopy() {
        return this;
      }

      @Override
      public BigInteger getAsBigInteger() {
        throw new UnsupportedOperationException("JsonElement");
      }
    };

    UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, element::getAsBigInteger);
    assertEquals("JsonElement", exception.getMessage());
  }
}