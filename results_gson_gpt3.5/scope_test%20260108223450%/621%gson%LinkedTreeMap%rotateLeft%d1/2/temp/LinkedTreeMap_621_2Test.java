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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LinkedTreeMapRotateLeftTest {

  LinkedTreeMap<String, String> map;

  @BeforeEach
  void setUp() {
    map = new LinkedTreeMap<>();
  }

  @Test
    @Timeout(8000)
  void rotateLeft_rootWithRightChildAndPivotLeftNonNull_shouldRotateCorrectly() throws Exception {
    // Setup Nodes with reflection since Node is package-private static class inside LinkedTreeMap
    Class<?> nodeClass = null;
    for (Class<?> c : LinkedTreeMap.class.getDeclaredClasses()) {
      if ("Node".equals(c.getSimpleName())) {
        nodeClass = c;
        break;
      }
    }
    assertNotNull(nodeClass);

    // Create root Node
    Object root = createNode(nodeClass, "rootKey", "rootValue");

    // Create left child of root
    Object left = createNode(nodeClass, "leftKey", "leftValue");
    setField(root, "left", left);
    setField(left, "parent", root);

    // Create pivot (right child of root)
    Object pivot = createNode(nodeClass, "pivotKey", "pivotValue");
    setField(root, "right", pivot);
    setField(pivot, "parent", root);

    // Create pivotLeft (left child of pivot)
    Object pivotLeft = createNode(nodeClass, "pivotLeftKey", "pivotLeftValue");
    setField(pivot, "left", pivotLeft);
    setField(pivotLeft, "parent", pivot);

    // Create pivotRight (right child of pivot)
    Object pivotRight = createNode(nodeClass, "pivotRightKey", "pivotRightValue");
    setField(pivot, "right", pivotRight);
    setField(pivotRight, "parent", pivot);

    // Set heights
    setField(left, "height", 2);
    setField(pivotLeft, "height", 3);
    setField(pivotRight, "height", 4);
    setField(root, "height", 1);
    setField(pivot, "height", 1);

    // Spy on map to verify replaceInParent call
    LinkedTreeMap<String, String> spyMap = spy(map);

    // Use reflection to get rotateLeft method
    Method rotateLeft = LinkedTreeMap.class.getDeclaredMethod("rotateLeft", nodeClass);
    rotateLeft.setAccessible(true);

    // Use reflection to mock replaceInParent to verify invocation
    // Because replaceInParent takes Node<K,V> parameters, and root/pivot are Object,
    // we need to cast them to the Node type using a helper proxy method.
    // Mockito requires exact types, so we use doAnswer with reflection to bypass.
    doNothing().when(spyMap).replaceInParent(any(), any());
    // But to verify exact call with root and pivot, we use ArgumentMatchers.eq with casted values:
    // So instead of doNothing().when(spyMap).replaceInParent(root, pivot);
    // we do:
    //   doNothing().when(spyMap).replaceInParent((Node)root, (Node)pivot);
    // but Node is package-private, so we cannot cast directly.
    // Therefore, we use doAnswer and verify with custom matcher.

    // Invoke rotateLeft
    rotateLeft.invoke(spyMap, root);

    // Verify replaceInParent was called with correct args
    verify(spyMap).replaceInParent(root, pivot);

    // Assert root.right is pivotLeft and pivotLeft.parent is root
    assertSame(pivotLeft, getField(root, "right"));
    assertSame(root, getField(pivotLeft, "parent"));

    // Assert pivot.left is root and root.parent is pivot
    assertSame(root, getField(pivot, "left"));
    assertSame(pivot, getField(root, "parent"));

    // Assert heights updated correctly
    int expectedRootHeight = Math.max(getIntField(left, "height"), getIntField(pivotLeft, "height")) + 1;
    int expectedPivotHeight = Math.max(expectedRootHeight, getIntField(pivotRight, "height")) + 1;
    assertEquals(expectedRootHeight, getIntField(root, "height"));
    assertEquals(expectedPivotHeight, getIntField(pivot, "height"));
  }

  @Test
    @Timeout(8000)
  void rotateLeft_rootWithRightChildAndPivotLeftNull_shouldRotateCorrectly() throws Exception {
    Class<?> nodeClass = null;
    for (Class<?> c : LinkedTreeMap.class.getDeclaredClasses()) {
      if ("Node".equals(c.getSimpleName())) {
        nodeClass = c;
        break;
      }
    }
    assertNotNull(nodeClass);

    Object root = createNode(nodeClass, "rootKey", "rootValue");
    Object left = createNode(nodeClass, "leftKey", "leftValue");
    setField(root, "left", left);
    setField(left, "parent", root);

    Object pivot = createNode(nodeClass, "pivotKey", "pivotValue");
    setField(root, "right", pivot);
    setField(pivot, "parent", root);

    // pivotLeft is null
    setField(pivot, "left", null);

    Object pivotRight = createNode(nodeClass, "pivotRightKey", "pivotRightValue");
    setField(pivot, "right", pivotRight);
    setField(pivotRight, "parent", pivot);

    setField(left, "height", 2);
    setField(root, "height", 1);
    setField(pivot, "height", 1);
    setField(pivotRight, "height", 4);

    LinkedTreeMap<String, String> spyMap = spy(map);

    Method rotateLeft = LinkedTreeMap.class.getDeclaredMethod("rotateLeft", nodeClass);
    rotateLeft.setAccessible(true);

    doNothing().when(spyMap).replaceInParent(any(), any());

    rotateLeft.invoke(spyMap, root);

    verify(spyMap).replaceInParent(root, pivot);

    // root.right should be null (pivotLeft)
    assertNull(getField(root, "right"));

    // pivot.left is root and root.parent is pivot
    assertSame(root, getField(pivot, "left"));
    assertSame(pivot, getField(root, "parent"));

    int expectedRootHeight = Math.max(getIntField(left, "height"), 0) + 1;
    int expectedPivotHeight = Math.max(expectedRootHeight, getIntField(pivotRight, "height")) + 1;

    assertEquals(expectedRootHeight, getIntField(root, "height"));
    assertEquals(expectedPivotHeight, getIntField(pivot, "height"));
  }

  private Object createNode(Class<?> nodeClass, Object key, Object value) throws Exception {
    // Node constructor: Node<K,V>(K key, V value, Node<K,V> parent, Node<K,V> left, Node<K,V> right, int height)
    // But constructor is package-private, try to find one with (K key, V value, Node parent, Node left, Node right, int height)
    for (Constructor<?> ctor : nodeClass.getDeclaredConstructors()) {
      if (ctor.getParameterCount() == 6) {
        ctor.setAccessible(true);
        return ctor.newInstance(key, value, null, null, null, 1);
      }
    }
    throw new IllegalStateException("No suitable Node constructor found");
  }

  private void setField(Object obj, String fieldName, Object value) throws Exception {
    Field field = obj.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(obj, value);
  }

  private Object getField(Object obj, String fieldName) throws Exception {
    Field field = obj.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(obj);
  }

  private int getIntField(Object obj, String fieldName) throws Exception {
    Field field = obj.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.getInt(obj);
  }
}