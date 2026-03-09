package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class JavaVersion_81_1Test {

  @Test
    @Timeout(8000)
  void testGetMajorJavaVersion_withParseDottedSuccess() throws Exception {
    Method method = JavaVersion.class.getDeclaredMethod("getMajorJavaVersion", String.class);
    method.setAccessible(true);

    // parseDotted returns a valid version number, e.g. 11
    int result = (int) method.invoke(null, "11.0.4");
    assertEquals(11, result);
  }

  @Test
    @Timeout(8000)
  void testGetMajorJavaVersion_withParseDottedFailAndExtractBeginningIntSuccess() throws Exception {
    Method method = JavaVersion.class.getDeclaredMethod("getMajorJavaVersion", String.class);
    method.setAccessible(true);

    // parseDotted returns -1, extractBeginningInt returns 8
    // To simulate this, we use a string that parseDotted fails but extractBeginningInt succeeds
    int result = (int) method.invoke(null, "8u202");
    assertEquals(8, result);
  }

  @Test
    @Timeout(8000)
  void testGetMajorJavaVersion_withBothParseDottedAndExtractBeginningIntFail() throws Exception {
    Method method = JavaVersion.class.getDeclaredMethod("getMajorJavaVersion", String.class);
    method.setAccessible(true);

    // parseDotted and extractBeginningInt both return -1, e.g. empty or invalid string
    int result = (int) method.invoke(null, "invalid_version_string");
    assertEquals(6, result);
  }
}