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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class LinkedTreeMap_findByObject_Test {

  LinkedTreeMap<String, String> map;

  @BeforeEach
  void setUp() {
    map = new LinkedTreeMap<>();
  }

  @Test
    @Timeout(8000)
  void findByObject_withNullKey_returnsNull() throws Exception {
    Method findByObject = LinkedTreeMap.class.getDeclaredMethod("findByObject", Object.class);
    findByObject.setAccessible(true);

    Object result = findByObject.invoke(map, (Object) null);

    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void findByObject_withKeyClassCastException_returnsNull() throws Exception {
    LinkedTreeMap<String, String> spyMap = spy(map);

    // Use doAnswer to throw ClassCastException from find method
    doAnswer(invocation -> {
      throw new ClassCastException();
    }).when(spyMap).find(any(), eq(false));

    Method findByObject = LinkedTreeMap.class.getDeclaredMethod("findByObject", Object.class);
    findByObject.setAccessible(true);

    Object result = findByObject.invoke(spyMap, (Object) new Object());

    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void findByObject_withValidKey_returnsNode() throws Exception {
    Node<String, String> node = new Node<>("key", "value", null, 1);

    LinkedTreeMap<String, String> spyMap = spy(map);

    doReturn(node).when(spyMap).find(eq("key"), eq(false));

    Method findByObject = LinkedTreeMap.class.getDeclaredMethod("findByObject", Object.class);
    findByObject.setAccessible(true);

    Object result = findByObject.invoke(spyMap, (Object) "key");

    assertNotNull(result);
    assertSame(node, result);
  }

  static class Node<K, V> implements java.util.Map.Entry<K, V> {
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
      if (!(o instanceof java.util.Map.Entry))
        return false;
      java.util.Map.Entry<?, ?> e = (java.util.Map.Entry<?, ?>) o;
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