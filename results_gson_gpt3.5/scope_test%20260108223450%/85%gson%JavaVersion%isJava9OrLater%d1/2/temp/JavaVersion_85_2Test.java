package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

class JavaVersion_85_2Test {

  @Test
    @Timeout(8000)
  void testIsJava9OrLater_whenVersionIsLessThan9() throws Exception {
    try (MockedStatic<JavaVersion> mockedStatic = Mockito.mockStatic(JavaVersion.class, Mockito.CALLS_REAL_METHODS)) {
      setMajorJavaVersion(8);
      assertFalse(JavaVersion.isJava9OrLater());
    }
  }

  @Test
    @Timeout(8000)
  void testIsJava9OrLater_whenVersionIsEqualTo9() throws Exception {
    try (MockedStatic<JavaVersion> mockedStatic = Mockito.mockStatic(JavaVersion.class, Mockito.CALLS_REAL_METHODS)) {
      setMajorJavaVersion(9);
      assertTrue(JavaVersion.isJava9OrLater());
    }
  }

  @Test
    @Timeout(8000)
  void testIsJava9OrLater_whenVersionIsGreaterThan9() throws Exception {
    try (MockedStatic<JavaVersion> mockedStatic = Mockito.mockStatic(JavaVersion.class, Mockito.CALLS_REAL_METHODS)) {
      setMajorJavaVersion(11);
      assertTrue(JavaVersion.isJava9OrLater());
    }
  }

  @Test
    @Timeout(8000)
  void testDetermineMajorJavaVersion_invocation() throws Exception {
    Method method = JavaVersion.class.getDeclaredMethod("determineMajorJavaVersion");
    method.setAccessible(true);
    int version = (int) method.invoke(null);
    // The version should be positive
    assertTrue(version > 0);
  }

  @Test
    @Timeout(8000)
  void testGetMajorJavaVersion_string() throws Exception {
    Method method = JavaVersion.class.getDeclaredMethod("getMajorJavaVersion", String.class);
    method.setAccessible(true);

    assertEquals(8, ((Integer) method.invoke(null, "1.8.0_231")).intValue());
    assertEquals(9, ((Integer) method.invoke(null, "9")).intValue());
    assertEquals(11, ((Integer) method.invoke(null, "11.0.2")).intValue());
    assertEquals(-1, ((Integer) method.invoke(null, "abc")).intValue());
  }

  @Test
    @Timeout(8000)
  void testParseDotted() throws Exception {
    Method method = JavaVersion.class.getDeclaredMethod("parseDotted", String.class);
    method.setAccessible(true);

    assertEquals(8, ((Integer) method.invoke(null, "1.8.0_231")).intValue());
    assertEquals(11, ((Integer) method.invoke(null, "11.0.2")).intValue());
    assertEquals(-1, ((Integer) method.invoke(null, "abc.def")).intValue());
  }

  @Test
    @Timeout(8000)
  void testExtractBeginningInt() throws Exception {
    Method method = JavaVersion.class.getDeclaredMethod("extractBeginningInt", String.class);
    method.setAccessible(true);

    assertEquals(1, ((Integer) method.invoke(null, "1.8.0_231")).intValue());
    assertEquals(11, ((Integer) method.invoke(null, "11.0.2")).intValue());
    assertEquals(-1, ((Integer) method.invoke(null, "abc123")).intValue());
    assertEquals(-1, ((Integer) method.invoke(null, "")).intValue());
  }

  @Test
    @Timeout(8000)
  void testGetMajorJavaVersion_noArgs() {
    int version = JavaVersion.getMajorJavaVersion();
    assertTrue(version > 0);
  }

  private void setMajorJavaVersion(int version) throws Exception {
    Field field = JavaVersion.class.getDeclaredField("majorJavaVersion");
    field.setAccessible(true);

    // Remove final modifier using reflection
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

    // Reset the cached majorJavaVersion by forcibly setting the field value via reflection:
    field.set(null, version);
  }
}