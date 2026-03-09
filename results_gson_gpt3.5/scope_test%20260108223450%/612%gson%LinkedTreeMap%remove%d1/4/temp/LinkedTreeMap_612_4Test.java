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

  LinkedTreeMap<String, String> map;

  @BeforeEach
  void setUp() {
    map = new LinkedTreeMap<>();
  }

  @Test
    @Timeout(8000)
  void remove_existingKey_returnsValueAndRemovesNode() {
    map.put("key1", "value1");
    assertEquals("value1", map.remove("key1"));
    assertNull(map.get("key1"));
    assertEquals(0, map.size());
  }

  @Test
    @Timeout(8000)
  void remove_nonExistingKey_returnsNull() {
    assertNull(map.remove("nonexistent"));
  }

  @Test
    @Timeout(8000)
  void remove_nullKey_whenAllowed_removesEntry() {
    LinkedTreeMap<String, String> mapAllowNull = new LinkedTreeMap<>(null, true);
    mapAllowNull.put(null, "nullValue");
    assertEquals("nullValue", mapAllowNull.remove(null));
    assertNull(mapAllowNull.get(null));
  }

  @Test
    @Timeout(8000)
  void remove_nullKey_whenNotAllowed_returnsNull() {
    assertNull(map.remove(null));
  }

  @Test
    @Timeout(8000)
  void remove_internalByKey_privateMethod_behavior() throws Exception {
    map.put("key1", "value1");
    Method removeInternalByKey = LinkedTreeMap.class.getDeclaredMethod("removeInternalByKey", Object.class);
    removeInternalByKey.setAccessible(true);
    Object node = removeInternalByKey.invoke(map, "key1");
    assertNotNull(node);
    Object nodeNull = removeInternalByKey.invoke(map, "key1");
    assertNull(nodeNull);
  }

  @Test
    @Timeout(8000)
  void removeInternal_removesNodeAndRebalances() throws Exception {
    map.put("k1", "v1");
    map.put("k2", "v2");
    map.put("k3", "v3");

    Method findMethod = LinkedTreeMap.class.getDeclaredMethod("findByObject", Object.class);
    findMethod.setAccessible(true);
    Object node = findMethod.invoke(map, "k2");
    assertNotNull(node);

    Method removeInternal = LinkedTreeMap.class.getDeclaredMethod("removeInternal", node.getClass(), boolean.class);
    removeInternal.setAccessible(true);
    removeInternal.invoke(map, node, true);

    assertNull(map.get("k2"));
    assertEquals(2, map.size());
  }

  @Test
    @Timeout(8000)
  void remove_rebalanceAndRotateMethods_executeWithoutException() throws Exception {
    map.put("k1", "v1");
    map.put("k2", "v2");
    map.put("k3", "v3");

    Method findMethod = LinkedTreeMap.class.getDeclaredMethod("findByObject", Object.class);
    findMethod.setAccessible(true);
    Object node = findMethod.invoke(map, "k1");
    assertNotNull(node);

    Method rebalance = LinkedTreeMap.class.getDeclaredMethod("rebalance", node.getClass(), boolean.class);
    rebalance.setAccessible(true);
    rebalance.invoke(map, node, false);

    Method rotateLeft = LinkedTreeMap.class.getDeclaredMethod("rotateLeft", node.getClass());
    rotateLeft.setAccessible(true);
    rotateLeft.invoke(map, node);

    Method rotateRight = LinkedTreeMap.class.getDeclaredMethod("rotateRight", node.getClass());
    rotateRight.setAccessible(true);
    rotateRight.invoke(map, node);
  }
}