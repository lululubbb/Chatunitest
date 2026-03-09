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

import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.internal.LinkedTreeMap.Node;

import java.lang.reflect.Method;
import java.util.Map.Entry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LinkedTreeMap_findByEntry_Test {

  LinkedTreeMap<String, String> map;

  @BeforeEach
  void setUp() {
    map = new LinkedTreeMap<>();
  }

  @Test
    @Timeout(8000)
  void findByEntry_nullEntry_returnsNull() throws Exception {
    Entry<?, ?> entry = null;
    // Use reflection to invoke private method
    Method method = LinkedTreeMap.class.getDeclaredMethod("findByEntry", Entry.class);
    method.setAccessible(true);
    Node<String, String> result = (Node<String, String>) method.invoke(map, entry);
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void findByEntry_entryWithNullKeyAndValueNotFound_returnsNull() throws Exception {
    Entry<?, ?> entry = mock(Entry.class);
    when(entry.getKey()).thenReturn(null);

    LinkedTreeMap<String, String> spyMap = spy(map);
    Method findByObject = LinkedTreeMap.class.getDeclaredMethod("findByObject", Object.class);
    findByObject.setAccessible(true);
    doReturn(null).when(spyMap).findByObject(null);

    Method method = LinkedTreeMap.class.getDeclaredMethod("findByEntry", Entry.class);
    method.setAccessible(true);
    Node<String, String> result = (Node<String, String>) method.invoke(spyMap, entry);
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void findByEntry_entryFoundWithEqualValue_returnsNode() throws Exception {
    Entry<String, String> entry = mock(Entry.class);
    when(entry.getKey()).thenReturn("key1");
    when(entry.getValue()).thenReturn("value1");

    Node<String, String> node = new Node<>("key1", "value1", null, 0);

    LinkedTreeMap<String, String> spyMap = spy(map);
    doReturn(node).when(spyMap).findByObject("key1");

    Method method = LinkedTreeMap.class.getDeclaredMethod("findByEntry", Entry.class);
    method.setAccessible(true);
    Node<String, String> result = (Node<String, String>) method.invoke(spyMap, entry);
    assertNotNull(result);
    assertEquals(node, result);
  }

  @Test
    @Timeout(8000)
  void findByEntry_entryFoundWithNonEqualValue_returnsNull() throws Exception {
    Entry<String, String> entry = mock(Entry.class);
    when(entry.getKey()).thenReturn("key1");
    when(entry.getValue()).thenReturn("value2");

    Node<String, String> node = new Node<>("key1", "value1", null, 0);

    LinkedTreeMap<String, String> spyMap = spy(map);
    doReturn(node).when(spyMap).findByObject("key1");

    Method method = LinkedTreeMap.class.getDeclaredMethod("findByEntry", Entry.class);
    method.setAccessible(true);
    Node<String, String> result = (Node<String, String>) method.invoke(spyMap, entry);
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void findByEntry_entryFoundWithBothNullValues_returnsNode() throws Exception {
    Entry<String, String> entry = mock(Entry.class);
    when(entry.getKey()).thenReturn("key1");
    when(entry.getValue()).thenReturn(null);

    Node<String, String> node = new Node<>("key1", null, null, 0);

    LinkedTreeMap<String, String> spyMap = spy(map);
    doReturn(node).when(spyMap).findByObject("key1");

    Method method = LinkedTreeMap.class.getDeclaredMethod("findByEntry", Entry.class);
    method.setAccessible(true);
    Node<String, String> result = (Node<String, String>) method.invoke(spyMap, entry);
    assertNotNull(result);
    assertEquals(node, result);
  }

  // Helper constructor for Node class since it is package-private
  static class Node<K, V> implements Entry<K, V> {
    final K key;
    V value;
    Node<K, V> parent;
    int height;

    Node(K key, V value, Node<K, V> parent, int height) {
      this.key = key;
      this.value = value;
      this.parent = parent;
      this.height = height;
    }

    @Override
    public K getKey() {
      return key;
    }

    @Override
    public V getValue() {
      return value;
    }

    @Override
    public V setValue(V value) {
      V old = this.value;
      this.value = value;
      return old;
    }

    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Entry)) return false;
      Entry<?, ?> e = (Entry<?, ?>) o;
      return (key == null ? e.getKey() == null : key.equals(e.getKey())) &&
          (value == null ? e.getValue() == null : value.equals(e.getValue()));
    }

    @Override
    public int hashCode() {
      return (key == null ? 0 : key.hashCode()) ^
          (value == null ? 0 : value.hashCode());
    }
  }
}