package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;

class JsonPrimitiveEqualsTest {

  @Test
    @Timeout(8000)
  void testEquals_sameInstance() {
    JsonPrimitive jp = new JsonPrimitive("test");
    assertTrue(jp.equals(jp));
  }

  @Test
    @Timeout(8000)
  void testEquals_nullObject() {
    JsonPrimitive jp = new JsonPrimitive("test");
    assertFalse(jp.equals(null));
  }

  @Test
    @Timeout(8000)
  void testEquals_differentClass() {
    JsonPrimitive jp = new JsonPrimitive("test");
    Object other = "test";
    assertFalse(jp.equals(other));
  }

  @Test
    @Timeout(8000)
  void testEquals_bothValueNull() throws Exception {
    JsonPrimitive jp1 = (JsonPrimitive) createJsonPrimitiveWithValue(null);
    JsonPrimitive jp2 = (JsonPrimitive) createJsonPrimitiveWithValue(null);
    assertTrue(jp1.equals(jp2));
  }

  @Test
    @Timeout(8000)
  void testEquals_oneValueNull() throws Exception {
    JsonPrimitive jp1 = (JsonPrimitive) createJsonPrimitiveWithValue(null);
    JsonPrimitive jp2 = new JsonPrimitive("not null");
    assertFalse(jp1.equals(jp2));
    assertFalse(jp2.equals(jp1));
  }

  @Test
    @Timeout(8000)
  void testEquals_integralNumbers_equal() {
    JsonPrimitive jp1 = new JsonPrimitive(123);
    JsonPrimitive jp2 = new JsonPrimitive(123L);
    assertTrue(jp1.equals(jp2));
    assertTrue(jp2.equals(jp1));
  }

  @Test
    @Timeout(8000)
  void testEquals_integralNumbers_notEqual() {
    JsonPrimitive jp1 = new JsonPrimitive(123);
    JsonPrimitive jp2 = new JsonPrimitive(124L);
    assertFalse(jp1.equals(jp2));
    assertFalse(jp2.equals(jp1));
  }

  @Test
    @Timeout(8000)
  void testEquals_numbers_doubleNaN() {
    JsonPrimitive jp1 = new JsonPrimitive(Double.NaN);
    JsonPrimitive jp2 = new JsonPrimitive(Double.NaN);
    assertTrue(jp1.equals(jp2));
  }

  @Test
    @Timeout(8000)
  void testEquals_numbers_doubleEqual() {
    JsonPrimitive jp1 = new JsonPrimitive(1.23);
    JsonPrimitive jp2 = new JsonPrimitive(1.23);
    assertTrue(jp1.equals(jp2));
  }

  @Test
    @Timeout(8000)
  void testEquals_numbers_doubleNotEqual() {
    JsonPrimitive jp1 = new JsonPrimitive(1.23);
    JsonPrimitive jp2 = new JsonPrimitive(4.56);
    assertFalse(jp1.equals(jp2));
  }

  @Test
    @Timeout(8000)
  void testEquals_numbers_nonDoubleEqual() {
    JsonPrimitive jp1 = new JsonPrimitive(new LazilyParsedNumber("123.0"));
    JsonPrimitive jp2 = new JsonPrimitive(new LazilyParsedNumber("123.0"));
    assertTrue(jp1.equals(jp2));
  }

  @Test
    @Timeout(8000)
  void testEquals_otherValueEquals() {
    JsonPrimitive jp1 = new JsonPrimitive("testString");
    JsonPrimitive jp2 = new JsonPrimitive("testString");
    assertTrue(jp1.equals(jp2));
  }

  @Test
    @Timeout(8000)
  void testEquals_otherValueNotEquals() {
    JsonPrimitive jp1 = new JsonPrimitive("testString1");
    JsonPrimitive jp2 = new JsonPrimitive("testString2");
    assertFalse(jp1.equals(jp2));
  }

  @Test
    @Timeout(8000)
  void testIsIntegralPrivateMethod() throws Exception {
    Method isIntegralMethod = JsonPrimitive.class.getDeclaredMethod("isIntegral", JsonPrimitive.class);
    isIntegralMethod.setAccessible(true);

    JsonPrimitive integralInt = new JsonPrimitive(10);
    JsonPrimitive integralLong = new JsonPrimitive(10L);
    JsonPrimitive integralBigInt = new JsonPrimitive(BigInteger.TEN);

    JsonPrimitive nonIntegralDouble = new JsonPrimitive(10.1);
    JsonPrimitive nonIntegralString = new JsonPrimitive("10");

    assertTrue((Boolean) isIntegralMethod.invoke(null, integralInt));
    assertTrue((Boolean) isIntegralMethod.invoke(null, integralLong));
    assertTrue((Boolean) isIntegralMethod.invoke(null, integralBigInt));
    assertFalse((Boolean) isIntegralMethod.invoke(null, nonIntegralDouble));
    assertFalse((Boolean) isIntegralMethod.invoke(null, nonIntegralString));
  }

  private Object createJsonPrimitiveWithValue(Object value) throws Exception {
    // Use reflection to create JsonPrimitive instance with private final value field set to value
    // We use the public constructors for common types, but for null value we must use reflection
    if (value == null) {
      // Use any constructor, then set value field to null forcibly
      JsonPrimitive jp = new JsonPrimitive("dummy");
      var field = JsonPrimitive.class.getDeclaredField("value");
      field.setAccessible(true);
      field.set(jp, null);
      return jp;
    }
    // For other values, just use public constructors
    if (value instanceof Boolean) {
      return new JsonPrimitive((Boolean) value);
    } else if (value instanceof Number) {
      return new JsonPrimitive((Number) value);
    } else if (value instanceof Character) {
      return new JsonPrimitive((Character) value);
    } else if (value instanceof String) {
      return new JsonPrimitive((String) value);
    }
    throw new IllegalArgumentException("Unsupported value type: " + value);
  }
}