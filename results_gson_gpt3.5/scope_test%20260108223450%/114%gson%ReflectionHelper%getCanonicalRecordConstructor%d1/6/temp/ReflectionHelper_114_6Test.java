package com.google.gson.internal.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonIOException;
import com.google.gson.internal.GsonBuildConfig;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Constructor;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.invocation.InvocationOnMock;

class ReflectionHelper_114_6Test {

  static class TestRecord {
    final int x;
    final String y;

    public TestRecord(int x, String y) {
      this.x = x;
      this.y = y;
    }
  }

  static class NonRecordClass {
    public NonRecordClass() {}
  }

  @BeforeAll
  static void setup() {
    // No setup needed since RECORD_HELPER is private static final and used internally
  }

  @Test
    @Timeout(8000)
  void getCanonicalRecordConstructor_validRecord_returnsConstructor() {
    Constructor<TestRecord> constructor = ReflectionHelper.getCanonicalRecordConstructor(TestRecord.class);
    assertNotNull(constructor);
    assertEquals(TestRecord.class, constructor.getDeclaringClass());
    assertEquals(2, constructor.getParameterCount());
  }

  @Test
    @Timeout(8000)
  void getCanonicalRecordConstructor_nonRecord_throwsRuntimeException() {
    RuntimeException thrown = assertThrows(RuntimeException.class, () ->
      ReflectionHelper.getCanonicalRecordConstructor(NonRecordClass.class)
    );
    assertNotNull(thrown.getMessage());
  }

  @Test
    @Timeout(8000)
  void getCanonicalRecordConstructor_nullClass_throwsRuntimeException() {
    RuntimeException thrown = assertThrows(RuntimeException.class, () ->
      ReflectionHelper.getCanonicalRecordConstructor(null)
    );
    assertNotNull(thrown.getMessage());
  }

  @Test
    @Timeout(8000)
  void getCanonicalRecordConstructor_mockedRecordHelper_returnsMockedConstructor() throws Exception {
    try (MockedStatic<ReflectionHelper> mockedStatic = org.mockito.Mockito.mockStatic(ReflectionHelper.class, org.mockito.Mockito.CALLS_REAL_METHODS)) {
      // Use reflection to get the inner class RecordHelper
      Class<?> recordHelperClass = Class.forName("com.google.gson.internal.reflect.ReflectionHelper$RecordHelper");
      Constructor<TestRecord> mockConstructor = TestRecord.class.getDeclaredConstructor(int.class, String.class);

      // Create a mock with Answer to intercept calls
      Object mockRecordHelperWithAnswer = mock(recordHelperClass, (InvocationOnMock invocation) -> {
        if ("getCanonicalRecordConstructor".equals(invocation.getMethod().getName())
            && invocation.getArguments().length == 1
            && TestRecord.class.equals(invocation.getArgument(0))) {
          return mockConstructor;
        }
        return invocation.callRealMethod();
      });

      // Use reflection to set the private static final RECORD_HELPER field
      java.lang.reflect.Field recordHelperField = ReflectionHelper.class.getDeclaredField("RECORD_HELPER");
      recordHelperField.setAccessible(true);

      // Remove final modifier from the field
      java.lang.reflect.Field modifiersField = java.lang.reflect.Field.class.getDeclaredField("modifiers");
      modifiersField.setAccessible(true);
      modifiersField.setInt(recordHelperField, recordHelperField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

      recordHelperField.set(null, mockRecordHelperWithAnswer);

      Constructor<TestRecord> constructor = ReflectionHelper.getCanonicalRecordConstructor(TestRecord.class);
      assertSame(mockConstructor, constructor);

      // Verification of method call is complicated due to reflection and visibility, so omitted.
    }
  }
}