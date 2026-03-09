package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

public class JsonPrimitive_525_6Test {

  @Test
    @Timeout(8000)
  public void testConstructor_String_setsValue() throws Exception {
    String testString = "testValue";
    JsonPrimitive jsonPrimitive = new JsonPrimitive(testString);

    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    Object value = valueField.get(jsonPrimitive);

    assertEquals(testString, value);
  }

  @Test
    @Timeout(8000)
  public void testConstructor_String_null_throwsNullPointerException() {
    assertThrows(NullPointerException.class, () -> new JsonPrimitive((String) null));
  }

  @Test
    @Timeout(8000)
  public void testIsIntegral_privateStaticMethod() throws Exception {
    // Create JsonPrimitive instances with integral and non-integral values
    JsonPrimitive integralPrimitive = new JsonPrimitive(123);
    JsonPrimitive nonIntegralPrimitive = new JsonPrimitive(123.45);

    Method isIntegralMethod = JsonPrimitive.class.getDeclaredMethod("isIntegral", JsonPrimitive.class);
    isIntegralMethod.setAccessible(true);

    boolean integralResult = (boolean) isIntegralMethod.invoke(null, integralPrimitive);
    boolean nonIntegralResult = (boolean) isIntegralMethod.invoke(null, nonIntegralPrimitive);

    assertTrue(integralResult);
    assertFalse(nonIntegralResult);
  }
}