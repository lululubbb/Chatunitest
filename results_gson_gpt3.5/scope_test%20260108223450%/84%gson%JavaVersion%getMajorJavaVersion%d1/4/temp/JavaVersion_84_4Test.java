package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class JavaVersion_84_4Test {

  @Test
    @Timeout(8000)
  void testGetMajorJavaVersion() {
    int majorVersion = JavaVersion.getMajorJavaVersion();
    assertTrue(majorVersion > 0, "Major Java version should be positive");
  }

  @Test
    @Timeout(8000)
  void testDetermineMajorJavaVersionUsingReflection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = JavaVersion.class.getDeclaredMethod("determineMajorJavaVersion");
    method.setAccessible(true);
    int majorVersion = (int) method.invoke(null);
    assertTrue(majorVersion > 0, "determineMajorJavaVersion should return positive int");
  }

  @Test
    @Timeout(8000)
  void testGetMajorJavaVersionWithString() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = JavaVersion.class.getDeclaredMethod("getMajorJavaVersion", String.class);
    method.setAccessible(true);
    assertEquals(8, (int) method.invoke(null, "1.8.0_45"));
    assertEquals(9, (int) method.invoke(null, "9"));
    assertEquals(11, (int) method.invoke(null, "11.0.2"));
    assertEquals(17, (int) method.invoke(null, "17"));
    assertEquals(-1, (int) method.invoke(null, (Object) null));
    assertEquals(-1, (int) method.invoke(null, ""));
    assertEquals(-1, (int) method.invoke(null, "abc"));
  }

  @Test
    @Timeout(8000)
  void testParseDottedUsingReflection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = JavaVersion.class.getDeclaredMethod("parseDotted", String.class);
    method.setAccessible(true);
    assertEquals(8, (int) method.invoke(null, "1.8.0_45"));
    assertEquals(11, (int) method.invoke(null, "11.0.2"));
    assertEquals(-1, (int) method.invoke(null, ""));
    assertEquals(-1, (int) method.invoke(null, "abc.def"));
    assertEquals(-1, (int) method.invoke(null, (Object) null));  // Added null test to prevent NPE
  }

  @Test
    @Timeout(8000)
  void testExtractBeginningIntUsingReflection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = JavaVersion.class.getDeclaredMethod("extractBeginningInt", String.class);
    method.setAccessible(true);
    assertEquals(1, (int) method.invoke(null, "1.8.0_45"));
    assertEquals(11, (int) method.invoke(null, "11.0.2"));
    assertEquals(-1, (int) method.invoke(null, ""));
    assertEquals(-1, (int) method.invoke(null, "abc123"));
    assertEquals(-1, (int) method.invoke(null, (Object) null));  // Added null test to prevent NPE
  }

  @Test
    @Timeout(8000)
  void testIsJava9OrLater() {
    boolean result = JavaVersion.isJava9OrLater();
    assertEquals(JavaVersion.getMajorJavaVersion() >= 9, result);
  }
}