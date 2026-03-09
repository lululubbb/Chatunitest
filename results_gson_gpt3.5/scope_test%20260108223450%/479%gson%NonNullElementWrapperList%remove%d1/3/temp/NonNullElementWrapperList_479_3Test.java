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

class NonNullElementWrapperList_479_3Test {

  NonNullElementWrapperList<String> list;
  ArrayList<String> delegate;

  @BeforeEach
  void setUp() {
    delegate = mock(ArrayList.class);
    list = new NonNullElementWrapperList<>(delegate);
  }

  @Test
    @Timeout(8000)
  void remove_existingElement_returnsTrue() {
    String element = "test";
    when(delegate.remove(element)).thenReturn(true);

    boolean result = list.remove(element);

    assertTrue(result);
    verify(delegate).remove(element);
  }

  @Test
    @Timeout(8000)
  void remove_nonExistingElement_returnsFalse() {
    String element = "absent";
    when(delegate.remove(element)).thenReturn(false);

    boolean result = list.remove(element);

    assertFalse(result);
    verify(delegate).remove(element);
  }

  @Test
    @Timeout(8000)
  void remove_nullElement_returnsFalse() {
    when(delegate.remove(null)).thenReturn(false);

    boolean result = list.remove(null);

    assertFalse(result);
    verify(delegate).remove(null);
  }
}