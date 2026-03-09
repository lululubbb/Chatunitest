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

import org.junit.jupiter.api.Test;

class ReflectionHelper_110_4Test {

  @Test
    @Timeout(8000)
  void tryMakeAccessible_success() throws NoSuchMethodException {
    Constructor<String> constructor = String.class.getDeclaredConstructor(byte[].class);
    // The constructor is normally not accessible, but tryMakeAccessible should make it accessible and return null
    String result = ReflectionHelper.tryMakeAccessible(constructor);
    assertNull(result);
    assertTrue(constructor.canAccess(null));
  }

  @Test
    @Timeout(8000)
  void tryMakeAccessible_failure() throws Exception {
    // Create a real Constructor instance to avoid NullPointerException in constructorToString
    Constructor<String> realConstructor = String.class.getDeclaredConstructor(byte[].class);

    // Create a spy of the real constructor so we can mock setAccessible(true) to throw
    Constructor<?> constructor = spy(realConstructor);

    doThrow(new SecurityException("no access allowed")).when(constructor).setAccessible(true);

    // Override toString to a fixed value for the error message
    doReturn("mockConstructor").when(constructor).toString();

    String message = ReflectionHelper.tryMakeAccessible(constructor);

    assertNotNull(message);
    assertTrue(message.contains("Failed making constructor 'mockConstructor' accessible"));
    assertTrue(message.contains("no access allowed"));

    // The constructor should not be accessible after failure
    // Because constructor is a spy, canAccess(null) will call real method and return false,
    // so assertFalse is correct here
    assertFalse(constructor.canAccess(null));
  }
}