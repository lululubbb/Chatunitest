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

class LinkedTreeMap_606_1Test {

  private LinkedTreeMap<String, String> mapAllowNull;
  private LinkedTreeMap<String, String> mapDisallowNull;
  private Comparator<String> reverseComparator;

  @BeforeEach
  void setUp() {
    // natural order comparator (null means natural order)
    mapAllowNull = new LinkedTreeMap<>(null, true);
    mapDisallowNull = new LinkedTreeMap<>(null, false);
    reverseComparator = Comparator.reverseOrder();
  }

  @Test
    @Timeout(8000)
  void testConstructor_withNullComparator_allowsNullValues() throws Exception {
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>(null, true);
    assertNotNull(map);
    assertEquals(0, map.size());
  }

  @Test
    @Timeout(8000)
  void testConstructor_withNonNullComparator_disallowNullValues() throws Exception {
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>(reverseComparator, false);
    assertNotNull(map);
    assertEquals(0, map.size());
  }

  @Test
    @Timeout(8000)
  void testPutAndGet_withNaturalOrderComparator() {
    mapAllowNull.put("a", "valueA");
    mapAllowNull.put("b", "valueB");
    assertEquals("valueA", mapAllowNull.get("a"));
    assertEquals("valueB", mapAllowNull.get("b"));
  }

  @Test
    @Timeout(8000)
  void testPutAndGet_withReverseComparator() {
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>(reverseComparator, true);
    map.put("a", "valueA");
    map.put("b", "valueB");
    assertEquals("valueA", map.get("a"));
    assertEquals("valueB", map.get("b"));
  }

  @Test
    @Timeout(8000)
  void testPut_nullValueAllowed() {
    mapAllowNull.put("key", null);
    assertTrue(mapAllowNull.containsKey("key"));
    assertNull(mapAllowNull.get("key"));
  }

  @Test
    @Timeout(8000)
  void testPut_nullValueDisallowed() {
    assertThrows(NullPointerException.class, () -> mapDisallowNull.put("key", null));
  }

  @Test
    @Timeout(8000)
  void testContainsKey_andRemove() {
    mapAllowNull.put("key1", "val1");
    assertTrue(mapAllowNull.containsKey("key1"));
    assertFalse(mapAllowNull.containsKey("key2"));
    String removed = mapAllowNull.remove("key1");
    assertEquals("val1", removed);
    assertFalse(mapAllowNull.containsKey("key1"));
  }

  @Test
    @Timeout(8000)
  void testClear() {
    mapAllowNull.put("key1", "val1");
    mapAllowNull.put("key2", "val2");
    assertEquals(2, mapAllowNull.size());
    mapAllowNull.clear();
    assertEquals(0, mapAllowNull.size());
    assertFalse(mapAllowNull.containsKey("key1"));
  }

  @Test
    @Timeout(8000)
  void testSize() {
    assertEquals(0, mapAllowNull.size());
    mapAllowNull.put("key1", "val1");
    assertEquals(1, mapAllowNull.size());
    mapAllowNull.put("key2", "val2");
    assertEquals(2, mapAllowNull.size());
    mapAllowNull.remove("key1");
    assertEquals(1, mapAllowNull.size());
  }

  @Test
    @Timeout(8000)
  void testFindByObject_existingAndNonExisting() throws Exception {
    mapAllowNull.put("key", "value");
    // use reflection to access private method findByObject
    var method = LinkedTreeMap.class.getDeclaredMethod("findByObject", Object.class);
    method.setAccessible(true);

    Object node = method.invoke(mapAllowNull, "key");
    assertNotNull(node);

    Object nodeNull = method.invoke(mapAllowNull, "nonexistent");
    assertNull(nodeNull);
  }

  @Test
    @Timeout(8000)
  void testEqualMethod() throws Exception {
    var method = LinkedTreeMap.class.getDeclaredMethod("equal", Object.class, Object.class);
    method.setAccessible(true);

    // same object
    assertTrue((Boolean) method.invoke(mapAllowNull, "a", "a"));
    // equal objects
    assertTrue((Boolean) method.invoke(mapAllowNull, new String("a"), new String("a")));
    // different objects
    assertFalse((Boolean) method.invoke(mapAllowNull, "a", "b"));
    // one null
    assertFalse((Boolean) method.invoke(mapAllowNull, null, "b"));
    assertFalse((Boolean) method.invoke(mapAllowNull, "a", null));
    // both null
    assertTrue((Boolean) method.invoke(mapAllowNull, null, null));
  }

