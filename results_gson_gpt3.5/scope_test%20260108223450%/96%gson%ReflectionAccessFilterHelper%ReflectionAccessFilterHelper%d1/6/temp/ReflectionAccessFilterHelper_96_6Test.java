package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;

import org.junit.jupiter.api.Test;

class ReflectionAccessFilterHelper_96_6Test {

  @Test
    @Timeout(8000)
  void testIsJavaType_withJavaLangClass() throws Exception {
    // Using reflection to invoke private static boolean isJavaType(String)
    var method = ReflectionAccessFilterHelper.class.getDeclaredMethod("isJavaType", String.class);
    method.setAccessible(true);

    assertTrue((Boolean) method.invoke(null, "java.lang.String"));
    assertTrue((Boolean) method.invoke(null, "java.util.List"));
    assertFalse((Boolean) method.invoke(null, "com.google.gson.SomeClass"));
  }

  @Test
    @Timeout(8000)
  void testIsJavaType_withClass() {
    assertTrue(ReflectionAccessFilterHelper.isJavaType(String.class));
    assertTrue(ReflectionAccessFilterHelper.isJavaType(List.class));
    assertFalse(ReflectionAccessFilterHelper.isJavaType(ReflectionAccessFilterHelper.class));
  }

  @Test
    @Timeout(8000)
  void testIsAndroidType_withClass() {
    // We cannot test real android classes here, but test the logic with class name strings
    assertFalse(ReflectionAccessFilterHelper.isAndroidType(ReflectionAccessFilterHelper.class));
  }

  @Test
    @Timeout(8000)
  void testIsAndroidType_withString() throws Exception {
    var method = ReflectionAccessFilterHelper.class.getDeclaredMethod("isAndroidType", String.class);
    method.setAccessible(true);

    assertFalse((Boolean) method.invoke(null, "java.lang.String"));
    assertFalse((Boolean) method.invoke(null, "com.google.gson.SomeClass"));
  }

  @Test
    @Timeout(8000)
  void testIsAnyPlatformType() {
    assertTrue(ReflectionAccessFilterHelper.isAnyPlatformType(String.class));
    assertFalse(ReflectionAccessFilterHelper.isAnyPlatformType(ReflectionAccessFilterHelper.class));
  }

  @Test
    @Timeout(8000)
  void testGetFilterResult_emptyFilters() {
    List<ReflectionAccessFilter> filters = Collections.emptyList();
    FilterResult result = ReflectionAccessFilterHelper.getFilterResult(filters, String.class);
    assertEquals(FilterResult.ALLOW, result);
  }

  @Test
    @Timeout(8000)
  void testGetFilterResult_filtersAllowAndDeny() {
    ReflectionAccessFilter allowFilter = mock(ReflectionAccessFilter.class);
    ReflectionAccessFilter denyFilter = mock(ReflectionAccessFilter.class);

    when(allowFilter.apply(any())).thenReturn(FilterResult.ALLOW);
    when(denyFilter.apply(any())).thenReturn(FilterResult.DENIED);

    List<ReflectionAccessFilter> filters = Arrays.asList(allowFilter, denyFilter);
    FilterResult result = ReflectionAccessFilterHelper.getFilterResult(filters, String.class);

    // According to typical filter logic, the first DENY should override ALLOW
    assertEquals(FilterResult.DENIED, result);
  }

  @Test
    @Timeout(8000)
  void testCanAccess_true() throws Exception {
    Method method = String.class.getDeclaredMethod("length");
    AccessibleObject accessibleObject = method;

    // We test canAccess returns true when accessibleObject is accessible and object is not null
    assertTrue(ReflectionAccessFilterHelper.canAccess(accessibleObject, "test"));
  }

  @Test
    @Timeout(8000)
  void testCanAccess_false() throws Exception {
    Method method = String.class.getDeclaredMethod("length");
    AccessibleObject accessibleObject = method;

    // Using mock to simulate accessibleObject.isAccessible() false scenario
    AccessibleObject mockAccessible = mock(AccessibleObject.class);
    when(mockAccessible.isAccessible()).thenReturn(false);

    // When accessibleObject is not accessible and object is null, canAccess should be false
    assertFalse(ReflectionAccessFilterHelper.canAccess(mockAccessible, null));
  }
}