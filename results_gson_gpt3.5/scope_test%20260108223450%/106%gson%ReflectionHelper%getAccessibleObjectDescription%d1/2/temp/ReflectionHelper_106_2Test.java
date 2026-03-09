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
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class ReflectionHelper_106_2Test {

  @Test
    @Timeout(8000)
  void testGetAccessibleObjectDescription_field_lowercaseFalse() throws NoSuchFieldException {
    Field field = SampleClass.class.getDeclaredField("field");
    // spy to mock fieldToString static method
    try (MockedStatic<ReflectionHelper> mocked = Mockito.mockStatic(ReflectionHelper.class, Mockito.CALLS_REAL_METHODS)) {
      mocked.when(() -> ReflectionHelper.fieldToString(field)).thenReturn("SampleClass.field");
      String description = ReflectionHelper.getAccessibleObjectDescription(field, false);
      assertEquals("field 'SampleClass.field'", description);
    }
  }

  @Test
    @Timeout(8000)
  void testGetAccessibleObjectDescription_field_uppercaseTrue() throws NoSuchFieldException {
    Field field = SampleClass.class.getDeclaredField("field");
    try (MockedStatic<ReflectionHelper> mocked = Mockito.mockStatic(ReflectionHelper.class, Mockito.CALLS_REAL_METHODS)) {
      mocked.when(() -> ReflectionHelper.fieldToString(field)).thenReturn("sampleClass.field");
      String description = ReflectionHelper.getAccessibleObjectDescription(field, true);
      assertEquals("Field 'sampleClass.field'", description);
    }
  }

  @Test
    @Timeout(8000)
  void testGetAccessibleObjectDescription_method_lowercaseFalse() throws NoSuchMethodException {
    Method method = SampleClass.class.getDeclaredMethod("method", String.class);

    String description = ReflectionHelper.getAccessibleObjectDescription(method, false);
    assertTrue(description.startsWith("method '"));
    assertTrue(description.contains("#method("));
    assertTrue(description.contains("java.lang.String"));
    assertTrue(description.endsWith("')"));
  }

  @Test
    @Timeout(8000)
  void testGetAccessibleObjectDescription_method_uppercaseTrue() throws NoSuchMethodException {
    Method method = SampleClass.class.getDeclaredMethod("method", String.class);

    String description = ReflectionHelper.getAccessibleObjectDescription(method, true);
    assertTrue(description.startsWith("Method '"));
    assertTrue(description.contains("#method("));
    assertTrue(description.contains("java.lang.String"));
    assertTrue(description.endsWith("')"));
  }

  @Test
    @Timeout(8000)
  void testGetAccessibleObjectDescription_constructor_lowercaseFalse() throws NoSuchMethodException {
    Constructor<SampleClass> constructor = SampleClass.class.getDeclaredConstructor(String.class);
    try (MockedStatic<ReflectionHelper> mocked = Mockito.mockStatic(ReflectionHelper.class, Mockito.CALLS_REAL_METHODS)) {
      mocked.when(() -> ReflectionHelper.constructorToString(constructor)).thenReturn("SampleClass(String)");
      String description = ReflectionHelper.getAccessibleObjectDescription(constructor, false);
      assertEquals("constructor 'SampleClass(String)'", description);
    }
  }

  @Test
    @Timeout(8000)
  void testGetAccessibleObjectDescription_constructor_uppercaseTrue() throws NoSuchMethodException {
    Constructor<SampleClass> constructor = SampleClass.class.getDeclaredConstructor(String.class);
    try (MockedStatic<ReflectionHelper> mocked = Mockito.mockStatic(ReflectionHelper.class, Mockito.CALLS_REAL_METHODS)) {
      mocked.when(() -> ReflectionHelper.constructorToString(constructor)).thenReturn("sampleClass(String)");
      String description = ReflectionHelper.getAccessibleObjectDescription(constructor, true);
      assertEquals("Constructor 'sampleClass(String)'", description);
    }
  }

  @Test
    @Timeout(8000)
  void testGetAccessibleObjectDescription_unknownAccessibleObject_lowercaseFalse() {
    AccessibleObject unknown = new AccessibleObject() {};
    String description = ReflectionHelper.getAccessibleObjectDescription(unknown, false);
    assertTrue(description.startsWith("<unknown AccessibleObject>"));
    assertTrue(description.contains(unknown.toString()));
  }

  @Test
    @Timeout(8000)
  void testGetAccessibleObjectDescription_unknownAccessibleObject_uppercaseTrue() {
    AccessibleObject unknown = new AccessibleObject() {
      @Override
      public String toString() {
        return "unknownObject";
      }
    };
    String description = ReflectionHelper.getAccessibleObjectDescription(unknown, true);
    assertTrue(description.startsWith("<unknown AccessibleObject>"));
    assertTrue(description.contains("unknownObject"));
    // uppercase first letter check
    assertTrue(Character.isUpperCase(description.charAt(0)));
  }

  // Sample class for reflection
  static class SampleClass {
    private String field;

    public SampleClass(String field) {
      this.field = field;
    }

    public void method(String param) {
    }
  }
}