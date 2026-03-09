package com.google.gson;
import org.junit.jupiter.api.Timeout;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.StringWriter;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class JsonElement_593_1Test {

  @Test
    @Timeout(8000)
  void toString_returnsJsonString_whenNoException() throws IOException {
    // Arrange
    JsonElement element = new JsonElement() {
      @Override
      public JsonElement deepCopy() {
        return null;
      }
    };

    // Mock Streams.write to do nothing (simulate successful write)
    try (MockedStatic<Streams> streamsMockedStatic = Mockito.mockStatic(Streams.class)) {
      streamsMockedStatic.when(() -> Streams.write(any(JsonElement.class), any(JsonWriter.class)))
          .thenAnswer(invocation -> {
            // do nothing, simulate successful write
            return null;
          });

      // Act
      String result = element.toString();

      // Assert
      assertNotNull(result);
      assertTrue(result.isEmpty(), "Expected empty string because Streams.write did not write anything");
    }
  }

  @Test
    @Timeout(8000)
  void toString_throwsAssertionError_whenIOExceptionOccurs() throws IOException {
    JsonElement element = new JsonElement() {
      @Override
      public JsonElement deepCopy() {
        return null;
      }
    };

    // Mock Streams.write to throw IOException
    try (MockedStatic<Streams> streamsMockedStatic = Mockito.mockStatic(Streams.class)) {
      streamsMockedStatic.when(() -> Streams.write(any(JsonElement.class), any(JsonWriter.class)))
          .thenThrow(new IOException("mock IOException"));

      // Act & Assert
      AssertionError thrown = assertThrows(AssertionError.class, element::toString);
      assertTrue(thrown.getCause() instanceof IOException);
      assertEquals("mock IOException", thrown.getCause().getMessage());
    }
  }
}