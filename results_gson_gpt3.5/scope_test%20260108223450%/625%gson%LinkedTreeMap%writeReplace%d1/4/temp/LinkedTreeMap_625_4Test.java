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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LinkedTreeMap_writeReplace_Test {

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

    Object replacement;
    try {
      replacement = writeReplaceMethod.invoke(linkedTreeMap);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }

    assertNotNull(replacement);
    assertTrue(replacement instanceof LinkedHashMap);

    @SuppressWarnings("unchecked")
    LinkedHashMap<String, String> linkedHashMap = (LinkedHashMap<String, String>) replacement;
    assertEquals(2, linkedHashMap.size());
    assertEquals("value1", linkedHashMap.get("key1"));
    assertEquals("value2", linkedHashMap.get("key2"));
  }
}