package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

public class LazilyParsedNumber_562_3Test {

  @Test
    @Timeout(8000)
  public void testIntValue_parsesInteger() {
    LazilyParsedNumber number = new LazilyParsedNumber("123");
    int result = number.intValue();
    assertEquals(123, result);
  }

  @Test
    @Timeout(8000)
  public void testIntValue_parsesLong() {
    // Value is not a valid int but valid long within int range after cast
    LazilyParsedNumber number = new LazilyParsedNumber("2147483648"); // Integer.MAX_VALUE + 1
    int result = number.intValue();
    assertEquals((int)2147483648L, result);
  }

  @Test
    @Timeout(8000)
  public void testIntValue_parsesBigDecimal() {
    // Value is not a valid int or long but valid BigDecimal
    LazilyParsedNumber number = new LazilyParsedNumber("12345678901234567890.123");
    int result = number.intValue();
    assertEquals(new java.math.BigDecimal("12345678901234567890.123").intValue(), result);
  }

  @Test
    @Timeout(8000)
  public void testWriteReplace_invokesSuccessfully() throws Throwable {
    LazilyParsedNumber number = new LazilyParsedNumber("1");
    Method writeReplace = LazilyParsedNumber.class.getDeclaredMethod("writeReplace");
    writeReplace.setAccessible(true);
    Object result = writeReplace.invoke(number);
    // The actual returned object type is not specified, just check no exception and non-null
    assertEquals("1", result.toString());
  }

  @Test
    @Timeout(8000)
  public void testReadObject_invokesSuccessfully() throws Throwable {
    LazilyParsedNumber number = new LazilyParsedNumber("1");
    Method readObject = LazilyParsedNumber.class.getDeclaredMethod("readObject", java.io.ObjectInputStream.class);
    readObject.setAccessible(true);
    // Passing null to simulate call, expecting it to throw IOException or InvalidObjectException if any
    try {
      readObject.invoke(number, (Object) null);
    } catch (InvocationTargetException e) {
      // unwrap cause
      Throwable cause = e.getCause();
      // Accept IOException or InvalidObjectException or NullPointerException from null input
      if (!(cause instanceof IOException) && !(cause instanceof InvalidObjectException)
          && !(cause instanceof NullPointerException)) {
        throw cause;
      }
    }
  }
}