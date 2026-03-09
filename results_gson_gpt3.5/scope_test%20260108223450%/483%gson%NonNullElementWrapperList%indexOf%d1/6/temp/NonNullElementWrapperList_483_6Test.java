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

class NonNullElementWrapperList_483_6Test {

  NonNullElementWrapperList<String> nonNullList;
  ArrayList<String> mockDelegate;

  @BeforeEach
  void setUp() {
    mockDelegate = mock(ArrayList.class);
    nonNullList = new NonNullElementWrapperList<>(mockDelegate);
  }

  @Test
    @Timeout(8000)
  void indexOf_delegatesToDelegateList_whenObjectIsPresent() {
    String obj = "test";
    when(mockDelegate.indexOf(obj)).thenReturn(2);

    int index = nonNullList.indexOf(obj);

    assertEquals(2, index);
    verify(mockDelegate).indexOf(obj);
  }

  @Test
    @Timeout(8000)
  void indexOf_delegatesToDelegateList_whenObjectIsNotPresent() {
    String obj = "absent";
    when(mockDelegate.indexOf(obj)).thenReturn(-1);

    int index = nonNullList.indexOf(obj);

    assertEquals(-1, index);
    verify(mockDelegate).indexOf(obj);
  }

  @Test
    @Timeout(8000)
  void indexOf_delegatesToDelegateList_withNullObject() {
    when(mockDelegate.indexOf(null)).thenReturn(-1);

    int index = nonNullList.indexOf(null);

    assertEquals(-1, index);
    verify(mockDelegate).indexOf(null);
  }

  @Test
    @Timeout(8000)
  void indexOf_reflectivelyInvokes_indexOf() throws Exception {
    // Use reflection to invoke private delegate field and call indexOf
    Field delegateField = NonNullElementWrapperList.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    delegateField.set(nonNullList, mockDelegate);

    when(mockDelegate.indexOf("reflect")).thenReturn(5);

    // invoke indexOf via reflection
    int result = (int) NonNullElementWrapperList.class.getDeclaredMethod("indexOf", Object.class)
        .invoke(nonNullList, "reflect");

    assertEquals(5, result);
    verify(mockDelegate).indexOf("reflect");
  }
}