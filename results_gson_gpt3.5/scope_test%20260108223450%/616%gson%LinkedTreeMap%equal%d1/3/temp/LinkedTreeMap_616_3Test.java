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

  private LinkedTreeMap<Object, Object> linkedTreeMap;
  private Method equalMethod;

  @BeforeEach
  void setUp() throws NoSuchMethodException {
    linkedTreeMap = new LinkedTreeMap<>();
    equalMethod = LinkedTreeMap.class.getDeclaredMethod("equal", Object.class, Object.class);
    equalMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  void testEqual_bothNull() throws InvocationTargetException, IllegalAccessException {
    Object a = null;
    Object b = null;
    boolean result = (boolean) equalMethod.invoke(linkedTreeMap, a, b);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testEqual_firstNullSecondNonNull() throws InvocationTargetException, IllegalAccessException {
    Object a = null;
    Object b = "test";
    boolean result = (boolean) equalMethod.invoke(linkedTreeMap, a, b);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testEqual_firstNonNullSecondNull() throws InvocationTargetException, IllegalAccessException {
    Object a = "test";
    Object b = null;
    boolean result = (boolean) equalMethod.invoke(linkedTreeMap, a, b);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testEqual_sameObject() throws InvocationTargetException, IllegalAccessException {
    Object a = "same";
    Object b = a;
    boolean result = (boolean) equalMethod.invoke(linkedTreeMap, a, b);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testEqual_equalObjects() throws InvocationTargetException, IllegalAccessException {
    Object a = new String("equal");
    Object b = new String("equal");
    boolean result = (boolean) equalMethod.invoke(linkedTreeMap, a, b);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testEqual_nonEqualObjects() throws InvocationTargetException, IllegalAccessException {
    Object a = "a";
    Object b = "b";
    boolean result = (boolean) equalMethod.invoke(linkedTreeMap, a, b);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testEqual_differentTypes() throws InvocationTargetException, IllegalAccessException {
    Object a = "string";
    Object b = 123;
    boolean result = (boolean) equalMethod.invoke(linkedTreeMap, a, b);
    assertFalse(result);
  }
}