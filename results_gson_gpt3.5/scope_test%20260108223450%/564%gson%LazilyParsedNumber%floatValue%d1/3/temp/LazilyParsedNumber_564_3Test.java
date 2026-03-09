package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class LazilyParsedNumber_564_3Test {

  @Test
    @Timeout(8000)
  public void testFloatValue_validFloatString() {
    LazilyParsedNumber num = new LazilyParsedNumber("123.45");
    float result = num.floatValue();
    assertEquals(123.45f, result, 0.00001f);
  }

  @Test
    @Timeout(8000)
  public void testFloatValue_integerString() {
    LazilyParsedNumber num = new LazilyParsedNumber("42");
    float result = num.floatValue();
    assertEquals(42.0f, result, 0.00001f);
  }

  @Test
    @Timeout(8000)
  public void testFloatValue_scientificNotation() {
    LazilyParsedNumber num = new LazilyParsedNumber("1.23e2");
    float result = num.floatValue();
    assertEquals(123.0f, result, 0.00001f);
  }

  @Test
    @Timeout(8000)
  public void testFloatValue_invalidFloatString_throwsNumberFormatException() {
    LazilyParsedNumber num = new LazilyParsedNumber("abc");
    assertThrows(NumberFormatException.class, num::floatValue);
  }

  @Test
    @Timeout(8000)
  public void testFloatValue_emptyString_throwsNumberFormatException() {
    LazilyParsedNumber num = new LazilyParsedNumber("");
    assertThrows(NumberFormatException.class, num::floatValue);
  }

  @Test
    @Timeout(8000)
  public void testFloatValue_nullString_throwsNullPointerException() throws Exception {
    // Use reflection to create instance with normal constructor
    Constructor<LazilyParsedNumber> constructor = LazilyParsedNumber.class.getDeclaredConstructor(String.class);
    constructor.setAccessible(true);
    LazilyParsedNumber num = constructor.newInstance("0");

    // Set private final field 'value' to null via reflection
    Field field = LazilyParsedNumber.class.getDeclaredField("value");
    field.setAccessible(true);
    field.set(num, null);

    assertThrows(NullPointerException.class, num::floatValue);
  }
}