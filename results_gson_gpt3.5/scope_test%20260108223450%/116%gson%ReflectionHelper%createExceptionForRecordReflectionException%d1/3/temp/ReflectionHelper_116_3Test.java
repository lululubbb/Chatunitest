package com.google.gson.internal.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonIOException;
import com.google.gson.internal.GsonBuildConfig;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.ReflectiveOperationException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

class ReflectionHelper_116_3Test {

  @Test
    @Timeout(8000)
  void testCreateExceptionForRecordReflectionException() throws Throwable {
    ReflectiveOperationException cause = new ReflectiveOperationException("test cause");

    Method method = ReflectionHelper.class.getDeclaredMethod(
        "createExceptionForRecordReflectionException", ReflectiveOperationException.class);
    method.setAccessible(true);

    RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
      try {
        method.invoke(null, cause);
      } catch (InvocationTargetException e) {
        // unwrap the exception thrown by invoked method
        throw e.getCause();
      }
    });

    String message = thrown.getMessage();
    assertTrue(message.contains("Unexpected ReflectiveOperationException occurred"));
    assertTrue(message.contains("Gson"));
    assertTrue(message.contains("reflection is utilized"));
    assertSame(cause, thrown.getCause());
  }
}