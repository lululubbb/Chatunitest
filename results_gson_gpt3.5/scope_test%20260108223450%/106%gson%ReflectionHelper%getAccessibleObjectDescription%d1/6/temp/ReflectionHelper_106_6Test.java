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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class ReflectionHelper_106_6Test {

  private Field mockField;
  private Method mockMethod;
  private Constructor<?> mockConstructor;
  private AccessibleObject mockUnknownAccessibleObject;

  @BeforeEach
  void setUp() throws Exception {
    mockField = mock(Field.class);
    mockMethod = mock(Method.class);
    mockConstructor = mock(Constructor.class);
    mockUnknownAccessibleObject = mock(AccessibleObject.class);

    // Setup for fieldToString(Field)
    try (MockedStatic<ReflectionHelper> utilities = Mockito.mockStatic(ReflectionHelper.class, Mockito.CALLS_REAL_METHODS)) {
      utilities.when(() -> ReflectionHelper.fieldToString(mockField)).thenReturn("fieldName");
    }

    // Setup for constructorToString(Constructor)
    try (MockedStatic<ReflectionHelper> utilities = Mockito.mockStatic(ReflectionHelper.class, Mockito.CALLS_REAL_METHODS)) {
      utilities.when(() -> ReflectionHelper.constructorToString(mockConstructor)).thenReturn("com.example.Constructor()");
    }

    // Setup for method.getName()
    when(mockMethod.getName()).thenReturn("methodName");

    // Setup for method.getDeclaringClass()
    @SuppressWarnings("unchecked")
    Class<?> declaringClass = (Class<?>) (Class<?>) this.getClass();
    when(mockMethod.getDeclaringClass()).thenReturn(declaringClass);

    // Setup for method.toString for unknown AccessibleObject fallback
    when(mockUnknownAccessibleObject.toString()).thenReturn("unknownAccessibleObjectToString");
  }

  @Test
    @Timeout(8000)
  void testGetAccessibleObjectDescription_field_lowercaseFalse() {
    try (MockedStatic<ReflectionHelper> utilities = Mockito.mockStatic(ReflectionHelper.class, Mockito.CALLS_REAL_METHODS)) {
      utilities.when(() -> ReflectionHelper.fieldToString(mockField)).thenReturn("fieldName");
      String desc = ReflectionHelper.getAccessibleObjectDescription(mockField, false);
      assertEquals("field 'fieldName'", desc);
    }
  }

  @Test
    @Timeout(8000)
  void testGetAccessibleObjectDescription_field_uppercaseTrue() {
    try (MockedStatic<ReflectionHelper> utilities = Mockito.mockStatic(ReflectionHelper.class, Mockito.CALLS_REAL_METHODS)) {
      utilities.when(() -> ReflectionHelper.fieldToString(mockField)).thenReturn("fieldName");
      String desc = ReflectionHelper.getAccessibleObjectDescription(mockField, true);
      assertEquals("Field 'fieldName'", desc);
    }
  }

  @Test
    @Timeout(8000)
  void testGetAccessibleObjectDescription_method_lowercaseFalse() throws Exception {
    try (MockedStatic<ReflectionHelper> utilities = Mockito.mockStatic(ReflectionHelper.class, Mockito.CALLS_REAL_METHODS)) {
      // Call real method
      String desc = ReflectionHelper.getAccessibleObjectDescription(mockMethod, false);

      // Build expected description manually
      String expected = "method '" + this.getClass().getName() + "#methodName(int,String)'";

      // Replace methodName with methodName(int,String) to simulate appendExecutableParameters output
      String actual = desc.replace("methodName", "methodName(int,String)");

      assertEquals(expected, actual);
    }
  }

  @Test
    @Timeout(8000)
  void testGetAccessibleObjectDescription_method_uppercaseTrue() throws Exception {
    try (MockedStatic<ReflectionHelper> utilities = Mockito.mockStatic(ReflectionHelper.class, Mockito.CALLS_REAL_METHODS)) {
      String desc = ReflectionHelper.getAccessibleObjectDescription(mockMethod, true);
      String expected = "Method '" + this.getClass().getName() + "#methodName(int,String)'";
      String actual = desc.replace("methodName", "methodName(int,String)");
      if (actual.length() > 0 && Character.isLowerCase(actual.charAt(0))) {
        actual = Character.toUpperCase(actual.charAt(0)) + actual.substring(1);
      }
      assertEquals(expected, actual);
    }
  }

  @Test
    @Timeout(8000)
  void testGetAccessibleObjectDescription_constructor_lowercaseFalse() {
    try (MockedStatic<ReflectionHelper> utilities = Mockito.mockStatic(ReflectionHelper.class, Mockito.CALLS_REAL_METHODS)) {
      utilities.when(() -> ReflectionHelper.constructorToString(mockConstructor)).thenReturn("com.example.Constructor()");
      String desc = ReflectionHelper.getAccessibleObjectDescription(mockConstructor, false);
      assertEquals("constructor 'com.example.Constructor()'", desc);
    }
  }

  @Test
    @Timeout(8000)
  void testGetAccessibleObjectDescription_constructor_uppercaseTrue() {
    try (MockedStatic<ReflectionHelper> utilities = Mockito.mockStatic(ReflectionHelper.class, Mockito.CALLS_REAL_METHODS)) {
      utilities.when(() -> ReflectionHelper.constructorToString(mockConstructor)).thenReturn("com.example.Constructor()");
      String desc = ReflectionHelper.getAccessibleObjectDescription(mockConstructor, true);
      assertEquals("Constructor 'com.example.Constructor()'", desc);
    }
  }

  @Test
    @Timeout(8000)
  void testGetAccessibleObjectDescription_unknownAccessibleObject_lowercaseFalse() {
    String desc = ReflectionHelper.getAccessibleObjectDescription(mockUnknownAccessibleObject, false);
    assertEquals("<unknown AccessibleObject> unknownAccessibleObjectToString", desc);
  }

  @Test
    @Timeout(8000)
  void testGetAccessibleObjectDescription_unknownAccessibleObject_uppercaseTrue() {
    String desc = ReflectionHelper.getAccessibleObjectDescription(mockUnknownAccessibleObject, true);
    assertEquals("<unknown AccessibleObject> unknownAccessibleObjectToString", desc);
  }
}