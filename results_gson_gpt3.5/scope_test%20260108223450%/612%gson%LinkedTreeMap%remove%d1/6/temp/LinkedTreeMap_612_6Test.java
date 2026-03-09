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

class LinkedTreeMapRemoveTest {

  private LinkedTreeMap<String, String> map;

  @BeforeEach
  void setUp() {
    // Use default constructor which assumes natural ordering and allowNullValues false
    map = new LinkedTreeMap<>();
  }

  @Test
    @Timeout(8000)
  void remove_existingKey_returnsValueAndRemovesEntry() {
    map.put("key1", "value1");
    map.put("key2", "value2");

    String removedValue = map.remove("key1");

    assertEquals("value1", removedValue);
    assertFalse(map.containsKey("key1"));
    assertEquals(1, map.size());
  }

  @Test
    @Timeout(8000)
  void remove_nonExistingKey_returnsNull() {
    map.put("key1", "value1");

    String removedValue = map.remove("nonExistingKey");

    assertNull(removedValue);
    assertEquals(1, map.size());
  }

  @Test
    @Timeout(8000)
  void remove_nullKey_whenAllowed_removesEntry() {
    LinkedTreeMap<String, String> nullAllowedMap = new LinkedTreeMap<>(null, true);
    nullAllowedMap.put(null, "nullValue");
    assertTrue(nullAllowedMap.containsKey(null));

    String removed = nullAllowedMap.remove(null);

    assertEquals("nullValue", removed);
    assertFalse(nullAllowedMap.containsKey(null));
    assertEquals(0, nullAllowedMap.size());
  }

  @Test
    @Timeout(8000)
  void remove_nullKey_whenNotAllowed_returnsNull() {
    map.put("key", "value");

    String removed = map.remove(null);

    assertNull(removed);
    assertEquals(1, map.size());
  }

  @Test
    @Timeout(8000)
  void remove_internalNodeIsProperlyUnlinked_andRebalanced() {
    // Insert multiple entries to create a tree with multiple nodes
    map.put("c", "3");
    map.put("a", "1");
    map.put("b", "2");
    map.put("d", "4");
    map.put("e", "5");

    // Remove "c" which should trigger internal removal logic and rebalancing
    String removed = map.remove("c");

    assertEquals("3", removed);
    assertFalse(map.containsKey("c"));
    assertEquals(4, map.size());

    // Remove "a" to check further rebalancing
    removed = map.remove("a");
    assertEquals("1", removed);
    assertFalse(map.containsKey("a"));
    assertEquals(3, map.size());
  }

  @Test
    @Timeout(8000)
  void remove_callsRemoveInternalByKey() {
    // Spy on LinkedTreeMap to verify removeInternalByKey is called
    LinkedTreeMap<String, String> spyMap = spy(new LinkedTreeMap<>());
    spyMap.put("key", "value");

    spyMap.remove("key");

    verify(spyMap).removeInternalByKey("key");
  }

  @Test
    @Timeout(8000)
  void removeInternalByKey_reflectiveInvocation_removesCorrectly() throws Exception {
    map.put("key", "value");

    Method removeInternalByKey = LinkedTreeMap.class.getDeclaredMethod("removeInternalByKey", Object.class);
    removeInternalByKey.setAccessible(true);

    Object node = removeInternalByKey.invoke(map, "key");
    assertNotNull(node);

    // Remove again should return null since key no longer exists
    Object node2 = removeInternalByKey.invoke(map, "key");
    assertNull(node2);
  }

  @Test
    @Timeout(8000)
  void removeInternalNode_removesNodeAndMaintainsHeaderLinks() throws Exception {
    map.put("a", "1");
    map.put("b", "2");

    Method findMethod = LinkedTreeMap.class.getDeclaredMethod("findByObject", Object.class);
    findMethod.setAccessible(true);
    Object nodeA = findMethod.invoke(map, "a");
    assertNotNull(nodeA);

    Method removeInternalMethod = LinkedTreeMap.class.getDeclaredMethod("removeInternal", nodeA.getClass(), boolean.class);
    removeInternalMethod.setAccessible(true);
    removeInternalMethod.invoke(map, nodeA, true);

    assertFalse(map.containsKey("a"));
    assertEquals(1, map.size());
  }
}