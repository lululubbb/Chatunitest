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

class NonNullElementWrapperList_481_3Test {

  private ArrayList<String> mockDelegate;
  private NonNullElementWrapperList<String> list;

  @BeforeEach
  void setUp() {
    mockDelegate = mock(ArrayList.class);
    list = new NonNullElementWrapperList<>(mockDelegate);
  }

  @Test
    @Timeout(8000)
  void retainAll_delegateReturnsTrue_shouldReturnTrue() {
    Collection<String> coll = Arrays.asList("a", "b");
    when(mockDelegate.retainAll(coll)).thenReturn(true);

    boolean result = list.retainAll(coll);

    assertTrue(result);
    verify(mockDelegate).retainAll(coll);
  }

  @Test
    @Timeout(8000)
  void retainAll_delegateReturnsFalse_shouldReturnFalse() {
    Collection<String> coll = Arrays.asList("x", "y");
    when(mockDelegate.retainAll(coll)).thenReturn(false);

    boolean result = list.retainAll(coll);

    assertFalse(result);
    verify(mockDelegate).retainAll(coll);
  }

  @Test
    @Timeout(8000)
  void retainAll_withEmptyCollection_shouldReturnFalse() {
    Collection<String> coll = Arrays.asList();
    when(mockDelegate.retainAll(coll)).thenReturn(false);

    boolean result = list.retainAll(coll);

    assertFalse(result);
    verify(mockDelegate).retainAll(coll);
  }

  @Test
    @Timeout(8000)
  void retainAll_withNullCollection_shouldThrowNullPointerException() throws Exception {
    // Use reflection to set delegate to a real ArrayList to get NPE on null input
    ArrayList<String> realDelegate = new ArrayList<>();
    java.lang.reflect.Field delegateField = NonNullElementWrapperList.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    delegateField.set(list, realDelegate);

    assertThrows(NullPointerException.class, () -> list.retainAll(null));
  }
}