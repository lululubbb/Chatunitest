package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;

class JsonElement_576_1Test {

  @Test
    @Timeout(8000)
  void testIsJsonNull_whenInstanceIsJsonNull() {
    JsonElement jsonElement = JsonNull.INSTANCE;
    assertTrue(jsonElement.isJsonNull());
  }

  @Test
    @Timeout(8000)
  void testIsJsonNull_whenInstanceIsNotJsonNull() {
    JsonElement jsonElement = new JsonElement() {
      @Override
      public JsonElement deepCopy() {
        return this;
      }
    };
    assertFalse(jsonElement.isJsonNull());
  }
}