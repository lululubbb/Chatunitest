package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

public class LazilyParsedNumber_563_4Test {

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
    // The string is too large for Long.parseLong and will cause NumberFormatException
    String largeNumber = "92233720368547758079223372036854775807"; 
    LazilyParsedNumber number = new LazilyParsedNumber(largeNumber);
    long expected = new BigDecimal(largeNumber).longValue();
    long result = number.longValue();
    assertEquals(expected, result);
  }

  @Test
    @Timeout(8000)
  public void testPrivateWriteReplace_methodInvocation() throws Exception {
    LazilyParsedNumber number = new LazilyParsedNumber("123");
    Method writeReplace = LazilyParsedNumber.class.getDeclaredMethod("writeReplace");
    writeReplace.setAccessible(true);
    Object result = writeReplace.invoke(number);
    // The writeReplace method returns Object (likely LazilyParsedNumber or a proxy), just check not null
    assertEquals(number.toString(), result.toString());
  }

  @Test
    @Timeout(8000)
  public void testPrivateReadObject_methodInvocation() throws Exception {
    LazilyParsedNumber number = new LazilyParsedNumber("123");
    Method readObject = LazilyParsedNumber.class.getDeclaredMethod("readObject", java.io.ObjectInputStream.class);
    readObject.setAccessible(true);
    // We use null instead of a mock ObjectInputStream because the method throws InvalidObjectException regardless
    try {
      readObject.invoke(number, new Object[] { null });
    } catch (java.lang.reflect.InvocationTargetException e) {
      // Expect InvalidObjectException wrapped in InvocationTargetException; test should pass if thrown
      Throwable cause = e.getCause();
      if (!(cause instanceof java.io.InvalidObjectException)) {
        throw e;
      }
    }
  }
}