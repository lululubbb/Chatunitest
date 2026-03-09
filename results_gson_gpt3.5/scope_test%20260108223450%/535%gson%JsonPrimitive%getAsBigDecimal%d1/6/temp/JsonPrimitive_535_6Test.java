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

public class JsonPrimitive_535_6Test {

  private JsonPrimitive jsonPrimitive;

  @BeforeEach
  public void setUp() {
    // No default constructor exposed, will initialize in each test separately
  }

  @Test
    @Timeout(8000)
  public void testGetAsBigDecimal_withBigDecimalValue() throws Exception {
    BigDecimal bd = new BigDecimal("123.45");
    jsonPrimitive = createJsonPrimitiveWithValue(bd);

    BigDecimal result = jsonPrimitive.getAsBigDecimal();

    assertSame(bd, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsBigDecimal_withStringValue() throws Exception {
    String strValue = "6789.01";
    jsonPrimitive = createJsonPrimitiveWithValue(strValue);

    BigDecimal result = jsonPrimitive.getAsBigDecimal();

    assertEquals(new BigDecimal(strValue), result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsBigDecimal_withLazilyParsedNumberValue() throws Exception {
    // LazilyParsedNumber extends Number, getAsString returns string representation
    String numberStr = "42.42";
    Object lazilyParsedNumber = new LazilyParsedNumber(numberStr);
    jsonPrimitive = createJsonPrimitiveWithValue(lazilyParsedNumber);

    BigDecimal result = jsonPrimitive.getAsBigDecimal();

    assertEquals(new BigDecimal(numberStr), result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsBigDecimal_withIntegerValue() throws Exception {
    Integer intValue = 100;
    jsonPrimitive = createJsonPrimitiveWithValue(intValue);

    BigDecimal result = jsonPrimitive.getAsBigDecimal();

    assertEquals(new BigDecimal(intValue.toString()), result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsBigDecimal_withDoubleValue() throws Exception {
    Double doubleValue = 1234.5678;
    jsonPrimitive = createJsonPrimitiveWithValue(doubleValue);

    BigDecimal result = jsonPrimitive.getAsBigDecimal();

    assertEquals(new BigDecimal(doubleValue.toString()), result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsBigDecimal_withInvalidString_throwsNumberFormatException() throws Exception {
    String invalidNumber = "notANumber";
    jsonPrimitive = createJsonPrimitiveWithValue(invalidNumber);

    assertThrows(NumberFormatException.class, () -> jsonPrimitive.getAsBigDecimal());
  }

  // Helper method to create JsonPrimitive instance and set private final field 'value' via reflection
  private JsonPrimitive createJsonPrimitiveWithValue(Object value) throws Exception {
    // Use one of the public constructors (e.g. with String) to create instance
    JsonPrimitive instance;
    if (value instanceof Boolean) {
      instance = new JsonPrimitive((Boolean) value);
    } else if (value instanceof Number) {
      instance = new JsonPrimitive((Number) value);
    } else if (value instanceof Character) {
      instance = new JsonPrimitive((Character) value);
    } else if (value instanceof String) {
      instance = new JsonPrimitive((String) value);
    } else {
      // fallback: create with String "0"
      instance = new JsonPrimitive("0");
    }

    // Set private final field 'value' to desired test value
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);

    // Remove final modifier if needed (optional, usually works with setAccessible)
    // Set the field to the provided value
    valueField.set(instance, value);

    return instance;
  }
}