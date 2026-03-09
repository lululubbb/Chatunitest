package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class JavaVersion_82_4Test {

  private static int invokeParseDotted(String version) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = JavaVersion.class.getDeclaredMethod("parseDotted", String.class);
    method.setAccessible(true);
    return (int) method.invoke(null, version);
  }

  @Test
    @Timeout(8000)
  public void testParseDotted_singleNumber() throws Exception {
    assertEquals(9, invokeParseDotted("9"));
    assertEquals(11, invokeParseDotted("11"));
    assertEquals(17, invokeParseDotted("17"));
  }

  @Test
    @Timeout(8000)
  public void testParseDotted_withUnderscore() throws Exception {
    assertEquals(9, invokeParseDotted("9_0_1"));
    assertEquals(11, invokeParseDotted("11_0_2"));
  }

  @Test
    @Timeout(8000)
  public void testParseDotted_withDots() throws Exception {
    assertEquals(8, invokeParseDotted("1.8.0_181"));
    assertEquals(7, invokeParseDotted("1.7.0"));
  }

  @Test
    @Timeout(8000)
  public void testParseDotted_withMixedSeparators() throws Exception {
    assertEquals(8, invokeParseDotted("1.8_0_181"));
    assertEquals(8, invokeParseDotted("1_8.0_181"));
  }

  @Test
    @Timeout(8000)
  public void testParseDotted_invalidNumberFormat() throws Exception {
    assertEquals(-1, invokeParseDotted("a.b.c"));
    assertEquals(-1, invokeParseDotted("1.a"));
    assertEquals(-1, invokeParseDotted("one.two"));
    assertEquals(-1, invokeParseDotted(""));
  }

  @Test
    @Timeout(8000)
  public void testParseDotted_singleOneNoSecondPart() throws Exception {
    // "1" alone should return 1 (firstVer = 1, parts.length = 1)
    assertEquals(1, invokeParseDotted("1"));
  }
}