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
import java.lang.reflect.Method;
import java.util.Comparator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LinkedTreeMap_609_3Test {

  LinkedTreeMap<String, String> map;

  @BeforeEach
  void setUp() {
    map = new LinkedTreeMap<>();
  }

  @Test
    @Timeout(8000)
  void containsKey_whenKeyPresent_returnsTrue() throws Exception {
    // Put a key to ensure it is present
    map.put("key1", "value1");

    // containsKey should return true for existing key
    assertTrue(map.containsKey("key1"));
  }

  @Test
    @Timeout(8000)
  void containsKey_whenKeyAbsent_returnsFalse() throws Exception {
    // containsKey should return false for non-existing key
    assertFalse(map.containsKey("absentKey"));
  }

  @Test
    @Timeout(8000)
  void containsKey_withNullKey_whenNullAllowed_returnsTrue() throws Exception {
    // Create map allowing null keys (and values)
    // Use reflection to create instance with comparator = NATURAL_ORDER and allowNullValues = true
    // NATURAL_ORDER is a private static final field, get it by reflection
    java.lang.reflect.Field naturalOrderField = LinkedTreeMap.class.getDeclaredField("NATURAL_ORDER");
    naturalOrderField.setAccessible(true);
    @SuppressWarnings("unchecked")
    Comparator<? super String> naturalOrder = (Comparator<? super String>) naturalOrderField.get(null);

    Constructor<LinkedTreeMap> constructor = LinkedTreeMap.class.getDeclaredConstructor(java.util.Comparator.class, boolean.class);
    constructor.setAccessible(true);
    LinkedTreeMap<String, String> nullAllowedMap = constructor.newInstance(naturalOrder, true);

    // Put a null key with value - use reflection to call put to avoid NPE in put()
    Method putMethod = LinkedTreeMap.class.getDeclaredMethod("put", Object.class, Object.class);
    putMethod.setAccessible(true);
    putMethod.invoke(nullAllowedMap, null, "nullValue");

    assertTrue(nullAllowedMap.containsKey(null));
  }

  @Test
    @Timeout(8000)
  void containsKey_withNullKey_whenNullNotAllowed_returnsFalse() throws Exception {
    // Default map does not allow null keys, containsKey should return false
    assertFalse(map.containsKey(null));
  }

  @Test
    @Timeout(8000)
  void containsKey_invokesFindByObject_correctly() throws Exception {
    // Use reflection to get private findByObject method
    Method findByObject = LinkedTreeMap.class.getDeclaredMethod("findByObject", Object.class);
    findByObject.setAccessible(true);

    // Put a key-value pair
    map.put("key2", "value2");

    // Invoke findByObject directly and check it returns non-null for existing key
    Object node = findByObject.invoke(map, "key2");
    assertNotNull(node);

    // Invoke findByObject directly and check it returns null for absent key
    Object absentNode = findByObject.invoke(map, "absentKey");
    assertNull(absentNode);

    // containsKey should be consistent with findByObject
    assertEquals(node != null, map.containsKey("key2"));
    assertEquals(absentNode != null, map.containsKey("absentKey"));
  }

}