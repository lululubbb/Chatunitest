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

class LinkedTreeMapPutTest {

  LinkedTreeMap<String, String> mapAllowNull;
  LinkedTreeMap<String, String> mapDisallowNull;

  @BeforeEach
  void setUp() {
    // Using constructor with allowNullValues = true
    mapAllowNull = new LinkedTreeMap<>(null, true);
    // Using constructor with allowNullValues = false
    mapDisallowNull = new LinkedTreeMap<>(null, false);
  }

  @Test
    @Timeout(8000)
  void put_nullKey_throwsNullPointerException() {
    NullPointerException exception = assertThrows(NullPointerException.class, () -> {
      mapAllowNull.put(null, "value");
    });
    assertEquals("key == null", exception.getMessage());
  }

  @Test
    @Timeout(8000)
  void put_nullValue_allowed() {
    // allowNullValues = true, so null value is allowed
    String result = mapAllowNull.put("key", null);
    assertNull(result);
    // verify value stored is null by putting again with a value
    String prev = mapAllowNull.put("key", "value");
    assertNull(prev);
  }

  @Test
    @Timeout(8000)
  void put_nullValue_disallowed_throwsNullPointerException() {
    NullPointerException exception = assertThrows(NullPointerException.class, () -> {
      mapDisallowNull.put("key", null);
    });
    assertEquals("value == null", exception.getMessage());
  }

  @Test
    @Timeout(8000)
  void put_newKey_returnsNull() {
    String prev = mapAllowNull.put("newKey", "newValue");
    assertNull(prev);
  }

  @Test
    @Timeout(8000)
  void put_existingKey_returnsOldValue() {
    mapAllowNull.put("key", "value1");
    String prev = mapAllowNull.put("key", "value2");
    assertEquals("value1", prev);
  }

  @Test
    @Timeout(8000)
  void put_internalFindCalledAndValueReplaced() throws Exception {
    // Use reflection to access private find method
    Method findMethod = LinkedTreeMap.class.getDeclaredMethod("find", Object.class, boolean.class);
    findMethod.setAccessible(true);

    // Put a key-value pair
    String key = "key";
    String value1 = "value1";
    String value2 = "value2";

    // First put to create the node
    String prev1 = mapAllowNull.put(key, value1);
    assertNull(prev1);

    // Use find to get the node
    Object node = findMethod.invoke(mapAllowNull, key, false);
    assertNotNull(node);

    // Put again to replace value
    String prev2 = mapAllowNull.put(key, value2);
    assertEquals(value1, prev2);

    // Use reflection to get the value field of node
    Field valueField = node.getClass().getDeclaredField("value");
    valueField.setAccessible(true);
    Object storedValue = valueField.get(node);
    assertEquals(value2, storedValue);
  }

}