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

public class JsonPrimitive_545_3Test {

  private Method isIntegralMethod;

  @BeforeEach
  public void setUp() throws NoSuchMethodException {
    isIntegralMethod = JsonPrimitive.class.getDeclaredMethod("isIntegral", JsonPrimitive.class);
    isIntegralMethod.setAccessible(true);
  }

  private boolean invokeIsIntegral(JsonPrimitive instance)
      throws InvocationTargetException, IllegalAccessException {
    return (boolean) isIntegralMethod.invoke(null, instance);
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
    JsonPrimitive primitive = new JsonPrimitive(Short.valueOf((short) 12));
    assertTrue(invokeIsIntegral(primitive));
  }

  @Test
    @Timeout(8000)
  public void testIsIntegral_withByte() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive(Byte.valueOf((byte) 1));
    assertTrue(invokeIsIntegral(primitive));
  }

  @Test
    @Timeout(8000)
  public void testIsIntegral_withNonIntegralNumber() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive(Double.valueOf(1.23));
    assertFalse(invokeIsIntegral(primitive));
  }

  @Test
    @Timeout(8000)
  public void testIsIntegral_withLazilyParsedNumberIntegral() throws Exception {
    // LazilyParsedNumber is not one of the integral classes checked in the method, so false expected
    JsonPrimitive primitive = new JsonPrimitive(new com.google.gson.internal.LazilyParsedNumber("123"));
    assertFalse(invokeIsIntegral(primitive));
  }

  @Test
    @Timeout(8000)
  public void testIsIntegral_withNonNumberValue() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive("stringValue");
    assertFalse(invokeIsIntegral(primitive));
  }

  @Test
    @Timeout(8000)
  public void testIsIntegral_withBooleanValue() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive(Boolean.TRUE);
    assertFalse(invokeIsIntegral(primitive));
  }

  @Test
    @Timeout(8000)
  public void testIsIntegral_withCharacterValue() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive('c');
    assertFalse(invokeIsIntegral(primitive));
  }
}