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
import org.mockito.Mockito;

class NonNullElementWrapperList_480_1Test {

  private ArrayList<String> delegate;
  private NonNullElementWrapperList<String> list;

  @BeforeEach
  void setUp() {
    delegate = Mockito.spy(new ArrayList<>());
    list = new NonNullElementWrapperList<>(delegate);
  }

  @Test
    @Timeout(8000)
  void removeAll_whenCollectionIsEmpty_shouldReturnFalseAndNotModifyList() {
    delegate.addAll(Arrays.asList("a", "b", "c"));
    Collection<String> toRemove = Arrays.asList();

    boolean result = list.removeAll(toRemove);

    assertFalse(result);
    assertEquals(3, list.size());
    verify(delegate).removeAll(toRemove);
  }

  @Test
    @Timeout(8000)
  void removeAll_whenCollectionContainsElements_shouldRemoveAndReturnTrue() {
    delegate.addAll(Arrays.asList("a", "b", "c", "d"));
    Collection<String> toRemove = Arrays.asList("b", "d");

    boolean result = list.removeAll(toRemove);

    assertTrue(result);
    assertEquals(2, list.size());
    assertFalse(list.contains("b"));
    assertFalse(list.contains("d"));
    verify(delegate).removeAll(toRemove);
  }

  @Test
    @Timeout(8000)
  void removeAll_whenCollectionContainsNonExistingElements_shouldReturnFalse() {
    delegate.addAll(Arrays.asList("a", "b", "c"));
    Collection<String> toRemove = Arrays.asList("x", "y");

    boolean result = list.removeAll(toRemove);

    assertFalse(result);
    assertEquals(3, list.size());
    verify(delegate).removeAll(toRemove);
  }

  @Test
    @Timeout(8000)
  void removeAll_withNullCollection_shouldThrowNullPointerException() {
    assertThrows(NullPointerException.class, () -> list.removeAll(null));
  }
}