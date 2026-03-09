package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class JavaVersion_84_2Test {

  @Test
    @Timeout(8000)
  public void testGetMajorJavaVersion_returnsStaticField() {
    int majorVersion = JavaVersion.getMajorJavaVersion();
    // The major version should be positive (usually 8 or higher)
    assertTrue(majorVersion > 0);
  }

  @Test
    @Timeout(8000)
  public void testGetMajorJavaVersion_StringInput_variousVersions() throws Exception {
    Method getMajorJavaVersionString = JavaVersion.class.getDeclaredMethod("getMajorJavaVersion", String.class);
    getMajorJavaVersionString.setAccessible(true);

    assertEquals(8, ((Integer) getMajorJavaVersionString.invoke(null, "1.8.0_151")).intValue());
    assertEquals(9, ((Integer) getMajorJavaVersionString.invoke(null, "9")).intValue());
    assertEquals(11, ((Integer) getMajorJavaVersionString.invoke(null, "11.0.2")).intValue());
    assertEquals(17, ((Integer) getMajorJavaVersionString.invoke(null, "17.0.1")).intValue());
    assertEquals(-1, ((Integer) getMajorJavaVersionString.invoke(null, "abc")).intValue());
  }

  @Test
    @Timeout(8000)
  public void testParseDotted_variousInputs() throws Exception {
    Method parseDotted = JavaVersion.class.getDeclaredMethod("parseDotted", String.class);
    parseDotted.setAccessible(true);

    assertEquals(8, ((Integer) parseDotted.invoke(null, "1.8.0_151")).intValue());
    assertEquals(9, ((Integer) parseDotted.invoke(null, "9")).intValue());
    assertEquals(11, ((Integer) parseDotted.invoke(null, "11.0.2")).intValue());
    assertEquals(17, ((Integer) parseDotted.invoke(null, "17.0.1")).intValue());
    assertEquals(-1, ((Integer) parseDotted.invoke(null, "abc")).intValue());
  }

  @Test
    @Timeout(8000)
  public void testExtractBeginningInt_variousInputs() throws Exception {
    Method extractBeginningInt = JavaVersion.class.getDeclaredMethod("extractBeginningInt", String.class);
    extractBeginningInt.setAccessible(true);

    assertEquals(1, ((Integer) extractBeginningInt.invoke(null, "1.8.0_151")).intValue());
    assertEquals(9, ((Integer) extractBeginningInt.invoke(null, "9")).intValue());
    assertEquals(11, ((Integer) extractBeginningInt.invoke(null, "11.0.2")).intValue());
    assertEquals(17, ((Integer) extractBeginningInt.invoke(null, "17.0.1")).intValue());
    assertEquals(-1, ((Integer) extractBeginningInt.invoke(null, "abc")).intValue());
    assertEquals(-1, ((Integer) extractBeginningInt.invoke(null, "")).intValue());
    // Pass null explicitly as (String) null to avoid NullPointerException in reflection
    assertEquals(-1, ((Integer) extractBeginningInt.invoke(null, (String) null)).intValue());
  }

  @Test
    @Timeout(8000)
  public void testIsJava9OrLater() {
    int major = JavaVersion.getMajorJavaVersion();
    boolean is9OrLater = JavaVersion.isJava9OrLater();
    assertEquals(major >= 9, is9OrLater);
  }
}