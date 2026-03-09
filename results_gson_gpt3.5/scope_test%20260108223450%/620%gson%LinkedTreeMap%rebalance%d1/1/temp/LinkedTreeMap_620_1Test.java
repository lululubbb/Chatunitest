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

class LinkedTreeMap_RebalanceTest {

  private LinkedTreeMap<String, String> map;

  private Method rebalanceMethod;

  @BeforeEach
  void setUp() throws NoSuchMethodException {
    map = new LinkedTreeMap<>();
    rebalanceMethod = LinkedTreeMap.class.getDeclaredMethod("rebalance", LinkedTreeMap.Node.class, boolean.class);
    rebalanceMethod.setAccessible(true);
  }

  /**
   * Helper to create a Node instance with specified parameters.
   */
  private LinkedTreeMap.Node<String, String> createNode(
      String key,
      LinkedTreeMap.Node<String, String> parent,
      LinkedTreeMap.Node<String, String> left,
      LinkedTreeMap.Node<String, String> right,
      int height) throws Exception {

    LinkedTreeMap.Node<String, String> node = new LinkedTreeMap.Node<>(key, null, parent);
    // Using reflection to set fields left, right, height, parent
    setField(node, "left", left);
    setField(node, "right", right);
    setField(node, "height", height);
    setField(node, "parent", parent);
    return node;
  }

