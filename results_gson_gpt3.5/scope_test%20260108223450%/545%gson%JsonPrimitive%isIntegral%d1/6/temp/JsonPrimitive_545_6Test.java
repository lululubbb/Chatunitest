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

public class JsonPrimitive_545_6Test {

  private Method isIntegralMethod;

  @BeforeEach
  public void setUp() throws NoSuchMethodException {
    isIntegralMethod = JsonPrimitive.class.getDeclaredMethod("isIntegral", JsonPrimitive.class);
    isIntegralMethod.setAccessible(true);
  }

  private boolean invokeIsIntegral(JsonPrimitive primitive) {
    try {
      return (boolean) isIntegralMethod.invoke(null, primitive);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
    @Timeout(8000)
  public void testIsIntegral_withBigInteger() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive(BigInteger.TEN);
    assertTrue(invokeIsIntegral(primitive));
  }

  @Test
    @Timeout(8000)
  public void testIsIntegral_withLong() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive(Long.valueOf(10L));
    assertTrue(invokeIsIntegral(primitive));
  }

  @Test
    @Timeout(8000)
  public void testIsIntegral_withInteger() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive(Integer.valueOf(10));
    assertTrue(invokeIsIntegral(primitive));
  }

  @Test
    @Timeout(8000)
  public void testIsIntegral_withShort() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive(Short.valueOf((short)10));
    assertTrue(invokeIsIntegral(primitive));
  }

  @Test
    @Timeout(8000)
  public void testIsIntegral_withByte() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive(Byte.valueOf((byte)10));
    assertTrue(invokeIsIntegral(primitive));
  }

  @Test
    @Timeout(8000)
  public void testIsIntegral_withDouble() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive(Double.valueOf(10.0));
    assertFalse(invokeIsIntegral(primitive));
  }

  @Test
    @Timeout(8000)
  public void testIsIntegral_withFloat() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive(Float.valueOf(10.0f));
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
    JsonPrimitive primitive = mock(JsonPrimitive.class);
    // forcibly set private field value to null
    try {
      var valueField = JsonPrimitive.class.getDeclaredField("value");
      valueField.setAccessible(true);
      valueField.set(primitive, null);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    // Since value is null, not instance of Number, should return false
    assertFalse(invokeIsIntegral(primitive));
  }
}