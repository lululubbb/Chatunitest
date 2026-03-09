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

class JsonElement_577_5Test {

  @Test
    @Timeout(8000)
  void getAsJsonObject_whenIsJsonObjectTrue_returnsThisAsJsonObject() {
    JsonObject spyObject = spy(JsonObject.class);
    doReturn(true).when(spyObject).isJsonObject();

    JsonObject result = spyObject.getAsJsonObject();

    assertSame(spyObject, result);
  }

  @Test
    @Timeout(8000)
  void getAsJsonObject_whenIsJsonObjectFalse_throwsIllegalStateException() throws Exception {
    // Create a real instance of JsonElement via anonymous subclass without overriding toString()
    JsonElement element = new JsonElement() {
      @Override
      public JsonElement deepCopy() {
        return this;
      }
      @Override
      public boolean isJsonObject() {
        return false;
      }
      @Override
      public boolean isJsonArray() { return false; }
      @Override
      public boolean isJsonPrimitive() { return false; }
      @Override
      public boolean isJsonNull() { return false; }
      @Override
      public String toString() {
        return "JsonElement{}";
      }
    };

    IllegalStateException exception = assertThrows(IllegalStateException.class, element::getAsJsonObject);
    assertEquals("Not a JSON Object: JsonElement{}", exception.getMessage());
  }
}