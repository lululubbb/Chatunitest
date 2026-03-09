package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.util.List;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.lang.reflect.AccessibleObject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

class ReflectionAccessFilterHelper_97_1Test {

  @Test
    @Timeout(8000)
  @DisplayName("isJavaType(Class) returns true for java.lang.String")
  void testIsJavaTypeWithJavaLangString() {
    assertTrue(ReflectionAccessFilterHelper.isJavaType(String.class));
  }

  @Test
    @Timeout(8000)
  @DisplayName("isJavaType(Class) returns false for custom class")
  void testIsJavaTypeWithCustomClass() {
    class CustomClass {}
    assertFalse(ReflectionAccessFilterHelper.isJavaType(CustomClass.class));
  }

  @Test
    @Timeout(8000)
  @DisplayName("isJavaType(String) private method returns true for java.lang.String")
  void testIsJavaTypePrivateMethodTrue() throws Exception {
    Method method = ReflectionAccessFilterHelper.class.getDeclaredMethod("isJavaType", String.class);
    method.setAccessible(true);
    boolean result = (boolean) method.invoke(null, "java.lang.String");
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  @DisplayName("isJavaType(String) private method returns false for custom class name")
  void testIsJavaTypePrivateMethodFalse() throws Exception {
    Method method = ReflectionAccessFilterHelper.class.getDeclaredMethod("isJavaType", String.class);
    method.setAccessible(true);
    boolean result = (boolean) method.invoke(null, "com.example.CustomClass");
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  @DisplayName("ReflectionAccessFilterHelper private constructor coverage")
  void testPrivateConstructor() throws Exception {
    Constructor<ReflectionAccessFilterHelper> constructor = ReflectionAccessFilterHelper.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    ReflectionAccessFilterHelper instance = constructor.newInstance();
    assertNotNull(instance);
  }
}