package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;

class ReflectionAccessFilterHelper_96_2Test {

  @Test
    @Timeout(8000)
  void testIsJavaType_class() throws Exception {
    // Use reflection to invoke private static method isJavaType(String)
    Method isJavaTypeString = ReflectionAccessFilterHelper.class.getDeclaredMethod("isJavaType", String.class);
    isJavaTypeString.setAccessible(true);

    // Test with java.lang.String (should be true)
    boolean resultTrue = (boolean) isJavaTypeString.invoke(null, "java.lang.String");
    assertTrue(resultTrue);

    // Test with custom class name (should be false)
    boolean resultFalse = (boolean) isJavaTypeString.invoke(null, "com.example.MyClass");
    assertFalse(resultFalse);

    // Test public isJavaType(Class<?>)
    assertTrue(ReflectionAccessFilterHelper.isJavaType(String.class));
    assertFalse(ReflectionAccessFilterHelper.isJavaType(ReflectionAccessFilterHelper.class));
  }

  @Test
    @Timeout(8000)
  void testIsAndroidType_class() throws Exception {
    // Use reflection to invoke private static method isAndroidType(String)
    Method isAndroidTypeString = ReflectionAccessFilterHelper.class.getDeclaredMethod("isAndroidType", String.class);
    isAndroidTypeString.setAccessible(true);

    // Test with android.os.Build (should be true)
    // Use class.forName to avoid compile error when android.os.Build is not present
    Class<?> androidBuildClass = null;
    try {
      androidBuildClass = Class.forName("android.os.Build");
    } catch (ClassNotFoundException e) {
      // skip test if android.os.Build does not exist
    }
    if (androidBuildClass != null) {
      boolean resultTrue = (boolean) isAndroidTypeString.invoke(null, "android.os.Build");
      assertTrue(resultTrue);

      // Test public isAndroidType(Class<?>)
      assertTrue(ReflectionAccessFilterHelper.isAndroidType(androidBuildClass));
    }

    // Test with custom class name (should be false)
    boolean resultFalse = (boolean) isAndroidTypeString.invoke(null, "com.example.MyClass");
    assertFalse(resultFalse);

    assertFalse(ReflectionAccessFilterHelper.isAndroidType(ReflectionAccessFilterHelper.class));
  }

  @Test
    @Timeout(8000)
  void testIsAnyPlatformType() throws Exception {
    // java type
    assertTrue(ReflectionAccessFilterHelper.isAnyPlatformType(String.class));

    // android type
    Class<?> androidBuildClass = null;
    try {
      androidBuildClass = Class.forName("android.os.Build");
    } catch (ClassNotFoundException e) {
      // ignore
    }
    if (androidBuildClass != null) {
      assertTrue(ReflectionAccessFilterHelper.isAnyPlatformType(androidBuildClass));
    }

    // neither
    assertFalse(ReflectionAccessFilterHelper.isAnyPlatformType(ReflectionAccessFilterHelper.class));
  }

  @Test
    @Timeout(8000)
  void testGetFilterResult() {
    ReflectionAccessFilter filterAllow = (c) -> FilterResult.ALLOWED;
    ReflectionAccessFilter filterDeny = (c) -> FilterResult.DENIED;
    ReflectionAccessFilter filterNeutral = (c) -> FilterResult.NEUTRAL;

    List<ReflectionAccessFilter> filters;

    // No filters returns NEUTRAL
    filters = List.of();
    assertEquals(FilterResult.NEUTRAL, ReflectionAccessFilterHelper.getFilterResult(filters, String.class));

    // Single ALLOW filter returns ALLOWED
    filters = List.of(filterAllow);
    assertEquals(FilterResult.ALLOWED, ReflectionAccessFilterHelper.getFilterResult(filters, String.class));

    // Single DENY filter returns DENIED
    filters = List.of(filterDeny);
    assertEquals(FilterResult.DENIED, ReflectionAccessFilterHelper.getFilterResult(filters, String.class));

    // Single NEUTRAL filter returns NEUTRAL
    filters = List.of(filterNeutral);
    assertEquals(FilterResult.NEUTRAL, ReflectionAccessFilterHelper.getFilterResult(filters, String.class));

    // Multiple filters: ALLOWED then DENIED returns ALLOWED (first ALLOWED short-circuits)
    filters = List.of(filterAllow, filterDeny);
    assertEquals(FilterResult.ALLOWED, ReflectionAccessFilterHelper.getFilterResult(filters, String.class));

    // Multiple filters: NEUTRAL then DENIED returns DENIED
    filters = List.of(filterNeutral, filterDeny);
    assertEquals(FilterResult.DENIED, ReflectionAccessFilterHelper.getFilterResult(filters, String.class));

    // Multiple filters: all NEUTRAL returns NEUTRAL
    filters = List.of(filterNeutral, filterNeutral);
    assertEquals(FilterResult.NEUTRAL, ReflectionAccessFilterHelper.getFilterResult(filters, String.class));
  }

  @Test
    @Timeout(8000)
  void testCanAccess_true() throws Exception {
    AccessibleObject accessibleObject = mock(AccessibleObject.class);
    Object obj = new Object();

    // accessibleObject.setAccessible(true) should be called, no exception thrown
    doNothing().when(accessibleObject).setAccessible(true);

    assertTrue(ReflectionAccessFilterHelper.canAccess(accessibleObject, obj));

    verify(accessibleObject).setAccessible(true);
  }

  @Test
    @Timeout(8000)
  void testCanAccess_false_onSecurityException() throws Exception {
    AccessibleObject accessibleObject = mock(AccessibleObject.class);
    Object obj = new Object();

    doThrow(new SecurityException("forbidden")).when(accessibleObject).setAccessible(true);

    assertFalse(ReflectionAccessFilterHelper.canAccess(accessibleObject, obj));

    verify(accessibleObject).setAccessible(true);
  }
}