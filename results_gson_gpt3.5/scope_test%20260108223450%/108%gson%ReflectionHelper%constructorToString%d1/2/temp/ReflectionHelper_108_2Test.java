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

class ReflectionHelper_108_2Test {

  // A sample class with constructors to test constructorToString
  static class SampleClass {
    public SampleClass() {}
    public SampleClass(int a, String b) {}
  }

  @Test
    @Timeout(8000)
  void testConstructorToString_noParams() throws Exception {
    Constructor<SampleClass> constructor = SampleClass.class.getConstructor();
    String result = ReflectionHelper.constructorToString(constructor);
    // Expected: fully qualified class name + ()
    assertEquals(SampleClass.class.getName() + "()", result);
  }

  @Test
    @Timeout(8000)
  void testConstructorToString_withParams() throws Exception {
    Constructor<SampleClass> constructor = SampleClass.class.getConstructor(int.class, String.class);
    String result = ReflectionHelper.constructorToString(constructor);
    // Expected: fully qualified class name + (int,java.lang.String)
    String expected = SampleClass.class.getName() + "(int,java.lang.String)";
    assertEquals(expected, result);
  }

  static class PrivateCtorClass {
    @SuppressWarnings("unused")
    private PrivateCtorClass(double d) {}
  }

  @Test
    @Timeout(8000)
  void testConstructorToString_withPrivateConstructor() throws Exception {
    Class<?> privateCtorClass = PrivateCtorClass.class;
    Constructor<?> constructor = privateCtorClass.getDeclaredConstructor(double.class);
    constructor.setAccessible(true);
    String result = ReflectionHelper.constructorToString(constructor);
    assertEquals(privateCtorClass.getName() + "(double)", result);
  }

  @Test
    @Timeout(8000)
  void testAppendExecutableParameters_viaReflection() throws Exception {
    Constructor<SampleClass> constructor = SampleClass.class.getConstructor();
    StringBuilder sb = new StringBuilder("prefix");
    // Use reflection to invoke private static method appendExecutableParameters
    var method = ReflectionHelper.class.getDeclaredMethod("appendExecutableParameters",
        java.lang.reflect.AccessibleObject.class, StringBuilder.class);
    method.setAccessible(true);
    method.invoke(null, constructor, sb);
    assertEquals("prefix()", sb.toString());
  }
}