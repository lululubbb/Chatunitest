package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.io.InvalidObjectException;
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

import java.io.ObjectInputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Comparator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LinkedTreeMap_605_4Test {

  LinkedTreeMap<String, String> mapAllowNull;
  LinkedTreeMap<String, String> mapNoNull;
  Comparator<String> mockComparator;

  @BeforeEach
  void setUp() {
    mapAllowNull = new LinkedTreeMap<>(true);
    mapNoNull = new LinkedTreeMap<>(false);
    mockComparator = mock(Comparator.class);
  }

  @Test
    @Timeout(8000)
  void testConstructor_noArgs() throws Exception {
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
    assertEquals(0, map.size());
    // default comparator is NATURAL_ORDER, which is private static final
    Field compField = LinkedTreeMap.class.getDeclaredField("comparator");
    compField.setAccessible(true);
    Comparator<?> comp = (Comparator<?>) compField.get(map);
    assertNotNull(comp);
  }

  @Test
    @Timeout(8000)
  void testConstructor_withComparatorAndAllowNull() {
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>(mockComparator, true);
    assertEquals(0, map.size());
    Field compField = null;
    try {
      compField = LinkedTreeMap.class.getDeclaredField("comparator");
      compField.setAccessible(true);
      Comparator<?> comp = (Comparator<?>) compField.get(map);
      assertEquals(mockComparator, comp);
    } catch (Exception e) {
      fail(e);
    }
  }

  @Test
    @Timeout(8000)
  void testConstructor_withComparatorAndDisallowNull() {
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>(mockComparator, false);
    assertEquals(0, map.size());
    try {
      Field allowNullField = LinkedTreeMap.class.getDeclaredField("allowNullValues");
      allowNullField.setAccessible(true);
      assertFalse(allowNullField.getBoolean(map));
    } catch (Exception e) {
      fail(e);
    }
  }

  @Test
    @Timeout(8000)
  void testConstructor_booleanAllowNull() throws Exception {
    Constructor<LinkedTreeMap> constructor = LinkedTreeMap.class.getDeclaredConstructor(boolean.class);
    constructor.setAccessible(true);
    LinkedTreeMap<String, String> mapTrue = constructor.newInstance(true);
    LinkedTreeMap<String, String> mapFalse = constructor.newInstance(false);

    Field allowNullField = LinkedTreeMap.class.getDeclaredField("allowNullValues");
    allowNullField.setAccessible(true);
    assertTrue(allowNullField.getBoolean(mapTrue));
    assertFalse(allowNullField.getBoolean(mapFalse));
  }

  @Test
    @Timeout(8000)
  void testNaturalOrderComparator() throws Exception {
    Field natOrderField = LinkedTreeMap.class.getDeclaredField("NATURAL_ORDER");
    natOrderField.setAccessible(true);
    Comparator<Comparable> comp = (Comparator<Comparable>) natOrderField.get(null);
    assertEquals(0, comp.compare("a", "a"));
    assertTrue(comp.compare("a", "b") < 0);
    assertTrue(comp.compare("b", "a") > 0);
  }
}