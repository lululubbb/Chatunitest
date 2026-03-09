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

class NonNullElementWrapperList_482_5Test {

  NonNullElementWrapperList<String> list;
  ArrayList<String> delegateMock;

  @BeforeEach
  void setUp() throws Exception {
    delegateMock = mock(ArrayList.class);
    // Use reflection to instantiate NonNullElementWrapperList with the mocked delegate
    list = new NonNullElementWrapperList<>(delegateMock);
  }

  @Test
    @Timeout(8000)
  void testContains_delegateReturnsTrue() {
    String element = "test";
    when(delegateMock.contains(element)).thenReturn(true);

    boolean result = list.contains(element);

    assertTrue(result);
    verify(delegateMock).contains(element);
  }

  @Test
    @Timeout(8000)
  void testContains_delegateReturnsFalse() {
    String element = "absent";
    when(delegateMock.contains(element)).thenReturn(false);

    boolean result = list.contains(element);

    assertFalse(result);
    verify(delegateMock).contains(element);
  }

  @Test
    @Timeout(8000)
  void testContains_nullElement() {
    when(delegateMock.contains(null)).thenReturn(false);

    boolean result = list.contains(null);

    assertFalse(result);
    verify(delegateMock).contains(null);
  }
}