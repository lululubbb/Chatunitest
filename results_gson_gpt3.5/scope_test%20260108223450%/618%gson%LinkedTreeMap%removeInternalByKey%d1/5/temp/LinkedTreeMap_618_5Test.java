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

import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.internal.LinkedTreeMap.Node;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class LinkedTreeMapRemoveInternalByKeyTest {

  private LinkedTreeMap<String, String> map;

  @BeforeEach
  public void setUp() {
    // Using default constructor which assumes String is Comparable
    map = new LinkedTreeMap<>();
  }

  @Test
    @Timeout(8000)
  public void testRemoveInternalByKey_NodeExists() throws Exception {
    // Put a key-value pair so that node exists
    map.put("key1", "value1");

    // Use reflection to call private findByObject to get the node for "key1"
    Method findByObjectMethod = LinkedTreeMap.class.getDeclaredMethod("findByObject", Object.class);
    findByObjectMethod.setAccessible(true);
    Node<String, String> node = (Node<String, String>) findByObjectMethod.invoke(map, "key1");
    assertNotNull(node);

    // Spy on map to verify removeInternal call
    LinkedTreeMap<String, String> spyMap = spy(map);

    // Call removeInternalByKey with existing key
    Node<String, String> removedNode = spyMap.removeInternalByKey("key1");

    // Verify removeInternal was called with the found node and unlink=true
    verify(spyMap).removeInternal(node, true);

    // The returned node should be the same as found node
    assertSame(node, removedNode);

    // After removal, node should no longer be found
    Node<String, String> nodeAfterRemoval = (Node<String, String>) findByObjectMethod.invoke(spyMap, "key1");
    assertNull(nodeAfterRemoval);
  }

  @Test
    @Timeout(8000)
  public void testRemoveInternalByKey_NodeDoesNotExist() {
    // Key that does not exist in map
    String missingKey = "missingKey";

    // Call removeInternalByKey with non-existing key
    Node<String, String> result = map.removeInternalByKey(missingKey);

    // Should return null
    assertNull(result);
  }
}