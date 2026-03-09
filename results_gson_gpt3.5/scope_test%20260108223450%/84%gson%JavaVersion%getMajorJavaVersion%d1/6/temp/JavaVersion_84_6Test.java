package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class JavaVersion_84_6Test {

  private static Method determineMajorJavaVersionMethod;
  private static Method getMajorJavaVersionStringMethod;
  private static Method parseDottedMethod;
  private static Method extractBeginningIntMethod;

  @BeforeAll
  public static void setUp() throws NoSuchMethodException {
    determineMajorJavaVersionMethod = JavaVersion.class.getDeclaredMethod("determineMajorJavaVersion");
    determineMajorJavaVersionMethod.setAccessible(true);

    getMajorJavaVersionStringMethod = JavaVersion.class.getDeclaredMethod("getMajorJavaVersion", String.class);
    getMajorJavaVersionStringMethod.setAccessible(true);

    parseDottedMethod = JavaVersion.class.getDeclaredMethod("parseDotted", String.class);
    parseDottedMethod.setAccessible(true);

    extractBeginningIntMethod = JavaVersion.class.getDeclaredMethod("extractBeginningInt", String.class);
    extractBeginningIntMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  public void testGetMajorJavaVersion_staticField() {
    int majorVersion = JavaVersion.getMajorJavaVersion();
    assertTrue(majorVersion > 0);
  }

  @Test
    @Timeout(8000)
  public void testDetermineMajorJavaVersion() throws InvocationTargetException, IllegalAccessException {
    int version = (int) determineMajorJavaVersionMethod.invoke(null);
    assertTrue(version > 0);
  }

  @Test
    @Timeout(8000)
  public void testGetMajorJavaVersion_String() throws InvocationTargetException, IllegalAccessException {
    assertEquals(8, (int) getMajorJavaVersionStringMethod.invoke(null, "1.8.0_151"));
    assertEquals(9, (int) getMajorJavaVersionStringMethod.invoke(null, "9"));
    assertEquals(11, (int) getMajorJavaVersionStringMethod.invoke(null, "11.0.2"));
    assertEquals(17, (int) getMajorJavaVersionStringMethod.invoke(null, "17"));
    assertEquals(1, (int) getMajorJavaVersionStringMethod.invoke(null, "1"));
  }

  @Test
    @Timeout(8000)
  public void testParseDotted() throws InvocationTargetException, IllegalAccessException {
    assertEquals(8, (int) parseDottedMethod.invoke(null, "1.8"));
    assertEquals(9, (int) parseDottedMethod.invoke(null, "9.0"));
    assertEquals(11, (int) parseDottedMethod.invoke(null, "11.0.2"));
    assertEquals(1, (int) parseDottedMethod.invoke(null, "1"));
    assertEquals(0, (int) parseDottedMethod.invoke(null, (Object) null));
    assertEquals(0, (int) parseDottedMethod.invoke(null, ""));
  }

  @Test
    @Timeout(8000)
  public void testExtractBeginningInt() throws InvocationTargetException, IllegalAccessException {
    assertEquals(1, (int) extractBeginningIntMethod.invoke(null, "1.8.0_151"));
    assertEquals(9, (int) extractBeginningIntMethod.invoke(null, "9"));
    assertEquals(11, (int) extractBeginningIntMethod.invoke(null, "11.0.2"));
    assertEquals(0, (int) extractBeginningIntMethod.invoke(null, "abc"));
    assertEquals(0, (int) extractBeginningIntMethod.invoke(null, (Object) null));
    assertEquals(0, (int) extractBeginningIntMethod.invoke(null, ""));
  }

  @Test
    @Timeout(8000)
  public void testIsJava9OrLater() {
    int major = JavaVersion.getMajorJavaVersion();
    boolean is9OrLater = JavaVersion.isJava9OrLater();
    assertEquals(major >= 9, is9OrLater);
  }
}