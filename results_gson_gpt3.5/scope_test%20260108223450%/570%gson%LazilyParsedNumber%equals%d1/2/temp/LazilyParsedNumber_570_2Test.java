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

public class LazilyParsedNumber_570_2Test {

  @Test
    @Timeout(8000)
  public void testEquals_sameObject() {
    LazilyParsedNumber number = new LazilyParsedNumber("123");
    assertTrue(number.equals(number));
  }

  @Test
    @Timeout(8000)
  public void testEquals_nullObject() {
    LazilyParsedNumber number = new LazilyParsedNumber("123");
    assertFalse(number.equals(null));
  }

  @Test
    @Timeout(8000)
  public void testEquals_differentClass() {
    LazilyParsedNumber number = new LazilyParsedNumber("123");
    String other = "123";
    assertFalse(number.equals(other));
  }

  @Test
    @Timeout(8000)
  public void testEquals_differentValueObjects() {
    LazilyParsedNumber number1 = new LazilyParsedNumber("123");
    LazilyParsedNumber number2 = new LazilyParsedNumber("456");
    assertFalse(number1.equals(number2));
  }

  @Test
    @Timeout(8000)
  public void testEquals_sameValueStringObjects() {
    LazilyParsedNumber number1 = new LazilyParsedNumber("123");
    LazilyParsedNumber number2 = new LazilyParsedNumber("123");
    assertTrue(number1.equals(number2));
  }

  @Test
    @Timeout(8000)
  public void testEquals_valueReferenceEquality() throws Exception {
    // Use reflection to create two LazilyParsedNumber instances with the same String instance for value
    String sharedValue = "789";
    LazilyParsedNumber number1 = new LazilyParsedNumber(sharedValue);
    LazilyParsedNumber number2 = new LazilyParsedNumber(sharedValue);

    // equals should return true because value == other.value is true
    assertTrue(number1.equals(number2));
  }

  @Test
    @Timeout(8000)
  public void testEquals_valueDifferentStringObjectsSameContent() throws Exception {
    // Create two LazilyParsedNumber with equal but different String objects for value
    String value1 = new String("1000");
    String value2 = new String("1000");
    LazilyParsedNumber number1 = new LazilyParsedNumber(value1);
    LazilyParsedNumber number2 = new LazilyParsedNumber(value2);

    // equals should return true because value.equals(other.value) is true
    assertTrue(number1.equals(number2));
  }
}