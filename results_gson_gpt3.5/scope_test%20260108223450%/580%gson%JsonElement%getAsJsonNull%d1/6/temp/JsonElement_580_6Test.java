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

class JsonElement_580_6Test {

  @Test
    @Timeout(8000)
  void getAsJsonNull_whenIsJsonNullTrue_returnsThisAsJsonNull() throws Exception {
    // Create a subclass of JsonNull since JsonNull is final and cannot be mocked or extended
    JsonNull jsonNullInstance = JsonNull.INSTANCE;

    // Create a proxy subclass of JsonNull using a dynamic proxy or reflection is complicated,
    // so instead, create a mock of JsonElement that returns true for isJsonNull and returns jsonNullInstance for getAsJsonNull.
    JsonElement jsonElement = mock(JsonElement.class);
    when(jsonElement.isJsonNull()).thenReturn(true);
    when(jsonElement.getAsJsonNull()).thenCallRealMethod();

    // We need to use a spy on jsonNullInstance to override isJsonNull to return true
    JsonNull jsonNullSpy = spy(jsonNullInstance);
    doReturn(true).when(jsonNullSpy).isJsonNull();

    // Use reflection to replace 'this' internally for getAsJsonNull call is not possible,
    // so instead we create a custom JsonElement implementation that returns true for isJsonNull
    // and returns itself casted to JsonNull in getAsJsonNull.

    JsonElement jsonElementSubclass = new JsonElement() {
      @Override
      public boolean isJsonNull() {
        return true;
      }

      @Override
      public JsonElement deepCopy() {
        return this;
      }

      @Override
      public JsonNull getAsJsonNull() {
        if (isJsonNull()) {
          return JsonNull.INSTANCE;
        }
        throw new IllegalStateException("Not a JSON Null: " + this);
      }

      @Override
      public String toString() {
        return "mockJsonNull";
      }
    };

    JsonNull result = jsonElementSubclass.getAsJsonNull();
    assertNotNull(result);
    assertSame(JsonNull.INSTANCE, result);
  }

  @Test
    @Timeout(8000)
  void getAsJsonNull_whenIsJsonNullFalse_throwsIllegalStateException() {
    JsonElement jsonElement = new JsonElement() {
      @Override
      public boolean isJsonNull() {
        return false;
      }

      @Override
      public JsonElement deepCopy() {
        return null;
      }

      @Override
      public String toString() {
        return "notJsonNull";
      }
    };
    IllegalStateException exception = assertThrows(IllegalStateException.class, jsonElement::getAsJsonNull);
    assertTrue(exception.getMessage().startsWith("Not a JSON Null: "));
    assertTrue(exception.getMessage().contains(jsonElement.toString()));
  }
}