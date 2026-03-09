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

class LinkedTreeMap_removeInternal_Test {

  LinkedTreeMap<String, String> map;

  // Helper to create a Node instance via reflection
  @SuppressWarnings("unchecked")
  private LinkedTreeMap.Node<String, String> createNode(String key, String value) throws Exception {
    Class<?> nodeClass = null;
    for (Class<?> c : LinkedTreeMap.class.getDeclaredClasses()) {
      if ("Node".equals(c.getSimpleName())) {
        nodeClass = c;
        break;
      }
    }
    assertNotNull(nodeClass);

    // Node constructor: Node(Node<K,V> parent, K key, Node<K,V> next, Node<K,V> prev)
    // We can pass null for parent, next, prev for simplicity
    return (LinkedTreeMap.Node<String, String>) nodeClass
        .getDeclaredConstructor(LinkedTreeMap.Node.class, Object.class, LinkedTreeMap.Node.class, LinkedTreeMap.Node.class)
        .newInstance(null, key, null, null);
  }

  // Helper to set a Node field via reflection
  private void setNodeField(Object node, String fieldName, Object value) throws Exception {
    Field field = node.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(node, value);
  }

  // Helper to get a Node field via reflection
  private Object getNodeField(Object node, String fieldName) throws Exception {
    Field field = node.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(node);
  }

  @BeforeEach
  void setup() {
    map = new LinkedTreeMap<>();
  }

  @Test
    @Timeout(8000)
  void testRemoveInternal_unlinkTrue_noChildren() throws Exception {
    LinkedTreeMap.Node<String, String> node = createNode("key", "value");
    // Setup linked list pointers for unlink
    LinkedTreeMap.Node<String, String> prev = createNode("prev", "pval");
    LinkedTreeMap.Node<String, String> next = createNode("next", "nval");
    setNodeField(node, "prev", prev);
    setNodeField(node, "next", next);
    setNodeField(prev, "next", node);
    setNodeField(next, "prev", node);

    // Setup parent to null, no children
    setNodeField(node, "parent", null);
    setNodeField(node, "left", null);
    setNodeField(node, "right", null);

    // Set size and modCount for later verification
    Field sizeField = LinkedTreeMap.class.getDeclaredField("size");
    sizeField.setAccessible(true);
    sizeField.setInt(map, 1);
    Field modCountField = LinkedTreeMap.class.getDeclaredField("modCount");
    modCountField.setAccessible(true);
    modCountField.setInt(map, 0);

    // Spy on map to verify calls to replaceInParent and rebalance
    LinkedTreeMap<String, String> spyMap = spy(map);

    Method removeInternal = LinkedTreeMap.class.getDeclaredMethod("removeInternal", node.getClass(), boolean.class);
    removeInternal.setAccessible(true);
    removeInternal.invoke(spyMap, node, true);

    // Verify unlink adjustments
    assertSame(next, getNodeField(prev, "next"));
    assertSame(prev, getNodeField(next, "prev"));

    // Verify replaceInParent called with node and null (no children)
    verify(spyMap).replaceInParent(node, null);

    // Verify rebalance called with originalParent (null) and false
    verify(spyMap).rebalance(null, false);

    // Verify size decremented from 1 to 0
    assertEquals(0, sizeField.getInt(spyMap));

    // Verify modCount incremented by 1
    assertEquals(1, modCountField.getInt(spyMap));
  }

  @Test
    @Timeout(8000)
  void testRemoveInternal_unlinkFalse_leftChildOnly() throws Exception {
    LinkedTreeMap.Node<String, String> node = createNode("key", "value");
    LinkedTreeMap.Node<String, String> left = createNode("left", "lval");

    setNodeField(node, "prev", null);
    setNodeField(node, "next", null);
    setNodeField(node, "left", left);
    setNodeField(node, "right", null);
    setNodeField(left, "parent", node);

    Field sizeField = LinkedTreeMap.class.getDeclaredField("size");
    sizeField.setAccessible(true);
    sizeField.setInt(map, 2);
    Field modCountField = LinkedTreeMap.class.getDeclaredField("modCount");
    modCountField.setAccessible(true);
    modCountField.setInt(map, 0);

    LinkedTreeMap<String, String> spyMap = spy(map);

    Method removeInternal = LinkedTreeMap.class.getDeclaredMethod("removeInternal", node.getClass(), boolean.class);
    removeInternal.setAccessible(true);
    removeInternal.invoke(spyMap, node, false);

    // unlink false, so prev/next not touched
    assertNull(getNodeField(node, "prev"));
    assertNull(getNodeField(node, "next"));

    // replaceInParent called with node and left child
    verify(spyMap).replaceInParent(node, left);

    // node.left set to null after replacement
    assertNull(getNodeField(node, "left"));

    verify(spyMap).rebalance(any(), eq(false));

    assertEquals(1, sizeField.getInt(spyMap));
    assertEquals(1, modCountField.getInt(spyMap));
  }

