package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.util.AbstractList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NonNullElementWrapperList_473_3Test {

  private NonNullElementWrapperList<String> list;
  private ArrayList<String> delegate;

  @BeforeEach
  void setUp() throws Exception {
    delegate = new ArrayList<>();
    list = new NonNullElementWrapperList<>(delegate);
  }

  @Test
    @Timeout(8000)
  void testSize_emptyDelegate() {
    assertEquals(0, list.size());
  }

  @Test
    @Timeout(8000)
  void testSize_nonEmptyDelegate() throws Exception {
    delegate.add("a");
    delegate.add("b");
    delegate.add("c");
    assertEquals(3, list.size());
  }

  @Test
    @Timeout(8000)
  void testSize_reflectDelegateModification() throws Exception {
    // Using reflection to modify private final delegate field to a new ArrayList with elements
    ArrayList<String> newDelegate = new ArrayList<>();
    newDelegate.add("x");
    newDelegate.add("y");
    Field delegateField = NonNullElementWrapperList.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    delegateField.set(list, newDelegate);

    assertEquals(2, list.size());
  }
}