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

class LinkedTreeMapRemoveInternalTest {

  LinkedTreeMap<String, String> map;

  // Helper to create a Node instance via reflection
  @SuppressWarnings("unchecked")
  private LinkedTreeMap.Node<String, String> createNode(String key, String value) throws Exception {
    Class<?> nodeClass = Class.forName("com.google.gson.internal.LinkedTreeMap$Node");
    // Node constructor: Node(Node<K,V> parent, K key, V value, Node<K,V> next)
    // We'll pass null for parent and next for simplicity
    return (LinkedTreeMap.Node<String, String>) nodeClass
        .getDeclaredConstructor(
            nodeClass, Object.class, Object.class, nodeClass)
        .newInstance(null, key, value, null);
  }

  @BeforeEach
  void setup() {
    map = new LinkedTreeMap<>();
  }

  @Test
    @Timeout(8000)
  void testRemoveInternal_unlinkTrue_noChildren() throws Exception {
    // Setup node with no children, unlink = true
    LinkedTreeMap.Node<String, String> node = createNode("key", "value");
    // Setup doubly linked list prev and next nodes for unlinking
    LinkedTreeMap.Node<String, String> prev = createNode("prev", "vprev");
    LinkedTreeMap.Node<String, String> next = createNode("next", "vnext");
    setField(node, "prev", prev);
    setField(node, "next", next);
    setField(prev, "next", node);
    setField(next, "prev", node);

    // Set size and modCount
    setField(map, "size", 1);
    setField(map, "modCount", 0);

    // Call removeInternal(node, true)
    invokeRemoveInternal(node, true);

    // After removal, prev.next should be next and next.prev should be prev
    assertSame(next, getField(prev, "next"));
    assertSame(prev, getField(next, "prev"));

    // size should decrement by 1
    assertEquals(0, getField(map, "size"));
    // modCount should increment by 1
    assertEquals(1, getField(map, "modCount"));
  }

  @Test
    @Timeout(8000)
  void testRemoveInternal_unlinkFalse_twoChildren_leftHigher() throws Exception {
    // Setup node with two children, left.height > right.height
    LinkedTreeMap.Node<String, String> node = createNode("key", "value");
    LinkedTreeMap.Node<String, String> left = createNode("left", "vleft");
    LinkedTreeMap.Node<String, String> right = createNode("right", "vright");
    setField(node, "left", left);
    setField(node, "right", right);
    setField(left, "height", 3);
    setField(right, "height", 2);
    // Setup parent for node
    LinkedTreeMap.Node<String, String> parent = createNode("parent", "vparent");
    setField(node, "parent", parent);

    // Setup left.last() to return a leaf node
    LinkedTreeMap.Node<String, String> leftLast = createNode("leftLast", "vleftLast");
    setField(leftLast, "height", 1);
    // Override left.last() by mocking left node with spy and reflection
    LinkedTreeMap.Node<String, String> leftSpy = spy(left);
    doReturn(leftLast).when(leftSpy).last();
    setField(node, "left", leftSpy);

    // Setup right.first() to a node (not used in this test)
    LinkedTreeMap.Node<String, String> rightFirst = createNode("rightFirst", "vrightFirst");
    setField(rightFirst, "height", 1);
    LinkedTreeMap.Node<String, String> rightSpy = spy(right);
    doReturn(rightFirst).when(rightSpy).first();
    setField(node, "right", rightSpy);

    // Setup map size and modCount
    setField(map, "size", 5);
    setField(map, "modCount", 10);

    // Spy on map to verify recursive removeInternal call
    LinkedTreeMap<String, String> spyMap = spy(map);

    // Replace the map instance with spy for reflection calls
    // We'll invoke removeInternal on spyMap
    invokeRemoveInternal(spyMap, node, false);

    // Verify removeInternal(adjacent, false) called once with leftLast node
    verify(spyMap, times(1)).removeInternal(leftLast, false);

    // Verify leftLast.left and leftLast.right are set properly and heights updated
    assertSame(leftSpy, getField(leftLast, "left"));
    assertSame(rightSpy, getField(leftLast, "right"));
    int leftHeight = (int) getField(leftSpy, "height");
    int rightHeight = (int) getField(rightSpy, "height");
    int expectedHeight = Math.max(leftHeight, rightHeight) + 1;
    assertEquals(expectedHeight, getField(leftLast, "height"));

    // Verify node.left and node.right set to null
    assertNull(getField(node, "left"));
    assertNull(getField(node, "right"));
  }

