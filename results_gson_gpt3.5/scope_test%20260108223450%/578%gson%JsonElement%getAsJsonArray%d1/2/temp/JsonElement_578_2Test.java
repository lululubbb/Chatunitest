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

public class JsonElement_578_2Test {

  @Test
    @Timeout(8000)
  public void testGetAsJsonArray_whenIsJsonArrayTrue_returnsThisAsJsonArray() {
    // Create a real JsonArray instance instead of mocking it
    JsonArray jsonArray = new JsonArray();

    // Spy on the real instance to override isJsonArray()
    JsonArray spyJsonArray = spy(jsonArray);

    when(spyJsonArray.isJsonArray()).thenReturn(true);

    JsonArray result = spyJsonArray.getAsJsonArray();

    assertSame(spyJsonArray, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsJsonArray_whenIsJsonArrayFalse_throwsIllegalStateException() {
    JsonElement element = new JsonElement() {
      @Override
      public boolean isJsonArray() {
        return false;
      }

      @Override
      public JsonElement deepCopy() {
        return null;
      }

      @Override
      public String toString() {
        return "notJsonArrayElement";
      }
    };

    IllegalStateException thrown = assertThrows(IllegalStateException.class, element::getAsJsonArray);
    assertEquals("Not a JSON Array: notJsonArrayElement", thrown.getMessage());
  }
}