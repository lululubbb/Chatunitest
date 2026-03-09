package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class LazilyParsedNumber_562_2Test {

  @Test
    @Timeout(8000)
  public void testIntValue_withValidInteger() {
    LazilyParsedNumber number = new LazilyParsedNumber("123");
    int result = number.intValue();
    assertEquals(123, result);
  }

  @Test
    @Timeout(8000)
  public void testIntValue_withValidLongWithinIntRange() {
    LazilyParsedNumber number = new LazilyParsedNumber("2147483647"); // Integer.MAX_VALUE
    int result = number.intValue();
    assertEquals(2147483647, result);
  }

  @Test
    @Timeout(8000)
  public void testIntValue_withValidLongOutsideIntRange() {
    LazilyParsedNumber number = new LazilyParsedNumber("2147483648"); // Integer.MAX_VALUE + 1
    int result = number.intValue();
    // Parsing as Integer.parseInt throws, Long.parseLong works, cast to int overflows to Integer.MIN_VALUE
    assertEquals((int) 2147483648L, result);
  }

  @Test
    @Timeout(8000)
  public void testIntValue_withDecimalValue() {
    LazilyParsedNumber number = new LazilyParsedNumber("123.456");
    int result = number.intValue();
    assertEquals(123, result);
  }

  @Test
    @Timeout(8000)
  public void testIntValue_withInvalidNumberFormat() {
    LazilyParsedNumber number = new LazilyParsedNumber("abc");
    // The call to intValue() will throw NumberFormatException, so assertThrows is used directly
    assertThrows(NumberFormatException.class, number::intValue);
  }

  @Test
    @Timeout(8000)
  public void testWriteReplace_privateMethod() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    LazilyParsedNumber number = new LazilyParsedNumber("123");
    Method writeReplace = LazilyParsedNumber.class.getDeclaredMethod("writeReplace");
    writeReplace.setAccessible(true);
    Object replacement = writeReplace.invoke(number);
    assertNotNull(replacement);
    assertTrue(replacement instanceof Number);
  }

  @Test
    @Timeout(8000)
  public void testReadObject_privateMethod() throws Exception {
    LazilyParsedNumber number = new LazilyParsedNumber("123");
    Method readObject = LazilyParsedNumber.class.getDeclaredMethod("readObject", java.io.ObjectInputStream.class);
    readObject.setAccessible(true);
    // The invoked method throws InvalidObjectException wrapped in InvocationTargetException
    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> readObject.invoke(number, new Object[] {null}));
    // Check that cause is InvalidObjectException with expected message
    assertTrue(thrown.getCause() instanceof java.io.InvalidObjectException);
    assertEquals("Deserialization is unsupported", thrown.getCause().getMessage());
  }
}