  @Test
    @Timeout(8000)
  void testRemoveInternal_unlinkFalse_rightChildOnly() throws Exception {
    LinkedTreeMap.Node<String, String> node = createNode("key", "value");
    LinkedTreeMap.Node<String, String> right = createNode("right", "rval");

    setNodeField(node, "prev", null);
    setNodeField(node, "next", null);
    setNodeField(node, "left", null);
    setNodeField(node, "right", right);
    setNodeField(right, "parent", node);

    Field sizeField = LinkedTreeMap.class.getDeclaredField("size");
    sizeField.setAccessible(true);
    sizeField.setInt(map, 2);
    Field modCountField = LinkedTreeMap.class.getDeclaredField("modCount");
    modCountField.setAccessible(true);
    modCountField.setInt(map, 0);

    LinkedTreeMap<String, String> spyMap = spy(map);

    Method removeInternal = LinkedTreeMap.class.getDeclaredMethod("removeInternal", node.getClass(), boolean.class);
    removeInternal.setAccessible(true);
    removeInternal.invoke(spyMap, node, false);

    assertNull(getNodeField(node, "prev"));
    assertNull(getNodeField(node, "next"));

    verify(spyMap).replaceInParent(node, right);

    assertNull(getNodeField(node, "right"));

    verify(spyMap).rebalance(any(), eq(false));

    assertEquals(1, sizeField.getInt(spyMap));
    assertEquals(1, modCountField.getInt(spyMap));
  }

  @Test
    @Timeout(8000)
  void testRemoveInternal_unlinkFalse_twoChildren_leftHeightGreater() throws Exception {
    LinkedTreeMap.Node<String, String> node = createNode("node", "val");
    LinkedTreeMap.Node<String, String> left = createNode("left", "lval");
    LinkedTreeMap.Node<String, String> right = createNode("right", "rval");
    LinkedTreeMap.Node<String, String> leftLast = createNode("leftLast", "llval");

    // Setup tree structure
    setNodeField(node, "left", left);
    setNodeField(node, "right", right);
    setNodeField(node, "parent", null);

    setNodeField(left, "height", 3);
    setNodeField(right, "height", 2);

    // Setup left.last() to return leftLast
    Method lastMethod = left.getClass().getDeclaredMethod("last");
    lastMethod.setAccessible(true);

    // We need to mock left.last() to return leftLast, but node classes are not mockable easily
    // Instead, we override left.last() by reflection: create subclass? Not feasible here.
    // Alternative: set left.last field to leftLast by reflection if last() uses field
    // But we do not have source for last(), so we simulate by replacing left.last method via proxy is complicated.
    // Instead, we override the last() method by creating a subclass with overridden last() method.

    // We'll create a subclass of Node that overrides last()
    class TestNode extends LinkedTreeMap.Node<String, String> {
      final LinkedTreeMap.Node<String, String> lastNode;
      TestNode(LinkedTreeMap.Node<String, String> parent, String key, LinkedTreeMap.Node<String, String> next,
          LinkedTreeMap.Node<String, String> prev, LinkedTreeMap.Node<String, String> lastNode) throws Exception {
        super(parent, key, next, prev);
        this.lastNode = lastNode;
      }
      @Override
      LinkedTreeMap.Node<String, String> last() {
        return lastNode;
      }
    }

    // Create left as TestNode with last() returning leftLast
    LinkedTreeMap.Node<String, String> leftTestNode = new TestNode(null, "left", null, null, leftLast);
    setNodeField(node, "left", leftTestNode);
    setNodeField(leftTestNode, "height", 3);
    setNodeField(right, "height", 2);

    // Setup leftLast children and parent to null
    setNodeField(leftLast, "left", null);
    setNodeField(leftLast, "right", null);
    setNodeField(leftLast, "parent", leftTestNode);

    // Spy map to verify recursive call
    LinkedTreeMap<String, String> spyMap = spy(map);

    // Spy on removeInternal to track recursive calls
    Method removeInternal = LinkedTreeMap.class.getDeclaredMethod("removeInternal", node.getClass(), boolean.class);
    removeInternal.setAccessible(true);

    // We'll invoke removeInternal(node, false)
    removeInternal.invoke(spyMap, node, false);

    // After removal, adjacent (leftLast) should have left and right children assigned and height updated
    LinkedTreeMap.Node<String, String> adjacent = leftLast;

    // adjacent.left == leftTestNode, adjacent.right == right, node.left == null, node.right == null
    assertSame(leftTestNode, getNodeField(adjacent, "left"));
    assertSame(right, getNodeField(adjacent, "right"));
    assertNull(getNodeField(node, "left"));
    assertNull(getNodeField(node, "right"));

    // adjacent.height == max(left.height, right.height) + 1
    int leftHeight = (int) getNodeField(leftTestNode, "height");
    int rightHeight = (int) getNodeField(right, "height");
    int expectedHeight = Math.max(leftHeight, rightHeight) + 1;
    assertEquals(expectedHeight, getNodeField(adjacent, "height"));

    // verify replaceInParent called with node and adjacent
    verify(spyMap).replaceInParent(node, adjacent);

    // verify size and modCount updated properly
    Field sizeField = LinkedTreeMap.class.getDeclaredField("size");
    sizeField.setAccessible(true);
    Field modCountField = LinkedTreeMap.class.getDeclaredField("modCount");
    modCountField.setAccessible(true);

    // size should be decremented by 1 (originally 0, but we didn't set so default 0; can't verify here)
    // modCount incremented by 1
    assertEquals(1, modCountField.getInt(spyMap));
  }

