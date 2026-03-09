package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class JavaVersion_84_5Test {

  @Test
    @Timeout(8000)
  public void testGetMajorJavaVersion_returnsExpectedValue() {
    int majorVersion = JavaVersion.getMajorJavaVersion();
    // The major version should be a positive integer (e.g. 8, 11, 17, etc)
    assertTrue(majorVersion > 0);
  }

  @Test
    @Timeout(8000)
  public void testDetermineMajorJavaVersion_reflection() throws Exception {
    Method method = JavaVersion.class.getDeclaredMethod("determineMajorJavaVersion");
    method.setAccessible(true);
    int majorVersion = (int) method.invoke(null);
    assertTrue(majorVersion > 0);
  }

  @Test
    @Timeout(8000)
  public void testGetMajorJavaVersion_stringParameter() throws Exception {
    Method method = JavaVersion.class.getDeclaredMethod("getMajorJavaVersion", String.class);
    method.setAccessible(true);
    // Test typical Java version strings
    assertEquals(8, ((Integer) method.invoke(null, "1.8.0_181")).intValue());
    assertEquals(9, ((Integer) method.invoke(null, "9")).intValue());
    assertEquals(11, ((Integer) method.invoke(null, "11.0.2")).intValue());
    assertEquals(17, ((Integer) method.invoke(null, "17")).intValue());
  }

  @Test
    @Timeout(8000)
  public void testParseDotted() throws Exception {
    Method method = JavaVersion.class.getDeclaredMethod("parseDotted", String.class);
    method.setAccessible(true);
    assertEquals(8, ((Integer) method.invoke(null, "1.8.0_181")).intValue());
    assertEquals(9, ((Integer) method.invoke(null, "9")).intValue());
    assertEquals(11, ((Integer) method.invoke(null, "11.0.2")).intValue());
    assertEquals(17, ((Integer) method.invoke(null, "17")).intValue());
    assertEquals(1, ((Integer) method.invoke(null, "1")).intValue());
    assertEquals(-1, ((Integer) method.invoke(null, "")).intValue());
  }

  @Test
    @Timeout(8000)
  public void testExtractBeginningInt() throws Exception {
    Method method = JavaVersion.class.getDeclaredMethod("extractBeginningInt", String.class);
    method.setAccessible(true);
    assertEquals(1, ((Integer) method.invoke(null, "1.8.0_181")).intValue());
    assertEquals(9, ((Integer) method.invoke(null, "9")).intValue());
    assertEquals(11, ((Integer) method.invoke(null, "11.0.2")).intValue());
    assertEquals(17, ((Integer) method.invoke(null, "17")).intValue());
    assertEquals(-1, ((Integer) method.invoke(null, "")).intValue());
    assertEquals(-1, ((Integer) method.invoke(null, "abc")).intValue());
  }

  @Test
    @Timeout(8000)
  public void testIsJava9OrLater() {
    boolean isJava9OrLater = JavaVersion.isJava9OrLater();
    // This depends on current runtime version but should be consistent with majorJavaVersion
    assertEquals(JavaVersion.getMajorJavaVersion() >= 9, isJava9OrLater);
  }

  @Test
    @Timeout(8000)
  public void testPrivateConstructor() throws Exception {
    var constructor = JavaVersion.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    Object instance = constructor.newInstance();
    assertNotNull(instance);
  }
}