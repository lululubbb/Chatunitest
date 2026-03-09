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

class LinkedTreeMapPutTest {

  LinkedTreeMap<String, String> mapAllowNull;
  LinkedTreeMap<String, String> mapDisallowNull;

  @BeforeEach
  void setUp() {
    mapAllowNull = new LinkedTreeMap<>((Comparator<? super String>) null, true);
    mapDisallowNull = new LinkedTreeMap<>((Comparator<? super String>) null, false);
  }

  @Test
    @Timeout(8000)
  void put_nullKey_throwsNullPointerException() {
    NullPointerException ex = assertThrows(NullPointerException.class, () -> mapAllowNull.put(null, "value"));
    assertEquals("key == null", ex.getMessage());
  }

  @Test
    @Timeout(8000)
  void put_nullValueAllowed_acceptsNullValue() {
    String previous = mapAllowNull.put("key", null);
    assertNull(previous);
    assertNull(mapAllowNull.get("key"));
  }

  @Test
    @Timeout(8000)
  void put_nullValueDisallowed_throwsNullPointerException() {
    NullPointerException ex = assertThrows(NullPointerException.class, () -> mapDisallowNull.put("key", null));
    assertEquals("value == null", ex.getMessage());
  }

  @Test
    @Timeout(8000)
  void put_newKey_returnsNullAndAddsEntry() {
    String previous = mapDisallowNull.put("key1", "value1");
    assertNull(previous);
    assertEquals("value1", mapDisallowNull.get("key1"));
    assertEquals(1, mapDisallowNull.size());
  }

  @Test
    @Timeout(8000)
  void put_existingKey_returnsOldValueAndReplaces() {
    mapDisallowNull.put("key1", "value1");
    String previous = mapDisallowNull.put("key1", "value2");
    assertEquals("value1", previous);
    assertEquals("value2", mapDisallowNull.get("key1"));
    assertEquals(1, mapDisallowNull.size());
  }

  @Test
    @Timeout(8000)
  void put_reflectiveAccessToFindMethod() throws Exception {
    Method findMethod = LinkedTreeMap.class.getDeclaredMethod("find", Object.class, boolean.class);
    findMethod.setAccessible(true);

    Object node = findMethod.invoke(mapDisallowNull, "reflectKey", true);
    assertNotNull(node);
  }
}