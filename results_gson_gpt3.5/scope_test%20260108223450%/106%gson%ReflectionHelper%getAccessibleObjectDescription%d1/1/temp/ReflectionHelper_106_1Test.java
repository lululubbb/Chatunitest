package com.google.gson.internal.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonIOException;
import com.google.gson.internal.GsonBuildConfig;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

class ReflectionHelper_106_1Test {

  @Test
    @Timeout(8000)
  void testGetAccessibleObjectDescription_field_lowercaseFalse() throws NoSuchFieldException {
    Field field = SampleClass.class.getDeclaredField("sampleField");
    String description = ReflectionHelper.getAccessibleObjectDescription(field, false);
    assertEquals("field '" + ReflectionHelper.fieldToString(field) + "'", description);
  }

  @Test
    @Timeout(8000)
  void testGetAccessibleObjectDescription_field_uppercaseTrue() throws NoSuchFieldException {
    Field field = SampleClass.class.getDeclaredField("sampleField");
    String description = ReflectionHelper.getAccessibleObjectDescription(field, true);
    assertEquals("Field '" + ReflectionHelper.fieldToString(field) + "'", description);
  }

  @Test
    @Timeout(8000)
  void testGetAccessibleObjectDescription_method_lowercaseFalse() throws NoSuchMethodException {
    Method method = SampleClass.class.getDeclaredMethod("sampleMethod", String.class, int.class);
    String description = ReflectionHelper.getAccessibleObjectDescription(method, false);
    // method signature: sampleMethod(String,int)
    assertTrue(description.startsWith("method '"));
    assertTrue(description.contains(SampleClass.class.getName() + "#sampleMethod"));
    assertTrue(description.endsWith("'"));
  }

  @Test
    @Timeout(8000)
  void testGetAccessibleObjectDescription_method_uppercaseTrue() throws NoSuchMethodException {
    Method method = SampleClass.class.getDeclaredMethod("sampleMethod", String.class, int.class);
    String description = ReflectionHelper.getAccessibleObjectDescription(method, true);
    assertTrue(description.startsWith("Method '"));
    assertTrue(description.contains(SampleClass.class.getName() + "#sampleMethod"));
    assertTrue(description.endsWith("'"));
  }

  @Test
    @Timeout(8000)
  void testGetAccessibleObjectDescription_constructor_lowercaseFalse() throws NoSuchMethodException {
    Constructor<SampleClass> constructor = SampleClass.class.getDeclaredConstructor(String.class, int.class);
    String description = ReflectionHelper.getAccessibleObjectDescription(constructor, false);
    assertTrue(description.startsWith("constructor '"));
    assertTrue(description.contains(SampleClass.class.getName() + "("));
    assertTrue(description.endsWith("'"));
  }

  @Test
    @Timeout(8000)
  void testGetAccessibleObjectDescription_constructor_uppercaseTrue() throws NoSuchMethodException {
    Constructor<SampleClass> constructor = SampleClass.class.getDeclaredConstructor(String.class, int.class);
    String description = ReflectionHelper.getAccessibleObjectDescription(constructor, true);
    assertTrue(description.startsWith("Constructor '"));
    assertTrue(description.contains(SampleClass.class.getName() + "("));
    assertTrue(description.endsWith("'"));
  }

  @Test
    @Timeout(8000)
  void testGetAccessibleObjectDescription_unknownAccessibleObject_lowercaseFalse() {
    AccessibleObject unknown = new AccessibleObject() {};
    String description = ReflectionHelper.getAccessibleObjectDescription(unknown, false);
    assertTrue(description.startsWith("<unknown AccessibleObject> "));
    assertTrue(description.contains(unknown.toString()));
  }

  @Test
    @Timeout(8000)
  void testGetAccessibleObjectDescription_unknownAccessibleObject_uppercaseTrue() {
    AccessibleObject unknown = new AccessibleObject() {
      @Override
      public String toString() {
        return "unknown-object";
      }
    };
    String description = ReflectionHelper.getAccessibleObjectDescription(unknown, true);
    assertTrue(description.startsWith("<unknown AccessibleObject> "));
    assertTrue(description.contains("unknown-object"));
  }

  @Test
    @Timeout(8000)
  void testGetAccessibleObjectDescription_uppercaseFirstLetterDoesNotChangeIfAlreadyUppercase() throws NoSuchMethodException {
    Method method = SampleClass.class.getDeclaredMethod("sampleMethod", String.class, int.class);
    String description = ReflectionHelper.getAccessibleObjectDescription(method, true);
    assertTrue(description.startsWith("Method '"));
  }

  // Helper class for tests
  static class SampleClass {
    int sampleField;

    public SampleClass(String s, int i) {}

    public void sampleMethod(String s, int i) {}
  }
}