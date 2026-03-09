package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigInteger;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.gson.JsonElement;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.math.BigDecimal;

public class JsonElement_590_1Test {

  @Test
    @Timeout(8000)
  public void testGetAsBigDecimal_throwsUnsupportedOperationException() throws Exception {
    // Create anonymous subclass instance of abstract JsonElement
    JsonElement jsonElement = new JsonElement() {
      @Override
      public JsonElement deepCopy() {
        return this;
      }
    };

    UnsupportedOperationException thrown = assertThrows(UnsupportedOperationException.class, () -> {
      jsonElement.getAsBigDecimal();
    });

    String expectedMessage = jsonElement.getClass().getSimpleName();
    assertEquals(expectedMessage, thrown.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testGetAsBigDecimal_usingReflection_directInvocation() throws Exception {
    JsonElement jsonElement = new JsonElement() {
      @Override
      public JsonElement deepCopy() {
        return this;
      }
    };

    Method method = JsonElement.class.getDeclaredMethod("getAsBigDecimal");
    method.setAccessible(true);

    try {
      method.invoke(jsonElement);
    } catch (Exception e) {
      Throwable cause = e.getCause();
      assertEquals(UnsupportedOperationException.class, cause.getClass());
      assertEquals(jsonElement.getClass().getSimpleName(), cause.getMessage());
    }
  }
}