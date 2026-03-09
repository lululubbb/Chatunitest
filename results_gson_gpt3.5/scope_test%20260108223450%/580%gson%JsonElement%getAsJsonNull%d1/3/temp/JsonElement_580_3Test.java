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

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonElement_580_3Test {

  JsonElement jsonElementMock;
  JsonNull jsonNullMock;

  @BeforeEach
  void setUp() {
    jsonElementMock = mock(JsonElement.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));
    jsonNullMock = mock(JsonNull.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));
  }

  @Test
    @Timeout(8000)
  void getAsJsonNull_whenIsJsonNullTrue_returnsThisAsJsonNull() {
    when(jsonNullMock.isJsonNull()).thenReturn(true);
    JsonNull result = jsonNullMock.getAsJsonNull();
    assertSame(jsonNullMock, result);
  }

  @Test
    @Timeout(8000)
  void getAsJsonNull_whenIsJsonNullFalse_throwsIllegalStateException() {
    when(jsonElementMock.isJsonNull()).thenReturn(false);
    // Mock toString to avoid serialization issues in exception message
    when(jsonElementMock.toString()).thenReturn("JsonElementMock");
    IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
      jsonElementMock.getAsJsonNull();
    });
    assertTrue(exception.getMessage().startsWith("Not a JSON Null: "));
  }
}