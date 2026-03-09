package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.AccessibleObject;
import java.util.List;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class ReflectionAccessFilterHelper_97_4Test {

  @Test
    @Timeout(8000)
  void testIsJavaType_withJavaLangString_shouldReturnTrue() {
    assertTrue(ReflectionAccessFilterHelper.isJavaType(String.class));
  }

  @Test
    @Timeout(8000)
  void testIsJavaType_withCustomClass_shouldReturnFalse() {
    class CustomClass {}
    assertFalse(ReflectionAccessFilterHelper.isJavaType(CustomClass.class));
  }

  @Test
    @Timeout(8000)
  void testIsJavaType_privateStringMethod_withJavaLangString_shouldReturnTrue() throws Exception {
    Method isJavaTypeStringMethod = ReflectionAccessFilterHelper.class.getDeclaredMethod("isJavaType", String.class);
    isJavaTypeStringMethod.setAccessible(true);
    boolean result = (boolean) isJavaTypeStringMethod.invoke(null, "java.lang.String");
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testIsJavaType_privateStringMethod_withCustomClass_shouldReturnFalse() throws Exception {
    Method isJavaTypeStringMethod = ReflectionAccessFilterHelper.class.getDeclaredMethod("isJavaType", String.class);
    isJavaTypeStringMethod.setAccessible(true);
    boolean result = (boolean) isJavaTypeStringMethod.invoke(null, "com.example.CustomClass");
    assertFalse(result);
  }
}