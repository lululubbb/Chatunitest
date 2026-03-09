package com.google.gson;
import org.junit.jupiter.api.Timeout;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.io.StringWriter;

class JsonElement_593_5Test {

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
  void toString_returnsJsonString_whenNoException() throws IOException {
    // Arrange
    try (MockedStatic<Streams> streamsMockedStatic = mockStatic(Streams.class)) {
      streamsMockedStatic.when(() -> Streams.write(eq(jsonElement), any(JsonWriter.class)))
          .thenAnswer(invocation -> {
            JsonWriter writer = invocation.getArgument(1);
            writer.jsonValue("{\"mocked\":true}");
            return null;
          });

      // Act
      String result = jsonElement.toString();

      // Assert
      assertNotNull(result);
      assertTrue(result.contains("mocked"));
      streamsMockedStatic.verify(() -> Streams.write(eq(jsonElement), any(JsonWriter.class)));
    }
  }

  @Test
    @Timeout(8000)
  void toString_throwsAssertionError_whenIOExceptionOccurs() throws IOException {
    // Arrange
    try (MockedStatic<Streams> streamsMockedStatic = mockStatic(Streams.class)) {
      streamsMockedStatic.when(() -> Streams.write(eq(jsonElement), any(JsonWriter.class)))
          .thenThrow(new IOException("mock IO error"));

      // Act & Assert
      AssertionError error = assertThrows(AssertionError.class, () -> jsonElement.toString());
      assertTrue(error.getCause() instanceof IOException);
      assertEquals("mock IO error", error.getCause().getMessage());
      streamsMockedStatic.verify(() -> Streams.write(eq(jsonElement), any(JsonWriter.class)));
    }
  }
}