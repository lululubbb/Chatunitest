package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LinkedTreeMap_606_6Test {

  Comparator<String> naturalComparator = Comparator.naturalOrder();

  LinkedTreeMap<String, String> mapAllowNull;
  LinkedTreeMap<String, String> mapDisallowNull;
  LinkedTreeMap<String, String> mapCustomComparator;

  @BeforeEach
  void setUp() {
    mapAllowNull = new LinkedTreeMap<>(null, true);
    mapDisallowNull = new LinkedTreeMap<>(null, false);
    mapCustomComparator = new LinkedTreeMap<>(Comparator.reverseOrder(), true);
  }

  @Test
    @Timeout(8000)
  void testConstructor_withNullComparator_andAllowNullTrue() {
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>(null, true);
    assertNotNull(map);
    assertEquals(0, map.size());
  }

  @Test
    @Timeout(8000)
  void testConstructor_withNullComparator_andAllowNullFalse() {
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>(null, false);
    assertNotNull(map);
    assertEquals(0, map.size());
  }

  @Test
    @Timeout(8000)
  void testConstructor_withCustomComparator() {
    Comparator<String> cmp = Comparator.reverseOrder();
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>(cmp, true);
    assertNotNull(map);
    assertEquals(0, map.size());
  }

  @Test
    @Timeout(8000)
  void testPutAndGet_withAllowNullValuesTrue() {
    mapAllowNull.put("key1", "value1");
    mapAllowNull.put("key2", null);
    assertEquals("value1", mapAllowNull.get("key1"));
    assertNull(mapAllowNull.get("key2"));
    assertTrue(mapAllowNull.containsKey("key2"));
  }

  @Test
    @Timeout(8000)
  void testPutAndGet_withAllowNullValuesFalse() {
    mapDisallowNull.put("key1", "value1");
    assertEquals("value1", mapDisallowNull.get("key1"));

    assertThrows(NullPointerException.class, () -> mapDisallowNull.put("key2", null));
  }

  @Test
    @Timeout(8000)
  void testPut_overwriteValue() {
    mapAllowNull.put("key1", "value1");
    String oldValue = mapAllowNull.put("key1", "value2");
    assertEquals("value1", oldValue);
    assertEquals("value2", mapAllowNull.get("key1"));
  }

  @Test
    @Timeout(8000)
  void testSizeAndClear() {
    assertEquals(0, mapAllowNull.size());
    mapAllowNull.put("a", "A");
    mapAllowNull.put("b", "B");
    assertEquals(2, mapAllowNull.size());
    mapAllowNull.clear();
    assertEquals(0, mapAllowNull.size());
    assertNull(mapAllowNull.get("a"));
  }

  @Test
    @Timeout(8000)
  void testRemove_existingKey() {
    mapAllowNull.put("key1", "value1");
    String removed = mapAllowNull.remove("key1");
    assertEquals("value1", removed);
    assertNull(mapAllowNull.get("key1"));
    assertEquals(0, mapAllowNull.size());
  }

  @Test
    @Timeout(8000)
  void testRemove_nonExistingKey() {
    assertNull(mapAllowNull.remove("nonexistent"));
  }

  @Test
    @Timeout(8000)
  void testContainsKey() {
    mapAllowNull.put("key1", "value1");
    assertTrue(mapAllowNull.containsKey("key1"));
    assertFalse(mapAllowNull.containsKey("key2"));
  }

  @Test
    @Timeout(8000)
  void testEntrySet_and_KeySet_notNullAndReflectsContents() {
    mapAllowNull.put("k1", "v1");
    mapAllowNull.put("k2", "v2");

    assertNotNull(mapAllowNull.entrySet());
    assertNotNull(mapAllowNull.keySet());

    assertEquals(2, mapAllowNull.entrySet().size());
    assertEquals(2, mapAllowNull.keySet().size());
    assertTrue(mapAllowNull.keySet().contains("k1"));
    assertTrue(mapAllowNull.keySet().contains("k2"));
  }

  @Test
    @Timeout(8000)
  void testFind_privateMethod_behavior() throws Exception {
    // Use reflection to invoke private find(K, boolean)
    Constructor<LinkedTreeMap> ctor = LinkedTreeMap.class.getDeclaredConstructor(Comparator.class, boolean.class);
    ctor.setAccessible(true);
    LinkedTreeMap<String, String> map = ctor.newInstance(null, true);

    var findMethod = LinkedTreeMap.class.getDeclaredMethod("find", Object.class, boolean.class);
    findMethod.setAccessible(true);

    // create = false, should return null for missing key
    Object result = findMethod.invoke(map, "missing", false);
    assertNull(result);

    // create = true, should create node and return non-null
    Object createdNode = findMethod.invoke(map, "newKey", true);
    assertNotNull(createdNode);
  }

  @Test
    @Timeout(8000)
  void testFindByObject_privateMethod_behavior() throws Exception {
    var findByObjectMethod = LinkedTreeMap.class.getDeclaredMethod("findByObject", Object.class);
    findByObjectMethod.setAccessible(true);

    Object result = findByObjectMethod.invoke(mapAllowNull, "missing");
    assertNull(result);

    mapAllowNull.put("key", "value");
    Object found = findByObjectMethod.invoke(mapAllowNull, "key");
    assertNotNull(found);
  }

  @Test
    @Timeout(8000)
  void testEqual_privateMethod_behavior() throws Exception {
    var equalMethod = LinkedTreeMap.class.getDeclaredMethod("equal", Object.class, Object.class);
    equalMethod.setAccessible(true);

    Boolean b1 = (Boolean) equalMethod.invoke(mapAllowNull, null, null);
    assertTrue(b1);

    Boolean b2 = (Boolean) equalMethod.invoke(mapAllowNull, "a", "a");
    assertTrue(b2);

    Boolean b3 = (Boolean) equalMethod.invoke(mapAllowNull, "a", "b");
    assertFalse(b3);

    Boolean b4 = (Boolean) equalMethod.invoke(mapAllowNull, "a", null);
    assertFalse(b4);
  }
}