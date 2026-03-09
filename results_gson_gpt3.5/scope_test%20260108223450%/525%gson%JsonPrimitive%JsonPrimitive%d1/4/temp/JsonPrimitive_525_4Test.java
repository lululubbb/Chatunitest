package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Test;

class JsonPrimitive_525_4Test {

  @Test
    @Timeout(8000)
  void testConstructor_String_setsValue() throws NoSuchFieldException, IllegalAccessException {
    String testString = "testValue";
    JsonPrimitive primitive = new JsonPrimitive(testString);

    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    Object value = valueField.get(primitive);

    assertNotNull(value);
    assertEquals(testString, value);
  }

  @Test
    @Timeout(8000)
  void testConstructor_String_null_throwsNullPointerException() {
    assertThrows(NullPointerException.class, () -> new JsonPrimitive((String) null));
  }

  @Test
    @Timeout(8000)
  void testPrivateIsIntegral_trueAndFalse() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
    Constructor<JsonPrimitive> constructor = JsonPrimitive.class.getDeclaredConstructor(String.class);
    constructor.setAccessible(true);
    JsonPrimitive integralPrimitive = constructor.newInstance("123");
    JsonPrimitive nonIntegralPrimitive = constructor.newInstance("123.45");

    // Access private static method isIntegral(JsonPrimitive)
    var method = JsonPrimitive.class.getDeclaredMethod("isIntegral", JsonPrimitive.class);
    method.setAccessible(true);

    Boolean resultIntegral = (Boolean) method.invoke(null, integralPrimitive);
    Boolean resultNonIntegral = (Boolean) method.invoke(null, nonIntegralPrimitive);

    // Fix: The "integralPrimitive" is constructed with a String, but internally JsonPrimitive treats numbers as LazilyParsedNumber.
    // We need to create JsonPrimitive with a Number to properly test isIntegral.
    Constructor<JsonPrimitive> numberConstructor = JsonPrimitive.class.getDeclaredConstructor(Number.class);
    numberConstructor.setAccessible(true);
    JsonPrimitive integralNumberPrimitive = numberConstructor.newInstance(123);
    JsonPrimitive nonIntegralNumberPrimitive = numberConstructor.newInstance(123.45);

    Boolean resultIntegralNumber = (Boolean) method.invoke(null, integralNumberPrimitive);
    Boolean resultNonIntegralNumber = (Boolean) method.invoke(null, nonIntegralNumberPrimitive);

    assertTrue(resultIntegralNumber);
    assertFalse(resultNonIntegralNumber);
  }
}