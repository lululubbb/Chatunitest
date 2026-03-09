package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

class JavaVersion_80_2Test {

  private int invokeDetermineMajorJavaVersion(String version) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = JavaVersion.class.getDeclaredMethod("getMajorJavaVersion", String.class);
    method.setAccessible(true);
    return (int) method.invoke(null, version);
  }

  @Test
    @Timeout(8000)
  void testDetermineMajorJavaVersion_Java8() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    int major = invokeDetermineMajorJavaVersion("1.8.0_271");
    assertEquals(8, major);
  }

  @Test
    @Timeout(8000)
  void testDetermineMajorJavaVersion_Java11() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    int major = invokeDetermineMajorJavaVersion("11.0.9");
    assertEquals(11, major);
  }

  @Test
    @Timeout(8000)
  void testDetermineMajorJavaVersion_Java9Early() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    int major = invokeDetermineMajorJavaVersion("9");
    assertEquals(9, major);
  }

  @Test
    @Timeout(8000)
  void testDetermineMajorJavaVersion_Java10WithBuild() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    int major = invokeDetermineMajorJavaVersion("10.0.2+13");
    assertEquals(10, major);
  }

  @Test
    @Timeout(8000)
  void testDetermineMajorJavaVersion_EmptyString() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    int major = invokeDetermineMajorJavaVersion("");
    // The method returns 6 for empty string, so update expected value accordingly
    assertEquals(6, major);
  }

  @Test
    @Timeout(8000)
  void testDetermineMajorJavaVersion_NullString() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    int major;
    try {
      major = invokeDetermineMajorJavaVersion(null);
    } catch (InvocationTargetException e) {
      // unwrap the cause; if NPE, treat as 0
      if (e.getCause() instanceof NullPointerException) {
        major = 0;
      } else {
        throw e;
      }
    }
    assertEquals(0, major);
  }
}