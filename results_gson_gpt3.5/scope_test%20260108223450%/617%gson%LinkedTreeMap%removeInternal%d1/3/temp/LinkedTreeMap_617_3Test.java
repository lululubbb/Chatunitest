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

class LinkedTreeMap_removeInternal_Test {

  LinkedTreeMap<String, String> map;
  LinkedTreeMap.Node<String, String> node;
  LinkedTreeMap.Node<String, String> left;
  LinkedTreeMap.Node<String, String> right;
  LinkedTreeMap.Node<String, String> parent;
  LinkedTreeMap.Node<String, String> adjacent;

  @BeforeEach
  void setUp() throws Exception {
    map = new LinkedTreeMap<>();
    // Create nodes manually because Node is package-private static class
    // Using reflection to get Node class and constructor
    Class<?> nodeClass = Class.forName("com.google.gson.internal.LinkedTreeMap$Node");
    // Node constructor: (K key, V value, Node<K,V> parent)
    node = (LinkedTreeMap.Node<String, String>) nodeClass.getDeclaredConstructor(
        Object.class, Object.class, nodeClass).newInstance("nodeKey", "nodeVal", null);
    left = (LinkedTreeMap.Node<String, String>) nodeClass.getDeclaredConstructor(
        Object.class, Object.class, nodeClass).newInstance("leftKey", "leftVal", node);
    right = (LinkedTreeMap.Node<String, String>) nodeClass.getDeclaredConstructor(
        Object.class, Object.class, nodeClass).newInstance("rightKey", "rightVal", node);
    parent = (LinkedTreeMap.Node<String, String>) nodeClass.getDeclaredConstructor(
        Object.class, Object.class, nodeClass).newInstance("parentKey", "parentVal", null);
    adjacent = (LinkedTreeMap.Node<String, String>) nodeClass.getDeclaredConstructor(
        Object.class, Object.class, nodeClass).newInstance("adjKey", "adjVal", null);

    // Setup node links for tests
    node.left = left;
    node.right = right;
    node.parent = parent;

    // Setup left and right heights
    setField(left, "height", 3);
    setField(right, "height", 2);

    // Setup linked list prev/next for unlinking
    LinkedTreeMap.Node<String, String> prev = (LinkedTreeMap.Node<String, String>) nodeClass.getDeclaredConstructor(
        Object.class, Object.class, nodeClass).newInstance("prevKey", "prevVal", null);
    LinkedTreeMap.Node<String, String> next = (LinkedTreeMap.Node<String, String>) nodeClass.getDeclaredConstructor(
        Object.class, Object.class, nodeClass).newInstance("nextKey", "nextVal", null);
    node.prev = prev;
    node.next = next;
    prev.next = node;
    next.prev = node;

    // Setup map root for replaceInParent
    setField(map, "root", node);

    // Setup size and modCount
    setField(map, "size", 1);
    setField(map, "modCount", 0);
  }

