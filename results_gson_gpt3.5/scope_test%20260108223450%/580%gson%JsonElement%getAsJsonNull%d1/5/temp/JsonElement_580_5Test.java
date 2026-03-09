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

class JsonElement_580_5Test {

  @Test
    @Timeout(8000)
  void getAsJsonNull_whenIsJsonNullTrue_returnsThisAsJsonNull() {
    // Use a mock of JsonNull since JsonNull is final and cannot be subclassed
    JsonNull jsonNullMock = mock(JsonNull.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));
    when(jsonNullMock.isJsonNull()).thenReturn(true);
    when(jsonNullMock.getAsJsonNull()).thenCallRealMethod();

    JsonNull result = jsonNullMock.getAsJsonNull();

    assertSame(jsonNullMock, result);
  }

  @Test
    @Timeout(8000)
  void getAsJsonNull_whenIsJsonNullFalse_throwsIllegalStateException() {
    JsonElement element = mock(JsonElement.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));
    when(element.isJsonNull()).thenReturn(false);

    // Override toString to avoid calling the real toString() which tries to serialize the mock
    when(element.toString()).thenReturn("mocked JsonElement");

    IllegalStateException exception = assertThrows(IllegalStateException.class, element::getAsJsonNull);
    assertTrue(exception.getMessage().startsWith("Not a JSON Null: "));
  }
}