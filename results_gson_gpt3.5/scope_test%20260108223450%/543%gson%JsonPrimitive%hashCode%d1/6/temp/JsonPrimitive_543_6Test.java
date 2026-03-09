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

public class JsonPrimitive_543_6Test {

  private JsonPrimitive jsonPrimitive;

  @BeforeEach
  public void setUp() {
    // Default initialization before each test
    jsonPrimitive = new JsonPrimitive("default");
  }

  @Test
    @Timeout(8000)
  public void testHashCode_valueNull() throws Exception {
    JsonPrimitive instance = new JsonPrimitive("dummy");
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(instance, null);
    int result = instance.hashCode();
    assertEquals(31, result);
  }

  @Test
    @Timeout(8000)
  public void testHashCode_integralValue() throws Exception {
    // Create JsonPrimitive with integral Number value
    JsonPrimitive instance = new JsonPrimitive(123L);

    // We cannot mock static private method easily without a framework like PowerMock,
    // so we test with real behavior. 123L is integral so isIntegral should return true.

    int hash = instance.hashCode();

    long val = instance.getAsNumber().longValue();
    int expected = (int) (val ^ (val >>> 32));
    assertEquals(expected, hash);
  }

  @Test
    @Timeout(8000)
  public void testHashCode_numberNonIntegral() throws Exception {
    // Use a Double value that is not integral
    JsonPrimitive instance = new JsonPrimitive(123.456);

    // isIntegral should return false, so second branch executes
    int hash = instance.hashCode();

    long val = Double.doubleToLongBits(instance.getAsNumber().doubleValue());
    int expected = (int) (val ^ (val >>> 32));
    assertEquals(expected, hash);
  }

  @Test
    @Timeout(8000)
  public void testHashCode_valueIsNumberSubclass() throws Exception {
    // Use LazilyParsedNumber (subclass of Number)
    LazilyParsedNumber lazyNumber = new LazilyParsedNumber("789");
    JsonPrimitive instance = new JsonPrimitive(lazyNumber);

    int hash = instance.hashCode();

    // Fix: LazilyParsedNumber returns doubleValue() 789.0, so hash should be computed accordingly
    // Since isIntegral returns false for LazilyParsedNumber, hashCode uses doubleValue() branch
    long val = Double.doubleToLongBits(instance.getAsNumber().doubleValue());
    int expected = (int) (val ^ (val >>> 32));
    assertEquals(expected, hash);
  }

  @Test
    @Timeout(8000)
  public void testHashCode_valueIsNonNumberNonNull() throws Exception {
    // Use a String value that is not a Number
    JsonPrimitive instance = new JsonPrimitive("testString");

    int hash = instance.hashCode();

    assertEquals("testString".hashCode(), hash);
  }

  @Test
    @Timeout(8000)
  public void testIsIntegral_true() throws Exception {
    // Access private static method isIntegral via reflection
    Method isIntegral = JsonPrimitive.class.getDeclaredMethod("isIntegral", JsonPrimitive.class);
    isIntegral.setAccessible(true);

    JsonPrimitive integralPrimitive = new JsonPrimitive(123);
    boolean result = (boolean) isIntegral.invoke(null, integralPrimitive);
    assertTrue(result);

    JsonPrimitive longPrimitive = new JsonPrimitive(123L);
    result = (boolean) isIntegral.invoke(null, longPrimitive);
    assertTrue(result);

    JsonPrimitive bigIntegerPrimitive = new JsonPrimitive(BigInteger.TEN);
    result = (boolean) isIntegral.invoke(null, bigIntegerPrimitive);
    assertTrue(result);

    JsonPrimitive bigDecimalIntegral = new JsonPrimitive(new BigDecimal("123"));
    result = (boolean) isIntegral.invoke(null, bigDecimalIntegral);
    // Fix: BigDecimal with no fractional part is considered integral
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  public void testIsIntegral_false() throws Exception {
    Method isIntegral = JsonPrimitive.class.getDeclaredMethod("isIntegral", JsonPrimitive.class);
    isIntegral.setAccessible(true);

    JsonPrimitive doublePrimitive = new JsonPrimitive(123.456);
    boolean result = (boolean) isIntegral.invoke(null, doublePrimitive);
    assertFalse(result);

    JsonPrimitive floatPrimitive = new JsonPrimitive(123.4f);
    result = (boolean) isIntegral.invoke(null, floatPrimitive);
    assertFalse(result);

    JsonPrimitive stringPrimitive = new JsonPrimitive("string");
    result = (boolean) isIntegral.invoke(null, stringPrimitive);
    assertFalse(result);

    // Fix: Use the constructor with a valid non-null value and then set value to null by reflection
    JsonPrimitive nullPrimitive = new JsonPrimitive("dummy");
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(nullPrimitive, null);
    result = (boolean) isIntegral.invoke(null, nullPrimitive);
    assertFalse(result);
  }
}