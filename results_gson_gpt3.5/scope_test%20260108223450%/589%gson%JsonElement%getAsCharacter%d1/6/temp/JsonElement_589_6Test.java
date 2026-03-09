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

class JsonElement_589_6Test {

  private static class JsonElementImpl extends JsonElement {
    @Override
    public JsonElement deepCopy() {
      return this;
    }
  }

  @Test
    @Timeout(8000)
  void getAsCharacter_throwsUnsupportedOperationException() throws Exception {
    JsonElement jsonElement = new JsonElementImpl();

    UnsupportedOperationException thrown = assertThrows(UnsupportedOperationException.class, () -> {
      jsonElement.getAsCharacter();
    });

    String expectedMessage = jsonElement.getClass().getSimpleName();
    assert(thrown.getMessage().equals(expectedMessage));
  }
}