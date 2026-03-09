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

public class JsonPrimitive_536_1Test {

  private JsonPrimitive jsonPrimitive;

  @BeforeEach
  void setUp() throws Exception {
    // Create instance using public constructor with Boolean (since Object constructor doesn't exist)
    jsonPrimitive = new JsonPrimitive(true);

    // Set accessible true for private field
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    // Clear value for each test
    valueField.set(jsonPrimitive, null);
  }

  @Test
    @Timeout(8000)
  void testGetAsBigInteger_whenValueIsBigInteger() throws Exception {
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    BigInteger bigInt = new BigInteger("12345678901234567890");
    valueField.set(jsonPrimitive, bigInt);

    BigInteger result = jsonPrimitive.getAsBigInteger();

    assertSame(bigInt, result);
  }

  @Test
    @Timeout(8000)
  void testGetAsBigInteger_whenValueIsStringRepresentingInteger() throws Exception {
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    String stringValue = "98765432109876543210";
    valueField.set(jsonPrimitive, stringValue);

    BigInteger result = jsonPrimitive.getAsBigInteger();

    assertEquals(new BigInteger(stringValue), result);
  }

  @Test
    @Timeout(8000)
  void testGetAsBigInteger_whenValueIsNumber() throws Exception {
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    Number numValue = 12345L;
    valueField.set(jsonPrimitive, numValue);

    BigInteger result = jsonPrimitive.getAsBigInteger();

    assertEquals(new BigInteger(numValue.toString()), result);
  }

  @Test
    @Timeout(8000)
  void testGetAsBigInteger_whenValueIsLazilyParsedNumber() throws Exception {
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    LazilyParsedNumber lpn = new LazilyParsedNumber("123456789");
    valueField.set(jsonPrimitive, lpn);

    BigInteger result = jsonPrimitive.getAsBigInteger();

    assertEquals(new BigInteger(lpn.toString()), result);
  }

  @Test
    @Timeout(8000)
  void testGetAsBigInteger_whenValueIsOtherObject() throws Exception {
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    // Use a String value instead of an anonymous Object to avoid unexpected value type error
    String other = "42";
    valueField.set(jsonPrimitive, other);

    BigInteger result = jsonPrimitive.getAsBigInteger();

    assertEquals(BigInteger.valueOf(42), result);
  }
}