  @Test
    @Timeout(8000)
  void testRemoveInternal_unlinkFalse_leftOnly() throws Exception {
    LinkedTreeMap.Node<String, String> node = createNode("key", "value");
    LinkedTreeMap.Node<String, String> left = createNode("left", "vleft");
    setField(node, "left", left);
    setField(node, "right", null);
    setField(node, "parent", null);

    setField(map, "size", 2);
    setField(map, "modCount", 3);

    // Spy on map to verify replaceInParent call
    LinkedTreeMap<String, String> spyMap = spy(map);
    invokeRemoveInternal(spyMap, node, false);

    // node.left should be null after removal
    assertNull(getField(node, "left"));
    // size decremented
    assertEquals(1, getField(spyMap, "size"));
  }

  @Test
    @Timeout(8000)
  void testRemoveInternal_unlinkFalse_rightOnly() throws Exception {
    LinkedTreeMap.Node<String, String> node = createNode("key", "value");
    LinkedTreeMap.Node<String, String> right = createNode("right", "vright");
    setField(node, "left", null);
    setField(node, "right", right);
    setField(node, "parent", null);

    setField(map, "size", 2);
    setField(map, "modCount", 3);

    LinkedTreeMap<String, String> spyMap = spy(map);
    invokeRemoveInternal(spyMap, node, false);

    // node.right should be null after removal
    assertNull(getField(node, "right"));
    // size decremented
    assertEquals(1, getField(spyMap, "size"));
  }

  @Test
    @Timeout(8000)
  void testRemoveInternal_unlinkFalse_noChildren() throws Exception {
    LinkedTreeMap.Node<String, String> node = createNode("key", "value");
    setField(node, "left", null);
    setField(node, "right", null);
    setField(node, "parent", null);

    setField(map, "size", 1);
    setField(map, "modCount", 0);

    LinkedTreeMap<String, String> spyMap = spy(map);
    invokeRemoveInternal(spyMap, node, false);

    // size decremented
    assertEquals(0, getField(spyMap, "size"));
  }

  // Helper methods

  private void invokeRemoveInternal(LinkedTreeMap.Node<String, String> node, boolean unlink) throws Exception {
    invokeRemoveInternal(map, node, unlink);
  }

  private void invokeRemoveInternal(LinkedTreeMap<String, String> instance, LinkedTreeMap.Node<String, String> node, boolean unlink) throws Exception {
    Method method = LinkedTreeMap.class.getDeclaredMethod("removeInternal", Class.forName("com.google.gson.internal.LinkedTreeMap$Node"), boolean.class);
    method.setAccessible(true);
    method.invoke(instance, node, unlink);
  }

  private void setField(Object obj, String fieldName, Object value) throws Exception {
    Field field = getFieldFromHierarchy(obj.getClass(), fieldName);
    field.setAccessible(true);
    field.set(obj, value);
  }

  private Object getField(Object obj, String fieldName) throws Exception {
    Field field = getFieldFromHierarchy(obj.getClass(), fieldName);
    field.setAccessible(true);
    return field.get(obj);
  }

  private Field getFieldFromHierarchy(Class<?> clazz, String fieldName) throws NoSuchFieldException {
    Class<?> current = clazz;
    while (current != null) {
      try {
        return current.getDeclaredField(fieldName);
      } catch (NoSuchFieldException e) {
        current = current.getSuperclass();
      }
    }
    throw new NoSuchFieldException(fieldName);
  }
}