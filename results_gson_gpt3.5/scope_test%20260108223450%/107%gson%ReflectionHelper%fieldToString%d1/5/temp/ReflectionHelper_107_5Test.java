package com.google.gson.internal.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonIOException;
import com.google.gson.internal.GsonBuildConfig;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

class ReflectionHelper_107_5Test {

  @Test
    @Timeout(8000)
  void fieldToString_shouldReturnDeclaringClassNameAndFieldName() throws NoSuchFieldException {
    Field field = SampleClass.class.getDeclaredField("sampleField");
    String expected = SampleClass.class.getName() + "#sampleField";
    String actual = ReflectionHelper.fieldToString(field);
    assertEquals(expected, actual);
  }

  private static class SampleClass {
    private int sampleField;
  }
}