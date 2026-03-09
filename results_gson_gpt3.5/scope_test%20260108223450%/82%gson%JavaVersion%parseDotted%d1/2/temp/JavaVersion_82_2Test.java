package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class JavaVersion_82_2Test {

  private int invokeParseDotted(String javaVersion) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = JavaVersion.class.getDeclaredMethod("parseDotted", String.class);
    method.setAccessible(true);
    return (int) method.invoke(null, javaVersion);
  }

  @Test
    @Timeout(8000)
  public void testParseDotted_singleNumber() throws Exception {
    assertEquals(11, invokeParseDotted("11"));
    assertEquals(9, invokeParseDotted("9"));
    assertEquals(8, invokeParseDotted("8"));
  }

  @Test
    @Timeout(8000)
  public void testParseDotted_1DotXVersion() throws Exception {
    assertEquals(6, invokeParseDotted("1.6"));
    assertEquals(7, invokeParseDotted("1.7"));
    assertEquals(8, invokeParseDotted("1.8"));
  }

  @Test
    @Timeout(8000)
  public void testParseDotted_1UnderscoreXVersion() throws Exception {
    assertEquals(6, invokeParseDotted("1_6"));
    assertEquals(7, invokeParseDotted("1_7"));
    assertEquals(8, invokeParseDotted("1_8"));
  }

  @Test
    @Timeout(8000)
  public void testParseDotted_otherDots() throws Exception {
    assertEquals(9, invokeParseDotted("9.0.4"));
    assertEquals(11, invokeParseDotted("11.0.2"));
    assertEquals(15, invokeParseDotted("15.0.1"));
  }

  @Test
    @Timeout(8000)
  public void testParseDotted_invalidNumberFormat() throws Exception {
    assertEquals(-1, invokeParseDotted("abc"));
    assertEquals(-1, invokeParseDotted("1.a"));
    assertEquals(-1, invokeParseDotted("a.b.c"));
    assertEquals(-1, invokeParseDotted(""));
    assertEquals(-1, invokeParseDotted("."));
  }

  @Test
    @Timeout(8000)
  public void testParseDotted_nullInput() throws Exception {
    Method method = JavaVersion.class.getDeclaredMethod("parseDotted", String.class);
    method.setAccessible(true);
    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      method.invoke(null, (Object) null);
    });
    assertEquals(NullPointerException.class, thrown.getCause().getClass());
  }
}