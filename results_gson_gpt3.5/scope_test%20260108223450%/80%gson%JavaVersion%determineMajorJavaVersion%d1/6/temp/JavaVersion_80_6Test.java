package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JavaVersion_80_6Test {

  @BeforeEach
  void resetMajorJavaVersion() throws NoSuchFieldException, IllegalAccessException {
    // Reset majorJavaVersion to initial value before each test
    Field field = JavaVersion.class.getDeclaredField("majorJavaVersion");
    field.setAccessible(true);

    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(field, field.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

    Method determineMethod;
    try {
      determineMethod = JavaVersion.class.getDeclaredMethod("determineMajorJavaVersion");
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
    determineMethod.setAccessible(true);

    int originalValue;
    try {
      originalValue = (int) determineMethod.invoke(null);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }

    field.setInt(null, originalValue);
  }

  private void setJavaVersion(String version) throws NoSuchFieldException, IllegalAccessException {
    // Set the system property java.version to the desired version string
    System.setProperty("java.version", version);

    // Reset majorJavaVersion to reflect the new java.version system property
    Field field = JavaVersion.class.getDeclaredField("majorJavaVersion");
    field.setAccessible(true);

    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(field, field.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

    Method determineMethod;
    try {
      determineMethod = JavaVersion.class.getDeclaredMethod("determineMajorJavaVersion");
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
    determineMethod.setAccessible(true);

    int majorVersion;
    try {
      majorVersion = (int) determineMethod.invoke(null);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }

    field.setInt(null, majorVersion);
  }

  @Test
    @Timeout(8000)
  void testDetermineMajorJavaVersion_Java8() throws NoSuchFieldException, IllegalAccessException {
    setJavaVersion("1.8.0_271");

    Method method;
    try {
      method = JavaVersion.class.getDeclaredMethod("determineMajorJavaVersion");
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
    method.setAccessible(true);

    int result;
    try {
      result = (int) method.invoke(null);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }

    assertEquals(8, result);
  }

  @Test
    @Timeout(8000)
  void testDetermineMajorJavaVersion_Java11() throws NoSuchFieldException, IllegalAccessException {
    setJavaVersion("11.0.9");

    Method method;
    try {
      method = JavaVersion.class.getDeclaredMethod("determineMajorJavaVersion");
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
    method.setAccessible(true);

    int result;
    try {
      result = (int) method.invoke(null);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }

    assertEquals(11, result);
  }

  @Test
    @Timeout(8000)
  void testDetermineMajorJavaVersion_Java9_Early() throws NoSuchFieldException, IllegalAccessException {
    setJavaVersion("9");

    Method method;
    try {
      method = JavaVersion.class.getDeclaredMethod("determineMajorJavaVersion");
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
    method.setAccessible(true);

    int result;
    try {
      result = (int) method.invoke(null);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }

    assertEquals(9, result);
  }

  @Test
    @Timeout(8000)
  void testDetermineMajorJavaVersion_Java17_EarlyAccess() throws NoSuchFieldException, IllegalAccessException {
    setJavaVersion("17-ea");

    Method method;
    try {
      method = JavaVersion.class.getDeclaredMethod("determineMajorJavaVersion");
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
    method.setAccessible(true);

    int result;
    try {
      result = (int) method.invoke(null);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }

    assertEquals(17, result);
  }

  @Test
    @Timeout(8000)
  void testDetermineMajorJavaVersion_Java10_WithPatch() throws NoSuchFieldException, IllegalAccessException {
    setJavaVersion("10.0.2");

    Method method;
    try {
      method = JavaVersion.class.getDeclaredMethod("determineMajorJavaVersion");
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
    method.setAccessible(true);

    int result;
    try {
      result = (int) method.invoke(null);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }

    assertEquals(10, result);
  }
}