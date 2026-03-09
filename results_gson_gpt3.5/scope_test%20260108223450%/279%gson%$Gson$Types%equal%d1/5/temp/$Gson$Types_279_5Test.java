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
import java.lang.reflect.Type;
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

class GsonTypesEqualTest {

  @Test
    @Timeout(8000)
  public void testEqualWithBothNull() throws Exception {
    Method equalMethod = $Gson$Types.class.getDeclaredMethod("equal", Object.class, Object.class);
    equalMethod.setAccessible(true);
    Boolean result = (Boolean) equalMethod.invoke(null, (Object) null, (Object) null);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  public void testEqualWithSameObject() throws Exception {
    String obj = "test";
    Method equalMethod = $Gson$Types.class.getDeclaredMethod("equal", Object.class, Object.class);
    equalMethod.setAccessible(true);
    Boolean result = (Boolean) equalMethod.invoke(null, obj, obj);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  public void testEqualWithEqualObjects() throws Exception {
    String a = new String("abc");
    String b = new String("abc");
    Method equalMethod = $Gson$Types.class.getDeclaredMethod("equal", Object.class, Object.class);
    equalMethod.setAccessible(true);
    Boolean result = (Boolean) equalMethod.invoke(null, a, b);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  public void testEqualWithUnequalObjects() throws Exception {
    String a = "a";
    String b = "b";
    Method equalMethod = $Gson$Types.class.getDeclaredMethod("equal", Object.class, Object.class);
    equalMethod.setAccessible(true);
    Boolean result = (Boolean) equalMethod.invoke(null, a, b);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  public void testEqualWithFirstNullSecondNonNull() throws Exception {
    String b = "non-null";
    Method equalMethod = $Gson$Types.class.getDeclaredMethod("equal", Object.class, Object.class);
    equalMethod.setAccessible(true);
    Boolean result = (Boolean) equalMethod.invoke(null, (Object) null, b);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  public void testEqualWithFirstNonNullSecondNull() throws Exception {
    String a = "non-null";
    Method equalMethod = $Gson$Types.class.getDeclaredMethod("equal", Object.class, Object.class);
    equalMethod.setAccessible(true);
    Boolean result = (Boolean) equalMethod.invoke(null, a, (Object) null);
    assertFalse(result);
  }
}