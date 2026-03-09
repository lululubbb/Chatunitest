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

class NonNullElementWrapperList_481_6Test {

  private ArrayList<String> mockDelegate;
  private NonNullElementWrapperList<String> list;

  @BeforeEach
  void setUp() {
    mockDelegate = mock(ArrayList.class);
    list = new NonNullElementWrapperList<>(mockDelegate);
  }

  @Test
    @Timeout(8000)
  void retainAll_delegateReturnsTrue_returnsTrue() {
    Collection<String> c = Arrays.asList("a", "b");
    when(mockDelegate.retainAll(c)).thenReturn(true);

    boolean result = list.retainAll(c);

    verify(mockDelegate).retainAll(c);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void retainAll_delegateReturnsFalse_returnsFalse() {
    Collection<String> c = Arrays.asList("a", "b");
    when(mockDelegate.retainAll(c)).thenReturn(false);

    boolean result = list.retainAll(c);

    verify(mockDelegate).retainAll(c);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void retainAll_nullCollection_throwsNullPointerException() {
    // Use a real delegate to let it throw NPE on null
    ArrayList<String> realDelegate = new ArrayList<>();
    NonNullElementWrapperList<String> realList = new NonNullElementWrapperList<>(realDelegate);
    assertThrows(NullPointerException.class, () -> realList.retainAll(null));
  }
}