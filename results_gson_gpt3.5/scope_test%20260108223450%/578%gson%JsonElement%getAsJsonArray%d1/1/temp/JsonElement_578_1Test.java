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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.junit.jupiter.api.Test;

class JsonElement_578_1Test {

  @Test
    @Timeout(8000)
  void getAsJsonArray_whenIsJsonArrayTrue_returnsThisAsJsonArray() {
    // Create a real JsonArray instance (or a spy) instead of mocking with extraInterfaces
    JsonArray jsonArray = mock(JsonArray.class);

    // Create a spy of JsonElement that delegates to jsonArray
    JsonElement jsonElement = spy(jsonArray);

    // Override isJsonArray to return true
    doReturn(true).when(jsonElement).isJsonArray();

    // The real getAsJsonArray method will cast 'this' to JsonArray, which is safe here
    when(jsonElement.getAsJsonArray()).thenCallRealMethod();

    JsonArray result = jsonElement.getAsJsonArray();
    assertNotNull(result);
    assertSame(jsonElement, result);
  }

  @Test
    @Timeout(8000)
  void getAsJsonArray_whenIsJsonArrayFalse_throwsIllegalStateException() {
    JsonElement jsonElement = mock(JsonElement.class);
    when(jsonElement.isJsonArray()).thenReturn(false);
    when(jsonElement.toString()).thenReturn("NotJsonArray");

    when(jsonElement.getAsJsonArray()).thenCallRealMethod();

    IllegalStateException exception = assertThrows(IllegalStateException.class, jsonElement::getAsJsonArray);
    assertEquals("Not a JSON Array: NotJsonArray", exception.getMessage());
  }
}