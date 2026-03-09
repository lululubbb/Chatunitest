package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

class JsonElement_580_4Test {

  @Test
    @Timeout(8000)
  void getAsJsonNull_whenIsJsonNullTrue_returnsThisAsJsonNull() throws Exception {
    // Create a mock JsonNull instance via Mockito
    JsonNull jsonNull = mock(JsonNull.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));
    when(jsonNull.isJsonNull()).thenReturn(true);
    when(jsonNull.toString()).thenReturn("JsonNull");
    // deepCopy returns itself for simplicity
    when(jsonNull.deepCopy()).thenReturn(jsonNull);

    JsonNull result = jsonNull.getAsJsonNull();

    assertSame(jsonNull, result);
  }

  @Test
    @Timeout(8000)
  void getAsJsonNull_whenIsJsonNullFalse_throwsIllegalStateException() {
    // Instead of mocking JsonElement (which causes serialization issues in toString),
    // create an anonymous subclass overriding isJsonNull and toString.

    JsonElement jsonElement = new JsonElement() {
      @Override
      public boolean isJsonNull() {
        return false;
      }

      @Override
      public JsonElement deepCopy() {
        return this;
      }

      @Override
      public String toString() {
        return "NotJsonNull";
      }
    };

    IllegalStateException exception = assertThrows(IllegalStateException.class, jsonElement::getAsJsonNull);
    assertEquals("Not a JSON Null: NotJsonNull", exception.getMessage());
  }
}