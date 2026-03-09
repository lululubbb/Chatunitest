package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.util.AbstractList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class NonNullElementWrapperList_477_2Test {

  private ArrayList<String> delegate;
  private NonNullElementWrapperList<String> list;

  @BeforeEach
  void setUp() {
    delegate = Mockito.spy(new ArrayList<>());
    list = new NonNullElementWrapperList<>(delegate);
  }

  @Test
    @Timeout(8000)
  void remove_validIndex_removesAndReturnsElement() {
    delegate.add("a");
    delegate.add("b");
    delegate.add("c");

    String removed = list.remove(1);

    assertEquals("b", removed);
    assertEquals(2, list.size());
    verify(delegate).remove(1);
  }

  @Test
    @Timeout(8000)
  void remove_indexOutOfBounds_throwsException() {
    delegate.add("a");

    assertThrows(IndexOutOfBoundsException.class, () -> list.remove(5));
    verify(delegate).remove(5);
  }
}