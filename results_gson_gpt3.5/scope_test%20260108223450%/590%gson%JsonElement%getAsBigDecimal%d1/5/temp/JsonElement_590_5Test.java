package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.gson.JsonElement;
import org.junit.jupiter.api.Test;

public class JsonElement_590_5Test {

  @Test
    @Timeout(8000)
  public void testGetAsBigDecimal_throwsUnsupportedOperationException() {
    JsonElement element = new JsonElement() {
      @Override
      public JsonElement deepCopy() {
        return this;
      }
    };

    UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, element::getAsBigDecimal);
    // Optionally verify exception message contains class simple name
    // assertTrue(exception.getMessage().contains("JsonElement"));
  }
}