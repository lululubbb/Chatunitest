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

import org.junit.jupiter.api.Test;

class ReflectionHelper_109_5Test {

  @Test
    @Timeout(8000)
  void testAppendExecutableParameters_withMethod_noParameters() throws Exception {
    Method method = SampleClass.class.getDeclaredMethod("noParamMethod");
    StringBuilder sb = new StringBuilder();

    invokeAppendExecutableParameters(method, sb);

    assertEquals("()", sb.toString());
  }

  @Test
    @Timeout(8000)
  void testAppendExecutableParameters_withMethod_multipleParameters() throws Exception {
    Method method = SampleClass.class.getDeclaredMethod("multiParamMethod", int.class, String.class);
    StringBuilder sb = new StringBuilder();

    invokeAppendExecutableParameters(method, sb);

    assertEquals("(int, String)", sb.toString());
  }

  @Test
    @Timeout(8000)
  void testAppendExecutableParameters_withConstructor_noParameters() throws Exception {
    Constructor<SampleClass> constructor = SampleClass.class.getDeclaredConstructor();
    StringBuilder sb = new StringBuilder();

    invokeAppendExecutableParameters(constructor, sb);

    assertEquals("()", sb.toString());
  }

  @Test
    @Timeout(8000)
  void testAppendExecutableParameters_withConstructor_multipleParameters() throws Exception {
    Constructor<SampleClass> constructor = SampleClass.class.getDeclaredConstructor(int.class, String.class);
    StringBuilder sb = new StringBuilder();

    invokeAppendExecutableParameters(constructor, sb);

    assertEquals("(int, String)", sb.toString());
  }

  @Test
    @Timeout(8000)
  void testAppendExecutableParameters_withMockedAccessibleObject() throws Exception {
    // Mock AccessibleObject that is neither Method nor Constructor
    AccessibleObject mockObj = mock(AccessibleObject.class);
    StringBuilder sb = new StringBuilder();

    Exception exception = assertThrows(Exception.class, () -> invokeAppendExecutableParameters(mockObj, sb));
    Throwable cause = exception.getCause();
    assertNotNull(cause);
    assertTrue(cause instanceof ClassCastException);
  }

  private void invokeAppendExecutableParameters(AccessibleObject executable, StringBuilder sb) throws Exception {
    Method method = ReflectionHelper.class.getDeclaredMethod("appendExecutableParameters", AccessibleObject.class, StringBuilder.class);
    method.setAccessible(true);
    method.invoke(null, executable, sb);
  }

  // Sample class to use in tests
  static class SampleClass {
    public SampleClass() {}
    public SampleClass(int x, String y) {}

    public void noParamMethod() {}
    public void multiParamMethod(int x, String y) {}
  }
}