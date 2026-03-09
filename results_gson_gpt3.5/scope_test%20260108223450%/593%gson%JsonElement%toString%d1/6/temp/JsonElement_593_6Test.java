package com.google.gson;
import org.junit.jupiter.api.Timeout;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.StringWriter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class JsonElement_593_6Test {

  private JsonElement jsonElement;

  @BeforeEach
  void setUp() {
    jsonElement = new JsonElement() {
      @Override
      public JsonElement deepCopy() {
        return this;
      }
    };
  }

  @Test
    @Timeout(8000)
  void toString_shouldReturnJsonString_whenNoException() throws IOException {
    try (MockedStatic<Streams> streamsMockedStatic = Mockito.mockStatic(Streams.class)) {
      streamsMockedStatic.when(() -> Streams.write(eq(jsonElement), any(JsonWriter.class)))
          .thenAnswer(invocation -> {
            JsonWriter jsonWriter = invocation.getArgument(1);
            jsonWriter.jsonValue("{\"mockKey\":\"mockValue\"}");
            jsonWriter.flush();
            return null;
          });

      String result = jsonElement.toString();

      assertEquals("{\"mockKey\":\"mockValue\"}", result);
      streamsMockedStatic.verify(() -> Streams.write(eq(jsonElement), any(JsonWriter.class)));
    }
  }

  @Test
    @Timeout(8000)
  void toString_shouldThrowAssertionError_whenIOException() throws IOException {
    try (MockedStatic<Streams> streamsMockedStatic = Mockito.mockStatic(Streams.class)) {
      streamsMockedStatic.when(() -> Streams.write(any(JsonElement.class), any(JsonWriter.class)))
          .thenThrow(new IOException("forced IO exception"));

      JsonElement element = new JsonElement() {
        @Override
        public JsonElement deepCopy() {
          return this;
        }
      };

      AssertionError thrown = assertThrows(AssertionError.class, element::toString);
      assertTrue(thrown.getCause() instanceof IOException);
      assertEquals("forced IO exception", thrown.getCause().getMessage());
    }
  }
}