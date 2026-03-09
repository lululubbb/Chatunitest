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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LinkedTreeMap_findByObject_Test {

  LinkedTreeMap<String, String> map;

  @BeforeEach
  void setUp() {
    // Use default constructor that assumes K is Comparable
    map = new LinkedTreeMap<>();
  }

  @Test
    @Timeout(8000)
  void findByObject_withNullKey_returnsNull() throws Exception {
    Method findByObject = LinkedTreeMap.class.getDeclaredMethod("findByObject", Object.class);
    findByObject.setAccessible(true);

    Object result = findByObject.invoke(map, (Object) null);

    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void findByObject_withValidKey_callsFindAndReturnsNode() throws Exception {
    // Spy on map to mock find method
    LinkedTreeMap<String, String> spyMap = spy(map);

    // Use reflection to get the Node class and its constructor
    Class<?> nodeClass = Class.forName("com.google.gson.internal.LinkedTreeMap$Node");
    Constructor<?> nodeConstructor = nodeClass.getDeclaredConstructor(Object.class, Object.class, nodeClass);
    nodeConstructor.setAccessible(true);

    // Prepare a dummy Node to be returned by find
    Object dummyNode = nodeConstructor.newInstance("key", "value", null);

    doReturn(dummyNode).when(spyMap).find("key", false);

    Method findByObject = LinkedTreeMap.class.getDeclaredMethod("findByObject", Object.class);
    findByObject.setAccessible(true);

    Object result = findByObject.invoke(spyMap, "key");

    assertSame(dummyNode, result);
    verify(spyMap).find("key", false);
  }

  @Test
    @Timeout(8000)
  void findByObject_withInvalidKeyType_returnsNull() throws Exception {
    // Spy on map to throw ClassCastException on find
    LinkedTreeMap<String, String> spyMap = spy(map);

    doThrow(ClassCastException.class).when(spyMap).find(any(), eq(false));

    Method findByObject = LinkedTreeMap.class.getDeclaredMethod("findByObject", Object.class);
    findByObject.setAccessible(true);

    Object result = findByObject.invoke(spyMap, new Object());

    assertNull(result);
    verify(spyMap).find(any(), eq(false));
  }

}