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
  void testEqual_bothNull() throws Exception {
    Method equalMethod = getEqualMethod();
    Object result = equalMethod.invoke(null, (Object) null, (Object) null);
    assertEquals(true, result);
  }

  @Test
    @Timeout(8000)
  void testEqual_firstNullSecondNonNull() throws Exception {
    Method equalMethod = getEqualMethod();
    Object result = equalMethod.invoke(null, (Object) null, "non-null");
    assertEquals(false, result);
  }

  @Test
    @Timeout(8000)
  void testEqual_firstNonNullSecondNull() throws Exception {
    Method equalMethod = getEqualMethod();
    Object result = equalMethod.invoke(null, "non-null", (Object) null);
    assertEquals(false, result);
  }

  @Test
    @Timeout(8000)
  void testEqual_sameObject() throws Exception {
    Method equalMethod = getEqualMethod();
    String s = "test";
    Object result = equalMethod.invoke(null, s, s);
    assertEquals(true, result);
  }

  @Test
    @Timeout(8000)
  void testEqual_differentObjectsEqual() throws Exception {
    Method equalMethod = getEqualMethod();
    Integer a = Integer.valueOf(5);
    Integer b = Integer.valueOf(5);
    Object result = equalMethod.invoke(null, a, b);
    assertEquals(true, result);
  }

  @Test
    @Timeout(8000)
  void testEqual_differentObjectsNotEqual() throws Exception {
    Method equalMethod = getEqualMethod();
    Integer a = Integer.valueOf(5);
    Integer b = Integer.valueOf(10);
    Object result = equalMethod.invoke(null, a, b);
    assertEquals(false, result);
  }

  private Method getEqualMethod() throws Exception {
    Class<?> gsonTypesClass = Class.forName("com.google.gson.internal.$Gson$Types");
    Method equalMethod = null;
    for (Method m : gsonTypesClass.getDeclaredMethods()) {
      if (m.getName().equals("equal") && m.getParameterCount() == 2) {
        equalMethod = m;
        break;
      }
    }
    if (equalMethod == null) {
      throw new NoSuchMethodException("Method 'equal' with two parameters not found");
    }
    equalMethod.setAccessible(true);
    return equalMethod;
  }
}