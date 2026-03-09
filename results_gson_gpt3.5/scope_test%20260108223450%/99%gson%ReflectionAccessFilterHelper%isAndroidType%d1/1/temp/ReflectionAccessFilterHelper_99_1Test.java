package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.AccessibleObject;
import java.util.List;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;

class ReflectionAccessFilterHelper_99_1Test {

  @Test
    @Timeout(8000)
  void testIsAndroidType_withAndroidClassName() throws Exception {
    Method method = ReflectionAccessFilterHelper.class.getDeclaredMethod("isAndroidType", String.class);
    method.setAccessible(true);
    String androidClassName = "android.app.Activity";
    boolean result = (boolean) method.invoke(null, androidClassName);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testIsAndroidType_withNonAndroidClassName() throws Exception {
    Method method = ReflectionAccessFilterHelper.class.getDeclaredMethod("isAndroidType", String.class);
    method.setAccessible(true);
    // Use a class name that is neither Android nor Java platform type
    String nonAndroidClassName = "com.example.MyClass";
    boolean result = (boolean) method.invoke(null, nonAndroidClassName);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testIsAndroidType_withNullClassName() throws Exception {
    Method method = ReflectionAccessFilterHelper.class.getDeclaredMethod("isAndroidType", String.class);
    method.setAccessible(true);
    // The method under test throws NPE on null, so expect that
    try {
      method.invoke(null, (Object) null);
      fail("Expected NullPointerException");
    } catch (java.lang.reflect.InvocationTargetException e) {
      assertTrue(e.getCause() instanceof NullPointerException);
    }
  }

  @Test
    @Timeout(8000)
  void testIsAndroidType_withClass() throws Exception {
    // Use reflection to invoke private isAndroidType(String) method
    Method method = ReflectionAccessFilterHelper.class.getDeclaredMethod("isAndroidType", String.class);
    method.setAccessible(true);

    // Test with android.app.Activity name
    assertTrue((boolean) method.invoke(null, "android.app.Activity"));
  }

  @Test
    @Timeout(8000)
  void testIsAndroidType_withNonAndroidClass() {
    // Use public isAndroidType(Class<?>) method
    // Use a class that is neither Android nor Java platform type
    // Use a custom class defined here for test
    class CustomClass {}
    assertFalse(ReflectionAccessFilterHelper.isAndroidType(CustomClass.class));
  }
}