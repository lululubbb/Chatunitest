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

class NonNullElementWrapperList_482_4Test {

  NonNullElementWrapperList<String> list;
  ArrayList<String> delegateMock;

  @BeforeEach
  void setUp() {
    delegateMock = mock(ArrayList.class);
    list = new NonNullElementWrapperList<>(delegateMock);
  }

  @Test
    @Timeout(8000)
  void contains_shouldReturnTrue_whenDelegateContainsObject() {
    String element = "test";
    when(delegateMock.contains(element)).thenReturn(true);

    boolean result = list.contains(element);

    assertTrue(result);
    verify(delegateMock).contains(element);
  }

  @Test
    @Timeout(8000)
  void contains_shouldReturnFalse_whenDelegateDoesNotContainObject() {
    String element = "absent";
    when(delegateMock.contains(element)).thenReturn(false);

    boolean result = list.contains(element);

    assertFalse(result);
    verify(delegateMock).contains(element);
  }

  @Test
    @Timeout(8000)
  void contains_shouldReturnFalse_whenNullPassedAndDelegateReturnsFalse() {
    when(delegateMock.contains(null)).thenReturn(false);

    boolean result = list.contains(null);

    assertFalse(result);
    verify(delegateMock).contains(null);
  }

  @Test
    @Timeout(8000)
  void contains_shouldReturnTrue_whenNullPassedAndDelegateReturnsTrue() {
    when(delegateMock.contains(null)).thenReturn(true);

    boolean result = list.contains(null);

    assertTrue(result);
    verify(delegateMock).contains(null);
  }

  @Test
    @Timeout(8000)
  void contains_reflectivelyVerifyDelegateField() throws Exception {
    Field delegateField = NonNullElementWrapperList.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    Object delegateValue = delegateField.get(list);
    assertSame(delegateMock, delegateValue);
  }
}