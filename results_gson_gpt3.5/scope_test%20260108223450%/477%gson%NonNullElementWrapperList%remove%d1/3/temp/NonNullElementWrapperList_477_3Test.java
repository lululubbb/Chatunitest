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

class NonNullElementWrapperList_477_3Test {

  NonNullElementWrapperList<String> list;
  ArrayList<String> delegate;

  @BeforeEach
  void setUp() {
    delegate = mock(ArrayList.class);
    list = new NonNullElementWrapperList<>(delegate);
  }

  @Test
    @Timeout(8000)
  void remove_shouldDelegateToDelegateRemove() {
    when(delegate.remove(0)).thenReturn("removedElement");

    String result = list.remove(0);

    verify(delegate).remove(0);
    assertEquals("removedElement", result);
  }

  @Test
    @Timeout(8000)
  void remove_shouldThrowIndexOutOfBoundsException_whenDelegateThrows() {
    when(delegate.remove(10)).thenThrow(new IndexOutOfBoundsException());

    assertThrows(IndexOutOfBoundsException.class, () -> list.remove(10));
    verify(delegate).remove(10);
  }
}