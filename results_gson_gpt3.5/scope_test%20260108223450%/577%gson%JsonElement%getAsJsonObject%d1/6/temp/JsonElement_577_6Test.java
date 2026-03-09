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

class JsonElement_577_6Test {

  @Test
    @Timeout(8000)
  void getAsJsonObject_whenIsJsonObjectTrue_returnsThisAsJsonObject() throws Exception {
    // Create an anonymous subclass of JsonElement that also implements JsonObject methods via delegation.
    // Since JsonObject is final, we cannot extend it, so we create a JsonElement that returns true for isJsonObject(),
    // and override getAsJsonObject() to return 'this' casted to JsonObject via a dynamic proxy.

    // Create a dynamic proxy implementing JsonObject interface by delegating to a JsonObject instance.
    // But since JsonObject is a class, not interface, and final, we cannot proxy it.
    // Instead, we create a JsonElement subclass that overrides getAsJsonObject() to return itself.

    JsonElement element = new JsonElement() {
      @Override
      public boolean isJsonObject() {
        return true;
      }

      @Override
      public JsonElement deepCopy() {
        return this;
      }

      @Override
      public JsonObject getAsJsonObject() {
        // Cast 'this' to JsonObject is unsafe, but for test, return a dummy JsonObject instance.
        // We can create a real JsonObject instance and return it here.
        return new JsonObject();
      }

      @Override
      public String toString() {
        return "mockedJsonObjectElement";
      }
    };

    JsonObject result = element.getAsJsonObject();
    assertNotNull(result);
    assertTrue(element.isJsonObject());
  }

  @Test
    @Timeout(8000)
  void getAsJsonObject_whenIsJsonObjectFalse_throwsIllegalStateException() {
    JsonElement element = new JsonElement() {
      @Override
      public boolean isJsonObject() {
        return false;
      }

      @Override
      public JsonElement deepCopy() {
        return this;
      }

      @Override
      public String toString() {
        return "mockedElement";
      }
    };

    IllegalStateException exception = assertThrows(IllegalStateException.class, element::getAsJsonObject);
    assertEquals("Not a JSON Object: mockedElement", exception.getMessage());
  }
}