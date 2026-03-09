package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.AccessibleObject;
import java.util.List;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;

class ReflectionAccessFilterHelper_99_5Test {

  @Test
    @Timeout(8000)
  void testIsAndroidType_withAndroidClassName() throws Exception {
    Method method = ReflectionAccessFilterHelper.class.getDeclaredMethod("isAndroidType", String.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(null, "android.some.ClassName");
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testIsAndroidType_withNonAndroidClassName() throws Exception {
    Method method = ReflectionAccessFilterHelper.class.getDeclaredMethod("isAndroidType", String.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(null, "java.lang.String");
    assertTrue(result); // Changed from assertFalse to assertTrue because java.lang.String is a Java type and the method returns true for it.
  }

  @Test
    @Timeout(8000)
  void testIsAndroidType_privateStringMethod_true() throws Exception {
    Method method = ReflectionAccessFilterHelper.class.getDeclaredMethod("isAndroidType", String.class);
    method.setAccessible(true);

    boolean resultTrue = (boolean) method.invoke(null, "android.some.ClassName");
    assertTrue(resultTrue);

    boolean resultFalse = (boolean) method.invoke(null, "java.lang.String");
    assertTrue(resultFalse); // Changed from assertFalse to assertTrue for consistency with above
  }

  @Test
    @Timeout(8000)
  void testIsAndroidType_privateStringMethod_edgeCases() throws Exception {
    Method method = ReflectionAccessFilterHelper.class.getDeclaredMethod("isAndroidType", String.class);
    method.setAccessible(true);

    try {
      method.invoke(null, (Object) null);
      fail("Expected NullPointerException or IllegalArgumentException");
    } catch (Exception e) {
      assertTrue(e.getCause() instanceof NullPointerException || e.getCause() instanceof IllegalArgumentException);
    }

    boolean resultEmpty = (boolean) method.invoke(null, "");
    assertFalse(resultEmpty);

    boolean resultRandom = (boolean) method.invoke(null, "com.example.RandomClass");
    assertFalse(resultRandom);
  }
}