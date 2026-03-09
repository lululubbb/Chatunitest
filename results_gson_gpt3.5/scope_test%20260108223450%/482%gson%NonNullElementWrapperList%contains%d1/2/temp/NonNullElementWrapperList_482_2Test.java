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

class NonNullElementWrapperList_482_2Test {

  private NonNullElementWrapperList<String> nonNullList;
  private ArrayList<String> delegateMock;

  @BeforeEach
  void setUp() throws Exception {
    delegateMock = mock(ArrayList.class);
    nonNullList = new NonNullElementWrapperList<>(delegateMock);
  }

  @Test
    @Timeout(8000)
  void contains_delegatesToDelegate_returnsTrue() {
    when(delegateMock.contains("test")).thenReturn(true);
    assertTrue(nonNullList.contains("test"));
    verify(delegateMock).contains("test");
  }

  @Test
    @Timeout(8000)
  void contains_delegatesToDelegate_returnsFalse() {
    when(delegateMock.contains("missing")).thenReturn(false);
    assertFalse(nonNullList.contains("missing"));
    verify(delegateMock).contains("missing");
  }

  @Test
    @Timeout(8000)
  void contains_nullElement_delegatesToDelegate() {
    when(delegateMock.contains(null)).thenReturn(true);
    assertTrue(nonNullList.contains(null));
    verify(delegateMock).contains(null);
  }

  @Test
    @Timeout(8000)
  void contains_privateDelegateFieldIsNonNull() throws Exception {
    // Use reflection to check delegate field is non-null and used
    Field delegateField = NonNullElementWrapperList.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    Object delegateValue = delegateField.get(nonNullList);
    assertNotNull(delegateValue);
    assertSame(delegateMock, delegateValue);
  }
}