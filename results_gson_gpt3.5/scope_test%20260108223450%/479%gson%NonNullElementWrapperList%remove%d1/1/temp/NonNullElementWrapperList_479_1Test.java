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

class NonNullElementWrapperList_479_1Test {

  NonNullElementWrapperList<String> list;
  ArrayList<String> delegate;

  @BeforeEach
  void setUp() {
    delegate = Mockito.spy(new ArrayList<>());
    list = new NonNullElementWrapperList<>(delegate);
  }

  @Test
    @Timeout(8000)
  void remove_existingElement_returnsTrue() {
    delegate.add("element");
    assertTrue(list.remove("element"));
    verify(delegate).remove("element");
  }

  @Test
    @Timeout(8000)
  void remove_nonExistingElement_returnsFalse() {
    assertFalse(list.remove("nonexistent"));
    verify(delegate).remove("nonexistent");
  }

  @Test
    @Timeout(8000)
  void remove_nullElement_delegateRemovesNull() {
    delegate.add(null);
    assertTrue(list.remove(null));
    verify(delegate).remove(null);
  }
}