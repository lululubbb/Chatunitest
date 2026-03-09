package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;

class ReflectionAccessFilterHelper_96_3Test {

  @Test
    @Timeout(8000)
  void testIsJavaType_Class() throws Exception {
    // Access private static method isJavaType(String)
    Method isJavaTypeString = ReflectionAccessFilterHelper.class.getDeclaredMethod("isJavaType", String.class);
    isJavaTypeString.setAccessible(true);

    // Test with Java core class
    boolean result1 = (boolean) isJavaTypeString.invoke(null, "java.lang.String");
    assertTrue(result1);

    // Test with non-Java core class
    boolean result2 = (boolean) isJavaTypeString.invoke(null, "com.example.Foo");
    assertFalse(result2);

    // Test public method isJavaType(Class<?>)
    assertTrue(ReflectionAccessFilterHelper.isJavaType(String.class));
    assertFalse(ReflectionAccessFilterHelper.isJavaType(ReflectionAccessFilterHelper.class));
  }

  @Test
    @Timeout(8000)
  void testIsAndroidType_Class() throws Exception {
    // Access private static method isAndroidType(String)
    Method isAndroidTypeString = ReflectionAccessFilterHelper.class.getDeclaredMethod("isAndroidType", String.class);
    isAndroidTypeString.setAccessible(true);

    // Test with Android core class prefix
    boolean result1 = (boolean) isAndroidTypeString.invoke(null, "android.app.Activity");
    assertTrue(result1);

    // Test with non-Android class
    boolean result2 = (boolean) isAndroidTypeString.invoke(null, "java.lang.String");
    assertFalse(result2);

    // Test public method isAndroidType(Class<?>)
    // Use reflection to get android.app.Activity class to avoid compile error
    Class<?> androidActivityClass = null;
    try {
      androidActivityClass = Class.forName("android.app.Activity");
    } catch (ClassNotFoundException e) {
      // android.app.Activity class not found, skip this test part
      androidActivityClass = null;
    }
    if (androidActivityClass != null) {
      assertTrue(ReflectionAccessFilterHelper.isAndroidType(androidActivityClass));
    }
    assertFalse(ReflectionAccessFilterHelper.isAndroidType(String.class));
  }

  @Test
    @Timeout(8000)
  void testIsAnyPlatformType() {
    // Should be true for Java type
    assertTrue(ReflectionAccessFilterHelper.isAnyPlatformType(String.class));
    // Should be true for Android type (android.app.Activity)
    Class<?> androidActivityClass = null;
    try {
      androidActivityClass = Class.forName("android.app.Activity");
    } catch (ClassNotFoundException e) {
      androidActivityClass = null;
    }
    if (androidActivityClass != null) {
      assertTrue(ReflectionAccessFilterHelper.isAnyPlatformType(androidActivityClass));
    }
    // Should be false for user class
    assertFalse(ReflectionAccessFilterHelper.isAnyPlatformType(ReflectionAccessFilterHelper.class));
  }

  @Test
    @Timeout(8000)
  void testGetFilterResult_emptyFilters() {
    List<ReflectionAccessFilter> emptyFilters = Collections.emptyList();
    FilterResult result = ReflectionAccessFilterHelper.getFilterResult(emptyFilters, String.class);
    assertEquals(FilterResult.UNDECIDED, result);
  }

  @Test
    @Timeout(8000)
  void testGetFilterResult_filtersDecide() {
    @SuppressWarnings("unchecked")
    ReflectionAccessFilter filter1 = mock(ReflectionAccessFilter.class);
    @SuppressWarnings("unchecked")
    ReflectionAccessFilter filter2 = mock(ReflectionAccessFilter.class);

    when(filter1.apply(String.class)).thenReturn(FilterResult.UNDECIDED);
    when(filter2.apply(String.class)).thenReturn(FilterResult.ALLOWED);

    List<ReflectionAccessFilter> filters = List.of(filter1, filter2);

    FilterResult result = ReflectionAccessFilterHelper.getFilterResult(filters, String.class);
    // Should return ALLOWED because second filter returns ALLOWED
    assertEquals(FilterResult.ALLOWED, result);

    verify(filter1).apply(String.class);
    verify(filter2).apply(String.class);
  }

