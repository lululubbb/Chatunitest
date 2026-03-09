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

import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LinkedTreeMap_rotateLeft_Test {

  LinkedTreeMap<String, String> map;

  static class Node<K, V> {
    K key;
    V value;
    Node<K, V> left;
    Node<K, V> right;
    Node<K, V> parent;
    int height;

    Node(K key, V value) {
      this.key = key;
      this.value = value;
      this.height = 1;
    }
  }

  @BeforeEach
  void setUp() {
    map = new LinkedTreeMap<>();
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private Object invokeReplaceInParent(Object mapInstance, Object node, Object replacement) throws Exception {
    Method replaceInParentMethod = LinkedTreeMap.class.getDeclaredMethod("replaceInParent", LinkedTreeMap.Node.class, LinkedTreeMap.Node.class);
    replaceInParentMethod.setAccessible(true);
    return replaceInParentMethod.invoke(mapInstance, node, replacement);
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private Object invokeRotateLeft(Object mapInstance, Object node) throws Exception {
    Method rotateLeftMethod = LinkedTreeMap.class.getDeclaredMethod("rotateLeft", LinkedTreeMap.Node.class);
    rotateLeftMethod.setAccessible(true);
    return rotateLeftMethod.invoke(mapInstance, node);
  }

  @Test
    @Timeout(8000)
  void rotateLeft_shouldRotateProperly() throws Exception {
    // Create nodes to form the tree structure needed for rotateLeft
    LinkedTreeMap.Node<String, String> root = new LinkedTreeMap.Node<>("root", "r");
    LinkedTreeMap.Node<String, String> left = new LinkedTreeMap.Node<>("left", "l");
    LinkedTreeMap.Node<String, String> pivot = new LinkedTreeMap.Node<>("pivot", "p");
    LinkedTreeMap.Node<String, String> pivotLeft = new LinkedTreeMap.Node<>("pivotLeft", "pl");
    LinkedTreeMap.Node<String, String> pivotRight = new LinkedTreeMap.Node<>("pivotRight", "pr");

    // Setup relations
    root.left = left;
    root.right = pivot;
    left.parent = root;
    pivot.parent = root;
    pivot.left = pivotLeft;
    pivot.right = pivotRight;
    pivotLeft.parent = pivot;
    pivotRight.parent = pivot;

    // Set heights to test height recalculation
    left.height = 2;
    pivotLeft.height = 3;
    pivotRight.height = 4;
    root.height = 5;
    pivot.height = 6;

    // Use reflection to call rotateLeft
    invokeRotateLeft(map, root);

    // Verify structure after rotation:
    // pivot should replace root as parent's child
    // root should be left child of pivot
    // root.right should be pivotLeft
    // pivotLeft.parent should be root
    assertSame(pivot.left, root);
    assertSame(root.parent, pivot);
    assertSame(root.right, pivotLeft);
    assertSame(pivotLeft.parent, root);

    // Heights should be updated correctly
    int expectedRootHeight = Math.max(left.height, pivotLeft.height) + 1;
    int expectedPivotHeight = Math.max(expectedRootHeight, pivotRight.height) + 1;
    assertEquals(expectedRootHeight, root.height);
    assertEquals(expectedPivotHeight, pivot.height);
  }

  @Test
    @Timeout(8000)
  void rotateLeft_withNullPivotLeft_shouldHandleNull() throws Exception {
    LinkedTreeMap.Node<String, String> root = new LinkedTreeMap.Node<>("root", "r");
    LinkedTreeMap.Node<String, String> left = new LinkedTreeMap.Node<>("left", "l");
    LinkedTreeMap.Node<String, String> pivot = new LinkedTreeMap.Node<>("pivot", "p");
    LinkedTreeMap.Node<String, String> pivotRight = new LinkedTreeMap.Node<>("pivotRight", "pr");

    root.left = left;
    root.right = pivot;
    left.parent = root;
    pivot.parent = root;
    pivot.left = null;
    pivot.right = pivotRight;
    pivotRight.parent = pivot;

    left.height = 3;
    pivotRight.height = 4;
    root.height = 5;
    pivot.height = 6;

    invokeRotateLeft(map, root);

    // pivot.left should be root
    assertSame(pivot.left, root);
    // root.parent should be pivot
    assertSame(root.parent, pivot);
    // root.right should be null since pivot.left is null
    assertNull(root.right);

    int expectedRootHeight = Math.max(left.height, 0) + 1;
    int expectedPivotHeight = Math.max(expectedRootHeight, pivotRight.height) + 1;
    assertEquals(expectedRootHeight, root.height);
    assertEquals(expectedPivotHeight, pivot.height);
  }

  @Test
    @Timeout(8000)
  void rotateLeft_withNullLeft_shouldHandleNull() throws Exception {
    LinkedTreeMap.Node<String, String> root = new LinkedTreeMap.Node<>("root", "r");
    LinkedTreeMap.Node<String, String> pivot = new LinkedTreeMap.Node<>("pivot", "p");
    LinkedTreeMap.Node<String, String> pivotLeft = new LinkedTreeMap.Node<>("pivotLeft", "pl");
    LinkedTreeMap.Node<String, String> pivotRight = new LinkedTreeMap.Node<>("pivotRight", "pr");

    root.left = null;
    root.right = pivot;
    pivot.parent = root;
    pivot.left = pivotLeft;
    pivot.right = pivotRight;
    pivotLeft.parent = pivot;
    pivotRight.parent = pivot;

    pivotLeft.height = 2;
    pivotRight.height = 3;
    root.height = 4;
    pivot.height = 5;

    invokeRotateLeft(map, root);

    assertSame(pivot.left, root);
    assertSame(root.parent, pivot);
    assertSame(root.right, pivotLeft);
    assertSame(pivotLeft.parent, root);

    int expectedRootHeight = Math.max(0, pivotLeft.height) + 1;
    int expectedPivotHeight = Math.max(expectedRootHeight, pivotRight.height) + 1;
    assertEquals(expectedRootHeight, root.height);
    assertEquals(expectedPivotHeight, pivot.height);
  }
}