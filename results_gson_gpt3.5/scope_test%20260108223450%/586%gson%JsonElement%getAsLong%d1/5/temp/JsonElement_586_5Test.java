package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class JsonElement_586_5Test {

  @Test
    @Timeout(8000)
  void getAsLong_unsupportedOperationException() throws Exception {
    JsonElement element = new JsonElement() {
      @Override
      public JsonElement deepCopy() {
        return this;
      }
    };

    UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, element::getAsLong);
    // exception message should be the simple name of the anonymous subclass, which is empty string or something similar
  }
}