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

class NonNullElementWrapperList_481_4Test {

  private ArrayList<String> mockDelegate;
  private NonNullElementWrapperList<String> list;

  @BeforeEach
  void setUp() {
    mockDelegate = mock(ArrayList.class);
    list = new NonNullElementWrapperList<>(mockDelegate);
  }

  @Test
    @Timeout(8000)
  void testRetainAll_returnsTrue() {
    Collection<String> toRetain = Arrays.asList("a", "b");
    when(mockDelegate.retainAll(toRetain)).thenReturn(true);

    boolean result = list.retainAll(toRetain);

    assertTrue(result);
    verify(mockDelegate).retainAll(toRetain);
  }

  @Test
    @Timeout(8000)
  void testRetainAll_returnsFalse() {
    Collection<String> toRetain = Arrays.asList("x", "y");
    when(mockDelegate.retainAll(toRetain)).thenReturn(false);

    boolean result = list.retainAll(toRetain);

    assertFalse(result);
    verify(mockDelegate).retainAll(toRetain);
  }
}