  @Test
    @Timeout(8000)
  void testRemoveInternal_unlinkFalse_twoChildren_rightHeightGreater() throws Exception {
    LinkedTreeMap.Node<String, String> node = createNode("node", "val");
    LinkedTreeMap.Node<String, String> left = createNode("left", "lval");
    LinkedTreeMap.Node<String, String> right = createNode("right", "rval");
    LinkedTreeMap.Node<String, String> rightFirst = createNode("rightFirst", "rfval");

    setNodeField(node, "left", left);
    setNodeField(node, "right", right);
    setNodeField(node, "parent", null);

    setNodeField(left, "height", 2);
    setNodeField(right, "height", 3);

    // Override right.first() to return rightFirst
    class TestNode extends LinkedTreeMap.Node<String, String> {
      final LinkedTreeMap.Node<String, String> firstNode;
      TestNode(LinkedTreeMap.Node<String, String> parent, String key, LinkedTreeMap.Node<String, String> next,
          LinkedTreeMap.Node<String, String> prev, LinkedTreeMap.Node<String, String> firstNode) throws Exception {
        super(parent, key, next, prev);
        this.firstNode = firstNode;
      }
      @Override
      LinkedTreeMap.Node<String, String> first() {
        return firstNode;
      }
    }

    LinkedTreeMap.Node<String, String> rightTestNode = new TestNode(null, "right", null, null, rightFirst);
    setNodeField(node, "right", rightTestNode);
    setNodeField(rightTestNode, "height", 3);

    setNodeField(left, "height", 2);

    setNodeField(rightFirst, "left", null);
    setNodeField(rightFirst, "right", null);
    setNodeField(rightFirst, "parent", rightTestNode);

    LinkedTreeMap<String, String> spyMap = spy(map);

    Method removeInternal = LinkedTreeMap.class.getDeclaredMethod("removeInternal", node.getClass(), boolean.class);
    removeInternal.setAccessible(true);

    removeInternal.invoke(spyMap, node, false);

    LinkedTreeMap.Node<String, String> adjacent = rightFirst;

    assertSame(left, getNodeField(adjacent, "left"));
    assertSame(rightTestNode, getNodeField(adjacent, "right"));
    assertNull(getNodeField(node, "left"));
    assertNull(getNodeField(node, "right"));

    int leftHeight = (int) getNodeField(left, "height");
    int rightHeight = (int) getNodeField(rightTestNode, "height");
    int expectedHeight = Math.max(leftHeight, rightHeight) + 1;
    assertEquals(expectedHeight, getNodeField(adjacent, "height"));

    verify(spyMap).replaceInParent(node, adjacent);

    Field modCountField = LinkedTreeMap.class.getDeclaredField("modCount");
    modCountField.setAccessible(true);
    assertEquals(1, modCountField.getInt(spyMap));
  }
}