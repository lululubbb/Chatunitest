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

import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.internal.LinkedTreeMap.Node;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Comparator;

class LinkedTreeMap_FindByObjectTest {

  private LinkedTreeMap<Object, Object> map;

  @BeforeEach
  void setUp() {
    // Use default constructor which assumes Comparable keys
    map = new LinkedTreeMap<>();
  }

  @Test
    @Timeout(8000)
  void findByObject_withNullKey_returnsNull() throws Exception {
    Method method = LinkedTreeMap.class.getDeclaredMethod("findByObject", Object.class);
    method.setAccessible(true);

    Object result = method.invoke(map, new Object[] {null});
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void findByObject_withKeyFound_returnsNode() throws Exception {
    // Put an entry so findByObject can find it
    String key = "key1";
    String value = "value1";
    map.put(key, value);

    Method method = LinkedTreeMap.class.getDeclaredMethod("findByObject", Object.class);
    method.setAccessible(true);

    @SuppressWarnings("unchecked")
    Node<Object, Object> node = (Node<Object, Object>) method.invoke(map, key);
    assertNotNull(node);
    assertEquals(key, node.key);
    assertEquals(value, node.value);
  }

  @Test
    @Timeout(8000)
  void findByObject_withKeyNotFound_returnsNull() throws Exception {
    Method method = LinkedTreeMap.class.getDeclaredMethod("findByObject", Object.class);
    method.setAccessible(true);

    @SuppressWarnings("unchecked")
    Node<Object, Object> node = (Node<Object, Object>) method.invoke(map, "nonexistent");
    assertNull(node);
  }

  @Test
    @Timeout(8000)
  void findByObject_withClassCastException_returnsNull() throws Exception {
    // Create a map with a comparator that expects Strings
    Comparator<Object> comparator = (o1, o2) -> ((String) o1).compareTo((String) o2);
    LinkedTreeMap<Object, Object> stringMap = new LinkedTreeMap<>(comparator, false);
    stringMap.put("a", "A");

    Method method = LinkedTreeMap.class.getDeclaredMethod("findByObject", Object.class);
    method.setAccessible(true);

    // Pass an Integer key that will cause ClassCastException inside find
    Object badKey = Integer.valueOf(1);
    Object result = method.invoke(stringMap, badKey);
    assertNull(result);
  }
}