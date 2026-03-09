package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class JavaVersion_82_6Test {

  private int invokeParseDotted(String javaVersion) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = JavaVersion.class.getDeclaredMethod("parseDotted", String.class);
    method.setAccessible(true);
    return (int) method.invoke(null, javaVersion);
  }

  @Test
    @Timeout(8000)
  public void testParseDotted_SimpleVersion() throws Exception {
    assertEquals(8, invokeParseDotted("8"));
    assertEquals(9, invokeParseDotted("9"));
    assertEquals(11, invokeParseDotted("11"));
  }

  @Test
    @Timeout(8000)
  public void testParseDotted_VersionWithUnderscore() throws Exception {
    assertEquals(8, invokeParseDotted("8_0"));
    assertEquals(9, invokeParseDotted("9_1"));
  }

  @Test
    @Timeout(8000)
  public void testParseDotted_VersionWithDot() throws Exception {
    assertEquals(8, invokeParseDotted("8.0"));
    assertEquals(9, invokeParseDotted("9.1"));
  }

  @Test
    @Timeout(8000)
  public void testParseDotted_Java1Version() throws Exception {
    assertEquals(6, invokeParseDotted("1.6"));
    assertEquals(7, invokeParseDotted("1.7"));
    assertEquals(8, invokeParseDotted("1.8"));
  }

  @Test
    @Timeout(8000)
  public void testParseDotted_Java1VersionWithExtraParts() throws Exception {
    assertEquals(6, invokeParseDotted("1.6.0"));
    assertEquals(7, invokeParseDotted("1.7.0_80"));
  }

  @Test
    @Timeout(8000)
  public void testParseDotted_InvalidNumberFormat() throws Exception {
    assertEquals(-1, invokeParseDotted("abc"));
    assertEquals(-1, invokeParseDotted("1.a"));
    assertEquals(-1, invokeParseDotted("1_"));
    assertEquals(-1, invokeParseDotted(""));
    assertEquals(-1, invokeParseDotted("."));
    assertEquals(-1, invokeParseDotted("_"));
  }

  @Test
    @Timeout(8000)
  public void testParseDotted_NullInput() throws Exception {
    // Method does not handle null explicitly, expecting NullPointerException
    try {
      invokeParseDotted(null);
    } catch (InvocationTargetException e) {
      assertEquals(NullPointerException.class, e.getCause().getClass());
    }
  }
}