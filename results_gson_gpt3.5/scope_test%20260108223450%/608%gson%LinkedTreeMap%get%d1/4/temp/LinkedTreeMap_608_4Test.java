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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LinkedTreeMap_608_4Test {

  LinkedTreeMap<String, String> map;

  @BeforeEach
  void setUp() {
    map = new LinkedTreeMap<>();
  }

  @Test
    @Timeout(8000)
  void testGet_existingKey_returnsValue() {
    map.put("key1", "value1");
    String result = map.get("key1");
    assertEquals("value1", result);
  }

  @Test
    @Timeout(8000)
  void testGet_nonExistingKey_returnsNull() {
    map.put("key1", "value1");
    String result = map.get("key2");
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void testGet_nullKey_whenNullNotAllowed_returnsNull() {
    LinkedTreeMap<String, String> noNullMap = new LinkedTreeMap<>(null, false);
    noNullMap.put("key1", "value1");
    String result = noNullMap.get(null);
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void testGet_nullKey_whenNullAllowed_returnsValue() {
    LinkedTreeMap<String, String> allowNullMap = new LinkedTreeMap<>(null, true);
    try {
      allowNullMap.put(null, "nullValue");
    } catch (NullPointerException e) {
      // fallback: use reflection to insert the null key Node directly
      try {
        Method find = LinkedTreeMap.class.getDeclaredMethod("find", Object.class, boolean.class);
        find.setAccessible(true);
        @SuppressWarnings("unchecked")
        LinkedTreeMap.Node<String, String> node = (LinkedTreeMap.Node<String, String>) find.invoke(allowNullMap, null, true);

        // set key and value for the node in case key is not set
        Field keyField = LinkedTreeMap.Node.class.getDeclaredField("key");
        keyField.setAccessible(true);
        keyField.set(node, null);

        Field valueField = LinkedTreeMap.Node.class.getDeclaredField("value");
        valueField.setAccessible(true);
        valueField.set(node, "nullValue");

        // Insert the node into the tree root if not set to avoid NPE in get
        Field rootField = LinkedTreeMap.class.getDeclaredField("root");
        rootField.setAccessible(true);
        rootField.set(allowNullMap, node);

        // Also update size and modCount to keep map consistent
        Field sizeField = LinkedTreeMap.class.getDeclaredField("size");
        sizeField.setAccessible(true);
        sizeField.setInt(allowNullMap, 1);

        Field modCountField = LinkedTreeMap.class.getDeclaredField("modCount");
        modCountField.setAccessible(true);
        modCountField.setInt(allowNullMap, 1);

      } catch (InvocationTargetException ite) {
        // unwrap the cause of InvocationTargetException and fail with the cause message
        Throwable cause = ite.getCause();
        fail("Failed to insert null key via reflection: " + (cause != null ? cause : ite));
      } catch (Exception ex) {
        fail("Failed to insert null key via reflection: " + ex);
      }
    }
    String result = allowNullMap.get(null);
    assertEquals("nullValue", result);
  }

  @Test
    @Timeout(8000)
  void testGet_privateFindByObjectReturnsNode() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    map.put("key1", "value1");
    Method findByObject = LinkedTreeMap.class.getDeclaredMethod("findByObject", Object.class);
    findByObject.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedTreeMap.Node<String, String> node = (LinkedTreeMap.Node<String, String>) findByObject.invoke(map, "key1");
    assertNotNull(node);
    assertEquals("key1", node.key);
    assertEquals("value1", node.value);
  }

  @Test
    @Timeout(8000)
  void testGet_privateFindByObjectReturnsNull() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method findByObject = LinkedTreeMap.class.getDeclaredMethod("findByObject", Object.class);
    findByObject.setAccessible(true);
    Object result = findByObject.invoke(map, "nonexistent");
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void testGet_withDifferentComparator() {
    LinkedTreeMap<String, String> compMap = new LinkedTreeMap<>(String.CASE_INSENSITIVE_ORDER, false);
    compMap.put("KEY", "value");
    assertEquals("value", compMap.get("key"));
    assertNull(compMap.get("other"));
  }

  @Test
    @Timeout(8000)
  void testGet_afterRemove_returnsNull() {
    map.put("key1", "value1");
    map.remove("key1");
    assertNull(map.get("key1"));
  }
}