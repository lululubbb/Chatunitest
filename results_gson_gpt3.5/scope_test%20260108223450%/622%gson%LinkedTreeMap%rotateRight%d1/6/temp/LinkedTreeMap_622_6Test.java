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

class LinkedTreeMapRotateRightTest {

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
    }
  }

  @BeforeEach
  void setUp() {
    map = new LinkedTreeMap<>();
  }

  @SuppressWarnings("unchecked")
  @Test
    @Timeout(8000)
  void testRotateRight() throws Exception {
    // Create nodes manually to form a subtree to rotate
    Node<String, String> root = new Node<>("root", "rootVal");
    Node<String, String> pivot = new Node<>("pivot", "pivotVal");
    Node<String, String> pivotLeft = new Node<>("pivotLeft", "pivotLeftVal");
    Node<String, String> pivotRight = new Node<>("pivotRight", "pivotRightVal");
    Node<String, String> right = new Node<>("right", "rightVal");

    // Set up tree structure:
    //        root
    //       /    \
    //    pivot   right
    //    /    \
    // pivotLeft pivotRight

    root.left = pivot;
    root.right = right;
    pivot.left = pivotLeft;
    pivot.right = pivotRight;

    // Set parents
    pivot.parent = root;
    right.parent = root;
    pivotLeft.parent = pivot;
    pivotRight.parent = pivot;

    // Set heights to test height recalculation
    pivotLeft.height = 1;
    pivotRight.height = 2;
    right.height = 3;
    pivot.height = 4;
    root.height = 5;

    // Inject the Node class into LinkedTreeMap (reflection)
    Field rootField = LinkedTreeMap.class.getDeclaredField("root");
    rootField.setAccessible(true);
    rootField.set(map, root);

    // Use reflection to access private rotateRight
    Method rotateRightMethod = LinkedTreeMap.class.getDeclaredMethod("rotateRight", root.getClass());
    rotateRightMethod.setAccessible(true);

    // Spy on the LinkedTreeMap instance
    LinkedTreeMap<String, String> spyMap = spy(map);

    // Replace the root in spyMap as well
    rootField.set(spyMap, root);

    // Call rotateRight via reflection on spyMap (cannot mock private method directly)
    rotateRightMethod.invoke(spyMap, root);

    // After rotation:
    // pivot should be new root of subtree
    // pivot.right == root
    // root.left == pivotRight
    // Parents updated accordingly

    // Check pivot is now parent of root (root.parent == pivot)
    assertSame(pivot, root.parent);

    // Check root.left is pivotRight
    assertSame(pivotRight, root.left);

    // Check pivot.right is root
    assertSame(root, pivot.right);

    // Check pivot.parent is root's old parent (should be null here because root was root)
    // Since root was root of tree, pivot should now be root of whole tree (parent null)
    assertNull(pivot.parent);

    // Check pivotRight parent is root
    assertSame(root, pivotRight.parent);

    // Heights recalculated correctly:
    int expectedRootHeight = Math.max(right.height, pivotRight.height) + 1;
    int expectedPivotHeight = Math.max(expectedRootHeight, pivotLeft.height) + 1;
    assertEquals(expectedRootHeight, root.height);
    assertEquals(expectedPivotHeight, pivot.height);
  }

  @SuppressWarnings("unchecked")
  private LinkedTreeMap.Node<String, String> createNode(String key, String value) throws Exception {
    Class<?> nodeClass = Class.forName("com.google.gson.internal.LinkedTreeMap$Node");
    LinkedTreeMap.Node<String, String> node = (LinkedTreeMap.Node<String, String>) nodeClass
        .getConstructor(Object.class, Object.class).newInstance(key, value);
    return node;
  }
}