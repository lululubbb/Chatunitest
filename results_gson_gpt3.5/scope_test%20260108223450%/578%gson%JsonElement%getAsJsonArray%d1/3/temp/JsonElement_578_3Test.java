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

class JsonElement_578_3Test {

  @Test
    @Timeout(8000)
  void getAsJsonArray_whenIsJsonArrayTrue_returnsThisAsJsonArray() {
    // Arrange
    JsonArray jsonArray = mock(JsonArray.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));
    when(jsonArray.isJsonArray()).thenReturn(true);

    // Act
    JsonArray result = jsonArray.getAsJsonArray();

    // Assert
    assertNotNull(result);
    assertSame(jsonArray, result);
  }

  @Test
    @Timeout(8000)
  void getAsJsonArray_whenIsJsonArrayFalse_throwsIllegalStateException() {
    // Arrange
    JsonElement jsonElement = mock(JsonElement.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));
    when(jsonElement.isJsonArray()).thenReturn(false);

    // Override toString to avoid Gson serialization of the mock
    doReturn("mockedJsonElement").when(jsonElement).toString();

    // Act & Assert
    IllegalStateException exception = assertThrows(IllegalStateException.class, jsonElement::getAsJsonArray);
    assertEquals("Not a JSON Array: mockedJsonElement", exception.getMessage());
  }
}