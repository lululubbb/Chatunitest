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

class NonNullElementWrapperList_483_4Test {

  private ArrayList<String> delegate;
  private NonNullElementWrapperList<String> list;

  @BeforeEach
  void setUp() {
    delegate = mock(ArrayList.class);
    list = new NonNullElementWrapperList<>(delegate);
  }

  @Test
    @Timeout(8000)
  void indexOf_existingElement_returnsCorrectIndex() {
    String element = "element";
    when(delegate.indexOf(element)).thenReturn(2);
    int index = list.indexOf(element);
    assertEquals(2, index);
  }

  @Test
    @Timeout(8000)
  void indexOf_nonExistingElement_returnsMinusOne() {
    String element = "missing";
    when(delegate.indexOf(element)).thenReturn(-1);
    int index = list.indexOf(element);
    assertEquals(-1, index);
  }

  @Test
    @Timeout(8000)
  void indexOf_nullElement_returnsIndex() {
    when(delegate.indexOf(null)).thenReturn(1);
    int index = list.indexOf(null);
    assertEquals(1, index);
  }
}