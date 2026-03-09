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
  void setUp() throws NoSuchMethodException, SecurityException {
    map = new LinkedTreeMap<>();
    equalMethod = LinkedTreeMap.class.getDeclaredMethod("equal", Object.class, Object.class);
    equalMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  void testEqualBothNull() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    assertTrue((Boolean) equalMethod.invoke(map, new Object[] {null, null}));
  }

  @Test
    @Timeout(8000)
  void testEqualFirstNullSecondNonNull() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    Object obj = new Object();
    assertFalse((Boolean) equalMethod.invoke(map, new Object[] {null, obj}));
  }

  @Test
    @Timeout(8000)
  void testEqualFirstNonNullSecondNull() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    Object obj = new Object();
    assertFalse((Boolean) equalMethod.invoke(map, new Object[] {obj, null}));
  }

  @Test
    @Timeout(8000)
  void testEqualSameObject() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    Object obj = new Object();
    assertTrue((Boolean) equalMethod.invoke(map, new Object[] {obj, obj}));
  }

  @Test
    @Timeout(8000)
  void testEqualDifferentObjectsButEqual() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    String s1 = new String("test");
    String s2 = new String("test");
    assertTrue((Boolean) equalMethod.invoke(map, new Object[] {s1, s2}));
  }

  @Test
    @Timeout(8000)
  void testEqualDifferentObjectsNotEqual() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    String s1 = "test1";
    String s2 = "test2";
    assertFalse((Boolean) equalMethod.invoke(map, new Object[] {s1, s2}));
  }
}