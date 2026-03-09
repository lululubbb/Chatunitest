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

class JsonElement_577_2Test {

  @Test
    @Timeout(8000)
  void getAsJsonObject_whenIsJsonObjectTrue_returnsThisAsJsonObject() {
    JsonObject jsonObjectMock = mock(JsonObject.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));
    when(jsonObjectMock.isJsonObject()).thenReturn(true);
    JsonObject result = jsonObjectMock.getAsJsonObject();
    assertSame(jsonObjectMock, result);
  }

  @Test
    @Timeout(8000)
  void getAsJsonObject_whenIsJsonObjectFalse_throwsIllegalStateException() {
    JsonElement jsonElement = mock(JsonElement.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));
    when(jsonElement.isJsonObject()).thenReturn(false);
    // Override toString to avoid Gson serialization of the mock object
    doReturn("mockedJsonElement").when(jsonElement).toString();
    IllegalStateException exception = assertThrows(IllegalStateException.class, jsonElement::getAsJsonObject);
    assertEquals("Not a JSON Object: mockedJsonElement", exception.getMessage());
  }
}