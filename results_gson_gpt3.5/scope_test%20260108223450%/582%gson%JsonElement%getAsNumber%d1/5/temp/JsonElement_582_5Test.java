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

import com.google.gson.JsonElement;
import org.junit.jupiter.api.Test;

public class JsonElement_582_5Test {

  private static class JsonElementImpl extends JsonElement {
    @Override
    public JsonElement deepCopy() {
      return this;
    }
  }

  @Test
    @Timeout(8000)
  public void testGetAsNumber_throwsUnsupportedOperationException() {
    JsonElement element = new JsonElementImpl();
    UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, element::getAsNumber);
    assertEquals("JsonElementImpl", exception.getMessage());
  }
}