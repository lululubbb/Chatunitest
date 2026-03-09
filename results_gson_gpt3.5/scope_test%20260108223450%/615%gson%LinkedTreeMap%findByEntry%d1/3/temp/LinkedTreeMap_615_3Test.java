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

import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.internal.LinkedTreeMap.Node;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Map.Entry;

class LinkedTreeMap_FindByEntry_Test {

  LinkedTreeMap<String, String> map;

  @BeforeEach
  void setUp() {
    // Use default constructor (assumes natural ordering)
    map = new LinkedTreeMap<>();
  }

  @Test
    @Timeout(8000)
  void findByEntry_returnsNode_whenEntryMatches() throws Exception {
    // Put an entry so findByObject can find a node
    map.put("key", "value");

    // Create a real Entry with matching key and value instead of mock
    Entry<String, String> entry = new Entry<String, String>() {
      @Override
      public String getKey() {
        return "key";
      }

      @Override
      public String getValue() {
        return "value";
      }

      @Override
      public String setValue(String value) {
        throw new UnsupportedOperationException();
      }
    };

    // Use reflection to invoke private findByEntry
    Method findByEntryMethod = LinkedTreeMap.class.getDeclaredMethod("findByEntry", Entry.class);
    findByEntryMethod.setAccessible(true);
    @SuppressWarnings("unchecked")
    Node<String, String> result = (Node<String, String>) findByEntryMethod.invoke(map, entry);

    assertNotNull(result);
    assertEquals("key", result.key);
    assertEquals("value", result.value);
  }

  @Test
    @Timeout(8000)
  void findByEntry_returnsNull_whenEntryKeyNotFound() throws Exception {
    // Entry with missing key and some value
    Entry<String, String> entry = new Entry<String, String>() {
      @Override
      public String getKey() {
        return "missing";
      }

      @Override
      public String getValue() {
        return "value";
      }

      @Override
      public String setValue(String value) {
        throw new UnsupportedOperationException();
      }
    };

    Method findByEntryMethod = LinkedTreeMap.class.getDeclaredMethod("findByEntry", Entry.class);
    findByEntryMethod.setAccessible(true);
    @SuppressWarnings("unchecked")
    Node<String, String> result = (Node<String, String>) findByEntryMethod.invoke(map, entry);

    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void findByEntry_returnsNull_whenEntryValueDiffers() throws Exception {
    // Put an entry with key "key" and value "value"
    map.put("key", "value");

    // Entry with same key but different value
    Entry<String, String> entry = new Entry<String, String>() {
      @Override
      public String getKey() {
        return "key";
      }

      @Override
      public String getValue() {
        return "differentValue";
      }

      @Override
      public String setValue(String value) {
        throw new UnsupportedOperationException();
      }
    };

    Method findByEntryMethod = LinkedTreeMap.class.getDeclaredMethod("findByEntry", Entry.class);
    findByEntryMethod.setAccessible(true);
    @SuppressWarnings("unchecked")
    Node<String, String> result = (Node<String, String>) findByEntryMethod.invoke(map, entry);

    assertNull(result);
  }
}