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

class ReflectionAccessFilterHelper_98_6Test {

  @Test
    @Timeout(8000)
  void testIsJavaType_withJavaPrefix() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = ReflectionAccessFilterHelper.class.getDeclaredMethod("isJavaType", String.class);
    method.setAccessible(true);

    // className starts with "java."
    boolean resultJava = (boolean) method.invoke(null, "java.lang.String");
    assertTrue(resultJava);

    // className starts with "javax."
    boolean resultJavax = (boolean) method.invoke(null, "javax.servlet.Servlet");
    assertTrue(resultJavax);

    // className does not start with "java." or "javax."
    boolean resultOther = (boolean) method.invoke(null, "com.google.gson.Gson");
    assertFalse(resultOther);

    // className is exactly "java."
    boolean resultExactJava = (boolean) method.invoke(null, "java.");
    assertTrue(resultExactJava);

    // className is exactly "javax."
    boolean resultExactJavax = (boolean) method.invoke(null, "javax.");
    assertTrue(resultExactJavax);

    // className is empty string
    boolean resultEmpty = (boolean) method.invoke(null, "");
    assertFalse(resultEmpty);

    // className is null - should throw NullPointerException
    assertThrows(InvocationTargetException.class, () -> method.invoke(null, (Object) null));
  }
}