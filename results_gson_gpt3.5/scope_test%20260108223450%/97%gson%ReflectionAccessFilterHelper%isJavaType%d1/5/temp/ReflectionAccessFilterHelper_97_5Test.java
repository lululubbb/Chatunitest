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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class ReflectionAccessFilterHelper_97_5Test {

  @Test
    @Timeout(8000)
  public void testIsJavaType_withJavaLangString() {
    // java.lang.String is a Java type, should return true
    assertTrue(ReflectionAccessFilterHelper.isJavaType(String.class));
  }

  @Test
    @Timeout(8000)
  public void testIsJavaType_withCustomClass() {
    // A custom class should return false
    class CustomClass {}
    assertFalse(ReflectionAccessFilterHelper.isJavaType(CustomClass.class));
  }

  @Test
    @Timeout(8000)
  public void testIsJavaType_privateStringMethodInvocation() throws Exception {
    // Use reflection to invoke private isJavaType(String className) method
    Method isJavaTypeStringMethod = ReflectionAccessFilterHelper.class.getDeclaredMethod("isJavaType", String.class);
    isJavaTypeStringMethod.setAccessible(true);

    // java.lang.String should return true
    boolean resultString = (boolean) isJavaTypeStringMethod.invoke(null, "java.lang.String");
    assertTrue(resultString);

    // some random class name should return false
    boolean resultRandom = (boolean) isJavaTypeStringMethod.invoke(null, "com.example.NonExistent");
    assertFalse(resultRandom);
  }

  @Test
    @Timeout(8000)
  public void testPrivateConstructor() throws Exception {
    // Use reflection to invoke private constructor to achieve 100% coverage
    Constructor<ReflectionAccessFilterHelper> constructor = ReflectionAccessFilterHelper.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    ReflectionAccessFilterHelper instance = constructor.newInstance();
    assertNotNull(instance);
  }
}