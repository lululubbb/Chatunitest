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
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class $Gson$Types_279_1Test {

  private static Method equalMethod;

  @BeforeAll
  static void setUp() throws Exception {
    equalMethod = $Gson$Types.class.getDeclaredMethod("equal", Object.class, Object.class);
    equalMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  void equal_BothNull_ReturnsTrue() throws Exception {
    Boolean result = (Boolean) equalMethod.invoke(null, null, null);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void equal_FirstNullSecondNonNull_ReturnsFalse() throws Exception {
    Object obj = new Object();
    Boolean result = (Boolean) equalMethod.invoke(null, null, obj);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void equal_FirstNonNullSecondNull_ReturnsFalse() throws Exception {
    Object obj = new Object();
    Boolean result = (Boolean) equalMethod.invoke(null, obj, null);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void equal_BothSameObject_ReturnsTrue() throws Exception {
    Object obj = new Object();
    Boolean result = (Boolean) equalMethod.invoke(null, obj, obj);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void equal_BothEqualObjects_ReturnsTrue() throws Exception {
    String s1 = new String("test");
    String s2 = new String("test");
    Boolean result = (Boolean) equalMethod.invoke(null, s1, s2);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void equal_BothDifferentObjects_ReturnsFalse() throws Exception {
    String s1 = new String("test1");
    String s2 = new String("test2");
    Boolean result = (Boolean) equalMethod.invoke(null, s1, s2);
    assertFalse(result);
  }
}