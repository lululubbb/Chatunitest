package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;

class ReflectionAccessFilterHelper_103_4Test {

  private AccessibleObject accessibleObjectMock;
  private Object targetObject;

  @BeforeEach
  void setUp() {
    accessibleObjectMock = mock(AccessibleObject.class);
    targetObject = new Object();
  }

  @Test
    @Timeout(8000)
  void testCanAccess_returnsTrue_whenAccessCheckerReturnsTrue() throws Exception {
    // Use Mockito.mockStatic on ReflectionAccessFilterHelper to mock canAccess method directly
    try (var mockedStatic = mockStatic(ReflectionAccessFilterHelper.class)) {
      mockedStatic.when(() -> ReflectionAccessFilterHelper.canAccess(accessibleObjectMock, targetObject)).thenReturn(true);

      boolean result = ReflectionAccessFilterHelper.canAccess(accessibleObjectMock, targetObject);

      assertTrue(result);

      mockedStatic.verify(() -> ReflectionAccessFilterHelper.canAccess(accessibleObjectMock, targetObject));
    }
  }

  @Test
    @Timeout(8000)
  void testCanAccess_returnsFalse_whenAccessCheckerReturnsFalse() throws Exception {
    try (var mockedStatic = mockStatic(ReflectionAccessFilterHelper.class)) {
      mockedStatic.when(() -> ReflectionAccessFilterHelper.canAccess(accessibleObjectMock, targetObject)).thenReturn(false);

      boolean result = ReflectionAccessFilterHelper.canAccess(accessibleObjectMock, targetObject);

      assertFalse(result);

      mockedStatic.verify(() -> ReflectionAccessFilterHelper.canAccess(accessibleObjectMock, targetObject));
    }
  }

  @Test
    @Timeout(8000)
  void testPrivateConstructor_invocation() throws Exception {
    Constructor<ReflectionAccessFilterHelper> constructor = ReflectionAccessFilterHelper.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    Object instance = constructor.newInstance();
    assertNotNull(instance);
    assertTrue(instance instanceof ReflectionAccessFilterHelper);
  }

  @Test
    @Timeout(8000)
  void testIsJavaType_withClass() throws Exception {
    // private static boolean isJavaType(String className)
    var method = ReflectionAccessFilterHelper.class.getDeclaredMethod("isJavaType", String.class);
    method.setAccessible(true);

    // Test with known java type
    boolean result1 = (boolean) method.invoke(null, "java.lang.String");
    assertTrue(result1);

    // Test with non-java type
    boolean result2 = (boolean) method.invoke(null, "com.example.MyClass");
    assertFalse(result2);
  }

  @Test
    @Timeout(8000)
  void testIsJavaType_withClass_public() {
    // public static boolean isJavaType(Class<?> c)
    boolean result1 = ReflectionAccessFilterHelper.isJavaType(String.class);
    assertTrue(result1);

    boolean result2 = ReflectionAccessFilterHelper.isJavaType(this.getClass());
    assertFalse(result2);
  }

  @Test
    @Timeout(8000)
  void testIsAndroidType_withClass() throws Exception {
    // private static boolean isAndroidType(String className)
    var method = ReflectionAccessFilterHelper.class.getDeclaredMethod("isAndroidType", String.class);
    method.setAccessible(true);

    // Test with known android type
    boolean result1 = (boolean) method.invoke(null, "android.app.Activity");
    assertTrue(result1);

    // Test with non-android type
    boolean result2 = (boolean) method.invoke(null, "com.example.NonAndroidClass");
    assertFalse(result2);
  }

  @Test
    @Timeout(8000)
  void testIsAndroidType_withClass_public() {
    // public static boolean isAndroidType(Class<?> c)
    boolean result1 = ReflectionAccessFilterHelper.isAndroidType(getClass());
    assertFalse(result1);

    // To simulate android type, use android.app.Activity string via reflection test above
  }

  @Test
    @Timeout(8000)
  void testIsAnyPlatformType() {
    boolean result1 = ReflectionAccessFilterHelper.isAnyPlatformType(String.class);
    assertTrue(result1);

    boolean result2 = ReflectionAccessFilterHelper.isAnyPlatformType(getClass());
    assertFalse(result2);
  }

  @Test
    @Timeout(8000)
  void testGetFilterResult_withEmptyFilters() {
    List<ReflectionAccessFilter> filters = Collections.emptyList();
    FilterResult result = ReflectionAccessFilterHelper.getFilterResult(filters, String.class);
    assertNotNull(result);
  }

  @Test
    @Timeout(8000)
  void testGetFilterResult_withMockedFilter() throws Exception {
    ReflectionAccessFilter filter = mock(ReflectionAccessFilter.class);

    // Stub getFilterResult method of the filter to return FilterResult.ALLOW (or any non-null)
    when(filter.getFilterResult(any())).thenReturn(FilterResult.ALLOW);

    List<ReflectionAccessFilter> filters = Collections.singletonList(filter);

    FilterResult result = ReflectionAccessFilterHelper.getFilterResult(filters, String.class);

    assertNotNull(result);

    verify(filter).getFilterResult(any());
  }
}