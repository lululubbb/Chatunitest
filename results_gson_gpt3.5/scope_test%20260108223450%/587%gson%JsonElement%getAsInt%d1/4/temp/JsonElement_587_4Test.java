package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JsonElement_587_4Test {

  private static class JsonElementSubclass extends JsonElement {
    @Override
    public JsonElement deepCopy() {
      return this;
    }
  }

  @Test
    @Timeout(8000)
  public void testGetAsInt_throwsUnsupportedOperationException() throws Exception {
    JsonElement element = new JsonElementSubclass();

    Method getAsIntMethod = JsonElement.class.getDeclaredMethod("getAsInt");
    getAsIntMethod.setAccessible(true);

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      getAsIntMethod.invoke(element);
    });

    Throwable cause = thrown.getCause();
    assertEquals(UnsupportedOperationException.class, cause.getClass());
    String expectedMessage = element.getClass().getSimpleName();
    assertEquals(expectedMessage, cause.getMessage());
  }
}