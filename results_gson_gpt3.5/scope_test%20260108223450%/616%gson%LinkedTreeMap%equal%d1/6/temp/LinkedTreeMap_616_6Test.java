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

public class LinkedTreeMap_616_6Test {

  private LinkedTreeMap<Object, Object> map;
  private Method equalMethod;

  @BeforeEach
  public void setUp() throws NoSuchMethodException {
    map = new LinkedTreeMap<>();
    equalMethod = LinkedTreeMap.class.getDeclaredMethod("equal", Object.class, Object.class);
    equalMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  public void testEqual_bothNull() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    assertTrue((Boolean) equalMethod.invoke(map, null, null));
  }

  @Test
    @Timeout(8000)
  public void testEqual_firstNullSecondNonNull() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    Object b = new Object();
    assertFalse((Boolean) equalMethod.invoke(map, null, b));
  }

  @Test
    @Timeout(8000)
  public void testEqual_firstNonNullSecondNull() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    Object a = new Object();
    assertFalse((Boolean) equalMethod.invoke(map, a, null));
  }

  @Test
    @Timeout(8000)
  public void testEqual_sameObject() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    Object a = new Object();
    assertTrue((Boolean) equalMethod.invoke(map, a, a));
  }

  @Test
    @Timeout(8000)
  public void testEqual_equalObjects() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    String a = "test";
    String b = new String("test");
    assertTrue((Boolean) equalMethod.invoke(map, a, b));
  }

  @Test
    @Timeout(8000)
  public void testEqual_notEqualObjects() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    String a = "test1";
    String b = "test2";
    assertFalse((Boolean) equalMethod.invoke(map, a, b));
  }
}