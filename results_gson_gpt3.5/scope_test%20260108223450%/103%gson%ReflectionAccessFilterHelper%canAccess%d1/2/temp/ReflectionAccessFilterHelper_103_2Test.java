package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.util.List;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReflectionAccessFilterHelper_103_2Test {

  private AccessibleObject accessibleObjectMock;
  private Object targetObject;
  private Object accessCheckerMock;
  private Class<?> accessCheckerClass;

  @BeforeEach
  void setUp() throws Exception {
    accessibleObjectMock = mock(AccessibleObject.class);
    targetObject = new Object();

    accessCheckerClass = Class.forName("com.google.gson.internal.ReflectionAccessFilterHelper$AccessChecker");
    accessCheckerMock = mock(accessCheckerClass);

    Field instanceField = accessCheckerClass.getDeclaredField("INSTANCE");
    instanceField.setAccessible(true);

    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(instanceField, instanceField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

    instanceField.set(null, accessCheckerMock);
  }

  private interface AccessCheckerInterface {
    boolean canAccess(AccessibleObject ao, Object o);
  }

  @Test
    @Timeout(8000)
  void testCanAccess_returnsTrue_whenAccessCheckerReturnsTrue() throws Exception {
    // Find interfaces implemented by AccessChecker (if any)
    Class<?>[] interfaces = accessCheckerClass.getInterfaces();
    if (interfaces.length == 0) {
      // No interfaces, use our helper interface proxy
      AccessCheckerInterface proxy = (AccessCheckerInterface) Proxy.newProxyInstance(
          AccessCheckerInterface.class.getClassLoader(),
          new Class<?>[] { AccessCheckerInterface.class },
          new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
              if ("canAccess".equals(method.getName())
                  && args.length == 2
                  && args[0] == accessibleObjectMock
                  && args[1] == targetObject) {
                return true;
              }
              Method m = accessCheckerClass.getMethod("canAccess", AccessibleObject.class, Object.class);
              return m.invoke(accessCheckerMock, args);
            }
          });

      Field instanceField = accessCheckerClass.getDeclaredField("INSTANCE");
      instanceField.setAccessible(true);
      instanceField.set(null, proxy);

      boolean result = ReflectionAccessFilterHelper.canAccess(accessibleObjectMock, targetObject);
      assertTrue(result);
      return;
    }

    // If AccessChecker implements interfaces, create a Proxy with those interfaces:
    Object proxy = Proxy.newProxyInstance(
        accessCheckerClass.getClassLoader(),
        interfaces,
        (proxyObj, method, args) -> {
          if ("canAccess".equals(method.getName())
              && args.length == 2
              && args[0] == accessibleObjectMock
              && args[1] == targetObject) {
            return true;
          }
          return method.invoke(accessCheckerMock, args);
        });

    Field instanceField = accessCheckerClass.getDeclaredField("INSTANCE");
    instanceField.setAccessible(true);
    instanceField.set(null, proxy);

    boolean result = ReflectionAccessFilterHelper.canAccess(accessibleObjectMock, targetObject);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testCanAccess_returnsFalse_whenAccessCheckerReturnsFalse() throws Exception {
    AccessCheckerInterface proxy = (AccessCheckerInterface) Proxy.newProxyInstance(
        AccessCheckerInterface.class.getClassLoader(),
        new Class<?>[] { AccessCheckerInterface.class },
        new InvocationHandler() {
          @Override
          public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if ("canAccess".equals(method.getName())
                && args.length == 2
                && args[0] == accessibleObjectMock
                && args[1] == targetObject) {
              return false;
            }
            Method m = accessCheckerClass.getMethod("canAccess", AccessibleObject.class, Object.class);
            return m.invoke(accessCheckerMock, args);
          }
        });

    Field instanceField = accessCheckerClass.getDeclaredField("INSTANCE");
    instanceField.setAccessible(true);
    instanceField.set(null, proxy);

    boolean result = ReflectionAccessFilterHelper.canAccess(accessibleObjectMock, targetObject);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testPrivateConstructor() throws Exception {
    var constructor = ReflectionAccessFilterHelper.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    Object instance = constructor.newInstance();
    assertNotNull(instance);
  }
}