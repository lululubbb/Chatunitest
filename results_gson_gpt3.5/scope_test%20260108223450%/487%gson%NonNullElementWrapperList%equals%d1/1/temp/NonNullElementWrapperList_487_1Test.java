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

class NonNullElementWrapperList_487_1Test {

  private ArrayList<String> mockDelegate;
  private NonNullElementWrapperList<String> list;

  @BeforeEach
  void setUp() throws Exception {
    mockDelegate = spy(new ArrayList<>());
    list = new NonNullElementWrapperList<>(new ArrayList<>());
    // Inject mockDelegate into list using reflection
    java.lang.reflect.Field delegateField = NonNullElementWrapperList.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    delegateField.set(list, mockDelegate);
  }

  @Test
    @Timeout(8000)
  void equals_SameObject_ReturnsTrue() {
    // Since equals delegates to delegate.equals(o),
    // and delegate.equals(o) returns false for o == this (different object),
    // we must override delegate.equals to handle this case.
    doAnswer(invocation -> {
      Object arg = invocation.getArgument(0);
      return arg == list;
    }).when(mockDelegate).equals(any());

    assertTrue(list.equals(list));
  }

  @Test
    @Timeout(8000)
  void equals_NullObject_ReturnsFalse() {
    assertFalse(list.equals(null));
  }

  @Test
    @Timeout(8000)
  void equals_EqualDelegate_ReturnsTrue() throws Exception {
    Object other = new Object();

    java.lang.reflect.Field delegateField = NonNullElementWrapperList.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);

    class DelegateWithEquals extends ArrayList<String> {
      private final boolean equalsReturn;
      DelegateWithEquals(boolean equalsReturn) {
        this.equalsReturn = equalsReturn;
      }
      @Override
      public boolean equals(Object o) {
        return equalsReturn;
      }
    }

    DelegateWithEquals delegateWithEquals = new DelegateWithEquals(true);
    delegateField.set(list, delegateWithEquals);

    assertTrue(list.equals(other));
  }

  @Test
    @Timeout(8000)
  void equals_NotEqualDelegate_ReturnsFalse() throws Exception {
    Object other = new Object();

    java.lang.reflect.Field delegateField = NonNullElementWrapperList.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);

    class DelegateWithEquals extends ArrayList<String> {
      private final boolean equalsReturn;
      DelegateWithEquals(boolean equalsReturn) {
        this.equalsReturn = equalsReturn;
      }
      @Override
      public boolean equals(Object o) {
        return equalsReturn;
      }
    }

    DelegateWithEquals delegateWithEquals = new DelegateWithEquals(false);
    delegateField.set(list, delegateWithEquals);

    assertFalse(list.equals(other));
  }
}