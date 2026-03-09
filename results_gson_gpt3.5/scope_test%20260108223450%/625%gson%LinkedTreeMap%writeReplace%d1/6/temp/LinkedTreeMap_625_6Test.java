package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
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

import java.io.ObjectStreamException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LinkedTreeMapWriteReplaceTest {

  private LinkedTreeMap<String, String> linkedTreeMap;

  @BeforeEach
  void setUp() {
    linkedTreeMap = new LinkedTreeMap<>();
    linkedTreeMap.put("key1", "value1");
    linkedTreeMap.put("key2", "value2");
  }

  @Test
    @Timeout(8000)
  void testWriteReplace_returnsLinkedHashMapWithSameEntries() throws Throwable {
    Method writeReplaceMethod = LinkedTreeMap.class.getDeclaredMethod("writeReplace");
    writeReplaceMethod.setAccessible(true);

    Object result;
    try {
      result = writeReplaceMethod.invoke(linkedTreeMap);
    } catch (InvocationTargetException e) {
      // unwrap possible ObjectStreamException
      throw e.getCause();
    }

    assertNotNull(result);
    assertTrue(result instanceof LinkedHashMap<?, ?>);

    @SuppressWarnings("unchecked")
    LinkedHashMap<String, String> linkedHashMap = (LinkedHashMap<String, String>) result;

    // The LinkedHashMap should contain all entries from the LinkedTreeMap
    assertEquals(linkedTreeMap.size(), linkedHashMap.size());
    for (var entry : linkedTreeMap.entrySet()) {
      assertTrue(linkedHashMap.containsKey(entry.getKey()));
      assertEquals(entry.getValue(), linkedHashMap.get(entry.getKey()));
    }
  }
}