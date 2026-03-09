package com.google.gson.internal.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonIOException;
import com.google.gson.internal.GsonBuildConfig;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Test;

class ReflectionHelper_110_2Test {

  @Test
    @Timeout(8000)
  void tryMakeAccessible_success() throws NoSuchMethodException {
    Constructor<String> constructor = String.class.getDeclaredConstructor(String.class);
    // Should be accessible without exception
    String result = ReflectionHelper.tryMakeAccessible(constructor);
    assertNull(result);
    assertTrue(constructor.canAccess(null));
  }

  @Test
    @Timeout(8000)
  void tryMakeAccessible_failure() throws Exception {
    Constructor<String> constructor = spy(String.class.getDeclaredConstructor(String.class));
    doThrow(new SecurityException("access denied")).when(constructor).setAccessible(true);

    String result = ReflectionHelper.tryMakeAccessible(constructor);

    assertNotNull(result);
    assertTrue(result.contains("Failed making constructor"));
    assertTrue(result.contains("access denied"));
  }
}