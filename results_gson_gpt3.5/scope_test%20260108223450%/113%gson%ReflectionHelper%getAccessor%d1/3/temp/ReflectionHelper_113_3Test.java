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
import java.lang.reflect.Proxy;
import java.lang.reflect.InvocationHandler;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ReflectionHelper_113_3Test {

  private static Field dummyField;
  private static Method dummyMethod;
  private static Object originalRecordHelper;

  @BeforeAll
  static void setup() throws Exception {
    dummyField = DummyClass.class.getField("field");
    dummyMethod = DummyClass.class.getMethod("accessorMethod");

    java.lang.reflect.Field recordHelperField = ReflectionHelper.class.getDeclaredField("RECORD_HELPER");
    recordHelperField.setAccessible(true);

    // Remove final modifier from RECORD_HELPER
    java.lang.reflect.Field modifiersField = java.lang.reflect.Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(recordHelperField, recordHelperField.getModifiers() & ~Modifier.FINAL);

    // Save original value to restore if needed
    originalRecordHelper = recordHelperField.get(null);

    // Instead of searching for an interface named "RecordHelper" inside ReflectionHelper,
    // get the type of the RECORD_HELPER field itself, which is the interface type.
    Class<?> recordHelperInterface = recordHelperField.getType();

    Object dummyRecordHelper = Proxy.newProxyInstance(
        recordHelperInterface.getClassLoader(),
        new Class<?>[] { recordHelperInterface },
        new InvocationHandler() {
          @Override
          public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if ("getAccessor".equals(method.getName())) {
              return dummyMethod;
            }
            throw new UnsupportedOperationException("Unsupported method: " + method.getName());
          }
        });

    recordHelperField.set(null, dummyRecordHelper);
  }

  private void setDummyRecordHelper(final Method accessor) throws Exception {
    java.lang.reflect.Field recordHelperField = ReflectionHelper.class.getDeclaredField("RECORD_HELPER");
    recordHelperField.setAccessible(true);

    // Remove final modifier if still present
    java.lang.reflect.Field modifiersField = java.lang.reflect.Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(recordHelperField, recordHelperField.getModifiers() & ~Modifier.FINAL);

    Class<?> recordHelperInterface = recordHelperField.getType();

    Object dummyRecordHelper = Proxy.newProxyInstance(
        recordHelperInterface.getClassLoader(),
        new Class<?>[] { recordHelperInterface },
        new InvocationHandler() {
          @Override
          public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if ("getAccessor".equals(method.getName())) {
              return accessor;
            }
            throw new UnsupportedOperationException("Unsupported method: " + method.getName());
          }
        });

    recordHelperField.set(null, dummyRecordHelper);
  }

  @Test
    @Timeout(8000)
  void getAccessor_returnsMethodFromRecordHelper() throws Exception {
    setDummyRecordHelper(dummyMethod);
    Method result = ReflectionHelper.getAccessor(DummyClass.class, dummyField);
    assertNotNull(result);
    assertEquals(dummyMethod, result);
  }

  @Test
    @Timeout(8000)
  void getAccessor_nullField_returnsNull() throws Exception {
    setDummyRecordHelper(null);
    Method result = ReflectionHelper.getAccessor(DummyClass.class, null);
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void getAccessor_nullClass_returnsNull() throws Exception {
    setDummyRecordHelper(null);
    Method result = ReflectionHelper.getAccessor(null, dummyField);
    assertNull(result);
  }

  static class DummyClass {
    public String field;
    public String accessorMethod() { return "accessor"; }
  }
}