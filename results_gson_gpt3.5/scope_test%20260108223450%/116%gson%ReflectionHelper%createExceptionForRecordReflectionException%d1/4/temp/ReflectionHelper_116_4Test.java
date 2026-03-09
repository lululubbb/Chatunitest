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

class ReflectionHelper_116_4Test {

  @Test
    @Timeout(8000)
  void createExceptionForRecordReflectionException_throwsRuntimeExceptionWithCauseAndMessage() throws Throwable {
    // Arrange
    ReflectiveOperationException cause = new ReflectiveOperationException("test cause");

    // Use reflection to access private static method
    Method method = ReflectionHelper.class.getDeclaredMethod("createExceptionForRecordReflectionException", ReflectiveOperationException.class);
    method.setAccessible(true);

    // Act & Assert
    RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
      try {
        method.invoke(null, cause);
      } catch (InvocationTargetException e) {
        // unwrap the exception thrown by the invoked method
        throw e.getCause();
      }
    });

    String message = thrown.getMessage();
    assertNotNull(message);
    assertTrue(message.contains("Unexpected ReflectiveOperationException occurred"));
    assertTrue(message.contains("Gson"));
    assertTrue(message.contains("records"));
    assertSame(cause, thrown.getCause());
  }
}