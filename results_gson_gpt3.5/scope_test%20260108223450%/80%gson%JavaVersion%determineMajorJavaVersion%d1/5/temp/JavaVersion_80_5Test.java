package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JavaVersion_80_5Test {

  private Field majorJavaVersionField;
  private Field modifiersField;
  private int originalMajorJavaVersion;

  @BeforeEach
  void setUp() throws Exception {
    majorJavaVersionField = JavaVersion.class.getDeclaredField("majorJavaVersion");
    majorJavaVersionField.setAccessible(true);

    modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);

    // Remove final modifier from majorJavaVersion field once before tests
    modifiersField.setInt(majorJavaVersionField, majorJavaVersionField.getModifiers() & ~Modifier.FINAL);

    // Save original value
    originalMajorJavaVersion = majorJavaVersionField.getInt(null);
  }

  private void setJavaVersion(String version) throws Exception {
    Method getMajorJavaVersionMethod = JavaVersion.class.getDeclaredMethod("getMajorJavaVersion", String.class);
    getMajorJavaVersionMethod.setAccessible(true);

    int majorVersion = (int) getMajorJavaVersionMethod.invoke(null, version);

    // Set the majorJavaVersion field to the calculated majorVersion
    majorJavaVersionField.setInt(null, majorVersion);
  }

  @AfterEach
  void resetJavaVersion() throws Exception {
    // Reset majorJavaVersion to original value
    majorJavaVersionField.setInt(null, originalMajorJavaVersion);
  }

  @Test
    @Timeout(8000)
  void testDetermineMajorJavaVersion_java8() throws Exception {
    setJavaVersion("1.8.0_251");

    int majorVersion = JavaVersion.getMajorJavaVersion();

    assertEquals(8, majorVersion);
  }

  @Test
    @Timeout(8000)
  void testDetermineMajorJavaVersion_java11() throws Exception {
    setJavaVersion("11.0.7");

    int majorVersion = JavaVersion.getMajorJavaVersion();

    assertEquals(11, majorVersion);
  }

  @Test
    @Timeout(8000)
  void testDetermineMajorJavaVersion_java9() throws Exception {
    setJavaVersion("9");

    int majorVersion = JavaVersion.getMajorJavaVersion();

    assertEquals(9, majorVersion);
  }

  @Test
    @Timeout(8000)
  void testDetermineMajorJavaVersion_java13_0_1() throws Exception {
    setJavaVersion("13.0.1");

    int majorVersion = JavaVersion.getMajorJavaVersion();

    assertEquals(13, majorVersion);
  }

  @Test
    @Timeout(8000)
  void testDetermineMajorJavaVersion_unusualFormat() throws Exception {
    setJavaVersion("17-ea");

    int majorVersion = JavaVersion.getMajorJavaVersion();

    assertEquals(17, majorVersion);
  }
}