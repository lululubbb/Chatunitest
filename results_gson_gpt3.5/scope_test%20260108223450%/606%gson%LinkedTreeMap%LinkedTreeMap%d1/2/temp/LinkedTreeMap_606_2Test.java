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

public class LinkedTreeMap_606_2Test {

  private Comparator<String> comparator;
  private LinkedTreeMap<String, String> mapAllowNull;
  private LinkedTreeMap<String, String> mapDisallowNull;

  @BeforeEach
  public void setUp() {
    comparator = String::compareTo;
    mapAllowNull = new LinkedTreeMap<>(comparator, true);
    mapDisallowNull = new LinkedTreeMap<>(null, false); // uses NATURAL_ORDER internally
  }

  @Test
    @Timeout(8000)
  public void testConstructor_withComparator_allowNullValuesTrue() {
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>(comparator, true);
    assertNotNull(map);
    assertEquals(0, map.size());
  }

  @Test
    @Timeout(8000)
  public void testConstructor_withNullComparator_allowNullValuesFalse_usesNaturalOrder() {
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>(null, false);
    assertNotNull(map);
    assertEquals(0, map.size());
  }

  @Test
    @Timeout(8000)
  public void testConstructor_reflection_defaultConstructorAssumesAllowNullFalse() throws Exception {
    Constructor<LinkedTreeMap> ctor = LinkedTreeMap.class.getDeclaredConstructor();
    ctor.setAccessible(true);
    LinkedTreeMap<?, ?> map = (LinkedTreeMap<?, ?>) ctor.newInstance();
    assertNotNull(map);
    assertEquals(0, map.size());
  }

  @Test
    @Timeout(8000)
  public void testConstructor_reflection_constructorWithAllowNull() throws Exception {
    Constructor<LinkedTreeMap> ctor = LinkedTreeMap.class.getDeclaredConstructor(boolean.class);
    ctor.setAccessible(true);
    LinkedTreeMap<?, ?> mapTrue = ctor.newInstance(true);
    LinkedTreeMap<?, ?> mapFalse = ctor.newInstance(false);
    assertNotNull(mapTrue);
    assertNotNull(mapFalse);
    assertEquals(0, mapTrue.size());
    assertEquals(0, mapFalse.size());
  }

  @Test
    @Timeout(8000)
  public void testNaturalOrderComparator_compare() throws Exception {
    // Access private static NATURAL_ORDER comparator via reflection
    var field = LinkedTreeMap.class.getDeclaredField("NATURAL_ORDER");
    field.setAccessible(true);
    Comparator<Comparable> naturalOrder = (Comparator<Comparable>) field.get(null);
    assertEquals(0, naturalOrder.compare("a", "a"));
    assertTrue(naturalOrder.compare("a", "b") < 0);
    assertTrue(naturalOrder.compare("b", "a") > 0);
  }
}