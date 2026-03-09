package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonPrimitive_535_2Test {

  private JsonPrimitive jsonPrimitive;

  @BeforeEach
  public void setUp() {
    // no setup needed here, will create instances per test
  }

  @Test
    @Timeout(8000)
  public void testGetAsBigDecimal_whenValueIsBigDecimal() throws Exception {
    BigDecimal bdValue = new BigDecimal("123.456");
    jsonPrimitive = createJsonPrimitiveWithValue(bdValue);

    BigDecimal result = jsonPrimitive.getAsBigDecimal();

    assertSame(bdValue, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsBigDecimal_whenValueIsStringRepresentingNumber() throws Exception {
    String strValue = "789.123";
    jsonPrimitive = createJsonPrimitiveWithValue(strValue);

    BigDecimal result = jsonPrimitive.getAsBigDecimal();

    assertEquals(new BigDecimal(strValue), result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsBigDecimal_whenValueIsIntegerNumber() throws Exception {
    Integer intValue = 42;
    jsonPrimitive = createJsonPrimitiveWithValue(intValue);

    BigDecimal result = jsonPrimitive.getAsBigDecimal();

    assertEquals(new BigDecimal(intValue.toString()), result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsBigDecimal_whenValueIsLazilyParsedNumber() throws Exception {
    // LazilyParsedNumber is internal, create via constructor with string
    Object lazilyParsedNumber = new com.google.gson.internal.LazilyParsedNumber("12345.6789");
    jsonPrimitive = createJsonPrimitiveWithValue(lazilyParsedNumber);

    BigDecimal result = jsonPrimitive.getAsBigDecimal();

    assertEquals(new BigDecimal("12345.6789"), result);
  }

  @Test
    @Timeout(8000)
  public void testPrivateIsIntegralMethod_trueCases() throws Exception {
    Method isIntegralMethod = JsonPrimitive.class.getDeclaredMethod("isIntegral", JsonPrimitive.class);
    isIntegralMethod.setAccessible(true);

    JsonPrimitive intPrimitive = createJsonPrimitiveWithValue(10);
    JsonPrimitive longPrimitive = createJsonPrimitiveWithValue(10L);
    JsonPrimitive bigIntegerPrimitive = createJsonPrimitiveWithValue(new java.math.BigInteger("123456789"));

    assertTrue((Boolean) isIntegralMethod.invoke(null, intPrimitive));
    assertTrue((Boolean) isIntegralMethod.invoke(null, longPrimitive));
    assertTrue((Boolean) isIntegralMethod.invoke(null, bigIntegerPrimitive));
  }

  @Test
    @Timeout(8000)
  public void testPrivateIsIntegralMethod_falseCases() throws Exception {
    Method isIntegralMethod = JsonPrimitive.class.getDeclaredMethod("isIntegral", JsonPrimitive.class);
    isIntegralMethod.setAccessible(true);

    JsonPrimitive doublePrimitive = createJsonPrimitiveWithValue(10.5);
    JsonPrimitive floatPrimitive = createJsonPrimitiveWithValue(10.5f);
    JsonPrimitive bigDecimalPrimitive = createJsonPrimitiveWithValue(new BigDecimal("10.5"));
    JsonPrimitive stringPrimitive = createJsonPrimitiveWithValue("123");

    assertFalse((Boolean) isIntegralMethod.invoke(null, doublePrimitive));
    assertFalse((Boolean) isIntegralMethod.invoke(null, floatPrimitive));
    assertFalse((Boolean) isIntegralMethod.invoke(null, bigDecimalPrimitive));
    assertFalse((Boolean) isIntegralMethod.invoke(null, stringPrimitive));
  }

  private JsonPrimitive createJsonPrimitiveWithValue(Object value) throws Exception {
    // Use default constructor via reflection and set private final field 'value'
    // The class does not expose a no-arg constructor, so we create via one of the public constructors
    JsonPrimitive primitive;
    if (value instanceof Boolean) {
      primitive = new JsonPrimitive((Boolean) value);
    } else if (value instanceof Number) {
      primitive = new JsonPrimitive((Number) value);
    } else if (value instanceof Character) {
      primitive = new JsonPrimitive((Character) value);
    } else if (value instanceof String) {
      primitive = new JsonPrimitive((String) value);
    } else {
      // fallback: create with string and then override value field forcibly
      primitive = new JsonPrimitive(value.toString());
      setValueField(primitive, value);
      return primitive;
    }
    // forcibly override value field to exact object (for LazilyParsedNumber etc)
    setValueField(primitive, value);
    return primitive;
  }

  private void setValueField(JsonPrimitive primitive, Object value) throws Exception {
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(primitive, value);
  }
}