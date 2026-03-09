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

class ReflectionHelper_106_4Test {

  @Test
    @Timeout(8000)
  void testGetAccessibleObjectDescription_Field() throws NoSuchFieldException {
    Field field = SampleClass.class.getDeclaredField("sampleField");
    String description = ReflectionHelper.getAccessibleObjectDescription(field, false);
    assertEquals("field '" + SampleClass.class.getName() + "#sampleField'", description);

    String descriptionUpper = ReflectionHelper.getAccessibleObjectDescription(field, true);
    assertEquals("Field '" + SampleClass.class.getName() + "#sampleField'", descriptionUpper);
  }

  @Test
    @Timeout(8000)
  void testGetAccessibleObjectDescription_Method() throws NoSuchMethodException {
    Method method = SampleClass.class.getDeclaredMethod("sampleMethod", String.class, int.class);
    String description = ReflectionHelper.getAccessibleObjectDescription(method, false);
    assertTrue(description.startsWith("method '" + SampleClass.class.getName() + "#sampleMethod("));
    assertTrue(description.endsWith(")'"));

    String descriptionUpper = ReflectionHelper.getAccessibleObjectDescription(method, true);
    assertTrue(descriptionUpper.startsWith("Method '" + SampleClass.class.getName() + "#sampleMethod("));
    assertTrue(descriptionUpper.endsWith(")'"));
  }

  @Test
    @Timeout(8000)
  void testGetAccessibleObjectDescription_Constructor() throws NoSuchMethodException {
    Constructor<SampleClass> constructor = SampleClass.class.getDeclaredConstructor(String.class);
    String description = ReflectionHelper.getAccessibleObjectDescription(constructor, false);
    assertTrue(description.startsWith("constructor '"));
    assertTrue(description.contains(SampleClass.class.getName() + "#<init>(java.lang.String)"));

    String descriptionUpper = ReflectionHelper.getAccessibleObjectDescription(constructor, true);
    assertTrue(descriptionUpper.startsWith("Constructor '"));
    assertTrue(descriptionUpper.contains(SampleClass.class.getName() + "#<init>(java.lang.String)"));
  }

  @Test
    @Timeout(8000)
  void testGetAccessibleObjectDescription_UnknownAccessibleObject() {
    AccessibleObject anonAccessibleObject = new AccessibleObject() {
      @Override
      public String toString() {
        return "anonymousAccessibleObject";
      }
    };
    String description = ReflectionHelper.getAccessibleObjectDescription(anonAccessibleObject, false);
    assertEquals("<unknown AccessibleObject> anonymousAccessibleObject", description);

    String descriptionUpper = ReflectionHelper.getAccessibleObjectDescription(anonAccessibleObject, true);
    assertEquals("<unknown AccessibleObject> anonymousAccessibleObject", descriptionUpper);
  }

  // Sample class with members for reflection tests
  static class SampleClass {
    int sampleField;

    public SampleClass(String s) {}

    public void sampleMethod(String s, int i) {}
  }
}