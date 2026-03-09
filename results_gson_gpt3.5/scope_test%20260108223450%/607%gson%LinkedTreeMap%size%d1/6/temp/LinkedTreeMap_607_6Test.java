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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.google.gson.internal.LinkedTreeMap;

class LinkedTreeMapSizeTest {

  private LinkedTreeMap<String, String> map;

  @BeforeEach
  void setUp() {
    map = new LinkedTreeMap<>(null, true);
  }

  @Test
    @Timeout(8000)
  void size_WhenEmpty_ShouldReturnZero() {
    assertEquals(0, map.size());
  }

  @Test
    @Timeout(8000)
  void size_AfterPutOneEntry_ShouldReturnOne() {
    map.put("key1", "value1");
    assertEquals(1, map.size());
  }

  @Test
    @Timeout(8000)
  void size_AfterPutMultipleEntries_ShouldReturnCorrectSize() {
    map.put("key1", "value1");
    map.put("key2", "value2");
    map.put("key3", "value3");
    assertEquals(3, map.size());
  }

  @Test
    @Timeout(8000)
  void size_AfterRemoveEntry_ShouldReturnCorrectSize() {
    map.put("key1", "value1");
    map.put("key2", "value2");
    map.remove("key1");
    assertEquals(1, map.size());
  }

  @Test
    @Timeout(8000)
  void size_AfterClear_ShouldReturnZero() {
    map.put("key1", "value1");
    map.put("key2", "value2");
    map.clear();
    assertEquals(0, map.size());
  }
}