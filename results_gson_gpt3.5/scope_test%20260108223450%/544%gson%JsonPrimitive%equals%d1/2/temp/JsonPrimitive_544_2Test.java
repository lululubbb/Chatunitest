package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;

public class JsonPrimitive_544_2Test {

  @Test
    @Timeout(8000)
  public void testEquals_sameInstance() {
    JsonPrimitive jp = new JsonPrimitive("test");
    assertTrue(jp.equals(jp));
  }

  @Test
    @Timeout(8000)
  public void testEquals_nullObject() {
    JsonPrimitive jp = new JsonPrimitive("test");
    assertFalse(jp.equals(null));
  }

  @Test
    @Timeout(8000)
  public void testEquals_differentClass() {
    JsonPrimitive jp = new JsonPrimitive("test");
    assertFalse(jp.equals("test"));
  }

  @Test
    @Timeout(8000)
  public void testEquals_bothValuesNull() throws Exception {
    JsonPrimitive jp1 = new JsonPrimitive("dummy");
    JsonPrimitive jp2 = new JsonPrimitive("dummy");
    // Use reflection to set private final field 'value' to null
    setValue(jp1, null);
    setValue(jp2, null);
    assertTrue(jp1.equals(jp2));
  }

  @Test
    @Timeout(8000)
  public void testEquals_oneValueNull() throws Exception {
    JsonPrimitive jp1 = new JsonPrimitive("dummy");
    JsonPrimitive jp2 = new JsonPrimitive("dummy");
    setValue(jp1, null);
    setValue(jp2, "something");
    assertFalse(jp1.equals(jp2));
  }

  @Test
    @Timeout(8000)
  public void testEquals_bothIntegralNumbers_equal() {
    JsonPrimitive jp1 = new JsonPrimitive(123);
    JsonPrimitive jp2 = new JsonPrimitive(123L);
    assertTrue(jp1.equals(jp2));
  }

  @Test
    @Timeout(8000)
  public void testEquals_bothIntegralNumbers_notEqual() {
    JsonPrimitive jp1 = new JsonPrimitive(123);
    JsonPrimitive jp2 = new JsonPrimitive(456L);
    assertFalse(jp1.equals(jp2));
  }

  @Test
    @Timeout(8000)
  public void testEquals_bothNumbers_doubleNaN() {
    JsonPrimitive jp1 = new JsonPrimitive(Double.NaN);
    JsonPrimitive jp2 = new JsonPrimitive(Double.NaN);
    assertTrue(jp1.equals(jp2));
  }

  @Test
    @Timeout(8000)
  public void testEquals_bothNumbers_doubleEqual() {
    JsonPrimitive jp1 = new JsonPrimitive(1.23);
    JsonPrimitive jp2 = new JsonPrimitive(1.23);
    assertTrue(jp1.equals(jp2));
  }

  @Test
    @Timeout(8000)
  public void testEquals_bothNumbers_doubleNotEqual() {
    JsonPrimitive jp1 = new JsonPrimitive(1.23);
    JsonPrimitive jp2 = new JsonPrimitive(4.56);
    assertFalse(jp1.equals(jp2));
  }

  @Test
    @Timeout(8000)
  public void testEquals_otherValueNotNumberOrIntegral() {
    JsonPrimitive jp1 = new JsonPrimitive("string1");
    JsonPrimitive jp2 = new JsonPrimitive("string1");
    assertTrue(jp1.equals(jp2));
  }

  @Test
    @Timeout(8000)
  public void testEquals_otherValueNotNumberOrIntegral_notEqual() {
    JsonPrimitive jp1 = new JsonPrimitive("string1");
    JsonPrimitive jp2 = new JsonPrimitive("string2");
    assertFalse(jp1.equals(jp2));
  }

  @Test
    @Timeout(8000)
  public void testIsIntegral_true() throws Exception {
    JsonPrimitive jp = new JsonPrimitive(10);
    Method isIntegralMethod = JsonPrimitive.class.getDeclaredMethod("isIntegral", JsonPrimitive.class);
    isIntegralMethod.setAccessible(true);
    boolean result = (boolean) isIntegralMethod.invoke(null, jp);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  public void testIsIntegral_false() throws Exception {
    JsonPrimitive jp = new JsonPrimitive(10.5);
    Method isIntegralMethod = JsonPrimitive.class.getDeclaredMethod("isIntegral", JsonPrimitive.class);
    isIntegralMethod.setAccessible(true);
    boolean result = (boolean) isIntegralMethod.invoke(null, jp);
    assertFalse(result);
  }

  private void setValue(JsonPrimitive jp, Object value) throws Exception {
    java.lang.reflect.Field field = JsonPrimitive.class.getDeclaredField("value");
    field.setAccessible(true);
    field.set(jp, value);
  }
}