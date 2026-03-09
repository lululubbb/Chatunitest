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

public class JsonElement_578_6Test {

  private static class JsonArrayStub extends JsonArray {
    @Override
    public boolean isJsonArray() {
      return true;
    }

    @Override
    public JsonElement deepCopy() {
      throw new UnsupportedOperationException();
    }
  }

  @Test
    @Timeout(8000)
  public void testGetAsJsonArray_WhenIsJsonArrayTrue_ReturnsThisAsJsonArray() {
    JsonElement jsonElement = new JsonArrayStub();
    JsonArray result = jsonElement.getAsJsonArray();
    assertSame(jsonElement, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsJsonArray_WhenIsJsonArrayFalse_ThrowsIllegalStateException() {
    JsonElement element = mock(JsonElement.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));
    when(element.isJsonArray()).thenReturn(false);
    when(element.toString()).thenReturn("mocked element");
    IllegalStateException exception = assertThrows(IllegalStateException.class, element::getAsJsonArray);
    assertTrue(exception.getMessage().startsWith("Not a JSON Array: "));
  }
}