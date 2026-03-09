package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.util.AbstractList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;

import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NonNullElementWrapperList_488_4Test {

  private NonNullElementWrapperList<String> list;
  private ArrayList<String> delegate;

  @BeforeEach
  void setUp() {
    delegate = new ArrayList<>();
    list = new NonNullElementWrapperList<>(delegate);
  }

  @Test
    @Timeout(8000)
  void testHashCode_emptyDelegate() {
    int expectedHashCode = delegate.hashCode();
    int actualHashCode = list.hashCode();
    assertEquals(expectedHashCode, actualHashCode);
  }

  @Test
    @Timeout(8000)
  void testHashCode_nonEmptyDelegate() {
    delegate.add("a");
    delegate.add("b");
    int expectedHashCode = delegate.hashCode();
    int actualHashCode = list.hashCode();
    assertEquals(expectedHashCode, actualHashCode);
  }

  @Test
    @Timeout(8000)
  void testHashCode_reflection_modifyingDelegate() throws Exception {
    // Using reflection to replace delegate with a new ArrayList with different content
    ArrayList<String> newDelegate = new ArrayList<>();
    newDelegate.add("x");
    newDelegate.add("y");
    Field delegateField = NonNullElementWrapperList.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    delegateField.set(list, newDelegate);

    int expectedHashCode = newDelegate.hashCode();
    int actualHashCode = list.hashCode();
    assertEquals(expectedHashCode, actualHashCode);
  }
}