package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class JavaVersion_81_6Test {

  @Test
    @Timeout(8000)
  void testGetMajorJavaVersion_withValidDottedVersion() throws Exception {
    // Using reflection to access private static method parseDotted to verify behavior
    Method parseDotted = JavaVersion.class.getDeclaredMethod("parseDotted", String.class);
    parseDotted.setAccessible(true);
    int parsed = (int) parseDotted.invoke(null, "1.8.0_181");
    assertEquals(8, parsed);

    int result = invokeGetMajorJavaVersion("1.8.0_181");
    assertEquals(8, result);
  }

  @Test
    @Timeout(8000)
  void testGetMajorJavaVersion_withValidBeginningIntVersion() throws Exception {
    // parseDotted returns -1, so extractBeginningInt is used
    Method parseDotted = JavaVersion.class.getDeclaredMethod("parseDotted", String.class);
    parseDotted.setAccessible(true);
    int parseResult = (int) parseDotted.invoke(null, "9");
    // If parseDotted returns -1, fallback to extractBeginningInt
    if (parseResult != -1) {
      // To force fallback, we test with a string that parseDotted likely returns -1
      int fallbackResult = invokeGetMajorJavaVersion("9");
      assertEquals(9, fallbackResult);
    } else {
      int fallbackResult = invokeGetMajorJavaVersion("9");
      assertEquals(9, fallbackResult);
    }
  }

  @Test
    @Timeout(8000)
  void testGetMajorJavaVersion_withInvalidVersion() throws Exception {
    // Both parseDotted and extractBeginningInt return -1, expect default 6
    int result = invokeGetMajorJavaVersion("invalid.version.string");
    assertEquals(6, result);
  }

  private int invokeGetMajorJavaVersion(String version) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = JavaVersion.class.getDeclaredMethod("getMajorJavaVersion", String.class);
    method.setAccessible(true);
    return (int) method.invoke(null, version);
  }
}