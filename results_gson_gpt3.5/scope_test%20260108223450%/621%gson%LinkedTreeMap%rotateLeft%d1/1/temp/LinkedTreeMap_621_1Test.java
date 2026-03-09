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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LinkedTreeMapRotateLeftTest {

  private LinkedTreeMap<Integer, String> map;

  // Inner Node class accessor
  private Class<?> nodeClass;

  @BeforeEach
  void setUp() throws Exception {
    map = new LinkedTreeMap<>();
    nodeClass = Class.forName("com.google.gson.internal.LinkedTreeMap$Node");
  }

  private Object createNode(Integer key, String value, Object parent, Object left, Object right, int height) throws Exception {
    // Node constructor signature: Node(LinkedTreeMap<K,V> map, K key, V value, Node<K,V> parent)
    // We will create node with null children and set them later by reflection.
    Object node = nodeClass.getDeclaredConstructor(LinkedTreeMap.class, Object.class, Object.class, nodeClass)
        .newInstance(map, key, value, parent);
    // Set left, right, height fields by reflection
    if (left != null) {
      nodeClass.getDeclaredField("left").set(node, left);
    }
    if (right != null) {
      nodeClass.getDeclaredField("right").set(node, right);
    }
    nodeClass.getDeclaredField("height").setInt(node, height);
    return node;
  }

  @Test
    @Timeout(8000)
  void testRotateLeft_balancesTreeProperly() throws Exception {
    // Create nodes involved in rotation:
    // root with left child and right child (pivot)
    // pivot has left and right children

    // Create leaf nodes for pivot's children
    Object pivotLeft = createNode(3, "pivotLeft", null, null, null, 1);
    Object pivotRight = createNode(5, "pivotRight", null, null, null, 2);

    // Create pivot node with pivotLeft and pivotRight children
    Object pivot = createNode(4, "pivot", null, pivotLeft, pivotRight, 3);
    // Set parent pointers of pivot's children
    nodeClass.getDeclaredField("parent").set(pivotLeft, pivot);
    nodeClass.getDeclaredField("parent").set(pivotRight, pivot);

    // Create left child of root
    Object left = createNode(1, "left", null, null, null, 4);

    // Create root node with left and pivot as children
    Object root = createNode(2, "root", null, left, pivot, 5);
    // Set parent pointers
    nodeClass.getDeclaredField("parent").set(left, root);
    nodeClass.getDeclaredField("parent").set(pivot, root);

    // We need to mock replaceInParent to track call and simulate parent replacement
    // Use spy on map to override replaceInParent
    LinkedTreeMap<Integer, String> spyMap = spy(map);

    // We will verify replaceInParent is called with root and pivot
    doAnswer(invocation -> {
      Object nodeArg = invocation.getArgument(0);
      Object replacementArg = invocation.getArgument(1);
      // Simulate replacement in parent: set replacementArg's parent to root's parent (null here)
      nodeClass.getDeclaredField("parent").set(replacementArg, null);
      return null;
    }).when(spyMap).replaceInParent(any(), any());

    // Access private rotateLeft method
    Method rotateLeft = LinkedTreeMap.class.getDeclaredMethod("rotateLeft", nodeClass);
    rotateLeft.setAccessible(true);

    // Invoke rotateLeft on root
    rotateLeft.invoke(spyMap, root);

    // After rotation:
    // pivot becomes root's parent (here no parent, so root replaced by pivot)
    // root.right should be pivotLeft
    Object rootRight = nodeClass.getDeclaredField("right").get(root);
    assertSame(pivotLeft, rootRight, "root.right should be pivotLeft after rotation");
    // pivot.left should be root
    Object pivotLeftChild = nodeClass.getDeclaredField("left").get(pivot);
    assertSame(root, pivotLeftChild, "pivot.left should be root after rotation");
    // root.parent should be pivot
    Object rootParent = nodeClass.getDeclaredField("parent").get(root);
    assertSame(pivot, rootParent, "root.parent should be pivot after rotation");
    // pivot.parent should be null (no parent for new root)
    Object pivotParent = nodeClass.getDeclaredField("parent").get(pivot);
    assertNull(pivotParent, "pivot.parent should be null after rotation");

    // Heights updated correctly
    int leftHeight = (int) nodeClass.getDeclaredField("height").get(left);
    int pivotLeftHeight = (int) nodeClass.getDeclaredField("height").get(pivotLeft);
    int pivotRightHeight = (int) nodeClass.getDeclaredField("height").get(pivotRight);
    int rootHeight = (int) nodeClass.getDeclaredField("height").get(root);
    int pivotHeight = (int) nodeClass.getDeclaredField("height").get(pivot);

    assertEquals(Math.max(leftHeight, pivotLeftHeight) + 1, rootHeight, "root.height updated correctly");
    assertEquals(Math.max(rootHeight, pivotRightHeight) + 1, pivotHeight, "pivot.height updated correctly");

    // Verify replaceInParent called exactly once with root and pivot
    verify(spyMap, times(1)).replaceInParent(root, pivot);
  }

  @Test
    @Timeout(8000)
  void testRotateLeft_withNullPivotLeft() throws Exception {
    // Test case where pivot.left is null

    // Create pivotRight leaf
    Object pivotRight = createNode(5, "pivotRight", null, null, null, 2);

    // Create pivot with null left and pivotRight right child
    Object pivot = createNode(4, "pivot", null, null, pivotRight, 3);
    nodeClass.getDeclaredField("parent").set(pivotRight, pivot);

    // Create left child of root
    Object left = createNode(1, "left", null, null, null, 4);

    // Create root with left and pivot children
    Object root = createNode(2, "root", null, left, pivot, 5);
    nodeClass.getDeclaredField("parent").set(left, root);
    nodeClass.getDeclaredField("parent").set(pivot, root);

    LinkedTreeMap<Integer, String> spyMap = spy(map);

    doAnswer(invocation -> {
      Object nodeArg = invocation.getArgument(0);
      Object replacementArg = invocation.getArgument(1);
      nodeClass.getDeclaredField("parent").set(replacementArg, null);
      return null;
    }).when(spyMap).replaceInParent(any(), any());

    Method rotateLeft = LinkedTreeMap.class.getDeclaredMethod("rotateLeft", nodeClass);
    rotateLeft.setAccessible(true);

    rotateLeft.invoke(spyMap, root);

    Object rootRight = nodeClass.getDeclaredField("right").get(root);
    assertNull(rootRight, "root.right should be null after rotation when pivot.left is null");

    Object pivotLeftChild = nodeClass.getDeclaredField("left").get(pivot);
    assertSame(root, pivotLeftChild, "pivot.left should be root after rotation");

    Object rootParent = nodeClass.getDeclaredField("parent").get(root);
    assertSame(pivot, rootParent, "root.parent should be pivot");

    Object pivotParent = nodeClass.getDeclaredField("parent").get(pivot);
    assertNull(pivotParent, "pivot.parent should be null");

    int leftHeight = (int) nodeClass.getDeclaredField("height").get(left);
    int rootHeight = (int) nodeClass.getDeclaredField("height").get(root);
    int pivotRightHeight = (int) nodeClass.getDeclaredField("height").get(pivotRight);
    int pivotHeight = (int) nodeClass.getDeclaredField("height").get(pivot);

    assertEquals(Math.max(leftHeight, 0) + 1, rootHeight, "root.height updated correctly with null pivotLeft");
    assertEquals(Math.max(rootHeight, pivotRightHeight) + 1, pivotHeight, "pivot.height updated correctly");

    verify(spyMap, times(1)).replaceInParent(root, pivot);
  }
}