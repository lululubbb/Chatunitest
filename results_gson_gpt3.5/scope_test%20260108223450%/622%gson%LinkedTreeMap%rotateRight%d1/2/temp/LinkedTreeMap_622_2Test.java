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

class LinkedTreeMap_rotateRight_Test {

  LinkedTreeMap<Integer, String> map;

  @BeforeEach
  void setUp() {
    map = new LinkedTreeMap<>();
  }

  @Test
    @Timeout(8000)
  void rotateRight_basicRotation() throws Exception {
    // Create nodes manually to form a small subtree:
    //       root
    //      /
    //   pivot
    //   /    \
    // pLeft  pRight

    // Create nodes using reflection since Node is package-private static class
    Class<?> nodeClass = Class.forName("com.google.gson.internal.LinkedTreeMap$Node");
    // Constructor: Node(LinkedTreeMap, K key, V value, Node<K,V> parent)
    Constructor<?> constructor = nodeClass.getDeclaredConstructor(LinkedTreeMap.class, Object.class, Object.class, nodeClass);
    constructor.setAccessible(true);

    // Create pivot's children pLeft and pRight with height 1
    Object pLeft = constructor.newInstance(map, 1, "pLeft", null);
    setField(nodeClass, pLeft, "height", 1);
    Object pRight = constructor.newInstance(map, 2, "pRight", null);
    setField(nodeClass, pRight, "height", 1);

    // Create pivot with left=pLeft and right=pRight, height to be set later
    Object pivot = constructor.newInstance(map, 3, "pivot", null);
    setField(nodeClass, pivot, "left", pLeft);
    setField(nodeClass, pivot, "right", pRight);
    setField(nodeClass, pLeft, "parent", pivot);
    setField(nodeClass, pRight, "parent", pivot);
    setField(nodeClass, pivot, "height", 2);

    // Create root with left=pivot and right=null, height to be set later
    Object root = constructor.newInstance(map, 4, "root", null);
    setField(nodeClass, root, "left", pivot);
    setField(nodeClass, root, "right", null);
    setField(nodeClass, pivot, "parent", root);
    setField(nodeClass, root, "height", 3);

    // Set root as root of the map
    setField(LinkedTreeMap.class, map, "root", root);

    // Spy map to verify replaceInParent call
    LinkedTreeMap<Integer, String> spyMap = spy(map);

    // Use reflection to invoke private rotateRight method
    Method rotateRight = LinkedTreeMap.class.getDeclaredMethod("rotateRight", nodeClass);
    rotateRight.setAccessible(true);
    rotateRight.invoke(spyMap, root);

    // After rotation, pivot should be new root of subtree, root should be right child of pivot
    Object rootParent = getField(nodeClass, root, "parent");
    Object pivotParent = getField(nodeClass, pivot, "parent");

    // The pivot should replace root in parent, so pivot.parent should be null (root had no parent)
    assertNull(pivotParent);
    // root parent should be pivot
    assertSame(pivot, rootParent);

    // root.left should be pivotRight (which is pRight)
    Object rootLeft = getField(nodeClass, root, "left");
    assertSame(pRight, rootLeft);

    // pivot.right should be root
    Object pivotRightAfter = getField(nodeClass, pivot, "right");
    assertSame(root, pivotRightAfter);

    // Check heights updated correctly
    int rootHeight = (int) getField(nodeClass, root, "height");
    int pivotHeight = (int) getField(nodeClass, pivot, "height");

    // root height = max(right.height, pivotRight.height) + 1
    // right is null, so 0; pivotRight is pRight with height 1
    assertEquals(2, rootHeight);

    // pivot height = max(root.height, pivotLeft.height) + 1
    // root height is 2, pivotLeft height is 1
    assertEquals(3, pivotHeight);

    // Verify replaceInParent was called with root and pivot
    // Use reflection to get the method and verify with ArgumentMatchers.any()
    verify(spyMap).replaceInParent(
        nodeClass.cast(root),
        nodeClass.cast(pivot));
  }

  @Test
    @Timeout(8000)
  void rotateRight_withNullPivotRight() throws Exception {
    // Similar setup but pivot.right = null

    Class<?> nodeClass = Class.forName("com.google.gson.internal.LinkedTreeMap$Node");
    Constructor<?> constructor = nodeClass.getDeclaredConstructor(LinkedTreeMap.class, Object.class, Object.class, nodeClass);
    constructor.setAccessible(true);

    Object pLeft = constructor.newInstance(map, 1, "pLeft", null);
    setField(nodeClass, pLeft, "height", 1);

    Object pivot = constructor.newInstance(map, 3, "pivot", null);
    setField(nodeClass, pivot, "left", pLeft);
    setField(nodeClass, pivot, "right", null);
    setField(nodeClass, pLeft, "parent", pivot);
    setField(nodeClass, pivot, "height", 2);

    Object right = constructor.newInstance(map, 5, "right", null);
    setField(nodeClass, right, "height", 2);

    Object root = constructor.newInstance(map, 4, "root", null);
    setField(nodeClass, root, "left", pivot);
    setField(nodeClass, root, "right", right);
    setField(nodeClass, pivot, "parent", root);
    setField(nodeClass, right, "parent", root);
    setField(nodeClass, root, "height", 3);

    setField(LinkedTreeMap.class, map, "root", root);

    LinkedTreeMap<Integer, String> spyMap = spy(map);

    Method rotateRight = LinkedTreeMap.class.getDeclaredMethod("rotateRight", nodeClass);
    rotateRight.setAccessible(true);
    rotateRight.invoke(spyMap, root);

    Object rootLeft = getField(nodeClass, root, "left");
    assertNull(rootLeft); // pivotRight was null, so root.left should be null

    int rootHeight = (int) getField(nodeClass, root, "height");
    int pivotHeight = (int) getField(nodeClass, pivot, "height");

    // root height = max(right.height, pivotRight.height) + 1 = max(2,0)+1=3
    assertEquals(3, rootHeight);

    // pivot height = max(root.height, pivotLeft.height)+1 = max(3,1)+1=4
    assertEquals(4, pivotHeight);

    verify(spyMap).replaceInParent(
        nodeClass.cast(root),
        nodeClass.cast(pivot));
  }

  private static void setField(Class<?> clazz, Object target, String fieldName, Object value) throws Exception {
    Field field = clazz.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  private static void setField(Object target, String fieldName, Object value) throws Exception {
    setField(target.getClass(), target, fieldName, value);
  }

  private static Object getField(Class<?> clazz, Object target, String fieldName) throws Exception {
    Field field = clazz.getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(target);
  }
}