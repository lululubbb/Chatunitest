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

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LinkedTreeMap_ReplaceInParentTest {

  private LinkedTreeMap<String, String> map;

  // Use the actual LinkedTreeMap.Node class via reflection instead of custom Node class
  private Class<?> nodeClass;
  private Method replaceInParentMethod;
  private Field rootField;

  @BeforeEach
  void setUp() throws Exception {
    map = new LinkedTreeMap<>();

    // Get LinkedTreeMap.Node class first
    for (Class<?> innerClass : LinkedTreeMap.class.getDeclaredClasses()) {
      if ("Node".equals(innerClass.getSimpleName())) {
        nodeClass = innerClass;
        break;
      }
    }
    if (nodeClass == null) {
      throw new IllegalStateException("Node class not found");
    }

    // Access private replaceInParent method
    replaceInParentMethod = LinkedTreeMap.class.getDeclaredMethod("replaceInParent", nodeClass, nodeClass);
    replaceInParentMethod.setAccessible(true);

    // Access private root field
    rootField = LinkedTreeMap.class.getDeclaredField("root");
    rootField.setAccessible(true);
  }

  private Object createNode(String key, String value) throws Exception {
    // Node constructor: Node(K key, V value, Node<K,V> parent)
    // There is no constructor with 2 args, only with 3 args (key, value, parent)
    // So use the 3 args constructor with parent = null
    return nodeClass.getDeclaredConstructor(Object.class, Object.class, nodeClass).newInstance(key, value, null);
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

  @Test
    @Timeout(8000)
  void replaceInParent_replacesLeftChild() throws Exception {
    Object parent = createNode("parent", "pVal");
    Object node = createNode("node", "nVal");
    Object replacement = createNode("replacement", "rVal");

    setLeft(parent, node);
    setParent(node, parent);

    replaceInParentMethod.invoke(map, node, replacement);

    assertNull(getParent(node));
    assertSame(parent, getParent(replacement));
    assertSame(replacement, getLeft(parent));
    assertNull(getRight(parent));
    assertNull(rootField.get(map));
  }

  @Test
    @Timeout(8000)
  void replaceInParent_replacesRightChild() throws Exception {
    Object parent = createNode("parent", "pVal");
    Object node = createNode("node", "nVal");
    Object replacement = createNode("replacement", "rVal");

    setRight(parent, node);
    setParent(node, parent);

    replaceInParentMethod.invoke(map, node, replacement);

    assertNull(getParent(node));
    assertSame(parent, getParent(replacement));
    assertSame(replacement, getRight(parent));
    assertNull(getLeft(parent));
    assertNull(rootField.get(map));
  }

  @Test
    @Timeout(8000)
  void replaceInParent_replacementIsNull() throws Exception {
    Object parent = createNode("parent", "pVal");
    Object node = createNode("node", "nVal");

    setLeft(parent, node);
    setParent(node, parent);

    replaceInParentMethod.invoke(map, node, null);

    assertNull(getParent(node));
    assertNull(getLeft(parent));
    assertNull(getRight(parent));
    assertNull(rootField.get(map));
  }

  @Test
    @Timeout(8000)
  void replaceInParent_nodeHasNoParent_rootUpdated() throws Exception {
    Object node = createNode("node", "nVal");
    Object replacement = createNode("replacement", "rVal");

    rootField.set(map, node);

    replaceInParentMethod.invoke(map, node, replacement);

    assertNull(getParent(node));
    assertNull(getParent(replacement));
    assertSame(replacement, rootField.get(map));
  }

  @Test
    @Timeout(8000)
  void replaceInParent_nodeHasNoParent_replacementNull_rootSetNull() throws Exception {
    Object node = createNode("node", "nVal");

    rootField.set(map, node);

    replaceInParentMethod.invoke(map, node, null);

    assertNull(getParent(node));
    assertNull(rootField.get(map));
  }
}