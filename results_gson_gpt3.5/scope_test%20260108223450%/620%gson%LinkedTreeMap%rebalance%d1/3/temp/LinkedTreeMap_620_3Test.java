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

class LinkedTreeMap_RebalanceTest {

  private LinkedTreeMap<String, String> map;

  // Helper to create Node instances via reflection
  private LinkedTreeMap.Node<String, String> createNode(String key, String value, int height) throws Exception {
    // Node is package-private static nested class, access via reflection
    Class<?> nodeClass = Class.forName("com.google.gson.internal.LinkedTreeMap$Node");
    // Constructor: Node(LinkedTreeMap, K key, V value, Node<K,V> parent)
    // We can pass null for parent here
    var ctor = nodeClass.getDeclaredConstructors()[0];
    ctor.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedTreeMap.Node<String, String> node = (LinkedTreeMap.Node<String, String>) ctor.newInstance(map, key, value, null);

    // Set height field
    Field heightField = nodeClass.getDeclaredField("height");
    heightField.setAccessible(true);
    heightField.setInt(node, height);

    return node;
  }

  // Helper to set left, right, parent fields on Node
  private void setLeft(LinkedTreeMap.Node<String, String> node, LinkedTreeMap.Node<String, String> left) throws Exception {
    Field leftField = node.getClass().getDeclaredField("left");
    leftField.setAccessible(true);
    leftField.set(node, left);
  }

  private void setRight(LinkedTreeMap.Node<String, String> node, LinkedTreeMap.Node<String, String> right) throws Exception {
    Field rightField = node.getClass().getDeclaredField("right");
    rightField.setAccessible(true);
    rightField.set(node, right);
  }

  private void setParent(LinkedTreeMap.Node<String, String> node, LinkedTreeMap.Node<String, String> parent) throws Exception {
    Field parentField = node.getClass().getDeclaredField("parent");
    parentField.setAccessible(true);
    parentField.set(node, parent);
  }

  private void setHeight(LinkedTreeMap.Node<String, String> node, int height) throws Exception {
    Field heightField = node.getClass().getDeclaredField("height");
    heightField.setAccessible(true);
    heightField.setInt(node, height);
  }

  private int getHeight(LinkedTreeMap.Node<String, String> node) throws Exception {
    Field heightField = node.getClass().getDeclaredField("height");
    heightField.setAccessible(true);
    return heightField.getInt(node);
  }

  private Method getRebalanceMethod() throws Exception {
    Method rebalance = LinkedTreeMap.class.getDeclaredMethod("rebalance", Class.forName("com.google.gson.internal.LinkedTreeMap$Node"), boolean.class);
    rebalance.setAccessible(true);
    return rebalance;
  }

  private Method getRotateLeftMethod() throws Exception {
    Method rotateLeft = LinkedTreeMap.class.getDeclaredMethod("rotateLeft", Class.forName("com.google.gson.internal.LinkedTreeMap$Node"));
    rotateLeft.setAccessible(true);
    return rotateLeft;
  }

  private Method getRotateRightMethod() throws Exception {
    Method rotateRight = LinkedTreeMap.class.getDeclaredMethod("rotateRight", Class.forName("com.google.gson.internal.LinkedTreeMap$Node"));
    rotateRight.setAccessible(true);
    return rotateRight;
  }

  @BeforeEach
  void setUp() {
    map = new LinkedTreeMap<>();
  }

  @Test
    @Timeout(8000)
  void testRebalance_deltaMinus2_rightRight_insertTrue() throws Exception {
    /*
      delta == -2 triggers right rotations
      rightDelta == -1 triggers rotateLeft(node)
      insert == true breaks after rotation
    */
    // Build nodes
    LinkedTreeMap.Node<String, String> node = createNode("node", "v", 1);
    LinkedTreeMap.Node<String, String> right = createNode("right", "v", 2);
    LinkedTreeMap.Node<String, String> rightRight = createNode("rightRight", "v", 1);

    // Setup tree:
    // node.right = right
    setRight(node, right);
    setParent(right, node);
    // right.right = rightRight
    setRight(right, rightRight);
    setParent(rightRight, right);

    // Heights:
    setHeight(rightRight, 1);
    setHeight(right, 2);
    setHeight(node, 0);

    // left is null
    setLeft(node, null);

    Method rebalance = getRebalanceMethod();

    // Spy on map to verify rotateLeft call
    LinkedTreeMap<String, String> spyMap = spy(map);
    // Inject spyMap into node's class context by calling rebalance on spyMap
    // Use reflection to invoke rebalance on spyMap instance
    Method rebalanceOnSpy = LinkedTreeMap.class.getDeclaredMethod("rebalance", node.getClass(), boolean.class);
    rebalanceOnSpy.setAccessible(true);

    // Replace rotateLeft to track calls
    doNothing().when(spyMap).rotateLeft(node);
    doNothing().when(spyMap).rotateRight(any());

    // Invoke rebalance
    rebalanceOnSpy.invoke(spyMap, node, true);

    // Verify rotateLeft called once on node
    verify(spyMap, times(1)).rotateLeft(node);
    verify(spyMap, never()).rotateRight(any());
  }

