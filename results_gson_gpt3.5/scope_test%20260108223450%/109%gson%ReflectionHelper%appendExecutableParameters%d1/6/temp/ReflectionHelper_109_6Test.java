package com.google.gson.internal.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonIOException;
import com.google.gson.internal.GsonBuildConfig;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.InvocationTargetException;

class ReflectionHelper_109_6Test {

  // A sample method to use for testing
  public static class SampleClass {
    public void methodNoParams() {}
    public void methodOneParam(int a) {}
    public void methodTwoParams(int a, String b) {}
  }

  // A sample constructor class to use for testing
  public static class SampleConstructorClass {
    public SampleConstructorClass() {}
    public SampleConstructorClass(int a) {}
    public SampleConstructorClass(int a, String b) {}
  }

  @Test
    @Timeout(8000)
  void appendExecutableParameters_withMethodNoParams() throws Exception {
    Method method = SampleClass.class.getMethod("methodNoParams");
    StringBuilder sb = new StringBuilder();

    invokeAppendExecutableParameters(method, sb);

    assertEquals("()", sb.toString());
  }

  @Test
    @Timeout(8000)
  void appendExecutableParameters_withMethodOneParam() throws Exception {
    Method method = SampleClass.class.getMethod("methodOneParam", int.class);
    StringBuilder sb = new StringBuilder();

    invokeAppendExecutableParameters(method, sb);

    assertEquals("(int)", sb.toString());
  }

  @Test
    @Timeout(8000)
  void appendExecutableParameters_withMethodTwoParams() throws Exception {
    Method method = SampleClass.class.getMethod("methodTwoParams", int.class, String.class);
    StringBuilder sb = new StringBuilder();

    invokeAppendExecutableParameters(method, sb);

    assertEquals("(int, String)", sb.toString());
  }

  @Test
    @Timeout(8000)
  void appendExecutableParameters_withConstructorNoParams() throws Exception {
    Constructor<SampleConstructorClass> constructor = SampleConstructorClass.class.getConstructor();
    StringBuilder sb = new StringBuilder();

    invokeAppendExecutableParameters(constructor, sb);

    assertEquals("()", sb.toString());
  }

  @Test
    @Timeout(8000)
  void appendExecutableParameters_withConstructorOneParam() throws Exception {
    Constructor<SampleConstructorClass> constructor = SampleConstructorClass.class.getConstructor(int.class);
    StringBuilder sb = new StringBuilder();

    invokeAppendExecutableParameters(constructor, sb);

    assertEquals("(int)", sb.toString());
  }

  @Test
    @Timeout(8000)
  void appendExecutableParameters_withConstructorTwoParams() throws Exception {
    Constructor<SampleConstructorClass> constructor = SampleConstructorClass.class.getConstructor(int.class, String.class);
    StringBuilder sb = new StringBuilder();

    invokeAppendExecutableParameters(constructor, sb);

    assertEquals("(int, String)", sb.toString());
  }

  private void invokeAppendExecutableParameters(AccessibleObject executable, StringBuilder sb) throws Exception {
    // Using reflection to invoke private static method appendExecutableParameters
    var method = ReflectionHelper.class.getDeclaredMethod("appendExecutableParameters", AccessibleObject.class, StringBuilder.class);
    method.setAccessible(true);
    method.invoke(null, executable, sb);
  }
}