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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LinkedTreeMapRotateRightTest {

  LinkedTreeMap<String, String> map;

  @BeforeEach
  void setUp() {
    map = new LinkedTreeMap<>();
  }

  @Test
    @Timeout(8000)
  void rotateRight_basicRotation() throws Exception {
    // Create nodes manually to setup the structure:
    //       root
    //      /
    //   pivot
    //   /    \
    // pivotLeft pivotRight
    //
    // After rotateRight(root), pivot becomes root, root becomes pivot.right

    // Access Node class via reflection
    Class<?> nodeClass = null;
    for (Class<?> inner : LinkedTreeMap.class.getDeclaredClasses()) {
      if ("Node".equals(inner.getSimpleName())) {
        nodeClass = inner;
        break;
      }
    }
    assertNotNull(nodeClass);

    // Create nodes using reflection
    Object root = createNode(nodeClass, "rootKey", "rootValue");
    Object pivot = createNode(nodeClass, "pivotKey", "pivotValue");
    Object pivotLeft = createNode(nodeClass, "pivotLeftKey", "pivotLeftValue");
    Object pivotRight = createNode(nodeClass, "pivotRightKey", "pivotRightValue");
    Object right = createNode(nodeClass, "rightKey", "rightValue");

    // Set fields root.left = pivot, root.right = right
    setField(root, "left", pivot);
    setField(root, "right", right);
    setField(pivot, "parent", root);
    setField(right, "parent", root);

    // pivot.left = pivotLeft, pivot.right = pivotRight
    setField(pivot, "left", pivotLeft);
    setField(pivot, "right", pivotRight);
    setField(pivotLeft, "parent", pivot);
    setField(pivotRight, "parent", pivot);

    // Set heights to test height recalculation
    setField(right, "height", 2);
    setField(pivotRight, "height", 3);
    setField(pivotLeft, "height", 4);
    setField(root, "height", 1);
    setField(pivot, "height", 1);

    // Set root as map.root
    setField(map, "root", root);

    // Use reflection to invoke private rotateRight method
    Method rotateRight = LinkedTreeMap.class.getDeclaredMethod("rotateRight", nodeClass);
    rotateRight.setAccessible(true);
    rotateRight.invoke(map, root);

    // After rotation, pivot should be root
    Object newRoot = getField(map, "root");
    assertSame(pivot, newRoot);

    // root.left should be pivotRight
    Object newRootLeft = getField(root, "left");
    assertSame(pivotRight, newRootLeft);
    // pivotRight.parent should be root
    Object pivotRightParent = getField(pivotRight, "parent");
    assertSame(root, pivotRightParent);

    // pivot.right should be root
    Object pivotRightChild = getField(pivot, "right");
    assertSame(root, pivotRightChild);

    // root.parent should be pivot
    Object rootParent = getField(root, "parent");
    assertSame(pivot, rootParent);

    // Heights recalculated:
    int rootHeight = getIntField(root, "height");
    int pivotHeight = getIntField(pivot, "height");

    // root.height = max(right.height, pivotRight.height) +1 = max(2,3)+1=4
    assertEquals(4, rootHeight);

    // pivot.height = max(root.height, pivotLeft.height)+1 = max(4,4)+1=5
    assertEquals(5, pivotHeight);
  }

  @Test
    @Timeout(8000)
  void rotateRight_pivotRightIsNull() throws Exception {
    // Setup similar to previous test but pivot.right = null

    Class<?> nodeClass = null;
    for (Class<?> inner : LinkedTreeMap.class.getDeclaredClasses()) {
      if ("Node".equals(inner.getSimpleName())) {
        nodeClass = inner;
        break;
      }
    }
    assertNotNull(nodeClass);

    Object root = createNode(nodeClass, "rootKey", "rootValue");
    Object pivot = createNode(nodeClass, "pivotKey", "pivotValue");
    Object pivotLeft = createNode(nodeClass, "pivotLeftKey", "pivotLeftValue");
    Object right = createNode(nodeClass, "rightKey", "rightValue");

    setField(root, "left", pivot);
    setField(root, "right", right);
    setField(pivot, "parent", root);
    setField(right, "parent", root);

    setField(pivot, "left", pivotLeft);
    setField(pivot, "right", null);
    setField(pivotLeft, "parent", pivot);

    setField(right, "height", 1);
    setField(pivotLeft, "height", 2);
    setField(root, "height", 1);
    setField(pivot, "height", 1);

    setField(map, "root", root);

    Method rotateRight = LinkedTreeMap.class.getDeclaredMethod("rotateRight", nodeClass);
    rotateRight.setAccessible(true);
    rotateRight.invoke(map, root);

    Object newRoot = getField(map, "root");
    assertSame(pivot, newRoot);

    Object newRootLeft = getField(root, "left");
    assertNull(newRootLeft);

    Object pivotRightChild = getField(pivot, "right");
    assertSame(root, pivotRightChild);

    Object rootParent = getField(root, "parent");
    assertSame(pivot, rootParent);

    int rootHeight = getIntField(root, "height");
    int pivotHeight = getIntField(pivot, "height");

    // root.height = max(right.height, 0) +1 = max(1,0)+1=2
    assertEquals(2, rootHeight);

    // pivot.height = max(root.height, pivotLeft.height)+1 = max(2,2)+1=3
    assertEquals(3, pivotHeight);
  }

  // Helper methods for reflection

  private Object createNode(Class<?> nodeClass, Object key, Object value) throws Exception {
    // Node constructor: Node(Node parent, K key, V value)
    // Use null parent for now
    Constructor<?> constructor = nodeClass.getDeclaredConstructor(nodeClass, Object.class, Object.class);
    constructor.setAccessible(true);
    return constructor.newInstance(null, key, value);
  }

  private void setField(Object obj, String fieldName, Object value) throws Exception {
    Field field = getFieldRecursive(obj.getClass(), fieldName);
    field.setAccessible(true);
    field.set(obj, value);
  }

  private Object getField(Object obj, String fieldName) throws Exception {
    Field field = getFieldRecursive(obj.getClass(), fieldName);
    field.setAccessible(true);
    return field.get(obj);
  }

  private int getIntField(Object obj, String fieldName) throws Exception {
    Field field = getFieldRecursive(obj.getClass(), fieldName);
    field.setAccessible(true);
    return field.getInt(obj);
  }

  private Field getFieldRecursive(Class<?> clazz, String fieldName) throws NoSuchFieldException {
    Class<?> current = clazz;
    while (current != null) {
      try {
        return current.getDeclaredField(fieldName);
      } catch (NoSuchFieldException e) {
        current = current.getSuperclass();
      }
    }
    throw new NoSuchFieldException(fieldName);
  }
}