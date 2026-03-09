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

class LinkedTreeMap_rotateRight_Test {

  private LinkedTreeMap<String, String> map;

  // Helper method to create Node instances via reflection
  private LinkedTreeMap.Node<String, String> createNode(String key, String value, int height) throws Exception {
    Class<?> nodeClass = Class.forName("com.google.gson.internal.LinkedTreeMap$Node");
    // Node constructor: Node(LinkedTreeMap, K, V, Node<K,V>)
    // We pass null for parent in constructor, will set manually later
    var ctor = nodeClass.getDeclaredConstructor(LinkedTreeMap.class, Object.class, Object.class, nodeClass);
    ctor.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedTreeMap.Node<String, String> node = (LinkedTreeMap.Node<String, String>) ctor.newInstance(map, key, value, null);
    // set height field
    Field heightField = nodeClass.getDeclaredField("height");
    heightField.setAccessible(true);
    heightField.setInt(node, height);
    return node;
  }

  @BeforeEach
  void setUp() {
    map = new LinkedTreeMap<>();
  }

  @Test
    @Timeout(8000)
  void rotateRight_balancedTree_correctRotationAndHeight() throws Exception {
    // Setup nodes to simulate a tree:
    //       root
    //      /    \
    //   pivot   right
    //   /   \
    // pl    pr
    // Create root node
    var root = createNode("root", "r", 3);
    var pivot = createNode("pivot", "p", 2);
    var right = createNode("right", "ri", 1);
    var pivotLeft = createNode("pl", "plv", 1);
    var pivotRight = createNode("pr", "prv", 1);

    // Setup relationships
    setField(root, "left", pivot);
    setField(root, "right", right);
    setField(pivot, "left", pivotLeft);
    setField(pivot, "right", pivotRight);

    setField(pivot, "parent", root);
    setField(right, "parent", root);
    setField(pivotLeft, "parent", pivot);
    setField(pivotRight, "parent", pivot);

    // set map root to root node
    setField(map, "root", root);

    // Spy map to verify replaceInParent call
    LinkedTreeMap<String, String> spyMap = spy(map);

    // Use reflection to invoke private rotateRight
    Method rotateRight = LinkedTreeMap.class.getDeclaredMethod("rotateRight", Class.forName("com.google.gson.internal.LinkedTreeMap$Node"));
    rotateRight.setAccessible(true);

    // Use reflection to spy and stub private replaceInParent to do nothing (to avoid IllegalAccessError)
    Method replaceInParent = LinkedTreeMap.class.getDeclaredMethod("replaceInParent", Class.forName("com.google.gson.internal.LinkedTreeMap$Node"), Class.forName("com.google.gson.internal.LinkedTreeMap$Node"));
    replaceInParent.setAccessible(true);
    doAnswer(invocation -> null).when(spyMap).getClass().getDeclaredMethod("replaceInParent", Class.forName("com.google.gson.internal.LinkedTreeMap$Node"), Class.forName("com.google.gson.internal.LinkedTreeMap$Node"));

    // Instead of verify(spyMap).replaceInParent(root, pivot);
    // We invoke replaceInParent via reflection and verify it was called by spying on spyMap's method calls using a custom InvocationHandler
    // But it's complicated, so we skip verify on private method and only check state after rotation

    rotateRight.invoke(spyMap, root);

    // After rotation, pivot should be root of subtree, root becomes right child of pivot
    // Check parent relationship
    assertSame(pivot, getField(root, "parent"));
    assertSame(root, getField(pivot, "right"));

    // Check that root's left is pivotRight (pr)
    assertSame(pivotRight, getField(root, "left"));
    // pivotRight's parent is root
    assertSame(root, getField(pivotRight, "parent"));

    // Check heights updated correctly
    int rootHeight = (int) getField(root, "height");
    int pivotHeight = (int) getField(pivot, "height");

    int expectedRootHeight = Math.max(
        getHeight(right),
        getHeight(pivotRight)
    ) + 1;
    int expectedPivotHeight = Math.max(
        expectedRootHeight,
        getHeight(pivotLeft)
    ) + 1;

    assertEquals(expectedRootHeight, rootHeight);
    assertEquals(expectedPivotHeight, pivotHeight);
  }

  @Test
    @Timeout(8000)
  void rotateRight_nullPivotRight_noParentSet() throws Exception {
    // Setup nodes:
    //     root
    //    /
    //  pivot
    //  /    
    // pl
    var root = createNode("root", "r", 2);
    var pivot = createNode("pivot", "p", 2);
    var pivotLeft = createNode("pl", "plv", 1);

    setField(root, "left", pivot);
    setField(root, "right", null);
    setField(pivot, "left", pivotLeft);
    setField(pivot, "right", null);

    setField(pivot, "parent", root);
    setField(pivotLeft, "parent", pivot);

    setField(map, "root", root);

    LinkedTreeMap<String, String> spyMap = spy(map);

    Method rotateRight = LinkedTreeMap.class.getDeclaredMethod("rotateRight", Class.forName("com.google.gson.internal.LinkedTreeMap$Node"));
    rotateRight.setAccessible(true);

    rotateRight.invoke(spyMap, root);

    // root.left should be pivot.right which is null
    assertNull(getField(root, "left"));

    // No NullPointerException, pivotRight parent not set
    // pivot.right = root
    assertSame(root, getField(pivot, "right"));
    assertSame(pivot, getField(root, "parent"));

    // Heights updated correctly
    int rootHeight = (int) getField(root, "height");
    int pivotHeight = (int) getField(pivot, "height");

    int expectedRootHeight = Math.max(
        0, // root.right is null
        0  // pivotRight is null
    ) + 1;
    int expectedPivotHeight = Math.max(
        expectedRootHeight,
        getHeight(pivotLeft)
    ) + 1;

    assertEquals(expectedRootHeight, rootHeight);
    assertEquals(expectedPivotHeight, pivotHeight);
  }

  // Helper to get private field value
  private Object getField(Object obj, String fieldName) throws Exception {
    Field f = obj.getClass().getDeclaredField(fieldName);
    f.setAccessible(true);
    return f.get(obj);
  }

  // Helper to set private field value
  private void setField(Object obj, String fieldName, Object value) throws Exception {
    Field f = obj.getClass().getDeclaredField(fieldName);
    f.setAccessible(true);
    f.set(obj, value);
  }

  // Helper to get height field from Node or 0 if null
  private int getHeight(Object node) throws Exception {
    if (node == null) return 0;
    Field heightField = node.getClass().getDeclaredField("height");
    heightField.setAccessible(true);
    return heightField.getInt(node);
  }
}