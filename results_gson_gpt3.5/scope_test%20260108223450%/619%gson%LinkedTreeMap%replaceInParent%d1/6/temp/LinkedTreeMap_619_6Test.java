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

class LinkedTreeMapReplaceInParentTest {

  private LinkedTreeMap<String, String> map;

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

  @Test
    @Timeout(8000)
  void testReplaceInParent_replacementIsNull_parentIsNull_setsRootToNull() throws Exception {
    // Create node with no parent (root)
    Node<String, String> node = new Node<>("key", "value");
    setField(map, "root", node);

    // Call replaceInParent(node, null)
    invokeReplaceInParent(node, null);

    // root should be set to null
    assertNull(getField(map, "root"));
    // node.parent should be null
    assertNull(node.parent);
  }

  @Test
    @Timeout(8000)
  void testReplaceInParent_replacementIsNull_parentIsNotNull_leftChild() throws Exception {
    // Setup parent and node as left child
    Node<String, String> parent = new Node<>("parentKey", "parentValue");
    Node<String, String> node = new Node<>("key", "value");
    parent.left = node;
    node.parent = parent;
    setField(map, "root", parent);

    // Call replaceInParent(node, null)
    invokeReplaceInParent(node, null);

    // parent.left should be null
    assertNull(parent.left);
    // node.parent should be null
    assertNull(node.parent);
  }

  @Test
    @Timeout(8000)
  void testReplaceInParent_replacementIsNull_parentIsNotNull_rightChild() throws Exception {
    // Setup parent and node as right child
    Node<String, String> parent = new Node<>("parentKey", "parentValue");
    Node<String, String> node = new Node<>("key", "value");
    parent.right = node;
    node.parent = parent;
    setField(map, "root", parent);

    // Call replaceInParent(node, null)
    invokeReplaceInParent(node, null);

    // parent.right should be null
    assertNull(parent.right);
    // node.parent should be null
    assertNull(node.parent);
  }

  @Test
    @Timeout(8000)
  void testReplaceInParent_replacementNonNull_parentIsNull_setsRootToReplacement() throws Exception {
    // node is root
    Node<String, String> node = new Node<>("key", "value");
    Node<String, String> replacement = new Node<>("replacementKey", "replacementValue");
    setField(map, "root", node);

    // Call replaceInParent(node, replacement)
    invokeReplaceInParent(node, replacement);

    // root should be replacement
    assertSame(replacement, getField(map, "root"));
    // replacement.parent should be null
    assertNull(replacement.parent);
    // node.parent should be null
    assertNull(node.parent);
  }

  @Test
    @Timeout(8000)
  void testReplaceInParent_replacementNonNull_parentIsNotNull_leftChild() throws Exception {
    Node<String, String> parent = new Node<>("parentKey", "parentValue");
    Node<String, String> node = new Node<>("key", "value");
    Node<String, String> replacement = new Node<>("replacementKey", "replacementValue");

    parent.left = node;
    node.parent = parent;
    setField(map, "root", parent);

    invokeReplaceInParent(node, replacement);

    // parent.left should be replacement
    assertSame(replacement, parent.left);
    // replacement.parent should be parent
    assertSame(parent, replacement.parent);
    // node.parent should be null
    assertNull(node.parent);
  }

  @Test
    @Timeout(8000)
  void testReplaceInParent_replacementNonNull_parentIsNotNull_rightChild() throws Exception {
    Node<String, String> parent = new Node<>("parentKey", "parentValue");
    Node<String, String> node = new Node<>("key", "value");
    Node<String, String> replacement = new Node<>("replacementKey", "replacementValue");

    parent.right = node;
    node.parent = parent;
    setField(map, "root", parent);

    invokeReplaceInParent(node, replacement);

    // parent.right should be replacement
    assertSame(replacement, parent.right);
    // replacement.parent should be parent
    assertSame(parent, replacement.parent);
    // node.parent should be null
    assertNull(node.parent);
  }

  /**
   * Helper method to invoke the private replaceInParent method via reflection.
   */
  @SuppressWarnings("unchecked")
  private void invokeReplaceInParent(Node<String, String> node, Node<String, String> replacement)
      throws Exception {
    Method method = LinkedTreeMap.class.getDeclaredMethod("replaceInParent", Object.class, Object.class);
    method.setAccessible(true);

    // Wrap our test Node inside the actual LinkedTreeMap.Node type by copying fields
    Object actualNode = createActualNode(node);
    Object actualReplacement = replacement == null ? null : createActualNode(replacement);

    method.invoke(map, actualNode, actualReplacement);

    // Copy back parent, left, right references to our test Node objects for assertions
    copyBackNodeFields(actualNode, node);
    if (replacement != null) {
      copyBackNodeFields(actualReplacement, replacement);
    }
  }

  /**
   * Helper to create an instance of LinkedTreeMap.Node and copy fields from test Node.
   */
  private Object createActualNode(Node<String, String> testNode) throws Exception {
    Class<?> nodeClass = getNodeClass();
    Object actualNode = nodeClass.getDeclaredConstructor(Object.class, Object.class).newInstance(testNode.key, testNode.value);

    setNodeField(actualNode, "parent", testNode.parent == null ? null : createActualNode(testNode.parent));
    setNodeField(actualNode, "left", testNode.left == null ? null : createActualNode(testNode.left));
    setNodeField(actualNode, "right", testNode.right == null ? null : createActualNode(testNode.right));

    return actualNode;
  }

  /**
   * Helper to copy back parent, left, right from actualNode to testNode.
   */
  private void copyBackNodeFields(Object actualNode, Node<String, String> testNode) throws Exception {
    Object actualParent = getNodeField(actualNode, "parent");
    Object actualLeft = getNodeField(actualNode, "left");
    Object actualRight = getNodeField(actualNode, "right");

    testNode.parent = actualParent == null ? null : convertActualNodeToTestNode(actualParent);
    testNode.left = actualLeft == null ? null : convertActualNodeToTestNode(actualLeft);
    testNode.right = actualRight == null ? null : convertActualNodeToTestNode(actualRight);
  }

  /**
   * Convert actual LinkedTreeMap.Node instance back to test Node instance.
   * We only return a shallow Node with key and value, parent/left/right will be null to avoid cycles.
   */
  private Node<String, String> convertActualNodeToTestNode(Object actualNode) throws Exception {
    if (actualNode == null) {
      return null;
    }
    Object key = getNodeField(actualNode, "key");
    Object value = getNodeField(actualNode, "value");
    Node<String, String> node = new Node<>((String) key, (String) value);
    // For safety, parent/left/right are left null here to avoid infinite recursion
    return node;
  }

  private Class<?> getNodeClass() throws ClassNotFoundException {
    for (Class<?> innerClass : LinkedTreeMap.class.getDeclaredClasses()) {
      if (innerClass.getSimpleName().equals("Node")) {
        return innerClass;
      }
    }
    throw new ClassNotFoundException("LinkedTreeMap.Node class not found");
  }

  private void setNodeField(Object nodeInstance, String fieldName, Object value) throws Exception {
    Field field = getNodeClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(nodeInstance, value);
  }

  private Object getNodeField(Object nodeInstance, String fieldName) throws Exception {
    Field field = getNodeClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(nodeInstance);
  }

  /**
   * Helper method to set private fields via reflection.
   */
  private void setField(Object target, String fieldName, Object value) throws Exception {
    Field field = LinkedTreeMap.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  /**
   * Helper method to get private fields via reflection.
   */
  private Object getField(Object target, String fieldName) throws Exception {
    Field field = LinkedTreeMap.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(target);
  }
}