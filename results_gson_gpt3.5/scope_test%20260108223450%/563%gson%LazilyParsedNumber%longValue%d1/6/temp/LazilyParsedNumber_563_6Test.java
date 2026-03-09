package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

public class LazilyParsedNumber_563_6Test {

  @Test
    @Timeout(8000)
  public void testLongValue_parseLongSuccess() throws Exception {
    // value that can be parsed by Long.parseLong without exception
    LazilyParsedNumber number = new LazilyParsedNumber("123456789");
    long result = number.longValue();
    assertEquals(123456789L, result);
  }

  @Test
    @Timeout(8000)
  public void testLongValue_parseLongFails_bigDecimalUsed() throws Exception {
    // value that causes Long.parseLong to throw NumberFormatException but BigDecimal can parse
    // e.g. a number too big for long but valid decimal number
    String bigValue = "92233720368547758079223372036854775807"; // bigger than Long.MAX_VALUE
    LazilyParsedNumber number = new LazilyParsedNumber(bigValue);
    long expected = new java.math.BigDecimal(bigValue).longValue();
    long result = number.longValue();
    assertEquals(expected, result);
  }

  @Test
    @Timeout(8000)
  public void testLongValue_parseLongFails_invalidNumber() throws Exception {
    // value that is not a valid number at all
    // This will cause BigDecimal constructor to throw NumberFormatException,
    // but the focal method does not catch that, so test expects exception
    String invalidValue = "notANumber";
    LazilyParsedNumber number = new LazilyParsedNumber(invalidValue);
    try {
      number.longValue();
    } catch (NumberFormatException e) {
      // expected exception
      return;
    }
    throw new AssertionError("Expected NumberFormatException was not thrown");
  }
}