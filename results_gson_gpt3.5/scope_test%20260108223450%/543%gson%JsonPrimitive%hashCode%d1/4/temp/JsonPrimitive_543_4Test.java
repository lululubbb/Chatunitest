package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonPrimitive_543_4Test {
  private JsonPrimitive jsonPrimitive;

  @BeforeEach
  void setUp() {
    jsonPrimitive = new JsonPrimitive("initial");
  }

  @Test
    @Timeout(8000)
  void testHashCode_valueNull() throws Exception {
    // Set private field value to null
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(jsonPrimitive, null);

    assertEquals(31, jsonPrimitive.hashCode());
  }

  @Test
    @Timeout(8000)
  void testHashCode_integralNumber() throws Exception {
    // Create a JsonPrimitive with integral Number value
    jsonPrimitive = new JsonPrimitive(123L);

    // Use reflection to invoke private static isIntegral(JsonPrimitive)
    Method isIntegralMethod = JsonPrimitive.class.getDeclaredMethod("isIntegral", JsonPrimitive.class);
    isIntegralMethod.setAccessible(true);
    boolean integral = (boolean) isIntegralMethod.invoke(null, jsonPrimitive);
    assertTrue(integral);

    long longValue = jsonPrimitive.getAsNumber().longValue();
    int expectedHash = (int) (longValue ^ (longValue >>> 32));

    assertEquals(expectedHash, jsonPrimitive.hashCode());
  }

  @Test
    @Timeout(8000)
  void testHashCode_nonIntegralNumber() {
    // Create a JsonPrimitive with non-integral Number value (Double)
    jsonPrimitive = new JsonPrimitive(123.456);

    assertFalse(invokeIsIntegral(jsonPrimitive));

    long longBits = Double.doubleToLongBits(jsonPrimitive.getAsNumber().doubleValue());
    int expectedHash = (int) (longBits ^ (longBits >>> 32));

    assertEquals(expectedHash, jsonPrimitive.hashCode());
  }

  @Test
    @Timeout(8000)
  void testHashCode_valueIsOtherObject() throws Exception {
    // Set value to a String (non-Number)
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    String str = "testString";
    valueField.set(jsonPrimitive, str);

    assertEquals(str.hashCode(), jsonPrimitive.hashCode());
  }

  @Test
    @Timeout(8000)
  void testHashCode_valueIsLazilyParsedNumber_integral() throws Exception {
    // LazilyParsedNumber with integral value
    jsonPrimitive = new JsonPrimitive(new LazilyParsedNumber("789"));

    // LazilyParsedNumber is not considered integral by isIntegral method
    // So isIntegral returns false, and hashCode uses double bits.

    assertFalse(invokeIsIntegral(jsonPrimitive));

    double doubleValue = jsonPrimitive.getAsNumber().doubleValue();
    long longBits = Double.doubleToLongBits(doubleValue);
    int expectedHash = (int) (longBits ^ (longBits >>> 32));

    assertEquals(expectedHash, jsonPrimitive.hashCode());
  }

  @Test
    @Timeout(8000)
  void testHashCode_valueIsLazilyParsedNumber_nonIntegral() throws Exception {
    // LazilyParsedNumber with non-integral value
    jsonPrimitive = new JsonPrimitive(new LazilyParsedNumber("789.01"));

    assertFalse(invokeIsIntegral(jsonPrimitive));

    long longBits = Double.doubleToLongBits(jsonPrimitive.getAsNumber().doubleValue());
    int expectedHash = (int) (longBits ^ (longBits >>> 32));

    assertEquals(expectedHash, jsonPrimitive.hashCode());
  }

  private boolean invokeIsIntegral(JsonPrimitive primitive) {
    try {
      Method isIntegralMethod = JsonPrimitive.class.getDeclaredMethod("isIntegral", JsonPrimitive.class);
      isIntegralMethod.setAccessible(true);
      return (boolean) isIntegralMethod.invoke(null, primitive);
    } catch (Exception e) {
      fail("Reflection failed: " + e.getMessage());
      return false;
    }
  }
}