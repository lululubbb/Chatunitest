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
import com.google.gson.JsonElement;

public class JsonElement_586_6Test {

  static class JsonElementStub extends JsonElement {
    @Override
    public JsonElement deepCopy() {
      return this;
    }

    @Override
    public long getAsLong() {
      throw new UnsupportedOperationException("JsonElementStub");
    }
  }

  @Test
    @Timeout(8000)
  public void testGetAsLong_throwsUnsupportedOperationException() throws Exception {
    JsonElement element = new JsonElementStub();

    UnsupportedOperationException thrown = assertThrows(UnsupportedOperationException.class, () -> {
      element.getAsLong();
    });

    assertEquals("JsonElementStub", thrown.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testGetAsLong_reflectionInvocation_throwsUnsupportedOperationException() throws Exception {
    JsonElement element = new JsonElementStub();

    Method method = JsonElement.class.getDeclaredMethod("getAsLong");
    method.setAccessible(true);

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      method.invoke(element);
    });

    Throwable cause = thrown.getCause();
    if (cause instanceof UnsupportedOperationException) {
      assertEquals("JsonElementStub", cause.getMessage());
    } else {
      fail("Expected cause to be UnsupportedOperationException");
    }
  }
}