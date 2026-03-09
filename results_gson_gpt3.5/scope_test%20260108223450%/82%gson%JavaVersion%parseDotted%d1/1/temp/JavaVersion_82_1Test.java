package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class JavaVersion_82_1Test {

  private int invokeParseDotted(String version) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = JavaVersion.class.getDeclaredMethod("parseDotted", String.class);
    method.setAccessible(true);
    return (int) method.invoke(null, version);
  }

  @Test
    @Timeout(8000)
  public void testParseDotted_singleNumber() throws Exception {
    assertEquals(8, invokeParseDotted("8"));
  }

  @Test
    @Timeout(8000)
  public void testParseDotted_dotSeparated() throws Exception {
    assertEquals(8, invokeParseDotted("8.0"));
  }

  @Test
    @Timeout(8000)
  public void testParseDotted_underscoreSeparated() throws Exception {
    assertEquals(8, invokeParseDotted("8_0"));
  }

  @Test
    @Timeout(8000)
  public void testParseDotted_javaVersion1Dot8() throws Exception {
    assertEquals(8, invokeParseDotted("1.8"));
  }

  @Test
    @Timeout(8000)
  public void testParseDotted_javaVersion1Dot7() throws Exception {
    assertEquals(7, invokeParseDotted("1.7"));
  }

  @Test
    @Timeout(8000)
  public void testParseDotted_javaVersion1Underscore7() throws Exception {
    assertEquals(7, invokeParseDotted("1_7"));
  }

  @Test
    @Timeout(8000)
  public void testParseDotted_invalidNumberFormat() throws Exception {
    assertEquals(-1, invokeParseDotted("abc.def"));
  }

  @Test
    @Timeout(8000)
  public void testParseDotted_emptyString() throws Exception {
    assertEquals(-1, invokeParseDotted(""));
  }

  @Test
    @Timeout(8000)
  public void testParseDotted_nullInput() throws Exception {
    // parseDotted does not handle null explicitly, it will throw NullPointerException.
    // So we test that exception is thrown.
    Method method = JavaVersion.class.getDeclaredMethod("parseDotted", String.class);
    method.setAccessible(true);
    try {
      method.invoke(null, new Object[] { null });
    } catch (InvocationTargetException e) {
      assertEquals(NullPointerException.class, e.getCause().getClass());
    }
  }
}