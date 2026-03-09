package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.List;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

class ReflectionAccessFilterHelper_101_4Test {

  @Test
    @Timeout(8000)
  void testIsAnyPlatformType_androidType() {
    // android.app.Activity class does not exist in non-Android JVMs, so simulate with a dummy class name
    Class<?> androidClass = createClassWithName("android.app.Activity");
    boolean result = ReflectionAccessFilterHelper.isAnyPlatformType(androidClass);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testIsAnyPlatformType_javaType() {
    // Java type className starts with "java."
    Class<?> javaClass = String.class; // java.lang.String
    boolean result = ReflectionAccessFilterHelper.isAnyPlatformType(javaClass);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testIsAnyPlatformType_kotlin() {
    Class<?> kotlinClass = createClassWithName("kotlin.Dummy");
    boolean result = ReflectionAccessFilterHelper.isAnyPlatformType(kotlinClass);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testIsAnyPlatformType_kotlinx() {
    Class<?> kotlinxClass = createClassWithName("kotlinx.Dummy");
    boolean result = ReflectionAccessFilterHelper.isAnyPlatformType(kotlinxClass);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testIsAnyPlatformType_scala() {
    Class<?> scalaClass = createClassWithName("scala.Dummy");
    boolean result = ReflectionAccessFilterHelper.isAnyPlatformType(scalaClass);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testIsAnyPlatformType_false() {
    Class<?> otherClass = OtherDummy.class;
    boolean result = ReflectionAccessFilterHelper.isAnyPlatformType(otherClass);
    assertFalse(result);
  }

  // Dummy classes to simulate package names for kotlin, kotlinx, scala
  static class OtherDummy {}

  /**
   * Helper method to create a Class<?> instance with a custom name prefix for testing.
   * Since we cannot create classes dynamically with arbitrary package names easily,
   * we create a dynamic proxy that overrides getName() via an InvocationHandler.
   */
  private static Class<?> createClassWithName(String desiredName) {
    return (Class<?>) Proxy.newProxyInstance(
        ReflectionAccessFilterHelperTest.class.getClassLoader(),
        new Class<?>[] { Class.class },
        (proxy, method, args) -> {
          if ("getName".equals(method.getName())) {
            return desiredName;
          }
          // For other methods, fallback to Object.class's method if possible
          return method.invoke(Object.class, args);
        });
  }

}