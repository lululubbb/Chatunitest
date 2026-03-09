package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.AbstractSet;
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
import java.util.Comparator;

class LinkedTreeMap_FindByObjectTest {

  LinkedTreeMap<String, String> map;

  @BeforeEach
  void setUp() {
    // Use natural ordering comparator (null comparator)
    map = new LinkedTreeMap<>();
  }

  @Test
    @Timeout(8000)
  void findByObject_withExistingKey_returnsNode() throws Exception {
    // Put a key-value pair
    map.put("key1", "value1");

    // Access private findByObject via reflection
    Method findByObject = LinkedTreeMap.class.getDeclaredMethod("findByObject", Object.class);
    findByObject.setAccessible(true);

    @SuppressWarnings("unchecked")
    Node<String, String> node = (Node<String, String>) findByObject.invoke(map, "key1");
    assertNotNull(node);
    assertEquals("key1", node.key);
    assertEquals("value1", node.value);
  }

  @Test
    @Timeout(8000)
  void findByObject_withNonExistingKey_returnsNull() throws Exception {
    Method findByObject = LinkedTreeMap.class.getDeclaredMethod("findByObject", Object.class);
    findByObject.setAccessible(true);

    @SuppressWarnings("unchecked")
    Node<String, String> node = (Node<String, String>) findByObject.invoke(map, "nonexistent");
    assertNull(node);
  }

  @Test
    @Timeout(8000)
  void findByObject_withNullKey_returnsNull() throws Exception {
    Method findByObject = LinkedTreeMap.class.getDeclaredMethod("findByObject", Object.class);
    findByObject.setAccessible(true);

    @SuppressWarnings("unchecked")
    Node<String, String> node = (Node<String, String>) findByObject.invoke(map, (Object) null);
    assertNull(node);
  }

  @Test
    @Timeout(8000)
  void findByObject_withWrongKeyType_returnsNull() throws Exception {
    // Create a LinkedTreeMap with String keys
    LinkedTreeMap<String, String> stringMap = new LinkedTreeMap<>();

    // Put a valid entry
    stringMap.put("key", "value");

    // Access private findByObject via reflection
    Method findByObject = LinkedTreeMap.class.getDeclaredMethod("findByObject", Object.class);
    findByObject.setAccessible(true);

    Node<String, String> node = null;
    try {
      @SuppressWarnings("unchecked")
      Node<String, String> result = (Node<String, String>) findByObject.invoke(stringMap, 12345);
      node = result;
    } catch (Exception e) {
      Throwable cause = e.getCause();
      if (!(cause instanceof ClassCastException)) {
        throw e;
      }
      // expected ClassCastException wrapped, so node remains null
    }
    assertNull(node);
  }

  @Test
    @Timeout(8000)
  void findByObject_withComparatorDifferentKeyType_returnsNull() throws Exception {
    // Create a LinkedTreeMap with comparator that only accepts Integers (simulate)
    @SuppressWarnings("unchecked")
    Comparator<Object> comparator = mock(Comparator.class);
    // Mock comparator to throw ClassCastException when comparing a String key
    when(comparator.compare(any(), any())).thenAnswer(invocation -> {
      Object arg1 = invocation.getArgument(0);
      Object arg2 = invocation.getArgument(1);
      if (!(arg1 instanceof Integer) || !(arg2 instanceof Integer)) {
        throw new ClassCastException();
      }
      return ((Integer) arg1).compareTo((Integer) arg2);
    });

    LinkedTreeMap<Object, String> cmpMap = new LinkedTreeMap<>(comparator, false);
    cmpMap.put(1, "one");

    Method findByObject = LinkedTreeMap.class.getDeclaredMethod("findByObject", Object.class);
    findByObject.setAccessible(true);

    Node<Object, String> node = null;
    try {
      @SuppressWarnings("unchecked")
      Node<Object, String> result = (Node<Object, String>) findByObject.invoke(cmpMap, "stringKey");
      node = result;
    } catch (Exception e) {
      Throwable cause = e.getCause();
      if (!(cause instanceof ClassCastException)) {
        throw e;
      }
      // expected ClassCastException wrapped, so node remains null
    }
    assertNull(node);
  }
}