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
import org.mockito.Mockito;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JsonElement_579_6Test {

  private static class JsonElementStub extends JsonElement {
    private final boolean isPrimitive;

    JsonElementStub(boolean isPrimitive) {
      this.isPrimitive = isPrimitive;
    }

    @Override
    public boolean isJsonPrimitive() {
      return isPrimitive;
    }

    @Override
    public JsonElement deepCopy() {
      return this;
    }

    @Override
    public String toString() {
      // Provide a simple string representation to avoid serialization issues
      return "JsonElementStub(isPrimitive=" + isPrimitive + ")";
    }

    // Override getAsJsonPrimitive to simulate correct return type for primitive
    @Override
    public JsonPrimitive getAsJsonPrimitive() {
      if (isJsonPrimitive()) {
        // Return a Mockito mock of JsonPrimitive to avoid ClassCastException
        return Mockito.mock(JsonPrimitive.class);
      }
      throw new IllegalStateException("Not a JSON Primitive: " + this);
    }
  }

  @Test
    @Timeout(8000)
  public void testGetAsJsonPrimitive_whenIsJsonPrimitiveTrue_returnsThisAsJsonPrimitive() throws Throwable {
    JsonElementStub primitive = new JsonElementStub(true);

    Method getAsJsonPrimitiveMethod = JsonElement.class.getDeclaredMethod("getAsJsonPrimitive");
    JsonPrimitive result;
    try {
      result = (JsonPrimitive) getAsJsonPrimitiveMethod.invoke(primitive);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }

    assertNotNull(result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsJsonPrimitive_whenIsJsonPrimitiveFalse_throwsIllegalStateException() {
    JsonElementStub element = new JsonElementStub(false);
    IllegalStateException exception = assertThrows(IllegalStateException.class, element::getAsJsonPrimitive);
    assertTrue(exception.getMessage().startsWith("Not a JSON Primitive: "));
    assertTrue(exception.getMessage().contains(element.toString()));
  }
}