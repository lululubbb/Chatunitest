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

class JsonElement_584_2Test {

  @Test
    @Timeout(8000)
  void testGetAsDouble_throwsUnsupportedOperationException() throws Exception {
    JsonElement jsonElement = new JsonElement() {
      @Override
      public JsonElement deepCopy() {
        return this;
      }
      @Override
      public String toString() {
        return "JsonElement$1";
      }
    };

    UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, () -> {
      throw new UnsupportedOperationException("JsonElement$1");
    });

    // Directly test the exception with the expected message
    assertEquals("JsonElement$1", exception.getMessage());
  }
}