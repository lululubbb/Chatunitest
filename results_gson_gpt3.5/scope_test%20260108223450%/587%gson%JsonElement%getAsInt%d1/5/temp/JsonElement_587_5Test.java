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

public class JsonElement_587_5Test {

  @Test
    @Timeout(8000)
  public void testGetAsInt_UnsupportedOperationException() throws Exception {
    // Create anonymous subclass instance since JsonElement is abstract
    JsonElement element = new JsonElement() {
      @Override
      public JsonElement deepCopy() {
        return this;
      }
    };

    // Use reflection to invoke getAsInt (though it's public, reflection is requested)
    Method getAsIntMethod = JsonElement.class.getDeclaredMethod("getAsInt");
    getAsIntMethod.setAccessible(true);

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      getAsIntMethod.invoke(element);
    });

    Throwable cause = thrown.getCause();
    assertEquals(UnsupportedOperationException.class, cause.getClass());
    assertEquals(element.getClass().getSimpleName(), cause.getMessage());
  }
}