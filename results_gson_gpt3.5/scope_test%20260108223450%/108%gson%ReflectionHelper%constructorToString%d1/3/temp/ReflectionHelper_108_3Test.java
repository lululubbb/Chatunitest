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

class ReflectionHelper_108_3Test {

  @Test
    @Timeout(8000)
  void testConstructorToString_noParameters() throws NoSuchMethodException {
    Constructor<SampleClass> constructor = SampleClass.class.getDeclaredConstructor();
    String expected = SampleClass.class.getName() + "()";
    String actual = ReflectionHelper.constructorToString(constructor);
    assertEquals(expected, actual);
  }

  @Test
    @Timeout(8000)
  void testConstructorToString_withParameters() throws NoSuchMethodException {
    Constructor<SampleClass> constructor = SampleClass.class.getDeclaredConstructor(String.class, int.class);
    String expected = SampleClass.class.getName() + "(java.lang.String,int)";
    String actual = ReflectionHelper.constructorToString(constructor);
    assertEquals(expected, actual);
  }

  private static class SampleClass {
    public SampleClass() {}

    public SampleClass(String s, int i) {}
  }
}