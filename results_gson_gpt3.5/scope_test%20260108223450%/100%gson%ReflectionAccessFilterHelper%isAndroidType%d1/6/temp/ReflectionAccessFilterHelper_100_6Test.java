package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.AccessibleObject;
import java.util.List;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionAccessFilterHelper_100_6Test {

  @Test
    @Timeout(8000)
  void testIsAndroidType_withAndroidPrefix() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = ReflectionAccessFilterHelper.class.getDeclaredMethod("isAndroidType", String.class);
    method.setAccessible(true);

    assertTrue((Boolean) method.invoke(null, "android.foo.Bar"));
    assertTrue((Boolean) method.invoke(null, "androidx.foo.Bar"));
  }

  @Test
    @Timeout(8000)
  void testIsAndroidType_withJavaType() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = ReflectionAccessFilterHelper.class.getDeclaredMethod("isAndroidType", String.class);
    method.setAccessible(true);

    // "java.lang.String" is a Java type, so should return true
    assertTrue((Boolean) method.invoke(null, "java.lang.String"));
  }

  @Test
    @Timeout(8000)
  void testIsAndroidType_withNonAndroidNonJavaType() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = ReflectionAccessFilterHelper.class.getDeclaredMethod("isAndroidType", String.class);
    method.setAccessible(true);

    // A class name that does not start with android., androidx., or java.* should return false
    assertFalse((Boolean) method.invoke(null, "com.example.MyClass"));
    assertFalse((Boolean) method.invoke(null, ""));
    assertFalse((Boolean) method.invoke(null, "org.example.SomeClass"));
  }
}