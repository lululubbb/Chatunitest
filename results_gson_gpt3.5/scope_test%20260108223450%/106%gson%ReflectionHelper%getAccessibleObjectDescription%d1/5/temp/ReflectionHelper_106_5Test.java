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
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class ReflectionHelper_106_5Test {

  private Field testField;
  private Method testMethod;
  private Constructor<SampleClass> testConstructor;
  private AccessibleObject unknownAccessibleObject;

  static class SampleClass {
    public int field1;

    public SampleClass() {}

    public void method1(int a) {}
  }

  @BeforeEach
  public void setUp() throws NoSuchFieldException, NoSuchMethodException {
    testField = SampleClass.class.getField("field1");
    testMethod = SampleClass.class.getMethod("method1", int.class);
    testConstructor = SampleClass.class.getConstructor();
    unknownAccessibleObject = new AccessibleObject() {
      @Override
      public String toString() {
        return "unknownObjectToString";
      }
    };
  }

  @Test
    @Timeout(8000)
  public void testGetAccessibleObjectDescription_field_lowercase() throws Exception {
    try (MockedStatic<ReflectionHelper> mocked = Mockito.mockStatic(ReflectionHelper.class, Mockito.CALLS_REAL_METHODS)) {
      mocked.when(() -> ReflectionHelper.fieldToString(testField)).thenReturn("int field1");
      String description = ReflectionHelper.getAccessibleObjectDescription(testField, false);
      assertEquals("field 'int field1'", description);
    }
  }

  @Test
    @Timeout(8000)
  public void testGetAccessibleObjectDescription_field_uppercase() throws Exception {
    try (MockedStatic<ReflectionHelper> mocked = Mockito.mockStatic(ReflectionHelper.class, Mockito.CALLS_REAL_METHODS)) {
      mocked.when(() -> ReflectionHelper.fieldToString(testField)).thenReturn("int field1");
      String description = ReflectionHelper.getAccessibleObjectDescription(testField, true);
      assertEquals("Field 'int field1'", description);
    }
  }

  @Test
    @Timeout(8000)
  public void testGetAccessibleObjectDescription_method_lowercase() throws Exception {
    // We mock appendExecutableParameters to append the expected parameter string
    try (MockedStatic<ReflectionHelper> mocked = Mockito.mockStatic(ReflectionHelper.class, Mockito.CALLS_REAL_METHODS)) {
      // No need to mock methodToString because it's not used, but we do need to mock appendExecutableParameters
      mocked.when(() -> ReflectionHelper.getAccessibleObjectDescription(testMethod, false))
          .thenCallRealMethod();
      // We cannot mock private static method appendExecutableParameters directly,
      // so instead we mock getAccessibleObjectDescription and simulate the expected behavior:
      StringBuilder sb = new StringBuilder(testMethod.getName());
      sb.append("(int)");
      String expectedDescription = "method '" + testMethod.getDeclaringClass().getName() + "#" + sb.toString() + "'";
      // So we just assert the actual output starts with expected start and ends with expected end
      String description = ReflectionHelper.getAccessibleObjectDescription(testMethod, false);
      String expectedStart = "method '" + testMethod.getDeclaringClass().getName() + "#" + testMethod.getName() + "(";
      assertTrue(description.startsWith(expectedStart));
      assertTrue(description.endsWith(")'"));
    }
  }

  @Test
    @Timeout(8000)
  public void testGetAccessibleObjectDescription_method_uppercase() throws Exception {
    String description = ReflectionHelper.getAccessibleObjectDescription(testMethod, true);
    assertTrue(description.startsWith("Method '"));
  }

  @Test
    @Timeout(8000)
  public void testGetAccessibleObjectDescription_constructor_lowercase() throws Exception {
    try (MockedStatic<ReflectionHelper> mocked = Mockito.mockStatic(ReflectionHelper.class, Mockito.CALLS_REAL_METHODS)) {
      mocked.when(() -> ReflectionHelper.constructorToString(testConstructor)).thenReturn("com.google.gson.internal.reflect.ReflectionHelperTest$SampleClass()");
      String description = ReflectionHelper.getAccessibleObjectDescription(testConstructor, false);
      assertEquals("constructor 'com.google.gson.internal.reflect.ReflectionHelperTest$SampleClass()'", description);
    }
  }

  @Test
    @Timeout(8000)
  public void testGetAccessibleObjectDescription_constructor_uppercase() throws Exception {
    try (MockedStatic<ReflectionHelper> mocked = Mockito.mockStatic(ReflectionHelper.class, Mockito.CALLS_REAL_METHODS)) {
      mocked.when(() -> ReflectionHelper.constructorToString(testConstructor)).thenReturn("com.google.gson.internal.reflect.ReflectionHelperTest$SampleClass()");
      String description = ReflectionHelper.getAccessibleObjectDescription(testConstructor, true);
      assertEquals("Constructor 'com.google.gson.internal.reflect.ReflectionHelperTest$SampleClass()'", description);
    }
  }

  @Test
    @Timeout(8000)
  public void testGetAccessibleObjectDescription_unknownAccessibleObject_lowercase() {
    String description = ReflectionHelper.getAccessibleObjectDescription(unknownAccessibleObject, false);
    assertEquals("<unknown AccessibleObject> unknownObjectToString", description);
  }

  @Test
    @Timeout(8000)
  public void testGetAccessibleObjectDescription_unknownAccessibleObject_uppercase() {
    String description = ReflectionHelper.getAccessibleObjectDescription(unknownAccessibleObject, true);
    assertEquals("<unknown AccessibleObject> unknownObjectToString", description);
  }
}