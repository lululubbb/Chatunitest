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

class NonNullElementWrapperList_480_6Test {

  private ArrayList<String> mockDelegate;
  private NonNullElementWrapperList<String> list;

  @BeforeEach
  void setUp() {
    mockDelegate = Mockito.mock(ArrayList.class);
    list = new NonNullElementWrapperList<>(mockDelegate);
  }

  @Test
    @Timeout(8000)
  void removeAll_delegatesToDelegateAndReturnsTrue() {
    Collection<?> toRemove = Arrays.asList("a", "b");

    when(mockDelegate.removeAll(toRemove)).thenReturn(true);

    boolean result = list.removeAll(toRemove);

    verify(mockDelegate).removeAll(toRemove);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void removeAll_delegatesToDelegateAndReturnsFalse() {
    Collection<?> toRemove = Arrays.asList("x", "y", "z");

    when(mockDelegate.removeAll(toRemove)).thenReturn(false);

    boolean result = list.removeAll(toRemove);

    verify(mockDelegate).removeAll(toRemove);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void removeAll_withEmptyCollection() {
    Collection<?> toRemove = Arrays.asList();

    when(mockDelegate.removeAll(toRemove)).thenReturn(false);

    boolean result = list.removeAll(toRemove);

    verify(mockDelegate).removeAll(toRemove);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void removeAll_withNullCollection_throwsNullPointerException() {
    // Arrange: make the mock throw NullPointerException when removeAll is called with null
    when(mockDelegate.removeAll(null)).thenThrow(NullPointerException.class);

    // Act & Assert
    assertThrows(NullPointerException.class, () -> list.removeAll(null));
  }
}