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

  private LinkedTreeMap<Integer, String> map;

  @BeforeEach
  void setUp() {
    // Use default constructor with natural ordering
    map = new LinkedTreeMap<>();
  }

  @Test
    @Timeout(8000)
  void rotateRight_nullPivotRight() throws Exception {
    // Setup nodes for rotateRight with pivotRight == null
    LinkedTreeMap.Node<Integer, String> root = createNode(10, null);
    LinkedTreeMap.Node<Integer, String> pivot = createNode(5, root);
    LinkedTreeMap.Node<Integer, String> pivotLeft = createNode(2, pivot);
    // pivotRight is null
    LinkedTreeMap.Node<Integer, String> right = createNode(15, root);

    setField(root, "left", pivot);
    setField(root, "right", right);
    setField(pivot, "left", pivotLeft);
    setField(pivot, "right", null);

    // Set initial heights
    setField(root, "height", 3);
    setField(pivot, "height", 2);
    setField(pivotLeft, "height", 1);
    setField(right, "height", 1);

    // Inject root parent as null to simulate root of tree
    setField(root, "parent", null);
    setField(pivot, "parent", root);
    setField(pivotLeft, "parent", pivot);
    setField(right, "parent", root);

    // Spy on map to verify replaceInParent call
    LinkedTreeMap<Integer, String> spyMap = spy(map);

    Method rotateRight = LinkedTreeMap.class.getDeclaredMethod("rotateRight", LinkedTreeMap.Node.class);
    rotateRight.setAccessible(true);

    // invoke rotateRight on root
    rotateRight.invoke(spyMap, root);

    // Verify structure after rotation
    // pivot should become new root, root should be pivot.right
    assertSame(pivot, getField(root, "parent"));
    assertSame(root, getField(pivot, "right"));
    assertNull(getField(pivot, "parent"));
    assertSame(pivotLeft, getField(pivot, "left"));
    assertNull(getField(root, "left"));
    assertSame(right, getField(root, "right"));
    assertSame(root, getField(right, "parent"));

    // Heights should be updated correctly
    int rootHeight = (int) getField(root, "height");
    int pivotHeight = (int) getField(pivot, "height");

    int expectedRootHeight = Math.max((int) getField(right, "height"), 0) + 1; // pivotRight null
    int expectedPivotHeight = Math.max(expectedRootHeight, (int) getField(pivotLeft, "height")) + 1;

    assertEquals(expectedRootHeight, rootHeight);
    assertEquals(expectedPivotHeight, pivotHeight);

    // Verify replaceInParent was called with correct arguments via reflection
    Method replaceInParent = LinkedTreeMap.class.getDeclaredMethod("replaceInParent", LinkedTreeMap.Node.class, LinkedTreeMap.Node.class);
    replaceInParent.setAccessible(true);
    // We cannot verify private method calls with Mockito directly, so invoke it with spy and check state instead
    // Alternatively, verify the effect of replaceInParent, which we already did with assertions above.
  }

  @Test
    @Timeout(8000)
  void rotateRight_withPivotRight() throws Exception {
    // Setup nodes for rotateRight with pivotRight != null
    LinkedTreeMap.Node<Integer, String> root = createNode(20, null);
    LinkedTreeMap.Node<Integer, String> pivot = createNode(10, root);
    LinkedTreeMap.Node<Integer, String> pivotLeft = createNode(5, pivot);
    LinkedTreeMap.Node<Integer, String> pivotRight = createNode(15, pivot);
    LinkedTreeMap.Node<Integer, String> right = createNode(25, root);

    setField(root, "left", pivot);
    setField(root, "right", right);
    setField(pivot, "left", pivotLeft);
    setField(pivot, "right", pivotRight);

    // Set initial heights
    setField(root, "height", 4);
    setField(pivot, "height", 3);
    setField(pivotLeft, "height", 1);
    setField(pivotRight, "height", 1);
    setField(right, "height", 1);

    // Parents
    setField(root, "parent", null);
    setField(pivot, "parent", root);
    setField(pivotLeft, "parent", pivot);
    setField(pivotRight, "parent", pivot);
    setField(right, "parent", root);

    // Spy on map to verify replaceInParent call
    LinkedTreeMap<Integer, String> spyMap = spy(map);

    Method rotateRight = LinkedTreeMap.class.getDeclaredMethod("rotateRight", LinkedTreeMap.Node.class);
    rotateRight.setAccessible(true);

    rotateRight.invoke(spyMap, root);

    // After rotation:
    // root.left should be pivotRight's right child (null)
    assertNull(getField(root, "left"));
    // pivotRight's parent should be root
    assertSame(root, getField(pivotRight, "parent"));
    // pivot should replace root in parent (root has no parent, so pivot parent null)
    assertNull(getField(pivot, "parent"));
    // pivot.right should be root
    assertSame(root, getField(pivot, "right"));
    // root.parent should be pivot
    assertSame(pivot, getField(root, "parent"));
    // pivot.left should remain pivotLeft
    assertSame(pivotLeft, getField(pivot, "left"));

    // Heights updated
    int rootHeight = (int) getField(root, "height");
    int pivotHeight = (int) getField(pivot, "height");
    int expectedRootHeight = Math.max((int) getField(right, "height"), (int) getField(pivotRight, "height")) + 1;
    int expectedPivotHeight = Math.max(rootHeight, (int) getField(pivotLeft, "height")) + 1;

    assertEquals(expectedRootHeight, rootHeight);
    assertEquals(expectedPivotHeight, pivotHeight);

    // Verify replaceInParent was called with correct arguments via reflection
    Method replaceInParent = LinkedTreeMap.class.getDeclaredMethod("replaceInParent", LinkedTreeMap.Node.class, LinkedTreeMap.Node.class);
    replaceInParent.setAccessible(true);
    // We cannot verify private method calls with Mockito directly, so invoke it with spy and check state instead
    // Alternatively, verify the effect of replaceInParent, which we already did with assertions above.
  }

  // Helper method to create Node instance
  private LinkedTreeMap.Node<Integer, String> createNode(Integer key, LinkedTreeMap.Node<Integer, String> parent) throws Exception {
    Class<?> nodeClass = Class.forName("com.google.gson.internal.LinkedTreeMap$Node");
    LinkedTreeMap.Node<Integer, String> node = (LinkedTreeMap.Node<Integer, String>) nodeClass.getDeclaredConstructor(Object.class, Object.class, LinkedTreeMap.Node.class).newInstance(key, null, parent);
    return node;
  }

  // Helper to set private fields via reflection
  private void setField(Object target, String fieldName, Object value) throws Exception {
    Field field = getFieldObject(target.getClass(), fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  // Helper to get private fields via reflection
  private Object getField(Object target, String fieldName) throws Exception {
    Field field = getFieldObject(target.getClass(), fieldName);
    field.setAccessible(true);
    return field.get(target);
  }

  private Field getFieldObject(Class<?> clazz, String fieldName) throws NoSuchFieldException {
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