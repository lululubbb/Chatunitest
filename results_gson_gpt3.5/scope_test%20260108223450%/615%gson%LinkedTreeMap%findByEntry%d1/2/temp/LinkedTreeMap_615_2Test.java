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
  void findByEntry_returnsNode_whenEntryMatches() throws Exception {
    // Prepare a key-value pair in the map
    map.put("key1", "value1");

    // Create an Entry mock with matching key and value
    @SuppressWarnings("unchecked")
    Entry<String, String> entry = mock(Entry.class);
    when(entry.getKey()).thenReturn("key1");
    when(entry.getValue()).thenReturn("value1");

    // Use reflection to invoke findByEntry
    Method findByEntryMethod = LinkedTreeMap.class.getDeclaredMethod("findByEntry", Entry.class);
    findByEntryMethod.setAccessible(true);

    @SuppressWarnings("unchecked")
    Node<String, String> result = (Node<String, String>) findByEntryMethod.invoke(map, entry);

    assertNotNull(result);
    assertEquals("key1", result.key);
    assertEquals("value1", result.value);
  }

  @Test
    @Timeout(8000)
  void findByEntry_returnsNull_whenEntryKeyNotFound() throws Exception {
    // Entry with a key not in the map
    @SuppressWarnings("unchecked")
    Entry<String, String> entry = mock(Entry.class);
    when(entry.getKey()).thenReturn("missingKey");
    when(entry.getValue()).thenReturn("anyValue");

    Method findByEntryMethod = LinkedTreeMap.class.getDeclaredMethod("findByEntry", Entry.class);
    findByEntryMethod.setAccessible(true);

    @SuppressWarnings("unchecked")
    Node<String, String> result = (Node<String, String>) findByEntryMethod.invoke(map, entry);

    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void findByEntry_returnsNull_whenEntryValueDoesNotMatch() throws Exception {
    // Insert a key with a different value
    map.put("key1", "value1");

    // Entry with same key but different value
    @SuppressWarnings("unchecked")
    Entry<String, String> entry = mock(Entry.class);
    when(entry.getKey()).thenReturn("key1");
    when(entry.getValue()).thenReturn("differentValue");

    Method findByEntryMethod = LinkedTreeMap.class.getDeclaredMethod("findByEntry", Entry.class);
    findByEntryMethod.setAccessible(true);

    @SuppressWarnings("unchecked")
    Node<String, String> result = (Node<String, String>) findByEntryMethod.invoke(map, entry);

    assertNull(result);
  }
}