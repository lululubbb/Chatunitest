package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import java.lang.reflect.Method;

public class LazilyParsedNumber_564_5Test {

  @Test
    @Timeout(8000)
  public void testFloatValue_validFloatString() {
    LazilyParsedNumber number = new LazilyParsedNumber("3.14");
    float result = number.floatValue();
    assertEquals(3.14f, result, 0.00001f);
  }

  @Test
    @Timeout(8000)
  public void testFloatValue_integerString() {
    LazilyParsedNumber number = new LazilyParsedNumber("42");
    float result = number.floatValue();
    assertEquals(42.0f, result, 0.00001f);
  }

  @Test
    @Timeout(8000)
  public void testFloatValue_negativeFloatString() {
    LazilyParsedNumber number = new LazilyParsedNumber("-0.123");
    float result = number.floatValue();
    assertEquals(-0.123f, result, 0.00001f);
  }

  @Test
    @Timeout(8000)
  public void testFloatValue_invalidFloatString_throwsNumberFormatException() {
    LazilyParsedNumber number = new LazilyParsedNumber("notANumber");
    assertThrows(NumberFormatException.class, () -> number.floatValue());
  }

  @Test
    @Timeout(8000)
  public void testFloatValue_scientificNotation() {
    LazilyParsedNumber number = new LazilyParsedNumber("1.23e2");
    float result = number.floatValue();
    assertEquals(123.0f, result, 0.00001f);
  }

  @Test
    @Timeout(8000)
  public void testFloatValue_zero() {
    LazilyParsedNumber number = new LazilyParsedNumber("0");
    float result = number.floatValue();
    assertEquals(0.0f, result, 0.00001f);
  }
}