package com.google.gson.internal.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonIOException;
import com.google.gson.internal.GsonBuildConfig;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

class ReflectionHelper_109_2Test {

  @Test
    @Timeout(8000)
  void appendExecutableParameters_withMethod_noParameters() throws Exception {
    Method method = SampleClass.class.getDeclaredMethod("methodNoParams");
    StringBuilder sb = new StringBuilder();

    invokeAppendExecutableParameters(method, sb);

    assertEquals("()", sb.toString());
  }

  @Test
    @Timeout(8000)
  void appendExecutableParameters_withMethod_withParameters() throws Exception {
    Method method = SampleClass.class.getDeclaredMethod("methodWithParams", int.class, String.class);
    StringBuilder sb = new StringBuilder();

    invokeAppendExecutableParameters(method, sb);

    assertEquals("(int, String)", sb.toString());
  }

  @Test
    @Timeout(8000)
  void appendExecutableParameters_withConstructor_noParameters() throws Exception {
    Constructor<SampleClass> constructor = SampleClass.class.getDeclaredConstructor();
    StringBuilder sb = new StringBuilder();

    invokeAppendExecutableParameters(constructor, sb);

    assertEquals("()", sb.toString());
  }

  @Test
    @Timeout(8000)
  void appendExecutableParameters_withConstructor_withParameters() throws Exception {
    Constructor<SampleClass> constructor = SampleClass.class.getDeclaredConstructor(int.class, String.class);
    StringBuilder sb = new StringBuilder();

    invokeAppendExecutableParameters(constructor, sb);

    assertEquals("(int, String)", sb.toString());
  }

  @Test
    @Timeout(8000)
  void appendExecutableParameters_withAnonymousAccessibleObject() throws Exception {
    AccessibleObject anonymous = new AccessibleObject() {};
    StringBuilder sb = new StringBuilder();

    InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
      invokeAppendExecutableParameters(anonymous, sb);
    });
    Throwable cause = exception.getCause();
    assertTrue(cause instanceof ClassCastException);
    assertNotNull(cause.getMessage());
  }

  private void invokeAppendExecutableParameters(AccessibleObject executable, StringBuilder stringBuilder) throws Exception {
    Method method = ReflectionHelper.class.getDeclaredMethod("appendExecutableParameters", AccessibleObject.class, StringBuilder.class);
    method.setAccessible(true);
    method.invoke(null, executable, stringBuilder);
  }

  // Sample class used for testing
  static class SampleClass {
    public SampleClass() {}
    public SampleClass(int a, String b) {}

    public void methodNoParams() {}
    public void methodWithParams(int a, String b) {}
  }
}