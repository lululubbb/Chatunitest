package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.List;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ReflectionAccessFilterHelper_101_1Test {

  @Test
    @Timeout(8000)
  void testIsAnyPlatformType_androidType() {
    Class<?> androidClass = android.mock.Class.class;
    assertTrue(ReflectionAccessFilterHelper.isAnyPlatformType(androidClass));
  }

  @Test
    @Timeout(8000)
  void testIsAnyPlatformType_kotlinPackage() {
    Class<?> kotlinClass = kotlin.SomeClass.class;
    assertTrue(ReflectionAccessFilterHelper.isAnyPlatformType(kotlinClass));
  }

  @Test
    @Timeout(8000)
  void testIsAnyPlatformType_kotlinxPackage() {
    Class<?> kotlinxClass = kotlinx.SomeClass.class;
    assertTrue(ReflectionAccessFilterHelper.isAnyPlatformType(kotlinxClass));
  }

  @Test
    @Timeout(8000)
  void testIsAnyPlatformType_scalaPackage() {
    Class<?> scalaClass = scala.SomeClass.class;
    assertTrue(ReflectionAccessFilterHelper.isAnyPlatformType(scalaClass));
  }

  @Test
    @Timeout(8000)
  void testIsAnyPlatformType_otherPackage() {
    Class<?> otherClass = com.example.SomeClass.class;
    assertFalse(ReflectionAccessFilterHelper.isAnyPlatformType(otherClass));
  }
}