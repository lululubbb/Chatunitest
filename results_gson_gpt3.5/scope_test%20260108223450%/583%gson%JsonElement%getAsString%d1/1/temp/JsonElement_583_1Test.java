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

public class JsonElement_583_1Test {

  private static class JsonElementImpl extends JsonElement {
    @Override
    public JsonElement deepCopy() {
      return this;
    }
  }

  @Test
    @Timeout(8000)
  public void testGetAsString_throwsUnsupportedOperationException() {
    JsonElement element = new JsonElementImpl();
    UnsupportedOperationException thrown = assertThrows(UnsupportedOperationException.class, element::getAsString);
    // The exception message should be the simple class name "JsonElementImpl"
    assert(thrown.getMessage().equals("JsonElementImpl"));
  }
}