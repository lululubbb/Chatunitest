package com.google.gson.internal.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonIOException;
import com.google.gson.internal.GsonBuildConfig;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

@ExtendWith(MockitoExtension.class)
class ReflectionHelper_113_6Test {

  // Use raw Object type for mock to avoid access to private nested interface
  private static Object mockRecordHelper;

  private static Field testField;

  private static Method expectedMethod;

  static class DummyClass {
    private int someField;
    public int getSomeField() {
      return someField;
    }
  }

  @BeforeAll
  static void setup() throws Exception {
    expectedMethod = DummyClass.class.getMethod("getSomeField");
    testField = DummyClass.class.getDeclaredField("someField");

    // Get the private RecordHelper class via reflection
    Class<?> recordHelperClass = null;
    for (Class<?> innerClass : ReflectionHelper.class.getDeclaredClasses()) {
      if ("RecordHelper".equals(innerClass.getSimpleName())) {
        recordHelperClass = innerClass;
        break;
      }
    }
    assertNotNull(recordHelperClass, "RecordHelper class not found");

    // Create mock of RecordHelper class using Mockito.mock with withSettings().useConstructor()
    // to mock a class (not interface)
    mockRecordHelper = mock(recordHelperClass, withSettings().defaultAnswer(invocation -> {
      if ("getAccessor".equals(invocation.getMethod().getName())
          && invocation.getArguments().length == 2
          && invocation.getArgument(0) == DummyClass.class
          && invocation.getArgument(1) == testField) {
        return expectedMethod;
      }
      return null;
    }));

    // Use reflection to set the private static final RECORD_HELPER field in ReflectionHelper
    java.lang.reflect.Field recordHelperField = ReflectionHelper.class.getDeclaredField("RECORD_HELPER");
    recordHelperField.setAccessible(true);

    // Remove final modifier via reflection
    java.lang.reflect.Field modifiersField = java.lang.reflect.Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(recordHelperField, recordHelperField.getModifiers() & ~Modifier.FINAL);

    recordHelperField.set(null, mockRecordHelper);
  }

  @Test
    @Timeout(8000)
  void testGetAccessorDelegatesToRecordHelper() {
    Method method = ReflectionHelper.getAccessor(DummyClass.class, testField);
    assertNotNull(method);
    assertEquals(expectedMethod, method);
  }

  @Test
    @Timeout(8000)
  void testGetAccessorWithNullField() {
    Method method = ReflectionHelper.getAccessor(DummyClass.class, null);
    assertNull(method);
  }

  @Test
    @Timeout(8000)
  void testGetAccessorWithNullClass() {
    Method method = ReflectionHelper.getAccessor(null, testField);
    assertNull(method);
  }
}