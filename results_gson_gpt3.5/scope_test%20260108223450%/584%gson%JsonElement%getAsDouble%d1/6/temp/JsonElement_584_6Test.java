package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class JsonElement_584_6Test {

  @Test
    @Timeout(8000)
  void testGetAsDouble_throwsUnsupportedOperationException() {
    JsonElement element = new JsonElement() {
      @Override
      public JsonElement deepCopy() {
        return null;
      }
    };
    UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, element::getAsDouble);
    // The anonymous class simple name is empty string
    assertEquals("", exception.getMessage());
  }
}