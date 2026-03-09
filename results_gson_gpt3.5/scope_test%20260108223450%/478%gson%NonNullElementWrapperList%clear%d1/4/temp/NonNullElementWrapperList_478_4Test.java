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

class NonNullElementWrapperList_478_4Test {

  private NonNullElementWrapperList<String> list;
  private ArrayList<String> delegate;

  @BeforeEach
  void setUp() {
    delegate = spy(new ArrayList<>());
    list = new NonNullElementWrapperList<>(delegate);
  }

  @Test
    @Timeout(8000)
  void clear_delegatesToDelegateClear() {
    delegate.add("test1");
    delegate.add("test2");
    assertEquals(2, delegate.size());

    list.clear();

    verify(delegate).clear();
    assertEquals(0, delegate.size());
  }

  @Test
    @Timeout(8000)
  void clear_onEmptyDelegate() {
    assertEquals(0, delegate.size());

    list.clear();

    verify(delegate).clear();
    assertEquals(0, delegate.size());
  }

  @Test
    @Timeout(8000)
  void clear_reflectiveAccessToDelegate() throws Exception {
    // Use reflection to get the private delegate field and clear it directly
    Field delegateField = NonNullElementWrapperList.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    @SuppressWarnings("unchecked")
    ArrayList<String> reflectedDelegate = (ArrayList<String>) delegateField.get(list);

    reflectedDelegate.add("reflected");
    assertEquals(1, reflectedDelegate.size());

    list.clear();

    assertEquals(0, reflectedDelegate.size());
  }
}