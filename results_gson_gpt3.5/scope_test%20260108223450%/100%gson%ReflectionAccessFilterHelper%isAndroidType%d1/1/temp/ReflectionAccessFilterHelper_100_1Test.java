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

class ReflectionAccessFilterHelper_100_1Test {

  @Test
    @Timeout(8000)
  void testIsAndroidType_startsWithAndroid() throws Exception {
    Method method = ReflectionAccessFilterHelper.class.getDeclaredMethod("isAndroidType", String.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(null, "android.foo.Bar");
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testIsAndroidType_startsWithAndroidx() throws Exception {
    Method method = ReflectionAccessFilterHelper.class.getDeclaredMethod("isAndroidType", String.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(null, "androidx.foo.Bar");
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testIsAndroidType_isJavaTypeTrue() throws Exception {
    Method method = ReflectionAccessFilterHelper.class.getDeclaredMethod("isAndroidType", String.class);
    method.setAccessible(true);

    // Use a Java type to trigger isJavaType true
    boolean result = (boolean) method.invoke(null, "java.lang.String");
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testIsAndroidType_isJavaTypeFalse() throws Exception {
    Method method = ReflectionAccessFilterHelper.class.getDeclaredMethod("isAndroidType", String.class);
    method.setAccessible(true);

    // Use a non-android and non-java type string
    boolean result = (boolean) method.invoke(null, "com.example.Foo");
    assertFalse(result);
  }
}