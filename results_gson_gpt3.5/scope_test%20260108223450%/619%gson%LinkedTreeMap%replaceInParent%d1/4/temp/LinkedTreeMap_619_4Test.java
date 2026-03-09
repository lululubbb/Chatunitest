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

public class LinkedTreeMap_ReplaceInParentTest {

  private LinkedTreeMap<String, String> map;

  // Helper to create Node instances reflectively
  private LinkedTreeMap.Node<String, String> createNode(String key, String value) throws Exception {
    Class<?> nodeClass = Class.forName("com.google.gson.internal.LinkedTreeMap$Node");
    // Node constructor: Node<K,V> parent, K key, V value, Node<K,V> left, Node<K,V> right, int height
    // We will find the constructor with these parameters
    var constructors = nodeClass.getDeclaredConstructors();
    for (var ctor : constructors) {
      if (ctor.getParameterCount() == 6) {
        ctor.setAccessible(true);
        return (LinkedTreeMap.Node<String, String>) ctor.newInstance(null, key, value, null, null, 1);
      }
    }
    throw new IllegalStateException("Node constructor not found");
  }

  @BeforeEach
  public void setUp() {
    map = new LinkedTreeMap<>();
  }

  @Test
    @Timeout(8000)
  public void testReplaceInParent_replacementIsNull_parentIsNull() throws Exception {
    // Node with no parent, replacement null -> root should become null
    var node = createNode("key", "value");
    // Set root to node
    Field rootField = LinkedTreeMap.class.getDeclaredField("root");
    rootField.setAccessible(true);
    rootField.set(map, node);

    // Invoke replaceInParent(node, null)
    Method replaceInParent = LinkedTreeMap.class.getDeclaredMethod("replaceInParent", 
        Class.forName("com.google.gson.internal.LinkedTreeMap$Node"), 
        Class.forName("com.google.gson.internal.LinkedTreeMap$Node"));
    replaceInParent.setAccessible(true);
    replaceInParent.invoke(map, node, null);

    // root should be null now
    assertNull(rootField.get(map));
    // node.parent should be null
    Field parentField = node.getClass().getDeclaredField("parent");
    parentField.setAccessible(true);
    assertNull(parentField.get(node));
  }

  @Test
    @Timeout(8000)
  public void testReplaceInParent_replacementIsNotNull_parentIsNull() throws Exception {
    // Node with no parent, replacement not null -> root should become replacement
    var node = createNode("key1", "value1");
    var replacement = createNode("key2", "value2");

    // Set root to node
    Field rootField = LinkedTreeMap.class.getDeclaredField("root");
    rootField.setAccessible(true);
    rootField.set(map, node);

    Method replaceInParent = LinkedTreeMap.class.getDeclaredMethod("replaceInParent", 
        Class.forName("com.google.gson.internal.LinkedTreeMap$Node"), 
        Class.forName("com.google.gson.internal.LinkedTreeMap$Node"));
    replaceInParent.setAccessible(true);
    replaceInParent.invoke(map, node, replacement);

    // root should be replacement now
    assertSame(replacement, rootField.get(map));

    // replacement.parent should be null (because node.parent was null)
    Field parentField = replacement.getClass().getDeclaredField("parent");
    parentField.setAccessible(true);
    assertNull(parentField.get(replacement));

    // node.parent should be null
    Field nodeParentField = node.getClass().getDeclaredField("parent");
    nodeParentField.setAccessible(true);
    assertNull(nodeParentField.get(node));
  }

  @Test
    @Timeout(8000)
  public void testReplaceInParent_replacementIsNull_parentIsLeftChild() throws Exception {
    // Setup parent with left child = node
    var parent = createNode("parentKey", "parentValue");
    var node = createNode("key", "value");

    // parent.left = node; node.parent = parent
    Field leftField = parent.getClass().getDeclaredField("left");
    Field rightField = parent.getClass().getDeclaredField("right");
    Field parentField = node.getClass().getDeclaredField("parent");
    leftField.setAccessible(true);
    rightField.setAccessible(true);
    parentField.setAccessible(true);

    leftField.set(parent, node);
    rightField.set(parent, null);
    parentField.set(node, parent);

    // root = parent (not null)
    Field rootField = LinkedTreeMap.class.getDeclaredField("root");
    rootField.setAccessible(true);
    rootField.set(map, parent);

    Method replaceInParent = LinkedTreeMap.class.getDeclaredMethod("replaceInParent", 
        Class.forName("com.google.gson.internal.LinkedTreeMap$Node"), 
        Class.forName("com.google.gson.internal.LinkedTreeMap$Node"));
    replaceInParent.setAccessible(true);
    replaceInParent.invoke(map, node, null);

    // parent.left should now be null
    assertNull(leftField.get(parent));
    // parent.right unchanged
    assertNull(rightField.get(parent));
    // node.parent should be null
    assertNull(parentField.get(node));
  }

