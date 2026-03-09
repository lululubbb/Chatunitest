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

import java.util.Comparator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LinkedTreeMap_606_5Test {

  LinkedTreeMap<String, String> mapNatural;
  LinkedTreeMap<String, String> mapCustom;
  Comparator<String> mockComparator;

  @BeforeEach
  void setUp() {
    mockComparator = mock(Comparator.class);
    // natural order comparator for String
    mapNatural = new LinkedTreeMap<>(null, true);
    mapCustom = new LinkedTreeMap<>(mockComparator, false);
  }

  @Test
    @Timeout(8000)
  void testConstructor_withNullComparator_allowsNullValues() {
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>(null, true);
    assertNotNull(map);
    // Using reflection to check allowNullValues and comparator
    try {
      var allowNullValuesField = LinkedTreeMap.class.getDeclaredField("allowNullValues");
      allowNullValuesField.setAccessible(true);
      assertTrue((Boolean) allowNullValuesField.get(map));

      var comparatorField = LinkedTreeMap.class.getDeclaredField("comparator");
      comparatorField.setAccessible(true);
      Object comparator = comparatorField.get(map);
      assertNotNull(comparator);
    } catch (Exception e) {
      fail("Reflection failed: " + e.getMessage());
    }
  }

  @Test
    @Timeout(8000)
  void testConstructor_withCustomComparator_disallowsNullValues() {
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>(mockComparator, false);
    assertNotNull(map);
    try {
      var allowNullValuesField = LinkedTreeMap.class.getDeclaredField("allowNullValues");
      allowNullValuesField.setAccessible(true);
      assertFalse((Boolean) allowNullValuesField.get(map));

      var comparatorField = LinkedTreeMap.class.getDeclaredField("comparator");
      comparatorField.setAccessible(true);
      assertSame(mockComparator, comparatorField.get(map));
    } catch (Exception e) {
      fail("Reflection failed: " + e.getMessage());
    }
  }

  @Test
    @Timeout(8000)
  void testConstructor_reflection_NATURAL_ORDER_comparatorCompare() throws Exception {
    var natOrderField = LinkedTreeMap.class.getDeclaredField("NATURAL_ORDER");
    natOrderField.setAccessible(true);
    @SuppressWarnings("unchecked")
    Comparator<Comparable<?>> natOrder = (Comparator<Comparable<?>>) natOrderField.get(null);
    Integer a = 1;
    Integer b = 2;
    assertTrue(natOrder.compare(a, b) < 0);
    assertTrue(natOrder.compare(b, a) > 0);
    assertEquals(0, natOrder.compare(a, a));
  }

  @Test
    @Timeout(8000)
  void testConstructor_headerNodeInitialized() throws Exception {
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>(null, true);
    var headerField = LinkedTreeMap.class.getDeclaredField("header");
    headerField.setAccessible(true);
    Object header = headerField.get(map);
    assertNotNull(header);
    // header should be a Node with allowNullValues true
    var allowNullValuesField = header.getClass().getDeclaredField("allowNullValues");
    allowNullValuesField.setAccessible(true);
    // Fix: The field "allowNullValues" does not exist in Node, so check the LinkedTreeMap's allowNullValues instead
    // Instead, check the LinkedTreeMap's allowNullValues field to confirm the flag
    var allowNullValuesFieldMap = LinkedTreeMap.class.getDeclaredField("allowNullValues");
    allowNullValuesFieldMap.setAccessible(true);
    assertTrue((Boolean) allowNullValuesFieldMap.get(map));
  }
}