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

class NonNullElementWrapperList_479_5Test {

  NonNullElementWrapperList<String> list;
  ArrayList<String> delegate;

  @BeforeEach
  void setUp() {
    delegate = Mockito.mock(ArrayList.class);
    list = new NonNullElementWrapperList<>(delegate);
  }

  @Test
    @Timeout(8000)
  void remove_existingObject_removesAndReturnsTrue() {
    String obj = "element";
    when(delegate.remove(obj)).thenReturn(true);

    boolean result = list.remove(obj);

    assertTrue(result);
    verify(delegate).remove(obj);
  }

  @Test
    @Timeout(8000)
  void remove_nonExistingObject_returnsFalse() {
    String obj = "missing";
    when(delegate.remove(obj)).thenReturn(false);

    boolean result = list.remove(obj);

    assertFalse(result);
    verify(delegate).remove(obj);
  }

  @Test
    @Timeout(8000)
  void remove_nullObject_returnsFalse() {
    Object obj = null;
    when(delegate.remove(obj)).thenReturn(false);

    boolean result = list.remove(obj);

    assertFalse(result);
    verify(delegate).remove(obj);
  }
}