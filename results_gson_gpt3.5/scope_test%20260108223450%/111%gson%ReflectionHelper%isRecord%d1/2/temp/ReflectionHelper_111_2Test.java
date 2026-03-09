package com.google.gson.internal.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonIOException;
import com.google.gson.internal.GsonBuildConfig;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ReflectionHelper_111_2Test {

  private static class DummyRecord {
    public final int x;
    public DummyRecord(int x) { this.x = x; }
  }

  private static class DummyClass {}

  private static Object originalRecordHelper;

  @BeforeAll
  static void setup() throws Exception {
    // Backup original RECORD_HELPER value
    Field recordHelperField = ReflectionHelper.class.getDeclaredField("RECORD_HELPER");
    recordHelperField.setAccessible(true);

    // Save original for restoration if needed
    originalRecordHelper = recordHelperField.get(null);

    // Remove final modifier from RECORD_HELPER field to allow reassignment
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(recordHelperField, recordHelperField.getModifiers() & ~Modifier.FINAL);

    // Find private static inner class RecordHelper
    Class<?> recordHelperClass = null;
    for (Class<?> innerClass : ReflectionHelper.class.getDeclaredClasses()) {
      if ("RecordHelper".equals(innerClass.getSimpleName())) {
        recordHelperClass = innerClass;
        break;
      }
    }
    assertNotNull(recordHelperClass, "RecordHelper class not found");

    // Make the constructor of RecordHelper accessible to allow Mockito to create mock
    var constructor = recordHelperClass.getDeclaredConstructor();
    constructor.setAccessible(true);

    // Create mock of RecordHelper class
    Object mockRecordHelper = Mockito.mock(recordHelperClass, invocation -> {
      String methodName = invocation.getMethod().getName();
      if ("isRecord".equals(methodName) && invocation.getArguments().length == 1) {
        Class<?> clazz = (Class<?>) invocation.getArgument(0);
        if (clazz == DummyRecord.class) {
          return true;
        }
        if (clazz == DummyClass.class) {
          return false;
        }
        return false;
      }
      // For other methods, call real method if possible, or throw
      try {
        return invocation.callRealMethod();
      } catch (Throwable t) {
        throw new RuntimeException(t);
      }
    });

    // Set the mock instance to the RECORD_HELPER field
    recordHelperField.set(null, mockRecordHelper);
  }

  @Test
    @Timeout(8000)
  void isRecord_returnsTrue_whenRecordHelperReturnsTrue() {
    assertTrue(ReflectionHelper.isRecord(DummyRecord.class));
  }

  @Test
    @Timeout(8000)
  void isRecord_returnsFalse_whenRecordHelperReturnsFalse() {
    assertFalse(ReflectionHelper.isRecord(DummyClass.class));
  }
}