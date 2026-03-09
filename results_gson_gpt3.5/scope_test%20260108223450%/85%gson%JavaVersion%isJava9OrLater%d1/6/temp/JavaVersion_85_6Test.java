package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class JavaVersion_85_6Test {

  @Test
    @Timeout(8000)
  public void testIsJava9OrLater_whenVersionIs9OrMore() throws Exception {
    try (MockedStatic<JavaVersion> mocked = Mockito.mockStatic(JavaVersion.class, Mockito.CALLS_REAL_METHODS)) {
      setMajorJavaVersion(11);
      assertTrue(JavaVersion.isJava9OrLater());
    }
  }

  @Test
    @Timeout(8000)
  public void testIsJava9OrLater_whenVersionIsLessThan9() throws Exception {
    try (MockedStatic<JavaVersion> mocked = Mockito.mockStatic(JavaVersion.class, Mockito.CALLS_REAL_METHODS)) {
      setMajorJavaVersion(8);
      assertFalse(JavaVersion.isJava9OrLater());
    }
  }

  @Test
    @Timeout(8000)
  public void testDetermineMajorJavaVersion_invocation() throws Exception {
    Method method = JavaVersion.class.getDeclaredMethod("determineMajorJavaVersion");
    method.setAccessible(true);
    int version = (int) method.invoke(null);
    // It should return a positive int (depends on runtime)
    assertTrue(version > 0);
  }

  @Test
    @Timeout(8000)
  public void testGetMajorJavaVersion_withVariousVersions() throws Exception {
    Method method = JavaVersion.class.getDeclaredMethod("getMajorJavaVersion", String.class);
    method.setAccessible(true);

    assertEquals(9, ((Integer) method.invoke(null, "9")).intValue());
    assertEquals(9, ((Integer) method.invoke(null, "9.0.1")).intValue());
    assertEquals(8, ((Integer) method.invoke(null, "1.8.0_181")).intValue());
    assertEquals(11, ((Integer) method.invoke(null, "11.0.2")).intValue());
    assertEquals(-1, ((Integer) method.invoke(null, "invalid")).intValue());
  }

  @Test
    @Timeout(8000)
  public void testParseDotted_variousInputs() throws Exception {
    Method method = JavaVersion.class.getDeclaredMethod("parseDotted", String.class);
    method.setAccessible(true);

    assertEquals(9, ((Integer) method.invoke(null, "9")).intValue());
    assertEquals(9, ((Integer) method.invoke(null, "9.0.4")).intValue());
    assertEquals(8, ((Integer) method.invoke(null, "1.8.0_181")).intValue());
    assertEquals(11, ((Integer) method.invoke(null, "11.0.2")).intValue());
    assertEquals(-1, ((Integer) method.invoke(null, "abc")).intValue());
  }

  @Test
    @Timeout(8000)
  public void testExtractBeginningInt_variousInputs() throws Exception {
    Method method = JavaVersion.class.getDeclaredMethod("extractBeginningInt", String.class);
    method.setAccessible(true);

    assertEquals(9, ((Integer) method.invoke(null, "9abc")).intValue());
    assertEquals(123, ((Integer) method.invoke(null, "123.45")).intValue());
    assertEquals(-1, ((Integer) method.invoke(null, "abc123")).intValue());
    assertEquals(-1, ((Integer) method.invoke(null, "")).intValue());
  }

  private void setMajorJavaVersion(int version) throws Exception {
    Field field = JavaVersion.class.getDeclaredField("majorJavaVersion");
    field.setAccessible(true);

    // Remove final modifier via reflection hack
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(field, field.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

    // Set the field to the new version
    field.set(null, version);
  }
}