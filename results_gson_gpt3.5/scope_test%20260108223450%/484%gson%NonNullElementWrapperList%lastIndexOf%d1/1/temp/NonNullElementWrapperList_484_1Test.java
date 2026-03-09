package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.util.AbstractList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class NonNullElementWrapperList_484_1Test {

  private ArrayList<String> delegateMock;
  private NonNullElementWrapperList<String> list;

  @BeforeEach
  void setUp() {
    delegateMock = Mockito.mock(ArrayList.class);
    list = new NonNullElementWrapperList<>(delegateMock);
  }

  @Test
    @Timeout(8000)
  void lastIndexOf_existingElement_returnsCorrectIndex() {
    when(delegateMock.lastIndexOf("test")).thenReturn(2);
    int index = list.lastIndexOf("test");
    assertEquals(2, index);
    verify(delegateMock).lastIndexOf("test");
  }

  @Test
    @Timeout(8000)
  void lastIndexOf_nonExistingElement_returnsMinusOne() {
    when(delegateMock.lastIndexOf("absent")).thenReturn(-1);
    int index = list.lastIndexOf("absent");
    assertEquals(-1, index);
    verify(delegateMock).lastIndexOf("absent");
  }

  @Test
    @Timeout(8000)
  void lastIndexOf_nullElement_returnsCorrectIndex() {
    when(delegateMock.lastIndexOf(null)).thenReturn(1);
    int index = list.lastIndexOf(null);
    assertEquals(1, index);
    verify(delegateMock).lastIndexOf(null);
  }
}