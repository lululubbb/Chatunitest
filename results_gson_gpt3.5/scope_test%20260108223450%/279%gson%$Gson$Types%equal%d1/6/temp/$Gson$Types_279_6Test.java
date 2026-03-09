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
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class GsonTypesEqualTest {

  private static Method equalMethod;

  @BeforeAll
  public static void setUp() throws Exception {
    equalMethod = $Gson$Types.class.getDeclaredMethod("equal", Object.class, Object.class);
    equalMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  public void testEqualBothNull() throws Exception {
    Boolean result = (Boolean) equalMethod.invoke(null, (Object) null, (Object) null);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  public void testEqualFirstNullSecondNonNull() throws Exception {
    Boolean result = (Boolean) equalMethod.invoke(null, (Object) null, "test");
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  public void testEqualFirstNonNullSecondNull() throws Exception {
    Boolean result = (Boolean) equalMethod.invoke(null, "test", (Object) null);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  public void testEqualSameObject() throws Exception {
    String obj = "same";
    Boolean result = (Boolean) equalMethod.invoke(null, obj, obj);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  public void testEqualDifferentObjectsEqual() throws Exception {
    String a = new String("equal");
    String b = new String("equal");
    Boolean result = (Boolean) equalMethod.invoke(null, a, b);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  public void testEqualDifferentObjectsNotEqual() throws Exception {
    String a = "a";
    String b = "b";
    Boolean result = (Boolean) equalMethod.invoke(null, a, b);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  public void testEqualDifferentTypes() throws Exception {
    Integer a = 1;
    String b = "1";
    Boolean result = (Boolean) equalMethod.invoke(null, a, b);
    assertFalse(result);
  }
}