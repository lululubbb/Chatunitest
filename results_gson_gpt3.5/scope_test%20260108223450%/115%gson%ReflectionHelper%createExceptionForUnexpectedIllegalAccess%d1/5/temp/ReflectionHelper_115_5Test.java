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

public class ReflectionHelper_115_5Test {

  @Test
    @Timeout(8000)
  void testCreateExceptionForUnexpectedIllegalAccess_throwsRuntimeExceptionWithCause() throws Throwable {
    IllegalAccessException illegalAccessException = new IllegalAccessException("access denied");

    // Use reflection to invoke the static method since it's public static, direct call is possible too
    RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
      ReflectionHelper.createExceptionForUnexpectedIllegalAccess(illegalAccessException);
    });

    String expectedMessageStart = "Unexpected IllegalAccessException occurred (Gson ";
    assertEquals(illegalAccessException, thrown.getCause());
    // Check message contains expected parts
    String message = thrown.getMessage();
    assert message.startsWith(expectedMessageStart);
    assert message.contains("Certain ReflectionAccessFilter features require Java >= 9 to work correctly");
    assert message.contains("If you are not using ReflectionAccessFilter, report this to the Gson maintainers.");
  }
}