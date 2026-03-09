package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class JavaVersion_85_4Test {

  @Test
    @Timeout(8000)
  void testIsJava9OrLater_whenVersionIs9OrAbove() throws Exception {
    try (MockedStatic<JavaVersion> mocked = Mockito.mockStatic(JavaVersion.class, Mockito.CALLS_REAL_METHODS)) {
      Field majorJavaVersionField = JavaVersion.class.getDeclaredField("majorJavaVersion");
      majorJavaVersionField.setAccessible(true);

      Field modifiersField = Field.class.getDeclaredField("modifiers");
      modifiersField.setAccessible(true);
      modifiersField.setInt(majorJavaVersionField, majorJavaVersionField.getModifiers() & ~Modifier.FINAL);

      majorJavaVersionField.set(null, 11);
      assertTrue(JavaVersion.isJava9OrLater());
    }
  }

  @Test
    @Timeout(8000)
  void testIsJava9OrLater_whenVersionIsBelow9() throws Exception {
    try (MockedStatic<JavaVersion> mocked = Mockito.mockStatic(JavaVersion.class, Mockito.CALLS_REAL_METHODS)) {
      Field majorJavaVersionField = JavaVersion.class.getDeclaredField("majorJavaVersion");
      majorJavaVersionField.setAccessible(true);

      Field modifiersField = Field.class.getDeclaredField("modifiers");
      modifiersField.setAccessible(true);
      modifiersField.setInt(majorJavaVersionField, majorJavaVersionField.getModifiers() & ~Modifier.FINAL);

      majorJavaVersionField.set(null, 8);
      assertFalse(JavaVersion.isJava9OrLater());
    }
  }

  @Test
    @Timeout(8000)
  void testDetermineMajorJavaVersion_andHelpers() throws Exception {
    Method determineMajorJavaVersion = JavaVersion.class.getDeclaredMethod("determineMajorJavaVersion");
    determineMajorJavaVersion.setAccessible(true);

    Method getMajorJavaVersion_String = JavaVersion.class.getDeclaredMethod("getMajorJavaVersion", String.class);
    getMajorJavaVersion_String.setAccessible(true);

    Method parseDotted = JavaVersion.class.getDeclaredMethod("parseDotted", String.class);
    parseDotted.setAccessible(true);

    Method extractBeginningInt = JavaVersion.class.getDeclaredMethod("extractBeginningInt", String.class);
    extractBeginningInt.setAccessible(true);

    // getMajorJavaVersion(String)
    assertEquals(11, ((Integer) getMajorJavaVersion_String.invoke(null, "11.0.2")).intValue());
    assertEquals(8, ((Integer) getMajorJavaVersion_String.invoke(null, "1.8.0_45")).intValue());
    assertEquals(9, ((Integer) getMajorJavaVersion_String.invoke(null, "9.0.1")).intValue());
    assertEquals(0, ((Integer) getMajorJavaVersion_String.invoke(null, "")).intValue());

    // parseDotted(String)
    assertEquals(11, ((Integer) parseDotted.invoke(null, "11.0.2")).intValue());
    assertEquals(8, ((Integer) parseDotted.invoke(null, "1.8.0_45")).intValue());
    assertEquals(9, ((Integer) parseDotted.invoke(null, "9.0.1")).intValue());
    assertEquals(0, ((Integer) parseDotted.invoke(null, "")).intValue());

    // extractBeginningInt(String)
    assertEquals(11, ((Integer) extractBeginningInt.invoke(null, "11abc")).intValue());
    assertEquals(1, ((Integer) extractBeginningInt.invoke(null, "1.8.0_45")).intValue());
    assertEquals(0, ((Integer) extractBeginningInt.invoke(null, "abc")).intValue());
    assertEquals(0, ((Integer) extractBeginningInt.invoke(null, "")).intValue());
  }

  @Test
    @Timeout(8000)
  void testGetMajorJavaVersion_public() {
    int version = JavaVersion.getMajorJavaVersion();
    assertTrue(version > 0);
  }

  @Test
    @Timeout(8000)
  void testPrivateConstructor() throws Exception {
    var constructor = JavaVersion.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    assertNotNull(constructor.newInstance());
  }
}