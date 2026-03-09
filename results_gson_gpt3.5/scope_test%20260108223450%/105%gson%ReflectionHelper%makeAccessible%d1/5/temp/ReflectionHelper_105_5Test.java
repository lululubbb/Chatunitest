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
import org.mockito.Mockito;

class ReflectionHelper_105_5Test {

  @Test
    @Timeout(8000)
  void makeAccessible_setsAccessibleTrue_whenNoException() throws Exception {
    AccessibleObject accessibleObject = mock(AccessibleObject.class);

    ReflectionHelper.makeAccessible(accessibleObject);

    verify(accessibleObject).setAccessible(true);
  }

  @Test
    @Timeout(8000)
  void makeAccessible_throwsJsonIOException_withProperMessage_whenExceptionThrown() throws Exception {
    AccessibleObject accessibleObject = mock(AccessibleObject.class);
    doThrow(new SecurityException("denied")).when(accessibleObject).setAccessible(true);

    JsonIOException exception = assertThrows(JsonIOException.class, () -> {
      ReflectionHelper.makeAccessible(accessibleObject);
    });

    String message = exception.getMessage();
    assertTrue(message.startsWith("Failed making "));
    assertTrue(message.contains(" accessible; either increase its visibility"));
    assertNotNull(exception.getCause());
    assertEquals(SecurityException.class, exception.getCause().getClass());
  }
}