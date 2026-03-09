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

  @Test
    @Timeout(8000)
  void rotateRight_nullPivotRight() throws Exception {
    // Setup root and pivot nodes
    Node<String, String> root = new Node<>("root", "rootValue");
    Node<String, String> pivot = new Node<>("pivot", "pivotValue");
    Node<String, String> pivotLeft = new Node<>("pivotLeft", "pivotLeftValue");
    Node<String, String> right = new Node<>("right", "rightValue");

    // Build tree structure:
    // root.left = pivot
    // root.right = right
    // pivot.left = pivotLeft
    // pivot.right = null (test null pivotRight branch)
    root.left = pivot;
    root.right = right;
    pivot.left = pivotLeft;
    pivot.right = null;

    // Set parents
    pivot.parent = root;
    right.parent = root;
    pivotLeft.parent = pivot;

    // Set heights for calculation
    right.height = 3;
    pivotLeft.height = 2;
    pivot.height = 4;
    root.height = 5;

    // Inject Node class instance into LinkedTreeMap for testing
    // Use reflection to call private rotateRight(Node<K,V>)
    Method rotateRight = LinkedTreeMap.class.getDeclaredMethod("rotateRight", Node.class);
    rotateRight.setAccessible(true);

    // Replace replaceInParent(Node, Node) to update parent references correctly
    // We mock this method by reflection to avoid errors
    // But since it's private, we can spy on map and stub the method
    LinkedTreeMap<String, String> spyMap = spy(map);
    doAnswer(invocation -> {
      Node<String, String> node = invocation.getArgument(0);
      Node<String, String> replacement = invocation.getArgument(1);
      Node<String, String> parent = node.parent;
      if (parent == null) {
        // root replacement
        Field rootField = LinkedTreeMap.class.getDeclaredField("root");
        rootField.setAccessible(true);
        rootField.set(spyMap, replacement);
        if (replacement != null) {
          replacement.parent = null;
        }
      } else if (parent.left == node) {
        parent.left = replacement;
        if (replacement != null) {
          replacement.parent = parent;
        }
      } else if (parent.right == node) {
        parent.right = replacement;
        if (replacement != null) {
          replacement.parent = parent;
        }
      }
      return null;
    }).when(spyMap).replaceInParent(any(), any());

    // Use reflection to invoke rotateRight on spyMap
    Method rotateRightOnSpy = LinkedTreeMap.class.getDeclaredMethod("rotateRight", Node.class);
    rotateRightOnSpy.setAccessible(true);

    // Set spyMap.root to root node so replaceInParent can update root if needed
    Field rootField = LinkedTreeMap.class.getDeclaredField("root");
    rootField.setAccessible(true);
    rootField.set(spyMap, root);

    rotateRightOnSpy.invoke(spyMap, root);

    // After rotation, pivot should be new root
    Node<String, String> newRoot = (Node<String, String>) rootField.get(spyMap);
    assertSame(pivot, newRoot);
    assertSame(root, pivot.right);
    assertNull(root.left);
    assertSame(pivotLeft, pivot.left);
    assertSame(root, root.parent);
    assertSame(pivot, pivot.parent);
    assertSame(right, root.right);
    assertEquals(
        Math.max(right.height, 0) + 1,
        root.height,
        "root height should be max of right and pivotRight plus 1");
    assertEquals(
        Math.max(root.height, pivotLeft.height) + 1,
        pivot.height,
        "pivot height should be max of root and pivotLeft plus 1");
  }

  @Test
    @Timeout(8000)
  void rotateRight_nonNullPivotRight() throws Exception {
    Node<String, String> root = new Node<>("root", "rootValue");
    Node<String, String> pivot = new Node<>("pivot", "pivotValue");
    Node<String, String> pivotLeft = new Node<>("pivotLeft", "pivotLeftValue");
    Node<String, String> pivotRight = new Node<>("pivotRight", "pivotRightValue");
    Node<String, String> right = new Node<>("right", "rightValue");

    root.left = pivot;
    root.right = right;
    pivot.left = pivotLeft;
    pivot.right = pivotRight;

    pivot.parent = root;
    right.parent = root;
    pivotLeft.parent = pivot;
    pivotRight.parent = pivot;

    right.height = 3;
    pivotLeft.height = 2;
    pivotRight.height = 4;
    pivot.height = 5;
    root.height = 6;

    LinkedTreeMap<String, String> spyMap = spy(map);
    doAnswer(invocation -> {
      Node<String, String> node = invocation.getArgument(0);
      Node<String, String> replacement = invocation.getArgument(1);
      Node<String, String> parent = node.parent;
      if (parent == null) {
        Field rootField = LinkedTreeMap.class.getDeclaredField("root");
        rootField.setAccessible(true);
        rootField.set(spyMap, replacement);
        if (replacement != null) {
          replacement.parent = null;
        }
      } else if (parent.left == node) {
        parent.left = replacement;
        if (replacement != null) {
          replacement.parent = parent;
        }
      } else if (parent.right == node) {
        parent.right = replacement;
        if (replacement != null) {
          replacement.parent = parent;
        }
      }
      return null;
    }).when(spyMap).replaceInParent(any(), any());

    Field rootField = LinkedTreeMap.class.getDeclaredField("root");
    rootField.setAccessible(true);
    rootField.set(spyMap, root);

    Method rotateRightOnSpy = LinkedTreeMap.class.getDeclaredMethod("rotateRight", Node.class);
    rotateRightOnSpy.setAccessible(true);

    rotateRightOnSpy.invoke(spyMap, root);

    Node<String, String> newRoot = (Node<String, String>) rootField.get(spyMap);
    assertSame(pivot, newRoot);
    assertSame(root, pivot.right);
    assertSame(pivotRight, root.left);
    assertSame(pivotLeft, pivot.left);
    assertSame(root, root.parent);
    assertSame(pivot, pivot.parent);
    assertSame(right, root.right);
    assertSame(pivot, pivotRight.parent);

    assertEquals(
        Math.max(right.height, pivotRight.height) + 1,
        root.height,
        "root height should be max of right and pivotRight plus 1");
    assertEquals(
        Math.max(root.height, pivotLeft.height) + 1,
        pivot.height,
        "pivot height should be max of root and pivotLeft plus 1");
  }
}