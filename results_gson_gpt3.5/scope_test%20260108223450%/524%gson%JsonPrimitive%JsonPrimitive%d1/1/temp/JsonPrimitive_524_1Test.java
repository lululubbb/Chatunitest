package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

class JsonPrimitive_524_1Test {

  @Test
    @Timeout(8000)
  void testConstructorWithNumber() throws Exception {
    Number number = Integer.valueOf(123);
    JsonPrimitive jsonPrimitive = new JsonPrimitive(number);

    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    Object value = valueField.get(jsonPrimitive);

    assertNotNull(value);
    assertEquals(number, value);
  }

  @Test
    @Timeout(8000)
  void testConstructorWithNullNumberThrowsNPE() {
    assertThrows(NullPointerException.class, () -> new JsonPrimitive((Number) null));
  }

  @Test
    @Timeout(8000)
  void testIsIntegralPrivateStaticMethod() throws Exception {
    Constructor<JsonPrimitive> constructor = JsonPrimitive.class.getDeclaredConstructor(Number.class);
    constructor.setAccessible(true);
    JsonPrimitive integralPrimitive = constructor.newInstance(123);
    JsonPrimitive decimalPrimitive = constructor.newInstance(123.456);

    var method = JsonPrimitive.class.getDeclaredMethod("isIntegral", JsonPrimitive.class);
    method.setAccessible(true);

    boolean integralResult = (boolean) method.invoke(null, integralPrimitive);
    boolean decimalResult = (boolean) method.invoke(null, decimalPrimitive);

    assertTrue(integralResult);
    assertFalse(decimalResult);
  }

  @Test
    @Timeout(8000)
  void testConstructorWithVariousNumberTypes() throws Exception {
    Number[] numbers = new Number[] {
      Byte.valueOf((byte) 1),
      Short.valueOf((short) 2),
      Integer.valueOf(3),
      Long.valueOf(4L),
      Float.valueOf(5.5f),
      Double.valueOf(6.6),
      new BigDecimal("7.7"),
      new BigInteger("8"),
      new LazilyParsedNumber("9.9")
    };

    for (Number number : numbers) {
      JsonPrimitive primitive = new JsonPrimitive(number);
      Field valueField = JsonPrimitive.class.getDeclaredField("value");
      valueField.setAccessible(true);
      Object value = valueField.get(primitive);
      assertEquals(number, value);
    }
  }
}