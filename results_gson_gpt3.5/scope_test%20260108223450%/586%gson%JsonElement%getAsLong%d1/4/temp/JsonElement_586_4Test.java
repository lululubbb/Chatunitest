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

public class JsonElement_586_4Test {

  @Test
    @Timeout(8000)
  void testGetAsLong_throwsUnsupportedOperationException() throws Exception {
    JsonElement jsonElement = new JsonElement() {
      @Override
      public JsonElement deepCopy() {
        return this;
      }
    };

    UnsupportedOperationException exception = assertThrows(
        UnsupportedOperationException.class,
        jsonElement::getAsLong);
    // Optionally verify exception message equals class simple name
    // assertEquals("JsonElement", exception.getMessage());
  }
}