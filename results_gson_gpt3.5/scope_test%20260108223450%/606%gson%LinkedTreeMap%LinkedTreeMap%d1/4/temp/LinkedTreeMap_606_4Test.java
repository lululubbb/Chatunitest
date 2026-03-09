package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LinkedTreeMap_606_4Test {

  LinkedTreeMap<String, String> map;
  Comparator<String> comparator;

  @BeforeEach
  void setup() {
    comparator = Comparator.naturalOrder();
    map = new LinkedTreeMap<>(comparator, true);
  }

  @Test
    @Timeout(8000)
  void testConstructor_withComparatorAllowNullValues() {
    LinkedTreeMap<String, String> customMap = new LinkedTreeMap<>(null, false);
    assertNotNull(customMap);
    assertEquals(0, customMap.size());
  }

  @Test
    @Timeout(8000)
  void testConstructor_noArgs_assumesComparable() throws Exception {
    Constructor<LinkedTreeMap> ctor = LinkedTreeMap.class.getDeclaredConstructor();
    ctor.setAccessible(true);
    LinkedTreeMap<String, String> defaultMap = ctor.newInstance();
    assertNotNull(defaultMap);
    assertEquals(0, defaultMap.size());

    Constructor<LinkedTreeMap> ctorAllowNull = LinkedTreeMap.class.getDeclaredConstructor(boolean.class);
    ctorAllowNull.setAccessible(true);
    LinkedTreeMap<String, String> allowNullMap = ctorAllowNull.newInstance(true);
    assertNotNull(allowNullMap);
  }

  @Test
    @Timeout(8000)
  void testPut_andGet_basicOperations() {
    assertNull(map.put("a", "1"));
    assertEquals("1", map.get("a"));
    assertTrue(map.containsKey("a"));
    assertEquals(1, map.size());

    assertEquals("1", map.put("a", "2"));
    assertEquals("2", map.get("a"));
  }

  @Test
    @Timeout(8000)
  void testContainsKey_nonExisting() {
    assertFalse(map.containsKey("nonexistent"));
  }

  @Test
    @Timeout(8000)
  void testClear_andRemove() {
    map.put("key1", "val1");
    map.put("key2", "val2");
    assertEquals(2, map.size());

    map.clear();
    assertEquals(0, map.size());
    assertFalse(map.containsKey("key1"));

    map.put("key3", "val3");
    assertEquals("val3", map.remove("key3"));
    assertNull(map.remove("key3"));
  }

  @Test
    @Timeout(8000)
  void testEntrySet_and_KeySet_notNullEmptyOnNew() {
    Set<Map.Entry<String, String>> entries = map.entrySet();
    Set<String> keys = map.keySet();
    assertNotNull(entries);
    assertNotNull(keys);
    assertTrue(entries.isEmpty());
    assertTrue(keys.isEmpty());
  }

  @Test
    @Timeout(8000)
  void testFind_createNewNode() throws Exception {
    Method findMethod = LinkedTreeMap.class.getDeclaredMethod("find", Object.class, boolean.class);
    findMethod.setAccessible(true);

    Object node = findMethod.invoke(map, "testKey", true);
    assertNotNull(node);
    assertEquals(1, map.size());

    Object sameNode = findMethod.invoke(map, "testKey", false);
    assertNotNull(sameNode);
    assertEquals(node, sameNode);
  }

  @Test
    @Timeout(8000)
  void testFindByObject_existingAndNull() throws Exception {
    map.put("k1", "v1");
    Method findByObject = LinkedTreeMap.class.getDeclaredMethod("findByObject", Object.class);
    findByObject.setAccessible(true);

    Object node = findByObject.invoke(map, "k1");
    assertNotNull(node);

    Object none = findByObject.invoke(map, "k999");
    assertNull(none);

    Object nullNode = findByObject.invoke(map, (Object) null);
    assertNull(nullNode);
  }

  @Test
    @Timeout(8000)
  void testFindByEntry_existingAndNonExisting() throws Exception {
    map.put("k", "v");
    var entry = Map.entry("k", "v");

    Method findByEntry = LinkedTreeMap.class.getDeclaredMethod("findByEntry", Map.Entry.class);
    findByEntry.setAccessible(true);

    Object node = findByEntry.invoke(map, entry);
    assertNotNull(node);

    Map.Entry<Object,Object> wrongEntry = Map.entry("k", "wrong");
    Object notFound = findByEntry.invoke(map, wrongEntry);
    assertNull(notFound);
  }

  @Test
    @Timeout(8000)
  void testEqual_privateMethod() throws Exception {
    Method equalMethod = LinkedTreeMap.class.getDeclaredMethod("equal", Object.class, Object.class);
    equalMethod.setAccessible(true);

    Boolean t1 = (Boolean) equalMethod.invoke(map, null, null);
    assertTrue(t1);

    Boolean t2 = (Boolean) equalMethod.invoke(map, "a", "a");
    assertTrue(t2);

    Boolean f1 = (Boolean) equalMethod.invoke(map, "a", "b");
    assertFalse(f1);

    Boolean f2 = (Boolean) equalMethod.invoke(map, null, "a");
    assertFalse(f2);
  }

  @Test
    @Timeout(8000)
  void testRemoveInternalByKey() throws Exception {
    map.put("key", "value");
    Method removeInternalByKey = LinkedTreeMap.class.getDeclaredMethod("removeInternalByKey", Object.class);
    removeInternalByKey.setAccessible(true);

    Object removedNode = removeInternalByKey.invoke(map, "key");
    assertNotNull(removedNode);
    assertEquals(0, map.size());

    Object nullNode = removeInternalByKey.invoke(map, "key");
    assertNull(nullNode);
  }

  @Test
    @Timeout(8000)
  void testRemoveInternal_removesNode() throws Exception {
    map.put("a", "aval");
    Method findMethod = LinkedTreeMap.class.getDeclaredMethod("find", Object.class, boolean.class);
    findMethod.setAccessible(true);
    Object node = findMethod.invoke(map, "a", false);

    Method removeInternalMethod = LinkedTreeMap.class.getDeclaredMethod("removeInternal", Class.forName("com.google.gson.internal.LinkedTreeMap$Node"), boolean.class);
    removeInternalMethod.setAccessible(true);
    removeInternalMethod.invoke(map, node, true);

    assertEquals(0, map.size());
  }

  @Test
    @Timeout(8000)
  void testReplaceInParent_rotations() throws Exception {
    map.put("a", "1");
    map.put("b", "2");
    Method findMethod = LinkedTreeMap.class.getDeclaredMethod("find", Object.class, boolean.class);
    findMethod.setAccessible(true);
    Object nodeA = findMethod.invoke(map, "a", false);
    Object nodeB = findMethod.invoke(map, "b", false);

    Method replaceInParent = LinkedTreeMap.class.getDeclaredMethod("replaceInParent", Class.forName("com.google.gson.internal.LinkedTreeMap$Node"), Class.forName("com.google.gson.internal.LinkedTreeMap$Node"));
    replaceInParent.setAccessible(true);

    // Replace nodeA with nodeB in parent
    replaceInParent.invoke(map, nodeA, nodeB);
  }

  @Test
    @Timeout(8000)
  void testRebalance_rotations() throws Exception {
    map.put("a", "a");
    map.put("b", "b");
    map.put("c", "c");
    Method findMethod = LinkedTreeMap.class.getDeclaredMethod("find", Object.class, boolean.class);
    findMethod.setAccessible(true);
    Object nodeA = findMethod.invoke(map, "a", false);

    Method rebalance = LinkedTreeMap.class.getDeclaredMethod("rebalance", Class.forName("com.google.gson.internal.LinkedTreeMap$Node"), boolean.class);
    rebalance.setAccessible(true);

    rebalance.invoke(map, nodeA, true);
  }

  @Test
    @Timeout(8000)
  void testRotateLeftAndRight() throws Exception {
    map.put("a", "a");
    map.put("b", "b");
    map.put("c", "c");

    Method findMethod = LinkedTreeMap.class.getDeclaredMethod("find", Object.class, boolean.class);
    findMethod.setAccessible(true);
    Object nodeB = findMethod.invoke(map, "b", false);

    Method rotateLeft = LinkedTreeMap.class.getDeclaredMethod("rotateLeft", Class.forName("com.google.gson.internal.LinkedTreeMap$Node"));
    Method rotateRight = LinkedTreeMap.class.getDeclaredMethod("rotateRight", Class.forName("com.google.gson.internal.LinkedTreeMap$Node"));
    rotateLeft.setAccessible(true);
    rotateRight.setAccessible(true);

    rotateLeft.invoke(map, nodeB);
    rotateRight.invoke(map, nodeB);
  }

  @Test
    @Timeout(8000)
  void testWriteReplace_notNull() throws Exception {
    Method writeReplace = LinkedTreeMap.class.getDeclaredMethod("writeReplace");
    writeReplace.setAccessible(true);
    Object replacement = writeReplace.invoke(map);
    assertNotNull(replacement);
  }

  @Test
    @Timeout(8000)
  void testReadObject_invalidObjectStreamException() throws Exception {
    ObjectInputStream mockIn = mock(ObjectInputStream.class);
    doThrow(InvalidObjectException.class).when(mockIn).defaultReadObject();

    Method readObject = LinkedTreeMap.class.getDeclaredMethod("readObject", ObjectInputStream.class);
    readObject.setAccessible(true);

    // since readObject can throw, we verify it throws
    assertThrows(InvalidObjectException.class, () -> {
      readObject.invoke(map, mockIn);
    });
  }
}