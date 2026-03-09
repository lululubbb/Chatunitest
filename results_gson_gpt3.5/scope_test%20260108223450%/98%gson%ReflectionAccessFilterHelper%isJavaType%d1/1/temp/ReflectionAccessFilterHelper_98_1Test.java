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

class ReflectionAccessFilterHelper_98_1Test {

  @Test
    @Timeout(8000)
  void testIsJavaType_startsWithJava() throws NoSuchMethodException, IllegalAccessException {
    Method method = ReflectionAccessFilterHelper.class.getDeclaredMethod("isJavaType", String.class);
    method.setAccessible(true);

    try {
      // className starts with "java."
      boolean resultJava = (boolean) method.invoke(null, "java.lang.String");
      assertTrue(resultJava);

      // className starts with "javax."
      boolean resultJavax = (boolean) method.invoke(null, "javax.servlet.http.HttpServlet");
      assertTrue(resultJavax);

      // className does not start with "java." or "javax."
      boolean resultOther = (boolean) method.invoke(null, "com.google.gson.Gson");
      assertFalse(resultOther);

      // className is "java" (no dot)
      boolean resultNoDot = (boolean) method.invoke(null, "java");
      assertFalse(resultNoDot);

      // className is empty string
      boolean resultEmpty = (boolean) method.invoke(null, "");
      assertFalse(resultEmpty);

      // Passing null should throw InvocationTargetException caused by NullPointerException
      method.invoke(null, (Object) null);
      fail("Expected InvocationTargetException when passing null");
    } catch (InvocationTargetException e) {
      // The cause should be NullPointerException
      assertTrue(e.getCause() instanceof NullPointerException);
    }
  }
}