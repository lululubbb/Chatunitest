package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

public class JsonPrimitive_533_1Test {

  @Test
    @Timeout(8000)
  public void testGetAsString_withStringValue() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive("testString");
    String result = jsonPrimitive.getAsString();
    assertEquals("testString", result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsString_withNumberValue() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(123);
    String result = jsonPrimitive.getAsString();
    assertEquals("123", result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsString_withBooleanValue() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(true);
    String result = jsonPrimitive.getAsString();
    assertEquals("true", result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsString_withUnexpectedValue_throwsAssertionError() throws Exception {
    // Instantiate JsonPrimitive via a public constructor (e.g. with a String)
    Constructor<JsonPrimitive> constructor = JsonPrimitive.class.getDeclaredConstructor(String.class);
    constructor.setAccessible(true);
    JsonPrimitive jsonPrimitive = constructor.newInstance("dummy");

    // Use reflection to set the private final field 'value' to an unsupported type (e.g. Object)
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(jsonPrimitive, new Object());

    Method getAsStringMethod = JsonPrimitive.class.getDeclaredMethod("getAsString");
    getAsStringMethod.setAccessible(true);

    AssertionError error = assertThrows(AssertionError.class, () -> {
      try {
        getAsStringMethod.invoke(jsonPrimitive);
      } catch (Exception e) {
        // unwrap InvocationTargetException to throw cause
        Throwable cause = e.getCause();
        if (cause instanceof AssertionError) {
          throw cause;
        }
        throw e;
      }
    });
    assertTrue(error.getMessage().startsWith("Unexpected value type:"));
  }
}