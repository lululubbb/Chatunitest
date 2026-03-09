package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.AccessibleObject;
import java.util.List;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Test;

class ReflectionAccessFilterHelper_98_5Test {

  @Test
    @Timeout(8000)
  void testIsJavaType_startsWithJava() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = ReflectionAccessFilterHelper.class.getDeclaredMethod("isJavaType", String.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(null, "java.lang.String");
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testIsJavaType_startsWithJavax() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = ReflectionAccessFilterHelper.class.getDeclaredMethod("isJavaType", String.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(null, "javax.servlet.Servlet");
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testIsJavaType_notJavaOrJavax() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = ReflectionAccessFilterHelper.class.getDeclaredMethod("isJavaType", String.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(null, "com.example.MyClass");
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testIsJavaType_emptyString() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = ReflectionAccessFilterHelper.class.getDeclaredMethod("isJavaType", String.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(null, "");
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testIsJavaType_nullInput() throws NoSuchMethodException {
    Method method = ReflectionAccessFilterHelper.class.getDeclaredMethod("isJavaType", String.class);
    method.setAccessible(true);

    assertThrows(InvocationTargetException.class, () -> {
      method.invoke(null, new Object[] { null });
    });
  }
}