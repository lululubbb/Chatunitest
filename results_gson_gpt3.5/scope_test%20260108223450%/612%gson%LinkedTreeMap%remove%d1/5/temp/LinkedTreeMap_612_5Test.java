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
  public void setUp() {
    map = new LinkedTreeMap<>(null, false);
  }

  @Test
    @Timeout(8000)
  public void testRemove_existingKey_returnsValueAndRemovesNode() {
    // Put a key-value pair
    map.put("key1", "value1");
    assertEquals(1, map.size());

    // Remove the key
    String removedValue = map.remove("key1");

    // Verify returned value
    assertEquals("value1", removedValue);

    // Verify size decreased
    assertEquals(0, map.size());

    // Verify key is no longer contained
    assertFalse(map.containsKey("key1"));
  }

  @Test
    @Timeout(8000)
  public void testRemove_nonExistingKey_returnsNull() {
    // Map is empty
    assertNull(map.remove("nonexistent"));
  }

  @Test
    @Timeout(8000)
  public void testRemove_nullKey_whenAllowed() {
    // Create map allowing null values (and keys)
    LinkedTreeMap<String, String> mapAllowNull = new LinkedTreeMap<>(null, true);
    mapAllowNull.put(null, "nullValue");
    assertEquals("nullValue", mapAllowNull.remove(null));
    assertFalse(mapAllowNull.containsKey(null));
  }

  @Test
    @Timeout(8000)
  public void testRemove_nullKey_whenNotAllowed() {
    // By default allowNullValues is false, so put(null, ...) should throw or behave accordingly
    assertThrows(NullPointerException.class, () -> map.put(null, "value"));
    // Removing null key should return null
    assertNull(map.remove(null));
  }

  @Test
    @Timeout(8000)
  public void testRemove_internalRemoveInternalByKey_invokedAndResultHandled() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    // Use reflection to invoke private removeInternalByKey to verify remove uses it correctly
    Method removeInternalByKey = LinkedTreeMap.class.getDeclaredMethod("removeInternalByKey", Object.class);
    removeInternalByKey.setAccessible(true);

    // Put a key to have a node
    map.put("key2", "value2");

    // Remove node by key using reflection
    Object node = removeInternalByKey.invoke(map, "key2");
    assertNotNull(node);

    // Now remove should return null for that key since it was already removed by reflection call
    String removedValue = map.remove("key2");
    assertNull(removedValue);

    // Removing again returns null
    assertNull(map.remove("key2"));
  }

  @Test
    @Timeout(8000)
  public void testRemove_multipleEntries_removesCorrectOne() {
    map.put("k1", "v1");
    map.put("k2", "v2");
    map.put("k3", "v3");

    assertEquals("v2", map.remove("k2"));
    assertNull(map.remove("k2"));
    assertEquals(2, map.size());
    assertFalse(map.containsKey("k2"));
    assertTrue(map.containsKey("k1"));
    assertTrue(map.containsKey("k3"));
  }
}