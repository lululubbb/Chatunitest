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

import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.internal.LinkedTreeMap.Node;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class LinkedTreeMapRemoveInternalByKeyTest {

  private LinkedTreeMap<String, String> map;

  @BeforeEach
  void setUp() {
    map = new LinkedTreeMap<>();
  }

  @Test
    @Timeout(8000)
  void removeInternalByKey_keyExists_removesNodeAndReturnsNode() throws Exception {
    // Prepare: put a key-value pair so that node exists
    map.put("key1", "value1");

    // Use reflection to access private findByObject method to verify node presence
    Method findByObject = LinkedTreeMap.class.getDeclaredMethod("findByObject", Object.class);
    findByObject.setAccessible(true);
    @SuppressWarnings("unchecked")
    Node<String, String> nodeBefore = (Node<String, String>) findByObject.invoke(map, "key1");
    assertNotNull(nodeBefore);

    // Call removeInternalByKey
    Method removeInternalByKey = LinkedTreeMap.class.getDeclaredMethod("removeInternalByKey", Object.class);
    removeInternalByKey.setAccessible(true);
    @SuppressWarnings("unchecked")
    Node<String, String> removedNode = (Node<String, String>) removeInternalByKey.invoke(map, "key1");

    // Verify returned node is not null and matches the found node
    assertNotNull(removedNode);
    assertEquals(nodeBefore, removedNode);

    // Verify the key is removed from the map (size decreases)
    assertEquals(0, map.size());
    assertNull(map.get("key1"));

    // Verify that findByObject returns null now
    @SuppressWarnings("unchecked")
    Node<String, String> nodeAfter = (Node<String, String>) findByObject.invoke(map, "key1");
    assertNull(nodeAfter);
  }

  @Test
    @Timeout(8000)
  void removeInternalByKey_keyDoesNotExist_returnsNull() throws Exception {
    Method removeInternalByKey = LinkedTreeMap.class.getDeclaredMethod("removeInternalByKey", Object.class);
    removeInternalByKey.setAccessible(true);
    @SuppressWarnings("unchecked")
    Node<String, String> result = (Node<String, String>) removeInternalByKey.invoke(map, "nonexistent");
    assertNull(result);
  }
}