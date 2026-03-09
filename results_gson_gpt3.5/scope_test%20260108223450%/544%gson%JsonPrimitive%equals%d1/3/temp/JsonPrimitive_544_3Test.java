package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;

public class JsonPrimitive_544_3Test {

  @Test
    @Timeout(8000)
  void testEquals_sameInstance() {
    JsonPrimitive p = new JsonPrimitive("test");
    assertTrue(p.equals(p));
  }

  @Test
    @Timeout(8000)
  void testEquals_null() {
    JsonPrimitive p = new JsonPrimitive("test");
    assertFalse(p.equals(null));
  }

  @Test
    @Timeout(8000)
  void testEquals_differentClass() {
    JsonPrimitive p = new JsonPrimitive("test");
    assertFalse(p.equals("test"));
  }

  @Test
    @Timeout(8000)
  void testEquals_bothNullValue() throws Exception {
    JsonPrimitive p1 = (JsonPrimitive) createInstanceWithValue(null);
    JsonPrimitive p2 = (JsonPrimitive) createInstanceWithValue(null);
    assertTrue(p1.equals(p2));
  }

  @Test
    @Timeout(8000)
  void testEquals_oneNullValue() throws Exception {
    JsonPrimitive p1 = (JsonPrimitive) createInstanceWithValue(null);
    JsonPrimitive p2 = new JsonPrimitive("test");
    assertFalse(p1.equals(p2));
    assertFalse(p2.equals(p1));
  }

  @Test
    @Timeout(8000)
  void testEquals_integralSameLongValue() throws Exception {
    JsonPrimitive p1 = new JsonPrimitive(42);
    JsonPrimitive p2 = new JsonPrimitive(42L);
    assertTrue(p1.equals(p2));
    assertTrue(p2.equals(p1));
  }

  @Test
    @Timeout(8000)
  void testEquals_integralDifferentLongValue() throws Exception {
    JsonPrimitive p1 = new JsonPrimitive(42);
    JsonPrimitive p2 = new JsonPrimitive(43L);
    assertFalse(p1.equals(p2));
    assertFalse(p2.equals(p1));
  }

  @Test
    @Timeout(8000)
  void testEquals_numberDoubleNaN() {
    JsonPrimitive p1 = new JsonPrimitive(Double.NaN);
    JsonPrimitive p2 = new JsonPrimitive(Double.NaN);
    assertTrue(p1.equals(p2));
  }

  @Test
    @Timeout(8000)
  void testEquals_numberDoubleEqual() {
    JsonPrimitive p1 = new JsonPrimitive(0.1 + 0.2);
    JsonPrimitive p2 = new JsonPrimitive(0.3);
    assertFalse(p1.equals(p2)); // Because 0.1+0.2 != 0.3 exactly
  }

  @Test
    @Timeout(8000)
  void testEquals_numberDoubleDifferent() {
    JsonPrimitive p1 = new JsonPrimitive(1.0);
    JsonPrimitive p2 = new JsonPrimitive(2.0);
    assertFalse(p1.equals(p2));
  }

  @Test
    @Timeout(8000)
  void testEquals_otherValueEquals() throws Exception {
    JsonPrimitive p1 = new JsonPrimitive("stringValue");
    JsonPrimitive p2 = new JsonPrimitive("stringValue");
    assertTrue(p1.equals(p2));
    JsonPrimitive p3 = new JsonPrimitive("otherValue");
    assertFalse(p1.equals(p3));
  }

  @Test
    @Timeout(8000)
  void testIsIntegral_method() throws Exception {
    Method isIntegral = JsonPrimitive.class.getDeclaredMethod("isIntegral", JsonPrimitive.class);
    isIntegral.setAccessible(true);

    JsonPrimitive integralInt = new JsonPrimitive(10);
    JsonPrimitive integralLong = new JsonPrimitive(10L);
    JsonPrimitive integralShort = new JsonPrimitive((short)10);
    JsonPrimitive integralByte = new JsonPrimitive((byte)10);
    JsonPrimitive nonIntegralDouble = new JsonPrimitive(10.5);
    JsonPrimitive nonIntegralString = new JsonPrimitive("10");

    assertTrue((Boolean)isIntegral.invoke(null, integralInt));
    assertTrue((Boolean)isIntegral.invoke(null, integralLong));
    assertTrue((Boolean)isIntegral.invoke(null, integralShort));
    assertTrue((Boolean)isIntegral.invoke(null, integralByte));
    assertFalse((Boolean)isIntegral.invoke(null, nonIntegralDouble));
    assertFalse((Boolean)isIntegral.invoke(null, nonIntegralString));
  }

  // Helper method to create instance with private final value field set to given object
  private Object createInstanceWithValue(Object value) throws Exception {
    JsonPrimitive instance = (JsonPrimitive) JsonPrimitive.class.getDeclaredConstructor(String.class).newInstance("dummy");
    java.lang.reflect.Field f = JsonPrimitive.class.getDeclaredField("value");
    f.setAccessible(true);
    f.set(instance, value);
    return instance;
  }
}