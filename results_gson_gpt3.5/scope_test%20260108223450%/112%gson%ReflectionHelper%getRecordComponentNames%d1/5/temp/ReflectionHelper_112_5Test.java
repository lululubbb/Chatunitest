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
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class ReflectionHelper_112_5Test {

  private static Class<?> recordClassMock;
  private static String[] expectedComponentNames;

  @BeforeAll
  static void setup() {
    // Setup a mock record class and expected component names
    recordClassMock = String.class; // Using String.class as a dummy, will be mocked below
    expectedComponentNames = new String[] {"component1", "component2"};
  }

  private void setRecordHelperMock(Object recordHelperMock) throws Exception {
    Field recordHelperField = ReflectionHelper.class.getDeclaredField("RECORD_HELPER");
    recordHelperField.setAccessible(true);

    // Remove final modifier if present (Java 12+ requires this workaround)
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(recordHelperField, recordHelperField.getModifiers() & ~Modifier.FINAL);

    recordHelperField.set(null, recordHelperMock);
  }

  @Test
    @Timeout(8000)
  void testGetRecordComponentNames_returnsExpected() throws Exception {
    Class<?> recordHelperClass = Class.forName("com.google.gson.internal.reflect.ReflectionHelper$RecordHelper");

    Object recordHelperProxy = java.lang.reflect.Proxy.newProxyInstance(
        recordHelperClass.getClassLoader(),
        new Class<?>[] {recordHelperClass},
        (proxy, method, args) -> {
          if ("getRecordComponentNames".equals(method.getName()) && args.length == 1 && args[0] == recordClassMock) {
            return expectedComponentNames;
          }
          return null;
        });

    try (MockedStatic<ReflectionHelper> reflectionHelperStaticMock = Mockito.mockStatic(ReflectionHelper.class, Mockito.CALLS_REAL_METHODS)) {
      setRecordHelperMock(recordHelperProxy);

      String[] actual = ReflectionHelper.getRecordComponentNames(recordClassMock);

      assertArrayEquals(expectedComponentNames, actual);
    }
  }

  @Test
    @Timeout(8000)
  void testGetRecordComponentNames_handlesNullClass() throws Exception {
    Class<?> recordHelperClass = Class.forName("com.google.gson.internal.reflect.ReflectionHelper$RecordHelper");

    Object recordHelperProxy = java.lang.reflect.Proxy.newProxyInstance(
        recordHelperClass.getClassLoader(),
        new Class<?>[] {recordHelperClass},
        (proxy, method, args) -> {
          if ("getRecordComponentNames".equals(method.getName()) && args.length == 1 && args[0] == null) {
            return null;
          }
          return null;
        });

    try (MockedStatic<ReflectionHelper> reflectionHelperStaticMock = Mockito.mockStatic(ReflectionHelper.class, Mockito.CALLS_REAL_METHODS)) {
      setRecordHelperMock(recordHelperProxy);

      String[] actual = ReflectionHelper.getRecordComponentNames(null);

      assertNull(actual);
    }
  }
}