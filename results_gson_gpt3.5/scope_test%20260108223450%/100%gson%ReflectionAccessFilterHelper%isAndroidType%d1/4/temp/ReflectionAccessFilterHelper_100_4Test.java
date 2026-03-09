package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.AccessibleObject;
import java.util.List;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class ReflectionAccessFilterHelper_100_4Test {

  @Test
    @Timeout(8000)
  void testIsAndroidType_withAndroidPrefix() throws Exception {
    Method method = ReflectionAccessFilterHelper.class.getDeclaredMethod("isAndroidType", String.class);
    method.setAccessible(true);

    boolean result1 = (boolean) method.invoke(null, "android.foo.Bar");
    assertTrue(result1);

    boolean result2 = (boolean) method.invoke(null, "androidx.bar.Baz");
    assertTrue(result2);
  }

  @Test
    @Timeout(8000)
  void testIsAndroidType_withJavaType() throws Exception {
    Method method = ReflectionAccessFilterHelper.class.getDeclaredMethod("isAndroidType", String.class);
    method.setAccessible(true);

    // Use a class name that is a Java type, e.g. java.lang.String
    boolean result = (boolean) method.invoke(null, "java.lang.String");
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testIsAndroidType_withNonAndroidNonJavaType() throws Exception {
    Method method = ReflectionAccessFilterHelper.class.getDeclaredMethod("isAndroidType", String.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(null, "com.example.MyClass");
    assertFalse(result);
  }
}