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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LinkedTreeMap_ContainsKeyTest {

  LinkedTreeMap<String, String> map;

  @BeforeEach
  void setUp() {
    map = new LinkedTreeMap<>();
  }

  @Test
    @Timeout(8000)
  void containsKey_withNullKey_notFound() {
    assertFalse(map.containsKey(null));
  }

  @Test
    @Timeout(8000)
  void containsKey_withAbsentKey() {
    map.put("key1", "value1");
    assertFalse(map.containsKey("key2"));
  }

  @Test
    @Timeout(8000)
  void containsKey_withPresentKey() {
    map.put("key1", "value1");
    assertTrue(map.containsKey("key1"));
  }

  @Test
    @Timeout(8000)
  void containsKey_withDifferentObjectSameKey() {
    map.put("key1", "value1");
    // new String with same content
    String key = new String("key1");
    assertTrue(map.containsKey(key));
  }

  @Test
    @Timeout(8000)
  void containsKey_reflective_findByObjectReturnsNull() throws Exception {
    // Create spy to mock findByObject returning null
    LinkedTreeMap<String, String> spyMap = spy(new LinkedTreeMap<>());
    doReturn(null).when(spyMap).findByObject("missingKey");
    assertFalse(spyMap.containsKey("missingKey"));
  }

  @Test
    @Timeout(8000)
  void containsKey_reflective_findByObjectReturnsNode() throws Exception {
    // Use reflection to create a Node and mock findByObject to return it
    LinkedTreeMap<String, String> spyMap = spy(new LinkedTreeMap<>());

    Class<?> nodeClass = Class.forName("com.google.gson.internal.LinkedTreeMap$Node");
    // Node constructor is package-private: Node(K key, V value, Node<K,V> parent, Node<K,V> left, Node<K,V> right, int height, Node<K,V> prev, Node<K,V> next)
    // We will construct a node with minimal parameters (nulls and 1 height)
    Object node = nodeClass.getDeclaredConstructor(
        Object.class, Object.class, nodeClass, nodeClass, nodeClass, int.class, nodeClass, nodeClass)
        .newInstance("key", "value", null, null, null, 1, null, null);

    doReturn(node).when(spyMap).findByObject("key");
    assertTrue(spyMap.containsKey("key"));
  }
}