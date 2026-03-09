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

class ReflectionHelper_107_1Test {

  @Test
    @Timeout(8000)
  void testFieldToString() throws NoSuchFieldException {
    // Arrange
    Field field = SampleClass.class.getDeclaredField("sampleField");

    // Act
    String result = ReflectionHelper.fieldToString(field);

    // Assert
    assertEquals(field.getDeclaringClass().getName() + "#sampleField", result);
  }

  // Helper class to provide a field for testing
  private static class SampleClass {
    private int sampleField;
  }
}