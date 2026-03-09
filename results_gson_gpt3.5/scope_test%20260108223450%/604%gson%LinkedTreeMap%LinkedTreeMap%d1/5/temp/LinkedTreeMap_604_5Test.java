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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Test;

public class LinkedTreeMap_604_5Test {

  @Test
    @Timeout(8000)
  public void testConstructor_default() throws Exception {
    LinkedTreeMap<?, ?> map = new LinkedTreeMap<>();
    assertNotNull(map);
    assertEquals(0, map.size());
  }

  @Test
    @Timeout(8000)
  public void testConstructor_comparatorAllowNullValues() throws Exception {
    Comparator<String> comp = String::compareTo;
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>(comp, true);
    assertNotNull(map);
    assertEquals(0, map.size());
  }

  @Test
    @Timeout(8000)
  public void testConstructor_comparatorAllowNullValues_false() throws Exception {
    Comparator<String> comp = String::compareTo;
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>(comp, false);
    assertNotNull(map);
    assertEquals(0, map.size());
  }

  @Test
    @Timeout(8000)
  public void testConstructor_reflection_default() throws Exception {
    Constructor<LinkedTreeMap> constructor = LinkedTreeMap.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    LinkedTreeMap<?, ?> map = constructor.newInstance();
    assertNotNull(map);
    assertEquals(0, map.size());
  }

  @Test
    @Timeout(8000)
  public void testConstructor_reflection_comparatorAllowNullValues() throws Exception {
    Constructor<LinkedTreeMap> constructor = LinkedTreeMap.class.getDeclaredConstructor(Comparator.class, boolean.class);
    constructor.setAccessible(true);
    Comparator<String> comp = String::compareTo;
    LinkedTreeMap<String, String> map = constructor.newInstance(comp, true);
    assertNotNull(map);
    assertEquals(0, map.size());
  }
}