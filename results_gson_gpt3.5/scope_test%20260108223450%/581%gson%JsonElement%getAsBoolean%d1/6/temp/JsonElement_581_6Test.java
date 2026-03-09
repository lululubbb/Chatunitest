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

class JsonElement_581_6Test {

  static class JsonElementImpl extends JsonElement {
    @Override
    public JsonElement deepCopy() {
      return this;
    }
  }

  @Test
    @Timeout(8000)
  void getAsBoolean_throwsUnsupportedOperationException() {
    JsonElement element = new JsonElementImpl();
    UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, element::getAsBoolean);
    assertEquals("JsonElementImpl", exception.getMessage());
  }
}