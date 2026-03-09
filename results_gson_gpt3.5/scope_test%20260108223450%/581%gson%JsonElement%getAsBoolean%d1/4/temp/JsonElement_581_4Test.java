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

import com.google.gson.JsonElement;

public class JsonElement_581_4Test {

  private static class JsonElementStub extends JsonElement {
    @Override
    public JsonElement deepCopy() {
      return this;
    }
  }

  @Test
    @Timeout(8000)
  public void testGetAsBoolean_throwsUnsupportedOperationException() {
    JsonElement element = new JsonElementStub();
    UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, element::getAsBoolean);
    assertEquals("JsonElementStub", exception.getMessage());
  }
}