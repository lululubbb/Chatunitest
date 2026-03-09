package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class JsonPrimitive_545_1Test {

  private static Method isIntegralMethod;

  @BeforeAll
  static void setup() throws NoSuchMethodException {
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
  void testIsIntegral_withBigInteger() {
    JsonPrimitive primitive = new JsonPrimitive(BigInteger.TEN);
    assertTrue(invokeIsIntegral(primitive));
  }

  @Test
    @Timeout(8000)
  void testIsIntegral_withLong() {
    JsonPrimitive primitive = new JsonPrimitive(Long.valueOf(10L));
    assertTrue(invokeIsIntegral(primitive));
  }

  @Test
    @Timeout(8000)
  void testIsIntegral_withInteger() {
    JsonPrimitive primitive = new JsonPrimitive(Integer.valueOf(10));
    assertTrue(invokeIsIntegral(primitive));
  }

  @Test
    @Timeout(8000)
  void testIsIntegral_withShort() {
    JsonPrimitive primitive = new JsonPrimitive(Short.valueOf((short)10));
    assertTrue(invokeIsIntegral(primitive));
  }

  @Test
    @Timeout(8000)
  void testIsIntegral_withByte() {
    JsonPrimitive primitive = new JsonPrimitive(Byte.valueOf((byte)10));
    assertTrue(invokeIsIntegral(primitive));
  }

  @Test
    @Timeout(8000)
  void testIsIntegral_withDouble() {
    JsonPrimitive primitive = new JsonPrimitive(Double.valueOf(10.0));
    assertFalse(invokeIsIntegral(primitive));
  }

  @Test
    @Timeout(8000)
  void testIsIntegral_withFloat() {
    JsonPrimitive primitive = new JsonPrimitive(Float.valueOf(10.0f));
    assertFalse(invokeIsIntegral(primitive));
  }

  @Test
    @Timeout(8000)
  void testIsIntegral_withNonNumber() {
    JsonPrimitive primitive = new JsonPrimitive("string");
    assertFalse(invokeIsIntegral(primitive));
  }

  @Test
    @Timeout(8000)
  void testIsIntegral_withNullValue() throws Exception {
    // Use Unsafe to create instance without calling constructor
    // to avoid NoSuchMethodException on default constructor
    java.lang.reflect.Field unsafeField = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
    unsafeField.setAccessible(true);
    sun.misc.Unsafe unsafe = (sun.misc.Unsafe) unsafeField.get(null);

    JsonPrimitive primitive = (JsonPrimitive) unsafe.allocateInstance(JsonPrimitive.class);

    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(primitive, null);

    assertFalse(invokeIsIntegral(primitive));
  }
}