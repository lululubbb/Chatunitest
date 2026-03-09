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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReflectionHelper_113_1Test {

  private static class DummyClass {
    private String field;

    @Override
    public String toString() {
      return super.toString();
    }
  }

  private Field dummyField;

  @BeforeEach
  void setUp() throws NoSuchFieldException {
    dummyField = DummyClass.class.getDeclaredField("field");
  }

  @Test
    @Timeout(8000)
  void testGetAccessor_delegatesToRecordHelper() throws Exception {
    Method expectedMethod = DummyClass.class.getDeclaredMethod("toString");

    // Instead of loading RecordHelper by name, get its class from the RECORD_HELPER instance
    java.lang.reflect.Field recordHelperField = ReflectionHelper.class.getDeclaredField("RECORD_HELPER");
    recordHelperField.setAccessible(true);
    Object originalRecordHelper = recordHelperField.get(null);
    Class<?> recordHelperClass = originalRecordHelper.getClass();

    // Create a mock with custom Answer to return expectedMethod when getAccessor is called
    Object spy = mock(recordHelperClass, invocation -> {
      if (invocation.getMethod().getName().equals("getAccessor")
          && invocation.getMethod().getParameterCount() == 2
          && invocation.getMethod().getParameterTypes()[0] == Class.class
          && invocation.getMethod().getParameterTypes()[1] == Field.class) {
        return expectedMethod;
      }
      return invocation.callRealMethod();
    });

    // Remove final modifier and set the RECORD_HELPER field to the spy mock
    java.lang.reflect.Field modifiersField = java.lang.reflect.Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(recordHelperField, recordHelperField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

    recordHelperField.set(null, spy);

    // Call the method under test
    Method actualMethod = ReflectionHelper.getAccessor(DummyClass.class, dummyField);

    // Assert the returned method is the expected one
    assertSame(expectedMethod, actualMethod);

    // Verify the mock's getAccessor method was called with the correct arguments via reflection
    Method getAccessorMethod = recordHelperClass.getMethod("getAccessor", Class.class, Field.class);
    getAccessorMethod.invoke(verify(spy), DummyClass.class, dummyField);

    // Restore original RECORD_HELPER to avoid side effects
    recordHelperField.set(null, originalRecordHelper);
  }
}