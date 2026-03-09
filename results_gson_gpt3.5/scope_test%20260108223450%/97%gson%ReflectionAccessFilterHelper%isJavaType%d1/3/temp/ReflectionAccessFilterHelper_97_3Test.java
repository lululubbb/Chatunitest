package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.lang.reflect.AccessibleObject;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

class ReflectionAccessFilterHelper_97_3Test {

  @Test
    @Timeout(8000)
  void testIsJavaType_withJavaLangString_shouldReturnTrue() {
    // java.lang.String is a Java type
    assertTrue(ReflectionAccessFilterHelper.isJavaType(String.class));
  }

  @Test
    @Timeout(8000)
  void testIsJavaType_withPrimitive_shouldReturnFalse() {
    // primitives like int are NOT Java types according to implementation
    assertFalse(ReflectionAccessFilterHelper.isJavaType(int.class));
  }

  @Test
    @Timeout(8000)
  void testIsJavaType_withCustomClass_shouldReturnFalse() {
    // Custom class should not be Java type (assuming implementation)
    class CustomClass {}
    assertFalse(ReflectionAccessFilterHelper.isJavaType(CustomClass.class));
  }

  @Test
    @Timeout(8000)
  void testIsJavaType_privateMethod_withJavaLangString_shouldReturnTrue() throws Exception {
    // Access private static boolean isJavaType(String className) via reflection

    Method isJavaTypeString = ReflectionAccessFilterHelper.class.getDeclaredMethod("isJavaType", String.class);
    isJavaTypeString.setAccessible(true);

    Object result = isJavaTypeString.invoke(null, "java.lang.String");
    assertTrue((Boolean) result);
  }

  @Test
    @Timeout(8000)
  void testIsJavaType_privateMethod_withCustomClassName_shouldReturnFalse() throws Exception {
    Method isJavaTypeString = ReflectionAccessFilterHelper.class.getDeclaredMethod("isJavaType", String.class);
    isJavaTypeString.setAccessible(true);

    Object result = isJavaTypeString.invoke(null, "com.example.CustomClass");
    assertFalse((Boolean) result);
  }

  @Test
    @Timeout(8000)
  void testPrivateConstructor() throws Exception {
    Constructor<ReflectionAccessFilterHelper> constructor = ReflectionAccessFilterHelper.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    ReflectionAccessFilterHelper instance = constructor.newInstance();
    assertNotNull(instance);
  }
}