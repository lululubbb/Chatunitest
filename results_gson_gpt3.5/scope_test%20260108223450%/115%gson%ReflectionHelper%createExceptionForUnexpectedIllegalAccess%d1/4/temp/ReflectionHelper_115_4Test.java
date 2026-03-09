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

public class ReflectionHelper_115_4Test {

  @Test
    @Timeout(8000)
  public void testCreateExceptionForUnexpectedIllegalAccess_throwsRuntimeExceptionWithCause() throws Throwable {
    IllegalAccessException cause = new IllegalAccessException("access denied");

    // Use reflection to invoke the static method
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

    String expectedMessageStart = "Unexpected IllegalAccessException occurred (Gson ";
    String expectedMessageContains = "Certain ReflectionAccessFilter features require Java >= 9 to work correctly.";
    assertEquals(cause, thrown.getCause());
    String actualMessage = thrown.getMessage();
    // Check that message starts with expected prefix and contains expected notice
    assertEquals(true, actualMessage.startsWith(expectedMessageStart));
    assertEquals(true, actualMessage.contains(expectedMessageContains));
  }
}