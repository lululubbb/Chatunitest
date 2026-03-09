package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonPrimitive_543_5Test {

  private JsonPrimitive jsonPrimitive;

  @BeforeEach
  public void setUp() {
    // no-op, will create instances per test
  }

  @Test
    @Timeout(8000)
  public void testHashCode_valueNull() throws Exception {
    // Removed Proxy creation since JsonPrimitive is a class, not an interface

    // Using reflection to create instance with null value
    jsonPrimitive = new JsonPrimitive("dummy");
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(jsonPrimitive, null);

    int hash = jsonPrimitive.hashCode();
    assertEquals(31, hash);
  }

  @Test
    @Timeout(8000)
  public void testHashCode_integralNumber() throws Exception {
    // Create JsonPrimitive with integral Number value
    jsonPrimitive = new JsonPrimitive(123456789L);

    // Use reflection to invoke private static isIntegral
    Method isIntegralMethod = JsonPrimitive.class.getDeclaredMethod("isIntegral", JsonPrimitive.class);
    isIntegralMethod.setAccessible(true);
    boolean integral = (boolean) isIntegralMethod.invoke(null, jsonPrimitive);
    assertTrue(integral);

    int expectedHash;
    long value = jsonPrimitive.getAsNumber().longValue();
    expectedHash = (int) (value ^ (value >>> 32));

    int actualHash = jsonPrimitive.hashCode();
    assertEquals(expectedHash, actualHash);
  }

  @Test
    @Timeout(8000)
  public void testHashCode_nonIntegralNumber() throws Exception {
    // Create JsonPrimitive with non-integral Number value (Double)
    jsonPrimitive = new JsonPrimitive(123.456d);

    // Use reflection to invoke private static isIntegral
    Method isIntegralMethod = JsonPrimitive.class.getDeclaredMethod("isIntegral", JsonPrimitive.class);
    isIntegralMethod.setAccessible(true);
    boolean integral = (boolean) isIntegralMethod.invoke(null, jsonPrimitive);
    assertFalse(integral);

    long bits = Double.doubleToLongBits(jsonPrimitive.getAsNumber().doubleValue());
    int expectedHash = (int) (bits ^ (bits >>> 32));

    int actualHash = jsonPrimitive.hashCode();
    assertEquals(expectedHash, actualHash);
  }

  @Test
    @Timeout(8000)
  public void testHashCode_valueIsNotNumber() throws Exception {
    // Create JsonPrimitive with String value
    String stringValue = "testString";
    jsonPrimitive = new JsonPrimitive(stringValue);

    // Value is not Number so hashCode should be delegated to value.hashCode()
    int expectedHash = stringValue.hashCode();
    int actualHash = jsonPrimitive.hashCode();
    assertEquals(expectedHash, actualHash);
  }

  @Test
    @Timeout(8000)
  public void testIsIntegral_privateMethod() throws Exception {
    // Test coverage for private static isIntegral method with different values

    Method isIntegralMethod = JsonPrimitive.class.getDeclaredMethod("isIntegral", JsonPrimitive.class);
    isIntegralMethod.setAccessible(true);

    // Integral Number (Integer)
    JsonPrimitive integralPrimitive = new JsonPrimitive(42);
    assertTrue((boolean) isIntegralMethod.invoke(null, integralPrimitive));

    // Integral Number (Long)
    JsonPrimitive longPrimitive = new JsonPrimitive(42L);
    assertTrue((boolean) isIntegralMethod.invoke(null, longPrimitive));

    // Integral Number (Short)
    JsonPrimitive shortPrimitive = new JsonPrimitive((short) 42);
    assertTrue((boolean) isIntegralMethod.invoke(null, shortPrimitive));

    // Non-integral Number (Double)
    JsonPrimitive doublePrimitive = new JsonPrimitive(42.0d);
    assertFalse((boolean) isIntegralMethod.invoke(null, doublePrimitive));

    // Non-Number (String)
    JsonPrimitive stringPrimitive = new JsonPrimitive("42");
    assertFalse((boolean) isIntegralMethod.invoke(null, stringPrimitive));

    // LazilyParsedNumber integral
    JsonPrimitive lazyIntegral = new JsonPrimitive(new LazilyParsedNumber("42"));
    assertTrue((boolean) isIntegralMethod.invoke(null, lazyIntegral));

    // LazilyParsedNumber non-integral
    JsonPrimitive lazyNonIntegral = new JsonPrimitive(new LazilyParsedNumber("42.42"));
    assertFalse((boolean) isIntegralMethod.invoke(null, lazyNonIntegral));
  }
}