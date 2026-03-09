package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.util.AbstractList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NonNullElementWrapperList_483_1Test {

  NonNullElementWrapperList<String> list;
  ArrayList<String> mockDelegate;

  @BeforeEach
  void setUp() {
    mockDelegate = mock(ArrayList.class);
    list = new NonNullElementWrapperList<>(mockDelegate);
  }

  @Test
    @Timeout(8000)
  void indexOf_delegatesToDelegate() {
    String element = "element";
    when(mockDelegate.indexOf(element)).thenReturn(3);

    int index = list.indexOf(element);

    assertEquals(3, index);
    verify(mockDelegate).indexOf(element);
  }

  @Test
    @Timeout(8000)
  void indexOf_nullObject() {
    when(mockDelegate.indexOf(null)).thenReturn(-1);

    int index = list.indexOf(null);

    assertEquals(-1, index);
    verify(mockDelegate).indexOf(null);
  }

  @Test
    @Timeout(8000)
  void indexOf_objectNotFound() {
    Object obj = new Object();
    when(mockDelegate.indexOf(obj)).thenReturn(-1);

    int index = list.indexOf(obj);

    assertEquals(-1, index);
    verify(mockDelegate).indexOf(obj);
  }

  @Test
    @Timeout(8000)
  void indexOf_withDifferentTypeObject() {
    Integer obj = 123;
    when(mockDelegate.indexOf(obj)).thenReturn(-1);

    int index = list.indexOf(obj);

    assertEquals(-1, index);
    verify(mockDelegate).indexOf(obj);
  }

  @Test
    @Timeout(8000)
  void indexOf_reflection_delegateFieldIsSet() throws Exception {
    // Use reflection to verify that the delegate field is correctly assigned
    Field delegateField = NonNullElementWrapperList.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    @SuppressWarnings("unchecked")
    ArrayList<String> delegateValue = (ArrayList<String>) delegateField.get(list);

    assertSame(mockDelegate, delegateValue);
  }
}