package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.AccessibleObject;
import java.util.List;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

class ReflectionAccessFilterHelper_98_2Test {

  @Test
    @Timeout(8000)
  void testIsJavaType_javaPrefix() throws Exception {
    // Access private static method isJavaType(String)
    Method isJavaTypeMethod = ReflectionAccessFilterHelper.class.getDeclaredMethod("isJavaType", String.class);
    isJavaTypeMethod.setAccessible(true);

    // Test with className starting with "java."
    boolean resultJava = (boolean) isJavaTypeMethod.invoke(null, "java.lang.String");
    assertTrue(resultJava);

    // Test with className starting with "javax."
    boolean resultJavax = (boolean) isJavaTypeMethod.invoke(null, "javax.servlet.Servlet");
    assertTrue(resultJavax);

    // Test with className not starting with "java." or "javax."
    boolean resultOther = (boolean) isJavaTypeMethod.invoke(null, "com.google.gson.Gson");
    assertFalse(resultOther);

    // Test with empty string
    boolean resultEmpty = (boolean) isJavaTypeMethod.invoke(null, "");
    assertFalse(resultEmpty);

    // Test with null - should throw NullPointerException
    assertThrows(InvocationTargetException.class, () -> isJavaTypeMethod.invoke(null, (Object) null));
  }
}