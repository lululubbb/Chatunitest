package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.util.AbstractList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NonNullElementWrapperList_487_2Test {

  private ArrayList<String> mockDelegate;
  private NonNullElementWrapperList<String> list;

  @BeforeEach
  void setUp() throws Exception {
    // Create a real ArrayList and spy it instead of mocking equals (final method)
    ArrayList<String> realList = new ArrayList<>();
    mockDelegate = spy(realList);
    list = new NonNullElementWrapperList<>(new ArrayList<>());
    // Use reflection to set the private final delegate field to the spy
    Field delegateField = NonNullElementWrapperList.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    delegateField.set(list, mockDelegate);
  }

  @Test
    @Timeout(8000)
  void equals_returnsTrue_whenDelegateEqualsSameObject() {
    // Use doReturn to stub final equals method on spy
    doReturn(true).when(mockDelegate).equals(list);
    assertTrue(list.equals(list));
    verify(mockDelegate).equals(list);
  }

  @Test
    @Timeout(8000)
  void equals_returnsTrue_whenDelegateEqualsAnotherObject() {
    Object other = new Object();
    doReturn(true).when(mockDelegate).equals(other);
    assertTrue(list.equals(other));
    verify(mockDelegate).equals(other);
  }

  @Test
    @Timeout(8000)
  void equals_returnsFalse_whenDelegateEqualsReturnsFalse() {
    Object other = new Object();
    doReturn(false).when(mockDelegate).equals(other);
    assertFalse(list.equals(other));
    verify(mockDelegate).equals(other);
  }

  @Test
    @Timeout(8000)
  void equals_returnsFalse_whenDelegateEqualsNull() {
    doReturn(false).when(mockDelegate).equals(null);
    assertFalse(list.equals(null));
    verify(mockDelegate).equals(null);
  }
}