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

class LinkedTreeMapFindTest {

  LinkedTreeMap<String, String> map;

  @BeforeEach
  void setUp() {
    // Use natural order comparator and allowNullValues = false for tests
    map = new LinkedTreeMap<>();
  }

  @Test
    @Timeout(8000)
  void find_existingKey_returnsNode() throws Exception {
    // Put a key to create root node
    map.put("key1", "value1");

    // Reflectively invoke find with create = false for existing key
    Method findMethod = LinkedTreeMap.class.getDeclaredMethod("find", Object.class, boolean.class);
    findMethod.setAccessible(true);
    Object node = findMethod.invoke(map, "key1", false);

    assertNotNull(node);
    Field keyField = node.getClass().getDeclaredField("key");
    keyField.setAccessible(true);
    assertEquals("key1", keyField.get(node));
  }

  @Test
    @Timeout(8000)
  void find_nonExistingKey_createFalse_returnsNull() throws Exception {
    Method findMethod = LinkedTreeMap.class.getDeclaredMethod("find", Object.class, boolean.class);
    findMethod.setAccessible(true);
    Object node = findMethod.invoke(map, "missing", false);
    assertNull(node);
  }

  @Test
    @Timeout(8000)
  void find_nonExistingKey_createTrue_createsRootNode() throws Exception {
    Method findMethod = LinkedTreeMap.class.getDeclaredMethod("find", Object.class, boolean.class);
    findMethod.setAccessible(true);

    Object node = findMethod.invoke(map, "rootKey", true);
    assertNotNull(node);

    Field rootField = LinkedTreeMap.class.getDeclaredField("root");
    rootField.setAccessible(true);
    Object root = rootField.get(map);
    assertSame(node, root);

    Field sizeField = LinkedTreeMap.class.getDeclaredField("size");
    sizeField.setAccessible(true);
    assertEquals(1, sizeField.getInt(map));
  }

  @Test
    @Timeout(8000)
  void find_nonExistingKey_createTrue_insertsLeftAndRight() throws Exception {
    // Insert root first
    map.put("m", "root");

    Method findMethod = LinkedTreeMap.class.getDeclaredMethod("find", Object.class, boolean.class);
    findMethod.setAccessible(true);

    // Insert smaller key -> left child
    Object leftNode = findMethod.invoke(map, "c", true);
    assertNotNull(leftNode);

    Field rootField = LinkedTreeMap.class.getDeclaredField("root");
    rootField.setAccessible(true);
    Object root = rootField.get(map);

    Field leftField = root.getClass().getDeclaredField("left");
    leftField.setAccessible(true);
    assertSame(leftNode, leftField.get(root));

    // Insert greater key -> right child
    Object rightNode = findMethod.invoke(map, "z", true);
    assertNotNull(rightNode);

    Field rightField = root.getClass().getDeclaredField("right");
    rightField.setAccessible(true);
    assertSame(rightNode, rightField.get(root));

    Field sizeField = LinkedTreeMap.class.getDeclaredField("size");
    sizeField.setAccessible(true);
    assertEquals(3, sizeField.getInt(map));
  }

  @Test
    @Timeout(8000)
  void find_withNonComparableKeyAndNaturalOrder_throwsClassCastException() throws Exception {
    // Use raw LinkedTreeMap to insert non-comparable key
    LinkedTreeMap<Object, String> rawMap = new LinkedTreeMap<>();

    Method findMethod = LinkedTreeMap.class.getDeclaredMethod("find", Object.class, boolean.class);
    findMethod.setAccessible(true);

    Object nonComparableKey = new Object();

    ClassCastException ex = assertThrows(ClassCastException.class, () -> {
      findMethod.invoke(rawMap, nonComparableKey, true);
    });
    assertTrue(ex.getCause() instanceof ClassCastException);
  }

  @Test
    @Timeout(8000)
  void find_withCustomComparator_usesComparator() throws Exception {
    @SuppressWarnings("unchecked")
    Comparator<String> comparator = mock(Comparator.class);
    when(comparator.compare("a", "b")).thenReturn(-1);
    when(comparator.compare("b", "a")).thenReturn(1);
    when(comparator.compare("a", "a")).thenReturn(0);

    // Create map with custom comparator and allowNullValues false
    LinkedTreeMap<String, String> customMap = new LinkedTreeMap<>(comparator, false);

    // Insert "b" as root
    Method findMethod = LinkedTreeMap.class.getDeclaredMethod("find", Object.class, boolean.class);
    findMethod.setAccessible(true);
    Object nodeB = findMethod.invoke(customMap, "b", true);
    assertNotNull(nodeB);

    // Insert "a" to left of "b"
    Object nodeA = findMethod.invoke(customMap, "a", true);
    assertNotNull(nodeA);

    // Verify comparator used at least once
    verify(comparator, atLeastOnce()).compare(any(), any());

    // Insert "c" to right of "b"
    when(comparator.compare("c", "b")).thenReturn(1);
    when(comparator.compare("b", "c")).thenReturn(-1);
    Object nodeC = findMethod.invoke(customMap, "c", true);
    assertNotNull(nodeC);
  }
}