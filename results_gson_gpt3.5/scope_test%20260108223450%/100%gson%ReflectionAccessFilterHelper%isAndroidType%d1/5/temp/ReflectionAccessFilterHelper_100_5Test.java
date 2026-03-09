package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.AccessibleObject;
import java.util.List;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class ReflectionAccessFilterHelper_100_5Test {

  @Test
    @Timeout(8000)
  public void testIsAndroidType_withAndroidPrefix() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = ReflectionAccessFilterHelper.class.getDeclaredMethod("isAndroidType", String.class);
    method.setAccessible(true);

    assertTrue((Boolean) method.invoke(null, "android.app.Activity"));
    assertTrue((Boolean) method.invoke(null, "androidx.recyclerview.widget.RecyclerView"));
  }

  @Test
    @Timeout(8000)
  public void testIsAndroidType_withJavaType() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method isJavaTypeMethod = ReflectionAccessFilterHelper.class.getDeclaredMethod("isJavaType", String.class);
    isJavaTypeMethod.setAccessible(true);

    Method isAndroidTypeMethod = ReflectionAccessFilterHelper.class.getDeclaredMethod("isAndroidType", String.class);
    isAndroidTypeMethod.setAccessible(true);

    // A known Java type, e.g. java.lang.String
    String javaClassName = "java.lang.String";
    // Verify isJavaType returns true
    assertTrue((Boolean) isJavaTypeMethod.invoke(null, javaClassName));
    // isAndroidType should return true because isJavaType returns true
    assertTrue((Boolean) isAndroidTypeMethod.invoke(null, javaClassName));
  }

  @Test
    @Timeout(8000)
  public void testIsAndroidType_withNonAndroidNonJavaType() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = ReflectionAccessFilterHelper.class.getDeclaredMethod("isAndroidType", String.class);
    method.setAccessible(true);

    // A class name that is neither android/ androidx nor java type
    String className = "com.example.MyClass";
    assertFalse((Boolean) method.invoke(null, className));
  }
}