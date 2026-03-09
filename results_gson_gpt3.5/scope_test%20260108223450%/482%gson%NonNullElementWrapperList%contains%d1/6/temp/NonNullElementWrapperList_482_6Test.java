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

class NonNullElementWrapperList_482_6Test {

  NonNullElementWrapperList<String> list;
  ArrayList<String> mockDelegate;

  @BeforeEach
  void setUp() {
    mockDelegate = mock(ArrayList.class);
    list = new NonNullElementWrapperList<>(mockDelegate);
  }

  @Test
    @Timeout(8000)
  void testContains_whenDelegateContains() {
    when(mockDelegate.contains("test")).thenReturn(true);
    assertTrue(list.contains("test"));
    verify(mockDelegate).contains("test");
  }

  @Test
    @Timeout(8000)
  void testContains_whenDelegateDoesNotContain() {
    when(mockDelegate.contains("absent")).thenReturn(false);
    assertFalse(list.contains("absent"));
    verify(mockDelegate).contains("absent");
  }

  @Test
    @Timeout(8000)
  void testContains_withNull() {
    when(mockDelegate.contains(null)).thenReturn(false);
    assertFalse(list.contains(null));
    verify(mockDelegate).contains(null);
  }
}