  @Test
    @Timeout(8000)
  public void testReplaceInParent_replacementIsNotNull_parentIsLeftChild() throws Exception {
    var parent = createNode("parentKey", "parentValue");
    var node = createNode("key", "value");
    var replacement = createNode("replacementKey", "replacementValue");

    Field leftField = parent.getClass().getDeclaredField("left");
    Field rightField = parent.getClass().getDeclaredField("right");
    Field parentFieldNode = node.getClass().getDeclaredField("parent");
    Field parentFieldReplacement = replacement.getClass().getDeclaredField("parent");
    leftField.setAccessible(true);
    rightField.setAccessible(true);
    parentFieldNode.setAccessible(true);
    parentFieldReplacement.setAccessible(true);

    leftField.set(parent, node);
    rightField.set(parent, null);
    parentFieldNode.set(node, parent);
    parentFieldReplacement.set(replacement, null);

    Field rootField = LinkedTreeMap.class.getDeclaredField("root");
    rootField.setAccessible(true);
    rootField.set(map, parent);

    Method replaceInParent = LinkedTreeMap.class.getDeclaredMethod("replaceInParent", 
        Class.forName("com.google.gson.internal.LinkedTreeMap$Node"), 
        Class.forName("com.google.gson.internal.LinkedTreeMap$Node"));
    replaceInParent.setAccessible(true);
    replaceInParent.invoke(map, node, replacement);

    // parent.left should now be replacement
    assertSame(replacement, leftField.get(parent));
    // replacement.parent should be parent
    assertSame(parent, parentFieldReplacement.get(replacement));
    // node.parent should be null
    assertNull(parentFieldNode.get(node));
  }

  @Test
    @Timeout(8000)
  public void testReplaceInParent_replacementIsNull_parentIsRightChild() throws Exception {
    var parent = createNode("parentKey", "parentValue");
    var node = createNode("key", "value");

    Field leftField = parent.getClass().getDeclaredField("left");
    Field rightField = parent.getClass().getDeclaredField("right");
    Field parentField = node.getClass().getDeclaredField("parent");
    leftField.setAccessible(true);
    rightField.setAccessible(true);
    parentField.setAccessible(true);

    leftField.set(parent, null);
    rightField.set(parent, node);
    parentField.set(node, parent);

    Field rootField = LinkedTreeMap.class.getDeclaredField("root");
    rootField.setAccessible(true);
    rootField.set(map, parent);

    Method replaceInParent = LinkedTreeMap.class.getDeclaredMethod("replaceInParent", 
        Class.forName("com.google.gson.internal.LinkedTreeMap$Node"), 
        Class.forName("com.google.gson.internal.LinkedTreeMap$Node"));
    replaceInParent.setAccessible(true);
    replaceInParent.invoke(map, node, null);

    // parent.right should now be null
    assertNull(rightField.get(parent));
    // parent.left unchanged
    assertNull(leftField.get(parent));
    // node.parent should be null
    assertNull(parentField.get(node));
  }

  @Test
    @Timeout(8000)
  public void testReplaceInParent_replacementIsNotNull_parentIsRightChild() throws Exception {
    var parent = createNode("parentKey", "parentValue");
    var node = createNode("key", "value");
    var replacement = createNode("replacementKey", "replacementValue");

    Field leftField = parent.getClass().getDeclaredField("left");
    Field rightField = parent.getClass().getDeclaredField("right");
    Field parentFieldNode = node.getClass().getDeclaredField("parent");
    Field parentFieldReplacement = replacement.getClass().getDeclaredField("parent");
    leftField.setAccessible(true);
    rightField.setAccessible(true);
    parentFieldNode.setAccessible(true);
    parentFieldReplacement.setAccessible(true);

    leftField.set(parent, null);
    rightField.set(parent, node);
    parentFieldNode.set(node, parent);
    parentFieldReplacement.set(replacement, null);

    Field rootField = LinkedTreeMap.class.getDeclaredField("root");
    rootField.setAccessible(true);
    rootField.set(map, parent);

    Method replaceInParent = LinkedTreeMap.class.getDeclaredMethod("replaceInParent", 
        Class.forName("com.google.gson.internal.LinkedTreeMap$Node"), 
        Class.forName("com.google.gson.internal.LinkedTreeMap$Node"));
    replaceInParent.setAccessible(true);
    replaceInParent.invoke(map, node, replacement);

    // parent.right should now be replacement
    assertSame(replacement, rightField.get(parent));
    // replacement.parent should be parent
    assertSame(parent, parentFieldReplacement.get(replacement));
    // node.parent should be null
    assertNull(parentFieldNode.get(node));
  }

}