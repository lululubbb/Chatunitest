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
import java.lang.reflect.Proxy;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ReflectionHelper_112_4Test {

  // Create a helper interface with the same method signature as ReflectionHelper.RecordHelper
  private interface DummyRecordHelperInterface {
    String[] getRecordComponentNames(Class<?> raw);
  }

  @BeforeAll
  static void setUp() throws Exception {
    // Use reflection to get the private static final RECORD_HELPER field in ReflectionHelper
    Field field = ReflectionHelper.class.getDeclaredField("RECORD_HELPER");
    field.setAccessible(true);

    // Remove final modifier if needed
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

    // Find the RecordHelper interface declared inside ReflectionHelper
    Class<?> recordHelperInterface = null;
    for (Class<?> innerClass : ReflectionHelper.class.getDeclaredClasses()) {
      if ("RecordHelper".equals(innerClass.getSimpleName()) && innerClass.isInterface()) {
        recordHelperInterface = innerClass;
        break;
      }
    }
    if (recordHelperInterface == null) {
      throw new IllegalStateException("Could not find RecordHelper interface");
    }

    // Create a proxy instance implementing the RecordHelper interface
    Object proxyInstance = Proxy.newProxyInstance(
        ReflectionHelper.class.getClassLoader(),
        new Class<?>[] { recordHelperInterface },
        (proxy, method, args) -> {
          if ("getRecordComponentNames".equals(method.getName())) {
            Class<?> raw = (Class<?>) args[0];
            if (raw == null) {
              throw new NullPointerException();
            }
            if (raw == String.class) {
              return new String[] { "name", "value" };
            }
            return new String[0];
          }
          throw new UnsupportedOperationException("Unsupported method: " + method.getName());
        });

    // Set the RECORD_HELPER field to the proxy instance
    field.set(null, proxyInstance);
  }

  @Test
    @Timeout(8000)
  void getRecordComponentNames_nullClass_throwsNPE() {
    assertThrows(NullPointerException.class, () -> ReflectionHelper.getRecordComponentNames(null));
  }

  @Test
    @Timeout(8000)
  void getRecordComponentNames_knownClass_returnsExpectedNames() {
    String[] names = ReflectionHelper.getRecordComponentNames(String.class);
    assertArrayEquals(new String[] { "name", "value" }, names);
  }

  @Test
    @Timeout(8000)
  void getRecordComponentNames_unknownClass_returnsEmptyArray() {
    String[] names = ReflectionHelper.getRecordComponentNames(Integer.class);
    assertNotNull(names);
    assertEquals(0, names.length);
  }
}