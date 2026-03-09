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

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LinkedTreeMapRemoveTest {

  LinkedTreeMap<String, String> map;

  @BeforeEach
  public void setUp() {
    // Using default constructor that assumes natural ordering and no null values allowed
    map = new LinkedTreeMap<>();
  }

  @Test
    @Timeout(8000)
  public void testRemove_existingKey_returnsValueAndRemovesNode() throws Exception {
    // Put a key-value pair
    map.put("key1", "value1");
    assertEquals(1, map.size());

    // Remove the key
    String removed = map.remove("key1");
    assertEquals("value1", removed);

    // Confirm size is updated and key is gone
    assertEquals(0, map.size());
    assertFalse(map.containsKey("key1"));
  }

  @Test
    @Timeout(8000)
  public void testRemove_nonExistingKey_returnsNull() {
    // Remove a key not present
    String removed = map.remove("noSuchKey");
    assertNull(removed);
  }

  @Test
    @Timeout(8000)
  public void testRemove_nullKey_whenNotAllowed_returnsNull() {
    // By default allowNullValues is false, so null key should not be found
    String removed = map.remove(null);
    assertNull(removed);
  }

  @Test
    @Timeout(8000)
  public void testRemove_nullKey_whenAllowed_removesCorrectly() {
    // Create map allowing null values and keys
    LinkedTreeMap<String, String> mapWithNulls = new LinkedTreeMap<>(null, true);
    mapWithNulls.put(null, "nullValue");
    assertEquals("nullValue", mapWithNulls.get(null));

    String removed = mapWithNulls.remove(null);
    assertEquals("nullValue", removed);
    assertNull(mapWithNulls.get(null));
  }

  @Test
    @Timeout(8000)
  public void testRemove_internalNodeRemovalViaReflection() throws Exception {
    // Put multiple entries to create a tree with internal nodes
    map.put("b", "2");
    map.put("a", "1");
    map.put("c", "3");
    assertEquals(3, map.size());

    // Access private method removeInternalByKey via reflection
    Method removeInternalByKey = LinkedTreeMap.class.getDeclaredMethod("removeInternalByKey", Object.class);
    removeInternalByKey.setAccessible(true);

    // Remove internal node "b" (the root in this insertion order)
    Object node = removeInternalByKey.invoke(map, "b");
    assertNotNull(node);

    // Access the "value" field of the node via reflection
    Field valueField = node.getClass().getDeclaredField("value");
    valueField.setAccessible(true);
    Object value = valueField.get(node);
    assertEquals("2", value);

    // After removal, size should be 2
    assertEquals(2, map.size());

    // Removing again returns null
    Object node2 = removeInternalByKey.invoke(map, "b");
    assertNull(node2);
  }
}