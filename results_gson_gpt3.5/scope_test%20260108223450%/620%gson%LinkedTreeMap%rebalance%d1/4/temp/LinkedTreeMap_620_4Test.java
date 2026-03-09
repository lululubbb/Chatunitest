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

  LinkedTreeMap<String, String> map;
  Class<?> clazz;
  Method rebalanceMethod;
  Method rotateLeftMethod;
  Method rotateRightMethod;
  Field rootField;

  @BeforeEach
  void setUp() throws Exception {
    map = new LinkedTreeMap<>();
    clazz = LinkedTreeMap.class;
    rebalanceMethod = clazz.getDeclaredMethod("rebalance", clazz.getDeclaredClasses()[0], boolean.class);
    rebalanceMethod.setAccessible(true);
    rotateLeftMethod = clazz.getDeclaredMethod("rotateLeft", clazz.getDeclaredClasses()[0]);
    rotateLeftMethod.setAccessible(true);
    rotateRightMethod = clazz.getDeclaredMethod("rotateRight", clazz.getDeclaredClasses()[0]);
    rotateRightMethod.setAccessible(true);
    rootField = clazz.getDeclaredField("root");
    rootField.setAccessible(true);
  }

  /**
   * Helper to create a Node instance with given parameters.
   */
  private Object createNode(String key, int height, Object left, Object right, Object parent) throws Exception {
    Class<?> nodeClass = clazz.getDeclaredClasses()[0];
    Object node = nodeClass.getDeclaredConstructor(LinkedTreeMap.class, Object.class, Object.class).newInstance(map, key, null);
    // set height
    Field heightField = nodeClass.getDeclaredField("height");
    heightField.setAccessible(true);
    heightField.setInt(node, height);
    // set left
    Field leftField = nodeClass.getDeclaredField("left");
    leftField.setAccessible(true);
    leftField.set(node, left);
    // set right
    Field rightField = nodeClass.getDeclaredField("right");
    rightField.setAccessible(true);
    rightField.set(node, right);
    // set parent
    Field parentField = nodeClass.getDeclaredField("parent");
    parentField.setAccessible(true);
    parentField.set(node, parent);
    return node;
  }

  /**
   * Helper to get height of a node.
   */
  private int getHeight(Object node) throws Exception {
    if (node == null) return 0;
    Field heightField = node.getClass().getDeclaredField("height");
    heightField.setAccessible(true);
    return heightField.getInt(node);
  }

  /**
   * Helper to set height of a node.
   */
  private void setHeight(Object node, int height) throws Exception {
    Field heightField = node.getClass().getDeclaredField("height");
    heightField.setAccessible(true);
    heightField.setInt(node, height);
  }

  /**
   * Helper to set parent of a node.
   */
  private void setParent(Object node, Object parent) throws Exception {
    Field parentField = node.getClass().getDeclaredField("parent");
    parentField.setAccessible(true);
    parentField.set(node, parent);
  }

  /**
   * Helper to set left child of a node.
   */
  private void setLeft(Object node, Object left) throws Exception {
    Field leftField = node.getClass().getDeclaredField("left");
    leftField.setAccessible(true);
    leftField.set(node, left);
  }

  /**
   * Helper to set right child of a node.
   */
  private void setRight(Object node, Object right) throws Exception {
    Field rightField = node.getClass().getDeclaredField("right");
    rightField.setAccessible(true);
    rightField.set(node, right);
  }

  /**
   * Test rebalance when delta == -2 and rightDelta == -1 (right-right case)
   */
  @Test
    @Timeout(8000)
  void testRebalance_RightRight_InsertTrue() throws Exception {
    // Build nodes for right-right case
    Object rightRight = createNode("rr", 1, null, null, null);
    Object right = createNode("r", 2, null, rightRight, null);
    setParent(rightRight, right);
    Object unbalanced = createNode("u", 3, null, right, null);
    setParent(right, unbalanced);

    // Spy on map to verify rotateLeft called once, rotateRight not called
    LinkedTreeMap<String, String> spyMap = spy(map);

    // Replace rotateLeft and rotateRight with spies
    doNothing().when(spyMap).rotateLeft(unbalanced);
    doNothing().when(spyMap).rotateRight(right);

    // Use reflection to invoke rebalance on spyMap
    Method rebalanceSpy = clazz.getDeclaredMethod("rebalance", clazz.getDeclaredClasses()[0], boolean.class);
    rebalanceSpy.setAccessible(true);

    // Because rebalance is private, invoke on spyMap with unbalanced node and insert=true
    rebalanceSpy.invoke(spyMap, unbalanced, true);

    // Verify rotateLeft called once with unbalanced
    verify(spyMap, times(1)).rotateLeft(unbalanced);
    // Verify rotateRight never called
    verify(spyMap, never()).rotateRight(any());

  }

  /**
   * Test rebalance when delta == -2 and rightDelta == 1 (right-left case)
   */
  @Test
    @Timeout(8000)
  void testRebalance_RightLeft_InsertTrue() throws Exception {
    // Build nodes for right-left case
    Object rightLeft = createNode("rl", 2, null, null, null);
    Object rightRight = createNode("rr", 1, null, null, null);
    Object right = createNode("r", 3, rightLeft, rightRight, null);
    setParent(rightLeft, right);
    setParent(rightRight, right);
    Object unbalanced = createNode("u", 4, null, right, null);
    setParent(right, unbalanced);

    // Spy on map to verify rotateLeft and rotateRight called once each
    LinkedTreeMap<String, String> spyMap = spy(map);
    doNothing().when(spyMap).rotateLeft(unbalanced);
    doNothing().when(spyMap).rotateRight(right);

    Method rebalanceSpy = clazz.getDeclaredMethod("rebalance", clazz.getDeclaredClasses()[0], boolean.class);
    rebalanceSpy.setAccessible(true);

    rebalanceSpy.invoke(spyMap, unbalanced, true);

    verify(spyMap, times(1)).rotateRight(right);
    verify(spyMap, times(1)).rotateLeft(unbalanced);
  }

  /**
   * Test rebalance when delta == 2 and leftDelta == 1 (left-left case)
   */
  @Test
    @Timeout(8000)
  void testRebalance_LeftLeft_InsertTrue() throws Exception {
    // Build nodes for left-left case
    Object leftLeft = createNode("ll", 2, null, null, null);
    Object left = createNode("l", 3, leftLeft, null, null);
    setParent(leftLeft, left);
    Object unbalanced = createNode("u", 4, left, null, null);
    setParent(left, unbalanced);

    LinkedTreeMap<String, String> spyMap = spy(map);
    doNothing().when(spyMap).rotateRight(unbalanced);
    doNothing().when(spyMap).rotateLeft(left);

    Method rebalanceSpy = clazz.getDeclaredMethod("rebalance", clazz.getDeclaredClasses()[0], boolean.class);
    rebalanceSpy.setAccessible(true);

    rebalanceSpy.invoke(spyMap, unbalanced, true);

    verify(spyMap, times(1)).rotateRight(unbalanced);
    verify(spyMap, never()).rotateLeft(any());
  }

  /**
   * Test rebalance when delta == 2 and leftDelta == -1 (left-right case)
   */
  @Test
    @Timeout(8000)
  void testRebalance_LeftRight_InsertTrue() throws Exception {
    // Build nodes for left-right case
    Object leftLeft = createNode("ll", 1, null, null, null);
    Object leftRight = createNode("lr", 2, null, null, null);
    Object left = createNode("l", 3, leftLeft, leftRight, null);
    setParent(leftLeft, left);
    setParent(leftRight, left);
    Object unbalanced = createNode("u", 4, left, null, null);
    setParent(left, unbalanced);

    LinkedTreeMap<String, String> spyMap = spy(map);
    doNothing().when(spyMap).rotateRight(unbalanced);
    doNothing().when(spyMap).rotateLeft(left);
    doNothing().when(spyMap).rotateRight(leftRight);
    doNothing().when(spyMap).rotateLeft(left);

    // Actually only rotateLeft(left) and rotateRight(unbalanced) expected
    // But per code, rotateLeft(left) then rotateRight(unbalanced)

    Method rebalanceSpy = clazz.getDeclaredMethod("rebalance", clazz.getDeclaredClasses()[0], boolean.class);
    rebalanceSpy.setAccessible(true);

    // We expect rotateLeft(left) then rotateRight(unbalanced)
    rebalanceSpy.invoke(spyMap, unbalanced, true);

    verify(spyMap, times(1)).rotateLeft(left);
    verify(spyMap, times(1)).rotateRight(unbalanced);
  }

  /**
   * Test rebalance when delta == 0 and insert == true
   */
  @Test
    @Timeout(8000)
  void testRebalance_DeltaZero_InsertTrue() throws Exception {
    Object left = createNode("l", 1, null, null, null);
    Object right = createNode("r", 1, null, null, null);
    Object unbalanced = createNode("u", 1, left, right, null);
    setParent(left, unbalanced);
    setParent(right, unbalanced);

    // Initially height is 1, but leftHeight + 1 = 2
    setHeight(unbalanced, 1);

    rebalanceMethod.invoke(map, unbalanced, true);

    int newHeight = getHeight(unbalanced);
    assertEquals(2, newHeight);
  }

  /**
   * Test rebalance when delta == 0 and insert == false
   */
  @Test
    @Timeout(8000)
  void testRebalance_DeltaZero_InsertFalse() throws Exception {
    Object left = createNode("l", 1, null, null, null);
    Object right = createNode("r", 1, null, null, null);
    Object unbalanced = createNode("u", 1, left, right, null);
    setParent(left, unbalanced);
    setParent(right, unbalanced);

    setHeight(unbalanced, 1);

    rebalanceMethod.invoke(map, unbalanced, false);

    int newHeight = getHeight(unbalanced);
    assertEquals(2, newHeight);
  }

  /**
   * Test rebalance when delta == 1 or -1 and insert == false (height unchanged, break)
   */
  @Test
    @Timeout(8000)
  void testRebalance_DeltaOne_InsertFalse() throws Exception {
    Object left = createNode("l", 2, null, null, null);
    Object right = createNode("r", 1, null, null, null);
    Object unbalanced = createNode("u", 3, left, right, null);
    setParent(left, unbalanced);
    setParent(right, unbalanced);

    // delta = leftHeight - rightHeight = 2 - 1 = 1
    // height should be max(2,1)+1=3
    setHeight(unbalanced, 3);

    rebalanceMethod.invoke(map, unbalanced, false);

    int newHeight = getHeight(unbalanced);
    assertEquals(3, newHeight);
  }

  /**
   * Test rebalance when delta == 1 or -1 and insert == true (continue loop)
   */
  @Test
    @Timeout(8000)
  void testRebalance_DeltaOne_InsertTrue() throws Exception {
    Object left = createNode("l", 2, null, null, null);
    Object right = createNode("r", 1, null, null, null);
    Object parent = createNode("p", 3, null, null, null);
    Object unbalanced = createNode("u", 3, left, right, parent);
    setParent(left, unbalanced);
    setParent(right, unbalanced);
    setParent(unbalanced, parent);

    // delta = 1
    setHeight(unbalanced, 3);

    // We will call rebalance with insert=true, so loop continues up to parent
    // For testing, we just invoke rebalance and ensure no exceptions

    rebalanceMethod.invoke(map, unbalanced, true);

    int newHeight = getHeight(unbalanced);
    assertEquals(3, newHeight);
  }
}