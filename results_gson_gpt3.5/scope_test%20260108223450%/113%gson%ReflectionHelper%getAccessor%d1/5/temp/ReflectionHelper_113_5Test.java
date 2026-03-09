package com.google.gson.internal.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonIOException;
import com.google.gson.internal.GsonBuildConfig;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class ReflectionHelper_113_5Test {

  private static Object dummyRecordHelper;
  private static Method dummyMethod;
  private static Field dummyField;
  private static Field recordHelperField;

  @BeforeAll
  static void setup() throws Exception {
    dummyField = DummyClass.class.getDeclaredField("value");
    dummyMethod = DummyClass.class.getDeclaredMethod("getValue");

    recordHelperField = ReflectionHelper.class.getDeclaredField("RECORD_HELPER");
    recordHelperField.setAccessible(true);

    // Remove final modifier from RECORD_HELPER field
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(recordHelperField, recordHelperField.getModifiers() & ~Modifier.FINAL);

    // Get the type of RECORD_HELPER field (RecordHelper)
    Class<?> recordHelperClass = recordHelperField.getType();

    // Create a Mockito mock instance of RecordHelper with Answer to return dummyMethod on getAccessor call
    dummyRecordHelper = mock(recordHelperClass, invocation -> {
      if ("getAccessor".equals(invocation.getMethod().getName())
          && invocation.getArguments().length == 2) {
        return dummyMethod;
      }
      Class<?> returnType = invocation.getMethod().getReturnType();
      if (returnType.isPrimitive()) {
        if (returnType == boolean.class) return false;
        if (returnType == void.class) return null;
        if (returnType == byte.class) return (byte) 0;
        if (returnType == short.class) return (short) 0;
        if (returnType == int.class) return 0;
        if (returnType == long.class) return 0L;
        if (returnType == float.class) return 0f;
        if (returnType == double.class) return 0d;
        if (returnType == char.class) return '\0';
      }
      return null;
    });
  }

  @Test
    @Timeout(8000)
  void getAccessor_returnsMethodFromRecordHelper() throws Exception {
    Object originalRecordHelper = recordHelperField.get(null);
    try {
      recordHelperField.set(null, dummyRecordHelper);

      Method result = ReflectionHelper.getAccessor(DummyClass.class, dummyField);
      assertNotNull(result);
      assertEquals(dummyMethod, result);
    } finally {
      recordHelperField.set(null, originalRecordHelper);
    }
  }

  private static class DummyClass {
    private int value;

    public int getValue() {
      return value;
    }
  }
}