  private void setField(Object target, String fieldName, Object value) throws Exception {
    var field = LinkedTreeMap.Node.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  /**
   * Test rebalance when delta == -2 and rightDelta == -1 triggers rotateLeft(node)
   */
  @Test
    @Timeout(8000)
  void testRebalance_DeltaMinus2_RightDeltaMinus1_InsertTrue() throws Throwable {
    // Build nodes
    // rightRight height > rightLeft height to get rightDelta == -1
    LinkedTreeMap.Node<String, String> rightRight = createNode("RRight", null, null, null, 3);
    LinkedTreeMap.Node<String, String> rightLeft = createNode("RLeft", null, null, null, 2);
    LinkedTreeMap.Node<String, String> right = createNode("Right", null, rightLeft, rightRight, 4);
    LinkedTreeMap.Node<String, String> unbalanced = createNode("Unbalanced", null, null, right, 2);

    // parent chain
    setField(rightLeft, "parent", right);
    setField(rightRight, "parent", right);
    setField(right, "parent", unbalanced);

    // Spy on map to verify rotateLeft call
    LinkedTreeMap<String, String> spyMap = spy(map);
    // Use reflection to replace original map instance
    Method rebalance = rebalanceMethod;

    // Replace rotateLeft and rotateRight with spies
    doNothing().when(spyMap).rotateLeft(any());
    doNothing().when(spyMap).rotateRight(any());

    // Invoke rebalance with insert = true
    rebalance.invoke(spyMap, unbalanced, true);

    // Verify rotateLeft called once with unbalanced node
    verify(spyMap, times(1)).rotateLeft(unbalanced);
    verify(spyMap, never()).rotateRight(any());

  }

  /**
   * Test rebalance when delta == -2 and rightDelta == 1 triggers rotateRight(right) and rotateLeft(node)
   */
  @Test
    @Timeout(8000)
  void testRebalance_DeltaMinus2_RightDeltaPlus1_InsertFalse() throws Throwable {
    // Build nodes
    // rightLeft height > rightRight height to get rightDelta == 1
    LinkedTreeMap.Node<String, String> rightRight = createNode("RRight", null, null, null, 2);
    LinkedTreeMap.Node<String, String> rightLeft = createNode("RLeft", null, null, null, 3);
    LinkedTreeMap.Node<String, String> right = createNode("Right", null, rightLeft, rightRight, 4);
    LinkedTreeMap.Node<String, String> unbalanced = createNode("Unbalanced", null, null, right, 2);

    setField(rightLeft, "parent", right);
    setField(rightRight, "parent", right);
    setField(right, "parent", unbalanced);

    LinkedTreeMap<String, String> spyMap = spy(map);
    doNothing().when(spyMap).rotateLeft(any());
    doNothing().when(spyMap).rotateRight(any());

    rebalanceMethod.invoke(spyMap, unbalanced, false);

    verify(spyMap, times(1)).rotateRight(right);
    verify(spyMap, times(1)).rotateLeft(unbalanced);
  }

  /**
   * Test rebalance when delta == 2 and leftDelta == 1 triggers rotateRight(node)
   */
  @Test
    @Timeout(8000)
  void testRebalance_DeltaPlus2_LeftDeltaPlus1_InsertTrue() throws Throwable {
    // leftLeft height > leftRight height to get leftDelta == 1
    LinkedTreeMap.Node<String, String> leftLeft = createNode("LLeft", null, null, null, 3);
    LinkedTreeMap.Node<String, String> leftRight = createNode("LRight", null, null, null, 2);
    LinkedTreeMap.Node<String, String> left = createNode("Left", null, leftLeft, leftRight, 4);
    LinkedTreeMap.Node<String, String> unbalanced = createNode("Unbalanced", null, left, null, 2);

    setField(leftLeft, "parent", left);
    setField(leftRight, "parent", left);
    setField(left, "parent", unbalanced);

    LinkedTreeMap<String, String> spyMap = spy(map);
    doNothing().when(spyMap).rotateLeft(any());
    doNothing().when(spyMap).rotateRight(any());

    rebalanceMethod.invoke(spyMap, unbalanced, true);

    verify(spyMap, times(1)).rotateRight(unbalanced);
    verify(spyMap, never()).rotateLeft(any());
  }

  /**
   * Test rebalance when delta == 2 and leftDelta == -1 triggers rotateLeft(left) and rotateRight(node)
   */
  @Test
    @Timeout(8000)
  void testRebalance_DeltaPlus2_LeftDeltaMinus1_InsertFalse() throws Throwable {
    // leftRight height > leftLeft height to get leftDelta == -1
    LinkedTreeMap.Node<String, String> leftLeft = createNode("LLeft", null, null, null, 2);
    LinkedTreeMap.Node<String, String> leftRight = createNode("LRight", null, null, null, 3);
    LinkedTreeMap.Node<String, String> left = createNode("Left", null, leftLeft, leftRight, 4);
    LinkedTreeMap.Node<String, String> unbalanced = createNode("Unbalanced", null, left, null, 2);

    setField(leftLeft, "parent", left);
    setField(leftRight, "parent", left);
    setField(left, "parent", unbalanced);

    LinkedTreeMap<String, String> spyMap = spy(map);
    doNothing().when(spyMap).rotateLeft(any());
    doNothing().when(spyMap).rotateRight(any());

    rebalanceMethod.invoke(spyMap, unbalanced, false);

    verify(spyMap, times(1)).rotateLeft(left);
    verify(spyMap, times(1)).rotateRight(unbalanced);
  }

  /**
   * Test rebalance when delta == 0 and insert == true triggers height update and break
   */
  @Test
    @Timeout(8000)
  void testRebalance_DeltaZero_InsertTrue() throws Throwable {
    LinkedTreeMap.Node<String, String> left = createNode("Left", null, null, null, 2);
    LinkedTreeMap.Node<String, String> right = createNode("Right", null, null, null, 2);
    LinkedTreeMap.Node<String, String> unbalanced = createNode("Unbalanced", null, left, right, 1);

    setField(left, "parent", unbalanced);
    setField(right, "parent", unbalanced);

    LinkedTreeMap<String, String> spyMap = spy(map);

    rebalanceMethod.invoke(spyMap, unbalanced, true);

    // height should be leftHeight + 1 = 3
    assertEquals(3, unbalanced.height);
  }

  /**
   * Test rebalance when delta == 1 or -1 and insert == false triggers height update and break
   */
  @Test
    @Timeout(8000)
  void testRebalance_DeltaPlusMinusOne_InsertFalse() throws Throwable {
    // delta == 1 case
    LinkedTreeMap.Node<String, String> left = createNode("Left", null, null, null, 3);
    LinkedTreeMap.Node<String, String> right = createNode("Right", null, null, null, 2);
    LinkedTreeMap.Node<String, String> unbalanced = createNode("Unbalanced", null, left, right, 1);

    setField(left, "parent", unbalanced);
    setField(right, "parent", unbalanced);

    LinkedTreeMap<String, String> spyMap = spy(map);

    rebalanceMethod.invoke(spyMap, unbalanced, false);

    assertEquals(4, unbalanced.height);

    // delta == -1 case
    LinkedTreeMap.Node<String, String> left2 = createNode("Left2", null, null, null, 2);
    LinkedTreeMap.Node<String, String> right2 = createNode("Right2", null, null, null, 3);
    LinkedTreeMap.Node<String, String> unbalanced2 = createNode("Unbalanced2", null, left2, right2, 1);

    setField(left2, "parent", unbalanced2);
    setField(right2, "parent", unbalanced2);

    rebalanceMethod.invoke(spyMap, unbalanced2, false);

    assertEquals(4, unbalanced2.height);
  }
}