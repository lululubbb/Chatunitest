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

  private LinkedTreeMap<Integer, String> map;

  // Helper to create Node instances reflectively
  private LinkedTreeMap.Node<Integer, String> createNode(
      LinkedTreeMap.Node<Integer, String> parent,
      LinkedTreeMap.Node<Integer, String> left,
      LinkedTreeMap.Node<Integer, String> right,
      int height,
      Integer key,
      String value) throws Exception {
    LinkedTreeMap.Node<Integer, String> node =
        (LinkedTreeMap.Node<Integer, String>) Class.forName("com.google.gson.internal.LinkedTreeMap$Node")
            .getDeclaredConstructor(Object.class, Object.class, LinkedTreeMap.Node.class)
            .newInstance(key, value, parent);
    setField(node, "left", left);
    setField(node, "right", right);
    setField(node, "height", height);
    setField(node, "parent", parent);
    return node;
  }

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

  private void invokeRebalance(LinkedTreeMap.Node<Integer, String> node, boolean insert) throws Exception {
    Method rebalance = LinkedTreeMap.class.getDeclaredMethod("rebalance", 
        Class.forName("com.google.gson.internal.LinkedTreeMap$Node"), boolean.class);
    rebalance.setAccessible(true);
    rebalance.invoke(map, node, insert);
  }

  @BeforeEach
  void setUp() {
    map = new LinkedTreeMap<>();
  }

  @Test
    @Timeout(8000)
  void rebalance_deltaMinus2_rightRight_rotateLeft() throws Exception {
    // Setup nodes for right-right case (delta == -2, rightDelta == -1)
    LinkedTreeMap.Node<Integer, String> rightRight = createNode(null, null, null, 2, 3, "v3");
    LinkedTreeMap.Node<Integer, String> right = createNode(null, null, rightRight, 3, 2, "v2");
    setField(rightRight, "parent", right);
    LinkedTreeMap.Node<Integer, String> unbalanced = createNode(null, null, right, 1, 1, "v1");
    setField(right, "parent", unbalanced);

    // Spy map to verify rotateLeft call
    LinkedTreeMap<Integer, String> spyMap = spy(map);
    setField(spyMap, "root", unbalanced);

    Method rebalance = LinkedTreeMap.class.getDeclaredMethod("rebalance",
        Class.forName("com.google.gson.internal.LinkedTreeMap$Node"), boolean.class);
    rebalance.setAccessible(true);

    // Replace rotateLeft and rotateRight to track calls
    doNothing().when(spyMap).rotateLeft(any());
    doNothing().when(spyMap).rotateRight(any());

    rebalance.invoke(spyMap, unbalanced, true);

    verify(spyMap, times(1)).rotateLeft(unbalanced);
    verify(spyMap, never()).rotateRight(any());

    // height of unbalanced should be updated or break occurred
  }

  @Test
    @Timeout(8000)
  void rebalance_deltaMinus2_rightLeft_rotateRightThenLeft() throws Exception {
    // Setup nodes for right-left case (delta == -2, rightDelta == 1)
    LinkedTreeMap.Node<Integer, String> rightLeft = createNode(null, null, null, 2, 4, "v4");
    LinkedTreeMap.Node<Integer, String> rightRight = createNode(null, null, null, 1, 5, "v5");
    LinkedTreeMap.Node<Integer, String> right = createNode(null, rightLeft, rightRight, 3, 2, "v2");
    setField(rightLeft, "parent", right);
    setField(rightRight, "parent", right);
    LinkedTreeMap.Node<Integer, String> unbalanced = createNode(null, null, right, 1, 1, "v1");
    setField(right, "parent", unbalanced);

    LinkedTreeMap<Integer, String> spyMap = spy(map);
    setField(spyMap, "root", unbalanced);

    Method rebalance = LinkedTreeMap.class.getDeclaredMethod("rebalance",
        Class.forName("com.google.gson.internal.LinkedTreeMap$Node"), boolean.class);
    rebalance.setAccessible(true);

    doNothing().when(spyMap).rotateLeft(any());
    doNothing().when(spyMap).rotateRight(any());

    rebalance.invoke(spyMap, unbalanced, true);

    verify(spyMap, times(1)).rotateRight(right);
    verify(spyMap, times(1)).rotateLeft(unbalanced);
  }

  @Test
    @Timeout(8000)
  void rebalance_deltaPlus2_leftLeft_rotateRight() throws Exception {
    // Setup nodes for left-left case (delta == 2, leftDelta == 1)
    LinkedTreeMap.Node<Integer, String> leftLeft = createNode(null, null, null, 2, 1, "v1");
    LinkedTreeMap.Node<Integer, String> left = createNode(null, leftLeft, null, 3, 2, "v2");
    setField(leftLeft, "parent", left);
    LinkedTreeMap.Node<Integer, String> unbalanced = createNode(null, left, null, 1, 3, "v3");
    setField(left, "parent", unbalanced);

    LinkedTreeMap<Integer, String> spyMap = spy(map);
    setField(spyMap, "root", unbalanced);

    Method rebalance = LinkedTreeMap.class.getDeclaredMethod("rebalance",
        Class.forName("com.google.gson.internal.LinkedTreeMap$Node"), boolean.class);
    rebalance.setAccessible(true);

    doNothing().when(spyMap).rotateRight(any());
    doNothing().when(spyMap).rotateLeft(any());

    rebalance.invoke(spyMap, unbalanced, true);

    verify(spyMap, times(1)).rotateRight(unbalanced);
    verify(spyMap, never()).rotateLeft(any());
  }

  @Test
    @Timeout(8000)
  void rebalance_deltaPlus2_leftRight_rotateLeftThenRight() throws Exception {
    // Setup nodes for left-right case (delta == 2, leftDelta == -1)
    LinkedTreeMap.Node<Integer, String> leftLeft = createNode(null, null, null, 1, 1, "v1");
    LinkedTreeMap.Node<Integer, String> leftRight = createNode(null, null, null, 2, 4, "v4");
    LinkedTreeMap.Node<Integer, String> left = createNode(null, leftLeft, leftRight, 3, 2, "v2");
    setField(leftLeft, "parent", left);
    setField(leftRight, "parent", left);
    LinkedTreeMap.Node<Integer, String> unbalanced = createNode(null, left, null, 1, 3, "v3");
    setField(left, "parent", unbalanced);

    LinkedTreeMap<Integer, String> spyMap = spy(map);
    setField(spyMap, "root", unbalanced);

    Method rebalance = LinkedTreeMap.class.getDeclaredMethod("rebalance",
        Class.forName("com.google.gson.internal.LinkedTreeMap$Node"), boolean.class);
    rebalance.setAccessible(true);

    doNothing().when(spyMap).rotateRight(any());
    doNothing().when(spyMap).rotateLeft(any());

    rebalance.invoke(spyMap, unbalanced, true);

    verify(spyMap, times(1)).rotateLeft(left);
    verify(spyMap, times(1)).rotateRight(unbalanced);
  }

  @Test
    @Timeout(8000)
  void rebalance_deltaZero_insertTrue_breaksLoop() throws Exception {
    LinkedTreeMap.Node<Integer, String> unbalanced = createNode(null, null, null, 0, 1, "v1");

    LinkedTreeMap<Integer, String> spyMap = spy(map);
    setField(spyMap, "root", unbalanced);

    Method rebalance = LinkedTreeMap.class.getDeclaredMethod("rebalance",
        Class.forName("com.google.gson.internal.LinkedTreeMap$Node"), boolean.class);
    rebalance.setAccessible(true);

    doNothing().when(spyMap).rotateRight(any());
    doNothing().when(spyMap).rotateLeft(any());

    rebalance.invoke(spyMap, unbalanced, true);

    // No rotate calls, loop breaks on delta == 0 and insert == true
    verify(spyMap, never()).rotateRight(any());
    verify(spyMap, never()).rotateLeft(any());

    int height = (int) getField(unbalanced, "height");
    assertEquals(1, height);
  }

  @Test
    @Timeout(8000)
  void rebalance_deltaOne_insertFalse_breaksLoop() throws Exception {
    LinkedTreeMap.Node<Integer, String> left = createNode(null, null, null, 1, 1, "v1");
    LinkedTreeMap.Node<Integer, String> right = createNode(null, null, null, 0, 2, "v2");
    LinkedTreeMap.Node<Integer, String> unbalanced = createNode(null, left, right, 1, 3, "v3");
    setField(left, "parent", unbalanced);
    setField(right, "parent", unbalanced);

    LinkedTreeMap<Integer, String> spyMap = spy(map);
    setField(spyMap, "root", unbalanced);

    Method rebalance = LinkedTreeMap.class.getDeclaredMethod("rebalance",
        Class.forName("com.google.gson.internal.LinkedTreeMap$Node"), boolean.class);
    rebalance.setAccessible(true);

    doNothing().when(spyMap).rotateRight(any());
    doNothing().when(spyMap).rotateLeft(any());

    rebalance.invoke(spyMap, unbalanced, false);

    // No rotate calls, loop breaks on delta == 1 and insert == false
    verify(spyMap, never()).rotateRight(any());
    verify(spyMap, never()).rotateLeft(any());

    int height = (int) getField(unbalanced, "height");
    assertEquals(2, height);
  }
}