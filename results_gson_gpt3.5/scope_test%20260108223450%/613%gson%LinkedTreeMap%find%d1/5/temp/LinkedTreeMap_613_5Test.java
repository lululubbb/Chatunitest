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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Comparator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LinkedTreeMap_613_5Test {

  LinkedTreeMap<String, String> map;

  @BeforeEach
  void setUp() {
    map = new LinkedTreeMap<>();
  }

  @Test
    @Timeout(8000)
  void find_existingKey_returnsNode() throws Exception {
    // Put an entry to create root node
    map.put("key1", "value1");

    // Use reflection to invoke find with create = false
    Method findMethod = LinkedTreeMap.class.getDeclaredMethod("find", Object.class, boolean.class);
    findMethod.setAccessible(true);

    Object node = findMethod.invoke(map, "key1", false);
    assertNotNull(node);

    // Check that the node's key is "key1"
    Field keyField = node.getClass().getDeclaredField("key");
    keyField.setAccessible(true);
    Object keyValue = keyField.get(node);
    assertEquals("key1", keyValue);
  }

  @Test
    @Timeout(8000)
  void find_nonExistingKey_createFalse_returnsNull() throws Exception {
    Method findMethod = LinkedTreeMap.class.getDeclaredMethod("find", Object.class, boolean.class);
    findMethod.setAccessible(true);

    Object node = findMethod.invoke(map, "nonexistent", false);
    assertNull(node);
  }

  @Test
    @Timeout(8000)
  void find_nonExistingKey_createTrue_createsNode() throws Exception {
    Method findMethod = LinkedTreeMap.class.getDeclaredMethod("find", Object.class, boolean.class);
    findMethod.setAccessible(true);

    Object node = findMethod.invoke(map, "newKey", true);
    assertNotNull(node);

    // Confirm size incremented
    assertEquals(1, map.size());

    // Confirm root key is newKey
    Field rootField = LinkedTreeMap.class.getDeclaredField("root");
    rootField.setAccessible(true);
    Object rootNode = rootField.get(map);
    Field keyField = rootNode.getClass().getDeclaredField("key");
    keyField.setAccessible(true);
    Object rootKey = keyField.get(rootNode);
    assertEquals("newKey", rootKey);
  }

  @Test
    @Timeout(8000)
  void find_createTrue_nonComparableKeyWithoutNaturalOrder_throwsClassCastException() throws Exception {
    // Create LinkedTreeMap with NATURAL_ORDER comparator and allowNullValues false
    LinkedTreeMap<Object, Object> customMap = new LinkedTreeMap<>(null, false);

    Method findMethod = LinkedTreeMap.class.getDeclaredMethod("find", Object.class, boolean.class);
    findMethod.setAccessible(true);

    Object nonComparableKey = new Object();

    Throwable thrown = assertThrows(Throwable.class, () -> {
      try {
        findMethod.invoke(customMap, nonComparableKey, true);
      } catch (java.lang.reflect.InvocationTargetException e) {
        throw e.getCause();
      }
    });

    assertTrue(thrown instanceof ClassCastException);
    assertTrue(thrown.getMessage().contains("is not Comparable"));
  }

  @Test
    @Timeout(8000)
  void find_createTrue_insertsLeftAndRightChildAndRebalances() throws Exception {
    // Insert root key
    map.put("m", "root");

    // Insert smaller key, should go to left
    Method findMethod = LinkedTreeMap.class.getDeclaredMethod("find", Object.class, boolean.class);
    findMethod.setAccessible(true);
    Object leftNode = findMethod.invoke(map, "c", true);
    assertNotNull(leftNode);

    // Insert larger key, should go to right
    Object rightNode = findMethod.invoke(map, "z", true);
    assertNotNull(rightNode);

    // Verify root's left and right children keys
    Field rootField = LinkedTreeMap.class.getDeclaredField("root");
    rootField.setAccessible(true);
    Object rootNode = rootField.get(map);

    Field leftField = rootNode.getClass().getDeclaredField("left");
    Field rightField = rootNode.getClass().getDeclaredField("right");
    leftField.setAccessible(true);
    rightField.setAccessible(true);

    Object leftChild = leftField.get(rootNode);
    Object rightChild = rightField.get(rootNode);

    Field keyField = rootNode.getClass().getDeclaredField("key");
    keyField.setAccessible(true);

    Object leftKey = keyField.get(leftChild);
    Object rightKey = keyField.get(rightChild);

    assertEquals("c", leftKey);
    assertEquals("z", rightKey);

    // Size should be 3
    assertEquals(3, map.size());
  }

  @Test
    @Timeout(8000)
  void find_withCustomComparator_usesComparatorCompare() throws Exception {
    Comparator<String> comp = mock(Comparator.class);
    when(comp.compare(any(), any())).thenAnswer(invocation -> {
      String a = invocation.getArgument(0);
      String b = invocation.getArgument(1);
      return a.compareTo(b);
    });

    LinkedTreeMap<String, String> customMap = new LinkedTreeMap<>(comp, false);

    // Insert key "b" with put to create root
    customMap.put("b", "val");

    Method findMethod = LinkedTreeMap.class.getDeclaredMethod("find", Object.class, boolean.class);
    findMethod.setAccessible(true);

    // Invoke find for existing key
    Object node = findMethod.invoke(customMap, "b", false);
    assertNotNull(node);

    // Verify comparator.compare called at least once
    verify(comp, atLeastOnce()).compare(any(), any());
  }
}