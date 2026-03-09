package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.math.BigInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonPrimitive_536_5Test {

  private JsonPrimitive jsonPrimitive;

  @BeforeEach
  void setUp() throws Exception {
    // Instantiate JsonPrimitive using a public constructor (e.g. String)
    jsonPrimitive = new JsonPrimitive("0");
  }

  private void setValue(Object value) throws Exception {
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(jsonPrimitive, value);
  }

  @Test
    @Timeout(8000)
  void testGetAsBigInteger_whenValueIsBigInteger_returnsSameInstance() throws Exception {
    BigInteger bigInteger = new BigInteger("12345678901234567890");
    setValue(bigInteger);
    BigInteger result = jsonPrimitive.getAsBigInteger();
    assertSame(bigInteger, result);
  }

  @Test
    @Timeout(8000)
  void testGetAsBigInteger_whenValueIsStringParsableToBigInteger_returnsParsed() throws Exception {
    String numberStr = "98765432109876543210";
    setValue(numberStr);
    BigInteger result = jsonPrimitive.getAsBigInteger();
    assertEquals(new BigInteger(numberStr), result);
  }

  @Test
    @Timeout(8000)
  void testGetAsBigInteger_whenValueIsLazilyParsedNumber_returnsParsed() throws Exception {
    Object lazilyParsedNumber = new com.google.gson.internal.LazilyParsedNumber("123456789");
    setValue(lazilyParsedNumber);
    BigInteger result = jsonPrimitive.getAsBigInteger();
    assertEquals(new BigInteger("123456789"), result);
  }

  @Test
    @Timeout(8000)
  void testGetAsBigInteger_whenValueIsNumberButNotBigInteger_returnsParsedFromString() throws Exception {
    Number number = 12345L;
    setValue(number);
    BigInteger expected = new BigInteger(jsonPrimitive.getAsString());
    BigInteger result = jsonPrimitive.getAsBigInteger();
    assertEquals(expected, result);
  }
}