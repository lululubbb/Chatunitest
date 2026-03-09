package com.google.gson.internal.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.GsonBuildConfig;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonIOException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class ReflectionHelper_105_1Test {

  @Test
    @Timeout(8000)
  void makeAccessible_shouldSetAccessibleTrue_whenNoException() throws Exception {
    AccessibleObject accessibleObject = mock(AccessibleObject.class);

    ReflectionHelper.makeAccessible(accessibleObject);

    verify(accessibleObject).setAccessible(true);
  }

  @Test
    @Timeout(8000)
  void makeAccessible_shouldThrowJsonIOException_whenExceptionThrown() throws Exception {
    AccessibleObject accessibleObject = mock(AccessibleObject.class);
    doThrow(new SecurityException("not allowed")).when(accessibleObject).setAccessible(true);

    try (MockedStatic<ReflectionHelper> mockedStatic = Mockito.mockStatic(ReflectionHelper.class, CALLS_REAL_METHODS)) {
      // Stub getAccessibleObjectDescription to call real method
      mockedStatic.when(() -> ReflectionHelper.getAccessibleObjectDescription(accessibleObject, false))
          .thenCallRealMethod();

      JsonIOException exception = assertThrows(JsonIOException.class, () -> ReflectionHelper.makeAccessible(accessibleObject));
      String message = exception.getMessage();

      assertTrue(message.contains("Failed making"));
      assertTrue(message.contains("accessible"));
      assertTrue(message.contains("either increase its visibility"));
      assertNotNull(exception.getCause());
      assertEquals(SecurityException.class, exception.getCause().getClass());
    }
  }
}