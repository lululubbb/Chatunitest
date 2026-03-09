package com.google.gson.internal.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonIOException;
import com.google.gson.internal.GsonBuildConfig;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ReflectionHelper_112_1Test {

  private static Class<?> recordClassMock;
  private static String[] expectedNames;

  @BeforeAll
  static void setUp() {
    recordClassMock = String.class; // Using String.class as a dummy class for testing
    expectedNames = new String[] {"name1", "name2"};
  }

  @Test
    @Timeout(8000)
  void testGetRecordComponentNames_returnsExpected() {
    try {
      Class<?> recordHelperClass = getRecordHelperClass();

      Object recordHelperProxy = java.lang.reflect.Proxy.newProxyInstance(
          recordHelperClass.getClassLoader(),
          new Class<?>[] {recordHelperClass},
          (proxy, m, args) -> {
            if ("getRecordComponentNames".equals(m.getName()) && args.length == 1 && args[0] == recordClassMock) {
              return expectedNames;
            }
            return null;
          });

      setFinalStatic(ReflectionHelper.class.getDeclaredField("RECORD_HELPER"), recordHelperProxy);

      String[] actual = ReflectionHelper.getRecordComponentNames(recordClassMock);
      assertArrayEquals(expectedNames, actual);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection error: " + e.getMessage());
    }
  }

  @Test
    @Timeout(8000)
  void testGetRecordComponentNames_nullClass() {
    try {
      Class<?> recordHelperClass = getRecordHelperClass();

      Object recordHelperProxy = java.lang.reflect.Proxy.newProxyInstance(
          recordHelperClass.getClassLoader(),
          new Class<?>[] {recordHelperClass},
          (proxy, m, args) -> {
            if ("getRecordComponentNames".equals(m.getName()) && args.length == 1 && args[0] == null) {
              return null;
            }
            return null;
          });

      setFinalStatic(ReflectionHelper.class.getDeclaredField("RECORD_HELPER"), recordHelperProxy);

      String[] actual = ReflectionHelper.getRecordComponentNames(null);
      assertNull(actual);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection error: " + e.getMessage());
    }
  }

  /**
   * Helper method to get the Class object of the private static nested interface RecordHelper from ReflectionHelper.
   */
  private static Class<?> getRecordHelperClass() {
    for (Class<?> innerClass : ReflectionHelper.class.getDeclaredClasses()) {
      if ("RecordHelper".equals(innerClass.getSimpleName()) && innerClass.isInterface()) {
        return innerClass;
      }
    }
    throw new IllegalStateException("RecordHelper interface not found");
  }

  /**
   * Sets a static final field via reflection.
   */
  private static void setFinalStatic(Field field, Object newValue) throws NoSuchFieldException, IllegalAccessException {
    field.setAccessible(true);

    // Remove final modifier from field
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

    field.set(null, newValue);
  }
}