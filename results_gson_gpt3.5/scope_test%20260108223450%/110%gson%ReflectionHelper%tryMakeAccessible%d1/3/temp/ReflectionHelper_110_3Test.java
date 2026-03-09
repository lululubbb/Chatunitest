package com.google.gson.internal.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonIOException;
import com.google.gson.internal.GsonBuildConfig;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.AccessibleObject;

import org.junit.jupiter.api.Test;

class ReflectionHelper_110_3Test {

  @Test
    @Timeout(8000)
  void tryMakeAccessible_success() throws NoSuchMethodException {
    Constructor<String> constructor = String.class.getDeclaredConstructor(String.class);
    String result = ReflectionHelper.tryMakeAccessible(constructor);
    assertNull(result);
    assertTrue(constructor.canAccess(null));
  }

  @Test
    @Timeout(8000)
  void tryMakeAccessible_failure() throws Exception {
    // Use a real constructor to avoid NullPointerException in constructorToString
    Constructor<String> constructor = String.class.getDeclaredConstructor(String.class);

    // Spy on the constructor to throw SecurityException when setAccessible(true) is called
    Constructor<String> spyConstructor = spy(constructor);
    doThrow(new SecurityException("Access denied")).when(spyConstructor).setAccessible(true);

    String message = ReflectionHelper.tryMakeAccessible(spyConstructor);

    assertNotNull(message);
    assertTrue(message.contains("Failed making constructor"));
    assertTrue(message.contains("Access denied"));
  }
}