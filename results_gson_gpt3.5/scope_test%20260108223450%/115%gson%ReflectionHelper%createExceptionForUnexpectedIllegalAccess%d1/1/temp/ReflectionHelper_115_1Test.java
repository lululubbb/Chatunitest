package com.google.gson.internal.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonIOException;
import com.google.gson.internal.GsonBuildConfig;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class ReflectionHelper_115_1Test {

  @Test
    @Timeout(8000)
  public void testCreateExceptionForUnexpectedIllegalAccess() throws Throwable {
    IllegalAccessException cause = new IllegalAccessException("access denied");

    // Use reflection to access the private static method
    Method method = ReflectionHelper.class.getDeclaredMethod("createExceptionForUnexpectedIllegalAccess", IllegalAccessException.class);
    method.setAccessible(true);

    RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
      try {
        method.invoke(null, cause);
      } catch (InvocationTargetException e) {
        // unwrap the underlying exception thrown by the invoked method
        throw e.getCause();
      }
    });

    assertEquals("Unexpected IllegalAccessException occurred (Gson " + GsonBuildConfig.VERSION + "). Certain ReflectionAccessFilter features require Java >= 9 to work correctly. If you are not using ReflectionAccessFilter, report this to the Gson maintainers.", thrown.getMessage());
    assertEquals(cause, thrown.getCause());
  }
}