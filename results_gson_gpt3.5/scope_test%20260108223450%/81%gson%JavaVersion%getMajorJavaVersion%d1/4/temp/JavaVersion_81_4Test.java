package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class JavaVersion_81_4Test {

  @Test
    @Timeout(8000)
  public void testGetMajorJavaVersion_parseDottedSuccess() throws Exception {
    // parseDotted returns a valid version (simulate by calling getMajorJavaVersion with a dotted version string)
    int version = invokeGetMajorJavaVersion("1.8.0_181");
    assertEquals(8, version); // parseDotted("1.8.0_181") should parse major=8 (per method logic)
  }

  @Test
    @Timeout(8000)
  public void testGetMajorJavaVersion_parseDottedFail_extractBeginningIntSuccess() throws Exception {
    // Use a string that parseDotted returns -1 but extractBeginningInt returns a valid version
    // e.g. "11" (no dots), parseDotted returns -1, extractBeginningInt returns 11
    int version = invokeGetMajorJavaVersion("11");
    assertEquals(11, version);
  }

  @Test
    @Timeout(8000)
  public void testGetMajorJavaVersion_bothFail_returns6() throws Exception {
    // Use a string that both parseDotted and extractBeginningInt return -1
    int version = invokeGetMajorJavaVersion("invalid.version.string");
    assertEquals(6, version);
  }

  private int invokeGetMajorJavaVersion(String javaVersion) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = JavaVersion.class.getDeclaredMethod("getMajorJavaVersion", String.class);
    method.setAccessible(true);
    return (int) method.invoke(null, javaVersion);
  }
}