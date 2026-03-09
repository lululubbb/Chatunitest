package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class JavaVersion_83_2Test {

  @Test
    @Timeout(8000)
  public void testExtractBeginningInt_withLeadingDigits() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = JavaVersion.class.getDeclaredMethod("extractBeginningInt", String.class);
    method.setAccessible(true);

    int result = (int) method.invoke(null, "123abc");
    assertEquals(123, result);
  }

  @Test
    @Timeout(8000)
  public void testExtractBeginningInt_withOnlyDigits() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = JavaVersion.class.getDeclaredMethod("extractBeginningInt", String.class);
    method.setAccessible(true);

    int result = (int) method.invoke(null, "456789");
    assertEquals(456789, result);
  }

  @Test
    @Timeout(8000)
  public void testExtractBeginningInt_withNoLeadingDigits() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = JavaVersion.class.getDeclaredMethod("extractBeginningInt", String.class);
    method.setAccessible(true);

    int result = (int) method.invoke(null, "abc123");
    assertEquals(-1, result);
  }

  @Test
    @Timeout(8000)
  public void testExtractBeginningInt_withEmptyString() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = JavaVersion.class.getDeclaredMethod("extractBeginningInt", String.class);
    method.setAccessible(true);

    int result = (int) method.invoke(null, "");
    assertEquals(-1, result);
  }

  @Test
    @Timeout(8000)
  public void testExtractBeginningInt_withNonDigitFirstChar() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = JavaVersion.class.getDeclaredMethod("extractBeginningInt", String.class);
    method.setAccessible(true);

    int result = (int) method.invoke(null, "!@#");
    assertEquals(-1, result);
  }

  @Test
    @Timeout(8000)
  public void testExtractBeginningInt_withNumberFormatException() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = JavaVersion.class.getDeclaredMethod("extractBeginningInt", String.class);
    method.setAccessible(true);

    // Create a string with digits but too large to parse as int to trigger NumberFormatException
    String largeNumber = "9999999999999999999999999999999999999";
    int result = (int) method.invoke(null, largeNumber);

    assertEquals(-1, result);
  }
}