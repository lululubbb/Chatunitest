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

class ReflectionAccessFilterHelper_98_4Test {

  @Test
    @Timeout(8000)
  void testIsJavaType_startsWithJava() throws Exception {
    // invoke private static method isJavaType(String) via reflection
    Method method = ReflectionAccessFilterHelper.class.getDeclaredMethod("isJavaType", String.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(null, "java.lang.String");
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testIsJavaType_startsWithJavax() throws Exception {
    Method method = ReflectionAccessFilterHelper.class.getDeclaredMethod("isJavaType", String.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(null, "javax.servlet.Servlet");
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testIsJavaType_notStartsWithJavaOrJavax() throws Exception {
    Method method = ReflectionAccessFilterHelper.class.getDeclaredMethod("isJavaType", String.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(null, "com.google.gson.Gson");
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testIsJavaType_emptyString() throws Exception {
    Method method = ReflectionAccessFilterHelper.class.getDeclaredMethod("isJavaType", String.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(null, "");
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testIsJavaType_nullString() throws Exception {
    Method method = ReflectionAccessFilterHelper.class.getDeclaredMethod("isJavaType", String.class);
    method.setAccessible(true);

    // Passing null will cause NullPointerException in startsWith, test this behavior
    assertThrows(InvocationTargetException.class, () -> method.invoke(null, (Object) null));
  }
}