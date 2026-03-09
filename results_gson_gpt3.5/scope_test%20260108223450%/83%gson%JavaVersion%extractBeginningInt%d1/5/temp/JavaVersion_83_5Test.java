package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class JavaVersion_83_5Test {

  @Test
    @Timeout(8000)
  public void testExtractBeginningInt_validNumber() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = JavaVersion.class.getDeclaredMethod("extractBeginningInt", String.class);
    method.setAccessible(true);

    // Input starts with digits only
    int result = (int) method.invoke(null, "12345");
    assertEquals(12345, result);

    // Input starts with digits then letters
    result = (int) method.invoke(null, "987abc");
    assertEquals(987, result);

    // Input with single digit
    result = (int) method.invoke(null, "4version");
    assertEquals(4, result);

    // Input with digits then non-digit char immediately
    result = (int) method.invoke(null, "0_1_2");
    assertEquals(0, result);
  }

  @Test
    @Timeout(8000)
  public void testExtractBeginningInt_emptyString() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = JavaVersion.class.getDeclaredMethod("extractBeginningInt", String.class);
    method.setAccessible(true);

    // Empty string should cause NumberFormatException and return -1
    int result = (int) method.invoke(null, "");
    assertEquals(-1, result);
  }

  @Test
    @Timeout(8000)
  public void testExtractBeginningInt_noLeadingDigits() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = JavaVersion.class.getDeclaredMethod("extractBeginningInt", String.class);
    method.setAccessible(true);

    // String starts with non-digit character, should return -1
    int result = (int) method.invoke(null, "abc123");
    assertEquals(-1, result);

    // String with special characters first
    result = (int) method.invoke(null, "!@#456");
    assertEquals(-1, result);
  }
}