package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.util.AbstractList;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NonNullElementWrapperList_481_5Test {

  private ArrayList<String> delegate;
  private NonNullElementWrapperList<String> list;

  @BeforeEach
  void setUp() {
    delegate = spy(new ArrayList<>());
    list = new NonNullElementWrapperList<>(delegate);
  }

  @Test
    @Timeout(8000)
  void retainAll_withElementsToRetain_shouldModifyList() {
    delegate.addAll(Arrays.asList("a", "b", "c", "d"));
    Collection<String> toRetain = Arrays.asList("b", "d", "e");

    boolean changed = list.retainAll(toRetain);

    assertTrue(changed);
    assertEquals(2, list.size());
    assertTrue(list.contains("b"));
    assertTrue(list.contains("d"));
    verify(delegate).retainAll(toRetain);
  }

  @Test
    @Timeout(8000)
  void retainAll_withNoChange_shouldReturnFalse() {
    delegate.addAll(Arrays.asList("a", "b"));
    Collection<String> toRetain = Arrays.asList("a", "b", "c");

    boolean changed = list.retainAll(toRetain);

    assertFalse(changed);
    assertEquals(2, list.size());
    verify(delegate).retainAll(toRetain);
  }

  @Test
    @Timeout(8000)
  void retainAll_withEmptyCollection_shouldClearList() {
    delegate.addAll(Arrays.asList("a", "b", "c"));
    Collection<String> toRetain = Arrays.asList();

    boolean changed = list.retainAll(toRetain);

    assertTrue(changed);
    assertEquals(0, list.size());
    verify(delegate).retainAll(toRetain);
  }

  @Test
    @Timeout(8000)
  void retainAll_withNullCollection_shouldThrowNpe() {
    assertThrows(NullPointerException.class, () -> list.retainAll(null));
  }
}