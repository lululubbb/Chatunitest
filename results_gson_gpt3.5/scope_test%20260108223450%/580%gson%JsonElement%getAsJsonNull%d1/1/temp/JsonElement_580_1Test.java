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

class JsonElement_580_1Test {

  @Test
    @Timeout(8000)
  void getAsJsonNull_whenIsJsonNullTrue_returnsThisAsJsonNull() {
    JsonNull jsonNull = new JsonNull();
    JsonElement jsonElement = spy(jsonNull);
    when(jsonElement.isJsonNull()).thenReturn(true);
    // No need to stub getAsJsonNull since spy calls real methods

    JsonNull result = jsonElement.getAsJsonNull();

    assertNotNull(result);
    assertSame(jsonElement, result);
  }

  @Test
    @Timeout(8000)
  void getAsJsonNull_whenIsJsonNullFalse_throwsIllegalStateException() {
    JsonElement jsonElement = mock(JsonElement.class);
    when(jsonElement.isJsonNull()).thenReturn(false);
    when(jsonElement.toString()).thenReturn("mockedJsonElement");
    when(jsonElement.getAsJsonNull()).thenCallRealMethod();

    IllegalStateException exception = assertThrows(IllegalStateException.class, jsonElement::getAsJsonNull);
    assertEquals("Not a JSON Null: mockedJsonElement", exception.getMessage());
  }
}