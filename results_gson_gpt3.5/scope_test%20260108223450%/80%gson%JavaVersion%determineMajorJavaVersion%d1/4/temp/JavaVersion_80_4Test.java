package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JavaVersion_80_4Test {

  private String originalJavaVersion;
  private int originalMajorJavaVersion;
  private Field majorJavaVersionField;

  @BeforeEach
  public void setUp() throws Exception {
    originalJavaVersion = System.getProperty("java.version");

    majorJavaVersionField = JavaVersion.class.getDeclaredField("majorJavaVersion");
    majorJavaVersionField.setAccessible(true);

    // Remove final modifier from the field
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(majorJavaVersionField, majorJavaVersionField.getModifiers() & ~Modifier.FINAL);

    originalMajorJavaVersion = majorJavaVersionField.getInt(null);
  }

  @AfterEach
  public void tearDown() throws Exception {
    if (originalJavaVersion != null) {
      System.setProperty("java.version", originalJavaVersion);
    } else {
      System.clearProperty("java.version");
    }

    // Reset majorJavaVersion field to original value using reflection
    majorJavaVersionField.setInt(null, originalMajorJavaVersion);
  }

  private int invokeDetermineMajorJavaVersion() throws Exception {
    Method method = JavaVersion.class.getDeclaredMethod("determineMajorJavaVersion");
    method.setAccessible(true);
    return (int) method.invoke(null);
  }

  private void resetCachedMajorJavaVersion() throws Exception {
    // Recompute majorJavaVersion by invoking determineMajorJavaVersion()
    int newMajorVersion = invokeDetermineMajorJavaVersion();
    majorJavaVersionField.setInt(null, newMajorVersion);
  }

  @Test
    @Timeout(8000)
  public void testDetermineMajorJavaVersion_withJava8() throws Exception {
    System.setProperty("java.version", "1.8.0_271");
    resetCachedMajorJavaVersion();
    int majorVersion = invokeDetermineMajorJavaVersion();
    assertEquals(8, majorVersion);
  }

  @Test
    @Timeout(8000)
  public void testDetermineMajorJavaVersion_withJava9() throws Exception {
    System.setProperty("java.version", "9");
    resetCachedMajorJavaVersion();
    int majorVersion = invokeDetermineMajorJavaVersion();
    assertEquals(9, majorVersion);
  }

  @Test
    @Timeout(8000)
  public void testDetermineMajorJavaVersion_withJava11() throws Exception {
    System.setProperty("java.version", "11.0.2");
    resetCachedMajorJavaVersion();
    int majorVersion = invokeDetermineMajorJavaVersion();
    assertEquals(11, majorVersion);
  }

  @Test
    @Timeout(8000)
  public void testDetermineMajorJavaVersion_withMalformedVersion() throws Exception {
    System.setProperty("java.version", "abc.def");
    resetCachedMajorJavaVersion();
    int majorVersion = invokeDetermineMajorJavaVersion();
    // The actual code returns 6 for malformed versions, so assert accordingly
    assertEquals(6, majorVersion);
  }

  @Test
    @Timeout(8000)
  public void testDetermineMajorJavaVersion_withEmptyVersion() throws Exception {
    System.setProperty("java.version", "");
    resetCachedMajorJavaVersion();
    int majorVersion = invokeDetermineMajorJavaVersion();
    // The actual code returns 6 for empty version, so assert accordingly
    assertEquals(6, majorVersion);
  }
}