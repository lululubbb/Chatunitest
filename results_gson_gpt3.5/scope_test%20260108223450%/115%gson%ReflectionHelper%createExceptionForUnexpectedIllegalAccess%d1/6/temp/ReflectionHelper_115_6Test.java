package com.google.gson.internal.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonIOException;
import com.google.gson.internal.GsonBuildConfig;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Field;
import org.junit.jupiter.api.Test;

public class ReflectionHelper_115_6Test {

  @Test
    @Timeout(8000)
  public void testCreateExceptionForUnexpectedIllegalAccess() throws Exception {
    IllegalAccessException cause = new IllegalAccessException("access denied");

    RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
      // Use reflection to invoke the static method since it's public static,
      // can call directly as well.
      ReflectionHelper.createExceptionForUnexpectedIllegalAccess(cause);
    });

    String expectedMessageStart = "Unexpected IllegalAccessException occurred (Gson ";
    String expectedMessageContains = "Certain ReflectionAccessFilter features require Java >= 9 to work correctly.";
    String expectedMessageEnd = "If you are not using ReflectionAccessFilter, report this to the Gson maintainers.";

    String actualMessage = thrown.getMessage();

    // Check that message starts with expected prefix including version
    assertEquals(true, actualMessage.startsWith(expectedMessageStart));
    // Check that message contains required substring
    assertEquals(true, actualMessage.contains(expectedMessageContains));
    // Check that message ends with expected suffix
    assertEquals(true, actualMessage.endsWith(expectedMessageEnd));

    // Check cause is the original IllegalAccessException
    assertEquals(cause, thrown.getCause());
  }
}