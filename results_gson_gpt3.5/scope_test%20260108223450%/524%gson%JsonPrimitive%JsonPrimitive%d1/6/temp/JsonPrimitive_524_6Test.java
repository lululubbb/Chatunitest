package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

class JsonPrimitive_524_6Test {

  @Test
    @Timeout(8000)
  void constructor_Number_shouldSetValue() throws NoSuchFieldException, IllegalAccessException {
    Number number = 123;
    JsonPrimitive jsonPrimitive = new JsonPrimitive(number);

    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    Object value = valueField.get(jsonPrimitive);

    assertSame(number, value);
  }

  @Test
    @Timeout(8000)
  void constructor_Number_nullShouldThrow() {
    assertThrows(NullPointerException.class, () -> new JsonPrimitive((Number) null));
  }

  @Test
    @Timeout(8000)
  void isIntegral_privateStaticMethod() throws Exception {
    Constructor<JsonPrimitive> constructor = JsonPrimitive.class.getDeclaredConstructor(Number.class);
    constructor.setAccessible(true);
    JsonPrimitive jpInt = constructor.newInstance(10);
    JsonPrimitive jpLong = constructor.newInstance(10L);
    JsonPrimitive jpDouble = constructor.newInstance(10.5);
    JsonPrimitive jpBigInt = constructor.newInstance(BigInteger.TEN);
    JsonPrimitive jpBigDec = constructor.newInstance(BigDecimal.TEN);

    var isIntegralMethod = JsonPrimitive.class.getDeclaredMethod("isIntegral", JsonPrimitive.class);
    isIntegralMethod.setAccessible(true);

    assertTrue((Boolean) isIntegralMethod.invoke(null, jpInt));
    assertTrue((Boolean) isIntegralMethod.invoke(null, jpLong));
    assertFalse((Boolean) isIntegralMethod.invoke(null, jpDouble));
    assertTrue((Boolean) isIntegralMethod.invoke(null, jpBigInt));
    assertFalse((Boolean) isIntegralMethod.invoke(null, jpBigDec));
  }
}