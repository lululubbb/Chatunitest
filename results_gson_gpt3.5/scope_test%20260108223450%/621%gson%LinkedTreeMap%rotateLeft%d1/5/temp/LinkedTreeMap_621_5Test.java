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

class LinkedTreeMapRotateLeftTest {

  private LinkedTreeMap<String, String> map;

  @BeforeEach
  void setUp() {
    // Use the no-arg constructor, assuming it exists
    map = new LinkedTreeMap<>();
  }

  @Test
    @Timeout(8000)
  void rotateLeft_balancedNodes_shouldRotateCorrectly() throws Exception {
    // Create nodes manually via reflection
    Class<?> nodeClass = null;
    for (Class<?> inner : LinkedTreeMap.class.getDeclaredClasses()) {
      if ("Node".equals(inner.getSimpleName())) {
        nodeClass = inner;
        break;
      }
    }
    assertNotNull(nodeClass, "Node class not found");

    // Node constructor signature: Node(LinkedTreeMap, K, V, Node<K,V>, Node<K,V>)
    // We will create nodes with parent=null, left=null, right=null initially

    // Create root node
    Object root = nodeClass.getDeclaredConstructor(LinkedTreeMap.class, Object.class, Object.class, nodeClass, nodeClass)
        .newInstance(map, "root", "rootVal", null, null);
    setField(nodeClass, root, "height", 2);

    // Create left child node of root
    Object left = nodeClass.getDeclaredConstructor(LinkedTreeMap.class, Object.class, Object.class, nodeClass, nodeClass)
        .newInstance(map, "left", "leftVal", null, null);
    setField(nodeClass, left, "height", 1);
    setField(nodeClass, left, "parent", root);

    // Create pivot node (root.right)
    Object pivot = nodeClass.getDeclaredConstructor(LinkedTreeMap.class, Object.class, Object.class, nodeClass, nodeClass)
        .newInstance(map, "pivot", "pivotVal", null, null);
    setField(nodeClass, pivot, "height", 3);
    setField(nodeClass, pivot, "parent", null);

    // Create pivotLeft child
    Object pivotLeft = nodeClass.getDeclaredConstructor(LinkedTreeMap.class, Object.class, Object.class, nodeClass, nodeClass)
        .newInstance(map, "pivotLeft", "pivotLeftVal", null, null);
    setField(nodeClass, pivotLeft, "height", 1);
    setField(nodeClass, pivotLeft, "parent", pivot);

    // Create pivotRight child
    Object pivotRight = nodeClass.getDeclaredConstructor(LinkedTreeMap.class, Object.class, Object.class, nodeClass, nodeClass)
        .newInstance(map, "pivotRight", "pivotRightVal", null, null);
    setField(nodeClass, pivotRight, "height", 2);
    setField(nodeClass, pivotRight, "parent", pivot);

    // Setup root's children
    setField(nodeClass, root, "left", left);
    setField(nodeClass, root, "right", pivot);
    setField(nodeClass, left, "parent", root);

    // Setup pivot's children
    setField(nodeClass, pivot, "left", pivotLeft);
    setField(nodeClass, pivot, "right", pivotRight);

    // Setup parents
    setField(nodeClass, pivotLeft, "parent", pivot);
    setField(nodeClass, pivotRight, "parent", pivot);

    // Set root's parent to null (top of subtree)
    setField(nodeClass, root, "parent", null);

    // Set map.root to root to simulate tree root
    Field rootField = LinkedTreeMap.class.getDeclaredField("root");
    rootField.setAccessible(true);
    rootField.set(map, root);

    // Spy on map to verify replaceInParent is called correctly
    LinkedTreeMap<String, String> spyMap = spy(map);

    // Use reflection to invoke private rotateLeft
    Method rotateLeft = LinkedTreeMap.class.getDeclaredMethod("rotateLeft", nodeClass);
    rotateLeft.setAccessible(true);
    rotateLeft.invoke(spyMap, root);

    // After rotation, pivot should be in root's place
    // root's right should be pivotLeft
    assertSame(pivotLeft, getField(nodeClass, root, "right"));
    // pivotLeft's parent should be root
    assertSame(root, getField(nodeClass, pivotLeft, "parent"));
    // pivot's left should be root
    assertSame(root, getField(nodeClass, pivot, "left"));
    // root's parent should be pivot
    assertSame(pivot, getField(nodeClass, root, "parent"));

    // Heights updated correctly
    int rootHeight = (int) getField(nodeClass, root, "height");
    int pivotHeight = (int) getField(nodeClass, pivot, "height");
    int expectedRootHeight = Math.max(1, 1) + 1; // left.height=1, pivotLeft.height=1
    int expectedPivotHeight = Math.max(expectedRootHeight, 2) + 1; // pivotRight.height=2

    assertEquals(expectedRootHeight, rootHeight);
    assertEquals(expectedPivotHeight, pivotHeight);

    // Verify replaceInParent called once with root and pivot
    verify(spyMap, times(1)).replaceInParent(root, pivot);
  }

