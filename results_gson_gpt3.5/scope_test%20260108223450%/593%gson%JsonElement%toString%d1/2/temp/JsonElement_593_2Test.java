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
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class JsonElement_593_2Test {

  static class JsonElementImpl extends JsonElement {
    @Override
    public JsonElement deepCopy() {
      return null;
    }
  }

  @Test
    @Timeout(8000)
  void testToString_success() throws IOException {
    JsonElement element = spy(new JsonElementImpl());

    try (MockedStatic<Streams> streamsMockedStatic = Mockito.mockStatic(Streams.class)) {
      streamsMockedStatic.when(() -> Streams.write(eq(element), any(JsonWriter.class)))
          .thenAnswer(invocation -> {
            JsonWriter writer = invocation.getArgument(1);
            writer.value("mocked");
            return null;
          });

      String result = element.toString();

      assertNotNull(result);
      assertTrue(result.contains("mocked"));
      streamsMockedStatic.verify(() -> Streams.write(eq(element), any(JsonWriter.class)));
    }
  }

  @Test
    @Timeout(8000)
  void testToString_throwsIOException_assertionError() throws IOException {
    JsonElement element = spy(new JsonElementImpl());

    try (MockedStatic<Streams> streamsMockedStatic = Mockito.mockStatic(Streams.class)) {
      streamsMockedStatic.when(() -> Streams.write(eq(element), any(JsonWriter.class)))
          .thenThrow(new IOException("mock IO exception"));

      AssertionError error = assertThrows(AssertionError.class, element::toString);
      assertTrue(error.getCause() instanceof IOException);
      assertEquals("mock IO exception", error.getCause().getMessage());
    }
  }
}