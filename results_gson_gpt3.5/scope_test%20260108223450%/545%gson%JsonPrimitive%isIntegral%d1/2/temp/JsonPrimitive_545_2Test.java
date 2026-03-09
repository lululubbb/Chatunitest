package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonPrimitive_545_2Test {

  private Method isIntegralMethod;

  @BeforeEach
  public void setUp() throws NoSuchMethodException {
    isIntegralMethod = JsonPrimitive.class.getDeclaredMethod("isIntegral", JsonPrimitive.class);
    isIntegralMethod.setAccessible(true);
  }

  private boolean invokeIsIntegral(JsonPrimitive instance) {
    try {
      return (boolean) isIntegralMethod.invoke(null, instance);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
    @Timeout(8000)
  public void testIsIntegral_withBigInteger() {
    JsonPrimitive primitive = new JsonPrimitive(BigInteger.ONE);
    assertTrue(invokeIsIntegral(primitive));
  }

  @Test
    @Timeout(8000)
  public void testIsIntegral_withLong() {
    JsonPrimitive primitive = new JsonPrimitive(Long.valueOf(1L));
    assertTrue(invokeIsIntegral(primitive));
  }

  @Test
    @Timeout(8000)
  public void testIsIntegral_withInteger() {
    JsonPrimitive primitive = new JsonPrimitive(Integer.valueOf(1));
    assertTrue(invokeIsIntegral(primitive));
  }

  @Test
    @Timeout(8000)
  public void testIsIntegral_withShort() {
    JsonPrimitive primitive = new JsonPrimitive(Short.valueOf((short)1));
    assertTrue(invokeIsIntegral(primitive));
  }

  @Test
    @Timeout(8000)
  public void testIsIntegral_withByte() {
    JsonPrimitive primitive = new JsonPrimitive(Byte.valueOf((byte)1));
    assertTrue(invokeIsIntegral(primitive));
  }

  @Test
    @Timeout(8000)
  public void testIsIntegral_withNonIntegralNumber() {
    JsonPrimitive primitive = new JsonPrimitive(Double.valueOf(1.0));
    assertFalse(invokeIsIntegral(primitive));
  }

  @Test
    @Timeout(8000)
  public void testIsIntegral_withNonNumberValue() {
    JsonPrimitive primitive = new JsonPrimitive("string");
    assertFalse(invokeIsIntegral(primitive));
  }

  @Test
    @Timeout(8000)
  public void testIsIntegral_withNullValue() throws Exception {
    // Create JsonPrimitive instance using a constructor with a dummy value
    JsonPrimitive primitive = new JsonPrimitive("dummy");
    // Set private final field 'value' to null via reflection
    var valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(primitive, null);
    assertFalse(invokeIsIntegral(primitive));
  }
}