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

class LinkedTreeMapContainsKeyTest {

  private LinkedTreeMap<String, String> map;

  @BeforeEach
  public void setUp() {
    map = new LinkedTreeMap<>(null, false);
  }

  @Test
    @Timeout(8000)
  public void testContainsKey_existingKey() {
    map.put("key1", "value1");
    assertTrue(map.containsKey("key1"));
  }

  @Test
    @Timeout(8000)
  public void testContainsKey_nonExistingKey() {
    map.put("key1", "value1");
    assertFalse(map.containsKey("key2"));
  }

  @Test
    @Timeout(8000)
  public void testContainsKey_nullKeyAllowed() {
    LinkedTreeMap<String, String> mapAllowNull = new LinkedTreeMap<>(null, true);
    mapAllowNull.put(null, "nullValue");
    assertTrue(mapAllowNull.containsKey(null));
  }

  @Test
    @Timeout(8000)
  public void testContainsKey_nullKeyNotAllowed() {
    // default constructor disallows null keys (allowNullValues = false)
    assertFalse(map.containsKey(null));
  }

  @Test
    @Timeout(8000)
  public void testContainsKey_privateFindByObject_returnsNode() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    map.put("key1", "value1");
    Method findByObject = LinkedTreeMap.class.getDeclaredMethod("findByObject", Object.class);
    findByObject.setAccessible(true);
    Object node = findByObject.invoke(map, "key1");
    assertNotNull(node);
  }

  @Test
    @Timeout(8000)
  public void testContainsKey_privateFindByObject_returnsNull() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method findByObject = LinkedTreeMap.class.getDeclaredMethod("findByObject", Object.class);
    findByObject.setAccessible(true);
    Object node = findByObject.invoke(map, "nonexistent");
    assertNull(node);
  }
}