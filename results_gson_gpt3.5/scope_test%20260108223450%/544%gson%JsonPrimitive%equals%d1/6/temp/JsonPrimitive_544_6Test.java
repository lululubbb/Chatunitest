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

public class JsonPrimitive_544_6Test {

  private JsonPrimitive jsonPrimitive;
  private JsonPrimitive otherPrimitive;

  @BeforeEach
  public void setUp() {
    jsonPrimitive = new JsonPrimitive("test");
    otherPrimitive = new JsonPrimitive("test");
  }

  @Test
    @Timeout(8000)
  public void testEquals_SameObject_ReturnsTrue() {
    assertTrue(jsonPrimitive.equals(jsonPrimitive));
  }

  @Test
    @Timeout(8000)
  public void testEquals_NullObject_ReturnsFalse() {
    assertFalse(jsonPrimitive.equals(null));
  }

  @Test
    @Timeout(8000)
  public void testEquals_DifferentClass_ReturnsFalse() {
    assertFalse(jsonPrimitive.equals("some string"));
  }

  @Test
    @Timeout(8000)
  public void testEquals_BothValuesNull_ReturnsTrue() throws Exception {
    JsonPrimitive jp1 = new JsonPrimitive("dummy");
    JsonPrimitive jp2 = new JsonPrimitive("dummy");

    setValue(jp1, null);
    setValue(jp2, null);

    assertTrue(jp1.equals(jp2));
  }

  @Test
    @Timeout(8000)
  public void testEquals_OneValueNull_ReturnsFalse() throws Exception {
    JsonPrimitive jp1 = new JsonPrimitive("dummy");
    JsonPrimitive jp2 = new JsonPrimitive("dummy");

    setValue(jp1, null);
    setValue(jp2, "not null");

    assertFalse(jp1.equals(jp2));
    assertFalse(jp2.equals(jp1));
  }

  @Test
    @Timeout(8000)
  public void testEquals_BothIntegralValues_EqualLongValues_ReturnsTrue() throws Exception {
    JsonPrimitive jp1 = new JsonPrimitive(123L);
    JsonPrimitive jp2 = new JsonPrimitive(123);

    assertTrue(jp1.equals(jp2));
  }

  @Test
    @Timeout(8000)
  public void testEquals_BothIntegralValues_DifferentLongValues_ReturnsFalse() throws Exception {
    JsonPrimitive jp1 = new JsonPrimitive(123L);
    JsonPrimitive jp2 = new JsonPrimitive(456);

    assertFalse(jp1.equals(jp2));
  }

  @Test
    @Timeout(8000)
  public void testEquals_BothNumberValues_DoubleNaN_ReturnsTrue() {
    JsonPrimitive jp1 = new JsonPrimitive(Double.NaN);
    JsonPrimitive jp2 = new JsonPrimitive(Double.NaN);

    assertTrue(jp1.equals(jp2));
  }

  @Test
    @Timeout(8000)
  public void testEquals_BothNumberValues_DoubleEqual_ReturnsTrue() {
    JsonPrimitive jp1 = new JsonPrimitive(1.23);
    JsonPrimitive jp2 = new JsonPrimitive(1.23);

    assertTrue(jp1.equals(jp2));
  }

  @Test
    @Timeout(8000)
  public void testEquals_BothNumberValues_DoubleNotEqual_ReturnsFalse() {
    JsonPrimitive jp1 = new JsonPrimitive(1.23);
    JsonPrimitive jp2 = new JsonPrimitive(4.56);

    assertFalse(jp1.equals(jp2));
  }

  @Test
    @Timeout(8000)
  public void testEquals_BothValuesNonNumberAndNonNull_EqualsValue() {
    JsonPrimitive jp1 = new JsonPrimitive("abc");
    JsonPrimitive jp2 = new JsonPrimitive("abc");
    JsonPrimitive jp3 = new JsonPrimitive("def");

    assertTrue(jp1.equals(jp2));
    assertFalse(jp1.equals(jp3));
  }

  @Test
    @Timeout(8000)
  public void testIsIntegral_StaticMethod() throws Exception {
    Method isIntegral = JsonPrimitive.class.getDeclaredMethod("isIntegral", JsonPrimitive.class);
    isIntegral.setAccessible(true);

    JsonPrimitive integral1 = new JsonPrimitive(123);
    JsonPrimitive integral2 = new JsonPrimitive(123L);
    JsonPrimitive integral3 = new JsonPrimitive(new BigInteger("123"));
    JsonPrimitive nonIntegral = new JsonPrimitive(1.23);

    assertTrue((Boolean) isIntegral.invoke(null, integral1));
    assertTrue((Boolean) isIntegral.invoke(null, integral2));
    assertTrue((Boolean) isIntegral.invoke(null, integral3));
    assertFalse((Boolean) isIntegral.invoke(null, nonIntegral));
  }

  private void setValue(JsonPrimitive jp, Object value) throws Exception {
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(jp, value);
  }
}