package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.List;

import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;

import org.junit.jupiter.api.Test;

class ReflectionAccessFilterHelper_96_1Test {

  @Test
    @Timeout(8000)
  void testIsJavaType_Class() throws Exception {
    Method isJavaTypeString = ReflectionAccessFilterHelper.class.getDeclaredMethod("isJavaType", String.class);
    isJavaTypeString.setAccessible(true);

    // Test with java.lang.String (should be true)
    assertTrue(ReflectionAccessFilterHelper.isJavaType(String.class));
    // Test with a class outside java.* (should be false)
    assertFalse(ReflectionAccessFilterHelper.isJavaType(ReflectionAccessFilterHelper.class));

    // Test private isJavaType(String) via reflection
    assertTrue((boolean) isJavaTypeString.invoke(null, "java.lang.String"));
    assertFalse((boolean) isJavaTypeString.invoke(null, "com.example.Foo"));
  }

  @Test
    @Timeout(8000)
  void testIsAndroidType_Class() throws Exception {
    Method isAndroidTypeString = ReflectionAccessFilterHelper.class.getDeclaredMethod("isAndroidType", String.class);
    isAndroidTypeString.setAccessible(true);

    // Since android.app.Activity is not available, simulate with a dummy class name
    // The actual method should be tested with a class whose name starts with "android."
    // So we test the private method with the string "android.app.Activity"
    assertTrue((boolean) isAndroidTypeString.invoke(null, "android.app.Activity"));
    assertFalse((boolean) isAndroidTypeString.invoke(null, "com.example.Foo"));

    // Test with non-android (should be false)
    assertFalse(ReflectionAccessFilterHelper.isAndroidType(ReflectionAccessFilterHelper.class));
  }

  @Test
    @Timeout(8000)
  void testIsAnyPlatformType() throws Exception {
    Method isAndroidTypeString = ReflectionAccessFilterHelper.class.getDeclaredMethod("isAndroidType", String.class);
    isAndroidTypeString.setAccessible(true);

    // java type
    assertTrue(ReflectionAccessFilterHelper.isAnyPlatformType(String.class));

    // android type simulation: test private method on string "android.app.Activity"
    assertTrue((boolean) isAndroidTypeString.invoke(null, "android.app.Activity"));

    // neither
    assertFalse(ReflectionAccessFilterHelper.isAnyPlatformType(ReflectionAccessFilterHelper.class));
  }

  @Test
    @Timeout(8000)
  void testGetFilterResult() {
    ReflectionAccessFilter allowFilter = (c) -> FilterResult.ALLOWED;
    ReflectionAccessFilter denyFilter = (c) -> FilterResult.DENIED;
    ReflectionAccessFilter neutralFilter = (c) -> FilterResult.NEUTRAL;

    List<ReflectionAccessFilter> filters;

    // Empty list returns NEUTRAL
    filters = List.of();
    assertEquals(FilterResult.NEUTRAL, ReflectionAccessFilterHelper.getFilterResult(filters, String.class));

    // One ALLOW filter
    filters = List.of(allowFilter);
    assertEquals(FilterResult.ALLOWED, ReflectionAccessFilterHelper.getFilterResult(filters, String.class));

    // One DENY filter
    filters = List.of(denyFilter);
    assertEquals(FilterResult.DENIED, ReflectionAccessFilterHelper.getFilterResult(filters, String.class));

    // Mixed filters: DENY should have precedence over ALLOW and NEUTRAL
    filters = List.of(neutralFilter, allowFilter, denyFilter);
    assertEquals(FilterResult.DENIED, ReflectionAccessFilterHelper.getFilterResult(filters, String.class));

    // Mixed filters: ALLOW if no DENY
    filters = List.of(neutralFilter, allowFilter);
    assertEquals(FilterResult.ALLOWED, ReflectionAccessFilterHelper.getFilterResult(filters, String.class));

    // All NEUTRAL
    filters = List.of(neutralFilter, neutralFilter);
    assertEquals(FilterResult.NEUTRAL, ReflectionAccessFilterHelper.getFilterResult(filters, String.class));
  }

  @Test
    @Timeout(8000)
  void testCanAccess() throws Exception {
    Method privateMethod = SampleClass.class.getDeclaredMethod("privateMethod");
    AccessibleObject accessible = privateMethod;

    SampleClass instance = new SampleClass();

    // Initially not accessible
    accessible.setAccessible(false);
    // canAccess should set accessible to true and return true
    assertTrue(ReflectionAccessFilterHelper.canAccess(accessible, instance));
    assertTrue(accessible.canAccess(instance));

    // Test passing null object (static method)
    Method staticMethod = SampleClass.class.getDeclaredMethod("staticMethod");
    accessible = staticMethod;
    accessible.setAccessible(false);
    assertTrue(ReflectionAccessFilterHelper.canAccess(accessible, null));
    assertTrue(accessible.canAccess(null));

    // Test passing null accessibleObject returns false
    assertFalse(ReflectionAccessFilterHelper.canAccess(null, instance));
  }

  // Sample class for testing canAccess
  static class SampleClass {
    private void privateMethod() {}
    private static void staticMethod() {}
  }
}