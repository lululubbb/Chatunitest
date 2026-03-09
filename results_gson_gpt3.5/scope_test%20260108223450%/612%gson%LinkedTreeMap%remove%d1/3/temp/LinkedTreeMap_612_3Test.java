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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LinkedTreeMapRemoveTest {

  private LinkedTreeMap<String, String> map;

  @BeforeEach
  void setUp() {
    // Use the default constructor which uses natural ordering and allowNullValues false
    map = new LinkedTreeMap<>(null, false);
  }

  @Test
    @Timeout(8000)
  void remove_existingKey_removesAndReturnsValue() {
    map.put("key1", "value1");
    map.put("key2", "value2");

    String removed = map.remove("key1");

    assertEquals("value1", removed);
    assertFalse(map.containsKey("key1"));
    assertEquals(1, map.size());
  }

  @Test
    @Timeout(8000)
  void remove_nonExistingKey_returnsNull() {
    map.put("key1", "value1");

    String removed = map.remove("nonexistent");

    assertNull(removed);
    assertEquals(1, map.size());
  }

  @Test
    @Timeout(8000)
  void remove_nullKey_whenAllowed_removesEntry() {
    LinkedTreeMap<String, String> nullAllowedMap = new LinkedTreeMap<>(null, true);
    nullAllowedMap.put(null, "nullValue");
    assertTrue(nullAllowedMap.containsKey(null));

    String removed = nullAllowedMap.remove(null);

    assertEquals("nullValue", removed);
    assertFalse(nullAllowedMap.containsKey(null));
    assertEquals(0, nullAllowedMap.size());
  }

  @Test
    @Timeout(8000)
  void remove_nullKey_whenNotAllowed_returnsNull() {
    map.put("key1", "value1");

    String removed = map.remove(null);

    assertNull(removed);
    assertEquals(1, map.size());
  }

  @Test
    @Timeout(8000)
  void remove_internalNodeRemoval_behavior() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    // Insert multiple keys to create a tree structure
    map.put("b", "2");
    map.put("a", "1");
    map.put("c", "3");

    // Access private method removeInternalByKey via reflection
    Method removeInternalByKey = LinkedTreeMap.class.getDeclaredMethod("removeInternalByKey", Object.class);
    removeInternalByKey.setAccessible(true);

    // Remove existing key "b"
    Object node = removeInternalByKey.invoke(map, "b");
    assertNotNull(node);

    // Remove non-existing key returns null
    Object nullNode = removeInternalByKey.invoke(map, "nonexistent");
    assertNull(nullNode);
  }

  @Test
    @Timeout(8000)
  void removeInternalByKey_removesRootNode() throws Exception {
    map.put("m", "middle");
    map.put("a", "left");
    map.put("z", "right");

    // Remove root node "m"
    Method removeInternalByKey = LinkedTreeMap.class.getDeclaredMethod("removeInternalByKey", Object.class);
    removeInternalByKey.setAccessible(true);

    Object removedNode = removeInternalByKey.invoke(map, "m");
    assertNotNull(removedNode);
    assertFalse(map.containsKey("m"));
  }

  @Test
    @Timeout(8000)
  void removeInternalByKey_updatesSizeAndModCount() throws Exception {
    map.put("x", "xval");
    int initialSize = map.size();
    int initialModCount = getModCount(map);

    Method removeInternalByKey = LinkedTreeMap.class.getDeclaredMethod("removeInternalByKey", Object.class);
    removeInternalByKey.setAccessible(true);

    Object removedNode = removeInternalByKey.invoke(map, "x");
    assertNotNull(removedNode);
    assertEquals(initialSize - 1, map.size());
    assertEquals(initialModCount + 1, getModCount(map));
  }

  private int getModCount(LinkedTreeMap<?, ?> map) throws NoSuchFieldException, IllegalAccessException {
    var field = LinkedTreeMap.class.getDeclaredField("modCount");
    field.setAccessible(true);
    return field.getInt(map);
  }
}