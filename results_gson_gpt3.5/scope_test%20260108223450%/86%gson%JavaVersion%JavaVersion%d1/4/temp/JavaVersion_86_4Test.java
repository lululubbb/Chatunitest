package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

public class JavaVersion_86_4Test {

  @Test
    @Timeout(8000)
  void testDetermineMajorJavaVersion() throws Exception {
    Method method = JavaVersion.class.getDeclaredMethod("determineMajorJavaVersion");
    method.setAccessible(true);
    int version = (int) method.invoke(null);
    // The version should be positive (usually >= 8)
    assertTrue(version > 0);
  }

  @Test
    @Timeout(8000)
  void testGetMajorJavaVersion_String() throws Exception {
    Method method = JavaVersion.class.getDeclaredMethod("getMajorJavaVersion", String.class);
    method.setAccessible(true);

    assertEquals(8, ((Integer) method.invoke(null, "1.8.0_131")).intValue());
    assertEquals(9, ((Integer) method.invoke(null, "9")).intValue());
    assertEquals(11, ((Integer) method.invoke(null, "11")).intValue());
    assertEquals(17, ((Integer) method.invoke(null, "17.0.1")).intValue());
    assertEquals(0, ((Integer) method.invoke(null, "")).intValue());
    assertEquals(0, ((Integer) method.invoke(null, "abc")).intValue());
  }

  @Test
    @Timeout(8000)
  void testParseDotted() throws Exception {
    Method method = JavaVersion.class.getDeclaredMethod("parseDotted", String.class);
    method.setAccessible(true);

    assertEquals(8, ((Integer) method.invoke(null, "1.8.0_131")).intValue());
    assertEquals(0, ((Integer) method.invoke(null, "")).intValue());
    assertEquals(9, ((Integer) method.invoke(null, "9")).intValue());
    assertEquals(11, ((Integer) method.invoke(null, "11")).intValue());
    assertEquals(17, ((Integer) method.invoke(null, "17.0.1")).intValue());
    assertEquals(0, ((Integer) method.invoke(null, "abc")).intValue());
    assertEquals(0, ((Integer) method.invoke(null, "1.")).intValue());
    assertEquals(0, ((Integer) method.invoke(null, "1.a")).intValue());
  }

  @Test
    @Timeout(8000)
  void testExtractBeginningInt() throws Exception {
    Method method = JavaVersion.class.getDeclaredMethod("extractBeginningInt", String.class);
    method.setAccessible(true);

    assertEquals(1, ((Integer) method.invoke(null, "1.8.0_131")).intValue());
    assertEquals(9, ((Integer) method.invoke(null, "9")).intValue());
    assertEquals(11, ((Integer) method.invoke(null, "11")).intValue());
    assertEquals(17, ((Integer) method.invoke(null, "17.0.1")).intValue());
    assertEquals(0, ((Integer) method.invoke(null, "")).intValue());
    assertEquals(0, ((Integer) method.invoke(null, "abc")).intValue());
    assertEquals(0, ((Integer) method.invoke(null, ".123")).intValue());
  }

  @Test
    @Timeout(8000)
  void testGetMajorJavaVersion() {
    int version = JavaVersion.getMajorJavaVersion();
    assertTrue(version > 0);
  }

  @Test
    @Timeout(8000)
  void testIsJava9OrLater() {
    boolean is9OrLater = JavaVersion.isJava9OrLater();
    int version = JavaVersion.getMajorJavaVersion();
    assertEquals(version >= 9, is9OrLater);
  }
}