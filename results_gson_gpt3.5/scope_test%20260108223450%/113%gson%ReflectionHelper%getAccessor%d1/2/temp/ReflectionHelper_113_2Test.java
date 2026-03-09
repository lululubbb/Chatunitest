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
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ReflectionHelper_113_2Test {

  static class DummyClass {
    public String field;
  }

  private static Field dummyField;
  private static Method dummyMethod;

  @BeforeAll
  static void setup() throws NoSuchFieldException, NoSuchMethodException {
    dummyField = DummyClass.class.getField("field");
    dummyMethod = DummyClass.class.getMethod("toString");
  }

  @Test
    @Timeout(8000)
  void getAccessor_returnsMethodFromRecordHelper() throws Exception {
    // Access the private static final RECORD_HELPER field
    Field recordHelperField = ReflectionHelper.class.getDeclaredField("RECORD_HELPER");
    recordHelperField.setAccessible(true);

    // Get the type of RECORD_HELPER (likely a class, not an interface)
    Class<?> recordHelperType = recordHelperField.getType();

    // Create a Mockito mock of the RECORD_HELPER type
    Object dummyRecordHelper = mock(recordHelperType);

    // Stub the getAccessor method to return dummyMethod
    Method getAccessorMethod = recordHelperType.getMethod("getAccessor", Class.class, Field.class);
    when(getAccessorMethod.invoke(dummyRecordHelper, DummyClass.class, dummyField)).thenReturn(dummyMethod);

    // Alternatively, use Mockito's when...thenReturn directly on the mock:
    // Because recordHelperType is not an interface, cast is needed. Use reflection-based stubbing:
    // Use Mockito's doReturn().when() with reflection proxy:
    // Instead, use Mockito's Answer to handle invoke:
    // But simpler is to create a spy or use Mockito's inline mock maker.

    // Since Mockito cannot stub methods via reflection invoke, use a dynamic proxy for the class:
    // But since it's a class, dynamic proxy won't work.

    // So, use reflection to create a subclass proxy with Mockito's inline mock maker (default in Mockito 3+)
    // Cast dummyRecordHelper to Object and use Mockito.when for method:
    // This requires casting to recordHelperType:
    // Use Mockito's when with reflection:
    // Use Mockito's doReturn(dummyMethod).when(dummyRecordHelper).getAccessor(...);

    // To avoid complexity, use Mockito's Answer to mock getAccessor:

    // Use Mockito's doAnswer:
    doAnswer(invocation -> dummyMethod)
        .when(dummyRecordHelper)
        .getClass()
        .getMethod("getAccessor", Class.class, Field.class)
        .invoke(dummyRecordHelper, DummyClass.class, dummyField);

    // The above won't work because invoke is called immediately.

    // Instead, use Mockito's doAnswer on dummyRecordHelper:
    // Use Mockito's mock with Answer:

    dummyRecordHelper = mock(recordHelperType, invocation -> {
      if ("getAccessor".equals(invocation.getMethod().getName())) {
        return dummyMethod;
      }
      return invocation.callRealMethod();
    });

    // Remove final modifier from RECORD_HELPER field
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(recordHelperField, recordHelperField.getModifiers() & ~Modifier.FINAL);

    // Set the RECORD_HELPER field to dummyRecordHelper
    recordHelperField.set(null, dummyRecordHelper);

    // Call the method under test
    Method result = ReflectionHelper.getAccessor(DummyClass.class, dummyField);

    assertSame(dummyMethod, result);
  }

}