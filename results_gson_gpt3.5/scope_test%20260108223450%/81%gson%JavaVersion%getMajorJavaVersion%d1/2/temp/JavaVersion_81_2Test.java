package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

class JavaVersion_81_2Test {

  @Test
    @Timeout(8000)
  void testGetMajorJavaVersion_withValidDottedVersion() throws Exception {
    Method method = JavaVersion.class.getDeclaredMethod("getMajorJavaVersion", String.class);
    method.setAccessible(true);
    // "1.8.0_181" should parse to 8 by parseDotted
    int result = (int) method.invoke(null, "1.8.0_181");
    assertEquals(8, result);
  }

  @Test
    @Timeout(8000)
  void testGetMajorJavaVersion_withValidBeginningIntVersion() throws Exception {
    Method method = JavaVersion.class.getDeclaredMethod("getMajorJavaVersion", String.class);
    method.setAccessible(true);
    // "9" should fail parseDotted (return -1), but extractBeginningInt returns 9
    int result = (int) method.invoke(null, "9");
    assertEquals(9, result);
  }

  @Test
    @Timeout(8000)
  void testGetMajorJavaVersion_withInvalidVersion() throws Exception {
    Method method = JavaVersion.class.getDeclaredMethod("getMajorJavaVersion", String.class);
    method.setAccessible(true);
    // "invalid" returns -1 from both parseDotted and extractBeginningInt, so default 6
    int result = (int) method.invoke(null, "invalid");
    assertEquals(6, result);
  }

}