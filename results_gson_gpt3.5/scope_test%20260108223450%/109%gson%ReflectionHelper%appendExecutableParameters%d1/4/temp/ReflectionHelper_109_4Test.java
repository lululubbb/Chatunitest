package com.google.gson.internal.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonIOException;
import com.google.gson.internal.GsonBuildConfig;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ReflectionHelper_109_4Test {

  private static Method appendExecutableParametersMethod;

  @BeforeAll
  static void setUp() throws NoSuchMethodException {
    appendExecutableParametersMethod = ReflectionHelper.class.getDeclaredMethod(
        "appendExecutableParameters", AccessibleObject.class, StringBuilder.class);
    appendExecutableParametersMethod.setAccessible(true);
  }

  private static class SampleClass {
    public void methodNoParams() {}
    public void methodOneParam(int x) {}
    public void methodTwoParams(int x, String y) {}
    public SampleClass() {}
    public SampleClass(int x) {}
    public SampleClass(int x, String y) {}
  }

  @Test
    @Timeout(8000)
  void testAppendExecutableParametersWithMethodNoParams() throws Throwable {
    Method method = SampleClass.class.getMethod("methodNoParams");
    StringBuilder sb = new StringBuilder();

    appendExecutableParametersMethod.invoke(null, method, sb);

    assertEquals("()", sb.toString());
  }

  @Test
    @Timeout(8000)
  void testAppendExecutableParametersWithMethodOneParam() throws Throwable {
    Method method = SampleClass.class.getMethod("methodOneParam", int.class);
    StringBuilder sb = new StringBuilder();

    appendExecutableParametersMethod.invoke(null, method, sb);

    assertEquals("(int)", sb.toString());
  }

  @Test
    @Timeout(8000)
  void testAppendExecutableParametersWithMethodTwoParams() throws Throwable {
    Method method = SampleClass.class.getMethod("methodTwoParams", int.class, String.class);
    StringBuilder sb = new StringBuilder();

    appendExecutableParametersMethod.invoke(null, method, sb);

    assertEquals("(int, String)", sb.toString());
  }

  @Test
    @Timeout(8000)
  void testAppendExecutableParametersWithConstructorNoParams() throws Throwable {
    Constructor<SampleClass> constructor = SampleClass.class.getConstructor();
    StringBuilder sb = new StringBuilder();

    appendExecutableParametersMethod.invoke(null, constructor, sb);

    assertEquals("()", sb.toString());
  }

  @Test
    @Timeout(8000)
  void testAppendExecutableParametersWithConstructorOneParam() throws Throwable {
    Constructor<SampleClass> constructor = SampleClass.class.getConstructor(int.class);
    StringBuilder sb = new StringBuilder();

    appendExecutableParametersMethod.invoke(null, constructor, sb);

    assertEquals("(int)", sb.toString());
  }

  @Test
    @Timeout(8000)
  void testAppendExecutableParametersWithConstructorTwoParams() throws Throwable {
    Constructor<SampleClass> constructor = SampleClass.class.getConstructor(int.class, String.class);
    StringBuilder sb = new StringBuilder();

    appendExecutableParametersMethod.invoke(null, constructor, sb);

    assertEquals("(int, String)", sb.toString());
  }
}