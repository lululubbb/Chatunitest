package com.google.gson.internal.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonIOException;
import com.google.gson.internal.GsonBuildConfig;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReflectionHelper_108_4Test {

  private Constructor<?> constructor;

  @BeforeEach
  void setUp() throws NoSuchMethodException {
    constructor = SampleClass.class.getDeclaredConstructor(String.class, int.class);
  }

  @Test
    @Timeout(8000)
  void constructorToString_returnsCorrectString() throws Exception {
    String result = ReflectionHelper.constructorToString(constructor);
    assertTrue(result.startsWith(SampleClass.class.getName() + "("));
    assertTrue(result.contains("java.lang.String"));
    assertTrue(result.contains("int"));
    assertTrue(result.endsWith(")"));
  }

  @Test
    @Timeout(8000)
  void appendExecutableParameters_handlesNoParameters() throws Exception {
    Constructor<?> noParamConstructor = SampleClass.class.getDeclaredConstructor();
    StringBuilder sb = new StringBuilder(SampleClass.class.getName());

    // Use reflection to invoke private static method appendExecutableParameters
    Method method = ReflectionHelper.class.getDeclaredMethod("appendExecutableParameters",
        AccessibleObject.class, StringBuilder.class);
    method.setAccessible(true);
    method.invoke(null, noParamConstructor, sb);

    String result = sb.toString();
    assertEquals(SampleClass.class.getName() + "()", result);
  }

  @Test
    @Timeout(8000)
  void appendExecutableParameters_handlesMultipleParameters() throws Exception {
    StringBuilder sb = new StringBuilder(SampleClass.class.getName());

    Method method = ReflectionHelper.class.getDeclaredMethod("appendExecutableParameters",
        AccessibleObject.class, StringBuilder.class);
    method.setAccessible(true);
    method.invoke(null, constructor, sb);

    String result = sb.toString();
    assertTrue(result.startsWith(SampleClass.class.getName() + "("));
    assertTrue(result.contains("java.lang.String"));
    assertTrue(result.contains("int"));
    assertTrue(result.endsWith(")"));
  }

  // Sample class for testing
  static class SampleClass {
    public SampleClass() {}
    public SampleClass(String s, int i) {}
  }
}