package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class JavaVersion_86_1Test {

  @Test
    @Timeout(8000)
  public void testGetMajorJavaVersion_withVariousVersions() {
    assertEquals(8, JavaVersion.getMajorJavaVersion("1.8.0_151"));
    assertEquals(9, JavaVersion.getMajorJavaVersion("9"));
    assertEquals(11, JavaVersion.getMajorJavaVersion("11.0.2"));
    assertEquals(17, JavaVersion.getMajorJavaVersion("17"));
    assertEquals(-1, JavaVersion.getMajorJavaVersion("invalid"));
    assertEquals(-1, JavaVersion.getMajorJavaVersion(""));
    assertEquals(-1, JavaVersion.getMajorJavaVersion(null));
  }

  @Test
    @Timeout(8000)
  public void testParseDotted_reflection() throws Exception {
    Method parseDotted = JavaVersion.class.getDeclaredMethod("parseDotted", String.class);
    parseDotted.setAccessible(true);

    assertEquals(8, (int) parseDotted.invoke(null, "1.8.0_151"));
    assertEquals(11, (int) parseDotted.invoke(null, "11.0.2"));
    assertEquals(-1, (int) parseDotted.invoke(null, "invalid"));
    assertEquals(-1, (int) parseDotted.invoke(null, ""));
    assertEquals(-1, (int) parseDotted.invoke(null, (Object) null));
  }

  @Test
    @Timeout(8000)
  public void testExtractBeginningInt_reflection() throws Exception {
    Method extractBeginningInt = JavaVersion.class.getDeclaredMethod("extractBeginningInt", String.class);
    extractBeginningInt.setAccessible(true);

    assertEquals(1, (int) extractBeginningInt.invoke(null, "1.8.0_151"));
    assertEquals(11, (int) extractBeginningInt.invoke(null, "11.0.2"));
    assertEquals(-1, (int) extractBeginningInt.invoke(null, "abc"));
    assertEquals(-1, (int) extractBeginningInt.invoke(null, ""));
    assertEquals(-1, (int) extractBeginningInt.invoke(null, (Object) null));
  }

  @Test
    @Timeout(8000)
  public void testDetermineMajorJavaVersion_reflection() throws Exception {
    Method determineMajorJavaVersion = JavaVersion.class.getDeclaredMethod("determineMajorJavaVersion");
    determineMajorJavaVersion.setAccessible(true);

    // Since mocking System.getProperty("java.version") is not possible,
    // we call the method directly and assert the returned value is >= 0.
    // This avoids the Mockito static mock issue on System class.

    int v = (int) determineMajorJavaVersion.invoke(null);
    assertTrue(v >= 0);
  }

  @Test
    @Timeout(8000)
  public void testGetMajorJavaVersion_staticField() {
    // majorJavaVersion is private static final, so test getMajorJavaVersion()
    int version = JavaVersion.getMajorJavaVersion();
    assertTrue(version >= 0);
  }

  @Test
    @Timeout(8000)
  public void testIsJava9OrLater() {
    // We test the method returns true or false consistent with getMajorJavaVersion()
    int version = JavaVersion.getMajorJavaVersion();
    boolean expected = version >= 9;
    assertEquals(expected, JavaVersion.isJava9OrLater());
  }
}