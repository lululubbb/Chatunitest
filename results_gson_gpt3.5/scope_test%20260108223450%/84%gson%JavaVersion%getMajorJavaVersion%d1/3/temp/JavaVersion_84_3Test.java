package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

public class JavaVersion_84_3Test {

  @Test
    @Timeout(8000)
  public void testGetMajorJavaVersion_returnsStaticField() {
    int majorVersion = JavaVersion.getMajorJavaVersion();
    // majorJavaVersion is initialized by determineMajorJavaVersion, which is static final
    // We expect majorVersion to be positive (Java versions are positive)
    assertTrue(majorVersion > 0);
  }

  @Test
    @Timeout(8000)
  public void testDetermineMajorJavaVersion_viaReflection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = JavaVersion.class.getDeclaredMethod("determineMajorJavaVersion");
    method.setAccessible(true);
    int version = (int) method.invoke(null);
    assertTrue(version > 0);
  }

  @Test
    @Timeout(8000)
  public void testGetMajorJavaVersion_StringParameter_viaReflection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = JavaVersion.class.getDeclaredMethod("getMajorJavaVersion", String.class);
    method.setAccessible(true);
    assertEquals(8, (int) method.invoke(null, "1.8.0_131"));
    assertEquals(9, (int) method.invoke(null, "9"));
    assertEquals(11, (int) method.invoke(null, "11.0.2"));
    assertEquals(17, (int) method.invoke(null, "17"));
    assertEquals(-1, (int) method.invoke(null, (Object) null));
    assertEquals(-1, (int) method.invoke(null, ""));
    assertEquals(-1, (int) method.invoke(null, "abc"));
  }

  @Test
    @Timeout(8000)
  public void testParseDotted_viaReflection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = JavaVersion.class.getDeclaredMethod("parseDotted", String.class);
    method.setAccessible(true);
    assertEquals(8, (int) method.invoke(null, "1.8.0_131"));
    assertEquals(11, (int) method.invoke(null, "11.0.2"));
    assertEquals(-1, (int) method.invoke(null, ""));
    assertEquals(-1, (int) method.invoke(null, "abc"));
  }

  @Test
    @Timeout(8000)
  public void testExtractBeginningInt_viaReflection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = JavaVersion.class.getDeclaredMethod("extractBeginningInt", String.class);
    method.setAccessible(true);
    assertEquals(1, (int) method.invoke(null, "1.8.0_131"));
    assertEquals(11, (int) method.invoke(null, "11.0.2"));
    assertEquals(-1, (int) method.invoke(null, "abc"));
    assertEquals(-1, (int) method.invoke(null, ""));
  }

  @Test
    @Timeout(8000)
  public void testIsJava9OrLater() {
    boolean result = JavaVersion.isJava9OrLater();
    // isJava9OrLater returns true if majorJavaVersion >= 9
    if (JavaVersion.getMajorJavaVersion() >= 9) {
      assertTrue(result);
    } else {
      assertFalse(result);
    }
  }

  @Test
    @Timeout(8000)
  public void testPrivateConstructor() throws Exception {
    var constructor = JavaVersion.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    Object instance = constructor.newInstance();
    assertNotNull(instance);
    assertTrue(instance instanceof JavaVersion);
  }
}