package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class JsonElement_582_4Test {

  @Test
    @Timeout(8000)
  void getAsNumber_throwsUnsupportedOperationException() {
    JsonElement element = new JsonElement() {
      @Override
      public JsonElement deepCopy() {
        return this;
      }
    };
    UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, element::getAsNumber);
    assertEquals("JsonElement", exception.getMessage());
  }
}