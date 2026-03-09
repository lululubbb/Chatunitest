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

class NonNullElementWrapperList_487_5Test {

  NonNullElementWrapperList<String> list;
  ArrayList<String> delegateMock;

  @BeforeEach
  void setUp() throws Exception {
    delegateMock = mock(ArrayList.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));
    list = new NonNullElementWrapperList<>(new ArrayList<>());
    // Use reflection to replace the private final delegate field with the mock
    Field delegateField = NonNullElementWrapperList.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    delegateField.set(list, delegateMock);
  }

  @Test
    @Timeout(8000)
  void equals_sameDelegateObject_returnsTrue() {
    Object other = new Object();

    // Use doReturn() to stub equals method on mock to avoid mocking final equals method
    doReturn(true).when(delegateMock).equals(other);

    boolean result = list.equals(other);

    assertTrue(result);
    verify(delegateMock).equals(other);
  }

  @Test
    @Timeout(8000)
  void equals_differentDelegateObject_returnsFalse() {
    Object other = new Object();

    doReturn(false).when(delegateMock).equals(other);

    boolean result = list.equals(other);

    assertFalse(result);
    verify(delegateMock).equals(other);
  }

  @Test
    @Timeout(8000)
  void equals_nullObject_returnsFalse() {
    doReturn(false).when(delegateMock).equals(null);

    boolean result = list.equals(null);

    assertFalse(result);
    verify(delegateMock).equals(null);
  }

  @Test
    @Timeout(8000)
  void equals_selfObject_returnsTrue() {
    doReturn(true).when(delegateMock).equals(list);

    boolean result = list.equals(list);

    assertTrue(result);
    verify(delegateMock).equals(list);
  }

  @Test
    @Timeout(8000)
  void equals_delegateEqualsThrowsException_propagates() {
    Object other = new Object();

    doThrow(new RuntimeException("fail")).when(delegateMock).equals(other);

    RuntimeException thrown = assertThrows(RuntimeException.class, () -> list.equals(other));
    assertEquals("fail", thrown.getMessage());
  }
}