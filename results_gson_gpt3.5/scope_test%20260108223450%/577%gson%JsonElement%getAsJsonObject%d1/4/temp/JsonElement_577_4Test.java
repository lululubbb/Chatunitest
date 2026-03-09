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

class JsonElement_577_4Test {

  @Test
    @Timeout(8000)
  void getAsJsonObject_whenIsJsonObjectTrue_returnsThisAsJsonObject() throws Exception {
    JsonObject jsonObjectMock = mock(JsonObject.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));
    JsonObject spyJsonObject = spy(jsonObjectMock);
    doReturn(true).when(spyJsonObject).isJsonObject();

    JsonObject result = spyJsonObject.getAsJsonObject();
    assertSame(spyJsonObject, result);
  }

  @Test
    @Timeout(8000)
  void getAsJsonObject_whenIsJsonObjectFalse_throwsIllegalStateException() {
    JsonElement element = new JsonElement() {
      @Override public boolean isJsonObject() {
        return false;
      }
      @Override public JsonElement deepCopy() {
        return this;
      }
      @Override
      public String toString() {
        return "JsonElementMock";
      }
    };

    IllegalStateException exception = assertThrows(IllegalStateException.class, element::getAsJsonObject);
    assertTrue(exception.getMessage().startsWith("Not a JSON Object: "));
  }
}