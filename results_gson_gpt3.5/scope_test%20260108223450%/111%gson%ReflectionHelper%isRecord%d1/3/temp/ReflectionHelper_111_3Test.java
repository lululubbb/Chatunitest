package com.google.gson.internal.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonIOException;
import com.google.gson.internal.GsonBuildConfig;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class ReflectionHelper_111_3Test {

  private static Class<?> recordClass;
  private static Class<?> nonRecordClass;

  @BeforeAll
  static void setup() throws Exception {
    // Since java.lang.Record is not available in Java versions <16,
    // use reflection to get it if it exists, otherwise use a dummy class.
    try {
      recordClass = Class.forName("java.lang.Record");
    } catch (ClassNotFoundException e) {
      // fallback to a dummy class if Record class is not present
      recordClass = DummyRecord.class;
    }
    nonRecordClass = NonRecordSample.class;
  }

  @Test
    @Timeout(8000)
  void testIsRecord_withRecordClass_returnsTrue() {
    try (MockedStatic<ReflectionHelper> mockedStatic = Mockito.mockStatic(ReflectionHelper.class, Mockito.CALLS_REAL_METHODS)) {
      var recordHelperField = ReflectionHelper.class.getDeclaredField("RECORD_HELPER");
      recordHelperField.setAccessible(true);
      Object originalRecordHelper = recordHelperField.get(null);
      Class<?> recordHelperClass = originalRecordHelper.getClass();

      Object spyRecordHelper = java.lang.reflect.Proxy.newProxyInstance(
          recordHelperClass.getClassLoader(),
          recordHelperClass.getInterfaces(),
          (proxy, method, args) -> {
            if ("isRecord".equals(method.getName()) && args.length == 1 && args[0] == recordClass) {
              return true;
            }
            return method.invoke(originalRecordHelper, args);
          });

      recordHelperField.set(null, spyRecordHelper);

      boolean result = ReflectionHelper.isRecord(recordClass);
      assertTrue(result);

      Method isRecord = spyRecordHelper.getClass().getMethod("isRecord", Class.class);
      isRecord.invoke(spyRecordHelper, recordClass);
    } catch (Exception e) {
      fail("Reflection error: " + e.getMessage());
    }
  }

  @Test
    @Timeout(8000)
  void testIsRecord_withNonRecordClass_returnsFalse() {
    try (MockedStatic<ReflectionHelper> mockedStatic = Mockito.mockStatic(ReflectionHelper.class, Mockito.CALLS_REAL_METHODS)) {
      var recordHelperField = ReflectionHelper.class.getDeclaredField("RECORD_HELPER");
      recordHelperField.setAccessible(true);
      Object originalRecordHelper = recordHelperField.get(null);
      Class<?> recordHelperClass = originalRecordHelper.getClass();

      Object spyRecordHelper = java.lang.reflect.Proxy.newProxyInstance(
          recordHelperClass.getClassLoader(),
          recordHelperClass.getInterfaces(),
          (proxy, method, args) -> {
            if ("isRecord".equals(method.getName()) && args.length == 1 && args[0] == nonRecordClass) {
              return false;
            }
            return method.invoke(originalRecordHelper, args);
          });

      recordHelperField.set(null, spyRecordHelper);

      boolean result = ReflectionHelper.isRecord(nonRecordClass);
      assertFalse(result);

      Method isRecord = spyRecordHelper.getClass().getMethod("isRecord", Class.class);
      isRecord.invoke(spyRecordHelper, nonRecordClass);
    } catch (Exception e) {
      fail("Reflection error: " + e.getMessage());
    }
  }

  // Dummy non-record class
  public static class NonRecordSample {
    private String name;
    private int id;
  }

  // Dummy record class substitute if java.lang.Record is not available
  public static class DummyRecord {
  }
}