package com.google.gson.internal.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonIOException;
import com.google.gson.internal.GsonBuildConfig;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class ReflectionHelper_115_2Test {

  @Test
    @Timeout(8000)
  public void testCreateExceptionForUnexpectedIllegalAccess() throws Exception {
    IllegalAccessException cause = new IllegalAccessException("access denied");

    RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
      ReflectionHelper.createExceptionForUnexpectedIllegalAccess(cause);
    });

    assertEquals("Unexpected IllegalAccessException occurred (Gson " + GsonBuildConfig.VERSION + ")."
        + " Certain ReflectionAccessFilter features require Java >= 9 to work correctly. If you are not using"
        + " ReflectionAccessFilter, report this to the Gson maintainers.", thrown.getMessage());
    assertEquals(cause, thrown.getCause());
  }
}