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
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LinkedTreeMap_findByObject_Test {

  LinkedTreeMap<String, String> map;

  @BeforeEach
  void setUp() {
    map = spy(new LinkedTreeMap<>());
  }

  @Test
    @Timeout(8000)
  void findByObject_returnsNode_whenKeyNotNullAndFindReturnsNode() throws Exception {
    String key = "key";
    @SuppressWarnings("unchecked")
    LinkedTreeMap.Node<String, String> expectedNode = mock(LinkedTreeMap.Node.class);
    doReturn(expectedNode).when(map).find(key, false);

    Method method = LinkedTreeMap.class.getDeclaredMethod("findByObject", Object.class);
    method.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedTreeMap.Node<String, String> actualNode = (LinkedTreeMap.Node<String, String>) method.invoke(map, key);

    assertSame(expectedNode, actualNode);
    verify(map).find(key, false);
  }

  @Test
    @Timeout(8000)
  void findByObject_returnsNull_whenKeyIsNull() throws Exception {
    Method method = LinkedTreeMap.class.getDeclaredMethod("findByObject", Object.class);
    method.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedTreeMap.Node<String, String> actualNode = (LinkedTreeMap.Node<String, String>) method.invoke(map, (Object) null);

    assertNull(actualNode);
    verify(map, never()).find(any(), anyBoolean());
  }

  @Test
    @Timeout(8000)
  void findByObject_returnsNull_whenFindThrowsClassCastException() throws Exception {
    String key = "key";
    doThrow(ClassCastException.class).when(map).find(key, false);

    Method method = LinkedTreeMap.class.getDeclaredMethod("findByObject", Object.class);
    method.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedTreeMap.Node<String, String> actualNode = (LinkedTreeMap.Node<String, String>) method.invoke(map, key);
    assertNull(actualNode);

    verify(map).find(key, false);
  }
}