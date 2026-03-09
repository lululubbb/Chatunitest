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

class LinkedTreeMap_FindByEntry_Test {

  LinkedTreeMap<String, String> map;

  @BeforeEach
  void setUp() {
    map = new LinkedTreeMap<>();
  }

  @Test
    @Timeout(8000)
  void findByEntry_returnsNode_whenEntryMatches() throws Exception {
    // Prepare entry with key and value that exist in map
    String key = "key1";
    String value = "value1";
    map.put(key, value);

    // Create Entry mock with matching key and value
    @SuppressWarnings("unchecked")
    Entry<String, String> entry = mock(Entry.class);
    when(entry.getKey()).thenReturn(key);
    when(entry.getValue()).thenReturn(value);

    // Use reflection to invoke findByEntry
    Method method = LinkedTreeMap.class.getDeclaredMethod("findByEntry", Entry.class);
    method.setAccessible(true);

    @SuppressWarnings("unchecked")
    Node<String, String> result = (Node<String, String>) method.invoke(map, entry);

    assertNotNull(result);
    assertEquals(key, result.key);
    assertEquals(value, result.value);
  }

  @Test
    @Timeout(8000)
  void findByEntry_returnsNull_whenKeyNotFound() throws Exception {
    // Entry with key not in map
    @SuppressWarnings("unchecked")
    Entry<String, String> entry = mock(Entry.class);
    when(entry.getKey()).thenReturn("missingKey");
    when(entry.getValue()).thenReturn("anyValue");

    Method method = LinkedTreeMap.class.getDeclaredMethod("findByEntry", Entry.class);
    method.setAccessible(true);

    @SuppressWarnings("unchecked")
    Node<String, String> result = (Node<String, String>) method.invoke(map, entry);

    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void findByEntry_returnsNull_whenValueDoesNotMatch() throws Exception {
    // Put key with value
    String key = "key2";
    String value = "value2";
    map.put(key, value);

    // Entry with same key but different value
    @SuppressWarnings("unchecked")
    Entry<String, String> entry = mock(Entry.class);
    when(entry.getKey()).thenReturn(key);
    when(entry.getValue()).thenReturn("differentValue");

    Method method = LinkedTreeMap.class.getDeclaredMethod("findByEntry", Entry.class);
    method.setAccessible(true);

    @SuppressWarnings("unchecked")
    Node<String, String> result = (Node<String, String>) method.invoke(map, entry);

    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void findByEntry_returnsNode_whenValueIsNull_andMatches() throws Exception {
    // Put key with null value
    String key = "keyNull";
    map.put(key, null);

    @SuppressWarnings("unchecked")
    Entry<String, String> entry = mock(Entry.class);
    when(entry.getKey()).thenReturn(key);
    when(entry.getValue()).thenReturn(null);

    Method method = LinkedTreeMap.class.getDeclaredMethod("findByEntry", Entry.class);
    method.setAccessible(true);

    @SuppressWarnings("unchecked")
    Node<String, String> result = (Node<String, String>) method.invoke(map, entry);

    assertNotNull(result);
    assertEquals(key, result.key);
    assertNull(result.value);
  }

  @Test
    @Timeout(8000)
  void findByEntry_throwsException_whenEntryIsNull() throws Exception {
    Method method = LinkedTreeMap.class.getDeclaredMethod("findByEntry", Entry.class);
    method.setAccessible(true);

    // When invoking via reflection, NullPointerException is wrapped in InvocationTargetException
    Exception exception = assertThrows(Exception.class, () -> method.invoke(map, (Entry<?, ?>) null));
    assertTrue(exception.getCause() instanceof NullPointerException);
  }
}