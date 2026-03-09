package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class JavaVersion_85_5Test {

  @Test
    @Timeout(8000)
  public void testIsJava9OrLater_whenVersionIs9OrHigher() throws Exception {
    try (MockedStatic<JavaVersion> mocked = Mockito.mockStatic(JavaVersion.class, Mockito.CALLS_REAL_METHODS)) {
      setMajorJavaVersion(11);
      assertTrue(JavaVersion.isJava9OrLater());
    }
  }

  @Test
    @Timeout(8000)
  public void testIsJava9OrLater_whenVersionIsLowerThan9() throws Exception {
    try (MockedStatic<JavaVersion> mocked = Mockito.mockStatic(JavaVersion.class, Mockito.CALLS_REAL_METHODS)) {
      setMajorJavaVersion(8);
      assertFalse(JavaVersion.isJava9OrLater());
    }
  }

  @Test
    @Timeout(8000)
  public void testDetermineMajorJavaVersion() throws Exception {
    Method method = JavaVersion.class.getDeclaredMethod("determineMajorJavaVersion");
    method.setAccessible(true);

    String originalVersion = System.getProperty("java.version");
    try {
      System.setProperty("java.version", "11.0.2");
      int version = (int) method.invoke(null);
      assertEquals(11, version);

      System.setProperty("java.version", "1.8.0_201");
      version = (int) method.invoke(null);
      assertEquals(8, version);

      System.setProperty("java.version", "9");
      version = (int) method.invoke(null);
      assertEquals(9, version);

      System.setProperty("java.version", "10.0.1");
      version = (int) method.invoke(null);
      assertEquals(10, version);

    } finally {
      System.setProperty("java.version", originalVersion);
    }
  }

  @Test
    @Timeout(8000)
  public void testGetMajorJavaVersion_StringVariants() throws Exception {
    Method method = JavaVersion.class.getDeclaredMethod("getMajorJavaVersion", String.class);
    method.setAccessible(true);

    assertEquals(8, ((Integer) method.invoke(null, "1.8.0_201")).intValue());
    assertEquals(9, ((Integer) method.invoke(null, "9")).intValue());
    assertEquals(10, ((Integer) method.invoke(null, "10.0.1")).intValue());
    assertEquals(11, ((Integer) method.invoke(null, "11.0.2")).intValue());
    assertEquals(0, ((Integer) method.invoke(null, "x.y.z")).intValue());
    assertEquals(0, ((Integer) method.invoke(null, "")).intValue());
    assertEquals(0, ((Integer) method.invoke(null, (Object) null)).intValue());
  }

  @Test
    @Timeout(8000)
  public void testParseDotted() throws Exception {
    Method method = JavaVersion.class.getDeclaredMethod("parseDotted", String.class);
    method.setAccessible(true);

    assertEquals(8, ((Integer) method.invoke(null, "1.8.0_201")).intValue());
    assertEquals(9, ((Integer) method.invoke(null, "9")).intValue());
    assertEquals(10, ((Integer) method.invoke(null, "10.0.1")).intValue());
    assertEquals(11, ((Integer) method.invoke(null, "11.0.2")).intValue());
    assertEquals(0, ((Integer) method.invoke(null, "x.y.z")).intValue());
    assertEquals(0, ((Integer) method.invoke(null, "")).intValue());
    assertEquals(0, ((Integer) method.invoke(null, (Object) null)).intValue());
  }

  @Test
    @Timeout(8000)
  public void testExtractBeginningInt() throws Exception {
    Method method = JavaVersion.class.getDeclaredMethod("extractBeginningInt", String.class);
    method.setAccessible(true);

    assertEquals(1, ((Integer) method.invoke(null, "1.8.0_201")).intValue());
    assertEquals(9, ((Integer) method.invoke(null, "9")).intValue());
    assertEquals(10, ((Integer) method.invoke(null, "10.0.1")).intValue());
    assertEquals(11, ((Integer) method.invoke(null, "11.0.2")).intValue());
    assertEquals(0, ((Integer) method.invoke(null, "x.y.z")).intValue());
    assertEquals(0, ((Integer) method.invoke(null, "")).intValue());
    assertEquals(0, ((Integer) method.invoke(null, (Object) null)).intValue());
  }

  @Test
    @Timeout(8000)
  public void testGetMajorJavaVersion_noArgs() throws Exception {
    int version = JavaVersion.getMajorJavaVersion();
    assertTrue(version > 0);
  }

  private void setMajorJavaVersion(int value) throws Exception {
    Field field = JavaVersion.class.getDeclaredField("majorJavaVersion");
    field.setAccessible(true);

    // Remove final modifier if present
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

    // Set the field value
    field.set(null, value);
  }
}