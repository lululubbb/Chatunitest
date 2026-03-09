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

class ReflectionAccessFilterHelper_100_3Test {

  @Test
    @Timeout(8000)
  void testIsAndroidType_withAndroidPrefix() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = ReflectionAccessFilterHelper.class.getDeclaredMethod("isAndroidType", String.class);
    method.setAccessible(true);

    assertTrue((Boolean) method.invoke(null, "android.app.Activity"));
    assertTrue((Boolean) method.invoke(null, "androidx.recyclerview.widget.RecyclerView"));
  }

  @Test
    @Timeout(8000)
  void testIsAndroidType_withJavaType() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = ReflectionAccessFilterHelper.class.getDeclaredMethod("isAndroidType", String.class);
    method.setAccessible(true);

    // We need to test the branch isJavaType(className) returns true
    // For that, we must know what isJavaType(String) accepts as java types.
    // Assuming "java.lang.String" is java type.
    assertTrue((Boolean) method.invoke(null, "java.lang.String"));
  }

  @Test
    @Timeout(8000)
  void testIsAndroidType_withNonAndroidNonJavaType() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = ReflectionAccessFilterHelper.class.getDeclaredMethod("isAndroidType", String.class);
    method.setAccessible(true);

    assertFalse((Boolean) method.invoke(null, "com.example.MyClass"));
    assertFalse((Boolean) method.invoke(null, ""));
    assertFalse((Boolean) method.invoke(null, "androidxx.something"));
  }
}