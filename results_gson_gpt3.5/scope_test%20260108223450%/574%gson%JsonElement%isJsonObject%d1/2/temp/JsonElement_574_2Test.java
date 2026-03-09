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

class JsonElement_574_2Test {

  @Test
    @Timeout(8000)
  void testIsJsonObject_whenInstanceIsJsonObject_shouldReturnTrue() throws Exception {
    JsonObject jsonObject = JsonObject.class.getDeclaredConstructor().newInstance();
    assertTrue(jsonObject.isJsonObject());
  }

  @Test
    @Timeout(8000)
  void testIsJsonObject_whenInstanceIsJsonArray_shouldReturnFalse() throws Exception {
    JsonArray jsonArray = JsonArray.class.getDeclaredConstructor().newInstance();
    assertFalse(jsonArray.isJsonObject());
  }

  @Test
    @Timeout(8000)
  void testIsJsonObject_whenInstanceIsJsonPrimitive_shouldReturnFalse() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive("value");
    assertFalse(jsonPrimitive.isJsonObject());
  }

  @Test
    @Timeout(8000)
  void testIsJsonObject_whenInstanceIsJsonNull_shouldReturnFalse() {
    JsonNull jsonNull = JsonNull.INSTANCE;
    assertFalse(jsonNull.isJsonObject());
  }
}