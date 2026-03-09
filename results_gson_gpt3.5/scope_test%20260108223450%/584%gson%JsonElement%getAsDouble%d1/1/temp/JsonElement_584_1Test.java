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

public class JsonElement_584_1Test {

  @Test
    @Timeout(8000)
  void testGetAsDouble_throwsUnsupportedOperationException() throws Exception {
    JsonElement element = new JsonElement() {
      @Override
      public JsonElement deepCopy() {
        return this;
      }
    };

    UnsupportedOperationException thrown = assertThrows(UnsupportedOperationException.class, element::getAsDouble);
    // Optionally verify the exception message matches the class simple name
    assert(thrown.getMessage().equals("JsonElement"));
  }
}