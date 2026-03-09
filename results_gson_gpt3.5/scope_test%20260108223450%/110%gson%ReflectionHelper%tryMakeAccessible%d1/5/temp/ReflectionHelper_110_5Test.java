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

class ReflectionHelper_110_5Test {

  @Test
    @Timeout(8000)
  void tryMakeAccessible_success() throws NoSuchMethodException {
    Constructor<String> constructor = String.class.getDeclaredConstructor(byte[].class);
    String result = ReflectionHelper.tryMakeAccessible(constructor);
    assertNull(result, "Expected null when accessibility change succeeds");
    assertTrue(constructor.canAccess(null), "Constructor should be accessible");
  }

  @Test
    @Timeout(8000)
  void tryMakeAccessible_failure() throws Exception {
    // Create a real constructor instance to delegate calls to
    Constructor<String> realConstructor = String.class.getDeclaredConstructor(byte[].class);

    // Create a spy of the real constructor
    Constructor<String> spyConstructor = spy(realConstructor);

    // When setAccessible(true) is called on the spy, throw SecurityException
    doThrow(new SecurityException("access denied")).when(spyConstructor).setAccessible(true);

    // When toString() is called, delegate to real toString() to avoid mismatch in output
    doCallRealMethod().when(spyConstructor).toString();

    String result = ReflectionHelper.tryMakeAccessible(spyConstructor);
    assertNotNull(result);
    assertTrue(result.contains("Failed making constructor '"), "Message should contain failure notice");
    assertTrue(result.contains("access denied"), "Message should contain exception message");
  }
}