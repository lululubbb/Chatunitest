package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class JavaVersion_85_3Test {

  @Test
    @Timeout(8000)
  void testIsJava9OrLater_whenVersionIsLessThan9() throws Exception {
    setMajorJavaVersion(8);
    assertFalse(JavaVersion.isJava9OrLater());
  }

  @Test
    @Timeout(8000)
  void testIsJava9OrLater_whenVersionIs9() throws Exception {
    setMajorJavaVersion(9);
    assertTrue(JavaVersion.isJava9OrLater());
  }

  @Test
    @Timeout(8000)
  void testIsJava9OrLater_whenVersionIsGreaterThan9() throws Exception {
    setMajorJavaVersion(11);
    assertTrue(JavaVersion.isJava9OrLater());
  }

  @Test
    @Timeout(8000)
  void testDetermineMajorJavaVersion_reflectively() throws Exception {
    Method method = JavaVersion.class.getDeclaredMethod("determineMajorJavaVersion");
    method.setAccessible(true);
    int version = (int) method.invoke(null);
    // The version should be positive and reasonable
    assertTrue(version > 0);
  }

  @Test
    @Timeout(8000)
  void testGetMajorJavaVersion_withVariousStrings() throws Exception {
    Method method = JavaVersion.class.getDeclaredMethod("getMajorJavaVersion", String.class);
    method.setAccessible(true);

    assertEquals(8, ((Integer) method.invoke(null, "1.8.0_191")).intValue());
    assertEquals(9, ((Integer) method.invoke(null, "9")).intValue());
    assertEquals(11, ((Integer) method.invoke(null, "11.0.2")).intValue());
    assertEquals(-1, ((Integer) method.invoke(null, "")).intValue());
    assertEquals(-1, ((Integer) method.invoke(null, "abc")).intValue());
  }

  @Test
    @Timeout(8000)
  void testParseDotted_withVariousInputs() throws Exception {
    Method method = JavaVersion.class.getDeclaredMethod("parseDotted", String.class);
    method.setAccessible(true);

    assertEquals(8, ((Integer) method.invoke(null, "1.8.0_191")).intValue());
    assertEquals(9, ((Integer) method.invoke(null, "9")).intValue());
    assertEquals(11, ((Integer) method.invoke(null, "11.0.2")).intValue());
    assertEquals(-1, ((Integer) method.invoke(null, "")).intValue());
    assertEquals(-1, ((Integer) method.invoke(null, "abc")).intValue());
  }

  @Test
    @Timeout(8000)
  void testExtractBeginningInt_withVariousInputs() throws Exception {
    Method method = JavaVersion.class.getDeclaredMethod("extractBeginningInt", String.class);
    method.setAccessible(true);

    assertEquals(1, ((Integer) method.invoke(null, "1.8.0_191")).intValue());
    assertEquals(9, ((Integer) method.invoke(null, "9")).intValue());
    assertEquals(11, ((Integer) method.invoke(null, "11.0.2")).intValue());
    assertEquals(-1, ((Integer) method.invoke(null, "")).intValue());
    assertEquals(-1, ((Integer) method.invoke(null, "abc")).intValue());
    assertEquals(123, ((Integer) method.invoke(null, "123abc")).intValue());
  }

  @Test
    @Timeout(8000)
  void testGetMajorJavaVersion_public() {
    int version = JavaVersion.getMajorJavaVersion();
    assertTrue(version > 0);
  }

  private void setMajorJavaVersion(int version) throws Exception {
    Field field = JavaVersion.class.getDeclaredField("majorJavaVersion");
    field.setAccessible(true);

    // Remove final modifier via reflection
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

    // Set the field value
    field.set(null, version);

    // Reset cached value by invoking determineMajorJavaVersion via reflection to update any cached state
    // (Optional, can be omitted if no caching)
  }
}