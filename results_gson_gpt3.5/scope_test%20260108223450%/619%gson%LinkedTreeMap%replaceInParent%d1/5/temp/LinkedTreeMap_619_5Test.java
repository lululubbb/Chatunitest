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

class LinkedTreeMap_ReplaceInParentTest {

  LinkedTreeMap<String, String> map;

  // Helper to create Node instances via reflection
  static class Node<K, V> {
    K key;
    V value;
    Node<K, V> parent;
    Node<K, V> left;
    Node<K, V> right;

    Node(K key, V value) {
      this.key = key;
      this.value = value;
    }
  }

  @BeforeEach
  void setUp() {
    map = new LinkedTreeMap<>();
  }

  @SuppressWarnings("unchecked")
  private Object createNode(String key, String value) throws Exception {
    // Node is package-private inner class, create via reflection
    Class<?> nodeClass = null;
    for (Class<?> c : LinkedTreeMap.class.getDeclaredClasses()) {
      if ("Node".equals(c.getSimpleName())) {
        nodeClass = c;
        break;
      }
    }
    assertNotNull(nodeClass);
    // find constructor with (K key, V value)
    var ctor = nodeClass.getDeclaredConstructor(Object.class, Object.class);
    ctor.setAccessible(true);
    return ctor.newInstance(key, value);
  }

  @SuppressWarnings("unchecked")
  private void setField(Object target, String fieldName, Object value) throws Exception {
    Field f = target.getClass().getDeclaredField(fieldName);
    f.setAccessible(true);
    f.set(target, value);
  }

  @SuppressWarnings("unchecked")
  private Object getField(Object target, String fieldName) throws Exception {
    Field f = target.getClass().getDeclaredField(fieldName);
    f.setAccessible(true);
    return f.get(target);
  }

  private void invokeReplaceInParent(Object node, Object replacement) throws Exception {
    Method m = LinkedTreeMap.class.getDeclaredMethod("replaceInParent", 
        node.getClass(), node.getClass());
    m.setAccessible(true);
    m.invoke(map, node, replacement);
  }

  @Test
    @Timeout(8000)
  void replaceInParent_replacementIsNull_parentExists_leftChild() throws Exception {
    Object parent = createNode("parent", "pVal");
    Object node = createNode("node", "nVal");
    // Setup parent.left = node
    setField(parent, "left", node);
    setField(parent, "right", null);
    setField(node, "parent", parent);

    // Set root to some dummy node to verify root is unchanged
    Object root = createNode("root", "rVal");
    Field rootField = LinkedTreeMap.class.getDeclaredField("root");
    rootField.setAccessible(true);
    rootField.set(map, root);

    // replacement is null
    Object replacement = null;

    invokeReplaceInParent(node, replacement);

    // node.parent should be null
    assertNull(getField(node, "parent"));
    // parent's left should be null (replacement)
    assertNull(getField(parent, "left"));
    // parent's right unchanged
    assertNull(getField(parent, "right"));
    // root unchanged
    assertSame(root, rootField.get(map));
  }

  @Test
    @Timeout(8000)
  void replaceInParent_replacementIsNull_parentExists_rightChild() throws Exception {
    Object parent = createNode("parent", "pVal");
    Object node = createNode("node", "nVal");
    // Setup parent.right = node
    setField(parent, "left", null);
    setField(parent, "right", node);
    setField(node, "parent", parent);

    Object root = createNode("root", "rVal");
    Field rootField = LinkedTreeMap.class.getDeclaredField("root");
    rootField.setAccessible(true);
    rootField.set(map, root);

    Object replacement = null;

    invokeReplaceInParent(node, replacement);

    // node.parent should be null
    assertNull(getField(node, "parent"));
    // parent's right should be null (replacement)
    assertNull(getField(parent, "right"));
    // parent's left unchanged
    assertNull(getField(parent, "left"));
    // root unchanged
    assertSame(root, rootField.get(map));
  }

  @Test
    @Timeout(8000)
  void replaceInParent_replacementNotNull_parentExists_leftChild() throws Exception {
    Object parent = createNode("parent", "pVal");
    Object node = createNode("node", "nVal");
    Object replacement = createNode("replacement", "rVal");
    // parent.left = node
    setField(parent, "left", node);
    setField(parent, "right", null);
    setField(node, "parent", parent);

    Field rootField = LinkedTreeMap.class.getDeclaredField("root");
    rootField.setAccessible(true);
    Object root = createNode("root", "rootVal");
    rootField.set(map, root);

    invokeReplaceInParent(node, replacement);

    // node.parent should be null
    assertNull(getField(node, "parent"));
    // replacement.parent should be parent
    assertSame(parent, getField(replacement, "parent"));
    // parent's left should be replacement
    assertSame(replacement, getField(parent, "left"));
    // parent's right unchanged
    assertNull(getField(parent, "right"));
    // root unchanged
    assertSame(root, rootField.get(map));
  }

  @Test
    @Timeout(8000)
  void replaceInParent_replacementNotNull_parentExists_rightChild() throws Exception {
    Object parent = createNode("parent", "pVal");
    Object node = createNode("node", "nVal");
    Object replacement = createNode("replacement", "rVal");
    // parent.right = node
    setField(parent, "left", null);
    setField(parent, "right", node);
    setField(node, "parent", parent);

    Field rootField = LinkedTreeMap.class.getDeclaredField("root");
    rootField.setAccessible(true);
    Object root = createNode("root", "rootVal");
    rootField.set(map, root);

    invokeReplaceInParent(node, replacement);

    // node.parent should be null
    assertNull(getField(node, "parent"));
    // replacement.parent should be parent
    assertSame(parent, getField(replacement, "parent"));
    // parent's right should be replacement
    assertSame(replacement, getField(parent, "right"));
    // parent's left unchanged
    assertNull(getField(parent, "left"));
    // root unchanged
    assertSame(root, rootField.get(map));
  }

  @Test
    @Timeout(8000)
  void replaceInParent_replacementNotNull_parentIsNull_setsRoot() throws Exception {
    Object node = createNode("node", "nVal");
    Object replacement = createNode("replacement", "rVal");
    // node.parent is null
    setField(node, "parent", null);

    Field rootField = LinkedTreeMap.class.getDeclaredField("root");
    rootField.setAccessible(true);
    // root initially null
    rootField.set(map, null);

    invokeReplaceInParent(node, replacement);

    // node.parent should be null
    assertNull(getField(node, "parent"));
    // replacement.parent should be null (same as node.parent)
    assertNull(getField(replacement, "parent"));
    // root should be replacement
    assertSame(replacement, rootField.get(map));
  }

  @Test
    @Timeout(8000)
  void replaceInParent_replacementNull_parentIsNull_setsRootToNull() throws Exception {
    Object node = createNode("node", "nVal");
    // node.parent is null
    setField(node, "parent", null);

    Field rootField = LinkedTreeMap.class.getDeclaredField("root");
    rootField.setAccessible(true);
    Object oldRoot = createNode("oldRoot", "old");
    rootField.set(map, oldRoot);

    invokeReplaceInParent(node, null);

    // node.parent should be null
    assertNull(getField(node, "parent"));
    // root should be null (replacement is null)
    assertNull(rootField.get(map));
  }
}