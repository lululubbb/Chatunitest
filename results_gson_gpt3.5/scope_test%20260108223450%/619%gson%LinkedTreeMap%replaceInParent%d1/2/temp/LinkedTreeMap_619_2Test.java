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
  Class<?> clazz;
  Method replaceInParentMethod;

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
  void setup() throws Exception {
    map = new LinkedTreeMap<>();
    clazz = LinkedTreeMap.class;
    replaceInParentMethod = clazz.getDeclaredMethod("replaceInParent", Object.class, Object.class);
    replaceInParentMethod.setAccessible(true);
  }

  @SuppressWarnings("unchecked")
  private Node<String, String> createNode(String key, String value) {
    return new Node<>(key, value);
  }

  @Test
    @Timeout(8000)
  void testReplaceInParent_NodeHasParent_LeftChild() throws Exception {
    // Setup nodes
    Node<String, String> parent = createNode("parent", "pval");
    Node<String, String> node = createNode("node", "val");
    Node<String, String> replacement = createNode("replacement", "rval");

    // Link nodes: parent.left = node
    Field parentField = clazz.getDeclaredField("root");
    parentField.setAccessible(true);

    node.parent = parent;
    parent.left = node;

    // root field should not be changed (not null)
    parentField.set(map, parent);

    // Invoke replaceInParent(node, replacement)
    replaceInParentMethod.invoke(map, node, replacement);

    // Assert node.parent is null
    assertNull(node.parent);

    // Assert replacement.parent is parent
    assertSame(parent, replacement.parent);

    // Assert parent's left child is replacement
    assertSame(replacement, parent.left);

    // Assert parent's right child is still null
    assertNull(parent.right);

    // root should remain parent
    assertSame(parent, parentField.get(map));
  }

  @Test
    @Timeout(8000)
  void testReplaceInParent_NodeHasParent_RightChild() throws Exception {
    Node<String, String> parent = createNode("parent", "pval");
    Node<String, String> node = createNode("node", "val");
    Node<String, String> replacement = createNode("replacement", "rval");

    node.parent = parent;
    parent.right = node;

    Field parentField = clazz.getDeclaredField("root");
    parentField.setAccessible(true);
    parentField.set(map, parent);

    replaceInParentMethod.invoke(map, node, replacement);

    assertNull(node.parent);
    assertSame(parent, replacement.parent);
    assertSame(replacement, parent.right);
    assertNull(parent.left);
    assertSame(parent, parentField.get(map));
  }

  @Test
    @Timeout(8000)
  void testReplaceInParent_NodeHasParent_ReplacementNull_LeftChild() throws Exception {
    Node<String, String> parent = createNode("parent", "pval");
    Node<String, String> node = createNode("node", "val");

    node.parent = parent;
    parent.left = node;

    Field parentField = clazz.getDeclaredField("root");
    parentField.setAccessible(true);
    parentField.set(map, parent);

    replaceInParentMethod.invoke(map, node, null);

    assertNull(node.parent);
    assertNull(parent.left);
    assertNull(parent.right);
    assertSame(parent, parentField.get(map));
  }

  @Test
    @Timeout(8000)
  void testReplaceInParent_NodeHasParent_ReplacementNull_RightChild() throws Exception {
    Node<String, String> parent = createNode("parent", "pval");
    Node<String, String> node = createNode("node", "val");

    node.parent = parent;
    parent.right = node;

    Field parentField = clazz.getDeclaredField("root");
    parentField.setAccessible(true);
    parentField.set(map, parent);

    replaceInParentMethod.invoke(map, node, null);

    assertNull(node.parent);
    assertNull(parent.right);
    assertNull(parent.left);
    assertSame(parent, parentField.get(map));
  }

  @Test
    @Timeout(8000)
  void testReplaceInParent_NodeHasNoParent_ReplacementNotNull() throws Exception {
    Node<String, String> node = createNode("node", "val");
    Node<String, String> replacement = createNode("replacement", "rval");

    // root initially null
    Field rootField = clazz.getDeclaredField("root");
    rootField.setAccessible(true);
    rootField.set(map, null);

    // node.parent is null
    node.parent = null;

    replaceInParentMethod.invoke(map, node, replacement);

    // node.parent should be null
    assertNull(node.parent);

    // replacement.parent should be null (root has no parent)
    assertNull(replacement.parent);

    // root should be replacement
    assertSame(replacement, rootField.get(map));
  }

  @Test
    @Timeout(8000)
  void testReplaceInParent_NodeHasNoParent_ReplacementNull() throws Exception {
    Node<String, String> node = createNode("node", "val");

    Field rootField = clazz.getDeclaredField("root");
    rootField.setAccessible(true);
    rootField.set(map, node);

    node.parent = null;

    replaceInParentMethod.invoke(map, node, null);

    assertNull(node.parent);

    // root should be null as replacement is null and no parent
    assertNull(rootField.get(map));
  }
}