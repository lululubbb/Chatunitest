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

class LinkedTreeMap_607_5Test {

  LinkedTreeMap<String, String> map;

  @BeforeEach
  void setUp() {
    map = new LinkedTreeMap<>();
  }

  @Test
    @Timeout(8000)
  void size_emptyMap_returnsZero() {
    assertEquals(0, map.size());
  }

  @Test
    @Timeout(8000)
  void size_afterPutOneEntry_returnsOne() {
    map.put("key", "value");
    assertEquals(1, map.size());
  }

  @Test
    @Timeout(8000)
  void size_afterPutMultipleEntries_returnsCorrectSize() {
    map.put("key1", "value1");
    map.put("key2", "value2");
    map.put("key3", "value3");
    assertEquals(3, map.size());
  }

  @Test
    @Timeout(8000)
  void size_afterRemoveEntry_returnsCorrectSize() {
    map.put("key1", "value1");
    map.put("key2", "value2");
    map.remove("key1");
    assertEquals(1, map.size());
  }

  @Test
    @Timeout(8000)
  void size_afterClear_returnsZero() {
    map.put("key1", "value1");
    map.put("key2", "value2");
    map.clear();
    assertEquals(0, map.size());
  }
}