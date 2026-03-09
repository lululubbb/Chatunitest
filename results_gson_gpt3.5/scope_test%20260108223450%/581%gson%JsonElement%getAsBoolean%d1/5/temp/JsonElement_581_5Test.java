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

public class JsonElement_581_5Test {

  @Test
    @Timeout(8000)
  public void testGetAsBoolean_throwsUnsupportedOperationException() {
    JsonElement element = new JsonElement() {
      @Override
      public JsonElement deepCopy() {
        return this;
      }
    };

    UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, element::getAsBoolean);
    // Optional: assert the exception message equals the simple class name
    // assertEquals("JsonElement", exception.getMessage());
  }
}