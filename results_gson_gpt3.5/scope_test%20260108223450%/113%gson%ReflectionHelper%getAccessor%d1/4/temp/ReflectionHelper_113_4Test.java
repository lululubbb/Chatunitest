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

class ReflectionHelper_113_4Test {

  static DummyRecordHelper dummyRecordHelper;

  static class DummyRecordHelper {
    Method accessor;
    Class<?> rawArg;
    Field fieldArg;

    public Method getAccessor(Class<?> raw, Field field) {
      this.rawArg = raw;
      this.fieldArg = field;
      return accessor;
    }
  }

  @BeforeAll
  static void setup() throws Exception {
    dummyRecordHelper = new DummyRecordHelper();

    // Get the RECORD_HELPER field from ReflectionHelper
    Field recordHelperField = ReflectionHelper.class.getDeclaredField("RECORD_HELPER");
    recordHelperField.setAccessible(true);

    // Remove final modifier from the field
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(recordHelperField, recordHelperField.getModifiers() & ~Modifier.FINAL);

    // Create a mock of the actual RecordHelper class (which is not an interface)
    Class<?> recordHelperType = recordHelperField.getType();
    Object mockRecordHelper = mock(recordHelperType);

    // Setup mock behavior for getAccessor method via Mockito's doAnswer
    Method getAccessorMethod = recordHelperType.getMethod("getAccessor", Class.class, Field.class);

    // Use Mockito's doAnswer to delegate calls to dummyRecordHelper.getAccessor
    // We need to find the mocked method on the mock object and stub it
    // Since we cannot do when(mockRecordHelper.getAccessor(...)) because of lack of compile-time type,
    // we use Mockito's doAnswer on the mock's method proxy obtained via reflection

    // Instead of reflection invoke, we use Mockito's stub for the method:
    // Use Mockito's Answer on the mock via spy of the class, or use Mockito's when with reflection proxy

    // The simplest approach: use Mockito's doAnswer with Mockito's 'doAnswer' + 'when' on the mock object
    // using Mockito's 'mockito-inline' or 'mockito' with deep stubs, but here we do it by reflection:

    // Create a proxy instance that delegates to dummyRecordHelper (since RecordHelper is a class, not interface)
    // So we cannot use Proxy. Instead, use a subclass mock with Mockito:

    // Create a subclass mock of the RecordHelper class
    Object subclassMock = mock(recordHelperType);

    // Stub the getAccessor method on the subclass mock
    // Because we have the Method object, we can use Mockito's doAnswer with the mock:
    // But we need a typed mock, so cast to Object and use Mockito's when with reflection API

    // Use Mockito's when with reflection:
    // when(subclassMock.getAccessor(any(), any())).thenAnswer(...)
    // But we cannot call getAccessor directly because we don't have the interface

    // Instead, use Mockito's doAnswer with Mockito's 'doAnswer' + 'when' on the mock's method via reflection:
    // Use Mockito's 'doAnswer' and 'when' with Mockito's 'mockito-inline' or use Mockito's InvocationOnMock

    // Use Mockito's doAnswer + doCallRealMethod for the method:

    // Use Mockito's doAnswer:
    // We can use Mockito's 'doAnswer' with 'doAnswer(...).when(subclassMock).getAccessor(any(), any())'
    // But we need to call getAccessor on subclassMock via reflection:

    // Use Mockito's doAnswer with reflection proxy:
    // Define a helper interface to cast the mock to, but since RecordHelper is a class, not interface,
    // we can create a subclass mock and override the method:

    // Use Mockito's Answer with doAnswer:
    org.mockito.stubbing.Answer<Method> answer = invocation -> {
      Class<?> raw = invocation.getArgument(0);
      Field field = invocation.getArgument(1);
      return dummyRecordHelper.getAccessor(raw, field);
    };

    // Use Mockito's doAnswer on the mock with reflection:
    // Use Mockito's doAnswer on the mock's method:
    // We can use Mockito's doAnswer().when(mock).method(args) only if we can call method on mock

    // So, get Method object and use Mockito's doAnswer with Mockito's 'doAnswer' + 'when' with mockito-inline:
    // Since we cannot call method directly, we use Mockito's 'doAnswer' + 'when' with Mockito's 'mockito-inline'

    // The easiest way: use Mockito's 'mock' with 'withSettings().defaultAnswer(...)' to delegate calls

    // Create a mock with default answer delegating to dummyRecordHelper:
    Object delegatingMock = mock(recordHelperType, invocation -> {
      if ("getAccessor".equals(invocation.getMethod().getName())) {
        Class<?> raw = invocation.getArgument(0);
        Field field = invocation.getArgument(1);
        return dummyRecordHelper.getAccessor(raw, field);
      }
      return invocation.callRealMethod();
    });

    // Set the RECORD_HELPER field to the delegating mock
    recordHelperField.set(null, delegatingMock);
  }

  @Test
    @Timeout(8000)
  void getAccessor_returnsMethodFromRecordHelper() throws NoSuchFieldException {
    Method expectedMethod = DummyRecord.class.getDeclaredMethods()[0];
    dummyRecordHelper.accessor = expectedMethod;

    Field field = DummyRecord.class.getDeclaredField("value");
    Method actualMethod = ReflectionHelper.getAccessor(DummyRecord.class, field);

    assertSame(expectedMethod, actualMethod);
    assertSame(DummyRecord.class, dummyRecordHelper.rawArg);
    assertSame(field, dummyRecordHelper.fieldArg);
  }

  static class DummyRecord {
    private final int value;

    public DummyRecord(int value) {
      this.value = value;
    }

    public int value() {
      return value;
    }
  }
}