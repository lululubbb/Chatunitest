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

import java.lang.reflect.Method;

public class JsonElement_586_1Test {

  private static class JsonElementConcrete extends JsonElement {
    @Override
    public JsonElement deepCopy() {
      return null;
    }
  }

  @Test
    @Timeout(8000)
  public void testGetAsLong_throwsUnsupportedOperationException() throws Exception {
    JsonElement element = new JsonElementConcrete();

    UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, () -> {
      element.getAsLong();
    });

    // The message should be the simple class name of the instance
    String expectedMessage = element.getClass().getSimpleName();
    assert(exception.getMessage().equals(expectedMessage));
  }
}