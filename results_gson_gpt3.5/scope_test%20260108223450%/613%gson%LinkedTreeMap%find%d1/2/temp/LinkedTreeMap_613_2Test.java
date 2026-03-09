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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Comparator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LinkedTreeMap_613_2Test {

  private LinkedTreeMap<String, String> map;

  @BeforeEach
  void setUp() {
    map = new LinkedTreeMap<>();
  }

  @Test
    @Timeout(8000)
  void find_returnsNullIfEmptyAndCreateFalse() throws Exception {
    LinkedTreeMap.Node<String, String> result = invokeFind(map, "key", false);
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void find_createsRootNodeIfEmptyAndCreateTrue() throws Exception {
    LinkedTreeMap.Node<String, String> node = invokeFind(map, "key", true);
    assertNotNull(node);
    assertEquals("key", node.key);
    assertSame(node, getField(map, "root"));
    assertEquals(1, ((Integer)getField(map, "size")).intValue());
    assertEquals(1, ((Integer)getField(map, "modCount")).intValue());
  }

  @Test
    @Timeout(8000)
  void find_findsExistingNodeUsingNaturalOrder() throws Exception {
    LinkedTreeMap.Node<String, String> created = invokeFind(map, "key", true);
    LinkedTreeMap.Node<String, String> found = invokeFind(map, "key", false);
    assertSame(created, found);
  }

  @Test
    @Timeout(8000)
  void find_returnsNullIfNotFoundAndCreateFalse() throws Exception {
    invokeFind(map, "key", true);
    LinkedTreeMap.Node<String, String> result = invokeFind(map, "other", false);
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void find_createsLeftChildWhenKeyLessThanRoot() throws Exception {
    LinkedTreeMap.Node<String, String> root = invokeFind(map, "m", true);
    LinkedTreeMap.Node<String, String> leftChild = invokeFind(map, "a", true);
    assertNotNull(leftChild);
    assertSame(leftChild, root.left);
    assertEquals(2, ((Integer)getField(map, "size")).intValue());
  }

  @Test
    @Timeout(8000)
  void find_createsRightChildWhenKeyGreaterThanRoot() throws Exception {
    LinkedTreeMap.Node<String, String> root = invokeFind(map, "m", true);
    LinkedTreeMap.Node<String, String> rightChild = invokeFind(map, "z", true);
    assertNotNull(rightChild);
    assertSame(rightChild, root.right);
    assertEquals(2, ((Integer)getField(map, "size")).intValue());
  }

  @Test
    @Timeout(8000)
  void find_throwsClassCastExceptionIfNonComparableKeyAndNaturalOrder() throws Exception {
    LinkedTreeMap<Object, Object> objectMap = new LinkedTreeMap<>();
    Object nonComparableKey = new Object();
    InvocationTargetException ex = assertThrows(InvocationTargetException.class, () -> {
      invokeFind(objectMap, nonComparableKey, true);
    });
    Throwable cause = ex.getCause();
    assertNotNull(cause);
    assertTrue(cause instanceof ClassCastException);
    assertTrue(cause.getMessage().contains("is not Comparable"));
  }

  @Test
    @Timeout(8000)
  void find_usesCustomComparator() throws Exception {
    @SuppressWarnings("unchecked")
    Comparator<String> comp = mock(Comparator.class);
    when(comp.compare(anyString(), anyString())).thenAnswer(invocation -> {
      String a = invocation.getArgument(0);
      String b = invocation.getArgument(1);
      return a.compareTo(b);
    });
    LinkedTreeMap<String, String> customMap = new LinkedTreeMap<>(comp, false);

    LinkedTreeMap.Node<String, String> root = invokeFind(customMap, "m", true);
    LinkedTreeMap.Node<String, String> left = invokeFind(customMap, "a", true);
    LinkedTreeMap.Node<String, String> right = invokeFind(customMap, "z", true);

    assertNotNull(root);
    assertNotNull(left);
    assertNotNull(right);
    assertSame(left, root.left);
    assertSame(right, root.right);
    verify(comp, atLeastOnce()).compare(anyString(), anyString());
  }

  // Helper to invoke private/protected find method
  @SuppressWarnings("unchecked")
  private <K, V> LinkedTreeMap.Node<K, V> invokeFind(LinkedTreeMap<K, V> map, K key, boolean create) throws Exception {
    Method findMethod = LinkedTreeMap.class.getDeclaredMethod("find", Object.class, boolean.class);
    findMethod.setAccessible(true);
    return (LinkedTreeMap.Node<K, V>) findMethod.invoke(map, key, create);
  }

  // Helper to get private fields
  @SuppressWarnings("unchecked")
  private <T> T getField(Object instance, String fieldName) throws Exception {
    Field field = LinkedTreeMap.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    return (T) field.get(instance);
  }
}