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
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LinkedTreeMap_keySet_Test {

  LinkedTreeMap<String, String> map;

  @BeforeEach
  void setUp() {
    map = new LinkedTreeMap<>();
  }

  @Test
    @Timeout(8000)
  void keySet_shouldReturnSameInstance_whenCalledMultipleTimes() throws Exception {
    // First call, keySet field is null, so new KeySet created and assigned
    Set<String> firstCall = map.keySet();
    assertNotNull(firstCall);

    // Using reflection to get private field keySet and verify assigned
    Field keySetField = LinkedTreeMap.class.getDeclaredField("keySet");
    keySetField.setAccessible(true);
    Object keySetValue = keySetField.get(map);
    assertSame(firstCall, keySetValue);

    // Second call returns same instance
    Set<String> secondCall = map.keySet();
    assertSame(firstCall, secondCall);
  }

  @Test
    @Timeout(8000)
  void keySet_shouldReturnNonNullKeySet_onEmptyMap() {
    Set<String> keys = map.keySet();
    assertNotNull(keys);
    assertTrue(keys.isEmpty());
  }

  @Test
    @Timeout(8000)
  void keySet_shouldReflectKeysPutInMap() {
    map.put("one", "1");
    map.put("two", "2");

    Set<String> keys = map.keySet();

    assertEquals(2, keys.size());
    assertTrue(keys.contains("one"));
    assertTrue(keys.contains("two"));
  }

  @Test
    @Timeout(8000)
  void keySet_shouldReflectKeysAfterRemove() {
    map.put("one", "1");
    map.put("two", "2");
    map.remove("one");

    Set<String> keys = map.keySet();

    assertEquals(1, keys.size());
    assertFalse(keys.contains("one"));
    assertTrue(keys.contains("two"));
  }
}