package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class JavaVersion_83_3Test {

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
  public void testExtractBeginningInt_allDigits() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = JavaVersion.class.getDeclaredMethod("extractBeginningInt", String.class);
    method.setAccessible(true);

    int result = (int) method.invoke(null, "456789");
    assertEquals(456789, result);
  }

  @Test
    @Timeout(8000)
  public void testExtractBeginningInt_noLeadingDigits() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = JavaVersion.class.getDeclaredMethod("extractBeginningInt", String.class);
    method.setAccessible(true);

    int result = (int) method.invoke(null, "abc123");
    assertEquals(-1, result);
  }

  @Test
    @Timeout(8000)
  public void testExtractBeginningInt_emptyString() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = JavaVersion.class.getDeclaredMethod("extractBeginningInt", String.class);
    method.setAccessible(true);

    int result = (int) method.invoke(null, "");
    assertEquals(-1, result);
  }

  @Test
    @Timeout(8000)
  public void testExtractBeginningInt_onlyNonDigits() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = JavaVersion.class.getDeclaredMethod("extractBeginningInt", String.class);
    method.setAccessible(true);

    int result = (int) method.invoke(null, "!!!@@@");
    assertEquals(-1, result);
  }

  @Test
    @Timeout(8000)
  public void testExtractBeginningInt_digitFollowedByNonDigitThenDigit() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = JavaVersion.class.getDeclaredMethod("extractBeginningInt", String.class);
    method.setAccessible(true);

    int result = (int) method.invoke(null, "7a8");
    assertEquals(7, result);
  }

  @Test
    @Timeout(8000)
  public void testExtractBeginningInt_leadingZeroes() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = JavaVersion.class.getDeclaredMethod("extractBeginningInt", String.class);
    method.setAccessible(true);

    int result = (int) method.invoke(null, "007xyz");
    assertEquals(7, result);
  }

}