  @Test
    @Timeout(8000)
  void rotateLeft_withNullPivotLeft_shouldHandleNull() throws Exception {
    // Similar setup but pivotLeft is null
    Class<?> nodeClass = null;
    for (Class<?> inner : LinkedTreeMap.class.getDeclaredClasses()) {
      if ("Node".equals(inner.getSimpleName())) {
        nodeClass = inner;
        break;
      }
    }
    assertNotNull(nodeClass);

    Object root = nodeClass.getDeclaredConstructor(LinkedTreeMap.class, Object.class, Object.class, nodeClass, nodeClass)
        .newInstance(map, "root", "rootVal", null, null);
    setField(nodeClass, root, "height", 1);

    Object left = nodeClass.getDeclaredConstructor(LinkedTreeMap.class, Object.class, Object.class, nodeClass, nodeClass)
        .newInstance(map, "left", "leftVal", null, null);
    setField(nodeClass, left, "height", 1);
    setField(nodeClass, left, "parent", root);

    Object pivot = nodeClass.getDeclaredConstructor(LinkedTreeMap.class, Object.class, Object.class, nodeClass, nodeClass)
        .newInstance(map, "pivot", "pivotVal", null, null);
    setField(nodeClass, pivot, "height", 2);

    Object pivotRight = nodeClass.getDeclaredConstructor(LinkedTreeMap.class, Object.class, Object.class, nodeClass, nodeClass)
        .newInstance(map, "pivotRight", "pivotRightVal", null, null);
    setField(nodeClass, pivotRight, "height", 3);
    setField(nodeClass, pivotRight, "parent", pivot);

    setField(nodeClass, root, "left", left);
    setField(nodeClass, root, "right", pivot);
    setField(nodeClass, left, "parent", root);

    setField(nodeClass, pivot, "left", null);
    setField(nodeClass, pivot, "right", pivotRight);
    setField(nodeClass, pivotRight, "parent", pivot);

    setField(nodeClass, root, "parent", null);

    Field rootField = LinkedTreeMap.class.getDeclaredField("root");
    rootField.setAccessible(true);
    rootField.set(map, root);

    LinkedTreeMap<String, String> spyMap = spy(map);

    Method rotateLeft = LinkedTreeMap.class.getDeclaredMethod("rotateLeft", nodeClass);
    rotateLeft.setAccessible(true);
    rotateLeft.invoke(spyMap, root);

    // root.right should be pivotLeft (null)
    assertNull(getField(nodeClass, root, "right"));
    // pivot.left should be root
    assertSame(root, getField(nodeClass, pivot, "left"));
    // root.parent should be pivot
    assertSame(pivot, getField(nodeClass, root, "parent"));

    int rootHeight = (int) getField(nodeClass, root, "height");
    int pivotHeight = (int) getField(nodeClass, pivot, "height");

    int expectedRootHeight = Math.max(1, 0) + 1; // left.height=1, pivotLeft=null=0
    int expectedPivotHeight = Math.max(expectedRootHeight, 3) + 1; // pivotRight.height=3

    assertEquals(expectedRootHeight, rootHeight);
    assertEquals(expectedPivotHeight, pivotHeight);

    verify(spyMap, times(1)).replaceInParent(root, pivot);
  }

  private static void setField(Class<?> clazz, Object instance, String fieldName, Object value) throws Exception {
    Field field = clazz.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(instance, value);
  }

  private static Object getField(Class<?> clazz, Object instance, String fieldName) throws Exception {
    Field field = clazz.getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(instance);
  }
}