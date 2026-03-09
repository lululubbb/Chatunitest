package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import static com.google.gson.internal.$Gson$Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

class $Gson$Types_279_4Test {

  @Test
    @Timeout(8000)
  void testEqual_BothNull() throws Exception {
    Method equalMethod = $Gson$Types.class.getDeclaredMethod("equal", Object.class, Object.class);
    equalMethod.setAccessible(true);
    boolean result = (boolean) equalMethod.invoke(null, null, null);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testEqual_FirstNullSecondNonNull() throws Exception {
    Method equalMethod = $Gson$Types.class.getDeclaredMethod("equal", Object.class, Object.class);
    equalMethod.setAccessible(true);
    boolean result = (boolean) equalMethod.invoke(null, null, "non-null");
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testEqual_FirstNonNullSecondNull() throws Exception {
    Method equalMethod = $Gson$Types.class.getDeclaredMethod("equal", Object.class, Object.class);
    equalMethod.setAccessible(true);
    boolean result = (boolean) equalMethod.invoke(null, "non-null", null);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testEqual_BothSameObject() throws Exception {
    Object obj = new Object();
    Method equalMethod = $Gson$Types.class.getDeclaredMethod("equal", Object.class, Object.class);
    equalMethod.setAccessible(true);
    boolean result = (boolean) equalMethod.invoke(null, obj, obj);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testEqual_BothEqualObjects() throws Exception {
    String a = new String("test");
    String b = new String("test");
    Method equalMethod = $Gson$Types.class.getDeclaredMethod("equal", Object.class, Object.class);
    equalMethod.setAccessible(true);
    boolean result = (boolean) equalMethod.invoke(null, a, b);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testEqual_BothDifferentObjects() throws Exception {
    String a = "test1";
    String b = "test2";
    Method equalMethod = $Gson$Types.class.getDeclaredMethod("equal", Object.class, Object.class);
    equalMethod.setAccessible(true);
    boolean result = (boolean) equalMethod.invoke(null, a, b);
    assertFalse(result);
  }
}