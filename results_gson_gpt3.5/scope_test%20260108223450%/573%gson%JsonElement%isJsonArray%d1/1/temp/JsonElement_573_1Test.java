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
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

class JsonElement_573_1Test {

  @Test
    @Timeout(8000)
  void testIsJsonArray_withJsonArrayInstance_returnsTrue() {
    JsonElement jsonArray = new JsonArray();
    assertTrue(jsonArray.isJsonArray());
  }

  @Test
    @Timeout(8000)
  void testIsJsonArray_withJsonElementInstance_returnsFalse() {
    JsonElement jsonElement = new JsonElement() {
      @Override
      public JsonElement deepCopy() {
        return this;
      }
    };
    assertFalse(jsonElement.isJsonArray());
  }
}