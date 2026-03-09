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
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

public class JsonPrimitive_523_4Test {

  @Test
    @Timeout(8000)
  public void testConstructorWithBoolean_nullThrowsNPE() {
    InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
      Constructor<JsonPrimitive> constructor = JsonPrimitive.class.getConstructor(Boolean.class);
      constructor.newInstance((Boolean) null);
    });
    assertNotNull(exception);
    assertTrue(exception.getCause() instanceof NullPointerException);
  }

  @Test
    @Timeout(8000)
  public void testConstructorWithBoolean_valueSet() throws Exception {
    Boolean bool = Boolean.TRUE;
    Constructor<JsonPrimitive> constructor = JsonPrimitive.class.getConstructor(Boolean.class);
    JsonPrimitive jsonPrimitive = constructor.newInstance(bool);

    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    Object value = valueField.get(jsonPrimitive);

    assertSame(bool, value);
  }

  @Test
    @Timeout(8000)
  public void testIsIntegral_privateMethod() throws Exception {
    Constructor<JsonPrimitive> constructor = JsonPrimitive.class.getConstructor(Boolean.class);
    JsonPrimitive instance = constructor.newInstance(Boolean.TRUE);

    Method isIntegralMethod = JsonPrimitive.class.getDeclaredMethod("isIntegral", JsonPrimitive.class);
    isIntegralMethod.setAccessible(true);

    // Create a JsonPrimitive with integral Number
    Constructor<JsonPrimitive> numberConstructor = JsonPrimitive.class.getConstructor(Number.class);
    JsonPrimitive integralPrimitive = numberConstructor.newInstance(42);

    Boolean resultIntegral = (Boolean) isIntegralMethod.invoke(null, integralPrimitive);
    assertTrue(resultIntegral);

    // Create a JsonPrimitive with non-integral Number
    JsonPrimitive nonIntegralPrimitive = numberConstructor.newInstance(3.14);

    Boolean resultNonIntegral = (Boolean) isIntegralMethod.invoke(null, nonIntegralPrimitive);
    assertFalse(resultNonIntegral);

    // Create a JsonPrimitive with Boolean (not a Number)
    Boolean resultBoolean = (Boolean) isIntegralMethod.invoke(null, instance);
    assertFalse(resultBoolean);
  }
}