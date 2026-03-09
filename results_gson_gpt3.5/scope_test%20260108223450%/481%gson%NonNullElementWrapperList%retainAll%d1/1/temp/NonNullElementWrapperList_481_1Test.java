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

class NonNullElementWrapperList_481_1Test {

  private ArrayList<String> delegateMock;
  private NonNullElementWrapperList<String> list;

  @BeforeEach
  void setUp() {
    delegateMock = mock(ArrayList.class);
    list = new NonNullElementWrapperList<>(delegateMock);
  }

  @Test
    @Timeout(8000)
  void retainAll_returnsTrue_whenDelegateReturnsTrue() {
    Collection<String> collection = Arrays.asList("a", "b");
    when(delegateMock.retainAll(collection)).thenReturn(true);

    boolean result = list.retainAll(collection);

    assertTrue(result);
    verify(delegateMock).retainAll(collection);
  }

  @Test
    @Timeout(8000)
  void retainAll_returnsFalse_whenDelegateReturnsFalse() {
    Collection<String> collection = Arrays.asList("x", "y");
    when(delegateMock.retainAll(collection)).thenReturn(false);

    boolean result = list.retainAll(collection);

    assertFalse(result);
    verify(delegateMock).retainAll(collection);
  }

  @Test
    @Timeout(8000)
  void retainAll_withEmptyCollection() {
    Collection<String> collection = Arrays.asList();
    when(delegateMock.retainAll(collection)).thenReturn(false);

    boolean result = list.retainAll(collection);

    assertFalse(result);
    verify(delegateMock).retainAll(collection);
  }

  @Test
    @Timeout(8000)
  void retainAll_withNullCollection_shouldThrowNullPointerException() {
    // We create a spy to throw NPE on null argument for retainAll
    ArrayList<String> spyDelegate = spy(new ArrayList<>());
    list = new NonNullElementWrapperList<>(spyDelegate);

    assertThrows(NullPointerException.class, () -> list.retainAll(null));
    // Do not verifyNoInteractions because delegate.retainAll(null) is actually called before NPE
  }
}