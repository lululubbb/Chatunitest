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

public class JsonPrimitive_535_4Test {

  private JsonPrimitive jsonPrimitive;

  @BeforeEach
  public void setUp() {
    // no-op, instances created per test
  }

  @Test
    @Timeout(8000)
  public void testGetAsBigDecimal_withBigDecimalValue() throws Exception {
    BigDecimal bd = new BigDecimal("123.456");
    jsonPrimitive = new JsonPrimitive(bd);

    BigDecimal result = jsonPrimitive.getAsBigDecimal();

    assertSame(bd, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsBigDecimal_withStringValue() throws Exception {
    String numericString = "789.0123";
    jsonPrimitive = new JsonPrimitive(numericString);

    BigDecimal result = jsonPrimitive.getAsBigDecimal();

    assertEquals(new BigDecimal(numericString), result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsBigDecimal_withNumberValue() throws Exception {
    Number number = 12345.6789d;
    jsonPrimitive = new JsonPrimitive(number);

    BigDecimal result = jsonPrimitive.getAsBigDecimal();

    assertEquals(new BigDecimal(number.toString()), result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsBigDecimal_withLazilyParsedNumberValue() throws Exception {
    // LazilyParsedNumber is package-private, so create via reflection or string
    // Using string constructor to simulate LazilyParsedNumber behavior:
    jsonPrimitive = new JsonPrimitive("123456789.987654321");

    BigDecimal result = jsonPrimitive.getAsBigDecimal();

    assertEquals(new BigDecimal("123456789.987654321"), result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsBigDecimal_withNonNumericString_throwsNumberFormatException() throws Exception {
    String nonNumeric = "not a number";
    jsonPrimitive = new JsonPrimitive(nonNumeric);

    assertThrows(NumberFormatException.class, () -> {
      jsonPrimitive.getAsBigDecimal();
    });
  }

  @Test
    @Timeout(8000)
  public void testGetAsBigDecimal_withNullValue_throwsNullPointerException() throws Exception {
    // Create instance with reflection to set private final field value to null
    jsonPrimitive = new JsonPrimitive("1"); // initial valid value
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(jsonPrimitive, null);

    assertThrows(NullPointerException.class, () -> {
      jsonPrimitive.getAsBigDecimal();
    });
  }
}