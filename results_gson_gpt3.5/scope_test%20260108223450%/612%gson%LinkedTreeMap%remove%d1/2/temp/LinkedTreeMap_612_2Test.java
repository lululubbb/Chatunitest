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

import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LinkedTreeMapRemoveTest {

  private LinkedTreeMap<String, String> map;

  @BeforeEach
  public void setUp() {
    map = new LinkedTreeMap<>(null, true);
  }

  @Test
    @Timeout(8000)
  public void remove_existingKey_returnsValueAndRemovesEntry() {
    map.put("key1", "value1");
    assertEquals(1, map.size());

    String removedValue = map.remove("key1");

    assertEquals("value1", removedValue);
    assertEquals(0, map.size());
    assertFalse(map.containsKey("key1"));
  }

  @Test
    @Timeout(8000)
  public void remove_nonExistingKey_returnsNull() {
    map.put("key1", "value1");

    String removedValue = map.remove("key2");

    assertNull(removedValue);
    assertEquals(1, map.size());
  }

  @Test
    @Timeout(8000)
  public void remove_nullKey_whenNullNotAllowed_returnsNull() {
    LinkedTreeMap<String, String> noNullMap = new LinkedTreeMap<>(null, false);
    noNullMap.put("a", "A");

    String removed = noNullMap.remove(null);

    assertNull(removed);
    assertEquals(1, noNullMap.size());
  }

  @Test
    @Timeout(8000)
  public void remove_nullKey_whenNullAllowed_removesEntry() {
    LinkedTreeMap<String, String> allowNullMap = new LinkedTreeMap<>(null, true);
    allowNullMap.put(null, "nullValue");

    String removed = allowNullMap.remove(null);

    assertEquals("nullValue", removed);
    assertEquals(0, allowNullMap.size());
  }

  @Test
    @Timeout(8000)
  public void remove_internalByKey_privateMethod_behavesAsExpected() throws Exception {
    map.put("k1", "v1");
    Method removeInternalByKey = LinkedTreeMap.class.getDeclaredMethod("removeInternalByKey", Object.class);
    removeInternalByKey.setAccessible(true);

    // Remove existing key
    Object node = removeInternalByKey.invoke(map, "k1");
    assertNotNull(node);

    // Remove again returns null
    Object node2 = removeInternalByKey.invoke(map, "k1");
    assertNull(node2);

    // Remove non-existing key returns null
    Object node3 = removeInternalByKey.invoke(map, "nonExisting");
    assertNull(node3);
  }

  @Test
    @Timeout(8000)
  public void removeInternalByKey_removesNodesCorrectly() throws Exception {
    LinkedTreeMap<Integer, String> intMap = new LinkedTreeMap<>(null, true);
    intMap.put(1, "one");
    intMap.put(2, "two");
    intMap.put(3, "three");

    Method removeInternalByKey = LinkedTreeMap.class.getDeclaredMethod("removeInternalByKey", Object.class);
    removeInternalByKey.setAccessible(true);

    Object node1 = removeInternalByKey.invoke(intMap, 2);
    assertNotNull(node1);
    assertEquals(2, intMap.size());

    Object node2 = removeInternalByKey.invoke(intMap, 4);
    assertNull(node2);
    assertEquals(2, intMap.size());

    Object node3 = removeInternalByKey.invoke(intMap, 1);
    assertNotNull(node3);
    assertEquals(1, intMap.size());

    Object node4 = removeInternalByKey.invoke(intMap, 3);
    assertNotNull(node4);
    assertEquals(0, intMap.size());
  }
}