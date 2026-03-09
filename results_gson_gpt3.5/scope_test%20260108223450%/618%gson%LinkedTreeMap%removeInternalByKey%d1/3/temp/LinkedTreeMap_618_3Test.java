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

import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LinkedTreeMap_removeInternalByKey_Test {

  LinkedTreeMap<String, String> map;

  @BeforeEach
  void setUp() {
    // Use default constructor which assumes natural ordering and allowNullValues false
    map = new LinkedTreeMap<>(null, false);
  }

  @Test
    @Timeout(8000)
  void removeInternalByKey_keyExists_removesAndReturnsNode() throws Exception {
    // We will add an element, then remove it by key

    // Put a key-value pair
    map.put("key1", "value1");

    // Use reflection to access the package-private removeInternalByKey method
    Method removeInternalByKey = LinkedTreeMap.class.getDeclaredMethod("removeInternalByKey", Object.class);
    removeInternalByKey.setAccessible(true);

    // Invoke removeInternalByKey with existing key
    Object node = removeInternalByKey.invoke(map, (Object) "key1");

    assertNotNull(node, "Node should be returned when key exists");

    // After removal, size should be 0
    assertEquals(0, map.size());

    // The map should no longer contain the key
    assertFalse(map.containsKey("key1"));
  }

  @Test
    @Timeout(8000)
  void removeInternalByKey_keyDoesNotExist_returnsNull() throws Exception {
    // Use reflection to access the package-private removeInternalByKey method
    Method removeInternalByKey = LinkedTreeMap.class.getDeclaredMethod("removeInternalByKey", Object.class);
    removeInternalByKey.setAccessible(true);

    // Invoke removeInternalByKey with a key that does not exist
    Object node = removeInternalByKey.invoke(map, (Object) "nonexistent");

    assertNull(node, "Node should be null when key does not exist");

    // Size remains zero
    assertEquals(0, map.size());
  }

  @Test
    @Timeout(8000)
  void removeInternalByKey_nullKey_behaviorDependsOnMap() throws Exception {
    // Create a LinkedTreeMap that allows null values (and keys)
    LinkedTreeMap<String, String> mapAllowNull = new LinkedTreeMap<>(null, true);
    mapAllowNull.put(null, "nullValue");

    Method removeInternalByKey = LinkedTreeMap.class.getDeclaredMethod("removeInternalByKey", Object.class);
    removeInternalByKey.setAccessible(true);

    Object node = removeInternalByKey.invoke(mapAllowNull, (Object) null);

    assertNotNull(node, "Node should be returned when null key exists");
    assertEquals(0, mapAllowNull.size());
    assertFalse(mapAllowNull.containsKey(null));
  }

  @Test
    @Timeout(8000)
  void removeInternalByKey_multipleRemovals_consistentBehavior() throws Exception {
    map.put("k1", "v1");
    map.put("k2", "v2");

    Method removeInternalByKey = LinkedTreeMap.class.getDeclaredMethod("removeInternalByKey", Object.class);
    removeInternalByKey.setAccessible(true);

    // Remove first key
    Object node1 = removeInternalByKey.invoke(map, (Object) "k1");
    assertNotNull(node1);
    assertEquals(1, map.size());
    assertFalse(map.containsKey("k1"));

    // Remove second key
    Object node2 = removeInternalByKey.invoke(map, (Object) "k2");
    assertNotNull(node2);
    assertEquals(0, map.size());
    assertFalse(map.containsKey("k2"));

    // Remove non-existing key
    Object node3 = removeInternalByKey.invoke(map, (Object) "k3");
    assertNull(node3);
  }
}