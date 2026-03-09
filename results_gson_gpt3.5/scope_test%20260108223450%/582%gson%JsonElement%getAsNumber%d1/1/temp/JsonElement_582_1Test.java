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

public class JsonElement_582_1Test {

  private static class JsonElementSubclass extends JsonElement {
    @Override
    public JsonElement deepCopy() {
      return this;
    }
  }

  @Test
    @Timeout(8000)
  public void getAsNumber_unsupportedOperationException() {
    JsonElement element = new JsonElementSubclass();
    UnsupportedOperationException thrown = assertThrows(UnsupportedOperationException.class, element::getAsNumber);
    assertEquals("JsonElementSubclass", thrown.getMessage());
  }
}