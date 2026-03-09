package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonPrimitive_535_5Test {

  private JsonPrimitive jsonPrimitive;

  @BeforeEach
  public void setUp() {
    // no-op, instances created in each test
  }

  @Test
    @Timeout(8000)
  public void testGetAsBigDecimal_withBigDecimalValue() throws Exception {
    BigDecimal bigDecimalValue = new BigDecimal("123.456");
    jsonPrimitive = new JsonPrimitive(bigDecimalValue);

    BigDecimal result = jsonPrimitive.getAsBigDecimal();

    assertSame(bigDecimalValue, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsBigDecimal_withStringValue() throws Exception {
    String stringValue = "789.0123";
    jsonPrimitive = new JsonPrimitive(stringValue);

    BigDecimal expected = new BigDecimal(stringValue);
    BigDecimal result = jsonPrimitive.getAsBigDecimal();

    assertEquals(expected, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsBigDecimal_withNumberValue() throws Exception {
    Number numberValue = 42;
    jsonPrimitive = new JsonPrimitive(numberValue);

    BigDecimal expected = new BigDecimal(jsonPrimitive.getAsString());
    BigDecimal result = jsonPrimitive.getAsBigDecimal();

    assertEquals(expected, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsBigDecimal_withLazilyParsedNumberValue() throws Exception {
    // LazilyParsedNumber is package-private, so create via constructor with String
    Number lazilyParsedNumber = new LazilyParsedNumber("12345.6789");
    jsonPrimitive = new JsonPrimitive(lazilyParsedNumber);

    BigDecimal expected = new BigDecimal(jsonPrimitive.getAsString());
    BigDecimal result = jsonPrimitive.getAsBigDecimal();

    assertEquals(expected, result);
  }

  // Helper constructor to set private final Object value via reflection for testing
  private JsonPrimitive createJsonPrimitiveWithValue(Object value) throws Exception {
    JsonPrimitive jp = (JsonPrimitive) JsonPrimitive.class.getDeclaredConstructor().newInstance();
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(jp, value);
    return jp;
  }
}