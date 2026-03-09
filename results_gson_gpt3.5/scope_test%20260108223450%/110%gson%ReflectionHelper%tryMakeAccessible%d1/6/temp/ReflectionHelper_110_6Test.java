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
import java.lang.reflect.Parameter;

import org.junit.jupiter.api.Test;

class ReflectionHelper_110_6Test {

  @Test
    @Timeout(8000)
  void tryMakeAccessible_success() throws NoSuchMethodException {
    Constructor<String> constructor = String.class.getDeclaredConstructor(byte[].class);

    // It should be accessible without exception
    String result = ReflectionHelper.tryMakeAccessible(constructor);
    assertNull(result);
    assertTrue(constructor.canAccess(null));
  }

  @Test
    @Timeout(8000)
  void tryMakeAccessible_failure() throws Exception {
    // Create a mock constructor that throws when setAccessible(true) is called
    @SuppressWarnings("unchecked")
    Constructor<?> mockConstructor = mock(Constructor.class);

    // Use raw Class<?> to avoid generic capture issues
    when(mockConstructor.getDeclaringClass()).thenReturn((Class) String.class);

    // Mock getParameters to return a non-null array to avoid NPE in appendExecutableParameters
    Parameter mockParameter = mock(Parameter.class);
    when(mockParameter.getType()).thenReturn((Class) byte[].class);
    when(mockParameter.getName()).thenReturn("arg0");
    when(mockConstructor.getParameters()).thenReturn(new Parameter[] { mockParameter });

    // Mock toString to return a string used in the error message
    when(mockConstructor.toString()).thenReturn("mockConstructorToString");

    // Throw SecurityException when setAccessible(true) is called
    doThrow(new SecurityException("not allowed")).when(mockConstructor).setAccessible(true);

    String message = ReflectionHelper.tryMakeAccessible(mockConstructor);

    assertNotNull(message);
    assertTrue(message.contains("Failed making constructor"));
    assertTrue(message.contains("mockConstructorToString"));
    assertTrue(message.contains("not allowed"));
  }
}