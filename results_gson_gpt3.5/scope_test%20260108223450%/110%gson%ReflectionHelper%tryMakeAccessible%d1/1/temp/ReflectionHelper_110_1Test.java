package com.google.gson.internal.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonIOException;
import com.google.gson.internal.GsonBuildConfig;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class ReflectionHelper_110_1Test {

  @Test
    @Timeout(8000)
  void tryMakeAccessible_success() throws NoSuchMethodException {
    Constructor<SampleClass> constructor = SampleClass.class.getDeclaredConstructor();
    // Should be accessible after tryMakeAccessible
    String result = ReflectionHelper.tryMakeAccessible(constructor);
    assertNull(result);
    assertTrue(constructor.canAccess(null));
  }

  @Test
    @Timeout(8000)
  void tryMakeAccessible_failure() throws NoSuchMethodException {
    // Create a spy of Constructor to throw exception on setAccessible
    Constructor<SampleClass> constructor = SampleClass.class.getDeclaredConstructor();
    Constructor<?> spyConstructor = Mockito.spy(constructor);
    Mockito.doThrow(new SecurityException("not allowed")).when(spyConstructor).setAccessible(true);

    String result = ReflectionHelper.tryMakeAccessible(spyConstructor);
    assertNotNull(result);
    assertTrue(result.contains("Failed making constructor"));
    assertTrue(result.contains("not allowed"));
  }

  static class SampleClass {
    private SampleClass() {}
  }
}