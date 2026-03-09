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
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LinkedTreeMap_writeReplace_Test {

  private LinkedTreeMap<String, String> map;

  @BeforeEach
  void setUp() {
    map = new LinkedTreeMap<>();
    map.put("key1", "value1");
    map.put("key2", "value2");
  }

  @Test
    @Timeout(8000)
  void writeReplace_returnsLinkedHashMapWithSameEntries() throws Exception {
    // Use reflection to access private writeReplace method
    Method writeReplaceMethod = LinkedTreeMap.class.getDeclaredMethod("writeReplace");
    writeReplaceMethod.setAccessible(true);

    Object replaced = writeReplaceMethod.invoke(map);

    assertNotNull(replaced);
    assertTrue(replaced instanceof LinkedHashMap);

    @SuppressWarnings("unchecked")
    LinkedHashMap<String, String> linkedHashMap = (LinkedHashMap<String, String>) replaced;

    // The LinkedHashMap should contain all entries from the original LinkedTreeMap
    assertEquals(map.size(), linkedHashMap.size());

    for (Entry<String, String> entry : map.entrySet()) {
      assertTrue(linkedHashMap.containsKey(entry.getKey()));
      assertEquals(entry.getValue(), linkedHashMap.get(entry.getKey()));
    }
  }
}