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
import java.util.LinkedHashMap;
import java.util.NoSuchElementException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LinkedTreeMap_entrySet_Test {

  LinkedTreeMap<String, String> map;

  @BeforeEach
  public void setUp() {
    map = new LinkedTreeMap<>();
  }

  @Test
    @Timeout(8000)
  public void entrySet_whenEntrySetIsNull_createsAndReturnsNewEntrySet() throws Exception {
    // Ensure entrySet field is null initially
    Field entrySetField = LinkedTreeMap.class.getDeclaredField("entrySet");
    entrySetField.setAccessible(true);
    entrySetField.set(map, null);

    Set<Entry<String, String>> result = map.entrySet();

    assertNotNull(result);
    // The entrySet field should now be set to the same instance returned
    assertSame(result, entrySetField.get(map));
  }

  @Test
    @Timeout(8000)
  public void entrySet_whenEntrySetIsNotNull_returnsExistingEntrySet() throws Exception {
    // Create a dummy EntrySet instance and set it via reflection
    Class<?> entrySetClass = null;
    for (Class<?> innerClass : LinkedTreeMap.class.getDeclaredClasses()) {
      if ("EntrySet".equals(innerClass.getSimpleName())) {
        entrySetClass = innerClass;
        break;
      }
    }
    assertNotNull(entrySetClass, "EntrySet inner class not found");

    // Instead of entrySetClass.setAccessible(true) which doesn't exist,
    // get the constructor and set it accessible
    Constructor<?> constructor = entrySetClass.getDeclaredConstructor(LinkedTreeMap.class);
    constructor.setAccessible(true);
    Object dummyEntrySet = constructor.newInstance(map);

    Field entrySetField = LinkedTreeMap.class.getDeclaredField("entrySet");
    entrySetField.setAccessible(true);
    entrySetField.set(map, dummyEntrySet);

    Set<Entry<String, String>> result = map.entrySet();

    assertNotNull(result);
    assertSame(dummyEntrySet, result);
  }

  @Test
    @Timeout(8000)
  public void entrySet_iterateEntries_emptyMap() {
    Set<Entry<String, String>> entries = map.entrySet();
    Iterator<Entry<String, String>> it = entries.iterator();
    assertFalse(it.hasNext());
  }

  @Test
    @Timeout(8000)
  public void entrySet_iterateEntries_withOneEntry() {
    map.put("key1", "value1");
    Set<Entry<String, String>> entries = map.entrySet();
    Iterator<Entry<String, String>> it = entries.iterator();

    assertTrue(it.hasNext());
    Entry<String, String> entry = it.next();
    assertEquals("key1", entry.getKey());
    assertEquals("value1", entry.getValue());
    assertFalse(it.hasNext());
  }

  @Test
    @Timeout(8000)
  public void entrySet_iteratorRemove_removesEntry() {
    map.put("key1", "value1");
    Set<Entry<String, String>> entries = map.entrySet();
    Iterator<Entry<String, String>> it = entries.iterator();

    Entry<String, String> entry = it.next();
    it.remove();

    assertEquals(0, map.size());
    assertFalse(map.containsKey("key1"));
  }

  @Test
    @Timeout(8000)
  public void entrySet_iteratorRemove_withoutNext_throwsException() {
    Set<Entry<String, String>> entries = map.entrySet();
    Iterator<Entry<String, String>> it = entries.iterator();

    assertThrows(IllegalStateException.class, it::remove);
  }

  @Test
    @Timeout(8000)
  public void entrySet_iterator_nextAfterEnd_throwsNoSuchElementException() {
    Set<Entry<String, String>> entries = map.entrySet();
    Iterator<Entry<String, String>> it = entries.iterator();

    assertFalse(it.hasNext());
    assertThrows(java.util.NoSuchElementException.class, it::next);
  }
}