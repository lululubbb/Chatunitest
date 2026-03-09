package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.AccessibleObject;
import java.util.List;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;

import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import org.junit.jupiter.api.Test;

class ReflectionAccessFilterHelper_100_2Test {

  @Test
    @Timeout(8000)
  void testIsAndroidType_withAndroidPrefix() throws Exception {
    Method method = ReflectionAccessFilterHelper.class.getDeclaredMethod("isAndroidType", String.class);
    method.setAccessible(true);
    assertTrue((Boolean) method.invoke(null, "android.foo.Bar"));
  }

  @Test
    @Timeout(8000)
  void testIsAndroidType_withAndroidxPrefix() throws Exception {
    Method method = ReflectionAccessFilterHelper.class.getDeclaredMethod("isAndroidType", String.class);
    method.setAccessible(true);
    assertTrue((Boolean) method.invoke(null, "androidx.bar.Baz"));
  }

  @Test
    @Timeout(8000)
  void testIsAndroidType_withJavaType() throws Exception {
    Method method = ReflectionAccessFilterHelper.class.getDeclaredMethod("isAndroidType", String.class);
    method.setAccessible(true);
    // Use a class name known to be java type, e.g. java.lang.String
    assertTrue((Boolean) method.invoke(null, "java.lang.String"));
  }

  @Test
    @Timeout(8000)
  void testIsAndroidType_withNonAndroidNonJava() throws Exception {
    Method method = ReflectionAccessFilterHelper.class.getDeclaredMethod("isAndroidType", String.class);
    method.setAccessible(true);
    assertFalse((Boolean) method.invoke(null, "com.example.MyClass"));
  }
}