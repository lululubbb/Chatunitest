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
  LinkedTreeMap.Node<String, String> header;

  @BeforeEach
  void setUp() throws Exception {
    // Use constructor with allowNullValues = true for testing
    map = new LinkedTreeMap<>(true);
    // Access header field via reflection
    Field headerField = LinkedTreeMap.class.getDeclaredField("header");
    headerField.setAccessible(true);
    header = (LinkedTreeMap.Node<String, String>) headerField.get(map);
  }

  @Test
    @Timeout(8000)
  void find_returnsNullWhenEmptyAndCreateFalse() throws Exception {
    // root == null, create == false => returns null
    LinkedTreeMap.Node<String, String> result = invokeFind(map, "key", false);
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void find_createsRootNodeWhenEmptyAndCreateTrue() throws Exception {
    // root == null, create == true => creates root node
    LinkedTreeMap.Node<String, String> result = invokeFind(map, "key", true);
    assertNotNull(result);
    assertEquals("key", result.key);
    // root field updated
    Field rootField = LinkedTreeMap.class.getDeclaredField("root");
    rootField.setAccessible(true);
    LinkedTreeMap.Node<String, String> root = (LinkedTreeMap.Node<String, String>) rootField.get(map);
    assertSame(result, root);
    // size and modCount incremented
    Field sizeField = LinkedTreeMap.class.getDeclaredField("size");
    sizeField.setAccessible(true);
    assertEquals(1, sizeField.getInt(map));
    Field modCountField = LinkedTreeMap.class.getDeclaredField("modCount");
    modCountField.setAccessible(true);
    assertEquals(1, modCountField.getInt(map));
  }

  @Test
    @Timeout(8000)
  void find_throwsClassCastExceptionIfNotComparableAndNaturalOrder() throws Exception {
    // Create map with natural order comparator (default) and allowNullValues true
    LinkedTreeMap<Object, Object> m = new LinkedTreeMap<>(true);
    // Try to find non-Comparable key with create true => throws ClassCastException
    assertThrows(ClassCastException.class, () -> {
      invokeFind(m, new Object(), true);
    });
  }

  @Test
    @Timeout(8000)
  void find_findsExistingNodeUsingNaturalOrder() throws Exception {
    // Insert two nodes manually to form a tree: root "b", left "a", right "c"
    LinkedTreeMap.Node<String, String> nodeB = createNode(null, "b");
    LinkedTreeMap.Node<String, String> nodeA = createNode(nodeB, "a");
    LinkedTreeMap.Node<String, String> nodeC = createNode(nodeB, "c");
    nodeB.left = nodeA;
    nodeB.right = nodeC;

    setRoot(nodeB);
    setSize(3);

    // find "a" with create false => returns nodeA
    LinkedTreeMap.Node<String, String> foundA = invokeFind(map, "a", false);
    assertSame(nodeA, foundA);

    // find "c" with create false => returns nodeC
    LinkedTreeMap.Node<String, String> foundC = invokeFind(map, "c", false);
    assertSame(nodeC, foundC);

    // find "b" with create false => returns nodeB
    LinkedTreeMap.Node<String, String> foundB = invokeFind(map, "b", false);
    assertSame(nodeB, foundB);

    // find "d" with create false => returns null
    LinkedTreeMap.Node<String, String> foundD = invokeFind(map, "d", false);
    assertNull(foundD);
  }

  @Test
    @Timeout(8000)
  void find_createsNodeInLeftOrRightSubtreeAndRebalances() throws Exception {
    // Create root node "b"
    LinkedTreeMap.Node<String, String> nodeB = createNode(null, "b");
    setRoot(nodeB);
    setSize(1);

    // Spy on map to verify rebalance call
    LinkedTreeMap<String, String> spyMap = spy(map);

    // find "a" with create true => creates left child of "b"
    LinkedTreeMap.Node<String, String> createdA = invokeFind(spyMap, "a", true);
    assertNotNull(createdA);
    assertEquals("a", createdA.key);
    assertSame(createdA, nodeB.left);

    // verify rebalance called with nodeB and true
    verify(spyMap).rebalance(nodeB, true);

    // find "c" with create true => creates right child of "b"
    LinkedTreeMap.Node<String, String> createdC = invokeFind(spyMap, "c", true);
    assertNotNull(createdC);
    assertEquals("c", createdC.key);
    assertSame(createdC, nodeB.right);

    verify(spyMap, times(2)).rebalance(nodeB, true);

    // size and modCount incremented accordingly
    Field sizeField = LinkedTreeMap.class.getDeclaredField("size");
    sizeField.setAccessible(true);
    assertEquals(3, sizeField.getInt(spyMap));
    Field modCountField = LinkedTreeMap.class.getDeclaredField("modCount");
    modCountField.setAccessible(true);
    assertEquals(3, modCountField.getInt(spyMap));
  }

  // Helper to invoke private find method via reflection
  @SuppressWarnings("unchecked")
  private LinkedTreeMap.Node<String, String> invokeFind(LinkedTreeMap<String, String> map, String key, boolean create) throws Exception {
    Method findMethod = LinkedTreeMap.class.getDeclaredMethod("find", Object.class, boolean.class);
    findMethod.setAccessible(true);
    return (LinkedTreeMap.Node<String, String>) findMethod.invoke(map, key, create);
  }

  // Helper to create a Node instance (reflection to constructor)
  private LinkedTreeMap.Node<String, String> createNode(LinkedTreeMap.Node<String, String> parent, String key) throws Exception {
    Class<?> nodeClass = Class.forName("com.google.gson.internal.LinkedTreeMap$Node");
    // Constructor: Node(boolean allowNullValues, Node<K,V> parent, K key, Node<K,V> next, Node<K,V> prev)
    // Use header for next and prev
    return (LinkedTreeMap.Node<String, String>) nodeClass
        .getDeclaredConstructor(boolean.class, nodeClass, Object.class, nodeClass, nodeClass)
        .newInstance(true, parent, key, header, header.prev);
  }

  private void setRoot(LinkedTreeMap.Node<String, String> rootNode) throws Exception {
    Field rootField = LinkedTreeMap.class.getDeclaredField("root");
    rootField.setAccessible(true);
    rootField.set(map, rootNode);
  }

  private void setSize(int size) throws Exception {
    Field sizeField = LinkedTreeMap.class.getDeclaredField("size");
    sizeField.setAccessible(true);
    sizeField.setInt(map, size);
  }
}