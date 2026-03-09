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

public class JsonPrimitive_544_1Test {

  private JsonPrimitive jsonPrimitive;

  @BeforeEach
  void setUp() throws Exception {
    // Create a JsonPrimitive with a default value using reflection since constructors are not shown
    jsonPrimitive = (JsonPrimitive) JsonPrimitive.class.getDeclaredConstructor(Boolean.class).newInstance(Boolean.TRUE);
  }

  private void setValue(JsonPrimitive instance, Object val) throws Exception {
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(instance, val);
  }

  private boolean callIsIntegral(JsonPrimitive instance) throws Exception {
    Method isIntegral = JsonPrimitive.class.getDeclaredMethod("isIntegral", JsonPrimitive.class);
    isIntegral.setAccessible(true);
    return (boolean) isIntegral.invoke(null, instance);
  }

  @Test
    @Timeout(8000)
  void testEquals_SameInstance() {
    assertTrue(jsonPrimitive.equals(jsonPrimitive));
  }

  @Test
    @Timeout(8000)
  void testEquals_NullObject() {
    assertFalse(jsonPrimitive.equals(null));
  }

  @Test
    @Timeout(8000)
  void testEquals_DifferentClass() {
    assertFalse(jsonPrimitive.equals("string"));
  }

  @Test
    @Timeout(8000)
  void testEquals_BothNullValue() throws Exception {
    JsonPrimitive other = (JsonPrimitive) JsonPrimitive.class.getDeclaredConstructor(Boolean.class).newInstance(Boolean.TRUE);
    setValue(jsonPrimitive, null);
    setValue(other, null);
    assertTrue(jsonPrimitive.equals(other));
  }

  @Test
    @Timeout(8000)
  void testEquals_ThisNullValueOtherNotNull() throws Exception {
    JsonPrimitive other = (JsonPrimitive) JsonPrimitive.class.getDeclaredConstructor(Boolean.class).newInstance(Boolean.TRUE);
    setValue(jsonPrimitive, null);
    setValue(other, "non-null");
    assertFalse(jsonPrimitive.equals(other));
  }

  @Test
    @Timeout(8000)
  void testEquals_BothIntegralNumbersEqual() {
    // Use real instances with integral values
    JsonPrimitive intPrimitive1 = new JsonPrimitive(10);
    JsonPrimitive intPrimitive2 = new JsonPrimitive(10L);
    assertTrue(intPrimitive1.equals(intPrimitive2));
  }

  @Test
    @Timeout(8000)
  void testEquals_BothIntegralNumbersNotEqual() {
    JsonPrimitive intPrimitive1 = new JsonPrimitive(10);
    JsonPrimitive intPrimitive2 = new JsonPrimitive(11L);
    assertFalse(intPrimitive1.equals(intPrimitive2));
  }

  @Test
    @Timeout(8000)
  void testEquals_BothNumbersDoubleEqual() {
    JsonPrimitive doublePrimitive1 = new JsonPrimitive(10.0);
    JsonPrimitive doublePrimitive2 = new JsonPrimitive(10.0);
    assertTrue(doublePrimitive1.equals(doublePrimitive2));
  }

  @Test
    @Timeout(8000)
  void testEquals_BothNumbersDoubleNaN() {
    JsonPrimitive doublePrimitive1 = new JsonPrimitive(Double.NaN);
    JsonPrimitive doublePrimitive2 = new JsonPrimitive(Double.NaN);
    assertTrue(doublePrimitive1.equals(doublePrimitive2));
  }

  @Test
    @Timeout(8000)
  void testEquals_BothNumbersDoubleNotEqual() {
    JsonPrimitive doublePrimitive1 = new JsonPrimitive(10.0);
    JsonPrimitive doublePrimitive2 = new JsonPrimitive(11.0);
    assertFalse(doublePrimitive1.equals(doublePrimitive2));
  }

  @Test
    @Timeout(8000)
  void testEquals_ValueEqualsFallback() {
    JsonPrimitive stringPrimitive1 = new JsonPrimitive("test");
    JsonPrimitive stringPrimitive2 = new JsonPrimitive("test");
    assertTrue(stringPrimitive1.equals(stringPrimitive2));

    JsonPrimitive stringPrimitive3 = new JsonPrimitive("test");
    JsonPrimitive stringPrimitive4 = new JsonPrimitive("other");
    assertFalse(stringPrimitive3.equals(stringPrimitive4));
  }

  @Test
    @Timeout(8000)
  void testIsIntegral_PrivateMethod() throws Exception {
    JsonPrimitive intPrimitive = new JsonPrimitive(10);
    JsonPrimitive longPrimitive = new JsonPrimitive(10L);
    JsonPrimitive doublePrimitive = new JsonPrimitive(10.0);
    JsonPrimitive bigIntegerPrimitive = new JsonPrimitive(BigInteger.TEN);
    JsonPrimitive bigDecimalPrimitive = new JsonPrimitive(BigDecimal.TEN);
    JsonPrimitive stringPrimitive = new JsonPrimitive("string");

    Method isIntegral = JsonPrimitive.class.getDeclaredMethod("isIntegral", JsonPrimitive.class);
    isIntegral.setAccessible(true);

    assertTrue((boolean) isIntegral.invoke(null, intPrimitive));
    assertTrue((boolean) isIntegral.invoke(null, longPrimitive));
    assertFalse((boolean) isIntegral.invoke(null, doublePrimitive));
    assertTrue((boolean) isIntegral.invoke(null, bigIntegerPrimitive));
    assertFalse((boolean) isIntegral.invoke(null, bigDecimalPrimitive));
    assertFalse((boolean) isIntegral.invoke(null, stringPrimitive));
  }
}