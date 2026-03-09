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

import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.internal.LinkedTreeMap.Node;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class LinkedTreeMap_RemoveInternalByKeyTest {

  private LinkedTreeMap<String, String> map;

  @BeforeEach
  void setUp() {
    // Use constructor with (Comparator<? super K> comparator, boolean allowNullValues)
    // to explicitly allow null keys only where needed.
    map = new LinkedTreeMap<>(null, false);
  }

  @Test
    @Timeout(8000)
  void removeInternalByKey_existingKey_removesNodeAndReturnsNode() throws Exception {
    // Arrange
    String key = "key1";
    String value = "value1";
    map.put(key, value);

    // Use reflection to get the private method removeInternalByKey
    Method removeInternalByKeyMethod = LinkedTreeMap.class.getDeclaredMethod("removeInternalByKey", Object.class);
    removeInternalByKeyMethod.setAccessible(true);

    // Act
    @SuppressWarnings("unchecked")
    Node<String, String> removedNode = (Node<String, String>) removeInternalByKeyMethod.invoke(map, key);

    // Assert
    assertNotNull(removedNode, "Removed node should not be null");
    assertEquals(key, removedNode.key);
    assertEquals(value, removedNode.value);
    assertFalse(map.containsKey(key), "Map should no longer contain the removed key");
  }

  @Test
    @Timeout(8000)
  void removeInternalByKey_nonExistingKey_returnsNull() throws Exception {
    // Arrange
    String key = "nonExistingKey";

    Method removeInternalByKeyMethod = LinkedTreeMap.class.getDeclaredMethod("removeInternalByKey", Object.class);
    removeInternalByKeyMethod.setAccessible(true);

    // Act
    @SuppressWarnings("unchecked")
    Node<String, String> removedNode = (Node<String, String>) removeInternalByKeyMethod.invoke(map, key);

    // Assert
    assertNull(removedNode, "Removed node should be null when key does not exist");
  }

  @Test
    @Timeout(8000)
  void removeInternalByKey_nullKeyWithNullAllowed_removesNodeAndReturnsNode() throws Exception {
    // Arrange
    LinkedTreeMap<String, String> mapAllowNull = new LinkedTreeMap<>(null, true);
    String key = null;
    String value = "valueNull";
    mapAllowNull.put(key, value);

    Method removeInternalByKeyMethod = LinkedTreeMap.class.getDeclaredMethod("removeInternalByKey", Object.class);
    removeInternalByKeyMethod.setAccessible(true);

    // Act
    @SuppressWarnings("unchecked")
    Node<String, String> removedNode = (Node<String, String>) removeInternalByKeyMethod.invoke(mapAllowNull, key);

    // Assert
    assertNotNull(removedNode, "Removed node should not be null for null key when null values allowed");
    assertNull(removedNode.key);
    assertEquals(value, removedNode.value);
    assertFalse(mapAllowNull.containsKey(key), "Map should no longer contain the removed null key");
  }

  @Test
    @Timeout(8000)
  void removeInternalByKey_nullKeyWithoutNullAllowed_throwsNullPointerException() throws Exception {
    // Arrange
    String key = null;

    Method removeInternalByKeyMethod = LinkedTreeMap.class.getDeclaredMethod("removeInternalByKey", Object.class);
    removeInternalByKeyMethod.setAccessible(true);

    // Act & Assert
    Exception exception = assertThrows(Exception.class, () -> {
      removeInternalByKeyMethod.invoke(map, key);
    });

    // The cause of the exception should be NullPointerException
    Throwable cause = exception.getCause();
    assertNotNull(cause);
    assertTrue(cause instanceof NullPointerException, "Expected NullPointerException for null key when null values not allowed");
  }
}