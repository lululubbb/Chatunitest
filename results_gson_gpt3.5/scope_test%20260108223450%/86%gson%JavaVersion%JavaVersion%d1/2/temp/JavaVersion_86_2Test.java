package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;

public class JavaVersion_86_2Test {

  @Test
    @Timeout(8000)
  public void testDetermineMajorJavaVersion() throws Exception {
    Method method = JavaVersion.class.getDeclaredMethod("determineMajorJavaVersion");
    method.setAccessible(true);
    int version = (int) method.invoke(null);
    assertTrue(version > 0);
  }

  @Test
    @Timeout(8000)
  public void testGetMajorJavaVersion_String() throws Exception {
    Method method = JavaVersion.class.getDeclaredMethod("getMajorJavaVersion", String.class);
    method.setAccessible(true);

    assertEquals(8, ((Integer) method.invoke(null, "1.8.0_231")).intValue());
    assertEquals(9, ((Integer) method.invoke(null, "9")).intValue());
    assertEquals(11, ((Integer) method.invoke(null, "11.0.2")).intValue());
    assertEquals(17, ((Integer) method.invoke(null, "17.0.1")).intValue());
    assertEquals(6, ((Integer) method.invoke(null, "")).intValue());
    assertEquals(6, ((Integer) method.invoke(null, "abc")).intValue());
  }

  @Test
    @Timeout(8000)
  public void testParseDotted() throws Exception {
    Method method = JavaVersion.class.getDeclaredMethod("parseDotted", String.class);
    method.setAccessible(true);

    assertEquals(8, ((Integer) method.invoke(null, "1.8.0_231")).intValue());
    assertEquals(11, ((Integer) method.invoke(null, "11.0.2")).intValue());
    assertEquals(-1, ((Integer) method.invoke(null, "abc.def")).intValue());
    assertEquals(-1, ((Integer) method.invoke(null, "")).intValue());
  }

  @Test
    @Timeout(8000)
  public void testExtractBeginningInt() throws Exception {
    Method method = JavaVersion.class.getDeclaredMethod("extractBeginningInt", String.class);
    method.setAccessible(true);

    assertEquals(1, ((Integer) method.invoke(null, "1.8.0_231")).intValue());
    assertEquals(9, ((Integer) method.invoke(null, "9")).intValue());
    assertEquals(11, ((Integer) method.invoke(null, "11abc")).intValue());
    assertEquals(-1, ((Integer) method.invoke(null, "abc11")).intValue());
    assertEquals(-1, ((Integer) method.invoke(null, "")).intValue());
  }

  @Test
    @Timeout(8000)
  public void testGetMajorJavaVersion_NoArgs() {
    int version = JavaVersion.getMajorJavaVersion();
    assertTrue(version > 0);
  }

  @Test
    @Timeout(8000)
  public void testIsJava9OrLater() {
    int version = JavaVersion.getMajorJavaVersion();
    boolean expected = version >= 9;
    assertEquals(expected, JavaVersion.isJava9OrLater());
  }
}