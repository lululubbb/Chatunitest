package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JsonPrimitive_523_6Test {

  @Test
    @Timeout(8000)
  public void testConstructor_withBoolean_shouldSetValue() throws NoSuchFieldException, IllegalAccessException {
    Boolean boolValue = Boolean.TRUE;
    JsonPrimitive jsonPrimitive = new JsonPrimitive(boolValue);

    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    Object value = valueField.get(jsonPrimitive);

    assertSame(boolValue, value);
  }

  @Test
    @Timeout(8000)
  public void testConstructor_withBoolean_nullShouldThrowNPE() {
    assertThrows(NullPointerException.class, () -> new JsonPrimitive((Boolean) null));
  }

  @Test
    @Timeout(8000)
  public void testIsIntegral_privateMethod() throws Exception {
    Constructor<JsonPrimitive> constructor = JsonPrimitive.class.getDeclaredConstructor(Boolean.class);
    constructor.setAccessible(true);
    JsonPrimitive primitiveTrue = constructor.newInstance(Boolean.TRUE);

    Method isIntegralMethod = JsonPrimitive.class.getDeclaredMethod("isIntegral", JsonPrimitive.class);
    isIntegralMethod.setAccessible(true);

    Boolean result = (Boolean) isIntegralMethod.invoke(null, primitiveTrue);
    assertFalse(result);

    // Create JsonPrimitive with Number that is integral
    JsonPrimitive primitiveLong = new JsonPrimitive(Long.valueOf(123L));
    Boolean resultLong = (Boolean) isIntegralMethod.invoke(null, primitiveLong);
    assertTrue(resultLong);

    // Create JsonPrimitive with Number that is not integral (Double)
    JsonPrimitive primitiveDouble = new JsonPrimitive(Double.valueOf(123.45));
    Boolean resultDouble = (Boolean) isIntegralMethod.invoke(null, primitiveDouble);
    assertFalse(resultDouble);
  }
}