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

public class ReflectionAccessFilterHelper_97_2Test {

  @Test
    @Timeout(8000)
  @DisplayName("Test isJavaType(Class<?>) returns true for java.lang.String")
  public void testIsJavaType_withJavaLangString() {
    assertTrue(ReflectionAccessFilterHelper.isJavaType(String.class));
  }

  @Test
    @Timeout(8000)
  @DisplayName("Test isJavaType(Class<?>) returns false for custom class")
  public void testIsJavaType_withCustomClass() {
    class CustomClass {}
    assertFalse(ReflectionAccessFilterHelper.isJavaType(CustomClass.class));
  }

  @Test
    @Timeout(8000)
  @DisplayName("Test private isJavaType(String) method returns true for java.lang.String")
  public void testPrivateIsJavaType_withJavaLangString() throws Exception {
    Method method = ReflectionAccessFilterHelper.class.getDeclaredMethod("isJavaType", String.class);
    method.setAccessible(true);
    boolean result = (boolean) method.invoke(null, "java.lang.String");
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  @DisplayName("Test private isJavaType(String) method returns false for custom class name")
  public void testPrivateIsJavaType_withCustomClassName() throws Exception {
    Method method = ReflectionAccessFilterHelper.class.getDeclaredMethod("isJavaType", String.class);
    method.setAccessible(true);
    boolean result = (boolean) method.invoke(null, "com.example.CustomClass");
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  @DisplayName("Test private constructor is inaccessible")
  public void testPrivateConstructor() throws Exception {
    Constructor<ReflectionAccessFilterHelper> constructor = ReflectionAccessFilterHelper.class.getDeclaredConstructor();
    assertFalse(constructor.canAccess(null));
    constructor.setAccessible(true);
    ReflectionAccessFilterHelper instance = constructor.newInstance();
    assertNotNull(instance);
  }
}