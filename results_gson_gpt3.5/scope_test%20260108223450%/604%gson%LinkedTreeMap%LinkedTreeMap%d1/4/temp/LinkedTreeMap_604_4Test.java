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

class LinkedTreeMap_604_4Test {

  @Test
    @Timeout(8000)
  void testNoArgConstructorCreatesEmptyMap() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
    Constructor<LinkedTreeMap> constructor = LinkedTreeMap.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    LinkedTreeMap<String, String> map = constructor.newInstance();

    assertNotNull(map);
    assertEquals(0, map.size());
    assertFalse(map.containsKey("anyKey"));
    assertNull(map.get("anyKey"));
  }

  @Test
    @Timeout(8000)
  void testConstructorWithComparatorAndAllowNullValues() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
    Constructor<LinkedTreeMap> constructor = LinkedTreeMap.class.getDeclaredConstructor(java.util.Comparator.class, boolean.class);
    constructor.setAccessible(true);

    // Use natural order comparator explicitly and allowNullValues = false
    LinkedTreeMap<String, String> map = constructor.newInstance(java.util.Comparator.naturalOrder(), false);
    assertNotNull(map);
    assertEquals(0, map.size());

    // Put and get a value to verify basic functionality
    map.put("key", "value");
    assertEquals("value", map.get("key"));
  }
}