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

class LinkedTreeMap_RotateLeftTest {

  private LinkedTreeMap<String, String> map;

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
  void rotateLeft_balancesCorrectly() throws Exception {
    // Create nodes to simulate tree structure:
    //        root
    //       /    \
    //    left   pivot
    //           /    \
    //    pivotLeft  pivotRight

    Node<String, String> root = new Node<>("root", "r");
    Node<String, String> left = new Node<>("left", "l");
    Node<String, String> pivot = new Node<>("pivot", "p");
    Node<String, String> pivotLeft = new Node<>("pivotLeft", "pl");
    Node<String, String> pivotRight = new Node<>("pivotRight", "pr");

    // Setup initial links
    root.left = left;
    root.right = pivot;
    left.parent = root;
    pivot.parent = root;
    pivot.left = pivotLeft;
    pivot.right = pivotRight;
    pivotLeft.parent = pivot;
    pivotRight.parent = pivot;

    // Heights setup
    left.height = 2;
    pivotLeft.height = 3;
    pivotRight.height = 4;
    root.height = 5;
    pivot.height = 6;

    // Inject Node class into LinkedTreeMap via reflection
    Field rootField = LinkedTreeMap.class.getDeclaredField("root");
    rootField.setAccessible(true);
    rootField.set(map, root);

    // ReplaceInParent is private, mock it by spy on map and override via reflection
    LinkedTreeMap<String, String> spyMap = spy(map);

    // Use doAnswer to simulate replaceInParent behavior
    Method replaceInParentMethod = LinkedTreeMap.class.getDeclaredMethod("replaceInParent", Object.class, Object.class);
    replaceInParentMethod.setAccessible(true);

    // Because replaceInParent is private and we cannot mock private methods easily,
    // we invoke rotateLeft on spyMap and rely on actual replaceInParent implementation.
    // Alternatively, we can call rotateLeft via reflection on spyMap.

    Method rotateLeftMethod = LinkedTreeMap.class.getDeclaredMethod("rotateLeft", Object.class);
    rotateLeftMethod.setAccessible(true);

    // Invoke rotateLeft
    rotateLeftMethod.invoke(spyMap, root);

    // After rotation:
    // pivot should be new root in parent position of root
    // root should be pivot.left
    // root.right should be pivotLeft
    // heights should be updated

    // Validate parent links
    assertNull(pivot.parent, "pivot parent should be null after replaceInParent");
    assertSame(pivot, root.parent, "root.parent should be pivot");
    assertSame(root, pivot.left, "pivot.left should be root");
    assertSame(pivotLeft, root.right, "root.right should be pivotLeft");
    if (pivotLeft != null) {
      assertSame(root, pivotLeft.parent, "pivotLeft.parent should be root");
    }

    // Validate heights
    int expectedRootHeight = Math.max(left.height, pivotLeft.height) + 1;
    int expectedPivotHeight = Math.max(expectedRootHeight, pivotRight.height) + 1;
    assertEquals(expectedRootHeight, root.height, "root height should be updated correctly");
    assertEquals(expectedPivotHeight, pivot.height, "pivot height should be updated correctly");
  }

  @Test
    @Timeout(8000)
  void rotateLeft_handlesNullPivotLeft() throws Exception {
    // Setup nodes with pivot.left == null
    Node<String, String> root = new Node<>("root", "r");
    Node<String, String> left = new Node<>("left", "l");
    Node<String, String> pivot = new Node<>("pivot", "p");
    Node<String, String> pivotRight = new Node<>("pivotRight", "pr");

    root.left = left;
    root.right = pivot;
    left.parent = root;
    pivot.parent = root;
    pivot.left = null;
    pivot.right = pivotRight;
    pivotRight.parent = pivot;

    left.height = 2;
    pivotRight.height = 4;
    root.height = 5;
    pivot.height = 6;

    Field rootField = LinkedTreeMap.class.getDeclaredField("root");
    rootField.setAccessible(true);
    rootField.set(map, root);

    Method rotateLeftMethod = LinkedTreeMap.class.getDeclaredMethod("rotateLeft", Object.class);
    rotateLeftMethod.setAccessible(true);

    rotateLeftMethod.invoke(map, root);

    // After rotation:
    // pivot.left == root
    // root.right == null
    // root.parent == pivot
    // heights updated accordingly

    assertNull(root.right, "root.right should be null after rotation");
    assertSame(pivot, root.parent, "root.parent should be pivot");
    assertSame(root, pivot.left, "pivot.left should be root");

    int expectedRootHeight = Math.max(left.height, 0) + 1;
    int expectedPivotHeight = Math.max(expectedRootHeight, pivotRight.height) + 1;
    assertEquals(expectedRootHeight, root.height, "root height updated correctly");
    assertEquals(expectedPivotHeight, pivot.height, "pivot height updated correctly");
  }

  @Test
    @Timeout(8000)
  void rotateLeft_handlesNullLeftChild() throws Exception {
    // Setup nodes with root.left == null
    Node<String, String> root = new Node<>("root", "r");
    Node<String, String> pivot = new Node<>("pivot", "p");
    Node<String, String> pivotLeft = new Node<>("pivotLeft", "pl");
    Node<String, String> pivotRight = new Node<>("pivotRight", "pr");

    root.left = null;
    root.right = pivot;
    pivot.parent = root;
    pivot.left = pivotLeft;
    pivot.right = pivotRight;
    pivotLeft.parent = pivot;
    pivotRight.parent = pivot;

    pivotLeft.height = 3;
    pivotRight.height = 4;
    root.height = 5;
    pivot.height = 6;

    Field rootField = LinkedTreeMap.class.getDeclaredField("root");
    rootField.setAccessible(true);
    rootField.set(map, root);

    Method rotateLeftMethod = LinkedTreeMap.class.getDeclaredMethod("rotateLeft", Object.class);
    rotateLeftMethod.setAccessible(true);

    rotateLeftMethod.invoke(map, root);

    assertSame(pivotLeft, root.right, "root.right should be pivotLeft");
    if (pivotLeft != null) {
      assertSame(root, pivotLeft.parent, "pivotLeft.parent should be root");
    }
    assertSame(pivot, root.parent, "root.parent should be pivot");
    assertSame(root, pivot.left, "pivot.left should be root");

    int expectedRootHeight = Math.max(0, pivotLeft.height) + 1;
    int expectedPivotHeight = Math.max(expectedRootHeight, pivotRight.height) + 1;
    assertEquals(expectedRootHeight, root.height, "root height updated correctly");
    assertEquals(expectedPivotHeight, pivot.height, "pivot height updated correctly");
  }
}