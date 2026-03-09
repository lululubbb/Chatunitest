package com.google.gson.internal.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonIOException;
import com.google.gson.internal.GsonBuildConfig;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Constructor;
import org.junit.jupiter.api.Test;

class ReflectionHelper_108_5Test {

  static class TestClassNoParams {
    public TestClassNoParams() {}
  }

  static class TestClassWithParams {
    public TestClassWithParams(int a, String b) {}
  }

  static class TestClassArrayParam {
    public TestClassArrayParam(int[] arr) {}
  }

  private String getExpectedClassName(Class<?> clazz) {
    // Return the actual class name of the class object passed in, to avoid mismatch in test class names.
    return clazz.getName();
  }

  @Test
    @Timeout(8000)
  void testConstructorToString_noParameters() throws Exception {
    Constructor<TestClassNoParams> constructor = TestClassNoParams.class.getConstructor();

    String result = ReflectionHelper.constructorToString(constructor);

    assertEquals(getExpectedClassName(TestClassNoParams.class) + "()", result);
  }

  @Test
    @Timeout(8000)
  void testConstructorToString_withParameters() throws Exception {
    Constructor<TestClassWithParams> constructor =
        TestClassWithParams.class.getConstructor(int.class, String.class);

    String result = ReflectionHelper.constructorToString(constructor);

    // Remove all whitespace from result to avoid formatting issues
    String normalizedResult = result.replaceAll("\\s+", "");
    assertEquals(
        getExpectedClassName(TestClassWithParams.class) + "(int,java.lang.String)",
        normalizedResult);
  }

  @Test
    @Timeout(8000)
  void testConstructorToString_withArrayParameter() throws Exception {
    Constructor<TestClassArrayParam> constructor =
        TestClassArrayParam.class.getConstructor(int[].class);

    String result = ReflectionHelper.constructorToString(constructor);

    assertEquals(
        getExpectedClassName(TestClassArrayParam.class) + "(int[])",
        result);
  }
}