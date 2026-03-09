package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.util.AbstractList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NonNullElementWrapperList_473_6Test {

  private ArrayList<String> delegateMock;
  private NonNullElementWrapperList<String> list;

  @BeforeEach
  public void setUp() {
    delegateMock = mock(ArrayList.class);
    list = new NonNullElementWrapperList<>(delegateMock);
  }

  @Test
    @Timeout(8000)
  public void size_whenDelegateHasElements_returnsDelegateSize() {
    when(delegateMock.size()).thenReturn(5);
    assertEquals(5, list.size());
  }

  @Test
    @Timeout(8000)
  public void size_whenDelegateIsEmpty_returnsZero() {
    when(delegateMock.size()).thenReturn(0);
    assertEquals(0, list.size());
  }
}