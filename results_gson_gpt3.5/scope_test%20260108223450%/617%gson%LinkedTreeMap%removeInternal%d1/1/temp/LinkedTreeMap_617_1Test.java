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

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LinkedTreeMap_RemoveInternalTest {

  LinkedTreeMap<String, String> map;

  // Helper method to create a Node instance
  @SuppressWarnings("unchecked")
  LinkedTreeMap.Node<String, String> createNode(String key, String value) throws Exception {
    // Node is a package-private static class inside LinkedTreeMap
    Class<?> nodeClass = null;
    for (Class<?> c : LinkedTreeMap.class.getDeclaredClasses()) {
      if ("Node".equals(c.getSimpleName())) {
        nodeClass = c;
        break;
      }
    }
    assertNotNull(nodeClass);

    // Constructor: Node(LinkedTreeMap, K key, V value, Node<K,V> parent)
    var ctor = nodeClass.getDeclaredConstructors()[0];
    ctor.setAccessible(true);

    // parent can be null for root or first node
    Object node = ctor.newInstance(map, key, value, null);

    return (LinkedTreeMap.Node<String, String>) node;
  }

  // Helper to set private fields of Node via reflection
  void setNodeField(LinkedTreeMap.Node<String, String> node, String fieldName, Object value) throws Exception {
    Field f = node.getClass().getDeclaredField(fieldName);
    f.setAccessible(true);
    f.set(node, value);
  }

  // Helper to get private fields of Node via reflection
  Object getNodeField(LinkedTreeMap.Node<String, String> node, String fieldName) throws Exception {
    Field f = node.getClass().getDeclaredField(fieldName);
    f.setAccessible(true);
    return f.get(node);
  }

  // Helper to invoke private method removeInternal via reflection
  void invokeRemoveInternal(LinkedTreeMap.Node<String, String> node, boolean unlink) throws Exception {
    Method m = LinkedTreeMap.class.getDeclaredMethod("removeInternal", 
        node.getClass(), boolean.class);
    m.setAccessible(true);
    m.invoke(map, node, unlink);
  }

  // Helper to invoke private method replaceInParent
  void invokeReplaceInParent(LinkedTreeMap.Node<String, String> node, LinkedTreeMap.Node<String, String> replacement) throws Exception {
    Method m = LinkedTreeMap.class.getDeclaredMethod("replaceInParent", 
        node.getClass(), node.getClass());
    m.setAccessible(true);
    m.invoke(map, node, replacement);
  }

  // Helper to invoke private method rebalance
  void invokeRebalance(LinkedTreeMap.Node<String, String> node, boolean insert) throws Exception {
    Method m = LinkedTreeMap.class.getDeclaredMethod("rebalance", 
        node.getClass(), boolean.class);
    m.setAccessible(true);
    m.invoke(map, node, insert);
  }

  @BeforeEach
  void setup() {
    map = new LinkedTreeMap<>();
  }

  @Test
    @Timeout(8000)
  void testRemoveInternal_unlinkTrue_noChildren() throws Exception {
    // Node with no children, unlink = true
    LinkedTreeMap.Node<String, String> node = createNode("key1", "val1");

    // Setup header and link node into linked list
    Field headerField = LinkedTreeMap.class.getDeclaredField("header");
    headerField.setAccessible(true);
    LinkedTreeMap.Node<String, String> header = (LinkedTreeMap.Node<String, String>) headerField.get(map);

    // Link node between header and header.next
    setNodeField(node, "prev", header);
    setNodeField(node, "next", header.next);
    setNodeField(header, "next", node);
    setNodeField(header.next, "prev", node);

    // Set parent null and no children
    setNodeField(node, "parent", null);
    setNodeField(node, "left", null);
    setNodeField(node, "right", null);
    setNodeField(node, "height", 1);

    // Set initial size and modCount
    Field sizeField = LinkedTreeMap.class.getDeclaredField("size");
    sizeField.setAccessible(true);
    sizeField.setInt(map, 1);

    Field modCountField = LinkedTreeMap.class.getDeclaredField("modCount");
    modCountField.setAccessible(true);
    modCountField.setInt(map, 0);

    // Spy on map to verify rebalance and replaceInParent calls
    LinkedTreeMap<String, String> spyMap = spy(map);

    // Replace map with spy for reflection invoke
    map = spyMap;

    invokeRemoveInternal(node, true);

    // Verify unlink happened: node.prev.next and node.next.prev updated to skip node
    LinkedTreeMap.Node<String, String> prev = (LinkedTreeMap.Node<String, String>) getNodeField(node, "prev");
    LinkedTreeMap.Node<String, String> next = (LinkedTreeMap.Node<String, String>) getNodeField(node, "next");
    assertSame(next, getNodeField(prev, "next"));
    assertSame(prev, getNodeField(next, "prev"));

    // Verify replaceInParent called with node and null
    verify(spyMap).replaceInParent(node, null);

    // Verify rebalance called with originalParent null and false
    verify(spyMap).rebalance(null, false);

    // Verify size decremented and modCount incremented
    assertEquals(0, sizeField.getInt(spyMap));
    assertEquals(1, modCountField.getInt(spyMap));
  }

  @Test
    @Timeout(8000)
  void testRemoveInternal_unlinkFalse_leftChildOnly() throws Exception {
    // Node with only left child, unlink = false
    LinkedTreeMap.Node<String, String> node = createNode("key1", "val1");
    LinkedTreeMap.Node<String, String> leftChild = createNode("key2", "val2");

    setNodeField(node, "left", leftChild);
    setNodeField(node, "right", null);
    setNodeField(node, "parent", null);
    setNodeField(leftChild, "parent", node);
    setNodeField(node, "height", 2);

    // Spy on map to verify calls
    LinkedTreeMap<String, String> spyMap = spy(map);
    map = spyMap;

    // Set size and modCount
    Field sizeField = LinkedTreeMap.class.getDeclaredField("size");
    sizeField.setAccessible(true);
    sizeField.setInt(spyMap, 2);

    Field modCountField = LinkedTreeMap.class.getDeclaredField("modCount");
    modCountField.setAccessible(true);
    modCountField.setInt(spyMap, 0);

    invokeRemoveInternal(node, false);

    // Verify replaceInParent called with node and leftChild
    verify(spyMap).replaceInParent(node, leftChild);

    // Node.left should be null after removal
    assertNull(getNodeField(node, "left"));

    // Verify rebalance called with originalParent null and false
    verify(spyMap).rebalance(null, false);

    // Verify size decremented and modCount incremented
    assertEquals(1, sizeField.getInt(spyMap));
    assertEquals(1, modCountField.getInt(spyMap));
  }

  @Test
    @Timeout(8000)
  void testRemoveInternal_unlinkTrue_rightChildOnly() throws Exception {
    // Node with only right child, unlink = true
    LinkedTreeMap.Node<String, String> node = createNode("key1", "val1");
    LinkedTreeMap.Node<String, String> rightChild = createNode("key3", "val3");

    // Setup header and link node into linked list for unlink test
    Field headerField = LinkedTreeMap.class.getDeclaredField("header");
    headerField.setAccessible(true);
    LinkedTreeMap.Node<String, String> header = (LinkedTreeMap.Node<String, String>) headerField.get(map);

    setNodeField(node, "prev", header);
    setNodeField(node, "next", header.next);
    setNodeField(header, "next", node);
    setNodeField(header.next, "prev", node);

    setNodeField(node, "left", null);
    setNodeField(node, "right", rightChild);
    setNodeField(node, "parent", null);
    setNodeField(rightChild, "parent", node);
    setNodeField(node, "height", 2);

    // Spy map
    LinkedTreeMap<String, String> spyMap = spy(map);
    map = spyMap;

    // Set size and modCount
    Field sizeField = LinkedTreeMap.class.getDeclaredField("size");
    sizeField.setAccessible(true);
    sizeField.setInt(spyMap, 2);

    Field modCountField = LinkedTreeMap.class.getDeclaredField("modCount");
    modCountField.setAccessible(true);
    modCountField.setInt(spyMap, 0);

    invokeRemoveInternal(node, true);

    // Verify unlink: node.prev.next and node.next.prev updated
    LinkedTreeMap.Node<String, String> prev = (LinkedTreeMap.Node<String, String>) getNodeField(node, "prev");
    LinkedTreeMap.Node<String, String> next = (LinkedTreeMap.Node<String, String>) getNodeField(node, "next");
    assertSame(next, getNodeField(prev, "next"));
    assertSame(prev, getNodeField(next, "prev"));

    // Verify replaceInParent called with node and rightChild
    verify(spyMap).replaceInParent(node, rightChild);

    // Node.right should be null after removal
    assertNull(getNodeField(node, "right"));

    // Verify rebalance called with originalParent null and false
    verify(spyMap).rebalance(null, false);

    // Verify size decremented and modCount incremented
    assertEquals(1, sizeField.getInt(spyMap));
    assertEquals(1, modCountField.getInt(spyMap));
  }

  @Test
    @Timeout(8000)
  void testRemoveInternal_unlinkFalse_twoChildren_leftHigher() throws Exception {
    // Node with two children, left.height > right.height
    LinkedTreeMap.Node<String, String> node = createNode("key1", "val1");
    LinkedTreeMap.Node<String, String> leftChild = createNode("key2", "val2");
    LinkedTreeMap.Node<String, String> rightChild = createNode("key3", "val3");
    LinkedTreeMap.Node<String, String> adjacent = createNode("key4", "val4");

    // Setup tree structure
    setNodeField(node, "left", leftChild);
    setNodeField(node, "right", rightChild);
    setNodeField(node, "parent", null);

    setNodeField(leftChild, "parent", node);
    setNodeField(rightChild, "parent", node);

    // Heights
    setNodeField(leftChild, "height", 3);
    setNodeField(rightChild, "height", 2);
    setNodeField(node, "height", 4);

    // Setup leftChild.last() to return adjacent
    Method lastMethod = leftChild.getClass().getDeclaredMethod("last");
    lastMethod.setAccessible(true);

    // Spy leftChild to override last() method
    LinkedTreeMap.Node<String, String> spyLeft = spy(leftChild);
    when(spyLeft.height).thenReturn(3);
    when(spyLeft.last()).thenReturn(adjacent);
    // Replace leftChild with spyLeft in node
    setNodeField(node, "left", spyLeft);
    setNodeField(spyLeft, "parent", node);

    // Setup adjacent children and parent null
    setNodeField(adjacent, "left", null);
    setNodeField(adjacent, "right", null);
    setNodeField(adjacent, "parent", null);
    setNodeField(adjacent, "height", 1);

    // Spy map to verify recursive removeInternal call and others
    LinkedTreeMap<String, String> spyMap = spy(map);
    map = spyMap;

    // Setup size and modCount
    Field sizeField = LinkedTreeMap.class.getDeclaredField("size");
    sizeField.setAccessible(true);
    sizeField.setInt(spyMap, 5);

    Field modCountField = LinkedTreeMap.class.getDeclaredField("modCount");
    modCountField.setAccessible(true);
    modCountField.setInt(spyMap, 0);

    // Spy removeInternal via doCallRealMethod for recursion
    doCallRealMethod().when(spyMap).removeInternal(any(), anyBoolean());
    doNothing().when(spyMap).replaceInParent(any(), any());
    doNothing().when(spyMap).rebalance(any(), anyBoolean());

    invokeRemoveInternal(node, false);

    // Verify recursive call with adjacent and unlink false
    verify(spyMap).removeInternal(adjacent, false);

    // Verify replaceInParent called with node and adjacent
    verify(spyMap).replaceInParent(node, adjacent);

    // Adjacent left and right set correctly
    assertSame(spyLeft, getNodeField(adjacent, "left"));
    assertSame(rightChild, getNodeField(adjacent, "right"));

    // Node left and right set to null
    assertNull(getNodeField(node, "left"));
    assertNull(getNodeField(node, "right"));

    // Adjacent height updated correctly
    int leftHeight = (int) getNodeField(spyLeft, "height");
    int rightHeight = (int) getNodeField(rightChild, "height");
    int expectedHeight = Math.max(leftHeight, rightHeight) + 1;
    assertEquals(expectedHeight, getNodeField(adjacent, "height"));

    // rebalance not called because replacedInParent is called and method returns early
    verify(spyMap, never()).rebalance(any(), anyBoolean());

    // size and modCount unchanged because recursive call handles size--
    assertEquals(5, sizeField.getInt(spyMap));
    assertEquals(0, modCountField.getInt(spyMap));
  }

  @Test
    @Timeout(8000)
  void testRemoveInternal_unlinkFalse_twoChildren_rightHigher() throws Exception {
    // Node with two children, right.height > left.height
    LinkedTreeMap.Node<String, String> node = createNode("key1", "val1");
    LinkedTreeMap.Node<String, String> leftChild = createNode("key2", "val2");
    LinkedTreeMap.Node<String, String> rightChild = createNode("key3", "val3");
    LinkedTreeMap.Node<String, String> adjacent = createNode("key5", "val5");

    setNodeField(node, "left", leftChild);
    setNodeField(node, "right", rightChild);
    setNodeField(node, "parent", null);

    setNodeField(leftChild, "parent", node);
    setNodeField(rightChild, "parent", node);

    setNodeField(leftChild, "height", 2);
    setNodeField(rightChild, "height", 4);
    setNodeField(node, "height", 5);

    // Setup rightChild.first() to return adjacent
    Method firstMethod = rightChild.getClass().getDeclaredMethod("first");
    firstMethod.setAccessible(true);

    LinkedTreeMap.Node<String, String> spyRight = spy(rightChild);
    when(spyRight.height).thenReturn(4);
    when(spyRight.first()).thenReturn(adjacent);
    setNodeField(node, "right", spyRight);
    setNodeField(spyRight, "parent", node);

    setNodeField(adjacent, "left", null);
    setNodeField(adjacent, "right", null);
    setNodeField(adjacent, "parent", null);
    setNodeField(adjacent, "height", 1);

    LinkedTreeMap<String, String> spyMap = spy(map);
    map = spyMap;

    Field sizeField = LinkedTreeMap.class.getDeclaredField("size");
    sizeField.setAccessible(true);
    sizeField.setInt(spyMap, 5);

    Field modCountField = LinkedTreeMap.class.getDeclaredField("modCount");
    modCountField.setAccessible(true);
    modCountField.setInt(spyMap, 0);

    doCallRealMethod().when(spyMap).removeInternal(any(), anyBoolean());
    doNothing().when(spyMap).replaceInParent(any(), any());
    doNothing().when(spyMap).rebalance(any(), anyBoolean());

    invokeRemoveInternal(node, false);

    verify(spyMap).removeInternal(adjacent, false);

    verify(spyMap).replaceInParent(node, adjacent);

    assertSame(leftChild, getNodeField(adjacent, "left"));
    assertSame(spyRight, getNodeField(adjacent, "right"));

    assertNull(getNodeField(node, "left"));
    assertNull(getNodeField(node, "right"));

    int leftHeight = (int) getNodeField(leftChild, "height");
    int rightHeight = (int) getNodeField(spyRight, "height");
    int expectedHeight = Math.max(leftHeight, rightHeight) + 1;
    assertEquals(expectedHeight, getNodeField(adjacent, "height"));

    verify(spyMap, never()).rebalance(any(), anyBoolean());

    assertEquals(5, sizeField.getInt(spyMap));
    assertEquals(0, modCountField.getInt(spyMap));
  }
}