  @Test
    @Timeout(8000)
  void testRebalance_deltaMinus2_rightLeft_insertFalse() throws Exception {
    /*
      delta == -2 triggers right rotations
      rightDelta == 1 triggers rotateRight(right) then rotateLeft(node)
      insert == false does not break after rotation
    */
    LinkedTreeMap.Node<String, String> node = createNode("node", "v", 1);
    LinkedTreeMap.Node<String, String> right = createNode("right", "v", 2);
    LinkedTreeMap.Node<String, String> rightLeft = createNode("rightLeft", "v", 2);
    LinkedTreeMap.Node<String, String> rightRight = createNode("rightRight", "v", 1);

    setRight(node, right);
    setParent(right, node);
    setLeft(right, rightLeft);
    setParent(rightLeft, right);
    setRight(right, rightRight);
    setParent(rightRight, right);

    setHeight(rightLeft, 2);
    setHeight(rightRight, 1);
    setHeight(right, 3);
    setLeft(node, null);

    LinkedTreeMap<String, String> spyMap = spy(map);
    Method rebalanceOnSpy = LinkedTreeMap.class.getDeclaredMethod("rebalance", node.getClass(), boolean.class);
    rebalanceOnSpy.setAccessible(true);

    doNothing().when(spyMap).rotateLeft(node);
    doNothing().when(spyMap).rotateRight(right);

    rebalanceOnSpy.invoke(spyMap, node, false);

    verify(spyMap, times(1)).rotateRight(right);
    verify(spyMap, times(1)).rotateLeft(node);
  }

  @Test
    @Timeout(8000)
  void testRebalance_delta2_leftLeft_insertTrue() throws Exception {
    /*
      delta == 2 triggers left rotations
      leftDelta == 1 triggers rotateRight(node)
      insert == true breaks after rotation
    */
    LinkedTreeMap.Node<String, String> node = createNode("node", "v", 1);
    LinkedTreeMap.Node<String, String> left = createNode("left", "v", 2);
    LinkedTreeMap.Node<String, String> leftLeft = createNode("leftLeft", "v", 2);
    LinkedTreeMap.Node<String, String> leftRight = createNode("leftRight", "v", 1);

    setLeft(node, left);
    setParent(left, node);
    setLeft(left, leftLeft);
    setParent(leftLeft, left);
    setRight(left, leftRight);
    setParent(leftRight, left);

    setHeight(leftLeft, 2);
    setHeight(leftRight, 1);
    setHeight(left, 3);
    setRight(node, null);

    LinkedTreeMap<String, String> spyMap = spy(map);
    Method rebalanceOnSpy = LinkedTreeMap.class.getDeclaredMethod("rebalance", node.getClass(), boolean.class);
    rebalanceOnSpy.setAccessible(true);

    doNothing().when(spyMap).rotateRight(node);
    doNothing().when(spyMap).rotateLeft(any());

    rebalanceOnSpy.invoke(spyMap, node, true);

    verify(spyMap, times(1)).rotateRight(node);
    verify(spyMap, never()).rotateLeft(any());
  }

  @Test
    @Timeout(8000)
  void testRebalance_delta2_leftRight_insertFalse() throws Exception {
    /*
      delta == 2 triggers left rotations
      leftDelta == -1 triggers rotateLeft(left) then rotateRight(node)
      insert == false does not break after rotation
    */
    LinkedTreeMap.Node<String, String> node = createNode("node", "v", 1);
    LinkedTreeMap.Node<String, String> left = createNode("left", "v", 2);
    LinkedTreeMap.Node<String, String> leftLeft = createNode("leftLeft", "v", 1);
    LinkedTreeMap.Node<String, String> leftRight = createNode("leftRight", "v", 2);

    setLeft(node, left);
    setParent(left, node);
    setLeft(left, leftLeft);
    setParent(leftLeft, left);
    setRight(left, leftRight);
    setParent(leftRight, left);

    setHeight(leftLeft, 1);
    setHeight(leftRight, 2);
    setHeight(left, 3);
    setRight(node, null);

    LinkedTreeMap<String, String> spyMap = spy(map);
    Method rebalanceOnSpy = LinkedTreeMap.class.getDeclaredMethod("rebalance", node.getClass(), boolean.class);
    rebalanceOnSpy.setAccessible(true);

    doNothing().when(spyMap).rotateLeft(left);
    doNothing().when(spyMap).rotateRight(node);

    rebalanceOnSpy.invoke(spyMap, node, false);

    verify(spyMap, times(1)).rotateLeft(left);
    verify(spyMap, times(1)).rotateRight(node);
  }

  @Test
    @Timeout(8000)
  void testRebalance_delta0_insertTrue_setsHeightAndBreaks() throws Exception {
    /*
      delta == 0 sets node.height = leftHeight + 1 and breaks if insert == true
    */
    LinkedTreeMap.Node<String, String> node = createNode("node", "v", 0);
    LinkedTreeMap.Node<String, String> left = createNode("left", "v", 2);
    LinkedTreeMap.Node<String, String> right = createNode("right", "v", 2);

    setLeft(node, left);
    setRight(node, right);
    setParent(left, node);
    setParent(right, node);

    setHeight(left, 2);
    setHeight(right, 2);

    Method rebalance = getRebalanceMethod();

    rebalance.invoke(map, node, true);

    int heightAfter = getHeight(node);
    assertEquals(3, heightAfter);
  }

  @Test
    @Timeout(8000)
  void testRebalance_delta1_insertFalse_setsHeightAndBreaks() throws Exception {
    /*
      delta == 1 or -1 sets node.height = max(leftHeight,rightHeight)+1 and breaks if insert == false
    */
    LinkedTreeMap.Node<String, String> node = createNode("node", "v", 0);
    LinkedTreeMap.Node<String, String> left = createNode("left", "v", 3);
    LinkedTreeMap.Node<String, String> right = createNode("right", "v", 2);

    setLeft(node, left);
    setRight(node, right);
    setParent(left, node);
    setParent(right, node);

    setHeight(left, 3);
    setHeight(right, 2);

    // Manually set delta = 1 (leftHeight - rightHeight)
    Method rebalance = getRebalanceMethod();

    rebalance.invoke(map, node, false);

    int heightAfter = getHeight(node);
    assertEquals(4, heightAfter);
  }
}