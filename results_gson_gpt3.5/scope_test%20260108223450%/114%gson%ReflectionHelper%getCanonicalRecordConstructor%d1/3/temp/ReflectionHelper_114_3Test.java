package com.google.gson.internal.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonIOException;
import com.google.gson.internal.GsonBuildConfig;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ReflectionHelper_114_3Test {

  private static Object recordHelperMockInstance;

  private static final class TestRecord {
    private final String name;
    private final int age;

    public TestRecord(String name, int age) {
      this.name = name;
      this.age = age;
    }
  }

  @BeforeAll
  static void setupRecordHelper() throws Exception {
    // Get the RecordHelper class via reflection
    Class<?> reflectionHelperClass = ReflectionHelper.class;
    Field recordHelperField = reflectionHelperClass.getDeclaredField("RECORD_HELPER");
    recordHelperField.setAccessible(true);

    // Remove final modifier from RECORD_HELPER field
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(recordHelperField, recordHelperField.getModifiers() & ~Modifier.FINAL);

    Class<?> recordHelperClass = recordHelperField.getType();

    Class<?>[] interfaces = recordHelperClass.getInterfaces();
    if (interfaces.length == 0) {
      // No interfaces, create a Proxy implementing the RecordHelper class itself using an interface proxy workaround
      // Since Proxy requires interfaces, create a dynamic subclass via Proxy with a marker interface
      // But RecordHelper is private, so fallback to a Mockito mock instead
      recordHelperMockInstance = mock(recordHelperClass, invocation -> {
        if ("getCanonicalRecordConstructor".equals(invocation.getMethod().getName()) && invocation.getArguments().length == 1) {
          Class<?> raw = (Class<?>) invocation.getArguments()[0];
          try {
            return raw.getDeclaredConstructor(String.class, int.class);
          } catch (NoSuchMethodException e) {
            return null;
          }
        }
        return null;
      });
    } else {
      // Interfaces exist, create Proxy instance
      recordHelperMockInstance = Proxy.newProxyInstance(
          recordHelperClass.getClassLoader(),
          interfaces,
          (proxy, method, args) -> {
            if ("getCanonicalRecordConstructor".equals(method.getName()) && args.length == 1) {
              Class<?> raw = (Class<?>) args[0];
              try {
                return raw.getDeclaredConstructor(String.class, int.class);
              } catch (NoSuchMethodException e) {
                return null;
              }
            }
            return null;
          });
    }

    recordHelperField.set(null, recordHelperMockInstance);
  }

  @Test
    @Timeout(8000)
  void getCanonicalRecordConstructor_returnsConstructor_whenRecordHasCanonicalConstructor() {
    Constructor<TestRecord> constructor = ReflectionHelper.getCanonicalRecordConstructor(TestRecord.class);
    assertNotNull(constructor);
    assertEquals(TestRecord.class, constructor.getDeclaringClass());
    Class<?>[] params = constructor.getParameterTypes();
    assertArrayEquals(new Class<?>[] { String.class, int.class }, params);
  }

  @Test
    @Timeout(8000)
  void getCanonicalRecordConstructor_returnsNull_whenNoSuchMethod() {
    class NoCanonicalConstructor {
      private NoCanonicalConstructor() {}
    }

    Constructor<NoCanonicalConstructor> constructor = ReflectionHelper.getCanonicalRecordConstructor(NoCanonicalConstructor.class);
    assertNull(constructor);
  }

  @Test
    @Timeout(8000)
  void getCanonicalRecordConstructor_throwsException_whenRecordHelperThrows() throws Exception {
    Field recordHelperField = ReflectionHelper.class.getDeclaredField("RECORD_HELPER");
    recordHelperField.setAccessible(true);

    // Remove final modifier from RECORD_HELPER field
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(recordHelperField, recordHelperField.getModifiers() & ~Modifier.FINAL);

    Class<?> recordHelperPrivateClass = null;
    for (Class<?> inner : ReflectionHelper.class.getDeclaredClasses()) {
      if (inner.getSimpleName().equals("RecordHelper")) {
        recordHelperPrivateClass = inner;
        break;
      }
    }
    assertNotNull(recordHelperPrivateClass);

    // Create mock with Answer throwing exception on getCanonicalRecordConstructor
    Object mockRecordHelper = mock(recordHelperPrivateClass, invocation -> {
      if ("getCanonicalRecordConstructor".equals(invocation.getMethod().getName())
          && invocation.getArguments().length == 1) {
        throw new RuntimeException("fail");
      }
      return invocation.callRealMethod();
    });

    recordHelperField.set(null, mockRecordHelper);

    RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
      ReflectionHelper.getCanonicalRecordConstructor(Object.class);
    });
    assertEquals("fail", thrown.getMessage());
  }
}