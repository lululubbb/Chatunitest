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

class NonNullElementWrapperList_488_1Test {

  private NonNullElementWrapperList<String> list;
  private ArrayList<String> delegate;

  @BeforeEach
  void setUp() {
    delegate = new ArrayList<>();
    list = new NonNullElementWrapperList<>(delegate);
  }

  @Test
    @Timeout(8000)
  void testHashCode_emptyDelegate() throws Exception {
    // delegate is empty
    int expectedHashCode = delegate.hashCode();

    // Use reflection to invoke private final delegate field to check internal state (optional)
    Field delegateField = NonNullElementWrapperList.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    @SuppressWarnings("unchecked")
    ArrayList<String> internalDelegate = (ArrayList<String>) delegateField.get(list);
    assertEquals(delegate, internalDelegate);

    int actualHashCode = list.hashCode();
    assertEquals(expectedHashCode, actualHashCode);
  }

  @Test
    @Timeout(8000)
  void testHashCode_nonEmptyDelegate() {
    delegate.add("one");
    delegate.add("two");
    int expectedHashCode = delegate.hashCode();

    int actualHashCode = list.hashCode();
    assertEquals(expectedHashCode, actualHashCode);
  }
}