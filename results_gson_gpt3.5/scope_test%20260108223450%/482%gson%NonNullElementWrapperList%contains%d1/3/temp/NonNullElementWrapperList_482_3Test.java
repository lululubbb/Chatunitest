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

class NonNullElementWrapperList_482_3Test {

  NonNullElementWrapperList<String> list;
  ArrayList<String> delegateMock;

  @BeforeEach
  void setUp() throws Exception {
    delegateMock = mock(ArrayList.class);
    list = new NonNullElementWrapperList<>(delegateMock);
  }

  @Test
    @Timeout(8000)
  void contains_delegateContainsTrue_returnsTrue() {
    String element = "test";
    when(delegateMock.contains(element)).thenReturn(true);

    boolean result = list.contains(element);

    assertTrue(result);
    verify(delegateMock).contains(element);
  }

  @Test
    @Timeout(8000)
  void contains_delegateContainsFalse_returnsFalse() {
    String element = "absent";
    when(delegateMock.contains(element)).thenReturn(false);

    boolean result = list.contains(element);

    assertFalse(result);
    verify(delegateMock).contains(element);
  }

  @Test
    @Timeout(8000)
  void contains_nullElement_delegateContains() {
    when(delegateMock.contains(null)).thenReturn(true);

    boolean result = list.contains(null);

    assertTrue(result);
    verify(delegateMock).contains(null);
  }

  @Test
    @Timeout(8000)
  void contains_privateDelegateFieldIsCorrectlySet() throws Exception {
    // Use reflection to verify private final delegate field references our mock
    Field delegateField = NonNullElementWrapperList.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    @SuppressWarnings("unchecked")
    ArrayList<String> actualDelegate = (ArrayList<String>) delegateField.get(list);
    assertSame(delegateMock, actualDelegate);
  }
}