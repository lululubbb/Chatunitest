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

import java.lang.reflect.Method;

public class JsonElement_584_4Test {

  private static class JsonElementImpl extends JsonElement {
    @Override
    public JsonElement deepCopy() {
      return null;
    }
  }

  @Test
    @Timeout(8000)
  public void testGetAsDouble_throwsUnsupportedOperationException() throws Exception {
    JsonElement element = new JsonElementImpl();

    UnsupportedOperationException thrown = assertThrows(UnsupportedOperationException.class, element::getAsDouble);
    assertEquals("JsonElementImpl", thrown.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testGetAsDouble_reflectionInvoke() throws Exception {
    JsonElement element = new JsonElementImpl();

    Method method = JsonElement.class.getDeclaredMethod("getAsDouble");
    method.setAccessible(true);

    try {
      method.invoke(element);
    } catch (Exception e) {
      Throwable cause = e.getCause();
      assertEquals(UnsupportedOperationException.class, cause.getClass());
      assertEquals("JsonElementImpl", cause.getMessage());
    }
  }
}