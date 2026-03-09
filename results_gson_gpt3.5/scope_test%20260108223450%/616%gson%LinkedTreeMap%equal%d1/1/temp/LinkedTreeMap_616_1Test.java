package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LinkedTreeMapEqualTest {

  private LinkedTreeMap<Object, Object> map;
  private Method equalMethod;

  @BeforeEach
  void setUp() throws NoSuchMethodException {
    map = new LinkedTreeMap<>();
    equalMethod = LinkedTreeMap.class.getDeclaredMethod("equal", Object.class, Object.class);
    equalMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  void testEqual_bothNull() throws IllegalAccessException, InvocationTargetException {
    assertTrue((Boolean) equalMethod.invoke(map, (Object) null, (Object) null));
  }

  @Test
    @Timeout(8000)
  void testEqual_firstNullSecondNonNull() throws IllegalAccessException, InvocationTargetException {
    assertFalse((Boolean) equalMethod.invoke(map, (Object) null, "nonNull"));
  }

  @Test
    @Timeout(8000)
  void testEqual_firstNonNullSecondNull() throws IllegalAccessException, InvocationTargetException {
    assertFalse((Boolean) equalMethod.invoke(map, "nonNull", (Object) null));
  }

  @Test
    @Timeout(8000)
  void testEqual_sameObject() throws IllegalAccessException, InvocationTargetException {
    Object obj = new Object();
    assertTrue((Boolean) equalMethod.invoke(map, obj, obj));
  }

  @Test
    @Timeout(8000)
  void testEqual_equalObjects() throws IllegalAccessException, InvocationTargetException {
    String a = new String("test");
    String b = new String("test");
    assertTrue((Boolean) equalMethod.invoke(map, a, b));
  }

  @Test
    @Timeout(8000)
  void testEqual_nonEqualObjects() throws IllegalAccessException, InvocationTargetException {
    String a = "test1";
    String b = "test2";
    assertFalse((Boolean) equalMethod.invoke(map, a, b));
  }

  @Test
    @Timeout(8000)
  void testEqual_differentTypesSameToString() throws IllegalAccessException, InvocationTargetException {
    Integer a = 123;
    String b = "123";
    assertFalse((Boolean) equalMethod.invoke(map, a, b));
  }
}