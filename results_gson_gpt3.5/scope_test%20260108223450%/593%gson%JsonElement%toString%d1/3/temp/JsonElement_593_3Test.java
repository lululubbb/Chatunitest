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
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class JsonElement_593_3Test {

  @Test
    @Timeout(8000)
  public void testToString_success() throws Exception {
    JsonElement jsonElement = new JsonElement() {
      @Override
      public JsonElement deepCopy() {
        return null;
      }
    };

    // Mock Streams.write to do nothing (normal successful case)
    try (MockedStatic<Streams> mockedStreams = Mockito.mockStatic(Streams.class)) {
      mockedStreams.when(() -> Streams.write(eq(jsonElement), any(JsonWriter.class))).thenAnswer(invocation -> null);

      String result = jsonElement.toString();
      // The result should be an empty string because StringWriter is empty and Streams.write mocked to do nothing
      assertNotNull(result);
      // Since Streams.write does not write anything, stringWriter.toString() is empty string
      assertEquals("", result);
      mockedStreams.verify(() -> Streams.write(eq(jsonElement), any(JsonWriter.class)));
    }
  }

  @Test
    @Timeout(8000)
  public void testToString_ioExceptionThrown() throws Exception {
    JsonElement jsonElement = new JsonElement() {
      @Override
      public JsonElement deepCopy() {
        return null;
      }
    };

    // Mock Streams.write to throw IOException
    try (MockedStatic<Streams> mockedStreams = Mockito.mockStatic(Streams.class)) {
      mockedStreams.when(() -> Streams.write(eq(jsonElement), any(JsonWriter.class))).thenThrow(new IOException("forced"));

      AssertionError thrown = assertThrows(AssertionError.class, jsonElement::toString);
      assertNotNull(thrown.getCause());
      assertTrue(thrown.getCause() instanceof IOException);
      assertEquals("forced", thrown.getCause().getMessage());
      mockedStreams.verify(() -> Streams.write(eq(jsonElement), any(JsonWriter.class)));
    }
  }
}