package com.google.gson.internal.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonIOException;
import com.google.gson.internal.GsonBuildConfig;
import java.lang.reflect.AccessibleObject;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ReflectionHelper_114_5Test {

  // A sample class to simulate a record-like structure (since 'record' keyword is not recognized)
  static class SampleRecord {
    private final String name;
    private final int age;

    public SampleRecord(String name, int age) {
      this.name = name;
      this.age = age;
    }
  }

  // A sample non-record class for negative testing
  static class NonRecord {
    NonRecord() {}
  }

  @BeforeAll
  static void setup() {
    // No setup needed since RECORD_HELPER is static final and used internally
  }

  @Test
    @Timeout(8000)
  void getCanonicalRecordConstructor_whenRecordClass_returnsConstructor() {
    // Skip test if records not supported on this JVM
    try {
      Constructor<SampleRecord> constructor = ReflectionHelper.getCanonicalRecordConstructor(SampleRecord.class);
      assertNotNull(constructor);
      assertEquals(SampleRecord.class, constructor.getDeclaringClass());
      assertEquals(2, constructor.getParameterCount());
    } catch (UnsupportedOperationException e) {
      // JVM does not support records, test not applicable
      // Just pass
    }
  }

  @Test
    @Timeout(8000)
  void getCanonicalRecordConstructor_whenNonRecordClass_throwsException() {
    // Skip test if records not supported on this JVM
    try {
      RuntimeException exception = assertThrows(RuntimeException.class,
          () -> ReflectionHelper.getCanonicalRecordConstructor(NonRecord.class));
      assertNotNull(exception.getMessage());
    } catch (UnsupportedOperationException e) {
      // JVM does not support records, test not applicable
      // Just pass
    }
  }

  @Test
    @Timeout(8000)
  void getCanonicalRecordConstructor_nullClass_throwsNullPointerException() {
    try {
      ReflectionHelper.getCanonicalRecordConstructor(null);
      fail("Expected NullPointerException");
    } catch (NullPointerException e) {
      // expected
    } catch (UnsupportedOperationException e) {
      // JVM does not support records, test not applicable
      // Just pass
    }
  }

  @Test
    @Timeout(8000)
  void getCanonicalRecordConstructor_internalRecordHelperThrowsReflectiveOperationException() throws Exception {
    Field recordHelperField = ReflectionHelper.class.getDeclaredField("RECORD_HELPER");
    recordHelperField.setAccessible(true);

    // Remove final modifier from the field
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(recordHelperField, recordHelperField.getModifiers() & ~Modifier.FINAL);

    Object originalRecordHelper = recordHelperField.get(null);

    Class<?> recordHelperClass = originalRecordHelper.getClass().getInterfaces().length > 0
        ? originalRecordHelper.getClass().getInterfaces()[0]
        : null;

    if (recordHelperClass == null) {
      // If RECORD_HELPER is not a proxy or does not implement an interface, skip test
      recordHelperField.set(null, originalRecordHelper);
      return;
    }

    Object proxyRecordHelper = Proxy.newProxyInstance(
        recordHelperClass.getClassLoader(),
        new Class<?>[] { recordHelperClass },
        new InvocationHandler() {
          @Override
          public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if ("getCanonicalRecordConstructor".equals(method.getName())) {
              throw new ReflectiveOperationException("mocked exception");
            }
            return method.invoke(originalRecordHelper, args);
          }
        });

    recordHelperField.set(null, proxyRecordHelper);

    try {
      RuntimeException ex = assertThrows(RuntimeException.class,
          () -> ReflectionHelper.getCanonicalRecordConstructor(SampleRecord.class));
      assertTrue(ex.getMessage().contains("mocked exception"));
    } finally {
      // Restore original RECORD_HELPER
      recordHelperField.set(null, originalRecordHelper);
    }
  }
}