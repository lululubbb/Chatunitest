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

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LinkedTreeMap_607_1Test {

  private LinkedTreeMap<String, String> map;

  @BeforeEach
  void setUp() {
    map = new LinkedTreeMap<>();
  }

  @Test
    @Timeout(8000)
  void size_whenEmpty_shouldReturnZero() {
    assertEquals(0, map.size());
  }

  @Test
    @Timeout(8000)
  void size_afterPut_shouldReturnCorrectSize() {
    map.put("key1", "value1");
    assertEquals(1, map.size());

    map.put("key2", "value2");
    assertEquals(2, map.size());

    // Put with existing key does not increase size
    map.put("key1", "value3");
    assertEquals(2, map.size());
  }

  @Test
    @Timeout(8000)
  void size_afterRemove_shouldReturnCorrectSize() {
    map.put("key1", "value1");
    map.put("key2", "value2");
    assertEquals(2, map.size());

    map.remove("key1");
    assertEquals(1, map.size());

    map.remove("key2");
    assertEquals(0, map.size());

    // Remove non-existing key does not change size
    map.remove("key3");
    assertEquals(0, map.size());
  }

  @Test
    @Timeout(8000)
  void size_afterClear_shouldReturnZero() {
    map.put("key1", "value1");
    map.put("key2", "value2");
    assertEquals(2, map.size());

    map.clear();
    assertEquals(0, map.size());
  }
}