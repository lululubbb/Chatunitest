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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonElement_579_2Test {

  JsonElement jsonElementPrimitive;
  JsonElement jsonElementNonPrimitive;

  @BeforeEach
  void setUp() {
    jsonElementPrimitive = mock(JsonElement.class);
    when(jsonElementPrimitive.isJsonPrimitive()).thenReturn(true);

    jsonElementNonPrimitive = mock(JsonElement.class);
    when(jsonElementNonPrimitive.isJsonPrimitive()).thenReturn(false);
    when(jsonElementNonPrimitive.toString()).thenReturn("JsonElementMock");

    // Stub getAsJsonPrimitive to call real method to trigger the exception correctly
    when(jsonElementNonPrimitive.getAsJsonPrimitive()).thenCallRealMethod();
  }

  @Test
    @Timeout(8000)
  void getAsJsonPrimitive_whenIsJsonPrimitive_returnsSelfCast() {
    JsonPrimitive primitive = new JsonPrimitive("test");
    JsonPrimitive spyPrimitive = spy(primitive);
    when(spyPrimitive.isJsonPrimitive()).thenReturn(true);

    JsonPrimitive result = spyPrimitive.getAsJsonPrimitive();
    assertSame(spyPrimitive, result);
  }

  @Test
    @Timeout(8000)
  void getAsJsonPrimitive_whenNotJsonPrimitive_throwsIllegalStateException() {
    IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
      jsonElementNonPrimitive.getAsJsonPrimitive();
    });
    assertEquals("Not a JSON Primitive: JsonElementMock", exception.getMessage());
  }
}