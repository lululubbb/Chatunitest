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

public class LinkedTreeMapRemoveInternalTest {

  LinkedTreeMap<String, String> map;

  @BeforeEach
  public void setUp() {
    map = new LinkedTreeMap<>();
  }

  static class Node<K, V> {
    K key;
    V value;
    Node<K, V> parent;
    Node<K, V> left;
    Node<K, V> right;
    Node<K, V> next;
    Node<K, V> prev;
    int height;

    Node() {}

    Node(K key, V value) {
      this.key = key;
      this.value = value;
    }

    Node<K, V> first() {
      Node<K, V> current = this;
      while (current.left != null) {
        current = current.left;
      }
      return current;
    }

    Node<K, V> last() {
      Node<K, V> current = this;
      while (current.right != null) {
        current = current.right;
      }
      return current;
    }
  }

  @SuppressWarnings("unchecked")
  private void setField(Object target, String fieldName, Object value) throws Exception {
    Field field = target.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  @SuppressWarnings("unchecked")
  private Object getField(Object target, String fieldName) throws Exception {
    Field field = target.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(target);
  }

  private void invokeRemoveInternal(LinkedTreeMap<String, String> map, Node<String, String> node, boolean unlink) throws Exception {
    Method removeInternal = LinkedTreeMap.class.getDeclaredMethod("removeInternal", Node.class, boolean.class);
    removeInternal.setAccessible(true);
    removeInternal.invoke(map, node, unlink);
  }

  private void invokeReplaceInParent(LinkedTreeMap<String, String> map, Node<String, String> node, Node<String, String> replacement) throws Exception {
    Method replaceInParent = LinkedTreeMap.class.getDeclaredMethod("replaceInParent", Node.class, Node.class);
    replaceInParent.setAccessible(true);
    replaceInParent.invoke(map, node, replacement);
  }

  private void invokeRebalance(LinkedTreeMap<String, String> map, Node<String, String> unbalanced, boolean insert) throws Exception {
    Method rebalance = LinkedTreeMap.class.getDeclaredMethod("rebalance", Node.class, boolean.class);
    rebalance.setAccessible(true);
    rebalance.invoke(map, unbalanced, insert);
  }

  @Test
    @Timeout(8000)
  public void testRemoveInternal_unlinkTrue_noChildren() throws Exception {
    // Setup a node with prev and next linked
    Node<String, String> node = new Node<>("key", "value");
    Node<String, String> prev = new Node<>("prev", "p");
    Node<String, String> next = new Node<>("next", "n");
    node.prev = prev;
    node.next = next;
    prev.next = node;
    next.prev = node;

    // Set size and modCount
    setField(map, "size", 1);
    setField(map, "modCount", 0);

    // Spy the map to verify calls to replaceInParent and rebalance
    LinkedTreeMap<String, String> spyMap = spy(map);

    // Using reflection to replaceInParent and rebalance to do nothing to isolate test
    doNothing().when(spyMap).replaceInParent(any(), any());
    doNothing().when(spyMap).rebalance(any(), anyBoolean());

    // Inject spyMap to test method
    Method removeInternal = LinkedTreeMap.class.getDeclaredMethod("removeInternal", Node.class, boolean.class);
    removeInternal.setAccessible(true);

    // Manually set node.left and node.right null
    node.left = null;
    node.right = null;

    // Link prev and next to node properly
    node.prev = prev;
    node.next = next;
    prev.next = node;
    next.prev = node;

    // Call method with unlink = true
    removeInternal.invoke(spyMap, node, true);

    // Verify unlink performed
    assertSame(next, prev.next);
    assertSame(prev, next.prev);

    // Verify size decremented
    int size = (int) getField(spyMap, "size");
    assertEquals(0, size);

    // Verify modCount incremented
    int modCount = (int) getField(spyMap, "modCount");
    assertEquals(1, modCount);

    // Verify replaceInParent called with node and null
    verify(spyMap).replaceInParent(node, null);

    // Verify rebalance called with originalParent and false
    verify(spyMap).rebalance(node.parent, false);
  }

  @Test
    @Timeout(8000)
  public void testRemoveInternal_unlinkFalse_leftChildOnly() throws Exception {
    // Setup node with left child only
    Node<String, String> node = new Node<>("key", "value");
    Node<String, String> left = new Node<>("left", "l");
    left.parent = node;
    node.left = left;
    node.right = null;

    // Spy map and stub replaceInParent and rebalance
    LinkedTreeMap<String, String> spyMap = spy(map);
    doNothing().when(spyMap).replaceInParent(any(), any());
    doNothing().when(spyMap).rebalance(any(), anyBoolean());

    // Set size and modCount
    setField(spyMap, "size", 1);
    setField(spyMap, "modCount", 0);

    // Call removeInternal with unlink false
    invokeRemoveInternal(spyMap, node, false);

    // Verify replaceInParent called with node and left
    verify(spyMap).replaceInParent(node, left);

    // Verify node.left is null
    assertNull(node.left);

    // Verify size decremented
    int size = (int) getField(spyMap, "size");
    assertEquals(0, size);

    // Verify modCount incremented
    int modCount = (int) getField(spyMap, "modCount");
    assertEquals(1, modCount);

    // Verify rebalance called with originalParent and false
    verify(spyMap).rebalance(node.parent, false);
  }

  @Test
    @Timeout(8000)
  public void testRemoveInternal_unlinkFalse_rightChildOnly() throws Exception {
    // Setup node with right child only
    Node<String, String> node = new Node<>("key", "value");
    Node<String, String> right = new Node<>("right", "r");
    right.parent = node;
    node.right = right;
    node.left = null;

    // Spy map and stub replaceInParent and rebalance
    LinkedTreeMap<String, String> spyMap = spy(map);
    doNothing().when(spyMap).replaceInParent(any(), any());
    doNothing().when(spyMap).rebalance(any(), anyBoolean());

    // Set size and modCount
    setField(spyMap, "size", 1);
    setField(spyMap, "modCount", 0);

    // Call removeInternal with unlink false
    invokeRemoveInternal(spyMap, node, false);

    // Verify replaceInParent called with node and right
    verify(spyMap).replaceInParent(node, right);

    // Verify node.right is null
    assertNull(node.right);

    // Verify size decremented
    int size = (int) getField(spyMap, "size");
    assertEquals(0, size);

    // Verify modCount incremented
    int modCount = (int) getField(spyMap, "modCount");
    assertEquals(1, modCount);

    // Verify rebalance called with originalParent and false
    verify(spyMap).rebalance(node.parent, false);
  }

  @Test
    @Timeout(8000)
  public void testRemoveInternal_unlinkFalse_twoChildren_leftHeightGreater() throws Exception {
    // Setup node with two children, left.height > right.height
    Node<String, String> node = new Node<>("key", "value");

    Node<String, String> left = new Node<>("left", "l");
    left.height = 3;
    left.parent = node;

    Node<String, String> right = new Node<>("right", "r");
    right.height = 2;
    right.parent = node;

    node.left = left;
    node.right = right;

    // Setup adjacent node to be left.last()
    Node<String, String> adjacent = new Node<>("adjacent", "a");
    adjacent.height = 1;
    adjacent.parent = left;
    left.right = adjacent;

    // Spy map and stub replaceInParent, rebalance and removeInternal (recursive call)
    LinkedTreeMap<String, String> spyMap = spy(map);
    doNothing().when(spyMap).replaceInParent(any(), any());
    doNothing().when(spyMap).rebalance(any(), anyBoolean());

    // Stub removeInternal recursive call to do nothing to avoid infinite recursion
    doAnswer(invocation -> null).when(spyMap).getClass()
        .getDeclaredMethod("removeInternal", Node.class, boolean.class);

    // To stub private method removeInternal, use reflection and spy hack:
    // Instead, we override removeInternal by reflection to do nothing for adjacent call
    Method removeInternalMethod = LinkedTreeMap.class.getDeclaredMethod("removeInternal", Node.class, boolean.class);
    removeInternalMethod.setAccessible(true);

    // We cannot stub private method easily, so we spy and call real method but break recursion by setting children null on adjacent
    // To avoid complexity, we create a subclass to override removeInternal for this test

    class TestLinkedTreeMap extends LinkedTreeMap<String, String> {
      boolean removedAdjacent = false;

      @Override
      void removeInternal(Node<String, String> node, boolean unlink) {
        if (node == adjacent && !removedAdjacent) {
          removedAdjacent = true;
          // simulate removal of adjacent node with no children
          if (unlink) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
          }
          replaceInParent(node, null);
          rebalance(node.parent, false);
          size--;
          modCount++;
          return;
        }
        super.removeInternal(node, unlink);
      }
    }

    TestLinkedTreeMap testMap = new TestLinkedTreeMap();
    setField(testMap, "size", 3);
    setField(testMap, "modCount", 0);

    // Set node.parent to null for simplicity
    node.parent = null;

    // Call removeInternal on node with unlink false
    removeInternalMethod.invoke(testMap, node, false);

    // Verify adjacent.left and adjacent.right set correctly
    assertSame(left, adjacent.left);
    assertSame(right, adjacent.right);
    assertEquals(Math.max(left.height, right.height) + 1, adjacent.height);

    // Verify node.left and node.right set to null
    assertNull(node.left);
    assertNull(node.right);

    // Verify size decremented by 2 (node + adjacent)
    int sizeAfter = (int) getField(testMap, "size");
    assertEquals(1, sizeAfter);

    // Verify modCount incremented by 2
    int modCountAfter = (int) getField(testMap, "modCount");
    assertEquals(2, modCountAfter);
  }

