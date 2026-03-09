package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;

class ReflectionAccessFilterHelper_103_6Test {

  private AccessibleObject accessibleObjectMock;
  private Object targetObject;

  @BeforeEach
  void setUp() {
    accessibleObjectMock = mock(AccessibleObject.class);
    targetObject = new Object();
  }

  @Test
    @Timeout(8000)
  void canAccess_returnsTrue_whenAccessCheckerReturnsTrue() throws Exception {
    Class<?> accessCheckerClass = Class.forName("com.google.gson.internal.AccessChecker");
    Field instanceField = accessCheckerClass.getDeclaredField("INSTANCE");
    instanceField.setAccessible(true);

    try (MockedStatic<?> mockedAccessChecker = Mockito.mockStatic(accessCheckerClass)) {
      Object proxy = Proxy.newProxyInstance(
          accessCheckerClass.getClassLoader(),
          new Class<?>[] { accessCheckerClass },
          (proxyObj, method, args) -> {
            if ("canAccess".equals(method.getName())
                && args.length == 2
                && args[0] == accessibleObjectMock
                && args[1] == targetObject) {
              return true;
            }
            // For other methods, return default values or null
            if (method.getReturnType().isPrimitive()) {
              if (method.getReturnType() == boolean.class) {
                return false;
              }
              if (method.getReturnType() == void.class) {
                return null;
              }
              if (method.getReturnType() == int.class
                  || method.getReturnType() == long.class
                  || method.getReturnType() == short.class
                  || method.getReturnType() == byte.class
                  || method.getReturnType() == float.class
                  || method.getReturnType() == double.class
                  || method.getReturnType() == char.class) {
                return 0;
              }
            }
            return null;
          });

      mockedAccessChecker.when(() -> instanceField.get(null)).thenReturn(proxy);

      boolean result = ReflectionAccessFilterHelper.canAccess(accessibleObjectMock, targetObject);

      assertTrue(result);
    }
  }

  @Test
    @Timeout(8000)
  void canAccess_returnsFalse_whenAccessCheckerReturnsFalse() throws Exception {
    Class<?> accessCheckerClass = Class.forName("com.google.gson.internal.AccessChecker");
    Field instanceField = accessCheckerClass.getDeclaredField("INSTANCE");
    instanceField.setAccessible(true);

    try (MockedStatic<?> mockedAccessChecker = Mockito.mockStatic(accessCheckerClass)) {
      Object proxy = Proxy.newProxyInstance(
          accessCheckerClass.getClassLoader(),
          new Class<?>[] { accessCheckerClass },
          (proxyObj, method, args) -> {
            if ("canAccess".equals(method.getName())
                && args.length == 2
                && args[0] == accessibleObjectMock
                && args[1] == targetObject) {
              return false;
            }
            if (method.getReturnType().isPrimitive()) {
              if (method.getReturnType() == boolean.class) {
                return false;
              }
              if (method.getReturnType() == void.class) {
                return null;
              }
              if (method.getReturnType() == int.class
                  || method.getReturnType() == long.class
                  || method.getReturnType() == short.class
                  || method.getReturnType() == byte.class
                  || method.getReturnType() == float.class
                  || method.getReturnType() == double.class
                  || method.getReturnType() == char.class) {
                return 0;
              }
            }
            return null;
          });

      mockedAccessChecker.when(() -> instanceField.get(null)).thenReturn(proxy);

      boolean result = ReflectionAccessFilterHelper.canAccess(accessibleObjectMock, targetObject);

      assertFalse(result);
    }
  }

  @Test
    @Timeout(8000)
  void privateConstructor_isPrivate() throws Exception {
    Constructor<ReflectionAccessFilterHelper> constructor = ReflectionAccessFilterHelper.class.getDeclaredConstructor();
    assertTrue(Modifier.isPrivate(constructor.getModifiers()));
    constructor.setAccessible(true);
    Object instance = constructor.newInstance();
    assertNotNull(instance);
  }

  @Test
    @Timeout(8000)
  void isJavaType_className_variants() throws Exception {
    var method = ReflectionAccessFilterHelper.class.getDeclaredMethod("isJavaType", String.class);
    method.setAccessible(true);

    assertTrue((Boolean) method.invoke(null, "java.lang.String"));
    assertTrue((Boolean) method.invoke(null, "java.util.List"));
    assertFalse((Boolean) method.invoke(null, "com.google.gson.internal.SomeClass"));
    assertFalse((Boolean) method.invoke(null, "android.os.Build"));
  }

  @Test
    @Timeout(8000)
  void isJavaType_class_variants() {
    assertTrue(ReflectionAccessFilterHelper.isJavaType(String.class));
    assertTrue(ReflectionAccessFilterHelper.isJavaType(List.class));
    assertFalse(ReflectionAccessFilterHelper.isJavaType(this.getClass()));
  }

  @Test
    @Timeout(8000)
  void isAndroidType_className_variants() throws Exception {
    var method = ReflectionAccessFilterHelper.class.getDeclaredMethod("isAndroidType", String.class);
    method.setAccessible(true);

    assertFalse((Boolean) method.invoke(null, "java.lang.String"));
    assertFalse((Boolean) method.invoke(null, "com.google.gson.internal.SomeClass"));
  }

  @Test
    @Timeout(8000)
  void isAndroidType_class_variants() {
    assertFalse(ReflectionAccessFilterHelper.isAndroidType(String.class));
    assertFalse(ReflectionAccessFilterHelper.isAndroidType(this.getClass()));
  }

  @Test
    @Timeout(8000)
  void isAnyPlatformType_returnsTrue_forJavaAndAndroid() {
    assertTrue(ReflectionAccessFilterHelper.isAnyPlatformType(String.class));
  }

  @Test
    @Timeout(8000)
  void isAnyPlatformType_returnsFalse_forOther() {
    assertFalse(ReflectionAccessFilterHelper.isAnyPlatformType(this.getClass()));
  }

  @Test
    @Timeout(8000)
  void getFilterResult_returnsExpectedResult() {
    @SuppressWarnings("unchecked")
    ReflectionAccessFilter filter1 = mock(ReflectionAccessFilter.class);
    @SuppressWarnings("unchecked")
    ReflectionAccessFilter filter2 = mock(ReflectionAccessFilter.class);
    List<ReflectionAccessFilter> filters = List.of(filter1, filter2);
    Class<?> clazz = String.class;

    when(filter1.apply(clazz)).thenReturn(FilterResult.ALLOW);
    when(filter2.apply(clazz)).thenReturn(FilterResult.BLOCK);

    FilterResult result = ReflectionAccessFilterHelper.getFilterResult(filters, clazz);

    assertEquals(FilterResult.BLOCK, result);
  }
}