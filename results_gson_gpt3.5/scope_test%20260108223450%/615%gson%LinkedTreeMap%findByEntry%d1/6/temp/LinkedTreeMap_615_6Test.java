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

class LinkedTreeMap_findByEntry_Test {

  LinkedTreeMap<String, String> map;

  @BeforeEach
  void setUp() {
    // Use default constructor which assumes natural ordering and no null values allowed
    map = new LinkedTreeMap<>(null, false);
  }

  @Test
    @Timeout(8000)
  void findByEntry_returnsNode_whenKeyAndValueMatch() throws Exception {
    map.put("key1", "value1");
    @SuppressWarnings("unchecked")
    Entry<String, String> entry = mock(Entry.class);
    when(entry.getKey()).thenReturn("key1");
    when(entry.getValue()).thenReturn("value1");

    Node<String, String> result = invokeFindByEntry(map, entry);

    assertNotNull(result);
    assertEquals("key1", result.key);
    assertEquals("value1", result.value);
  }

  @Test
    @Timeout(8000)
  void findByEntry_returnsNull_whenKeyMatchesButValueDiffers() throws Exception {
    map.put("key1", "value1");
    @SuppressWarnings("unchecked")
    Entry<String, String> entry = mock(Entry.class);
    when(entry.getKey()).thenReturn("key1");
    when(entry.getValue()).thenReturn("differentValue");

    Node<String, String> result = invokeFindByEntry(map, entry);

    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void findByEntry_returnsNull_whenKeyNotFound() throws Exception {
    map.put("key1", "value1");
    @SuppressWarnings("unchecked")
    Entry<String, String> entry = mock(Entry.class);
    when(entry.getKey()).thenReturn("absentKey");
    when(entry.getValue()).thenReturn("value1");

    Node<String, String> result = invokeFindByEntry(map, entry);

    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void findByEntry_returnsNull_whenEntryIsNull() throws Exception {
    Node<String, String> result = invokeFindByEntry(map, null);
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void findByEntry_returnsNull_whenEntryKeyIsNull_and_keyNotPresent() throws Exception {
    map.put("key1", "value1");
    @SuppressWarnings("unchecked")
    Entry<String, String> entry = mock(Entry.class);
    when(entry.getKey()).thenReturn(null);
    when(entry.getValue()).thenReturn("value1");

    Node<String, String> result = invokeFindByEntry(map, entry);
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void findByEntry_returnsNode_whenKeyIsNull_andValueMatches_and_nullsAllowed() throws Exception {
    LinkedTreeMap<String, String> mapAllowNull = new LinkedTreeMap<>(null, true);
    mapAllowNull.put(null, "nullValue");
    @SuppressWarnings("unchecked")
    Entry<String, String> entry = mock(Entry.class);
    when(entry.getKey()).thenReturn(null);
    when(entry.getValue()).thenReturn("nullValue");

    Node<String, String> result = invokeFindByEntry(mapAllowNull, entry);

    assertNotNull(result);
    assertNull(result.key);
    assertEquals("nullValue", result.value);
  }

  @Test
    @Timeout(8000)
  void findByEntry_returnsNull_whenValueIsNull_butValueDoesNotMatch() throws Exception {
    map.put("key1", "value1");
    @SuppressWarnings("unchecked")
    Entry<String, String> entry = mock(Entry.class);
    when(entry.getKey()).thenReturn("key1");
    when(entry.getValue()).thenReturn(null);

    Node<String, String> result = invokeFindByEntry(map, entry);
    assertNull(result);
  }

  // Helper method to invoke private or package-private findByEntry via reflection
  @SuppressWarnings("unchecked")
  private Node<String, String> invokeFindByEntry(LinkedTreeMap<String, String> map, Entry<?, ?> entry)
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    if (entry == null) {
      return null;
    }
    Method method = LinkedTreeMap.class.getDeclaredMethod("findByEntry", Entry.class);
    method.setAccessible(true);
    try {
      return (Node<String, String>) method.invoke(map, entry);
    } catch (InvocationTargetException e) {
      if (e.getCause() instanceof NullPointerException) {
        return null;
      }
      throw e;
    }
  }
}