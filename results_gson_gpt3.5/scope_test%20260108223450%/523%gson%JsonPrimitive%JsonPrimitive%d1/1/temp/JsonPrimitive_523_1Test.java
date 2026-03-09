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

public class JsonPrimitive_523_1Test {

  @Test
    @Timeout(8000)
  public void testConstructor_boolean_setsValue() throws NoSuchFieldException, IllegalAccessException {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(Boolean.TRUE);
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    Object value = valueField.get(jsonPrimitive);
    assertEquals(Boolean.TRUE, value);
  }

  @Test
    @Timeout(8000)
  public void testConstructor_boolean_null_throwsException() {
    assertThrows(NullPointerException.class, () -> new JsonPrimitive((Boolean) null));
  }

  @Test
    @Timeout(8000)
  public void testConstructor_reflection_boolean_setsValue() throws Exception {
    Constructor<JsonPrimitive> constructor = JsonPrimitive.class.getDeclaredConstructor(Boolean.class);
    constructor.setAccessible(true);
    JsonPrimitive jsonPrimitive = constructor.newInstance(Boolean.FALSE);
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    Object value = valueField.get(jsonPrimitive);
    assertEquals(Boolean.FALSE, value);
  }

  @Test
    @Timeout(8000)
  public void testIsIntegral_privateMethod() throws Exception {
    Constructor<JsonPrimitive> constructor = JsonPrimitive.class.getDeclaredConstructor(Boolean.class);
    constructor.setAccessible(true);
    JsonPrimitive jsonPrimitiveTrue = constructor.newInstance(Boolean.TRUE);

    Method isIntegral = JsonPrimitive.class.getDeclaredMethod("isIntegral", JsonPrimitive.class);
    isIntegral.setAccessible(true);

    // Should be false because value is Boolean
    boolean resultTrue = (boolean) isIntegral.invoke(null, jsonPrimitiveTrue);
    assertFalse(resultTrue);

    // Create JsonPrimitive with integral Number value via reflection
    Constructor<JsonPrimitive> numberConstructor = JsonPrimitive.class.getDeclaredConstructor(Number.class);
    numberConstructor.setAccessible(true);
    JsonPrimitive jsonPrimitiveInt = numberConstructor.newInstance(42);

    boolean resultInt = (boolean) isIntegral.invoke(null, jsonPrimitiveInt);
    assertTrue(resultInt);

    // Create JsonPrimitive with non-integral Number (Double)
    JsonPrimitive jsonPrimitiveDouble = numberConstructor.newInstance(42.5);
    boolean resultDouble = (boolean) isIntegral.invoke(null, jsonPrimitiveDouble);
    assertFalse(resultDouble);
  }

}