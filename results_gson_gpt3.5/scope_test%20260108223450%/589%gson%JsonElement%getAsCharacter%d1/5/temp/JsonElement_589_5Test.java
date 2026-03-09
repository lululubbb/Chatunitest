package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JsonElement_589_5Test {

  static class JsonElementSubclass extends JsonElement {
    @Override
    public JsonElement deepCopy() {
      return null;
    }
  }

  @Test
    @Timeout(8000)
  void getAsCharacter_throwsUnsupportedOperationException() {
    JsonElement element = new JsonElementSubclass();
    UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, element::getAsCharacter);
    assertEquals("JsonElementSubclass", exception.getMessage());
  }
}