  @Test
    @Timeout(8000)
  void testRemoveInternal_unlinkTrue_twoChildren_leftHigher() throws Exception {
    // left.height > right.height so adjacent = left.last()
    // We mock left.last() to return adjacent
    Method lastMethod = left.getClass().getDeclaredMethod("last");
    lastMethod.setAccessible(true);
    LinkedTreeMap.Node<String, String> spyLeft = spy(left);
    doReturn(adjacent).when(spyLeft).last();
    node.left = spyLeft;

    // right.first() should not be called but we prepare anyway
    Method firstMethod = right.getClass().getDeclaredMethod("first");
    firstMethod.setAccessible(true);
    LinkedTreeMap.Node<String, String> spyRight = spy(right);
    doReturn(adjacent).when(spyRight).first();
    node.right = spyRight;

    // Spy map to verify removeInternal call with unlink=false
    LinkedTreeMap<String, String> spyMap = spy(map);

    // Replace node.left with spyLeft and node.right with spyRight
    node.left = spyLeft;
    node.right = spyRight;

    // Also spy removeInternal method to call real method except for recursive call
    Method removeInternalMethod = LinkedTreeMap.class.getDeclaredMethod("removeInternal",
        Class.forName("com.google.gson.internal.LinkedTreeMap$Node"), boolean.class);
    removeInternalMethod.setAccessible(true);

    // To avoid infinite recursion, we stub recursive call to removeInternal(adjacent, false)
    doAnswer(invocation -> {
      LinkedTreeMap.Node<String, String> argNode = invocation.getArgument(0);
      boolean unlink = invocation.getArgument(1);
      if (argNode == adjacent && !unlink) {
        // simulate removal side effects on adjacent node: size-- and modCount++
        int size = (int) getField(spyMap, "size");
        setField(spyMap, "size", size - 1);
        int modCount = (int) getField(spyMap, "modCount");
        setField(spyMap, "modCount", modCount + 1);
        return null;
      } else {
        return removeInternalMethod.invoke(spyMap, argNode, unlink);
      }
    }).when(spyMap).removeInternal(any(), anyBoolean());

    // ReplaceInParent and rebalance are private, we spy them to verify called
    doNothing().when(spyMap).replaceInParent(any(), any());
    doNothing().when(spyMap).rebalance(any(), anyBoolean());

    // Call removeInternal with unlink=true
    removeInternalMethod.invoke(spyMap, node, true);

    // Verify unlinking prev/next pointers
    assertSame(node.next, node.prev.next);
    assertSame(node.prev, node.next.prev);

    // Verify replaceInParent called with node and adjacent
    verify(spyMap).replaceInParent(node, adjacent);

    // Verify rebalance called with originalParent and false
    verify(spyMap).rebalance(parent, false);

    // Verify size decremented by 1 (initial 1 - 1 from recursive call + 1 from this call)
    int finalSize = (int) getField(spyMap, "size");
    assertEquals(0, finalSize);

    // Verify modCount incremented at least once
    int finalModCount = (int) getField(spyMap, "modCount");
    assertTrue(finalModCount >= 1);

    // Adjacent should have left and right set properly and height updated
    assertSame(adjacent.left, spyLeft);
    assertSame(adjacent.right, spyRight);
    assertEquals(Math.max((int) getField(spyLeft, "height"), (int) getField(spyRight, "height")) + 1,
        (int) getField(adjacent, "height"));

    // Original node left and right set to null
    assertNull(node.left);
    assertNull(node.right);
  }

  @Test
    @Timeout(8000)
  void testRemoveInternal_unlinkFalse_twoChildren_rightHigher() throws Exception {
    // Setup right.height > left.height (3 > 2)
    setField(left, "height", 2);
    setField(right, "height", 3);

    // Spy right.first() to return adjacent
    Method firstMethod = right.getClass().getDeclaredMethod("first");
    firstMethod.setAccessible(true);
    LinkedTreeMap.Node<String, String> spyRight = spy(right);
    doReturn(adjacent).when(spyRight).first();
    node.right = spyRight;

    // Spy left.last()
    Method lastMethod = left.getClass().getDeclaredMethod("last");
    lastMethod.setAccessible(true);
    LinkedTreeMap.Node<String, String> spyLeft = spy(left);
    doReturn(adjacent).when(spyLeft).last();
    node.left = spyLeft;

    // Spy map
    LinkedTreeMap<String, String> spyMap = spy(map);

    Method removeInternalMethod = LinkedTreeMap.class.getDeclaredMethod("removeInternal",
        Class.forName("com.google.gson.internal.LinkedTreeMap$Node"), boolean.class);
    removeInternalMethod.setAccessible(true);

    // Stub recursive call to removeInternal(adjacent, false)
    doAnswer(invocation -> {
      LinkedTreeMap.Node<String, String> argNode = invocation.getArgument(0);
      boolean unlink = invocation.getArgument(1);
      if (argNode == adjacent && !unlink) {
        int size = (int) getField(spyMap, "size");
        setField(spyMap, "size", size - 1);
        int modCount = (int) getField(spyMap, "modCount");
        setField(spyMap, "modCount", modCount + 1);
        return null;
      } else {
        return removeInternalMethod.invoke(spyMap, argNode, unlink);
      }
    }).when(spyMap).removeInternal(any(), anyBoolean());

    doNothing().when(spyMap).replaceInParent(any(), any());
    doNothing().when(spyMap).rebalance(any(), anyBoolean());

    removeInternalMethod.invoke(spyMap, node, false);

    // unlink false so prev/next pointers unchanged
    assertSame(node.prev.next, node);
    assertSame(node.next.prev, node);

    verify(spyMap).replaceInParent(node, adjacent);
    verify(spyMap).rebalance(parent, false);

    int finalSize = (int) getField(spyMap, "size");
    assertEquals(0, finalSize);

    int finalModCount = (int) getField(spyMap, "modCount");
    assertTrue(finalModCount >= 1);

    assertSame(adjacent.left, spyLeft);
    assertSame(adjacent.right, spyRight);
    assertEquals(Math.max((int) getField(spyLeft, "height"), (int) getField(spyRight, "height")) + 1,
        (int) getField(adjacent, "height"));

    assertNull(node.left);
    assertNull(node.right);
  }

