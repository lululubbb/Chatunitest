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

class LinkedTreeMap_rotateLeft_Test {

  private LinkedTreeMap<String, String> map;

  // Helper method to create a Node instance via reflection
  private LinkedTreeMap.Node<String, String> createNode(String key, String value, int height) throws Exception {
    Class<?> nodeClass = Class.forName("com.google.gson.internal.LinkedTreeMap$Node");
    // Node constructor: Node(Node<K,V> parent, K key, V value, Node<K,V> next)
    // We'll pass null for parent and next for simplicity
    var ctor = nodeClass.getDeclaredConstructors()[0];
    ctor.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedTreeMap.Node<String, String> node = (LinkedTreeMap.Node<String, String>) ctor.newInstance(null, key, value, null);
    // Set height field
    Field heightField = nodeClass.getDeclaredField("height");
    heightField.setAccessible(true);
    heightField.setInt(node, height);
    return node;
  }

  @BeforeEach
  void setUp() {
    map = new LinkedTreeMap<>();
  }

  @Test
    @Timeout(8000)
  void rotateLeft_balancesCorrectly_withPivotChildren() throws Exception {
    // Setup nodes: root, left, pivot, pivotLeft, pivotRight
    LinkedTreeMap.Node<String, String> root = createNode("root", "r", 1);
    LinkedTreeMap.Node<String, String> left = createNode("left", "l", 2);
    LinkedTreeMap.Node<String, String> pivot = createNode("pivot", "p", 3);
    LinkedTreeMap.Node<String, String> pivotLeft = createNode("pivotLeft", "pl", 4);
    LinkedTreeMap.Node<String, String> pivotRight = createNode("pivotRight", "pr", 5);

    // Setup tree structure
    setField(root, "left", left);
    setField(root, "right", pivot);
    setField(left, "parent", root);
    setField(pivot, "parent", root);
    setField(pivot, "left", pivotLeft);
    setField(pivot, "right", pivotRight);
    setField(pivotLeft, "parent", pivot);
    setField(pivotRight, "parent", pivot);

    // Spy map to verify replaceInParent call
    LinkedTreeMap<String, String> spyMap = spy(map);
    // Replace root parent with null to simulate root node
    setField(root, "parent", null);

    // Use reflection to invoke private rotateLeft
    Method rotateLeft = LinkedTreeMap.class.getDeclaredMethod("rotateLeft", Class.forName("com.google.gson.internal.LinkedTreeMap$Node"));
    rotateLeft.setAccessible(true);
    rotateLeft.invoke(spyMap, root);

    // Use reflection to verify private replaceInParent was called by checking tree structure changes
    // Since replaceInParent is private, we cannot verify with Mockito directly.
    // Instead, verify the parent link from root to pivot and pivot's left child is root
    assertSame(root, getField(pivot, "left"));
    assertSame(pivot, getField(root, "parent"));

    // root.right should be pivotLeft
    assertSame(pivotLeft, getField(root, "right"));
    assertSame(root, getField(pivotLeft, "parent"));

    // Heights updated correctly
    int rootHeight = (int) getField(root, "height");
    int pivotHeight = (int) getField(pivot, "height");
    int expectedRootHeight = Math.max(getHeight(left), getHeight(pivotLeft)) + 1;
    int expectedPivotHeight = Math.max(expectedRootHeight, getHeight(pivotRight)) + 1;
    assertEquals(expectedRootHeight, rootHeight);
    assertEquals(expectedPivotHeight, pivotHeight);
  }

  @Test
    @Timeout(8000)
  void rotateLeft_handlesNullPivotLeft() throws Exception {
    LinkedTreeMap.Node<String, String> root = createNode("root", "r", 1);
    LinkedTreeMap.Node<String, String> left = createNode("left", "l", 2);
    LinkedTreeMap.Node<String, String> pivot = createNode("pivot", "p", 3);
    LinkedTreeMap.Node<String, String> pivotRight = createNode("pivotRight", "pr", 5);

    setField(root, "left", left);
    setField(root, "right", pivot);
    setField(left, "parent", root);
    setField(pivot, "parent", root);
    setField(pivot, "left", null);
    setField(pivot, "right", pivotRight);
    setField(pivotRight, "parent", pivot);
    setField(root, "parent", null);

    LinkedTreeMap<String, String> spyMap = spy(map);

    Method rotateLeft = LinkedTreeMap.class.getDeclaredMethod("rotateLeft", Class.forName("com.google.gson.internal.LinkedTreeMap$Node"));
    rotateLeft.setAccessible(true);
    rotateLeft.invoke(spyMap, root);

    // Use reflection to verify tree structure changes instead of Mockito verify
    assertSame(root, getField(pivot, "left"));
    assertSame(pivot, getField(root, "parent"));
    assertNull(getField(root, "right"));
    int rootHeight = (int) getField(root, "height");
    int pivotHeight = (int) getField(pivot, "height");
    int expectedRootHeight = Math.max(getHeight(left), 0) + 1;
    int expectedPivotHeight = Math.max(expectedRootHeight, getHeight(pivotRight)) + 1;
    assertEquals(expectedRootHeight, rootHeight);
    assertEquals(expectedPivotHeight, pivotHeight);
  }

  // Util to get height field from node or 0 if null
  private int getHeight(Object node) throws Exception {
    if (node == null) return 0;
    return (int) getField(node, "height");
  }

  // Reflection helpers
  private void setField(Object target, String fieldName, Object value) throws Exception {
    Field field = target.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  private Object getField(Object target, String fieldName) throws Exception {
    Field field = target.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(target);
  }
}