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

public class JsonElement_585_2Test {

  @Test
    @Timeout(8000)
  public void testGetAsFloat_throwsUnsupportedOperationException() {
    JsonElement element = new JsonElement() {
      @Override
      public JsonElement deepCopy() {
        return null;
      }
    };
    UnsupportedOperationException thrown = assertThrows(UnsupportedOperationException.class, element::getAsFloat);
    // Optional: assert the exception message equals the simple class name
    // assertEquals("JsonElement", thrown.getMessage());
  }
}