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

public class NonNullElementWrapperList_488_2Test {

  private NonNullElementWrapperList<String> list;
  private ArrayList<String> delegate;

  @BeforeEach
  public void setUp() {
    delegate = new ArrayList<>();
    list = new NonNullElementWrapperList<>(delegate);
  }

  @Test
    @Timeout(8000)
  public void testHashCode_emptyDelegate() {
    int expectedHashCode = delegate.hashCode();
    int actualHashCode = list.hashCode();
    assertEquals(expectedHashCode, actualHashCode);
  }

  @Test
    @Timeout(8000)
  public void testHashCode_nonEmptyDelegate() {
    delegate.add("one");
    delegate.add("two");
    delegate.add("three");
    int expectedHashCode = delegate.hashCode();
    int actualHashCode = list.hashCode();
    assertEquals(expectedHashCode, actualHashCode);
  }

  @Test
    @Timeout(8000)
  public void testHashCode_reflectionDelegateModification() throws Exception {
    // Use reflection to modify private final delegate field with a new ArrayList
    ArrayList<String> newDelegate = new ArrayList<>();
    newDelegate.add("alpha");
    newDelegate.add("beta");

    Field delegateField = NonNullElementWrapperList.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    delegateField.set(list, newDelegate);

    int expectedHashCode = newDelegate.hashCode();
    int actualHashCode = list.hashCode();
    assertEquals(expectedHashCode, actualHashCode);
  }
}