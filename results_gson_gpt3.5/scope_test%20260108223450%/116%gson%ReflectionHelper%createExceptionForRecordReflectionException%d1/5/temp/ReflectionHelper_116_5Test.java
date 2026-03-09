package com.google.gson.internal.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonIOException;
import com.google.gson.internal.GsonBuildConfig;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

class ReflectionHelper_116_5Test {

  @Test
    @Timeout(8000)
  void testCreateExceptionForRecordReflectionException_throwsRuntimeException() throws Throwable {
    ReflectiveOperationException cause = new ReflectiveOperationException("reflective failure");

    // Use reflection to access the private static method
    Method method = ReflectionHelper.class.getDeclaredMethod("createExceptionForRecordReflectionException", ReflectiveOperationException.class);
    method.setAccessible(true);

    RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
      try {
        method.invoke(null, cause);
      } catch (InvocationTargetException e) {
        // Unwrap the underlying exception thrown by the method
        throw e.getCause();
      }
    });

    assertNotNull(thrown.getCause());
    assertEquals(cause, thrown.getCause());
    String message = thrown.getMessage();
    assertTrue(message.contains("Unexpected ReflectiveOperationException occurred"));
    assertTrue(message.contains("Gson"));
    assertTrue(message.contains("records"));
  }
}