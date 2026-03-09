package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Method;

class LazilyParsedNumber_570_1Test {

  @Test
    @Timeout(8000)
  void testEquals_sameInstance() {
    LazilyParsedNumber num = new LazilyParsedNumber("123");
    assertTrue(num.equals(num));
  }

  @Test
    @Timeout(8000)
  void testEquals_equalValueObjects() {
    LazilyParsedNumber num1 = new LazilyParsedNumber("123");
    LazilyParsedNumber num2 = new LazilyParsedNumber("123");
    assertTrue(num1.equals(num2));
    assertTrue(num2.equals(num1));
  }

  @Test
    @Timeout(8000)
  void testEquals_differentValueObjects() {
    LazilyParsedNumber num1 = new LazilyParsedNumber("123");
    LazilyParsedNumber num2 = new LazilyParsedNumber("456");
    assertFalse(num1.equals(num2));
    assertFalse(num2.equals(num1));
  }

  @Test
    @Timeout(8000)
  void testEquals_null() {
    LazilyParsedNumber num = new LazilyParsedNumber("123");
    assertFalse(num.equals(null));
  }

  @Test
    @Timeout(8000)
  void testEquals_differentClass() {
    LazilyParsedNumber num = new LazilyParsedNumber("123");
    String other = "123";
    assertFalse(num.equals(other));
  }

  @Test
    @Timeout(8000)
  void testEquals_valueFieldEqualityByReflection() throws Exception {
    // Using reflection to create two LazilyParsedNumber with same value string but different instances
    LazilyParsedNumber num1 = new LazilyParsedNumber("789");
    LazilyParsedNumber num2 = new LazilyParsedNumber("789");

    // Access private final field 'value' via reflection and set to same String instance forcibly
    var field = LazilyParsedNumber.class.getDeclaredField("value");
    field.setAccessible(true);
    String sharedValue = "sharedValue";
    field.set(num1, sharedValue);
    field.set(num2, sharedValue);

    assertTrue(num1.equals(num2));
  }
}