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

  // Helper to create a Node instance via reflection
  private LinkedTreeMap.Node<String, String> createNode(String key, String value, int height) throws Exception {
    LinkedTreeMap.Node<String, String> node = (LinkedTreeMap.Node<String, String>) 
        Class.forName("com.google.gson.internal.LinkedTreeMap$Node")
        .getDeclaredConstructor(LinkedTreeMap.class, Object.class, Object.class, LinkedTreeMap.Node.class, LinkedTreeMap.Node.class, LinkedTreeMap.Node.class, int.class)
        .newInstance(map, key, value, null, null, null, height);
    return node;
  }

  // Helper to set private fields of Node
  private void setNodeField(LinkedTreeMap.Node<String, String> node, String fieldName, Object value) throws Exception {
    Field field = LinkedTreeMap.Node.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(node, value);
  }

  // Helper to get private field of LinkedTreeMap
  private Field getMapField(String fieldName) throws Exception {
    Field field = LinkedTreeMap.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    return field;
  }

  // Invoke private rebalance method
  private void invokeRebalance(LinkedTreeMap.Node<String, String> node, boolean insert) throws Exception {
    Method rebalance = LinkedTreeMap.class.getDeclaredMethod("rebalance", LinkedTreeMap.Node.class, boolean.class);
    rebalance.setAccessible(true);
    rebalance.invoke(map, node, insert);
  }

  @BeforeEach
  void setUp() {
    map = new LinkedTreeMap<>();
  }

  @Test
    @Timeout(8000)
  void rebalance_noRotation_deltaZero_insertTrue_breaksLoop() throws Exception {
    // Node with leftHeight == rightHeight, insert == true, should update height and break
    LinkedTreeMap.Node<String, String> node = createNode("key", "value", 1);
    setNodeField(node, "left", null);
    setNodeField(node, "right", null);
    setNodeField(node, "parent", null);

    invokeRebalance(node, true);

    // height should be leftHeight + 1 == 1
    Field heightField = LinkedTreeMap.Node.class.getDeclaredField("height");
    heightField.setAccessible(true);
    int height = (int) heightField.get(node);
    assertEquals(1, height);
  }

  @Test
    @Timeout(8000)
  void rebalance_deltaMinus2_rightRightRotation_insertTrue_breaksLoop() throws Exception {
    // Setup nodes for right-right case: delta == -2, rightDelta == -1
    LinkedTreeMap.Node<String, String> node = createNode("root", "v", 3);
    LinkedTreeMap.Node<String, String> right = createNode("right", "v", 2);
    LinkedTreeMap.Node<String, String> rightRight = createNode("rightRight", "v", 1);

    setNodeField(node, "left", null);
    setNodeField(node, "right", right);
    setNodeField(node, "parent", null);

    setNodeField(right, "left", null);
    setNodeField(right, "right", rightRight);
    setNodeField(right, "parent", node);

    setNodeField(rightRight, "left", null);
    setNodeField(rightRight, "right", null);
    setNodeField(rightRight, "parent", right);

    // Spy on map to verify rotateLeft called on node
    LinkedTreeMap<String, String> spyMap = spy(map);
    // Replace map with spy
    this.map = spyMap;

    // Replace node references in spyMap if needed
    invokeRebalance(node, true);

    verify(spyMap).rotateLeft(node);
  }

  @Test
    @Timeout(8000)
  void rebalance_deltaMinus2_rightLeftRotation_insertFalse_continuesLoop() throws Exception {
    // Setup nodes for right-left case: delta == -2, rightDelta == 1, insert == false
    LinkedTreeMap.Node<String, String> node = createNode("root", "v", 3);
    LinkedTreeMap.Node<String, String> right = createNode("right", "v", 2);
    LinkedTreeMap.Node<String, String> rightLeft = createNode("rightLeft", "v", 1);
    LinkedTreeMap.Node<String, String> rightRight = createNode("rightRight", "v", 0);

    setNodeField(node, "left", null);
    setNodeField(node, "right", right);
    setNodeField(node, "parent", null);

    setNodeField(right, "left", rightLeft);
    setNodeField(right, "right", rightRight);
    setNodeField(right, "parent", node);

    setNodeField(rightLeft, "left", null);
    setNodeField(rightLeft, "right", null);
    setNodeField(rightLeft, "parent", right);

    setNodeField(rightRight, "left", null);
    setNodeField(rightRight, "right", null);
    setNodeField(rightRight, "parent", right);

    LinkedTreeMap<String, String> spyMap = spy(map);
    this.map = spyMap;

    invokeRebalance(node, false);

    verify(spyMap).rotateRight(right);
    verify(spyMap).rotateLeft(node);
  }

  @Test
    @Timeout(8000)
  void rebalance_deltaPlus2_leftLeftRotation_insertTrue_breaksLoop() throws Exception {
    // Setup nodes for left-left case: delta == 2, leftDelta == 1, insert == true
    LinkedTreeMap.Node<String, String> node = createNode("root", "v", 3);
    LinkedTreeMap.Node<String, String> left = createNode("left", "v", 2);
    LinkedTreeMap.Node<String, String> leftLeft = createNode("leftLeft", "v", 1);

    setNodeField(node, "left", left);
    setNodeField(node, "right", null);
    setNodeField(node, "parent", null);

    setNodeField(left, "left", leftLeft);
    setNodeField(left, "right", null);
    setNodeField(left, "parent", node);

    setNodeField(leftLeft, "left", null);
    setNodeField(leftLeft, "right", null);
    setNodeField(leftLeft, "parent", left);

    LinkedTreeMap<String, String> spyMap = spy(map);
    this.map = spyMap;

    invokeRebalance(node, true);

    verify(spyMap).rotateRight(node);
  }

  @Test
    @Timeout(8000)
  void rebalance_deltaPlus2_leftRightRotation_insertFalse_continuesLoop() throws Exception {
    // Setup nodes for left-right case: delta == 2, leftDelta == -1, insert == false
    LinkedTreeMap.Node<String, String> node = createNode("root", "v", 3);
    LinkedTreeMap.Node<String, String> left = createNode("left", "v", 2);
    LinkedTreeMap.Node<String, String> leftLeft = createNode("leftLeft", "v", 0);
    LinkedTreeMap.Node<String, String> leftRight = createNode("leftRight", "v", 1);

    setNodeField(node, "left", left);
    setNodeField(node, "right", null);
    setNodeField(node, "parent", null);

    setNodeField(left, "left", leftLeft);
    setNodeField(left, "right", leftRight);
    setNodeField(left, "parent", node);

    setNodeField(leftLeft, "left", null);
    setNodeField(leftLeft, "right", null);
    setNodeField(leftLeft, "parent", left);

    setNodeField(leftRight, "left", null);
    setNodeField(leftRight, "right", null);
    setNodeField(leftRight, "parent", left);

    LinkedTreeMap<String, String> spyMap = spy(map);
    this.map = spyMap;

    invokeRebalance(node, false);

    verify(spyMap).rotateLeft(left);
    verify(spyMap).rotateRight(node);
  }

  @Test
    @Timeout(8000)
  void rebalance_deltaPlusMinus1_heightUpdated_insertFalse_breaksLoop() throws Exception {
    // delta == 1 or -1, height updated, insert == false, should break loop
    LinkedTreeMap.Node<String, String> node = createNode("root", "v", 1);
    LinkedTreeMap.Node<String, String> left = createNode("left", "v", 2);
    LinkedTreeMap.Node<String, String> right = createNode("right", "v", 1);

    setNodeField(node, "left", left);
    setNodeField(node, "right", right);
    setNodeField(node, "parent", null);

    // delta = leftHeight - rightHeight = 2 - 1 = 1
    invokeRebalance(node, false);

    Field heightField = LinkedTreeMap.Node.class.getDeclaredField("height");
    heightField.setAccessible(true);
    int height = (int) heightField.get(node);
    assertEquals(3, height);
  }
}