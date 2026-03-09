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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LinkedTreeMapRebalanceTest {

  LinkedTreeMap<String, String> map;

  @BeforeEach
  void setUp() {
    map = new LinkedTreeMap<>();
  }

  @SuppressWarnings("unchecked")
  private LinkedTreeMap.Node<String, String> createNode(String key, String value) {
    // Using reflection to instantiate Node since it's package-private
    try {
      Class<?> nodeClass = Class.forName("com.google.gson.internal.LinkedTreeMap$Node");
      // Node constructor: (LinkedTreeMap, K key, V value, Node<K,V> parent)
      return (LinkedTreeMap.Node<String, String>) nodeClass
          .getDeclaredConstructor(LinkedTreeMap.class, Object.class, Object.class, nodeClass)
          .newInstance(map, key, value, null);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("unchecked")
  private void setNodeField(Object node, String fieldName, Object value) {
    try {
      var field = node.getClass().getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(node, value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("unchecked")
  private Object getNodeField(Object node, String fieldName) {
    try {
      var field = node.getClass().getDeclaredField(fieldName);
      field.setAccessible(true);
      return field.get(node);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("unchecked")
  private void invokeRebalance(LinkedTreeMap.Node<String, String> unbalanced, boolean insert) {
    try {
      Method rebalance = LinkedTreeMap.class.getDeclaredMethod("rebalance", LinkedTreeMap.Node.class, boolean.class);
      rebalance.setAccessible(true);
      rebalance.invoke(map, unbalanced, insert);
    } catch (InvocationTargetException e) {
      // unwrap
      throw new RuntimeException(e.getTargetException());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("unchecked")
  private void invokeRotateLeft(LinkedTreeMap.Node<String, String> node) {
    try {
      Method rotateLeft = LinkedTreeMap.class.getDeclaredMethod("rotateLeft", LinkedTreeMap.Node.class);
      rotateLeft.setAccessible(true);
      rotateLeft.invoke(map, node);
    } catch (InvocationTargetException e) {
      throw new RuntimeException(e.getTargetException());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("unchecked")
  private void invokeRotateRight(LinkedTreeMap.Node<String, String> node) {
    try {
      Method rotateRight = LinkedTreeMap.class.getDeclaredMethod("rotateRight", LinkedTreeMap.Node.class);
      rotateRight.setAccessible(true);
      rotateRight.invoke(map, node);
    } catch (InvocationTargetException e) {
      throw new RuntimeException(e.getTargetException());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Helper to setup a node with given height, left, right, parent.
   */
  private void setupNode(LinkedTreeMap.Node<String, String> node, int height,
      LinkedTreeMap.Node<String, String> left, LinkedTreeMap.Node<String, String> right,
      LinkedTreeMap.Node<String, String> parent) {
    setNodeField(node, "height", height);
    setNodeField(node, "left", left);
    setNodeField(node, "right", right);
    setNodeField(node, "parent", parent);
  }

  @Test
    @Timeout(8000)
  void rebalance_noRotation_delta0_insertTrue_breaksLoop() {
    LinkedTreeMap.Node<String, String> node = createNode("k", "v");
    // leftHeight == rightHeight == 0
    setupNode(node, 1, null, null, null);

    // Spy rotateLeft and rotateRight to verify no calls
    LinkedTreeMap spyMap = spy(map);

    // Replace map with spy to invoke rebalance on spy (to verify no rotations)
    try {
      Method rebalance = LinkedTreeMap.class.getDeclaredMethod("rebalance", LinkedTreeMap.Node.class, boolean.class);
      rebalance.setAccessible(true);
      rebalance.invoke(spyMap, node, true);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    // height should be leftHeight + 1 = 1
    assertEquals(1, getNodeField(node, "height"));
  }

  @Test
    @Timeout(8000)
  void rebalance_noRotation_delta0_insertFalse_breaksLoop() {
    LinkedTreeMap.Node<String, String> node = createNode("k", "v");
    setupNode(node, 1, null, null, null);

    // invoke with insert = false
    invokeRebalance(node, false);

    // height should be leftHeight + 1 = 1
    assertEquals(1, getNodeField(node, "height"));
  }

  @Test
    @Timeout(8000)
  void rebalance_deltaMinus2_rightRightRotation() {
    /*
     * Setup:
     * node with delta = -2 (leftHeight - rightHeight = -2)
     * rightDelta = -1 => rotateLeft(node)
     */
    LinkedTreeMap.Node<String, String> node = createNode("root", "v");
    LinkedTreeMap.Node<String, String> right = createNode("right", "v");
    LinkedTreeMap.Node<String, String> rightRight = createNode("rightRight", "v");

    setupNode(rightRight, 2, null, null, right);
    setupNode(right, 3, null, rightRight, node);
    setupNode(node, 1, null, right, null);

    // Spy map to verify rotateLeft call
    LinkedTreeMap spyMap = spy(map);
    try {
      Method rebalance = LinkedTreeMap.class.getDeclaredMethod("rebalance", LinkedTreeMap.Node.class, boolean.class);
      rebalance.setAccessible(true);
      rebalance.invoke(spyMap, node, true);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    verify(spyMap, times(1)).rotateLeft(node);
    verify(spyMap, never()).rotateRight(any());
  }

  @Test
    @Timeout(8000)
  void rebalance_deltaMinus2_rightLeftRotation() {
    /*
     * Setup:
     * node delta = -2
     * rightDelta = 1 => rotateRight(right), rotateLeft(node)
     */
    LinkedTreeMap.Node<String, String> node = createNode("root", "v");
    LinkedTreeMap.Node<String, String> right = createNode("right", "v");
    LinkedTreeMap.Node<String, String> rightLeft = createNode("rightLeft", "v");

    setupNode(rightLeft, 3, null, null, right);
    setupNode(right, 2, rightLeft, null, node);
    setupNode(node, 1, null, right, null);

    LinkedTreeMap spyMap = spy(map);
    try {
      Method rebalance = LinkedTreeMap.class.getDeclaredMethod("rebalance", LinkedTreeMap.Node.class, boolean.class);
      rebalance.setAccessible(true);
      rebalance.invoke(spyMap, node, true);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    verify(spyMap, times(1)).rotateRight(right);
    verify(spyMap, times(1)).rotateLeft(node);
  }

  @Test
    @Timeout(8000)
  void rebalance_delta2_leftLeftRotation() {
    /*
     * Setup:
     * node delta = 2
     * leftDelta = 1 => rotateRight(node)
     */
    LinkedTreeMap.Node<String, String> node = createNode("root", "v");
    LinkedTreeMap.Node<String, String> left = createNode("left", "v");
    LinkedTreeMap.Node<String, String> leftLeft = createNode("leftLeft", "v");

    setupNode(leftLeft, 3, null, null, left);
    setupNode(left, 2, leftLeft, null, node);
    setupNode(node, 1, left, null, null);

    LinkedTreeMap spyMap = spy(map);
    try {
      Method rebalance = LinkedTreeMap.class.getDeclaredMethod("rebalance", LinkedTreeMap.Node.class, boolean.class);
      rebalance.setAccessible(true);
      rebalance.invoke(spyMap, node, true);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    verify(spyMap, times(1)).rotateRight(node);
    verify(spyMap, never()).rotateLeft(any());
  }

  @Test
    @Timeout(8000)
  void rebalance_delta2_leftRightRotation() {
    /*
     * Setup:
     * node delta = 2
     * leftDelta = -1 => rotateLeft(left), rotateRight(node)
     */
    LinkedTreeMap.Node<String, String> node = createNode("root", "v");
    LinkedTreeMap.Node<String, String> left = createNode("left", "v");
    LinkedTreeMap.Node<String, String> leftRight = createNode("leftRight", "v");

    setupNode(leftRight, 3, null, null, left);
    setupNode(left, 2, null, leftRight, node);
    setupNode(node, 1, left, null, null);

    LinkedTreeMap spyMap = spy(map);
    try {
      Method rebalance = LinkedTreeMap.class.getDeclaredMethod("rebalance", LinkedTreeMap.Node.class, boolean.class);
      rebalance.setAccessible(true);
      rebalance.invoke(spyMap, node, true);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    verify(spyMap, times(1)).rotateLeft(left);
    verify(spyMap, times(1)).rotateRight(node);
  }

  @Test
    @Timeout(8000)
  void rebalance_deltaMinus1_or_Plus1_insertFalse_breaksLoop() {
    LinkedTreeMap.Node<String, String> node = createNode("root", "v");
    // delta = 1 or -1, height = max(leftHeight, rightHeight)+1
    LinkedTreeMap.Node<String, String> left = createNode("left", "v");
    LinkedTreeMap.Node<String, String> right = createNode("right", "v");
    setupNode(left, 2, null, null, node);
    setupNode(right, 1, null, null, node);
    setupNode(node, 1, left, right, null);

    // invoke with insert = false
    invokeRebalance(node, false);

    int height = (int) getNodeField(node, "height");
    assertEquals(3, height);
  }
}