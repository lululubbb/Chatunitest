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

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class ReflectionHelper_114_4Test {

  static class TestRecord {
    private final String name;
    private final int age;

    public TestRecord(String name, int age) {
      this.name = name;
      this.age = age;
    }
  }

  // Instead of extending the private ReflectionHelper.RecordHelper,
  // create a standalone mock helper with the same method signature.
  static class RecordHelperMock {
    @SuppressWarnings("unchecked")
    <T> Constructor<T> getCanonicalRecordConstructor(Class<T> raw) {
      try {
        return raw.getDeclaredConstructor(String.class, int.class);
      } catch (NoSuchMethodException e) {
        return null;
      }
    }
  }

  private static void setFinalStatic(Field field, Object newValue) throws Exception {
    field.setAccessible(true);

    // Remove final modifier from field
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(field, field.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

    // Check that newValue is instance of field's type
    if (!field.getType().isInstance(newValue)) {
      throw new IllegalArgumentException("Cannot set field " + field.getName() +
          " of type " + field.getType().getName() +
          " to value of type " + newValue.getClass().getName());
    }

    field.set(null, newValue);
  }

  @Test
    @Timeout(8000)
  void getCanonicalRecordConstructor_returnsConstructor_whenRecordHelperReturnsConstructor() throws Exception {
    try (MockedStatic<ReflectionHelper> mockedStatic = Mockito.mockStatic(ReflectionHelper.class, Mockito.CALLS_REAL_METHODS)) {
      RecordHelperMock recordHelperMock = spy(new RecordHelperMock());

      Field recordHelperField = ReflectionHelper.class.getDeclaredField("RECORD_HELPER");
      recordHelperField.setAccessible(true);

      // Instead of using Proxy, directly set the field to recordHelperMock
      // because RECORD_HELPER is a class, not an interface.
      setFinalStatic(recordHelperField, recordHelperMock);

      Constructor<TestRecord> constructor = ReflectionHelper.getCanonicalRecordConstructor(TestRecord.class);
      assertNotNull(constructor);
      assertEquals(TestRecord.class, constructor.getDeclaringClass());
      assertEquals(2, constructor.getParameterCount());
      assertEquals(String.class, constructor.getParameterTypes()[0]);
      assertEquals(int.class, constructor.getParameterTypes()[1]);
    }
  }

  @Test
    @Timeout(8000)
  void getCanonicalRecordConstructor_returnsNull_whenRecordHelperReturnsNull() throws Exception {
    try (MockedStatic<ReflectionHelper> mockedStatic = Mockito.mockStatic(ReflectionHelper.class, Mockito.CALLS_REAL_METHODS)) {
      Field recordHelperField = ReflectionHelper.class.getDeclaredField("RECORD_HELPER");
      recordHelperField.setAccessible(true);

      // Create a subclass of RecordHelperMock overriding the method to return null
      RecordHelperMock recordHelperMock = new RecordHelperMock() {
        @Override
        <T> Constructor<T> getCanonicalRecordConstructor(Class<T> raw) {
          return null;
        }
      };

      setFinalStatic(recordHelperField, recordHelperMock);

      Constructor<TestRecord> constructor = ReflectionHelper.getCanonicalRecordConstructor(TestRecord.class);
      assertNull(constructor);
    }
  }
}