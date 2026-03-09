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
import java.lang.reflect.InvocationTargetException;

public class $Gson$Types_279_3Test {

  @Test
    @Timeout(8000)
  public void testEqual_withBothNull() throws Exception {
    Method equalMethod = $Gson$Types.class.getDeclaredMethod("equal", Object.class, Object.class);
    equalMethod.setAccessible(true);
    Boolean result = (Boolean) equalMethod.invoke(null, null, null);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  public void testEqual_withFirstNullSecondNonNull() throws Exception {
    Method equalMethod = $Gson$Types.class.getDeclaredMethod("equal", Object.class, Object.class);
    equalMethod.setAccessible(true);
    Boolean result = (Boolean) equalMethod.invoke(null, null, "test");
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  public void testEqual_withFirstNonNullSecondNull() throws Exception {
    Method equalMethod = $Gson$Types.class.getDeclaredMethod("equal", Object.class, Object.class);
    equalMethod.setAccessible(true);
    Boolean result = (Boolean) equalMethod.invoke(null, "test", null);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  public void testEqual_withSameObject() throws Exception {
    Object obj = new Object();
    Method equalMethod = $Gson$Types.class.getDeclaredMethod("equal", Object.class, Object.class);
    equalMethod.setAccessible(true);
    Boolean result = (Boolean) equalMethod.invoke(null, obj, obj);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  public void testEqual_withEqualObjects() throws Exception {
    String a = new String("hello");
    String b = new String("hello");
    Method equalMethod = $Gson$Types.class.getDeclaredMethod("equal", Object.class, Object.class);
    equalMethod.setAccessible(true);
    Boolean result = (Boolean) equalMethod.invoke(null, a, b);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  public void testEqual_withNonEqualObjects() throws Exception {
    String a = "hello";
    String b = "world";
    Method equalMethod = $Gson$Types.class.getDeclaredMethod("equal", Object.class, Object.class);
    equalMethod.setAccessible(true);
    Boolean result = (Boolean) equalMethod.invoke(null, a, b);
    assertFalse(result);
  }
}