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

class ReflectionAccessFilterHelper_98_3Test {

  @Test
    @Timeout(8000)
  void testIsJavaType_startsWithJava() throws Exception {
    // Use reflection to access private static method isJavaType(String)
    Method method = ReflectionAccessFilterHelper.class.getDeclaredMethod("isJavaType", String.class);
    method.setAccessible(true);

    // Test string starting with "java."
    boolean resultJava = (boolean) method.invoke(null, "java.lang.String");
    assertTrue(resultJava);

    // Test string starting with "javax."
    boolean resultJavax = (boolean) method.invoke(null, "javax.servlet.Servlet");
    assertTrue(resultJavax);

    // Test string not starting with "java." or "javax."
    boolean resultOther = (boolean) method.invoke(null, "com.google.gson.internal.ReflectionAccessFilterHelper");
    assertFalse(resultOther);

    // Test empty string
    boolean resultEmpty = (boolean) method.invoke(null, "");
    assertFalse(resultEmpty);

    // Test string starting with "java" but no dot
    boolean resultNoDot = (boolean) method.invoke(null, "javaLang");
    assertFalse(resultNoDot);

    // Test string starting with "javax" but no dot
    boolean resultNoDot2 = (boolean) method.invoke(null, "javaxx");
    assertFalse(resultNoDot2);
  }
}