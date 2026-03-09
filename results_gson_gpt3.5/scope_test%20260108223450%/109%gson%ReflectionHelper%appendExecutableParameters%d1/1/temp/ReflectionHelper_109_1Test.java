package com.google.gson.internal.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonIOException;
import com.google.gson.internal.GsonBuildConfig;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Field;

class ReflectionHelper_109_1Test {

  @Test
    @Timeout(8000)
  void appendExecutableParameters_withMethod_noParameters() throws Throwable {
    Method method = DummyClass.class.getDeclaredMethod("noParamMethod");
    StringBuilder sb = new StringBuilder();

    invokeAppendExecutableParameters(method, sb);

    assertEquals("()", sb.toString());
  }

  @Test
    @Timeout(8000)
  void appendExecutableParameters_withMethod_withParameters() throws Throwable {
    Method method = DummyClass.class.getDeclaredMethod("methodWithParams", int.class, String.class);
    StringBuilder sb = new StringBuilder();

    invokeAppendExecutableParameters(method, sb);

    assertEquals("(int, String)", sb.toString());
  }

  @Test
    @Timeout(8000)
  void appendExecutableParameters_withConstructor_noParameters() throws Throwable {
    Constructor<DummyClass> constructor = DummyClass.class.getDeclaredConstructor();
    StringBuilder sb = new StringBuilder();

    invokeAppendExecutableParameters(constructor, sb);

    assertEquals("()", sb.toString());
  }

  @Test
    @Timeout(8000)
  void appendExecutableParameters_withConstructor_withParameters() throws Throwable {
    Constructor<DummyClass> constructor = DummyClass.class.getDeclaredConstructor(int.class, String.class);
    StringBuilder sb = new StringBuilder();

    invokeAppendExecutableParameters(constructor, sb);

    assertEquals("(int, String)", sb.toString());
  }

  @Test
    @Timeout(8000)
  void appendExecutableParameters_withAnonymousConstructor_withParameters() throws Throwable {
    Constructor<AnonymousClass> constructor = AnonymousClass.class.getDeclaredConstructor(double.class);
    StringBuilder sb = new StringBuilder();

    invokeAppendExecutableParameters(constructor, sb);

    assertEquals("(double)", sb.toString());
  }

  private static void invokeAppendExecutableParameters(AccessibleObject executable, StringBuilder sb) throws Throwable {
    try {
      java.lang.reflect.Method method = ReflectionHelper.class.getDeclaredMethod("appendExecutableParameters", AccessibleObject.class, StringBuilder.class);
      method.setAccessible(true);
      method.invoke(null, executable, sb);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }

  // Dummy class to test method and constructor signatures
  private static class DummyClass {
    public DummyClass() {}
    public DummyClass(int i, String s) {}
    public void noParamMethod() {}
    public void methodWithParams(int i, String s) {}
  }

  // Anonymous class for edge case testing
  private static class AnonymousClass {
    public AnonymousClass(double d) {}
  }
}