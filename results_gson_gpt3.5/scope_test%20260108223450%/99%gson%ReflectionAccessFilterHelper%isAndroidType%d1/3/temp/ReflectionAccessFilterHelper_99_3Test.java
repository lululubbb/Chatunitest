package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.AccessibleObject;
import java.util.List;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;

class ReflectionAccessFilterHelper_99_3Test {

  @Test
    @Timeout(8000)
  void testIsAndroidType_withAndroidClassName() throws Exception {
    // Use reflection to invoke private static method isAndroidType(String)
    Method isAndroidTypeString = ReflectionAccessFilterHelper.class.getDeclaredMethod("isAndroidType", String.class);
    isAndroidTypeString.setAccessible(true);

    // Android class names that should return true
    String[] androidClassNames = {
      "android.app.Activity",
      "android.content.Context",
      "android.os.Bundle",
      "android.view.View"
    };

    for (String className : androidClassNames) {
      boolean result = (boolean) isAndroidTypeString.invoke(null, className);
      assertTrue(result, "Expected true for Android class name: " + className);
    }

    // Non-Android class names should return false except java.* which returns true
    String[] nonAndroidClassNames = {
      "java.lang.String",
      "com.google.gson.internal.ReflectionAccessFilterHelper",
      "javax.swing.JButton",
      "org.example.MyClass"
    };

    for (String className : nonAndroidClassNames) {
      boolean result = (boolean) isAndroidTypeString.invoke(null, className);
      if (className.startsWith("android.") || className.startsWith("java.")) {
        assertTrue(result, "Expected true for Android or java class name: " + className);
      } else {
        assertFalse(result, "Expected false for non-Android and non-java class name: " + className);
      }
    }
  }

  @Test
    @Timeout(8000)
  void testIsAndroidType_withClass() throws Exception {
    // Use reflection to invoke private static method isAndroidType(String)
    Method isAndroidTypeString = ReflectionAccessFilterHelper.class.getDeclaredMethod("isAndroidType", String.class);
    isAndroidTypeString.setAccessible(true);

    // Test with String.class (should return true because method treats java.* as Android types)
    boolean resultForStringClass = ReflectionAccessFilterHelper.isAndroidType(String.class);
    assertTrue(resultForStringClass, "Expected true for java.lang.String class");

    // Test with android class name string via reflection on private method
    boolean result = (boolean) isAndroidTypeString.invoke(null, "android.app.Activity");
    assertTrue(result);
  }
}