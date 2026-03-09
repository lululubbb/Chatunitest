package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class LazilyParsedNumber_564_1Test {

  @Test
    @Timeout(8000)
  @DisplayName("floatValue returns correct float for valid float string")
  public void testFloatValue_ValidFloatString() {
    LazilyParsedNumber number = new LazilyParsedNumber("123.456");
    float result = number.floatValue();
    assertEquals(123.456f, result, 0.0001f);
  }

  @Test
    @Timeout(8000)
  @DisplayName("floatValue throws NumberFormatException for invalid float string")
  public void testFloatValue_InvalidFloatString() {
    LazilyParsedNumber number = new LazilyParsedNumber("not_a_float");
    assertThrows(NumberFormatException.class, number::floatValue);
  }

  @Test
    @Timeout(8000)
  @DisplayName("floatValue returns correct float for integer string")
  public void testFloatValue_IntegerString() {
    LazilyParsedNumber number = new LazilyParsedNumber("42");
    float result = number.floatValue();
    assertEquals(42.0f, result, 0.0001f);
  }

  @Test
    @Timeout(8000)
  @DisplayName("floatValue returns correct float for negative float string")
  public void testFloatValue_NegativeFloatString() {
    LazilyParsedNumber number = new LazilyParsedNumber("-3.14");
    float result = number.floatValue();
    assertEquals(-3.14f, result, 0.0001f);
  }

  @Test
    @Timeout(8000)
  @DisplayName("floatValue returns correct float for scientific notation string")
  public void testFloatValue_ScientificNotation() {
    LazilyParsedNumber number = new LazilyParsedNumber("1.23e2");
    float result = number.floatValue();
    assertEquals(123f, result, 0.0001f);
  }
}