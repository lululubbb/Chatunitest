package com.google.gson.internal.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonIOException;
import com.google.gson.internal.GsonBuildConfig;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Constructor;
import org.junit.jupiter.api.Test;

class ReflectionHelper_108_6Test {

  @Test
    @Timeout(8000)
  void testConstructorToString_withNoParameters() throws NoSuchMethodException {
    Constructor<String> constructor = String.class.getConstructor();
    String expected = String.class.getName() + "()";
    String actual = ReflectionHelper.constructorToString(constructor);
    assertEquals(expected, actual);
  }

  @Test
    @Timeout(8000)
  void testConstructorToString_withParameters() throws NoSuchMethodException {
    Constructor<String> constructor = String.class.getConstructor(byte[].class);
    String expected = String.class.getName() + "(byte[])";
    String actual = ReflectionHelper.constructorToString(constructor);
    assertEquals(expected, actual);
  }

  @Test
    @Timeout(8000)
  void testConstructorToString_withMultipleParameters() throws NoSuchMethodException {
    Constructor<String> constructor = String.class.getConstructor(byte[].class, int.class, int.class);
    String expected = String.class.getName() + "(byte[], int, int)";
    String actual = ReflectionHelper.constructorToString(constructor);
    assertEquals(expected, actual);
  }

  @Test
    @Timeout(8000)
  void testConstructorToString_withMockedConstructor() throws Exception {
    @SuppressWarnings("unchecked")
    Constructor<?> constructor = mock(Constructor.class);
    Class<?> declaringClass = String.class;

    // Cast to (Class) to avoid generic capture issues
    when(constructor.getDeclaringClass()).thenReturn((Class) declaringClass);
    when(constructor.getParameterTypes()).thenReturn(new Class[] {int.class, String.class});

    // Use reflection to invoke private static method appendExecutableParameters
    java.lang.reflect.Method appendMethod = ReflectionHelper.class.getDeclaredMethod(
        "appendExecutableParameters", java.lang.reflect.AccessibleObject.class, StringBuilder.class);
    appendMethod.setAccessible(true);

    StringBuilder sb = new StringBuilder(declaringClass.getName());
    appendMethod.invoke(null, constructor, sb);
    String expected = sb.toString();

    String actual = ReflectionHelper.constructorToString(constructor);
    assertEquals(expected, actual);
  }
}