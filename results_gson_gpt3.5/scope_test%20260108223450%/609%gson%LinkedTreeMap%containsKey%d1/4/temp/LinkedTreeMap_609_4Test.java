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
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class LinkedTreeMap_ContainsKeyTest {

  LinkedTreeMap<String, String> map;

  @BeforeEach
  void setUp() {
    // Create instance with default constructor (assumes K is Comparable)
    map = new LinkedTreeMap<>();
  }

  @Test
    @Timeout(8000)
  void containsKey_whenKeyIsNullAndMapEmpty_returnsFalse() {
    assertFalse(map.containsKey(null));
  }

  @Test
    @Timeout(8000)
  void containsKey_whenKeyNotPresent_returnsFalse() {
    map.put("key1", "value1");
    assertFalse(map.containsKey("key2"));
  }

  @Test
    @Timeout(8000)
  void containsKey_whenKeyPresent_returnsTrue() {
    map.put("key1", "value1");
    assertTrue(map.containsKey("key1"));
  }

  @Test
    @Timeout(8000)
  void containsKey_withNullKeyAllowed_returnsTrue() {
    // Create map allowing null values (and keys)
    LinkedTreeMap<String, String> mapAllowNull = new LinkedTreeMap<>(null, true);
    mapAllowNull.put(null, "nullValue");
    assertTrue(mapAllowNull.containsKey(null));
  }

  @Test
    @Timeout(8000)
  void containsKey_reflectFindByObjectReturnsNull_returnsFalse() throws Exception {
    // Spy on map to mock private findByObject method to return null
    LinkedTreeMap<String, String> spyMap = Mockito.spy(map);

    Method findByObject = LinkedTreeMap.class.getDeclaredMethod("findByObject", Object.class);
    findByObject.setAccessible(true);

    // Use Mockito to mock findByObject to return null for any input
    doReturn(null).when(spyMap).findByObject(any());

    assertFalse(spyMap.containsKey("anyKey"));
  }

  @Test
    @Timeout(8000)
  void containsKey_reflectFindByObjectReturnsNode_returnsTrue() throws Exception {
    map.put("key1", "value1");

    Method findByObject = LinkedTreeMap.class.getDeclaredMethod("findByObject", Object.class);
    findByObject.setAccessible(true);

    Object node = findByObject.invoke(map, "key1");
    assertNotNull(node);

    assertTrue(map.containsKey("key1"));
  }

  @Test
    @Timeout(8000)
  void containsKey_withDifferentKeyTypes_returnsFalse() {
    LinkedTreeMap<Object, String> objMap = new LinkedTreeMap<>();
    objMap.put("stringKey", "value");
    assertFalse(objMap.containsKey(123)); // Integer key, different type
  }

}