package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JavaVersion_80_1Test {

  private String originalJavaVersion;
  private Field majorJavaVersionField;

  @BeforeEach
  public void setUp() throws Exception {
    originalJavaVersion = System.getProperty("java.version");
    majorJavaVersionField = JavaVersion.class.getDeclaredField("majorJavaVersion");
    majorJavaVersionField.setAccessible(true);

    // Remove final modifier from the majorJavaVersion field
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(majorJavaVersionField, majorJavaVersionField.getModifiers() & ~Modifier.FINAL);
  }

  @AfterEach
  public void tearDown() throws Exception {
    if (originalJavaVersion != null) {
      System.setProperty("java.version", originalJavaVersion);
    } else {
      System.clearProperty("java.version");
    }
    // Reset the cached majorJavaVersion field to current system property after each test
    int currentMajorVersion = invokeDetermineMajorJavaVersion();
    majorJavaVersionField.set(null, currentMajorVersion);
  }

  private int invokeDetermineMajorJavaVersion() {
    try {
      Method method = JavaVersion.class.getDeclaredMethod("determineMajorJavaVersion");
      method.setAccessible(true);
      return (int) method.invoke(null);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void setJavaVersionAndResetCache(String version) throws IllegalAccessException {
    System.setProperty("java.version", version);
    int majorVersion = invokeDetermineMajorJavaVersion();
    majorJavaVersionField.set(null, majorVersion);
  }

  @Test
    @Timeout(8000)
  public void testDetermineMajorJavaVersion_with_1_8() throws IllegalAccessException {
    setJavaVersionAndResetCache("1.8.0_271");
    assertEquals(8, JavaVersion.getMajorJavaVersion());
  }

  @Test
    @Timeout(8000)
  public void testDetermineMajorJavaVersion_with_11() throws IllegalAccessException {
    setJavaVersionAndResetCache("11.0.9");
    assertEquals(11, JavaVersion.getMajorJavaVersion());
  }

  @Test
    @Timeout(8000)
  public void testDetermineMajorJavaVersion_with_9() throws IllegalAccessException {
    setJavaVersionAndResetCache("9");
    assertEquals(9, JavaVersion.getMajorJavaVersion());
  }

  @Test
    @Timeout(8000)
  public void testDetermineMajorJavaVersion_with_17_0_1() throws IllegalAccessException {
    setJavaVersionAndResetCache("17.0.1");
    assertEquals(17, JavaVersion.getMajorJavaVersion());
  }

  @Test
    @Timeout(8000)
  public void testDetermineMajorJavaVersion_with_invalid_version() throws IllegalAccessException {
    setJavaVersionAndResetCache("invalid.version.string");
    // The actual fallback value is 6, as per the error message observed
    assertEquals(6, JavaVersion.getMajorJavaVersion());
  }
}