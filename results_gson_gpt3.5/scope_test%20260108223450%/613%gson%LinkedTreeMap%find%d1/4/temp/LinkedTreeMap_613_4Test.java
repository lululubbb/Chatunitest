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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Comparator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LinkedTreeMapFindTest {

  LinkedTreeMap<String, String> map;

  @BeforeEach
  void setUp() {
    // Use default constructor (assumes natural ordering, allowNullValues false)
    map = new LinkedTreeMap<>();
  }

  @Test
    @Timeout(8000)
  void find_existingKey_returnsNode() throws Exception {
    // Insert a key
    String key = "key";
    String value = "value";
    map.put(key, value);

    // Use reflection to invoke find with create=false
    Method findMethod = LinkedTreeMap.class.getDeclaredMethod("find", Object.class, boolean.class);
    findMethod.setAccessible(true);

    Object node = findMethod.invoke(map, key, false);
    assertNotNull(node);

    // Check that node.key equals key
    Field keyField = node.getClass().getDeclaredField("key");
    keyField.setAccessible(true);
    Object foundKey = keyField.get(node);
    assertEquals(key, foundKey);

    // Check that node.value equals value
    Field valueField = node.getClass().getDeclaredField("value");
    valueField.setAccessible(true);
    Object foundValue = valueField.get(node);
    assertEquals(value, foundValue);
  }

  @Test
    @Timeout(8000)
  void find_nonExistingKey_createFalse_returnsNull() throws Exception {
    Method findMethod = LinkedTreeMap.class.getDeclaredMethod("find", Object.class, boolean.class);
    findMethod.setAccessible(true);

    Object node = findMethod.invoke(map, "nonExisting", false);
    assertNull(node);
  }

  @Test
    @Timeout(8000)
  void find_nonExistingKey_createTrue_createsNode() throws Exception {
    Method findMethod = LinkedTreeMap.class.getDeclaredMethod("find", Object.class, boolean.class);
    findMethod.setAccessible(true);

    String newKey = "newKey";
    Object createdNode = findMethod.invoke(map, newKey, true);
    assertNotNull(createdNode);

    // Check that root is set to createdNode
    Field rootField = LinkedTreeMap.class.getDeclaredField("root");
    rootField.setAccessible(true);
    Object rootNode = rootField.get(map);
    assertSame(createdNode, rootNode);

    // Check that node.key equals newKey
    Field keyField = createdNode.getClass().getDeclaredField("key");
    keyField.setAccessible(true);
    Object foundKey = keyField.get(createdNode);
    assertEquals(newKey, foundKey);

    // Check size and modCount incremented
    Field sizeField = LinkedTreeMap.class.getDeclaredField("size");
    sizeField.setAccessible(true);
    int size = (int) sizeField.get(map);
    assertEquals(1, size);

    Field modCountField = LinkedTreeMap.class.getDeclaredField("modCount");
    modCountField.setAccessible(true);
    int modCount = (int) modCountField.get(map);
    assertEquals(1, modCount);
  }

  @Test
    @Timeout(8000)
  void find_createTrue_insertsLeftOrRightAndRebalances() throws Exception {
    // Insert root key
    map.put("m", "root");
    // Insert left key
    map.put("c", "left");
    // Insert right key
    map.put("t", "right");

    Method findMethod = LinkedTreeMap.class.getDeclaredMethod("find", Object.class, boolean.class);
    findMethod.setAccessible(true);

    // Insert key smaller than "c" (goes to left of left)
    Object leftMost = findMethod.invoke(map, "a", true);
    assertNotNull(leftMost);

    // Insert key larger than "t" (goes to right of right)
    Object rightMost = findMethod.invoke(map, "z", true);
    assertNotNull(rightMost);

    // Verify size increased accordingly
    Field sizeField = LinkedTreeMap.class.getDeclaredField("size");
    sizeField.setAccessible(true);
    int size = (int) sizeField.get(map);
    assertEquals(5, size);

    // Verify modCount increased accordingly
    Field modCountField = LinkedTreeMap.class.getDeclaredField("modCount");
    modCountField.setAccessible(true);
    int modCount = (int) modCountField.get(map);
    assertEquals(5, modCount);

    // Verify leftMost is left child of "c"
    Field rootField = LinkedTreeMap.class.getDeclaredField("root");
    rootField.setAccessible(true);
    Object rootNode = rootField.get(map);

    Field keyField = rootNode.getClass().getDeclaredField("key");
    keyField.setAccessible(true);
    String rootKey = (String) keyField.get(rootNode);
    assertEquals("m", rootKey);

    Field leftField = rootNode.getClass().getDeclaredField("left");
    leftField.setAccessible(true);
    Object leftNode = leftField.get(rootNode);
    assertNotNull(leftNode);

    String leftKey = (String) keyField.get(leftNode);
    assertEquals("c", leftKey);

    Field leftLeftField = leftNode.getClass().getDeclaredField("left");
    leftLeftField.setAccessible(true);
    Object leftLeftNode = leftLeftField.get(leftNode);
    assertSame(leftMost, leftLeftNode);

    // Verify rightMost is right child of "t"
    Field rightField = rootNode.getClass().getDeclaredField("right");
    rightField.setAccessible(true);
    Object rightNode = rightField.get(rootNode);
    assertNotNull(rightNode);

    String rightKey = (String) keyField.get(rightNode);
    assertEquals("t", rightKey);

    Field rightRightField = rightNode.getClass().getDeclaredField("right");
    rightRightField.setAccessible(true);
    Object rightRightNode = rightRightField.get(rightNode);
    assertSame(rightMost, rightRightNode);
  }

  @Test
    @Timeout(8000)
  void find_createTrue_nonComparableKeyWithNaturalOrder_throwsClassCastException() throws Exception {
    // Create map with natural order comparator (default)
    LinkedTreeMap<Object, Object> naturalMap = new LinkedTreeMap<>();

    Method findMethod = LinkedTreeMap.class.getDeclaredMethod("find", Object.class, boolean.class);
    findMethod.setAccessible(true);

    Object nonComparableKey = new Object();

    ClassCastException ex = assertThrows(ClassCastException.class,
        () -> findMethod.invoke(naturalMap, nonComparableKey, true));

    // The exception is wrapped in InvocationTargetException, unwrap it
    // So we check cause
    // Actually assertThrows unwraps InvocationTargetException automatically
    assertTrue(ex.getMessage().contains("is not Comparable"));
  }

  @Test
    @Timeout(8000)
  void find_withCustomComparator_usesComparator() throws Exception {
    // Comparator that compares strings by length
    Comparator<String> lengthComparator = Comparator.comparingInt(String::length);
    LinkedTreeMap<String, String> customMap = new LinkedTreeMap<>(lengthComparator, false);

    // Insert key with length 3
    customMap.put("abc", "three");

    Method findMethod = LinkedTreeMap.class.getDeclaredMethod("find", Object.class, boolean.class);
    findMethod.setAccessible(true);

    // Searching for key with length 3 should find existing node
    Object node = findMethod.invoke(customMap, "def", false);
    assertNotNull(node);

    // Searching for key with length 2 and create=false returns null
    Object nullNode = findMethod.invoke(customMap, "hi", false);
    assertNull(nullNode);

    // Searching for key with length 2 and create=true creates node
    Object createdNode = findMethod.invoke(customMap, "hi", true);
    assertNotNull(createdNode);

    // Size should be 2
    Field sizeField = LinkedTreeMap.class.getDeclaredField("size");
    sizeField.setAccessible(true);
    int size = (int) sizeField.get(customMap);
    assertEquals(2, size);
  }
}