  @Test
    @Timeout(8000)
  void testRemoveInternalAndReplaceInParent() throws Exception {
    mapAllowNull.put("key1", "val1");
    mapAllowNull.put("key2", "val2");

    var findByObject = LinkedTreeMap.class.getDeclaredMethod("findByObject", Object.class);
    findByObject.setAccessible(true);
    var node = findByObject.invoke(mapAllowNull, "key1");
    assertNotNull(node);

    var removeInternal = LinkedTreeMap.class.getDeclaredMethod("removeInternal", node.getClass(), boolean.class);
    removeInternal.setAccessible(true);

    // remove node and unlink true
    removeInternal.invoke(mapAllowNull, node, true);
    assertFalse(mapAllowNull.containsKey("key1"));

    // replaceInParent with reflection
    mapAllowNull.put("key3", "val3");
    var node3 = findByObject.invoke(mapAllowNull, "key3");
    var replaceInParent = LinkedTreeMap.class.getDeclaredMethod("replaceInParent", node3.getClass(), node3.getClass());
    replaceInParent.setAccessible(true);
    // replace with null
    replaceInParent.invoke(mapAllowNull, node3, null);
  }

  @Test
    @Timeout(8000)
  void testRebalanceRotateLeftRotateRight() throws Exception {
    // Insert keys to cause imbalance and force rotations
    LinkedTreeMap<Integer, String> intMap = new LinkedTreeMap<>(null, true);

    // Access private methods
    var rebalance = LinkedTreeMap.class.getDeclaredMethod("rebalance", Object.class, boolean.class);
    rebalance.setAccessible(true);
    var rotateLeft = LinkedTreeMap.class.getDeclaredMethod("rotateLeft", Object.class);
    rotateLeft.setAccessible(true);
    var rotateRight = LinkedTreeMap.class.getDeclaredMethod("rotateRight", Object.class);
    rotateRight.setAccessible(true);
    var find = LinkedTreeMap.class.getDeclaredMethod("find", Object.class, boolean.class);
    find.setAccessible(true);

    // Insert nodes to create tree
    intMap.put(1, "one");
    intMap.put(2, "two");
    intMap.put(3, "three");

    // Get node 1 and rebalance
    Object node1 = find.invoke(intMap, 1, false);
    rebalance.invoke(intMap, node1, true);

    // Test rotateLeft and rotateRight on node1
    rotateLeft.invoke(intMap, node1);
    rotateRight.invoke(intMap, node1);
  }

  @Test
    @Timeout(8000)
  void testEntrySetAndKeySet() {
    mapAllowNull.put("k1", "v1");
    mapAllowNull.put("k2", "v2");

    var entrySet = mapAllowNull.entrySet();
    assertNotNull(entrySet);
    assertEquals(2, entrySet.size());

    var keySet = mapAllowNull.keySet();
    assertNotNull(keySet);
    assertEquals(2, keySet.size());
    assertTrue(keySet.contains("k1"));
  }

  @Test
    @Timeout(8000)
  void testWriteReplaceAndReadObject() throws Exception {
    // writeReplace is private
    var writeReplace = LinkedTreeMap.class.getDeclaredMethod("writeReplace");
    writeReplace.setAccessible(true);
    Object replacement = writeReplace.invoke(mapAllowNull);
    assertNotNull(replacement);

    // readObject is private and takes ObjectInputStream
    var readObject = LinkedTreeMap.class.getDeclaredMethod("readObject", java.io.ObjectInputStream.class);
    readObject.setAccessible(true);

    // Mock ObjectInputStream
    java.io.ObjectInputStream mockIn = mock(java.io.ObjectInputStream.class);
    // Just call readObject without exception
    readObject.invoke(mapAllowNull, mockIn);
  }
}