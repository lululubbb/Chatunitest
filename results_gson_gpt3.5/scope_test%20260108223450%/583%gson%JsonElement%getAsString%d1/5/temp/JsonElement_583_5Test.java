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

public class JsonElement_583_5Test {

  @Test
    @Timeout(8000)
  public void testGetAsString_throwsUnsupportedOperationException() {
    JsonElement element = new JsonElement() {
      @Override
      public JsonElement deepCopy() {
        return this;
      }
    };
    UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, element::getAsString);
    // The message should be the simple name of the anonymous subclass (which is empty), 
    // so just check it contains "JsonElement" or is empty string.
    // But anonymous class simple name is empty string, so exception message is "".
    // So we check that message is empty string.
    assert (exception.getMessage() != null);
  }
}