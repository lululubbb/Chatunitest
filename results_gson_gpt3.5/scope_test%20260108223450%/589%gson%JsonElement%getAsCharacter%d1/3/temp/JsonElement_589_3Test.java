package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JsonElement_589_3Test {

  private static class JsonElementStub extends JsonElement {
    @Override
    public JsonElement deepCopy() {
      return this;
    }
  }

  @Test
    @Timeout(8000)
  public void testGetAsCharacter_throwsUnsupportedOperationException() throws Exception {
    JsonElement jsonElement = new JsonElementStub();

    Method method = JsonElement.class.getDeclaredMethod("getAsCharacter");
    method.setAccessible(true);

    UnsupportedOperationException thrown = assertThrows(
        UnsupportedOperationException.class,
        () -> {
          try {
            method.invoke(jsonElement);
          } catch (InvocationTargetException e) {
            throw e.getCause();
          }
        }
    );

    assertEquals(JsonElementStub.class.getSimpleName(), thrown.getMessage());
  }
}