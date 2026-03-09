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

public class JsonElement_587_6Test {

  @Test
    @Timeout(8000)
  public void testGetAsInt_throwsUnsupportedOperationException() {
    JsonElement element = new JsonElement() {
      @Override
      public JsonElement deepCopy() {
        return this;
      }
    };
    UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, element::getAsInt);
    // The message should be the simple class name "JsonElement"
    assert(exception.getMessage().equals("JsonElement"));
  }
}