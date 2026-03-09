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

public class JsonElement_582_6Test {

  @Test
    @Timeout(8000)
  public void testGetAsNumber_throwsUnsupportedOperationException() {
    JsonElement element = new JsonElement() {
      @Override
      public JsonElement deepCopy() {
        return this;
      }
    };
    UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, element::getAsNumber);
    // The exception message should be the simple class name of the anonymous subclass
    // which is an empty string, so the message is "".
  }
}