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
import java.util.LinkedHashMap;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LinkedTreeMapEntrySetTest {
  LinkedTreeMap<String, String> map;

  @BeforeEach
  void setUp() {
    map = new LinkedTreeMap<>();
  }

  @Test
    @Timeout(8000)
  void entrySet_whenEntrySetIsNull_createsAndReturnsEntrySet() {
    // Initially entrySet field is null
    Set<Entry<String, String>> firstCall = map.entrySet();
    assertNotNull(firstCall);

    // On second call, should return the same instance (cached)
    Set<Entry<String, String>> secondCall = map.entrySet();
    assertSame(firstCall, secondCall);
  }

  @Test
    @Timeout(8000)
  void entrySet_containsEntriesAfterPut() {
    map.put("key1", "value1");
    map.put("key2", "value2");

    Set<Entry<String, String>> entries = map.entrySet();
    assertEquals(2, entries.size());

    boolean foundKey1 = false;
    boolean foundKey2 = false;
    for (Entry<String, String> entry : entries) {
      if ("key1".equals(entry.getKey()) && "value1".equals(entry.getValue())) {
        foundKey1 = true;
      }
      if ("key2".equals(entry.getKey()) && "value2".equals(entry.getValue())) {
        foundKey2 = true;
      }
    }
    assertTrue(foundKey1);
    assertTrue(foundKey2);
  }

  @Test
    @Timeout(8000)
  void entrySet_iteratorRemoveRemovesEntry() {
    map.put("key1", "value1");
    Set<Entry<String, String>> entries = map.entrySet();
    Iterator<Entry<String, String>> it = entries.iterator();
    assertTrue(it.hasNext());
    Entry<String, String> entry = it.next();
    assertEquals("key1", entry.getKey());
    it.remove();
    assertEquals(0, map.size());
    assertFalse(map.containsKey("key1"));
  }

  @Test
    @Timeout(8000)
  void entrySet_iteratorThrowsConcurrentModificationException() {
    map.put("key1", "value1");
    Set<Entry<String, String>> entries = map.entrySet();
    Iterator<Entry<String, String>> it = entries.iterator();

    // Modify map to cause modCount mismatch
    map.put("key2", "value2");

    assertThrows(ConcurrentModificationException.class, it::next);
  }

  @Test
    @Timeout(8000)
  void entrySet_iteratorNextThrowsNoSuchElementException() {
    Set<Entry<String, String>> entries = map.entrySet();
    Iterator<Entry<String, String>> it = entries.iterator();
    assertThrows(NoSuchElementException.class, it::next);
  }
}