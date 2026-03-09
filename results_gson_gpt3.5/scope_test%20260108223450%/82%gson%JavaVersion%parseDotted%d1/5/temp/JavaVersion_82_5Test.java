package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class JavaVersion_82_5Test {

  @Test
    @Timeout(8000)
  public void testParseDotted_firstVersionIs1AndHasSecondPart() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = JavaVersion.class.getDeclaredMethod("parseDotted", String.class);
    method.setAccessible(true);
    // "1.8" -> firstVer=1, parts.length=2, returns parts[1] as int = 8
    int result = (int) method.invoke(null, "1.8");
    assertEquals(8, result);
  }

  @Test
    @Timeout(8000)
  public void testParseDotted_firstVersionIs1AndHasSecondPart_withUnderscore() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = JavaVersion.class.getDeclaredMethod("parseDotted", String.class);
    method.setAccessible(true);
    // "1_7" -> firstVer=1, parts.length=2, returns parts[1] as int = 7
    int result = (int) method.invoke(null, "1_7");
    assertEquals(7, result);
  }

  @Test
    @Timeout(8000)
  public void testParseDotted_firstVersionIsNot1() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = JavaVersion.class.getDeclaredMethod("parseDotted", String.class);
    method.setAccessible(true);
    // "11.0.2" -> firstVer=11, returns 11
    int result = (int) method.invoke(null, "11.0.2");
    assertEquals(11, result);
  }

  @Test
    @Timeout(8000)
  public void testParseDotted_singleNumber() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = JavaVersion.class.getDeclaredMethod("parseDotted", String.class);
    method.setAccessible(true);
    // "9" -> firstVer=9, returns 9
    int result = (int) method.invoke(null, "9");
    assertEquals(9, result);
  }

  @Test
    @Timeout(8000)
  public void testParseDotted_invalidNumberFormat() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = JavaVersion.class.getDeclaredMethod("parseDotted", String.class);
    method.setAccessible(true);
    // "abc" -> NumberFormatException caught, returns -1
    int result = (int) method.invoke(null, "abc");
    assertEquals(-1, result);
  }

  @Test
    @Timeout(8000)
  public void testParseDotted_invalidSecondPartNumberFormat() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = JavaVersion.class.getDeclaredMethod("parseDotted", String.class);
    method.setAccessible(true);
    // "1.xyz" -> firstVer=1, but parts[1] = "xyz" causes NumberFormatException, returns -1
    int result = (int) method.invoke(null, "1.xyz");
    assertEquals(-1, result);
  }

  @Test
    @Timeout(8000)
  public void testParseDotted_emptyString() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = JavaVersion.class.getDeclaredMethod("parseDotted", String.class);
    method.setAccessible(true);
    // "" -> parts[0] = "", Integer.parseInt("") throws NumberFormatException, returns -1
    int result = (int) method.invoke(null, "");
    assertEquals(-1, result);
  }
}