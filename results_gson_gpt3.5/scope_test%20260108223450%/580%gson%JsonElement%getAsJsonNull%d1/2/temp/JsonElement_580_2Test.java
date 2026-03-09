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

class JsonElement_580_2Test {

  @Test
    @Timeout(8000)
  void getAsJsonNull_whenIsJsonNullTrue_returnsThisAsJsonNull() {
    JsonNull jsonNull = mock(JsonNull.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));
    doReturn(true).when(jsonNull).isJsonNull();

    JsonNull result = jsonNull.getAsJsonNull();

    assertSame(jsonNull, result);
  }

  @Test
    @Timeout(8000)
  void getAsJsonNull_whenIsJsonNullFalse_throwsIllegalStateException() {
    JsonElement jsonElement = new JsonElement() {
      @Override
      public boolean isJsonNull() {
        return false;
      }
      @Override
      public JsonElement deepCopy() {
        throw new UnsupportedOperationException();
      }
      @Override
      public String toString() {
        // Provide a safe toString() to avoid Gson trying to serialize the anonymous class
        return "JsonElementMock";
      }
    };

    IllegalStateException exception = assertThrows(IllegalStateException.class, jsonElement::getAsJsonNull);
    assertTrue(exception.getMessage().startsWith("Not a JSON Null: "));
  }
}