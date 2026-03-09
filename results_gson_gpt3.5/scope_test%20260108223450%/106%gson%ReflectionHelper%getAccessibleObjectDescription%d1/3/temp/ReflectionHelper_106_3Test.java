package com.google.gson.internal.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonIOException;
import com.google.gson.internal.GsonBuildConfig;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

class ReflectionHelper_106_3Test {

  @Test
    @Timeout(8000)
  void testGetAccessibleObjectDescription_withField_uppercaseFalse() throws NoSuchFieldException {
    Field field = SampleClass.class.getDeclaredField("field");
    String description = ReflectionHelper.getAccessibleObjectDescription(field, false);
    assertEquals("field '" + ReflectionHelper.fieldToString(field) + "'", description);
  }

  @Test
    @Timeout(8000)
  void testGetAccessibleObjectDescription_withField_uppercaseTrue() throws NoSuchFieldException {
    Field field = SampleClass.class.getDeclaredField("field");
    String description = ReflectionHelper.getAccessibleObjectDescription(field, true);
    assertEquals("Field '" + ReflectionHelper.fieldToString(field) + "'", description);
  }

  @Test
    @Timeout(8000)
  void testGetAccessibleObjectDescription_withMethod_uppercaseFalse() throws NoSuchMethodException {
    Method method = SampleClass.class.getDeclaredMethod("method", String.class, int.class);
    String description = ReflectionHelper.getAccessibleObjectDescription(method, false);
    String expectedStart = "method '" + method.getDeclaringClass().getName() + "#" + method.getName() + "(";
    assertTrue(description.startsWith(expectedStart));
    assertTrue(description.endsWith(")'"));
  }

  @Test
    @Timeout(8000)
  void testGetAccessibleObjectDescription_withMethod_uppercaseTrue() throws NoSuchMethodException {
    Method method = SampleClass.class.getDeclaredMethod("method", String.class, int.class);
    String description = ReflectionHelper.getAccessibleObjectDescription(method, true);
    String expectedStart = "Method '" + method.getDeclaringClass().getName() + "#" + method.getName() + "(";
    assertTrue(description.startsWith(expectedStart));
    assertTrue(description.endsWith(")'"));
  }

  @Test
    @Timeout(8000)
  void testGetAccessibleObjectDescription_withConstructor_uppercaseFalse() throws NoSuchMethodException {
    Constructor<SampleClass> constructor = SampleClass.class.getDeclaredConstructor(String.class, int.class);
    String description = ReflectionHelper.getAccessibleObjectDescription(constructor, false);
    String expectedStart = "constructor '" + ReflectionHelper.constructorToString(constructor);
    assertTrue(description.startsWith(expectedStart));
    assertTrue(description.endsWith(")'"));
  }

  @Test
    @Timeout(8000)
  void testGetAccessibleObjectDescription_withConstructor_uppercaseTrue() throws NoSuchMethodException {
    Constructor<SampleClass> constructor = SampleClass.class.getDeclaredConstructor(String.class, int.class);
    String description = ReflectionHelper.getAccessibleObjectDescription(constructor, true);
    String expectedStart = "Constructor '" + ReflectionHelper.constructorToString(constructor);
    assertTrue(description.startsWith(expectedStart));
    assertTrue(description.endsWith(")'"));
  }

  @Test
    @Timeout(8000)
  void testGetAccessibleObjectDescription_withUnknownAccessibleObject_uppercaseFalse() {
    AccessibleObject unknown = new AccessibleObject() {};
    String description = ReflectionHelper.getAccessibleObjectDescription(unknown, false);
    assertEquals("<unknown AccessibleObject> " + unknown.toString(), description);
  }

  @Test
    @Timeout(8000)
  void testGetAccessibleObjectDescription_withUnknownAccessibleObject_uppercaseTrue() {
    AccessibleObject unknown = new AccessibleObject() {};
    String description = ReflectionHelper.getAccessibleObjectDescription(unknown, true);
    String expected = "<unknown AccessibleObject> " + unknown.toString();
    // First char is '<' which is not lowercase letter, so no uppercase change
    assertEquals(expected, description);
  }

  // Sample class for testing reflection objects
  static class SampleClass {
    int field;

    public SampleClass(String s, int i) {}

    public void method(String s, int i) {}
  }
}