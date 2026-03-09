package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.AccessibleObject;
import java.util.List;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;

import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class ReflectionAccessFilterHelper_99_4Test {

  @Test
    @Timeout(8000)
  void testIsAndroidType_withAndroidClassName() throws Exception {
    // Use reflection to invoke private static boolean isAndroidType(String className)
    Method isAndroidTypeStringMethod = ReflectionAccessFilterHelper.class.getDeclaredMethod("isAndroidType", String.class);
    isAndroidTypeStringMethod.setAccessible(true);

    // Android class name example: "android.app.Activity"
    boolean result = (boolean) isAndroidTypeStringMethod.invoke(null, "android.app.Activity");
    assertTrue(result);

    // Android class name with prefix "android." should return true
    assertTrue((boolean) isAndroidTypeStringMethod.invoke(null, "android.something.ClassName"));

    // Non-Android class names should return false
    assertFalse((boolean) isAndroidTypeStringMethod.invoke(null, "java.lang.String"));
    assertFalse((boolean) isAndroidTypeStringMethod.invoke(null, "javax.swing.JButton"));
    assertFalse((boolean) isAndroidTypeStringMethod.invoke(null, "com.google.gson.internal.ReflectionAccessFilterHelper"));
    assertFalse((boolean) isAndroidTypeStringMethod.invoke(null, ""));
    assertFalse((boolean) isAndroidTypeStringMethod.invoke(null, "androidx.appcompat.widget.AppCompatButton")); // androidx is not android prefix
  }

  @Test
    @Timeout(8000)
  void testIsAndroidType_withClass() throws Exception {
    // Class whose name starts with "android." - we cannot create such class here,
    // so we test with a normal class, expecting false
    assertFalse(ReflectionAccessFilterHelper.isAndroidType(String.class));

    // null class - should throw NullPointerException
    assertThrows(NullPointerException.class, () -> ReflectionAccessFilterHelper.isAndroidType(null));
  }
}