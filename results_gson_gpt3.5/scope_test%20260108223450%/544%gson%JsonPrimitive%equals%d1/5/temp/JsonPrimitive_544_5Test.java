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

class JsonPrimitiveEqualsTest {

  private JsonPrimitive jsonPrimitive;

  @BeforeEach
  public void setUp() throws Exception {
    jsonPrimitive = new JsonPrimitive("initial");
  }

  private void setValue(JsonPrimitive jp, Object value) throws Exception {
    Field field = JsonPrimitive.class.getDeclaredField("value");
    field.setAccessible(true);
    field.set(jp, value);
  }

  private boolean invokeIsIntegral(JsonPrimitive jp) throws Exception {
    Method method = JsonPrimitive.class.getDeclaredMethod("isIntegral", JsonPrimitive.class);
    method.setAccessible(true);
    return (boolean) method.invoke(null, jp);
  }

  @Test
    @Timeout(8000)
  public void testEquals_sameInstance() {
    assertTrue(jsonPrimitive.equals(jsonPrimitive));
  }

  @Test
    @Timeout(8000)
  public void testEquals_nullObject() {
    assertFalse(jsonPrimitive.equals(null));
  }

  @Test
    @Timeout(8000)
  public void testEquals_differentClass() {
    assertFalse(jsonPrimitive.equals("string"));
  }

  @Test
    @Timeout(8000)
  public void testEquals_bothValuesNull() throws Exception {
    JsonPrimitive jp1 = new JsonPrimitive("x");
    JsonPrimitive jp2 = new JsonPrimitive("y");
    setValue(jp1, null);
    setValue(jp2, null);
    assertTrue(jp1.equals(jp2));
  }

  @Test
    @Timeout(8000)
  public void testEquals_oneValueNull() throws Exception {
    JsonPrimitive jp1 = new JsonPrimitive("x");
    JsonPrimitive jp2 = new JsonPrimitive("y");
    setValue(jp1, null);
    setValue(jp2, "not null");
    assertFalse(jp1.equals(jp2));
  }

  @Test
    @Timeout(8000)
  public void testEquals_bothIntegralEqualLong() throws Exception {
    JsonPrimitive jp1 = new JsonPrimitive(42);
    JsonPrimitive jp2 = new JsonPrimitive(42L);
    assertTrue(jp1.equals(jp2));
  }

  @Test
    @Timeout(8000)
  public void testEquals_bothIntegralNotEqualLong() throws Exception {
    JsonPrimitive jp1 = new JsonPrimitive(42);
    JsonPrimitive jp2 = new JsonPrimitive(43L);
    assertFalse(jp1.equals(jp2));
  }

  @Test
    @Timeout(8000)
  public void testEquals_bothNumberDoubleEqual() throws Exception {
    JsonPrimitive jp1 = new JsonPrimitive(42.0);
    JsonPrimitive jp2 = new JsonPrimitive(42.0);
    assertTrue(jp1.equals(jp2));
  }

  @Test
    @Timeout(8000)
  public void testEquals_bothNumberDoubleNotEqual() throws Exception {
    JsonPrimitive jp1 = new JsonPrimitive(42.0);
    JsonPrimitive jp2 = new JsonPrimitive(43.0);
    assertFalse(jp1.equals(jp2));
  }

  @Test
    @Timeout(8000)
  public void testEquals_bothNumberDoubleNaN() throws Exception {
    JsonPrimitive jp1 = new JsonPrimitive(Double.NaN);
    JsonPrimitive jp2 = new JsonPrimitive(Double.NaN);
    assertTrue(jp1.equals(jp2));
  }

  @Test
    @Timeout(8000)
  public void testEquals_valueEquals() throws Exception {
    JsonPrimitive jp1 = new JsonPrimitive("test");
    JsonPrimitive jp2 = new JsonPrimitive("test");
    assertTrue(jp1.equals(jp2));
  }

  @Test
    @Timeout(8000)
  public void testEquals_valueNotEquals() throws Exception {
    JsonPrimitive jp1 = new JsonPrimitive("test1");
    JsonPrimitive jp2 = new JsonPrimitive("test2");
    assertFalse(jp1.equals(jp2));
  }

  @Test
    @Timeout(8000)
  public void testIsIntegral_reflection() throws Exception {
    JsonPrimitive jpInt = new JsonPrimitive(1);
    JsonPrimitive jpLong = new JsonPrimitive(1L);
    JsonPrimitive jpBigInt = new JsonPrimitive(BigInteger.ONE);
    JsonPrimitive jpDouble = new JsonPrimitive(1.0);
    JsonPrimitive jpString = new JsonPrimitive("1");

    assertTrue(invokeIsIntegral(jpInt));
    assertTrue(invokeIsIntegral(jpLong));
    assertTrue(invokeIsIntegral(jpBigInt));
    assertFalse(invokeIsIntegral(jpDouble));
    assertFalse(invokeIsIntegral(jpString));
  }
}