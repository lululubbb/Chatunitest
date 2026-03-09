package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class JavaVersion_83_4Test {

  @Test
    @Timeout(8000)
  public void testExtractBeginningInt_validNumber() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = JavaVersion.class.getDeclaredMethod("extractBeginningInt", String.class);
    method.setAccessible(true);

    // Simple number at start
    int result = (int) method.invoke(null, "123abc");
    assertEquals(123, result);

    // Number only
    result = (int) method.invoke(null, "456");
    assertEquals(456, result);

    // Number with non-digit after digits
    result = (int) method.invoke(null, "7.8.9");
    assertEquals(7, result);

    // Number with spaces after digits
    result = (int) method.invoke(null, "42 is the answer");
    assertEquals(42, result);
  }

  @Test
    @Timeout(8000)
  public void testExtractBeginningInt_noDigits() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = JavaVersion.class.getDeclaredMethod("extractBeginningInt", String.class);
    method.setAccessible(true);

    // No digits at start
    int result = (int) method.invoke(null, "abc123");
    assertEquals(-1, result);

    // Empty string
    result = (int) method.invoke(null, "");
    assertEquals(-1, result);

    // String starts with non-digit char
    result = (int) method.invoke(null, ".123");
    assertEquals(-1, result);
  }

  @Test
    @Timeout(8000)
  public void testExtractBeginningInt_leadingZeros() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = JavaVersion.class.getDeclaredMethod("extractBeginningInt", String.class);
    method.setAccessible(true);

    // Leading zeros
    int result = (int) method.invoke(null, "007bond");
    assertEquals(7, result);

    // Only zeros
    result = (int) method.invoke(null, "0000");
    assertEquals(0, result);
  }
}