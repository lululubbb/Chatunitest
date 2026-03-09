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

class NonNullElementWrapperList_488_6Test {

  private NonNullElementWrapperList<String> list;
  private ArrayList<String> delegate;

  @BeforeEach
  void setUp() throws Exception {
    delegate = new ArrayList<>();
    list = new NonNullElementWrapperList<>(delegate);
    // Use reflection to set the private final delegate field to our delegate instance
    Field delegateField = NonNullElementWrapperList.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    delegateField.set(list, delegate);
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
  void testHashCode_delegateWithNullElements() throws Exception {
    // Although NonNullElementWrapperList should disallow nulls, test delegate with null to cover branch
    delegate.add(null);
    delegate.add("x");
    int expectedHashCode = delegate.hashCode();
    int actualHashCode = list.hashCode();
    assertEquals(expectedHashCode, actualHashCode);
  }
}