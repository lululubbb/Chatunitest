package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class LazilyParsedNumber_570_4Test {

  @Test
    @Timeout(8000)
  void testEquals_sameInstance() {
    LazilyParsedNumber number = new LazilyParsedNumber("123");
    assertTrue(number.equals(number));
  }

  @Test
    @Timeout(8000)
  void testEquals_nullObject() {
    LazilyParsedNumber number = new LazilyParsedNumber("123");
    assertFalse(number.equals(null));
  }

  @Test
    @Timeout(8000)
  void testEquals_differentClass() {
    LazilyParsedNumber number = new LazilyParsedNumber("123");
    assertFalse(number.equals("123"));
  }

  @Test
    @Timeout(8000)
  void testEquals_sameValueString() {
    LazilyParsedNumber number1 = new LazilyParsedNumber("123");
    LazilyParsedNumber number2 = new LazilyParsedNumber("123");
    assertTrue(number1.equals(number2));
  }

  @Test
    @Timeout(8000)
  void testEquals_differentValueString() {
    LazilyParsedNumber number1 = new LazilyParsedNumber("123");
    LazilyParsedNumber number2 = new LazilyParsedNumber("456");
    assertFalse(number1.equals(number2));
  }

  @Test
    @Timeout(8000)
  void testEquals_valueFieldReferenceEquality() throws Exception {
    // Use reflection to create two LazilyParsedNumber instances with the same String object reference
    String sharedValue = "789";
    LazilyParsedNumber number1 = new LazilyParsedNumber(sharedValue);
    LazilyParsedNumber number2 = new LazilyParsedNumber(sharedValue);

    // Confirm equals returns true because value == other.value (same reference)
    assertTrue(number1.equals(number2));
  }

  @Test
    @Timeout(8000)
  void testWriteReplace_invocation() throws Exception {
    LazilyParsedNumber number = new LazilyParsedNumber("123");
    Method writeReplace = LazilyParsedNumber.class.getDeclaredMethod("writeReplace");
    writeReplace.setAccessible(true);
    Object replaced = writeReplace.invoke(number);
    // The writeReplace method returns a BigDecimal, so check its value instead of equality with number
    assertEquals("123", replaced.toString());
  }

  @Test
    @Timeout(8000)
  void testReadObject_invocation() throws Exception {
    LazilyParsedNumber number = new LazilyParsedNumber("123");
    Method readObject = LazilyParsedNumber.class.getDeclaredMethod("readObject", java.io.ObjectInputStream.class);
    readObject.setAccessible(true);

    // We expect readObject to throw InvalidObjectException on deserialization attempt
    try {
      readObject.invoke(number, (Object) null);
      fail("Expected exception not thrown");
    } catch (Exception e) {
      Throwable cause = e.getCause();
      assertTrue(cause instanceof java.io.InvalidObjectException);
    }
  }
}