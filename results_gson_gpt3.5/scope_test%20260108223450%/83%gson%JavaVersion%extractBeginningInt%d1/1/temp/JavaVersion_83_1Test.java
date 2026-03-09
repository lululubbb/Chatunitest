package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class JavaVersion_83_1Test {

  @Test
    @Timeout(8000)
  void extractBeginningInt_validLeadingDigits() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = JavaVersion.class.getDeclaredMethod("extractBeginningInt", String.class);
    method.setAccessible(true);

    int result = (int) method.invoke(null, "123abc");
    assertEquals(123, result);

    result = (int) method.invoke(null, "7.0.1");
    assertEquals(7, result);

    result = (int) method.invoke(null, "42");
    assertEquals(42, result);

    result = (int) method.invoke(null, "0something");
    assertEquals(0, result);
  }

  @Test
    @Timeout(8000)
  void extractBeginningInt_noLeadingDigits() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = JavaVersion.class.getDeclaredMethod("extractBeginningInt", String.class);
    method.setAccessible(true);

    int result = (int) method.invoke(null, "abc123");
    assertEquals(-1, result);

    result = (int) method.invoke(null, "");
    assertEquals(-1, result);

    result = (int) method.invoke(null, ".123");
    assertEquals(-1, result);
  }

  @Test
    @Timeout(8000)
  void extractBeginningInt_nonDigitLeadingChar() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = JavaVersion.class.getDeclaredMethod("extractBeginningInt", String.class);
    method.setAccessible(true);

    int result = (int) method.invoke(null, "-123");
    assertEquals(-1, result);

    result = (int) method.invoke(null, " 123");
    assertEquals(-1, result);

    result = (int) method.invoke(null, "_123");
    assertEquals(-1, result);
  }
}