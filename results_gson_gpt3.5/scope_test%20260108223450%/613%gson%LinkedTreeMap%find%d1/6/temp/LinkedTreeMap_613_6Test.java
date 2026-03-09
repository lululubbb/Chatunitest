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

  LinkedTreeMap<Object, Object> map;

  @BeforeEach
  void setUp() {
    // Use the constructor with (Comparator<? super K>, boolean allowNullValues)
    // We will provide natural order comparator and allowNullValues = true
    map = new LinkedTreeMap<>(null, true);
  }

  @Test
    @Timeout(8000)
  void find_whenRootIsNullAndCreateFalse_returnsNull() throws Exception {
    // root is null by default
    Method find = LinkedTreeMap.class.getDeclaredMethod("find", Object.class, boolean.class);
    find.setAccessible(true);

    Object result = find.invoke(map, "key", false);
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void find_whenRootIsNullAndCreateTrue_createsRootNode() throws Exception {
    Method find = LinkedTreeMap.class.getDeclaredMethod("find", Object.class, boolean.class);
    find.setAccessible(true);

    Object key = "key";
    Object node = find.invoke(map, key, true);
    assertNotNull(node);

    // Check root is set to created node
    Field rootField = LinkedTreeMap.class.getDeclaredField("root");
    rootField.setAccessible(true);
    Object root = rootField.get(map);
    assertSame(node, root);

    // Check size and modCount incremented
    Field sizeField = LinkedTreeMap.class.getDeclaredField("size");
    sizeField.setAccessible(true);
    int size = sizeField.getInt(map);
    assertEquals(1, size);

    Field modCountField = LinkedTreeMap.class.getDeclaredField("modCount");
    modCountField.setAccessible(true);
    int modCount = modCountField.getInt(map);
    assertEquals(1, modCount);
  }

  @Test
    @Timeout(8000)
  void find_whenKeyExists_returnsNode() throws Exception {
    // Insert a root node first by calling find with create=true
    Method find = LinkedTreeMap.class.getDeclaredMethod("find", Object.class, boolean.class);
    find.setAccessible(true);

    String key = "key";
    Object rootNode = find.invoke(map, key, true);
    assertNotNull(rootNode);

    // Now find with create=false should return existing node
    Object foundNode = find.invoke(map, key, false);
    assertSame(rootNode, foundNode);
  }

  @Test
    @Timeout(8000)
  void find_whenKeyLessThanRoot_goesLeftAndCreatesNode() throws Exception {
    Method find = LinkedTreeMap.class.getDeclaredMethod("find", Object.class, boolean.class);
    find.setAccessible(true);

    // Insert root node with key "m"
    String rootKey = "m";
    Object rootNode = find.invoke(map, rootKey, true);
    assertNotNull(rootNode);

    // Insert left child with key "a" (less than "m")
    String leftKey = "a";
    Object leftNode = find.invoke(map, leftKey, true);
    assertNotNull(leftNode);
    assertNotSame(rootNode, leftNode);

    // Check root.left == leftNode
    Field rootField = LinkedTreeMap.class.getDeclaredField("root");
    rootField.setAccessible(true);
    Object root = rootField.get(map);

    Field leftField = root.getClass().getDeclaredField("left");
    leftField.setAccessible(true);
    Object left = leftField.get(root);
    assertSame(leftNode, left);
  }

  @Test
    @Timeout(8000)
  void find_whenKeyGreaterThanRoot_goesRightAndCreatesNode() throws Exception {
    Method find = LinkedTreeMap.class.getDeclaredMethod("find", Object.class, boolean.class);
    find.setAccessible(true);

    // Insert root node with key "m"
    String rootKey = "m";
    Object rootNode = find.invoke(map, rootKey, true);
    assertNotNull(rootNode);

    // Insert right child with key "z" (greater than "m")
    String rightKey = "z";
    Object rightNode = find.invoke(map, rightKey, true);
    assertNotNull(rightNode);
    assertNotSame(rootNode, rightNode);

    // Check root.right == rightNode
    Field rootField = LinkedTreeMap.class.getDeclaredField("root");
    rootField.setAccessible(true);
    Object root = rootField.get(map);

    Field rightField = root.getClass().getDeclaredField("right");
    rightField.setAccessible(true);
    Object right = rightField.get(root);
    assertSame(rightNode, right);
  }

  @Test
    @Timeout(8000)
  void find_whenComparatorIsNaturalOrderAndKeyNotComparable_throwsClassCastException() throws Exception {
    // Create LinkedTreeMap with natural order comparator and allowNullValues true
    LinkedTreeMap<Object, Object> naturalMap = new LinkedTreeMap<>(null, true);

    Method find = LinkedTreeMap.class.getDeclaredMethod("find", Object.class, boolean.class);
    find.setAccessible(true);

    Object nonComparableKey = new Object();

    // root is null, so will try to create root node and check Comparable
    ClassCastException ex = assertThrows(ClassCastException.class, () -> {
      find.invoke(naturalMap, nonComparableKey, true);
    });
    assertTrue(ex.getCause() instanceof ClassCastException);
    assertTrue(ex.getCause().getMessage().contains("is not Comparable"));
  }

  @Test
    @Timeout(8000)
  void find_whenComparatorIsCustom_usesComparator() throws Exception {
    @SuppressWarnings("unchecked")
    Comparator<Object> comparator = mock(Comparator.class);
    // comparator.compare returns positive value so it goes right and creates node
    when(comparator.compare(any(), any())).thenReturn(1);

    LinkedTreeMap<Object, Object> customMap = new LinkedTreeMap<>(comparator, true);

    Method find = LinkedTreeMap.class.getDeclaredMethod("find", Object.class, boolean.class);
    find.setAccessible(true);

    Object key1 = "key1";
    Object key2 = "key2";

    // Insert root node with key1
    Object rootNode = find.invoke(customMap, key1, true);
    assertNotNull(rootNode);

    // Insert right child with key2 (because comparator returns 1)
    Object rightNode = find.invoke(customMap, key2, true);
    assertNotNull(rightNode);
    assertNotSame(rootNode, rightNode);

    // Verify comparator.compare called at least once
    verify(comparator, atLeastOnce()).compare(any(), any());
  }
}