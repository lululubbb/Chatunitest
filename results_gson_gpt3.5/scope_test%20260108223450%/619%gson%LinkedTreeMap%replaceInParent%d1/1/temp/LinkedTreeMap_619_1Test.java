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

public class LinkedTreeMap_619_1Test {

  private LinkedTreeMap<String, String> map;

  private Class<?> nodeClass;
  private Class<?> mapClass;

  @BeforeEach
  public void setUp() throws Exception {
    map = new LinkedTreeMap<>();
    mapClass = LinkedTreeMap.class;

    // Node is a package-private static inner class, get it via reflection
    for (Class<?> innerClass : mapClass.getDeclaredClasses()) {
      if ("Node".equals(innerClass.getSimpleName())) {
        nodeClass = innerClass;
        break;
      }
    }
    assertNotNull(nodeClass, "Node class not found");
  }

  private Object createNode(Object key, Object value) throws Exception {
    // Node has constructor Node(K key, V value, Node<K,V> parent)
    // We'll find a constructor with 3 params: key, value, parent
    for (var constructor : nodeClass.getDeclaredConstructors()) {
      if (constructor.getParameterCount() == 3) {
        constructor.setAccessible(true);
        return constructor.newInstance(key, value, null);
      }
    }
    fail("Node constructor not found");
    return null;
  }

  private void setParent(Object node, Object parent) throws Exception {
    Field parentField = nodeClass.getDeclaredField("parent");
    parentField.setAccessible(true);
    parentField.set(node, parent);
  }

  private Object getParent(Object node) throws Exception {
    Field parentField = nodeClass.getDeclaredField("parent");
    parentField.setAccessible(true);
    return parentField.get(node);
  }

  private void setLeft(Object node, Object left) throws Exception {
    Field leftField = nodeClass.getDeclaredField("left");
    leftField.setAccessible(true);
    leftField.set(node, left);
  }

  private Object getLeft(Object node) throws Exception {
    Field leftField = nodeClass.getDeclaredField("left");
    leftField.setAccessible(true);
    return leftField.get(node);
  }

  private void setRight(Object node, Object right) throws Exception {
    Field rightField = nodeClass.getDeclaredField("right");
    rightField.setAccessible(true);
    rightField.set(node, right);
  }

  private Object getRight(Object node) throws Exception {
    Field rightField = nodeClass.getDeclaredField("right");
    rightField.setAccessible(true);
    return rightField.get(node);
  }

  private Object getRoot() throws Exception {
    Field rootField = mapClass.getDeclaredField("root");
    rootField.setAccessible(true);
    return rootField.get(map);
  }

  private void setRoot(Object root) throws Exception {
    Field rootField = mapClass.getDeclaredField("root");
    rootField.setAccessible(true);
    rootField.set(map, root);
  }

  private void invokeReplaceInParent(Object node, Object replacement) throws Exception {
    Method method = mapClass.getDeclaredMethod("replaceInParent", nodeClass, nodeClass);
    method.setAccessible(true);
    method.invoke(map, node, replacement);
  }

  @Test
    @Timeout(8000)
  public void testReplaceInParent_NodeHasParent_LeftChildReplaced() throws Exception {
    // Setup parent node
    Object parent = createNode("parentKey", "parentValue");
    // Setup node to replace
    Object node = createNode("nodeKey", "nodeValue");
    // Setup replacement node
    Object replacement = createNode("replacementKey", "replacementValue");

    // Setup tree links
    setParent(node, parent);
    setLeft(parent, node);
    setRight(parent, null);

    // replacement's parent should be set to parent after method call
    setParent(replacement, null);

    // root should remain unchanged
    setRoot(parent);

    // Call method
    invokeReplaceInParent(node, replacement);

    // Assertions
    assertNull(getParent(node), "Original node's parent should be null");
    assertSame(parent, getParent(replacement), "Replacement's parent should be set to parent");
    assertSame(replacement, getLeft(parent), "Parent's left child should be replacement");
    assertNull(getRight(parent), "Parent's right child should remain null");
    assertSame(parent, getRoot(), "Root should remain the same");
  }

  @Test
    @Timeout(8000)
  public void testReplaceInParent_NodeHasParent_RightChildReplaced() throws Exception {
    Object parent = createNode("parentKey", "parentValue");
    Object node = createNode("nodeKey", "nodeValue");
    Object replacement = createNode("replacementKey", "replacementValue");

    setParent(node, parent);
    setRight(parent, node);
    setLeft(parent, null);

    setParent(replacement, null);
    setRoot(parent);

    invokeReplaceInParent(node, replacement);

    assertNull(getParent(node), "Original node's parent should be null");
    assertSame(parent, getParent(replacement), "Replacement's parent should be set to parent");
    assertSame(replacement, getRight(parent), "Parent's right child should be replacement");
    assertNull(getLeft(parent), "Parent's left child should remain null");
    assertSame(parent, getRoot(), "Root should remain the same");
  }

  @Test
    @Timeout(8000)
  public void testReplaceInParent_NodeHasNoParent_RootReplaced() throws Exception {
    Object node = createNode("nodeKey", "nodeValue");
    Object replacement = createNode("replacementKey", "replacementValue");

    setParent(node, null);
    setParent(replacement, null);
    setRoot(node);

    invokeReplaceInParent(node, replacement);

    assertNull(getParent(node), "Original node's parent should be null");
    assertNull(getParent(replacement), "Replacement's parent should be null since node's parent was null");
    assertSame(replacement, getRoot(), "Root should be replaced by replacement");
  }

  @Test
    @Timeout(8000)
  public void testReplaceInParent_ReplacementIsNull_NodeRemoved() throws Exception {
    Object parent = createNode("parentKey", "parentValue");
    Object node = createNode("nodeKey", "nodeValue");

    setParent(node, parent);
    setLeft(parent, node);
    setRight(parent, null);
    setRoot(parent);

    invokeReplaceInParent(node, null);

    assertNull(getParent(node), "Original node's parent should be null");
    assertNull(getLeft(parent), "Parent's left child should be null after removal");
    assertNull(getRight(parent), "Parent's right child should remain null");
    assertSame(parent, getRoot(), "Root should remain the same");
  }

  @Test
    @Timeout(8000)
  public void testReplaceInParent_ReplacementIsNull_NodeIsRoot() throws Exception {
    Object node = createNode("nodeKey", "nodeValue");
    setParent(node, null);
    setRoot(node);

    invokeReplaceInParent(node, null);

    assertNull(getParent(node), "Original node's parent should be null");
    assertNull(getRoot(), "Root should be null after replacement with null");
  }
}