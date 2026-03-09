package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.AccessibleObject;
import java.util.List;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;

public class ReflectionAccessFilterHelper_99_2Test {

  @Test
    @Timeout(8000)
  public void testIsAndroidType_withAndroidClassName() throws Exception {
    Method isAndroidTypeStringMethod = ReflectionAccessFilterHelper.class.getDeclaredMethod("isAndroidType", String.class);
    isAndroidTypeStringMethod.setAccessible(true);

    // Typical Android class prefix, e.g. android.os.Build
    boolean result = (boolean) isAndroidTypeStringMethod.invoke(null, "android.os.Build");
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  public void testIsAndroidType_withNonAndroidClassName() throws Exception {
    Method isAndroidTypeStringMethod = ReflectionAccessFilterHelper.class.getDeclaredMethod("isAndroidType", String.class);
    isAndroidTypeStringMethod.setAccessible(true);

    // Corrected non-Android class name, javax.lang.String does not exist, use java.lang.String
    boolean result = (boolean) isAndroidTypeStringMethod.invoke(null, "java.lang.String");
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  public void testIsAndroidType_withAndroidClass() throws Exception {
    Method isAndroidTypeClassMethod = ReflectionAccessFilterHelper.class.getDeclaredMethod("isAndroidType", Class.class);
    isAndroidTypeClassMethod.setAccessible(true);

    // Use java.lang.String which is not an Android type
    boolean result = (boolean) isAndroidTypeClassMethod.invoke(null, String.class);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  public void testIsAndroidType_withNonAndroidClass() {
    boolean result = ReflectionAccessFilterHelper.isAndroidType(String.class);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  public void testIsAndroidType_withAnonymousClass() {
    Class<?> anonClass = new Object(){}.getClass();
    boolean result = ReflectionAccessFilterHelper.isAndroidType(anonClass);
    assertFalse(result);
  }
}