  @Test
    @Timeout(8000)
  void testGetFilterResult_filtersDeny() {
    @SuppressWarnings("unchecked")
    ReflectionAccessFilter filter1 = mock(ReflectionAccessFilter.class);
    @SuppressWarnings("unchecked")
    ReflectionAccessFilter filter2 = mock(ReflectionAccessFilter.class);

    when(filter1.apply(String.class)).thenReturn(FilterResult.UNDECIDED);
    when(filter2.apply(String.class)).thenReturn(FilterResult.DENIED);

    List<ReflectionAccessFilter> filters = List.of(filter1, filter2);

    FilterResult result = ReflectionAccessFilterHelper.getFilterResult(filters, String.class);
    // Should return DENIED because second filter returns DENIED
    assertEquals(FilterResult.DENIED, result);

    verify(filter1).apply(String.class);
    verify(filter2).apply(String.class);
  }

  @Test
    @Timeout(8000)
  void testCanAccess_true() throws Exception {
    AccessibleObject accessibleObject = mock(AccessibleObject.class);
    when(accessibleObject.isAccessible()).thenReturn(true);

    Object obj = new Object();

    // Should return true immediately if accessibleObject.isAccessible() is true
    assertTrue(ReflectionAccessFilterHelper.canAccess(accessibleObject, obj));
    verify(accessibleObject).isAccessible();
  }

  @Test
    @Timeout(8000)
  void testCanAccess_false_noFilters() throws Exception {
    AccessibleObject accessibleObject = mock(AccessibleObject.class);
    when(accessibleObject.isAccessible()).thenReturn(false);

    // Spy static method getFilterResult to return UNDECIDED
    try (MockedStatic<ReflectionAccessFilterHelper> mockedStatic = mockStatic(ReflectionAccessFilterHelper.class, Mockito.CALLS_REAL_METHODS)) {
      mockedStatic.when(() -> ReflectionAccessFilterHelper.getFilterResult(anyList(), any())).thenReturn(FilterResult.UNDECIDED);

      // Since accessibleObject.isAccessible() == false and filter result is UNDECIDED, canAccess returns false
      assertFalse(ReflectionAccessFilterHelper.canAccess(accessibleObject, new Object()));
    }
  }

  @Test
    @Timeout(8000)
  void testCanAccess_allowedByFilter() throws Exception {
    AccessibleObject accessibleObject = mock(AccessibleObject.class);
    when(accessibleObject.isAccessible()).thenReturn(false);

    ReflectionAccessFilter filter = mock(ReflectionAccessFilter.class);
    List<ReflectionAccessFilter> filters = List.of(filter);

    // Spy getFilterResult to return ALLOWED
    try (MockedStatic<ReflectionAccessFilterHelper> mockedStatic = mockStatic(ReflectionAccessFilterHelper.class, Mockito.CALLS_REAL_METHODS)) {
      mockedStatic.when(() -> ReflectionAccessFilterHelper.getFilterResult(filters, Object.class)).thenReturn(FilterResult.ALLOWED);

      // We cannot inject filters into canAccess directly, so this test is limited.
      // We test canAccess returns true if getFilterResult returns ALLOWED

      // For this test, call canAccess and intercept getFilterResult with any list and class to ALLOWED
      mockedStatic.when(() -> ReflectionAccessFilterHelper.getFilterResult(anyList(), any())).thenReturn(FilterResult.ALLOWED);

      assertTrue(ReflectionAccessFilterHelper.canAccess(accessibleObject, new Object()));
    }
  }

  @Test
    @Timeout(8000)
  void testCanAccess_deniedByFilter() throws Exception {
    AccessibleObject accessibleObject = mock(AccessibleObject.class);
    when(accessibleObject.isAccessible()).thenReturn(false);

    // Spy getFilterResult to return DENIED
    try (MockedStatic<ReflectionAccessFilterHelper> mockedStatic = mockStatic(ReflectionAccessFilterHelper.class, Mockito.CALLS_REAL_METHODS)) {
      mockedStatic.when(() -> ReflectionAccessFilterHelper.getFilterResult(anyList(), any())).thenReturn(FilterResult.DENIED);

      assertFalse(ReflectionAccessFilterHelper.canAccess(accessibleObject, new Object()));
    }
  }
}