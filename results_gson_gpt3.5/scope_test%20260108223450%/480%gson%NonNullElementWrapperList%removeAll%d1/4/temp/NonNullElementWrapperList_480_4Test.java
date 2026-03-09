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
import org.mockito.Mockito;

class NonNullElementWrapperList_480_4Test {

  private ArrayList<String> delegateMock;
  private NonNullElementWrapperList<String> list;

  @BeforeEach
  void setUp() {
    delegateMock = Mockito.mock(ArrayList.class);
    list = new NonNullElementWrapperList<>(delegateMock);
  }

  @Test
    @Timeout(8000)
  void removeAll_whenDelegateRemovesElements_returnsTrue() {
    Collection<String> toRemove = Arrays.asList("a", "b");
    when(delegateMock.removeAll(toRemove)).thenReturn(true);

    boolean result = list.removeAll(toRemove);

    assertTrue(result);
    verify(delegateMock).removeAll(toRemove);
  }

  @Test
    @Timeout(8000)
  void removeAll_whenDelegateRemovesNoElements_returnsFalse() {
    Collection<String> toRemove = Arrays.asList("x", "y");
    when(delegateMock.removeAll(toRemove)).thenReturn(false);

    boolean result = list.removeAll(toRemove);

    assertFalse(result);
    verify(delegateMock).removeAll(toRemove);
  }
}