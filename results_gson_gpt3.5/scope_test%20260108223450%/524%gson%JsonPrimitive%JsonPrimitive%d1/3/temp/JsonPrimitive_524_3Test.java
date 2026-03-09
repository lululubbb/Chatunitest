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

public class JsonPrimitive_524_3Test {

  @Test
    @Timeout(8000)
  public void testConstructor_withIntegerNumber_setsValue() throws NoSuchFieldException, IllegalAccessException {
    Integer number = 42;
    JsonPrimitive primitive = new JsonPrimitive(number);

    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    Object value = valueField.get(primitive);

    assertSame(number, value);
  }

  @Test
    @Timeout(8000)
  public void testConstructor_withDoubleNumber_setsValue() throws NoSuchFieldException, IllegalAccessException {
    Double number = 3.1415;
    JsonPrimitive primitive = new JsonPrimitive(number);

    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    Object value = valueField.get(primitive);

    assertSame(number, value);
  }

  @Test
    @Timeout(8000)
  public void testConstructor_withNull_throwsNullPointerException() {
    assertThrows(NullPointerException.class, () -> new JsonPrimitive((Number) null));
  }

  @Test
    @Timeout(8000)
  public void testIsIntegral_privateMethod() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchFieldException {
    Constructor<JsonPrimitive> constructor = JsonPrimitive.class.getDeclaredConstructor(Number.class);
    constructor.setAccessible(true);
    JsonPrimitive integralPrimitive = constructor.newInstance(10);
    JsonPrimitive decimalPrimitive = constructor.newInstance(10.5);

    var isIntegralMethod = JsonPrimitive.class.getDeclaredMethod("isIntegral", JsonPrimitive.class);
    isIntegralMethod.setAccessible(true);

    boolean integralResult = (boolean) isIntegralMethod.invoke(null, integralPrimitive);
    boolean decimalResult = (boolean) isIntegralMethod.invoke(null, decimalPrimitive);

    assertTrue(integralResult);
    assertFalse(decimalResult);
  }
}