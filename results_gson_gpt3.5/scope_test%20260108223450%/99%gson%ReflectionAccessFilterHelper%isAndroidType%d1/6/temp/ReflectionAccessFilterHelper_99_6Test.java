package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.AccessibleObject;
import java.util.List;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

class ReflectionAccessFilterHelper_99_6Test {

  @Test
    @Timeout(8000)
  void testIsAndroidType_withAndroidClassName() throws Exception {
    // Use reflection to access private static method isAndroidType(String)
    var method = ReflectionAccessFilterHelper.class.getDeclaredMethod("isAndroidType", String.class);
    method.setAccessible(true);

    // Typical Android class prefix
    String androidClassName = "android.app.Activity";
    boolean result = (boolean) method.invoke(null, androidClassName);
    assertTrue(result);

    // Another Android package prefix
    androidClassName = "androidx.appcompat.widget.AppCompatButton";
    result = (boolean) method.invoke(null, androidClassName);
    assertTrue(result);

    // Android internal package
    androidClassName = "com.android.internal.R";
    result = (boolean) method.invoke(null, androidClassName);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testIsAndroidType_withNonAndroidClassName() throws Exception {
    var method = ReflectionAccessFilterHelper.class.getDeclaredMethod("isAndroidType", String.class);
    method.setAccessible(true);

    // Java standard class
    String javaClassName = "java.lang.String";
    boolean result = (boolean) method.invoke(null, javaClassName);
    assertFalse(result);

    // Random class name
    String randomClassName = "com.example.MyClass";
    result = (boolean) method.invoke(null, randomClassName);
    assertFalse(result);

    // Null or empty string edge cases
    result = (boolean) method.invoke(null, "");
    assertFalse(result);

    result = (boolean) method.invoke(null, (Object) null);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testIsAndroidType_withClassObject() throws Exception {
    // Use reflection to invoke isAndroidType(Class<?>)
    var method = ReflectionAccessFilterHelper.class.getDeclaredMethod("isAndroidType", Class.class);
    method.setAccessible(true);

    // Standard Android class name (simulate with class name string since android.app.Activity not available)
    Class<?> androidActivityClass = Class.forName("android.app.Activity");
    boolean result = (boolean) method.invoke(null, androidActivityClass);
    assertTrue(result);

    // Standard Java class
    result = (boolean) method.invoke(null, String.class);
    assertFalse(result);

    // Custom class outside android package
    result = (boolean) method.invoke(null, ReflectionAccessFilterHelper.class);
    assertFalse(result);
  }
}