  @Test
    @Timeout(8000)
  void testRemoveInternal_unlinkTrue_onlyLeftChild() throws Exception {
    node.right = null;
    node.left = left;
    left.parent = node;

    LinkedTreeMap<String, String> spyMap = spy(map);

    Method removeInternalMethod = LinkedTreeMap.class.getDeclaredMethod("removeInternal",
        Class.forName("com.google.gson.internal.LinkedTreeMap$Node"), boolean.class);
    removeInternalMethod.setAccessible(true);

    doNothing().when(spyMap).replaceInParent(any(), any());
    doNothing().when(spyMap).rebalance(any(), anyBoolean());

    removeInternalMethod.invoke(spyMap, node, true);

    verify(spyMap).replaceInParent(node, left);
    verify(spyMap).rebalance(parent, false);

    assertNull(node.left);

    int finalSize = (int) getField(spyMap, "size");
    assertEquals(0, finalSize);

    int finalModCount = (int) getField(spyMap, "modCount");
    assertTrue(finalModCount >= 1);
  }

  @Test
    @Timeout(8000)
  void testRemoveInternal_unlinkTrue_onlyRightChild() throws Exception {
    node.left = null;
    node.right = right;
    right.parent = node;

    LinkedTreeMap<String, String> spyMap = spy(map);

    Method removeInternalMethod = LinkedTreeMap.class.getDeclaredMethod("removeInternal",
        Class.forName("com.google.gson.internal.LinkedTreeMap$Node"), boolean.class);
    removeInternalMethod.setAccessible(true);

    doNothing().when(spyMap).replaceInParent(any(), any());
    doNothing().when(spyMap).rebalance(any(), anyBoolean());

    removeInternalMethod.invoke(spyMap, node, true);

    verify(spyMap).replaceInParent(node, right);
    verify(spyMap).rebalance(parent, false);

    assertNull(node.right);

    int finalSize = (int) getField(spyMap, "size");
    assertEquals(0, finalSize);

    int finalModCount = (int) getField(spyMap, "modCount");
    assertTrue(finalModCount >= 1);
  }

  @Test
    @Timeout(8000)
  void testRemoveInternal_unlinkTrue_noChildren() throws Exception {
    node.left = null;
    node.right = null;

    LinkedTreeMap<String, String> spyMap = spy(map);

    Method removeInternalMethod = LinkedTreeMap.class.getDeclaredMethod("removeInternal",
        Class.forName("com.google.gson.internal.LinkedTreeMap$Node"), boolean.class);
    removeInternalMethod.setAccessible(true);

    doNothing().when(spyMap).replaceInParent(any(), any());
    doNothing().when(spyMap).rebalance(any(), anyBoolean());

    removeInternalMethod.invoke(spyMap, node, true);

    verify(spyMap).replaceInParent(node, null);
    verify(spyMap).rebalance(parent, false);

    int finalSize = (int) getField(spyMap, "size");
    assertEquals(0, finalSize);

    int finalModCount = (int) getField(spyMap, "modCount");
    assertTrue(finalModCount >= 1);
  }

  // Helper methods to get/set private fields via reflection
  private static Object getField(Object obj, String fieldName) throws Exception {
    Field field = obj.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(obj);
  }

  private static void setField(Object obj, String fieldName, Object value) throws Exception {
    Field field = obj.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(obj, value);
  }
}