  @Test
    @Timeout(8000)
  public void testRemoveInternal_unlinkTrue_twoChildren_rightHeightGreater() throws Exception {
    // Setup node with two children, right.height > left.height
    Node<String, String> node = new Node<>("key", "value");

    Node<String, String> left = new Node<>("left", "l");
    left.height = 1;
    left.parent = node;

    Node<String, String> right = new Node<>("right", "r");
    right.height = 4;
    right.parent = node;

    node.left = left;
    node.right = right;

    // Setup adjacent node to be right.first()
    Node<String, String> adjacent = new Node<>("adjacent", "a");
    adjacent.height = 2;
    adjacent.parent = right;
    right.left = adjacent;

    // Setup prev and next for unlink
    Node<String, String> prev = new Node<>("prev", "p");
    Node<String, String> next = new Node<>("next", "n");
    node.prev = prev;
    node.next = next;
    prev.next = node;
    next.prev = node;

    // Create subclass to override removeInternal to avoid recursion
    class TestLinkedTreeMap extends LinkedTreeMap<String, String> {
      boolean removedAdjacent = false;

      @Override
      void removeInternal(Node<String, String> node, boolean unlink) {
        if (node == adjacent && !removedAdjacent) {
          removedAdjacent = true;
          if (unlink) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
          }
          replaceInParent(node, null);
          rebalance(node.parent, false);
          size--;
          modCount++;
          return;
        }
        super.removeInternal(node, unlink);
      }
    }

    TestLinkedTreeMap testMap = new TestLinkedTreeMap();
    setField(testMap, "size", 4);
    setField(testMap, "modCount", 0);

    // Call removeInternal on node with unlink true
    Method removeInternalMethod = LinkedTreeMap.class.getDeclaredMethod("removeInternal", Node.class, boolean.class);
    removeInternalMethod.setAccessible(true);
    removeInternalMethod.invoke(testMap, node, true);

    // Verify unlink performed on prev and next
    assertSame(next, prev.next);
    assertSame(prev, next.prev);

    // Verify adjacent.left and adjacent.right set correctly
    assertSame(left, adjacent.left);
    assertSame(right, adjacent.right);
    assertEquals(Math.max(left.height, right.height) + 1, adjacent.height);

    // Verify node.left and node.right set to null
    assertNull(node.left);
    assertNull(node.right);

    // Verify size decremented by 2 (node + adjacent)
    int sizeAfter = (int) getField(testMap, "size");
    assertEquals(2, sizeAfter);

    // Verify modCount incremented by 2
    int modCountAfter = (int) getField(testMap, "modCount");
    assertEquals(2, modCountAfter);
  }

}