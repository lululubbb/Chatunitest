package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;

public class JsonElement_579_1Test {

  @Test
    @Timeout(8000)
  public void testGetAsJsonPrimitive_whenIsJsonPrimitiveTrue_returnsThisAsJsonPrimitive() {
    JsonPrimitive jsonPrimitive = Mockito.mock(JsonPrimitive.class, Mockito.CALLS_REAL_METHODS);
    Mockito.when(jsonPrimitive.isJsonPrimitive()).thenReturn(true);

    JsonPrimitive result = jsonPrimitive.getAsJsonPrimitive();

    assertSame(jsonPrimitive, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsJsonPrimitive_whenIsJsonPrimitiveFalse_throwsIllegalStateException() throws Exception {
    JsonElement jsonElement = Mockito.mock(JsonElement.class, Mockito.CALLS_REAL_METHODS);
    Mockito.when(jsonElement.isJsonPrimitive()).thenReturn(false);

    // Use reflection to set the internal "toString" field or override toString method by proxying
    // Since toString() calls internal serialization which fails on mocks, override toString with a fixed string:
    // We create a subclass to override toString()

    JsonElement jsonElementWithToString = new JsonElement() {
      @Override
      public boolean isJsonPrimitive() {
        return false;
      }

      @Override
      public String toString() {
        return "mockedJsonElement";
      }

      @Override
      public JsonElement deepCopy() {
        throw new UnsupportedOperationException();
      }
    };

    IllegalStateException exception = assertThrows(IllegalStateException.class, jsonElementWithToString::getAsJsonPrimitive);
    assertEquals("Not a JSON Primitive: mockedJsonElement", exception.getMessage());
  }
}