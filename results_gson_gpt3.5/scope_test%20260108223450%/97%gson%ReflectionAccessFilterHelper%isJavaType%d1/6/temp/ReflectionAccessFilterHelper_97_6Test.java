package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

class ReflectionAccessFilterHelper_97_6Test {

  private Method isJavaTypeStringMethod;

  @BeforeEach
  void setup() throws Exception {
    isJavaTypeStringMethod = ReflectionAccessFilterHelper.class.getDeclaredMethod("isJavaType", String.class);
    isJavaTypeStringMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  void testIsJavaType_withJavaLangString() {
    // java.lang.String is a java type
    boolean result = ReflectionAccessFilterHelper.isJavaType(String.class);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testIsJavaType_withJavaUtilList() {
    // java.util.List is a java type
    boolean result = ReflectionAccessFilterHelper.isJavaType(List.class);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testIsJavaType_withNonJavaType() {
    // Custom class name should return false
    boolean result = ReflectionAccessFilterHelper.isJavaType(this.getClass());
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testIsJavaType_privateMethod_withJavaLangString() throws Exception {
    // invoke private static isJavaType(String) with java.lang.String
    boolean result = (boolean) isJavaTypeStringMethod.invoke(null, "java.lang.String");
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testIsJavaType_privateMethod_withJavaUtilMap() throws Exception {
    // invoke private static isJavaType(String) with java.util.Map
    boolean result = (boolean) isJavaTypeStringMethod.invoke(null, "java.util.Map");
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testIsJavaType_privateMethod_withCustomClassName() throws Exception {
    // invoke private static isJavaType(String) with a custom class name
    boolean result = (boolean) isJavaTypeStringMethod.invoke(null, "com.example.MyClass");
    assertFalse(result);
  }
}