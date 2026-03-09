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

import java.lang.reflect.Method;

class JsonElement_576_3Test {

  // Concrete subclass of JsonElement to test non-JsonNull instance
  static class JsonElementStub extends JsonElement {
    @Override
    public JsonElement deepCopy() {
      return this;
    }
  }

  // JsonNull is presumably a subclass of JsonElement representing JSON null
  static class JsonNull extends JsonElement {
    @Override
    public JsonElement deepCopy() {
      return this;
    }
  }

  @Test
    @Timeout(8000)
  void testIsJsonNull_withJsonNullInstance() {
    JsonElement jsonNull = new JsonNull() {
      @Override
      public boolean isJsonNull() {
        return true;
      }
    };
    assertTrue(jsonNull.isJsonNull(), "JsonNull instance should return true for isJsonNull()");
  }

  @Test
    @Timeout(8000)
  void testIsJsonNull_withNonJsonNullInstance() {
    JsonElement jsonElement = new JsonElementStub() {
      @Override
      public boolean isJsonNull() {
        return false;
      }
    };
    assertFalse(jsonElement.isJsonNull(), "Non-JsonNull instance should return false for isJsonNull()");
  }

  @Test
    @Timeout(8000)
  void testIsJsonNull_viaReflection() throws Exception {
    JsonElement jsonNull = new JsonNull() {
      @Override
      public boolean isJsonNull() {
        return true;
      }
    };
    JsonElement jsonElement = new JsonElementStub() {
      @Override
      public boolean isJsonNull() {
        return false;
      }
    };

    Method isJsonNullMethod = JsonElement.class.getDeclaredMethod("isJsonNull");
    isJsonNullMethod.setAccessible(true);

    Boolean resultForJsonNull = (Boolean) isJsonNullMethod.invoke(jsonNull);
    Boolean resultForJsonElement = (Boolean) isJsonNullMethod.invoke(jsonElement);

    assertTrue(resultForJsonNull, "Reflection invoke: JsonNull instance should return true");
    assertFalse(resultForJsonElement, "Reflection invoke: Non-JsonNull instance should return false");
  }
}