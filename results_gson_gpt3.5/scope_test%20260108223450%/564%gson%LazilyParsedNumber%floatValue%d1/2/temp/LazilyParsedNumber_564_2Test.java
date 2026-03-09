package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class LazilyParsedNumber_564_2Test {

  @Test
    @Timeout(8000)
  void floatValue_validFloatString_returnsParsedFloat() {
    LazilyParsedNumber number = new LazilyParsedNumber("3.14");
    float result = number.floatValue();
    assertEquals(3.14f, result, 0.00001f);
  }

  @Test
    @Timeout(8000)
  void floatValue_integerString_returnsParsedFloat() {
    LazilyParsedNumber number = new LazilyParsedNumber("42");
    float result = number.floatValue();
    assertEquals(42f, result, 0.00001f);
  }

  @Test
    @Timeout(8000)
  void floatValue_negativeFloatString_returnsParsedFloat() {
    LazilyParsedNumber number = new LazilyParsedNumber("-123.456");
    float result = number.floatValue();
    assertEquals(-123.456f, result, 0.00001f);
  }

  @Test
    @Timeout(8000)
  void floatValue_invalidFloatString_throwsNumberFormatException() {
    LazilyParsedNumber number = new LazilyParsedNumber("notANumber");
    assertThrows(NumberFormatException.class, number::floatValue);
  }

  @Test
    @Timeout(8000)
  void floatValue_emptyString_throwsNumberFormatException() {
    LazilyParsedNumber number = new LazilyParsedNumber("");
    assertThrows(NumberFormatException.class, number::floatValue);
  }
}