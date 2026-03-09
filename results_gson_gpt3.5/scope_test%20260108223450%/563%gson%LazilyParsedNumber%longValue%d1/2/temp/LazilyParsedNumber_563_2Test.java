package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import java.math.BigDecimal;

public class LazilyParsedNumber_563_2Test {

  @Test
    @Timeout(8000)
  public void testLongValue_parsableLong() {
    LazilyParsedNumber number = new LazilyParsedNumber("123456789");
    long result = number.longValue();
    assertEquals(123456789L, result);
  }

  @Test
    @Timeout(8000)
  public void testLongValue_notParsableLong_usesBigDecimal() {
    // Number larger than Long.MAX_VALUE to force NumberFormatException in parseLong
    String bigNumberStr = "9223372036854775808"; // Long.MAX_VALUE + 1
    LazilyParsedNumber number = new LazilyParsedNumber(bigNumberStr);
    long result = number.longValue();
    // BigDecimal.longValue() truncates to Long.MAX_VALUE for values > Long.MAX_VALUE
    assertEquals(new BigDecimal(bigNumberStr).longValue(), result);
  }

  @Test
    @Timeout(8000)
  public void testLongValue_negativeNumber() {
    LazilyParsedNumber number = new LazilyParsedNumber("-9876543210");
    long result = number.longValue();
    assertEquals(-9876543210L, result);
  }

  @Test
    @Timeout(8000)
  public void testLongValue_zero() {
    LazilyParsedNumber number = new LazilyParsedNumber("0");
    long result = number.longValue();
    assertEquals(0L, result);
  }

  @Test
    @Timeout(8000)
  public void testLongValue_invalidNumberFormat_throwsNumberFormatExceptionFromBigDecimal() {
    // LazilyParsedNumber.longValue() catches NumberFormatException from parseLong,
    // but if BigDecimal constructor throws NumberFormatException, it propagates.
    LazilyParsedNumber number = new LazilyParsedNumber("notANumber");
    assertThrows(NumberFormatException.class, number::longValue);
  }
}