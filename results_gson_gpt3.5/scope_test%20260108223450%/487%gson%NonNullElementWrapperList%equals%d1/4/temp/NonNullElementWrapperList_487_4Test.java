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

class NonNullElementWrapperList_487_4Test {

  NonNullElementWrapperList<String> list;
  ArrayList<String> delegate;

  @BeforeEach
  void setUp() {
    delegate = mock(ArrayList.class);
    list = new NonNullElementWrapperList<>(delegate);
  }

  @Test
    @Timeout(8000)
  void equals_sameDelegateObject_returnsTrue() {
    // equals() is final, cannot be stubbed with Mockito.
    // Instead, test behavior without stubbing equals.
    assertTrue(list.equals(delegate));
  }

  @Test
    @Timeout(8000)
  void equals_nullObject_returnsFalse() {
    assertFalse(list.equals(null));
  }

  @Test
    @Timeout(8000)
  void equals_differentObject_returnsFalse() {
    Object other = new Object();
    assertFalse(list.equals(other));
  }

  @Test
    @Timeout(8000)
  void equals_delegateEqualsReturnsTrue_returnsTrue() throws Exception {
    Object other = new ArrayList<String>();

    // Create spy delegate
    ArrayList<String> spyDelegate = spy(new ArrayList<>());

    // Use doReturn().when() to stub equals method to simulate desired behavior
    doReturn(true).when(spyDelegate).equals(other);

    // Create NonNullElementWrapperList with a dummy delegate
    NonNullElementWrapperList<String> listWithSpy = new NonNullElementWrapperList<>(new ArrayList<>());

    // Use reflection to set private final delegate field to spyDelegate
    Field delegateField = NonNullElementWrapperList.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    delegateField.set(listWithSpy, spyDelegate);

    assertTrue(listWithSpy.equals(other));
  }

  @Test
    @Timeout(8000)
  void equals_delegateEqualsReturnsFalse_returnsFalse() throws Exception {
    Object other = new ArrayList<String>();

    // Create spy delegate
    ArrayList<String> spyDelegate = spy(new ArrayList<>());

    // Use doReturn().when() to stub equals method to simulate desired behavior
    doReturn(false).when(spyDelegate).equals(other);

    // Create NonNullElementWrapperList with a dummy delegate
    NonNullElementWrapperList<String> listWithSpy = new NonNullElementWrapperList<>(new ArrayList<>());

    // Use reflection to set private final delegate field to spyDelegate
    Field delegateField = NonNullElementWrapperList.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    delegateField.set(listWithSpy, spyDelegate);

    assertFalse(listWithSpy.equals(other));
  }
}