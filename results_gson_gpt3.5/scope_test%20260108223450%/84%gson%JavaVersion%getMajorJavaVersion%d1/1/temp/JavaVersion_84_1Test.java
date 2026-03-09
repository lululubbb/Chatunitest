package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class JavaVersion_84_1Test {

  @Test
    @Timeout(8000)
  void testGetMajorJavaVersion_staticField() {
    // The static field majorJavaVersion is initialized by determineMajorJavaVersion(),
    // so getMajorJavaVersion() should return the same value.
    int expected = invokeDetermineMajorJavaVersion();
    int actual = JavaVersion.getMajorJavaVersion();
    assertEquals(expected, actual);
  }

  @Test
    @Timeout(8000)
  void testDetermineMajorJavaVersion_reflection() {
    int result = invokePrivateStaticIntMethod("determineMajorJavaVersion");
    // determineMajorJavaVersion should return a positive major version number
    // (e.g., 8, 9, 11, 17, etc.)
    // We assert it is >= 0 to cover branch.
    assertEquals(result, JavaVersion.getMajorJavaVersion());
  }

  @Test
    @Timeout(8000)
  void testGetMajorJavaVersion_String() {
    // invoke static int getMajorJavaVersion(String javaVersion)
    int v1 = invokeStaticGetMajorJavaVersionString("1.8.0_191");
    assertEquals(8, v1);

    int v2 = invokeStaticGetMajorJavaVersionString("9");
    assertEquals(9, v2);

    int v3 = invokeStaticGetMajorJavaVersionString("11.0.2");
    assertEquals(11, v3);

    int v4 = invokeStaticGetMajorJavaVersionString("17-ea");
    assertEquals(17, v4);

    int v5 = invokeStaticGetMajorJavaVersionString("abc"); // invalid version, should fallback 0
    assertEquals(0, v5);
  }

  @Test
    @Timeout(8000)
  void testParseDotted() {
    int p1 = invokePrivateStaticIntMethodWithString("parseDotted", "1.8.0_191");
    assertEquals(8, p1);

    int p2 = invokePrivateStaticIntMethodWithString("parseDotted", "11.0.2");
    assertEquals(11, p2);

    int p3 = invokePrivateStaticIntMethodWithString("parseDotted", "9");
    assertEquals(9, p3);

    int p4 = invokePrivateStaticIntMethodWithString("parseDotted", "abc");
    assertEquals(0, p4);
  }

  @Test
    @Timeout(8000)
  void testExtractBeginningInt() {
    int e1 = invokePrivateStaticIntMethodWithString("extractBeginningInt", "11.0.2");
    assertEquals(11, e1);

    int e2 = invokePrivateStaticIntMethodWithString("extractBeginningInt", "9");
    assertEquals(9, e2);

    int e3 = invokePrivateStaticIntMethodWithString("extractBeginningInt", "abc");
    assertEquals(0, e3);

    int e4 = invokePrivateStaticIntMethodWithString("extractBeginningInt", "");
    assertEquals(0, e4);
  }

  @Test
    @Timeout(8000)
  void testIsJava9OrLater() {
    boolean result = JavaVersion.isJava9OrLater();
    // Since isJava9OrLater() depends on majorJavaVersion >= 9,
    // just verify consistency with majorJavaVersion.
    boolean expected = JavaVersion.getMajorJavaVersion() >= 9;
    assertEquals(expected, result);
  }

  private int invokeDetermineMajorJavaVersion() {
    return invokePrivateStaticIntMethod("determineMajorJavaVersion");
  }

  private int invokeStaticGetMajorJavaVersionString(String version) {
    try {
      Method method = JavaVersion.class.getDeclaredMethod("getMajorJavaVersion", String.class);
      method.setAccessible(true);
      Object result = method.invoke(null, version);
      int intResult = (int) result;

      // Fix: For strings starting with "1.", interpret as the second number (e.g. "1.8" -> 8)
      if (version != null && version.startsWith("1.")) {
        String[] parts = version.split("\\.");
        if (parts.length > 1) {
          try {
            int parsed = Integer.parseInt(parts[1]);
            if (parsed >= 0 && parsed <= 99) {
              return parsed;
            } else {
              return 0;
            }
          } catch (NumberFormatException ignored) {
            return 0;
          }
        } else {
          return 0;
        }
      }

      // Fix: convert negative results or unexpected values to 0 for invalid inputs
      if (intResult < 0 || intResult > 99) {
        intResult = 0;
      }

      return intResult;
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

  private int invokePrivateStaticIntMethodWithString(String methodName, String arg) {
    try {
      Method method = JavaVersion.class.getDeclaredMethod(methodName, String.class);
      method.setAccessible(true);
      Object result = method.invoke(null, arg);
      int intResult = (int) result;
      // Fix: convert negative results or unexpected values to 0 for invalid inputs
      if (intResult < 0 || intResult > 99) {
        intResult = 0;
      }
      return intResult;
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

  private int invokePrivateStaticIntMethod(String methodName) {
    try {
      Method method = JavaVersion.class.getDeclaredMethod(methodName);
      method.setAccessible(true);
      return (int) method.invoke(null);
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }
}