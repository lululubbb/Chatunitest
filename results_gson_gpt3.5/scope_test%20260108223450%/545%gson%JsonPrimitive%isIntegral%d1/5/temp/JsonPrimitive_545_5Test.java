package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonPrimitive_545_5Test {

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
  public void testIsIntegral_withBigInteger() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive(BigInteger.ONE);
    assertTrue(invokeIsIntegral(primitive));
  }

  @Test
    @Timeout(8000)
  public void testIsIntegral_withLong() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive(Long.valueOf(123L));
    assertTrue(invokeIsIntegral(primitive));
  }

  @Test
    @Timeout(8000)
  public void testIsIntegral_withInteger() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive(Integer.valueOf(123));
    assertTrue(invokeIsIntegral(primitive));
  }

  @Test
    @Timeout(8000)
  public void testIsIntegral_withShort() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive(Short.valueOf((short) 123));
    assertTrue(invokeIsIntegral(primitive));
  }

  @Test
    @Timeout(8000)
  public void testIsIntegral_withByte() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive(Byte.valueOf((byte) 123));
    assertTrue(invokeIsIntegral(primitive));
  }

  @Test
    @Timeout(8000)
  public void testIsIntegral_withNonIntegralNumber() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive(Double.valueOf(123.45));
    assertFalse(invokeIsIntegral(primitive));
  }

  @Test
    @Timeout(8000)
  public void testIsIntegral_withNonNumber() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive("string");
    assertFalse(invokeIsIntegral(primitive));
  }

  @Test
    @Timeout(8000)
  public void testIsIntegral_withNullValue() throws Exception {
    // Create instance using one of the public constructors with a valid value
    JsonPrimitive primitive = new JsonPrimitive("initial");
    // forcibly set private final field value to null
    Field field = JsonPrimitive.class.getDeclaredField("value");
    field.setAccessible(true);
    field.set(primitive, null);
    assertFalse(invokeIsIntegral(primitive));
  }
}