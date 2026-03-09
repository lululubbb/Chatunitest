package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.util.AbstractList;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NonNullElementWrapperList_480_2Test {

  private ArrayList<String> mockDelegate;
  private NonNullElementWrapperList<String> list;

  @BeforeEach
  void setUp() {
    mockDelegate = mock(ArrayList.class);
    list = new NonNullElementWrapperList<>(mockDelegate);
  }

  @Test
    @Timeout(8000)
  void removeAll_delegatesCallAndReturnsTrue() {
    Collection<String> toRemove = Arrays.asList("a", "b", "c");
    when(mockDelegate.removeAll(toRemove)).thenReturn(true);

    boolean result = list.removeAll(toRemove);

    verify(mockDelegate).removeAll(toRemove);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void removeAll_delegatesCallAndReturnsFalse() {
    Collection<String> toRemove = Arrays.asList("x", "y");
    when(mockDelegate.removeAll(toRemove)).thenReturn(false);

    boolean result = list.removeAll(toRemove);

    verify(mockDelegate).removeAll(toRemove);
    assertFalse(result);
  }
}