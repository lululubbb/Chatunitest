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
import java.lang.reflect.InvocationTargetException;

class JsonElement_585_5Test {

  private static class JsonElementSubclass extends com.google.gson.JsonElement {
    @Override
    public com.google.gson.JsonElement deepCopy() {
      return null;
    }
  }

  @Test
    @Timeout(8000)
  void testGetAsFloat_throwsUnsupportedOperationException() throws Exception {
    JsonElementSubclass element = new JsonElementSubclass();
    Method getAsFloatMethod = JsonElementSubclass.class.getMethod("getAsFloat");

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      getAsFloatMethod.invoke(element);
    });
    Throwable cause = thrown.getCause();
    assertTrue(cause instanceof UnsupportedOperationException);
    assertEquals("JsonElementSubclass", cause.getMessage());
  }
}