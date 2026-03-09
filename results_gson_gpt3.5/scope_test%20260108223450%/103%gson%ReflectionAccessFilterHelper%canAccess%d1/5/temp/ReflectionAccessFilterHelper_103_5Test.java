package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.util.List;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReflectionAccessFilterHelper_103_5Test {

  interface AccessCheckerInterface {
    boolean canAccess(AccessibleObject accessibleObject, Object obj);
  }

  @Test
    @Timeout(8000)
  void canAccess_returnsTrue_whenAccessCheckerReturnsTrue() throws Exception {
    AccessibleObject accessibleObject = mock(AccessibleObject.class);
    Object obj = new Object();

    AccessCheckerInterface proxyInstance = (AccessCheckerInterface) Proxy.newProxyInstance(
        AccessCheckerInterface.class.getClassLoader(),
        new Class<?>[]{AccessCheckerInterface.class},
        (proxy, method, args) -> {
          if ("canAccess".equals(method.getName())
              && args.length == 2
              && args[0] == accessibleObject
              && args[1] == obj) {
            return true;
          }
          return false;
        });

    try (MockedStatic<AccessCheckerHolder> accessCheckerMockedStatic = mockStatic(AccessCheckerHolder.class)) {
      accessCheckerMockedStatic.when(AccessCheckerHolder::getInstance).thenReturn(proxyInstance);

      try (MockedStatic<AccessCheckerHolder> accessCheckerStatic = mockStatic(AccessCheckerHolder.class)) {
        accessCheckerStatic.when(AccessCheckerHolder::getInstance).thenReturn(proxyInstance);

        boolean result = ReflectionAccessFilterHelper.canAccess(accessibleObject, obj);
        assertTrue(result);
      }
    }
  }

  @Test
    @Timeout(8000)
  void canAccess_returnsFalse_whenAccessCheckerReturnsFalse() throws Exception {
    AccessibleObject accessibleObject = mock(AccessibleObject.class);
    Object obj = new Object();

    AccessCheckerInterface proxyInstance = (AccessCheckerInterface) Proxy.newProxyInstance(
        AccessCheckerInterface.class.getClassLoader(),
        new Class<?>[]{AccessCheckerInterface.class},
        (proxy, method, args) -> {
          if ("canAccess".equals(method.getName())
              && args.length == 2
              && args[0] == accessibleObject
              && args[1] == obj) {
            return false;
          }
          return false;
        });

    try (MockedStatic<AccessCheckerHolder> accessCheckerMockedStatic = mockStatic(AccessCheckerHolder.class)) {
      accessCheckerMockedStatic.when(AccessCheckerHolder::getInstance).thenReturn(proxyInstance);

      try (MockedStatic<AccessCheckerHolder> accessCheckerStatic = mockStatic(AccessCheckerHolder.class)) {
        accessCheckerStatic.when(AccessCheckerHolder::getInstance).thenReturn(proxyInstance);

        boolean result = ReflectionAccessFilterHelper.canAccess(accessibleObject, obj);
        assertFalse(result);
      }
    }
  }

  @Test
    @Timeout(8000)
  void privateConstructor_isPrivate() throws Exception {
    var constructor = ReflectionAccessFilterHelper.class.getDeclaredConstructor();
    assertTrue(Modifier.isPrivate(constructor.getModifiers()));
    constructor.setAccessible(true);
    constructor.newInstance();
  }

  // Helper class to abstract AccessChecker.INSTANCE access for mocking
  static class AccessCheckerHolder {
    static AccessCheckerInterface getInstance() {
      try {
        Class<?> accessCheckerClass = Class.forName("com.google.gson.internal.AccessChecker");
        Object instance = accessCheckerClass.getField("INSTANCE").get(null);
        return (AccessCheckerInterface) Proxy.newProxyInstance(
            AccessCheckerInterface.class.getClassLoader(),
            new Class<?>[]{AccessCheckerInterface.class},
            (proxy, method, args) -> {
              Method m = accessCheckerClass.getMethod(method.getName(), method.getParameterTypes());
              return m.invoke(instance, args);
            });
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }
}