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

class NonNullElementWrapperList_484_2Test {

  private ArrayList<String> delegate;
  private NonNullElementWrapperList<String> list;

  @BeforeEach
  void setUp() {
    delegate = mock(ArrayList.class);
    list = new NonNullElementWrapperList<>(delegate);
  }

  @Test
    @Timeout(8000)
  void lastIndexOf_existingElement_returnsCorrectIndex() {
    when(delegate.lastIndexOf("test")).thenReturn(3);
    int index = list.lastIndexOf("test");
    assertEquals(3, index);
  }

  @Test
    @Timeout(8000)
  void lastIndexOf_nonExistingElement_returnsMinusOne() {
    when(delegate.lastIndexOf("absent")).thenReturn(-1);
    int index = list.lastIndexOf("absent");
    assertEquals(-1, index);
  }

  @Test
    @Timeout(8000)
  void lastIndexOf_nullElement_returnsCorrectIndex() {
    when(delegate.lastIndexOf(null)).thenReturn(2);
    int index = list.lastIndexOf(null);
    assertEquals(2, index);
  }
}