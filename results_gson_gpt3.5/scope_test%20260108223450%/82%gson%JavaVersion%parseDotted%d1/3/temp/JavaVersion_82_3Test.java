package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class JavaVersion_82_3Test {

  private int invokeParseDotted(String javaVersion) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = JavaVersion.class.getDeclaredMethod("parseDotted", String.class);
    method.setAccessible(true);
    return (int) method.invoke(null, javaVersion);
  }

  @Test
    @Timeout(8000)
  public void testParseDotted_SimpleVersion() throws Exception {
    assertEquals(8, invokeParseDotted("8"));
  }

  @Test
    @Timeout(8000)
  public void testParseDotted_VersionWithUnderscore() throws Exception {
    assertEquals(8, invokeParseDotted("8_0"));
  }

  @Test
    @Timeout(8000)
  public void testParseDotted_VersionWithDot() throws Exception {
    assertEquals(8, invokeParseDotted("8.0"));
  }

  @Test
    @Timeout(8000)
  public void testParseDotted_Java8StyleVersion() throws Exception {
    assertEquals(8, invokeParseDotted("1.8"));
  }

  @Test
    @Timeout(8000)
  public void testParseDotted_Java7StyleVersionWithPatch() throws Exception {
    assertEquals(7, invokeParseDotted("1.7.0"));
  }

  @Test
    @Timeout(8000)
  public void testParseDotted_Java9StyleVersion() throws Exception {
    assertEquals(9, invokeParseDotted("9"));
  }

  @Test
    @Timeout(8000)
  public void testParseDotted_InvalidNumberFormat() throws Exception {
    assertEquals(-1, invokeParseDotted("abc"));
  }

  @Test
    @Timeout(8000)
  public void testParseDotted_EmptyString() throws Exception {
    assertEquals(-1, invokeParseDotted(""));
  }

  @Test
    @Timeout(8000)
  public void testParseDotted_OnlyDot() throws Exception {
    assertEquals(-1, invokeParseDotted("."));
  }

  @Test
    @Timeout(8000)
  public void testParseDotted_OnlyUnderscore() throws Exception {
    assertEquals(-1, invokeParseDotted("_"));
  }

  @Test
    @Timeout(8000)
  public void testParseDotted_LeadingNonNumber() throws Exception {
    assertEquals(-1, invokeParseDotted("a.8"));
  }

  @Test
    @Timeout(8000)
  public void testParseDotted_LeadingNumberWithNonNumberSecondPart() throws Exception {
    assertEquals(-1, invokeParseDotted("1.a"));
  }
}