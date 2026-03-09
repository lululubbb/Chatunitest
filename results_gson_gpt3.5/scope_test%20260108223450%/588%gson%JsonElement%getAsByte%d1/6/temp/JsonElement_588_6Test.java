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

public class JsonElement_588_6Test {

  static class JsonElementImpl extends JsonElement {
    @Override
    public JsonElement deepCopy() {
      return null;
    }
  }

  @Test
    @Timeout(8000)
  public void testGetAsByte_throwsUnsupportedOperationException() {
    JsonElement jsonElement = new JsonElementImpl();
    UnsupportedOperationException thrown = assertThrows(UnsupportedOperationException.class, jsonElement::getAsByte);
    // Optionally assert message equals class simple name
    // assertEquals("JsonElementImpl", thrown.getMessage());
  }
}