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
import org.mockito.Mockito;

class ReflectionHelper_114_1Test {

  static class RecordMock {
    final int x;
    final String y;

    public RecordMock(int x, String y) {
      this.x = x;
      this.y = y;
    }
  }

  @BeforeAll
  static void setup() {
    // No setup needed because RECORD_HELPER is private static final and cannot be mocked directly.
  }

  @Test
    @Timeout(8000)
  void testGetCanonicalRecordConstructor_returnsConstructor() throws Exception {
    try (MockedStatic<ReflectionHelper> mockedReflectionHelper = Mockito.mockStatic(ReflectionHelper.class)) {
      Constructor<RecordMock> constructor = RecordMock.class.getDeclaredConstructor(int.class, String.class);
      mockedReflectionHelper.when(() -> ReflectionHelper.getCanonicalRecordConstructor(RecordMock.class)).thenReturn(constructor);

      Constructor<RecordMock> result = ReflectionHelper.getCanonicalRecordConstructor(RecordMock.class);
      assertNotNull(result);
      assertEquals(RecordMock.class, result.getDeclaringClass());
      assertEquals(2, result.getParameterCount());
      assertSame(constructor, result);

      mockedReflectionHelper.verify(() -> ReflectionHelper.getCanonicalRecordConstructor(RecordMock.class));
    }
  }

  @Test
    @Timeout(8000)
  void testGetCanonicalRecordConstructor_withNullClass_throwsException() {
    try (MockedStatic<ReflectionHelper> mockedReflectionHelper = Mockito.mockStatic(ReflectionHelper.class)) {
      mockedReflectionHelper.when(() -> ReflectionHelper.getCanonicalRecordConstructor(null))
          .thenThrow(new NullPointerException());

      assertThrows(NullPointerException.class, () -> {
        ReflectionHelper.getCanonicalRecordConstructor(null);
      });

      mockedReflectionHelper.verify(() -> ReflectionHelper.getCanonicalRecordConstructor(null));
    }
  }

  @Test
    @Timeout(8000)
  void testGetCanonicalRecordConstructor_invokesUnderlyingMethod() throws Exception {
    try (MockedStatic<ReflectionHelper> mockedReflectionHelper = Mockito.mockStatic(ReflectionHelper.class)) {
      Constructor<RecordMock> mockConstructor = RecordMock.class.getDeclaredConstructor(int.class, String.class);
      mockedReflectionHelper.when(() -> ReflectionHelper.getCanonicalRecordConstructor(RecordMock.class)).thenReturn(mockConstructor);

      Constructor<RecordMock> result = ReflectionHelper.getCanonicalRecordConstructor(RecordMock.class);
      assertSame(mockConstructor, result);
      mockedReflectionHelper.verify(() -> ReflectionHelper.getCanonicalRecordConstructor(RecordMock.class));
    }
  }
}