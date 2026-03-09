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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map.Entry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LinkedTreeMap_FindByEntryTest {

  LinkedTreeMap<String, String> map;

  @BeforeEach
  void setUp() {
    map = new LinkedTreeMap<>();
  }

  @Test
    @Timeout(8000)
  void findByEntry_returnsNode_whenKeyAndValueMatch() throws Exception {
    // Prepare key and value
    String key = "key1";
    String value = "value1";

    // Put entry into map
    map.put(key, value);

    // Create Entry mock
    @SuppressWarnings("unchecked")
    Entry<String, String> entry = mock(Entry.class);
    when(entry.getKey()).thenReturn(key);
    when(entry.getValue()).thenReturn(value);

    // Use reflection to invoke findByEntry
    Method findByEntry = LinkedTreeMap.class.getDeclaredMethod("findByEntry", Entry.class);
    findByEntry.setAccessible(true);
    @SuppressWarnings("unchecked")
    Node<String, String> result = (Node<String, String>) findByEntry.invoke(map, entry);

    assertNotNull(result);
    assertEquals(key, result.key);
    assertEquals(value, result.value);
  }

  @Test
    @Timeout(8000)
  void findByEntry_returnsNull_whenKeyNotFound() throws Exception {
    // Prepare key and value
    String key = "key1";
    String value = "value1";

    // Map is empty, no entries

    // Create Entry mock with key not in map
    @SuppressWarnings("unchecked")
    Entry<String, String> entry = mock(Entry.class);
    when(entry.getKey()).thenReturn(key);
    when(entry.getValue()).thenReturn(value);

    // Use reflection to invoke findByEntry
    Method findByEntry = LinkedTreeMap.class.getDeclaredMethod("findByEntry", Entry.class);
    findByEntry.setAccessible(true);
    @SuppressWarnings("unchecked")
    Node<String, String> result = (Node<String, String>) findByEntry.invoke(map, entry);

    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void findByEntry_returnsNull_whenValueDoesNotMatch() throws Exception {
    // Prepare key and value
    String key = "key1";
    String valueInMap = "value1";
    String valueInEntry = "value2";

    // Put entry into map with valueInMap
    map.put(key, valueInMap);

    // Create Entry mock with same key but different value
    @SuppressWarnings("unchecked")
    Entry<String, String> entry = mock(Entry.class);
    when(entry.getKey()).thenReturn(key);
    when(entry.getValue()).thenReturn(valueInEntry);

    // Use reflection to invoke findByEntry
    Method findByEntry = LinkedTreeMap.class.getDeclaredMethod("findByEntry", Entry.class);
    findByEntry.setAccessible(true);
    @SuppressWarnings("unchecked")
    Node<String, String> result = (Node<String, String>) findByEntry.invoke(map, entry);

    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void findByEntry_handlesNullValueInMapAndEntry() throws Exception {
    // Prepare key and null value
    String key = "key1";
    String value = null;

    // Put entry into map with null value
    map.put(key, value);

    // Create Entry mock with same key and null value
    @SuppressWarnings("unchecked")
    Entry<String, String> entry = mock(Entry.class);
    when(entry.getKey()).thenReturn(key);
    when(entry.getValue()).thenReturn(value);

    // Use reflection to invoke findByEntry
    Method findByEntry = LinkedTreeMap.class.getDeclaredMethod("findByEntry", Entry.class);
    findByEntry.setAccessible(true);
    @SuppressWarnings("unchecked")
    Node<String, String> result = (Node<String, String>) findByEntry.invoke(map, entry);

    assertNotNull(result);
    assertEquals(key, result.key);
    assertNull(result.value);
  }

  @Test
    @Timeout(8000)
  void findByEntry_handlesNullValueInMapAndNonNullInEntry() throws Exception {
    // Prepare key and values
    String key = "key1";
    String valueInMap = null;
    String valueInEntry = "value";

    // Put entry into map with null value
    map.put(key, valueInMap);

    // Create Entry mock with same key but non-null value
    @SuppressWarnings("unchecked")
    Entry<String, String> entry = mock(Entry.class);
    when(entry.getKey()).thenReturn(key);
    when(entry.getValue()).thenReturn(valueInEntry);

    // Use reflection to invoke findByEntry
    Method findByEntry = LinkedTreeMap.class.getDeclaredMethod("findByEntry", Entry.class);
    findByEntry.setAccessible(true);
    @SuppressWarnings("unchecked")
    Node<String, String> result = (Node<String, String>) findByEntry.invoke(map, entry);

    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void findByEntry_handlesNullKeyInEntry() throws Exception {
    // Put entry with null key and some value
    map.put(null, "value");

    // Create Entry mock with null key and matching value
    @SuppressWarnings("unchecked")
    Entry<String, String> entry = mock(Entry.class);
    when(entry.getKey()).thenReturn(null);
    when(entry.getValue()).thenReturn("value");

    // Use reflection to invoke findByEntry
    Method findByEntry = LinkedTreeMap.class.getDeclaredMethod("findByEntry", Entry.class);
    findByEntry.setAccessible(true);
    @SuppressWarnings("unchecked")
    Node<String, String> result = (Node<String, String>) findByEntry.invoke(map, entry);

    assertNotNull(result);
    assertNull(result.key);
    assertEquals("value", result.value);
  }

  @Test
    @Timeout(8000)
  void findByEntry_handlesNullKeyInEntry_noMatch() throws Exception {
    // Map empty

    // Create Entry mock with null key and some value
    @SuppressWarnings("unchecked")
    Entry<String, String> entry = mock(Entry.class);
    when(entry.getKey()).thenReturn(null);
    when(entry.getValue()).thenReturn("value");

    // Use reflection to invoke findByEntry
    Method findByEntry = LinkedTreeMap.class.getDeclaredMethod("findByEntry", Entry.class);
    findByEntry.setAccessible(true);
    @SuppressWarnings("unchecked")
    Node<String, String> result = (Node<String, String>) findByEntry.invoke(map, entry);

    assertNull(result);
  }
}