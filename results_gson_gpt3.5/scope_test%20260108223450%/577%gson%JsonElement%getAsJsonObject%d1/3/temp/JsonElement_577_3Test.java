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

class JsonElement_577_3Test {

  @Test
    @Timeout(8000)
  void getAsJsonObject_whenIsJsonObjectTrue_returnsThisCastToJsonObject() {
    // Create a real JsonObject instance (not a mock) to avoid ClassCastException
    JsonObject jsonObject = new JsonObject();
    // Spy the JsonObject to override isJsonObject to return true
    JsonObject spyObject = spy(jsonObject);
    doReturn(true).when(spyObject).isJsonObject();

    JsonObject result = spyObject.getAsJsonObject();
    assertSame(spyObject, result);
  }

  @Test
    @Timeout(8000)
  void getAsJsonObject_whenIsJsonObjectFalse_throwsIllegalStateException() {
    // Create a real JsonElement subclass instance that is not a JsonObject
    JsonElement jsonElement = new JsonElement() {
      @Override
      public JsonElement deepCopy() {
        return this;
      }

      @Override
      public boolean isJsonObject() {
        return false;
      }

      @Override
      public String toString() {
        return "jsonElementToString";
      }
    };

    IllegalStateException exception = assertThrows(IllegalStateException.class, jsonElement::getAsJsonObject);
    assertEquals("Not a JSON Object: jsonElementToString", exception.getMessage());
  }
}