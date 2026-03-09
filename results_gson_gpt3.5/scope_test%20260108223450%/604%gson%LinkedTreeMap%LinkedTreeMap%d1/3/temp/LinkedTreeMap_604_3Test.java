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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Comparator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LinkedTreeMap_604_3Test {

  private LinkedTreeMap<String, String> map;

  @BeforeEach
  public void setUp() {
    map = new LinkedTreeMap<>();
  }

  @Test
    @Timeout(8000)
  public void testNoArgConstructor_createsEmptyMap() {
    assertEquals(0, map.size());
    assertNotNull(getField(map, "comparator"));
    assertTrue(getField(map, "allowNullValues") instanceof Boolean);
    assertNotNull(getField(map, "header"));
    assertNull(getField(map, "root"));
  }

  @Test
    @Timeout(8000)
  public void testConstructor_withComparatorAndAllowNullValues() throws Exception {
    Comparator<String> comp = Comparator.reverseOrder();
    Constructor<LinkedTreeMap> ctor = LinkedTreeMap.class.getDeclaredConstructor(Comparator.class, boolean.class);
    ctor.setAccessible(true);
    LinkedTreeMap<String, String> customMap = ctor.newInstance(comp, true);

    Comparator<?> comparator = (Comparator<?>) getField(customMap, "comparator");
    Boolean allowNullValues = (Boolean) getField(customMap, "allowNullValues");

    assertSame(comp, comparator);
    assertTrue(allowNullValues);
    assertEquals(0, customMap.size());
  }

  @Test
    @Timeout(8000)
  public void testNaturalOrderComparator_compare() throws Exception {
    Field naturalOrderField = LinkedTreeMap.class.getDeclaredField("NATURAL_ORDER");
    naturalOrderField.setAccessible(true);
    @SuppressWarnings("unchecked")
    Comparator<Comparable> naturalOrder = (Comparator<Comparable>) naturalOrderField.get(null);

    assertTrue(naturalOrder.compare("a", "b") < 0);
    assertEquals(0, naturalOrder.compare("abc", "abc"));
    assertTrue(naturalOrder.compare("z", "a") > 0);
  }

  @Test
    @Timeout(8000)
  public void testPrivateEqualMethod() throws Exception {
    Method equalMethod = LinkedTreeMap.class.getDeclaredMethod("equal", Object.class, Object.class);
    equalMethod.setAccessible(true);

    assertTrue((Boolean) equalMethod.invoke(map, null, null));
    assertFalse((Boolean) equalMethod.invoke(map, "a", null));
    assertFalse((Boolean) equalMethod.invoke(map, null, "b"));
    assertTrue((Boolean) equalMethod.invoke(map, "test", "test"));
    assertFalse((Boolean) equalMethod.invoke(map, "test", "other"));
  }

  // Helper to get private fields by name
  private Object getField(Object instance, String fieldName) {
    try {
      Field field = LinkedTreeMap.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      return field.get(instance);
    } catch (Exception e) {
      fail("Reflection failed for field: " + fieldName + " with exception: " + e);
      return null;
    }
  }
}