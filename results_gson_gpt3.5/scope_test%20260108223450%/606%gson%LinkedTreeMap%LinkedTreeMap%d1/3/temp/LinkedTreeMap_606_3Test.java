package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LinkedTreeMap_606_3Test {

  private Comparator<String> comparator;

  @BeforeEach
  void setUp() {
    comparator = String::compareTo;
  }

  @Test
    @Timeout(8000)
  void testConstructor_withComparatorAndAllowNullValues() throws Exception {
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>(comparator, true);
    assertNotNull(map);
    assertEquals(0, map.size());
  }

  @Test
    @Timeout(8000)
  void testConstructor_withNullComparatorAndAllowNullValues() throws Exception {
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>(null, false);
    assertNotNull(map);
    assertEquals(0, map.size());
  }

  @Test
    @Timeout(8000)
  void testConstructor_noArgs_assumesComparable() throws Exception {
    Constructor<LinkedTreeMap> ctor = LinkedTreeMap.class.getDeclaredConstructor();
    ctor.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedTreeMap<String, String> map = ctor.newInstance();
    assertNotNull(map);
    assertEquals(0, map.size());
  }

  @Test
    @Timeout(8000)
  void testConstructor_allowNullValuesOnly() throws Exception {
    Constructor<LinkedTreeMap> ctor = LinkedTreeMap.class.getDeclaredConstructor(boolean.class);
    ctor.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedTreeMap<String, String> map = ctor.newInstance(true);
    assertNotNull(map);
    assertEquals(0, map.size());
  }
}